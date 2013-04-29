package com.deepred.zhaolin.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.deepred.zhaolin.R;
import com.deepred.zhaolin.entity.SavedAppDAO;

public class SplashActivity extends Activity{
	
    private final int SPLASH_DISPLAY_LENGHT = 300;
	private final String PREFERENCES_NAME = "userinfo";
	public static SavedAppDAO sdao;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);
		SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
		boolean ifFirst = prefs.getBoolean("firstTime", true);
		
		//初始化DAO对象
		sdao = new SavedAppDAO(getApplicationContext());
		
		if (ifFirst){
        /* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
			new Handler().postDelayed(new Runnable(){
				@Override
				public void run() {
					// First time, login
					Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
					SplashActivity.this.startActivity(loginIntent);
					SplashActivity.this.finish();
            	}
        	}, SPLASH_DISPLAY_LENGHT);
		}
		else{
			Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
			SplashActivity.this.startActivity(mainIntent);
			SplashActivity.this.finish();
		}
	}   
}