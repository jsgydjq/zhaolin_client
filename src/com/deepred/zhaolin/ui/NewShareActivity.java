package com.deepred.zhaolin.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.R;
import com.deepred.zhaolin.entity.MyAppEntity;
import com.deepred.zhaolin.utils.ZhaolinConstants;

/**
 * @author Administrator
 */
public class NewShareActivity extends BaseActivity {
	List<MyAppEntity> list = new ArrayList<MyAppEntity>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.my_share);
		final PackageManager pm = getPackageManager();
		List<ApplicationInfo> packages = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);

		for (ApplicationInfo packageInfo : packages) {
			if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				MyAppEntity appEntity = new MyAppEntity();
				try {
					// get app version
					PackageInfo pk = pm.getPackageInfo(packageInfo.packageName,
							0);
					appEntity.setText("v" + pk.versionName);

				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				// get pak name
				String name = pm.getApplicationLabel(packageInfo).toString();
				appEntity.setTitle(name);
				// get pak icon
				Drawable icon = pm.getApplicationIcon(packageInfo);
				appEntity.setImgByte(icon);
				// get package size
				File f = new File(packageInfo.sourceDir);
				appEntity.setSize(f.length());
				list.add(appEntity);
			}
		}
		
		final ListView listView = (ListView) this.findViewById(R.id.appItem_listView);
		MyAdapter adapter = new MyAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setClickable(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MyAppEntity appEntity = (MyAppEntity) listView.getItemAtPosition(arg2);

				Intent intent = new Intent(NewShareActivity.this, NewActionActivity.class);
				intent.putExtra(ZhaolinConstants.APP_ENTITY, appEntity);				
				NewShareActivity.this.startActivity(intent);
			}
		});
	}
	/**
	 * @author Administrator
	 */
	class MyAdapter implements ListAdapter {

		private List<MyAppEntity> list;

		/** XML布局 */
		private LayoutInflater layoutInflater;

		public MyAdapter(Context context, List<MyAppEntity> list) {
			this.list = list;
			layoutInflater = LayoutInflater.from(context);
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}

		/**
		 * 控制ITEM布局
		 */
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				// 加载布局
				convertView = layoutInflater.inflate(R.layout.myadapter, null);
				ImageView iv = (ImageView) convertView.findViewById(R.id.appIcon);
				iv.setImageBitmap(list.get(position).getImgBitmap());

				TextView tv_1 = (TextView) convertView.findViewById(R.id.appName);
				tv_1.setText(list.get(position).getTitle());

				TextView tv_2 = (TextView) convertView.findViewById(R.id.appVersion);
				tv_2.setText(list.get(position).getText());
			}
			return convertView;
		}

		@Override
		public int getItemViewType(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {
			return list.size();
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		/**
		 * 是否ITEM 监听
		 */
		@Override
		public boolean isEmpty() {
			return true;
		}

		/**
		 * true 所有项目可选择可点击
		 */
		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		/**
		 *  是否现实分割线
		 */
		@Override
		public boolean isEnabled(int position) {
			return true;
		}
	}
}