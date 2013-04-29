package com.deepred.zhaolin.adapter;

import java.util.List;

import com.deepred.zhaolin.R;
import com.deepred.zhaolin.entity.NewCommentBrief;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CommentBriefAdapter extends ArrayAdapter<NewCommentBrief> implements ListAdapter{

	private List<NewCommentBrief> list;
	private LayoutInflater layoutInflater;
	
	public CommentBriefAdapter(Context context, int textViewResourceId,
			List<NewCommentBrief> objects) {
		super(context, textViewResourceId, objects);
		this.list = objects;
		layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public NewCommentBrief getItem(int position){
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		public TextView comment;
	}
	
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		NewCommentBrief comment = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.comment_brief_adapter, null);
			holder.comment = (TextView) convertView.findViewById(R.id.commentBrief);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.comment.getLayoutParams();
		if(comment.type==0){
			holder.comment.setText("顶: " + comment.content);
			holder.comment.setTextColor(Color.rgb(200, 0, 0));
			holder.comment.setGravity(Gravity.LEFT);
			holder.comment.setBackgroundResource(R.drawable.left);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			holder.comment.setLayoutParams(params);
		}
		else if(comment.type==1){
			holder.comment.setText(comment.content + " :踩");
			holder.comment.setTextColor(Color.rgb(0, 150, 0));
			holder.comment.setGravity(Gravity.RIGHT);
			holder.comment.setBackgroundResource(R.drawable.right);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			holder.comment.setLayoutParams(params);
		}
		else{
			holder.comment.setText(comment.content);
			holder.comment.setTextColor(Color.DKGRAY);
			holder.comment.setBackgroundResource(R.drawable.user_radiusback);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			holder.comment.setLayoutParams(params);
		}
		return convertView;
	}

}
