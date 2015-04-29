package com.example.layoutt;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Map_UI extends Activity {
	
	private TextView tv1;
	private TextView tv2;
	private MapView mapView;
	private BaiduMap mBaiduMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);   //务必记住！
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map);
		init();
	}

	private void init() {

		mapView = (MapView) findViewById(R.id.baidumapView);
		
		mBaiduMap = mapView.getMap();
		MapStatusUpdate msu1 = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu1);
		
		mBaiduMap.setMyLocationEnabled(true);
		
		Intent intent = getIntent();
		double Latitude = intent.getExtras().getDouble("tv1");
		double Longitude = intent.getExtras().getDouble("tv2");
		
	
		// 获得一个位置信息
		BDLocation location = new BDLocation();
		location.setLatitude(Latitude);
		location.setLongitude(Longitude);
		
		MyLocationData data = new MyLocationData.Builder()//
							 .accuracy(location.getRadius())//
							 .latitude(location.getLatitude())//
							 .longitude(location.getLongitude())
							 .build();
		
		LatLng latLng = new LatLng(Latitude, Longitude);
		MapStatusUpdate msu2 = MapStatusUpdateFactory.newLatLng(latLng);
		
		mBaiduMap.setMapStatus(msu2);
		mBaiduMap.setMyLocationData(data);
	
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		Log.v("map", "Destroy");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}
}
