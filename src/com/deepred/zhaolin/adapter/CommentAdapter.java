package com.deepred.zhaolin.adapter;

import java.util.List;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.R;
import com.deepred.zhaolin.entity.NewActionComment;
import com.deepred.zhaolin.utils.FileCache;
import com.deepred.zhaolin.utils.LoadImageTask;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CommentAdapter extends ArrayAdapter<NewActionComment> implements ListAdapter{

	private List<NewActionComment> list;
	private LayoutInflater layoutInflater;
	private Drawable drawable;
	
	public CommentAdapter(Context context, int textViewResourceId,
			List<NewActionComment> objects) {
		super(context, textViewResourceId, objects);
		this.list = objects;
		layoutInflater = LayoutInflater.from(context);
		drawable = context.getResources().getDrawable(R.drawable.icon_wait);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public NewActionComment getItem(int position){
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private void asyncLoadImage(ImageView iv, String url){
		new LoadImageTask(iv, url).execute();
	}

	class ViewHolder {
		public ImageView userIcon;
		public TextView userNameWithComment;
	}
	
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		NewActionComment actionComment = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.comment_adapter, null);
			holder.userIcon = (ImageView) convertView.findViewById(R.id.userIcon);
			holder.userNameWithComment = (TextView) convertView.findViewById(R.id.userNameWithComment);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		try {
			holder.userIcon.setImageURI(FileCache.getImageURI(actionComment.poster.headUrl, BaseActivity.cache));
		} catch (Exception e) {
			holder.userIcon.setImageDrawable(drawable);
			asyncLoadImage(holder.userIcon, actionComment.poster.headUrl);
		}
		holder.userNameWithComment.setText(actionComment.poster.userName + ": " + actionComment.content);
		return convertView;
	}

}
