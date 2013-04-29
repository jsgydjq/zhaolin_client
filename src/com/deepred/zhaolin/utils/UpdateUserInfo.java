package com.deepred.zhaolin.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;

import com.deepred.zhaolin.BaseActivity;
import com.deepred.zhaolin.ZhaolinApplication;
import com.deepred.zhaolin.entity.UpdateUserResponse;
import com.deepred.zhaolin.ui.LoginActivity;
import com.deepred.zhaolin.ui.MainActivity;
import com.google.gson.Gson;
import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.users.UsersGetInfoRequestParam;
import com.renren.api.connect.android.users.UsersGetInfoResponseBean;

public class UpdateUserInfo extends BaseActivity {
	private String userName64, loc="0,0";
	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		prefs = this.getSharedPreferences("com.deepred.zhaolin.prefs", Context.MODE_PRIVATE);
		renrenId = renren.getCurrentUid()+"";
		loc = ZhaolinApplication.location;
		startUpdate();
	}

	/**
     * start the api list activity
     */
    private void startUpdate() {
		if (renren != null) {
			AsyncRenren asyncRenren = new AsyncRenren(renren);
			String[] uids = new String[1];
			uids[0] = renrenId;
			showProgress("首次登录请稍等...");
			UsersGetInfoRequestParam param = new UsersGetInfoRequestParam(uids);
			AbstractRequestListener<UsersGetInfoResponseBean> listener = new AbstractRequestListener<UsersGetInfoResponseBean>() {
	
				@Override
				public void onComplete(final UsersGetInfoResponseBean bean) {
					runOnUiThread(new Runnable() {
						
						@SuppressLint("InlinedApi")
						@Override
						public void run() {
							userName = bean.getUsersInfo().get(0).getName();
							headUrl = bean.getUsersInfo().get(0).getTinyurl();
							userName64 = Base64.encodeToString(userName.getBytes(), Base64.DEFAULT);
							
							showTip("Renren Login Success!" + userName + ": " + headUrl);
							
							new EncodeUploadTask().execute(loc);
						}
					});
				}
	
				@Override
				public void onRenrenError(final RenrenError renrenError) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showTip(renrenError.getMessage());
							//dismissProgress();
						}
					});
				}
	
				@Override
				public void onFault(final Throwable fault) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showTip(fault.getMessage());
							//dismissProgress();
						}
					});
				}
			};
			asyncRenren.getUsersInfo(param, listener );
		}
    }

    class EncodeUploadTask extends AsyncTask<Object, Long, String> {
		@Override
		protected String doInBackground(Object... params) {
			String response = (String) params[0];
			
			// GET current location
			String location = (String) params[0];
			// GET IMEI
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String IMEI = telephonyManager.getDeviceId();
			// GET BT MAC ADDR
			//BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
			String btMac = "NULL";//bta.getAddress();
			// GET WIFI ADDR
			WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInf = wifiMan.getConnectionInfo();
			String wifiAddr = wifiInf.getMacAddress();

			updatePackage up = new updatePackage();
			
			up.setRenrenId(renrenId);
			
			up.setUserName(userName64);
			
			up.setHeadUrl(headUrl);
			
			up.setLocation(location);
			
			up.setIMEI(IMEI);
			
			up.setBtMac(btMac);
			
			up.setWlMac(wifiAddr);
			
			up.setMobileModel(Build.MANUFACTURER + " " + Build.MODEL);
			
			Gson gson = new Gson();
			String message = gson.toJson(up);
			
			// 上传JSON对象
			DefaultHttpClient httpClient = new DefaultHttpClient();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpPost postMethod = new HttpPost(ZhaolinConstants.updateUserUrl);
			postMethod.setHeader( "Content-Type", "application/json");
			
			try {
				postMethod.setEntity(new ByteArrayEntity(message
						.getBytes("UTF8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			try {
				response = httpClient.execute(postMethod, responseHandler);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			dismissProgress();
			
			Gson gson = new Gson();
			UpdateUserResponse resp = gson.fromJson(response, UpdateUserResponse.class);
			int result = resp.result;
			userPrimkey = resp.userPrimkey;
			Intent intent;
			if(result == 0){
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(ZhaolinConstants.RENREN_ID, renrenId);
				editor.putString(ZhaolinConstants.RENREN_USER_NAME, userName);
				editor.putString(ZhaolinConstants.RENREN_HEAD_URL, headUrl);
				editor.putLong(ZhaolinConstants.USER_PRIMARY_KEY, userPrimkey);
				editor.commit();
				
				intent = new Intent(UpdateUserInfo.this, MainActivity.class);
		    	
				ZhaolinApplication app = (ZhaolinApplication) UpdateUserInfo.this.getApplication();
		    	app.renrenId = renrenId;
		    	app.userName = userName;
		    	app.headUrl = headUrl;
		    	app.userPrimkey = userPrimkey;
		    	
			}
			else{
				showTip("首次登陆失败，请重试");
				intent = new Intent(UpdateUserInfo.this, LoginActivity.class);
			}
	    	UpdateUserInfo.this.startActivity(intent);
	    	UpdateUserInfo.this.finish();
		}
	}
 }

class updatePackage {
	private String renrenId;
	private String userName;
	private String headUrl;
	private String IMEI;
	private String location;
	// the local Bluetooth MAC address
	private String btmac;
	// the local WIFI MAC address
	private String wlmac;
	private String mobileModel;
	
	public String getRenrenId(){
		return renrenId;
	}
	
	public void setRenrenId(String renrenId){
		this.renrenId = renrenId;
	}
	
	public void setHeadUrl(String headUrl){
		this.headUrl = headUrl;
	}
	
	public String getHeadUrl(){
		return headUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String IMEI) {
		this.IMEI = IMEI;
	}
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMobileModel() {
		return mobileModel;
	}

	public void setMobileModel(String mobileModel) {
		this.mobileModel = mobileModel;
	}

	public String getBtMac() {
		return btmac;
	}

	public void setBtMac(String btmac) {
		this.btmac = btmac;
	}

	public String getWlMac() {
		return wlmac;
	}

	public void setWlMac(String wlmac) {
		this.wlmac = wlmac;
	}
}
