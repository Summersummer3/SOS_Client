package com.example.layoutt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Table1 extends Activity {
	
	private TextView tv1,tv2;
	private Button bt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.table1);
		tv1 = (TextView) findViewById(R.id.textView_username);
		tv2 = (TextView) findViewById(R.id.textView_password);
		bt = (Button) findViewById(R.id.button1_second);
				
				
				
		Bundle bd = this.getIntent().getExtras(); //extras
		String user_name = bd.getString("user_name");
		String pass_word = bd.getString("pass_word");
		tv1.setText(user_name);
		tv2.setText(pass_word);
		
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Table1.this.setResult(RESULT_OK,Table1.this.getIntent());
				Table1.this.finish();
			}
		});
		
		
	}
	

}
