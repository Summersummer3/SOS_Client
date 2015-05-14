package com.example.layoutt;

import java.io.IOException;
import java.util.ArrayList;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Map_UI extends Activity {
	
	private String Addr;
	private MapView mapView;
	private BaiduMap mBaiduMap;
	private ProgressBar mProgressBar;
	public static ArrayList<Location> locList;
	private BitmapDescriptor mMarker;
	private String helpName;
	
	
	
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
		locList = new ArrayList<Location>();
		
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar_Map);
		
		Intent intent = getIntent();
		double Latitude = intent.getExtras().getDouble("tv1");
		double Longitude = intent.getExtras().getDouble("tv2");
		Addr = intent.getExtras().getString("tv3");
		helpName = intent.getExtras().getString("tv4");
		
		//addOverLay
		
		addOverlay(Latitude, Longitude);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
			@Override
			public boolean onMarkerClick(Marker marker) {
				
				InfoWindow infoWindow;
				TextView tv = new TextView(Map_UI.this);
				tv.setBackgroundResource(R.drawable.location_tips);
				tv.setPadding(30, 20, 30, 50);
				tv.setText(marker.getExtraInfo().getString("TAG", ""));
				tv.setTextColor(Color.parseColor("#ffffff"));
				
				//将地图上的左边点转换成屏幕上的点，然后设置info window的偏移量！
				LatLng ll_1 = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll_1);
				p.y -= 47;
				LatLng ll_2 = mBaiduMap.getProjection().fromScreenLocation(p);
				
				infoWindow = new InfoWindow(tv,ll_2,1);
				mBaiduMap.showInfoWindow(infoWindow);
				
				return true;
			}
		});
		
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();
			}
		});
	}
	
	private void addOverlay(double Latitude,double Longitude){
		mBaiduMap.clear();
		LatLng mLatLng = new LatLng(Latitude, Longitude);
		OverlayOptions options = new MarkerOptions().position(mLatLng).icon(mMarker).zIndex(5);
		Marker marker = (Marker) mBaiduMap.addOverlay(options);
		Bundle arg0 = new Bundle();
		arg0.putString("TAG", Addr);
		marker.setExtraInfo(arg0);
		
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(mLatLng);
		mBaiduMap.setMapStatus(msu);
		
	}
	
	private void addOverlays(ArrayList<Location> locList){
		
		mBaiduMap.clear();
		LatLng mLatLng = null;
		OverlayOptions options = null;
		for(Location loc:locList){
			mLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
			options = new MarkerOptions().position(mLatLng).icon(mMarker).zIndex(5);
			Marker marker = (Marker) mBaiduMap.addOverlay(options);
			Bundle arg0 = new Bundle();
			arg0.putString("TAG",loc.getTime());
			marker.setExtraInfo(arg0);
			
		}
		
		
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(mLatLng);
		mBaiduMap.setMapStatus(msu);
		
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
			getHelpHistroy();
			
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void getHelpHistroy() {
		try {
			Socket_Service.out.write("4\n");
			Socket_Service.out.flush();
			Thread.sleep(1000);
			Socket_Service.out.write(helpName + "\n");
			Socket_Service.out.flush();
			
			new getHistroy().execute();
			Log.v("MAP", "start...");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	class getHistroy extends AsyncTask<Void, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBar.setVisibility(View.VISIBLE);
			
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			mProgressBar.setVisibility(View.GONE);
			if(result==false){
				Toast.makeText(getApplicationContext(), "未获取数据请检查网络", 0);
			}
			else{
				
				addOverlays(locList);
				Log.v("MAP", "addOverlays!");
				
			}
			
			super.onPostExecute(result);
		}

		

		protected Boolean doInBackground(Void... arg0) {
			int count = 0;
			while(Socket_Service.locGet = false){
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				count++;
				
				if(count>20){
					
					return false;
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
			
		}
		
		
	}
}
