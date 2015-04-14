package com.example.layoutt;

import org.xml.sax.Locator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Main_UI extends Activity implements LocationListener{
	
	
	private ImageButton ib1,ib2,ib3;
	private TextView tv;
	private LocationManager mLocationManager;
	private Location location;
	private String loc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_ui);
		uiInit();
		
	
		
	}
	
	private void uiInit(){
		ib1 = (ImageButton) findViewById(R.id.imageButton1);
		ib2 = (ImageButton) findViewById(R.id.imageButton2);
		ib3 = (ImageButton) findViewById(R.id.imageButton3);
		tv = (TextView) findViewById(R.id.textView_GPSLoc);
		
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		loc = changeLoc(location);
		
		while(location==null){
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
		}
				
		
		ib1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent dial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+"110"));
				startActivity(dial);
				
			}
		});
		
		ib2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent dial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+"15243611674"));
				startActivity(dial);
			}
		});
		
		ib3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i("gps", "点击按钮");
				tv.setText(loc);
				
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(this);
	}
	
	private String changeLoc(Location location) {
		if(location!=null){
			return location.getLatitude()+"  "+location.getLongitude();
		}
		else{
			return null;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		loc = changeLoc(location);
		Log.i("gps", "更新位置");
	}

	@Override
	public void onProviderDisabled(String arg0) {
		loc = changeLoc(null);
		Log.i("gps", "结束");
	}

	@Override
	public void onProviderEnabled(String provider) {
		loc = changeLoc(mLocationManager.getLastKnownLocation(provider));
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
	}
	

   
}
