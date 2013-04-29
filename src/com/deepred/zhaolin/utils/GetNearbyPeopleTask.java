package com.deepred.zhaolin.utils;

import java.util.List;

import android.annotation.SuppressLint;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.ZhaolinApplication;
import com.deepred.zhaolin.adapter.NearbyAdapter;
import com.deepred.zhaolin.entity.NearbyEntity;
import com.deepred.zhaolin.entity.NearbyPeopleResponse;
import com.deepred.zhaolin.widget.DragListView;
import com.google.gson.Gson;

@SuppressLint("InlinedApi")
public class GetNearbyPeopleTask extends PostAndGetResponse {

	int index;
	DragListView nearbyListView;
	NearbyAdapter nearbyAdapter;
	List<NearbyEntity> detailEntityList;
	public GetNearbyPeopleTask(int whichPage, int index, BaseActivity activity, DragListView dragListView, 
			NearbyAdapter discoverAdapter, List<NearbyEntity> detailEntityList){
		super(activity);
		this.index = index;
		this.nearbyListView = dragListView;
		this.nearbyAdapter = discoverAdapter;
		this.detailEntityList = detailEntityList;
		url = ZhaolinConstants.getNearbyPeople + whichPage;
		message = "{\"location\" : \"" +ZhaolinApplication.location+"\"}";
	}

	@Override
	protected void onPostExecute(String response){
		Gson gson = new Gson();
		activity.dismissProgress();
		try {
			NearbyPeopleResponse resp = gson.fromJson(response, NearbyPeopleResponse.class);
			int result = resp.result;
			List<NearbyEntity> userDetailList = resp.userList;
			if(result==0){
				for(NearbyEntity userDetail : userDetailList){
	    			if(userDetail.actionList.size() > 0)
	    				detailEntityList.add(userDetail);
	    		}
	            
	            if (index == ZhaolinConstants.DRAG_TAG){
    				nearbyListView.onRefreshComplete();
    			}
    			else if (index == ZhaolinConstants.LOADMORE_TAG){
    				nearbyListView.onLoadMoreComplete(false);
    			}
	            nearbyAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}