package com.deepred.zhaolin.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.R;
import com.deepred.zhaolin.adapter.NewsFeedAdapter;
import com.deepred.zhaolin.entity.NewActionInfo;
import com.deepred.zhaolin.utils.GetNewsFeedsTask;
import com.deepred.zhaolin.utils.ZhaolinConstants;
import com.deepred.zhaolin.widget.DragListView;

public class GetNewsFeedsActivity extends BaseActivity implements
	DragListView.OnRefreshLoadingMoreListener{
	private List<NewActionInfo> actionInfoList = new ArrayList<NewActionInfo>();
	private DragListView dragListView;	
	private int CURRENT_INDEX = 1;
	private String message = "";
	private NewsFeedAdapter newsFeedAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.friend_apps);

		dragListView = (DragListView) GetNewsFeedsActivity.this.findViewById(R.id.listView_friend_apps);
		newsFeedAdapter = new NewsFeedAdapter(GetNewsFeedsActivity.this, R.layout.newsfeed_item, actionInfoList);

		dragListView.setAdapter(newsFeedAdapter);
		dragListView.setOnRefreshListener(GetNewsFeedsActivity.this);

		getNewsFeeds();
	}

	@Override
	public void onPause(){
		super.onRestart();
		showTip("GetFriendApps paused!");
	}

	@Override
	public void onResume(){
		super.onResume();
		showTip("GetFriendApps resumed!");
	}

	@Override
	public void onRefresh() {
		new GetNewsFeedsTask(message, CURRENT_INDEX++, ZhaolinConstants.DRAG_TAG, GetNewsFeedsActivity.this, 
				dragListView, newsFeedAdapter, 
				actionInfoList).start();
	}
	
	@Override
	public void onLoadMore() {
		new GetNewsFeedsTask(message, CURRENT_INDEX++, ZhaolinConstants.LOADMORE_TAG, GetNewsFeedsActivity.this, 
				dragListView, newsFeedAdapter, 
				actionInfoList).start();
	}

	private void getNewsFeeds(){
		message = "{\"userPrimKey\" :" + userPrimkey + "}";
        showProgress();
        new GetNewsFeedsTask(message, CURRENT_INDEX++, ZhaolinConstants.DRAG_TAG, GetNewsFeedsActivity.this, 
				dragListView, newsFeedAdapter, 
				actionInfoList).start();
	}
}