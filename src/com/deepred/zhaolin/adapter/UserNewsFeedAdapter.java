package com.deepred.zhaolin.adapter;

import java.util.List;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.R;
import com.deepred.zhaolin.entity.MyAppEntity;
import com.deepred.zhaolin.entity.NewActionInfo;
import com.deepred.zhaolin.ui.PostNewActionActivity;
import com.deepred.zhaolin.utils.FileCache;
import com.deepred.zhaolin.utils.LoadImageTask;
import com.deepred.zhaolin.utils.ZhaolinConstants;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class UserNewsFeedAdapter extends ArrayAdapter<NewActionInfo> implements ListAdapter{

	private List<NewActionInfo> list;
	private LayoutInflater layoutInflater;
	private Drawable drawable;
	private Context context;
	public UserNewsFeedAdapter(Context context, int textViewResourceId,
			List<NewActionInfo> objects) {
		super(context, textViewResourceId, objects);
		this.list = objects;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		drawable = context.getResources().getDrawable(R.drawable.icon_wait);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public NewActionInfo getItem(int position){
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private void asyncLoadImage(ImageView iv, String url){
		new LoadImageTask(iv, url).execute();
	}

	class ViewHolder {
		public ImageView appIcon;
		public TextView appName;
		public TextView actionContent;
	}
	
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		NewActionInfo actionDetail = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.user_newsfeed_item, null);
			holder.appIcon = (ImageView) convertView.findViewById(R.id.appIcon);
			holder.appName = (TextView) convertView.findViewById(R.id.appName);
			holder.actionContent = (TextView) convertView.findViewById(R.id.actionContent);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		try {
			holder.appIcon.setImageURI(FileCache.getImageURI(actionDetail.appInfo.icon_url, BaseActivity.cache));
		} catch (Exception e) {
			holder.appIcon.setImageDrawable(drawable);
			asyncLoadImage(holder.appIcon, actionDetail.appInfo.icon_url);
		}
		holder.appName.setText(actionDetail.appInfo.appname);
		final MyAppEntity appEntity = new MyAppEntity();
		appEntity.setTitle(actionDetail.appInfo.appname);
		appEntity.setImgByte(drawable);
		holder.actionContent.setClickable(true);
		holder.actionContent.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PostNewActionActivity.class);
				intent.putExtra(ZhaolinConstants.APP_ENTITY, appEntity);
				context.startActivity(intent);
			}
		});

		String comment;
		/*
		 * actionType:
		 *  1: 顶, red text
		 *  0: 中立
		 * -1: 踩, green text
		 */
		if(actionDetail.actionType==-1){
			comment = "分享: ";
			holder.actionContent.setTextColor(Color.DKGRAY);
		}
		else if(actionDetail.actionType==0){
			comment = "顶: ";
			holder.actionContent.setTextColor(Color.rgb(200, 0, 0));
		}
		else{
			comment = "踩: ";
			holder.actionContent.setTextColor(Color.rgb(0, 150, 0));
		}
		comment += actionDetail.comment;
		holder.actionContent.setText(comment);		 
		return convertView;
	}
}
