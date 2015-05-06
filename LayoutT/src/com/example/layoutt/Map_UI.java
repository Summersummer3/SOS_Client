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
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class Map_UI extends Activity {
	
	private String Addr;
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
		Addr = intent.getExtras().getString("tv3");
	
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.id_getLocation:
			Toast.makeText(this, Addr, 0).show();
			break;
		case R.id.id_getLocationHistory:
			break;
		}
		return super.onOptionsItemSelected(item);
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
