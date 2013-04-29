package com.deepred.zhaolin.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.R;
import com.deepred.zhaolin.ZhaolinApplication;
import com.deepred.zhaolin.entity.DetailEntity;
import com.deepred.zhaolin.utils.CollectionActivity;
import com.deepred.zhaolin.utils.PostAndGetResponse;
import com.deepred.zhaolin.utils.SavedApp;
import com.deepred.zhaolin.utils.ZhaolinConstants;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.friends.FriendsGetRequestParam;
import com.renren.api.connect.android.friends.FriendsGetResponseBean;

@SuppressWarnings("deprecation")
public class MainActivity extends BaseActivity {
	Context context = null;
	LocalActivityManager manager = null;
	ViewPager pager = null;
	TabHost tabHost = null;
	TextView t1,t2,t3;
	public List<DetailEntity> list = new ArrayList<DetailEntity>();
	private int offset = 0;
	private int currIndex = 0; 
	private int bmpW;
	private ImageView cursor; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		updateLocation();
    	Intent intent = new Intent(this, NewShareActivity.class);
    	
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);		
		final Action menuAction = new IntentAction(this, intent, R.drawable.ic_title_export_default);
		
        actionBar.addAction(menuAction);
		context = MainActivity.this;
		manager = new LocalActivityManager(this , true);
		manager.dispatchCreate(savedInstanceState);
		this.list = convertToDetail(SplashActivity.sdao.list());
		InitImageView();
		initTextView();
		initPagerViewer();
	}
	
	public static List<DetailEntity> convertToDetail(List<SavedApp> list){
		List<DetailEntity> lde = new ArrayList<DetailEntity>();
		for (SavedApp sa : list){
			DetailEntity de = new DetailEntity();
			de.setLayoutID(R.layout.blueadapter);
			de.setTitle(sa.getAppname());
			de.setImageUrl(sa.getIcon());		
			de.setUser("xxx");
			lde.add(de);
		}
		return lde;
	}

	private void initTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));		
	}

	/**
	 * 
	 */
	private void initPagerViewer() {
		pager = (ViewPager) findViewById(R.id.viewpage);
		final ArrayList<View> list = new ArrayList<View>();

		Intent intent1 = new Intent(context, GetNearbyPeopleActivity.class);
		list.add(getView("A", intent1));
		
		Intent intent2 = new Intent(context, GetNewsFeedsActivity.class);
		list.add(getView("B", intent2));
		
		Intent intent3 = new Intent(context, CollectionActivity.class);
		list.add(getView("C", intent3));

		pager.setAdapter(new MyPagerAdapter(list));
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
		t1.setTextColor(Color.parseColor("#33CCCC"));
	}
	
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.roller)
		.getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels; 
		offset = (screenW / 3 - bmpW) / 2; 
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/**
	 * @param id
	 * @param intent
	 * @return
	 */
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}
	
	/**
	 * Pager
	 */
	public class MyPagerAdapter extends PagerAdapter{
		List<View> list =  new ArrayList<View>();
		public MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}
		
		@Override
		public int getItemPosition(Object object) {
		    return POSITION_NONE;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}
	/**
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + bmpW;// 
		int two = one * 2;// 
		
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				
				t1.setTextColor(Color.parseColor("#33CCCC"));
				t2.setTextColor(Color.BLACK);
				t3.setTextColor(Color.BLACK);
				CollectionActivity.refresh();
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);	
				}
				t2.setTextColor(Color.parseColor("#33CCCC"));
				t1.setTextColor(Color.BLACK);
				t3.setTextColor(Color.BLACK);
				CollectionActivity.refresh();
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				t3.setTextColor(Color.parseColor("#33CCCC"));
				t2.setTextColor(Color.BLACK);
				t1.setTextColor(Color.BLACK);
				CollectionActivity.refresh();
				break;
			}
			currIndex = arg0;		
			animation.setFillAfter(true);// True
			animation.setDuration(200);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;
		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			pager.setCurrentItem(index);
		}
	};
	
	private void updateLocation(){
		if (renren != null) {
			AsyncRenren asyncRenren = new AsyncRenren(renren);
			FriendsGetRequestParam param = new FriendsGetRequestParam();
			AbstractRequestListener<FriendsGetResponseBean> listener = new AbstractRequestListener<FriendsGetResponseBean>() {

				@Override
				public void onComplete(final FriendsGetResponseBean bean) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							long[] friendidList = bean.getUids();
							String tmp = arrayToString(friendidList);
							String message = "{\"primkey\":" +userPrimkey+ ", \"location\":\"" +
									ZhaolinApplication.location + "\",\"friendidList\" : \"" +tmp +"\"}";
					        //showProgress();
					        new PostAndGetResponse().execute(ZhaolinConstants.updateLocationUrl, message);
						}
					});
				}

				@Override
				public void onRenrenError(final RenrenError renrenError) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showTip("renren error");
						}
					});
				}

				@Override
				public void onFault(final Throwable fault) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showTip("renren fault");
						}
					});
				}
			};
			asyncRenren.getFriends(param, listener);
        }
	}
	
	private String arrayToString(long[] friendidList) {
		String result="";
		
		for(long item: friendidList){
			result += item + ",";
		}
		return result;
	}
	
	public static Intent createIntent(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}
