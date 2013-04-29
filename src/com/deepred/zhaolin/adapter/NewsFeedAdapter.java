package com.deepred.zhaolin.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.deepred.zhaolin.R;
import com.deepred.zhaolin.entity.MyAppEntity;
import com.deepred.zhaolin.entity.NewActionInfo;
import com.deepred.zhaolin.ui.NewActionActivity;
import com.deepred.zhaolin.ui.UserProfileActivity;
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

public class NewsFeedAdapter extends ArrayAdapter<NewActionInfo> implements ListAdapter{

	private List<NewActionInfo> list;
	private LayoutInflater layoutInflater;
	private Drawable drawable;
	private Context context;
	public NewsFeedAdapter(Context context, int textViewResourceId,
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

	static class ViewHolder {
		public ImageView userIcon;
		public TextView userName;
		public ImageView appIcon;
		public TextView appName;
		public TextView actionContent;
		public TextView commentCount;
		public ImageView upComment;
		public ImageView downComment;
		public TextView timeStamp;
	}
	
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		final NewActionInfo actionDetail = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.newsfeed_item, null);
			holder.userIcon = (ImageView) convertView.findViewById(R.id.userIcon);
			holder.userName = (TextView) convertView.findViewById(R.id.userName);
			holder.appIcon = (ImageView) convertView.findViewById(R.id.appIcon);
			holder.appName = (TextView) convertView.findViewById(R.id.appName);
			holder.actionContent = (TextView) convertView.findViewById(R.id.actionContent);
			holder.commentCount = (TextView) convertView.findViewById(R.id.commentCount);
			holder.upComment = (ImageView) convertView.findViewById(R.id.upComment);
			holder.downComment = (ImageView) convertView.findViewById(R.id.downComment);
			holder.timeStamp = (TextView) convertView.findViewById(R.id.timeStamp);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		asyncLoadImage(holder.userIcon, actionDetail.userInfo.headUrl);
		asyncLoadImage(holder.appIcon, actionDetail.appInfo.icon_url);
		
		Date date = new Date(Long.parseLong(actionDetail.timestamp));
		holder.timeStamp.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
		final MyAppEntity appEntity = new MyAppEntity();
		appEntity.setTitle(actionDetail.appInfo.appname);
		appEntity.setImgByte(drawable);
		holder.actionContent.setClickable(true);
		holder.actionContent.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, NewActionActivity.class);
				intent.putExtra(ZhaolinConstants.APP_ENTITY, appEntity);
				context.startActivity(intent);
			}
		});
		holder.userIcon.setClickable(true);
		holder.userIcon.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserProfileActivity.class);
				intent.putExtra("UserInfo", actionDetail.userInfo);
				context.startActivity(intent);
			}
		});
		
		holder.userName.setText(actionDetail.userInfo.userName);
		holder.appName.setText(actionDetail.appInfo.appname);
		holder.commentCount.setText("评论数: " + actionDetail.appInfo.commentCount);
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
		
		/*new AsyncTask<Object, String, String>(){

			ViewHolder holder;
			NewActionInfo actionDetail;
			@Override
			protected String doInBackground(Object... arg0) {
				holder = (ViewHolder) arg0[0];
				actionDetail = (NewActionInfo) arg0[1];
				try {
					holder.userIcon.setImageURI(FileCache.getImageURI(actionDetail.userInfo.headUrl, BaseActivity.cache));
				} catch (Exception e) {
					holder.userIcon.setImageDrawable(drawable);
					
				}
				try {
					holder.appIcon.setImageURI(FileCache.getImageURI(actionDetail.appInfo.icon_url, BaseActivity.cache));
				} catch (Exception e) {
					holder.appIcon.setImageDrawable(drawable);
					
				}
				
				
				
				Date date = new Date(Long.parseLong(actionDetail.timestamp));
				holder.timeStamp.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
				final MyAppEntity appEntity = new MyAppEntity();
				appEntity.setTitle(actionDetail.appInfo.appname);
				appEntity.setImgByte(drawable);
				holder.actionContent.setClickable(true);
				holder.actionContent.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, NewActionActivity.class);
						intent.putExtra(ZhaolinConstants.APP_ENTITY, appEntity);
						context.startActivity(intent);
					}
				});
				holder.userIcon.setClickable(true);
				holder.userIcon.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, UserProfileActivity.class);
						intent.putExtra("UserInfo", actionDetail.userInfo);
						context.startActivity(intent);
					}
				});
				
				return null;
			}
			
			 @Override
			    protected void onPostExecute(String result) {
			        super.onPostExecute(result);
			        
			 }
		}.execute(holder, actionDetail);*/
			 
		return convertView;
	}

}
