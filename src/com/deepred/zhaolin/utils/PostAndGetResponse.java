package com.deepred.zhaolin.utils;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.deepred.zhaolin.BaseActivity;

import android.os.AsyncTask;

public class PostAndGetResponse extends AsyncTask<Object, Integer, String>{

	protected BaseActivity activity;
	protected String url, message;
	public PostAndGetResponse(BaseActivity activity){
		this.activity = activity;
	}
	public PostAndGetResponse(){
		
	}
	
	@Override
	protected String doInBackground(Object... params) {
		String url = (String)params[0];
		String message = (String) params[1];
		String response = "";
		try {
        	DefaultHttpClient httpClient = new DefaultHttpClient();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpPost postMethod = new HttpPost(url);
			postMethod.setHeader( "Content-Type", "application/json");
			postMethod.setEntity(new ByteArrayEntity(message
						.getBytes("UTF8")));
			
			response = httpClient.execute(postMethod, responseHandler);
        }
		catch (Exception e) {
			e.printStackTrace();
			response = "{\"result\":-1}";
		}
		return response;
	}
	
	public void start(){
		this.execute(url, message);
	}
}
