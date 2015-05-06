package com.example.layoutt;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Locator;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Main_UI extends Activity{
	
	
	private ImageButton ib1,ib2,ib3;
	private ButtonFlat bf1;
	private TextView tv;
	private LocationManager mLocationManager;
	private Location location;
	public volatile static JSONObject loc;
	private boolean LocationServiceOn = false;
	private String PREFS_NAME = "com.example.layoutt";
	private String username,tel;
	
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
		bf1 = (ButtonFlat) findViewById(R.id.button_logout);
		
		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
		tel = setting.getString("Telephone" + username, "");
		
		
		ib1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent dial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+110));
				startActivity(dial);
				
			}
		});
		
		ib2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.v("tel", tel);
				Intent dial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+tel));
				startActivity(dial);
			}
		});
		
		ib3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				
				if(loc==null){
					Dialog dialog = new Dialog(Main_UI.this, "定位出错！", "定位未启动或者正在等待定位中，请稍后再试");
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
		
		bf1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SharedPreferences setting = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
				SharedPreferences.Editor editor = setting.edit();
				editor.putBoolean("Logined", false);
				editor.commit();
				try {
					
					Socket_Service.out.write("bye\n");
					Socket_Service.out.flush();
					Socket_Service.isConnect = false;

					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				Intent intent =new Intent();
				intent.setClass(Main_UI.this, MainActivity.class);
				startActivity(intent);
				
				finish();
			}
		});
		

		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Intent service = new Intent();
		service.setClass(this,Location_Service.class);
		
		switch(item.getItemId()){
		case R.id.id_location_on:
			if(LocationServiceOn==false){
				LocationServiceOn = true;
				startService(service);
				item.setTitle("定位已启动");
				break;
			}else{
				
				LocationServiceOn = false;
				stopService(service);
				item.setTitle("启动定位");
				break;
			}
			
		}
		
		return super.onOptionsItemSelected(item);
	}


   
}
