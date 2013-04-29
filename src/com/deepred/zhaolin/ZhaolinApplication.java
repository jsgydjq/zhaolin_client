package com.deepred.zhaolin;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.renren.api.connect.android.Renren;

import android.app.Application;

public class ZhaolinApplication extends Application{
	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener;
	public static String location;
	
	public Renren renren;
	
	public String renrenId, userName, headUrl;
	
	public long userPrimkey = -1;
	
	@Override
	public void onCreate() {
		super.onCreate(); 
		location = new String("0,0");
		myListener = new MyLocationListenner();
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(myListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		//option.setScanSpan(1000);
		option.setPriority(LocationClientOption.NetWorkFirst);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		mLocationClient.requestLocation();
		
	}
	
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			if (bdLocation == null)
				return ;
			double latitude = bdLocation.getLatitude();
			double longitude = bdLocation.getLongitude();
			location = (latitude + "," + longitude);
			System.out.println(location);
		}
		
		@Override
		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

}
