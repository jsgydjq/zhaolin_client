package com.deepred.zhaolin.utils;

import java.util.List;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.R;
import com.deepred.zhaolin.entity.SimpleResponse;
import com.deepred.zhaolin.entity.NewCommentBrief;
import com.google.gson.Gson;

public class NewActionCommentTask extends PostAndGetResponse {

	NewCommentBrief comment;
	List<NewCommentBrief> commentList;
	View view;
	public NewActionCommentTask(String message, BaseActivity activity,
			List<NewCommentBrief> commentList, NewCommentBrief comment, View view){
		super(activity);
		this.comment = comment;
		this.commentList = commentList;
		this.view = view;
		this.message = message;
		this.url = ZhaolinConstants.postNewAction;
	}

	@Override
	protected void onPostExecute(String response) {
		Gson gson = new Gson();
		try {
			SimpleResponse resp = gson.fromJson(response,
					SimpleResponse.class);
			int result = resp.result;
			if(result==0){
				commentList.add(comment);
				ListView lv = (ListView)view.findViewById(R.id.commentList);
				lv.setAdapter(lv.getAdapter());
				EditText commentBox = (EditText) view.findViewById(R.id.commentBox);
				commentBox.setText("");
				TextView hint = (TextView) view.findViewById(R.id.commentHint);
				hint.setVisibility(View.GONE);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
