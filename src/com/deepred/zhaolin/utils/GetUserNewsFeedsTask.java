package com.deepred.zhaolin.utils;

import java.util.List;

import android.annotation.SuppressLint;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.adapter.UserNewsFeedAdapter;
import com.deepred.zhaolin.entity.NewActionInfo;
import com.deepred.zhaolin.entity.NewActionInfoListResponse;
import com.google.gson.Gson;

public class GetUserNewsFeedsTask extends PostAndGetResponse {
	
	UserNewsFeedAdapter newsFeedAdapter;
	List<NewActionInfo> actionInfolList;
	public GetUserNewsFeedsTask(String message, BaseActivity activity,
			UserNewsFeedAdapter newsFeedAdapter, List<NewActionInfo> actionDetailList) {
		super(activity);
		this.newsFeedAdapter = newsFeedAdapter;
		this.actionInfolList = actionDetailList;
		url = ZhaolinConstants.getUserActionBriefList;
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
    			activity.dismissProgress();
    			newsFeedAdapter.notifyDataSetChanged();
			}
        }
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}