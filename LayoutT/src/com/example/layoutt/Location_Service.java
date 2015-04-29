package com.example.layoutt;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class Location_Service extends Service {
	
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private JSONObject locationJSON;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.v("Loc", "onCreate");
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.v("Loc", "start");
		locationJSON = null;
		initLocatinClient();
		
	}
	
	private void initLocatinClient() {
		
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationListener = new MyLocationListener();
		
		mLocationClient.registerLocationListener(mLocationListener);
		
		LocationClientOption options = new LocationClientOption();
		options.setCoorType("bd09ll");
		options.setIsNeedAddress(true);
		options.setOpenGps(true);
		
		mLocationClient.setLocOption(options);
		
		if(!mLocationClient.isStarted()){
			mLocationClient.start();
		}
		
	}
	
	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			
			Log.v("Loc", "getLoc");
			setLocationJSON(location);
			getLocationJSON();
										
		}

		
		
	}

	public void setLocationJSON(BDLocation location) {
		
		JSONObject loc = new JSONObject();
		try {
			loc.put("Latitude", location.getLatitude());
			loc.put("Longitude", location.getLongitude());
			loc.put("Address", location.getAddrStr());
			locationJSON = loc;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
	}
	
	public JSONObject getLocationJSON() {
		
		Main_UI.loc = locationJSON;
		
		return locationJSON;
	}
}
