package com.deepred.zhaolin.adapter;

import java.util.List;

import com.deepred.zhaolin.R;
import com.deepred.zhaolin.entity.NearbyEntity;
import com.deepred.zhaolin.entity.UserInfoDist;
import com.deepred.zhaolin.utils.LoadImageTask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NearbyAdapter extends ArrayAdapter<NearbyEntity> implements ListAdapter {
	private List<NearbyEntity> list;
	private LayoutInflater layoutInflater;
	private Context context;
	public NearbyAdapter(Context context, int resourceId, List<NearbyEntity> list) {
		super(context, resourceId, list);
		this.list = list;	
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public NearbyEntity getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private void asyncLoadImage(ImageView iv, String url){
		new LoadImageTask(iv, url).execute();
	}
	
	static class ViewHolder{
		ImageView userIcon;
		TextView userName;
		TextView collectInfo;
		ImageView follow;
		TextView userStatus;
		ListView actionList;
	}
	
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.nearby_item, null);
			holder = new ViewHolder();
			holder.userIcon = (ImageView) convertView.findViewById(R.id.userIcon);
			holder.userName = (TextView) convertView.findViewById(R.id.userName);
			holder.collectInfo = (TextView) convertView.findViewById(R.id.starCount);
			holder.follow = (ImageView) convertView.findViewById(R.id.followIcon);
			holder.userStatus = (TextView) convertView.findViewById(R.id.userStatus);
			holder.actionList = (ListView) convertView.findViewById(R.id._commentList);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		final NearbyEntity entity = list.get(position);
		UserInfoDist userInfo = entity.userInfo;
		asyncLoadImage(holder.userIcon, userInfo.headUrl);
		holder.userName.setText(userInfo.userName);
		holder.collectInfo.setText("收录数: "+userInfo.collectCount);
		if(userInfo.status!=null && userInfo.status.length()>0){
			holder.userStatus.setText(userInfo.status);
		}
		UserNewsFeedAdapter newsFeedAdapter = new UserNewsFeedAdapter(context, R.layout.user_newsfeed_item, entity.actionList);
		holder.actionList.setAdapter(newsFeedAdapter);
		return convertView;
	}
	
	@Override
	public int getItemViewType(int position) {
		return position;
	}
	@Override
	public int getViewTypeCount() {
		return list.size()>1?list.size():1;
	}
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}
}