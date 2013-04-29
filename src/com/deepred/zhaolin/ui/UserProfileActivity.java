package com.deepred.zhaolin.ui;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.R;
import com.deepred.zhaolin.adapter.UserNewsFeedAdapter;
import com.deepred.zhaolin.entity.NewActionInfo;
import com.deepred.zhaolin.entity.UserInfo;
import com.deepred.zhaolin.utils.FileCache;
import com.deepred.zhaolin.utils.GetUserNewsFeedsTask;
import com.deepred.zhaolin.utils.LoadImageTask;

public class UserProfileActivity extends BaseActivity {
	private List<NewActionInfo> actionInfoList = new ArrayList<NewActionInfo>();
	private String message = "";
	private UserNewsFeedAdapter newsFeedAdapter;
	private ListView dragListView;
	private UserInfo userInfo;
	private View view;
	class ViewHolder{
		ImageView userIcon;
		TextView userName;
		TextView collectInfo;
		ImageView follow;
		TextView userStatus;
	}
	ViewHolder holder;
	Drawable drawable;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = this.getLayoutInflater().inflate(R.layout.user_profile, null);
		setContentView(view);
		drawable = this.getResources().getDrawable(R.drawable.icon_wait);
		
		holder = new ViewHolder();
		holder.userIcon = (ImageView) view.findViewById(R.id.userIcon);
		holder.userName = (TextView) view.findViewById(R.id.userName);
		holder.collectInfo = (TextView) view.findViewById(R.id.starCount);
		holder.follow = (ImageView) view.findViewById(R.id.followIcon);
		holder.userStatus = (TextView) view.findViewById(R.id.userStatus);
		
		userInfo = (UserInfo) this.getIntent().getSerializableExtra("UserInfo");
		dragListView = (ListView) UserProfileActivity.this.findViewById(R.id._commentList);
		newsFeedAdapter = new UserNewsFeedAdapter(UserProfileActivity.this, R.layout.user_newsfeed_item, actionInfoList);
		dragListView.setAdapter(newsFeedAdapter);
		
		getNewsFeeds();
	}
	
	private void getNewsFeeds(){
		try {
			holder.userIcon.setImageURI(FileCache.getImageURI(userInfo.headUrl, BaseActivity.cache));
		} catch (Exception e) {
			holder.userIcon.setImageDrawable(drawable);
			asyncLoadImage(holder.userIcon, userInfo.headUrl);
		}
		holder.userName.setText(userInfo.userName);
		holder.collectInfo.setText("收录数: "+userInfo.collectCount);
		if(userInfo.status!=null && userInfo.status.length()>0){
			holder.userStatus.setText(userInfo.status);
		}
		message = "{\"userPrimKey\" :" + userInfo.userPrimkey + "}";
        showProgress();
        new GetUserNewsFeedsTask(message, UserProfileActivity.this, 
				newsFeedAdapter, 
				actionInfoList).start();
	}
	
	private void asyncLoadImage(ImageView iv, String url){
		new LoadImageTask(iv, url).execute();
	}
}
