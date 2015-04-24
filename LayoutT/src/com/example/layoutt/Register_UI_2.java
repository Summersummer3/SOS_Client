package com.example.layoutt;

import org.json.JSONObject;

import com.gc.materialdesign.views.ButtonFlat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Register_UI_2 extends Activity {
	
	private EditText et1,et2,et3;
	private ButtonFlat bf;
	private String helpUserName1,helpUserName2,helpUserName3;
	
	public volatile static String result = "0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_2);
		init();
	}

	private void init() {
		et1 = (EditText) findViewById(R.id.editText_HelpList1);
		et2 = (EditText) findViewById(R.id.editText_HelpList2);
		et3 = (EditText) findViewById(R.id.editText_HelpList3);
		bf = (ButtonFlat) findViewById(R.id.button_register2);
		
		bf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				helpUserName1 = et1.getText().toString();
				helpUserName2 = et2.getText().toString();
				helpUserName3 = et3.getText().toString();
				if(helpUserName1.equals("")){
					helpUserName1 = "null";
				}
				if(helpUserName2.equals("")){
					helpUserName2 = "null";
				}
				if(helpUserName3.equals("")){
					helpUserName3 = "null";
				}
				
				helpListRegister();
			}
		});
	}
	
		private void helpListRegister() {
				
				JSONObject json = new JSONObject();
				
				try {
					json.put("helpUserName1",helpUserName1);
					json.put("helpUserName2",helpUserName2);
					json.put("helpUserName3",helpUserName3);
					
					Socket_Service.out.write("3\n");
					Socket_Service.out.flush();
					Thread.sleep(2000);
					Socket_Service.out.write(json.toString() + "\n");
					Socket_Service.out.flush();
					Thread.sleep(2000);
					helpListRegisterResult();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		private void helpListRegisterResult() {
			
				if(result.equals("11")){
					ShowDialog("第一个账号不存在,请重新输入",et1);
				}
				if(result.equals("12")){
					ShowDialog("第二个账号不存在,请重新输入",et2);
				}
				if(result.equals("13")){
					ShowDialog("第三个账号不存在,请重新输入",et3);
				}
				if(result.equals("3")){
					  new AlertDialog.Builder(this).setTitle("恭喜您").setMessage("添加求救号成功,请进行重新登录")
		                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    	Intent intent = new Intent();
								intent.setClass(Register_UI_2.this, MainActivity.class);
								startActivity(intent);
								finish();
								
		                    }
		                }).show();
				}
				else{
					
					ShowDialog("网络或者其他出现错误，请重试",et1);
					
				}
			
		}
		
	    
		public void ShowDialog(String msg,final EditText et) {
		        new AlertDialog.Builder(this).setTitle("notification").setMessage(msg)
		                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								et.requestFocus();
		                    }
		                }).show();
		    }
	
}
