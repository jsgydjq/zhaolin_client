package com.deepred.zhaolin.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.R;
import com.deepred.zhaolin.adapter.NearbyAdapter;
import com.deepred.zhaolin.entity.NearbyEntity;
import com.deepred.zhaolin.utils.GetNearbyPeopleTask;
import com.deepred.zhaolin.utils.ZhaolinConstants;
import com.deepred.zhaolin.widget.DragListView;

public class GetNearbyPeopleActivity extends BaseActivity implements
	DragListView.OnRefreshLoadingMoreListener{
	private List<NearbyEntity> detailEntityList = new ArrayList<NearbyEntity>();
	private DragListView dragListView;	
	private int CURRENT_INDEX = 1;
	private NearbyAdapter nearbyAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.nearby);
		showProgress();
		dragListView = (DragListView) this.findViewById(R.id.listView_nearby);
		nearbyAdapter = new NearbyAdapter(this, R.layout.nearby_item, detailEntityList);
		dragListView.setOnRefreshListener(this);
		dragListView.setAdapter(nearbyAdapter);
		
		new GetNearbyPeopleTask(CURRENT_INDEX++, ZhaolinConstants.DRAG_TAG, GetNearbyPeopleActivity.this, 
				dragListView, nearbyAdapter, 
				detailEntityList).start();
	}

	@Override
	public void onRefresh() {
		new GetNearbyPeopleTask(CURRENT_INDEX++, ZhaolinConstants.DRAG_TAG, GetNearbyPeopleActivity.this, 
				dragListView, nearbyAdapter, 
				detailEntityList).start();
		nearbyAdapter.notifyDataSetChanged();
		dragListView.setAdapter(nearbyAdapter);
	}

	@Override
	public void onLoadMore() {
		new GetNearbyPeopleTask(CURRENT_INDEX++, ZhaolinConstants.LOADMORE_TAG, GetNearbyPeopleActivity.this, 
				dragListView, nearbyAdapter, 
				detailEntityList).start();
		nearbyAdapter.notifyDataSetChanged();
		dragListView.setAdapter(nearbyAdapter);
		dragListView.setSelection(dragListView.getAdapter().getCount()-1);
	}
}