package com.example.layoutt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Map_UI extends Activity {
	
	private TextView tv1;
	private TextView tv2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);   //务必记住！
		setContentView(R.layout.map);
		init();
	}

	private void init() {
		tv1 = (TextView) findViewById(R.id.textView_map1);
		tv2 = (TextView) findViewById(R.id.textView_map2);
		
		Intent intent = getIntent();
		String Latitude = "Latitude:"+intent.getExtras().getString("tv1");
		String Longitude = "Longitude:"+intent.getExtras().getString("tv2");
		
		tv1.setText(Latitude);
		tv2.setText(Longitude);
	}
	
	
}
