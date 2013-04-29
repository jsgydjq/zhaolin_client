package com.deepred.zhaolin.utils;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.deepred.zhaolin.BaseActivity;

public class LoadImageTask extends AsyncTask<Void, Integer, String> {
	private String url;
	private ImageView iv;
	private Uri uri;
	
	public LoadImageTask(ImageView iv, String url) {
		this.iv = iv;
		this.url = url;
	}
	
	@SuppressLint("NewApi")
	@Override
    protected String doInBackground(Void... params) {
		try {
			uri = FileCache.getImageURI(url, BaseActivity.cache);
            return "SUCCESS";
        } 
       catch (Exception e) {
    	   return "FAILED";
       }
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
    }
    
	@Override
	protected void onPostExecute(String result) {
		iv.setImageURI(uri);
    }	    	
}
