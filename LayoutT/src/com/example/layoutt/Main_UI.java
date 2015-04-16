package com.example.layoutt;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Locator;

import com.gc.materialdesign.widgets.Dialog;

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
import android.widget.Toast;

public class Main_UI extends Activity implements LocationListener{
	
	
	private ImageButton ib1,ib2,ib3;
	private TextView tv;
	private LocationManager mLocationManager;
	private Location location;
	private JSONObject loc;
	
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
		
		
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		loc = changeLoc(location);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
		
				
		
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
				if(loc==null){
					Dialog dialog = new Dialog(Main_UI.this, "定位出错！", "定位尚未成功 ,请稍等！");
					dialog.show();
					dialog.getButtonAccept().setText("等待定位");
					dialog.getButtonCancel().setText("");
					
				}
				else{
					try {
						Socket_Service.out.write("1\n");
						Socket_Service.out.flush();
						Thread.sleep(1000);
						Socket_Service.out.write(loc.toString()+"\n");
						Socket_Service.out.flush();
						Toast.makeText(Main_UI.this, "位置信息发送成功", 0).show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		while(loc==null)
		{
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(this);
	}
	
	private JSONObject changeLoc(Location location) {
		JSONObject json = new JSONObject();
		if(location!=null){
			try {
				json.put("Latitude", location.getLatitude());
				json.put("Longitude", location.getLongitude());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return json;
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
