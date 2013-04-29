package com.deepred.zhaolin.utils;

import java.util.List;

import android.annotation.SuppressLint;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.adapter.NewsFeedAdapter;
import com.deepred.zhaolin.entity.NewActionInfo;
import com.deepred.zhaolin.entity.NewActionInfoListResponse;
import com.deepred.zhaolin.widget.DragListView;
import com.google.gson.Gson;

public class GetNewsFeedsTask extends PostAndGetResponse {
	
	private int index;
	DragListView dragListView;
	NewsFeedAdapter newsFeedAdapter;
	List<NewActionInfo> actionInfolList;
	public GetNewsFeedsTask(String message, int whichPage, int index, 
			BaseActivity activity, DragListView dragListView, 
			NewsFeedAdapter newsFeedAdapter, 
			List<NewActionInfo> actionDetailList) {
		super(activity);
		this.index = index;
		this.dragListView = dragListView;
		this.newsFeedAdapter = newsFeedAdapter;
		this.actionInfolList = actionDetailList;
		url = ZhaolinConstants.getActionBriefList+whichPage;
		this.message = message;
	}
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onPostExecute(String response) {
		Gson gson = new Gson();
		try {
			NewActionInfoListResponse resp = gson.fromJson(response, NewActionInfoListResponse.class);
			int result = resp.result;
			List<NewActionInfo> actionList = resp.actionList;
			if(result==0){
				actionInfolList.addAll(actionList);
    			if (index == ZhaolinConstants.DRAG_TAG){
    				dragListView.onRefreshComplete();
    			}
    			else if (index == ZhaolinConstants.LOADMORE_TAG){
    				dragListView.onLoadMoreComplete(false);
    			}
    			activity.dismissProgress();
    			newsFeedAdapter.notifyDataSetChanged();
			}
        }
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}