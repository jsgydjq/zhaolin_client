package com.deepred.zhaolin;

import java.io.File;

import com.renren.api.connect.android.Renren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BaseActivity extends Activity {
	public static File cache;
	public Renren renren;
	protected String renrenId, userName, headUrl;
	protected long userPrimkey = -1;
	
	public ProgressDialog mProgressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
	    //create cache
        cache = new File(Environment.getExternalStorageDirectory(), "cache2");

        if(!cache.exists()){
        	cache.mkdirs();
        }
        updateData();
	}

	private void updateData() {
		ZhaolinApplication application = (ZhaolinApplication) this.getApplication();
	    renren = application.renren;
	    renrenId = application.renrenId;
	    userName = application.userName;
	    headUrl = application.headUrl;
	    userPrimkey = application.userPrimkey;
	}

	/**
	 * 显示等待框
	 */
	public void showProgress() {
		showProgress("奋力加载中...");
	}

	/**
	 * 显示等待框
	 * 
	 * @param title
	 * @param message
	 */
	protected void showProgress(String message) {
		mProgressDialog = new ProgressDialog(BaseActivity.this);
		mProgressDialog.setMessage(message);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}

	/**
	 * 取消等待框
	 */
	public void dismissProgress() {
		if (mProgressDialog != null) {
			try {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 显示Toast提示
	 * 
	 * @param message
	 */
	public void showTip(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissProgress();
	}
}
