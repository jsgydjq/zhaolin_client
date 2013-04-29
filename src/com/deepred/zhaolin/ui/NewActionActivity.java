package com.deepred.zhaolin.ui;

import java.util.ArrayList;
import java.util.List;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.R;
import com.deepred.zhaolin.entity.MyAppEntity;
import com.deepred.zhaolin.utils.NewActionCommentTask;
import com.deepred.zhaolin.utils.NewActionTask;
import com.deepred.zhaolin.utils.ZhaolinConstants;
import com.deepred.zhaolin.entity.NewCommentBrief;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NewActionActivity extends BaseActivity{

	private List<NewCommentBrief> commentList = new ArrayList<NewCommentBrief>();
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		Intent intent = getIntent();
		MyAppEntity appEntity = (MyAppEntity) intent.getSerializableExtra(ZhaolinConstants.APP_ENTITY);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.new_action, null);
		setContentView(view);
		initContentView(view, appEntity);
		new NewActionTask(getMessageFromEntity(appEntity), commentList, NewActionActivity.this, view).start();
	}

	private void initContentView(final View view, final MyAppEntity appEntity) {
		class ViewHolder{
			int commentType;
			ImageView appIcon;
			TextView appName;
			TextView appDescript;
			Button upButton;
			Button downButton;
			EditText commentBox;
			Button confirmButton;
		}
		final ViewHolder holder = new ViewHolder();
		holder.appIcon = (ImageView) view.findViewById(R.id.appIcon);
		holder.appName = (TextView) view.findViewById(R.id.appName);
		holder.appDescript = (TextView) view.findViewById(R.id.appDescript);
		holder.upButton = (Button) view.findViewById(R.id.upButton);
		holder.downButton = (Button) view.findViewById(R.id.downButton);
		holder.commentBox = (EditText) view.findViewById(R.id.commentBox);
		holder.confirmButton = (Button) view.findViewById(R.id.confirmAction);
		
		holder.commentType = -1;
		holder.appIcon.setImageBitmap(appEntity.getImgBitmap());
		holder.appName.setText(appEntity.getTitle());
		holder.appDescript.setText("Oops， 还木有描述哦");
		
		holder.upButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				holder.commentType = 0;
				holder.upButton.setBackgroundResource(R.drawable.btn_selected);
				holder.downButton.setBackgroundResource(R.drawable.btn_unselected);
				System.out.println("List size: "+commentList.size());
			}
		});
		holder.downButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				holder.commentType = 1;
				holder.upButton.setBackgroundResource(R.drawable.btn_unselected);
				holder.downButton.setBackgroundResource(R.drawable.btn_selected);
			}
		});
		holder.confirmButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				ActionUploadPackage actionPackage = new ActionUploadPackage();
				actionPackage.userPrimkey = NewActionActivity.this.userPrimkey;
				actionPackage.appName = appEntity.getTitle();
				actionPackage.comment = holder.commentBox.getText().toString();
				if(actionPackage.comment.length()>0){
					actionPackage.commentType = holder.commentType;
					
					NewCommentBrief comment = new NewCommentBrief();
					comment.content = actionPackage.comment;
					comment.type = holder.commentType;
					
					Gson gson = new Gson();
					String message = gson.toJson(actionPackage);
					showTip(message);
					new NewActionCommentTask(message, NewActionActivity.this, commentList, comment, view).start();
				}
				else{
					showTip("您还没有写评价哦~");
				}
			}
		});
	}

	private String getMessageFromEntity(MyAppEntity appEntity) {
		UploadPackage uploadPackage = new UploadPackage();
		uploadPackage.userPrimkey = 1;
		uploadPackage.appName = appEntity.getTitle();
		uploadPackage.appIcon = appEntity.getImgString();
		uploadPackage.appSize = appEntity.getSize()+"";
		Gson gson = new Gson();
		String msg = gson.toJson(uploadPackage);
		return msg;
	}

	class UploadPackage{
		public long userPrimkey;
		public String appName;
		public String appIcon;
		public String appSize;
	}

	class ActionUploadPackage{
		public long userPrimkey;
		public String appName;
		public String comment;
		public int commentType;
	}
}
