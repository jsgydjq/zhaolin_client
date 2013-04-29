package com.deepred.zhaolin.ui;

import com.deepred.zhaolin.R;
import com.deepred.zhaolin.ZhaolinApplication;
import com.deepred.zhaolin.utils.UpdateUserInfo;
import com.deepred.zhaolin.utils.ZhaolinConstants;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.view.RenrenAuthListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static final String RENREN_API_KEY = "6b1016db20c540e78bd1b20be4c707a3";
	private static final String RENREN_SECRET_KEY = "4723a695c09e4ddebbe8d87393d95fb4";
	private static final String RENREN_APP_ID = "105381";
	
	private SharedPreferences prefs;
	private String renrenId, userName, headUrl;
	private long userPrimkey;
	private LinearLayout loginLayout;
	private Button renrenLoginButton;
	
	private Renren renren;
	
	final RenrenAuthListener listener = new RenrenAuthListener() {
		@Override
		public void onComplete(Bundle values) {
			showTip("Renren Login Success!");
			String storedRenrenId = prefs.getString(ZhaolinConstants.RENREN_ID, null);
			renrenId = renren.getCurrentUid()+"";
			Intent intent;
			if(renrenId.equalsIgnoreCase(storedRenrenId)){
				userName = prefs.getString(ZhaolinConstants.RENREN_USER_NAME, "null");
				headUrl = prefs.getString(ZhaolinConstants.RENREN_HEAD_URL, "null");
				userPrimkey = prefs.getLong(ZhaolinConstants.USER_PRIMARY_KEY, -1);
				
				intent = new Intent(LoginActivity.this, MainActivity.class);

		    	ZhaolinApplication app = (ZhaolinApplication) LoginActivity.this.getApplication();
		    	app.renren = renren;
		    	app.renrenId = renrenId;
		    	app.userName = userName;
		    	app.headUrl = headUrl;
		    	app.userPrimkey = userPrimkey;
			}
			else{
				ZhaolinApplication app = (ZhaolinApplication) LoginActivity.this.getApplication();
		    	app.renren = renren;
				intent = new Intent(LoginActivity.this, UpdateUserInfo.class);
			}
			LoginActivity.this.startActivity(intent);
			LoginActivity.this.finish();
		}

		@Override
		public void onRenrenAuthError(
				RenrenAuthError renrenAuthError) {
				new Handler().post(new Runnable() {
				@Override
				public void run() {
					LoginActivity.this.showTip("验证失败");
				}
			});
		}

		@Override
		public void onCancelLogin() {
		}

		@Override
		public void onCancelAuth(Bundle values) {
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		renren = new Renren(RENREN_API_KEY, RENREN_SECRET_KEY, RENREN_APP_ID, this);
		loginLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.login, null);
		prefs = LoginActivity.this.getSharedPreferences("com.deepred.zhaolin.prefs", Context.MODE_PRIVATE);
		String storedRenrenId = prefs.getString(ZhaolinConstants.RENREN_ID, null);
		if(storedRenrenId != null){
			renren.authorize(LoginActivity.this, listener);
		}
		else{
			setContentView(loginLayout);
			initButtons();
		}
	}
	
	private void initButtons() {
		renrenLoginButton = (Button) loginLayout.findViewById(R.id.renren_login_button);
		renrenLoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				renren.authorize(LoginActivity.this, listener);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		renren.init(this);
	}
	
	private void showTip(String message){
		Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
	}
}