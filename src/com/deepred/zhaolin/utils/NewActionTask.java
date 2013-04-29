package com.deepred.zhaolin.utils;

import java.util.List;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.R;
import com.deepred.zhaolin.adapter.CommentBriefAdapter;
import com.deepred.zhaolin.entity.NewAppInfo;
import com.deepred.zhaolin.entity.NewAppWithCommentsResponse;
import com.deepred.zhaolin.entity.NewCommentBrief;
import com.google.gson.Gson;

public class NewActionTask extends PostAndGetResponse {
	
	private View view;
	List<NewCommentBrief> commentList;

	public NewActionTask(String message, List<NewCommentBrief> commentList, 
			BaseActivity activity, View view){
		super(activity);
		this.view = view;
		this.commentList = commentList;
		this.commentList.clear();
		this.message = message;
		this.url = ZhaolinConstants.getAppComments;
		activity.showProgress();
	}

	@Override
	protected void onPostExecute(String response) {
		activity.dismissProgress();
		Gson gson = new Gson();
		class ViewHolder{
			ImageView appIcon;
			TextView appName;
			TextView dnldCount;
			TextView starRate;
			TextView appDescript;
			ListView commentList;
		}
		ViewHolder holder = new ViewHolder();
		holder.appIcon = (ImageView) view.findViewById(R.id.appIcon);
		holder.appName = (TextView) view.findViewById(R.id.appName);
		holder.dnldCount = (TextView) view.findViewById(R.id.dnldCount);
		holder.starRate = (TextView) view.findViewById(R.id.starRate);
		holder.appDescript = (TextView) view.findViewById(R.id.appDescript);
		holder.commentList = (ListView) view.findViewById(R.id.commentList);
		
		try {
			NewAppWithCommentsResponse resp = gson.fromJson(response,
					NewAppWithCommentsResponse.class);
			int result = resp.result;
			for(NewCommentBrief comment : resp.commentList){
				commentList.add(comment);
			}
			NewAppInfo appInfo = resp.appInfo;
			if(result==0){
				try {
					holder.appIcon.setImageURI(FileCache.getImageURI(appInfo.icon_url, BaseActivity.cache));
				} catch (Exception e) {
					asyncLoadImage(holder.appIcon, appInfo.icon_url);
				}
				holder.appName.setText(appInfo.appname);
				holder.dnldCount.setText("下载量: "+appInfo.down_count);
				holder.starRate.setText("星级: "+appInfo.rateRank);
				holder.appDescript.setText(appInfo.user_descript);
				if(commentList.size()==0){
					TextView hint = (TextView) view.findViewById(R.id.commentHint);
					hint.setVisibility(View.VISIBLE);
				}
				CommentBriefAdapter adapter = new CommentBriefAdapter(activity, R.layout.comment_brief_adapter, commentList);
				holder.commentList.setAdapter(adapter);	
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void asyncLoadImage(ImageView iv, String url){
		new LoadImageTask(iv, url).execute();
	}
}
