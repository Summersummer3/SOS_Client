package com.example.layoutt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Main_UI extends Activity {
	
	
	private ImageButton ib1,ib2,ib3;
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
		ib1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i("test", "µã»÷Ò»´Î£¡£¡");
				Intent dial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+"15243611674"));
				startActivity(dial);
				
			}
		});
		
	}

}
