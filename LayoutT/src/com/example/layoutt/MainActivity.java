package com.example.layoutt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ProgressBarCircularIndetermininate;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity implements OnFocusChangeListener{

	
	public static ButtonFlat bt;
	private EditText et1,et2;
	private ProgressBar mProgressBar;
	private String user_name;
    private String pass_word;
    public volatile static String result = "00";
    private boolean Logined;
    private Boolean loginResult;
    private String PREFS_NAME = "com.example.layoutt";
    SharedPreferences settings;
    ButtonFlat bf;
    
    protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		if(Socket_Service.isConnect!=true){
		
			Intent serviceIntent = new Intent();
			serviceIntent.setClass(this, Socket_Service.class);
			startService(serviceIntent);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		settings = getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
		Logined = settings.getBoolean("Logined", false);
		
//		第一次进入初始化
	
		if(Logined==false){
			
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("Logined", true);
			editor.putString("username", "");
			editor.putString("password", "");
			editor.commit();
			
		}
		
		bt = (ButtonFlat) findViewById(R.id.button1);
		bf = (ButtonFlat) findViewById(R.id.button_goRegister);
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		et1 = (EditText) findViewById(R.id.editText1);
		et2 = (EditText) findViewById(R.id.editText2);
		
		et1.setTag(et1.getHint().toString());
		et2.setTag(et2.getHint().toString());
		
		et1.setOnFocusChangeListener(this);
        et2.setOnFocusChangeListener(this);
		
        et1.requestFocus();
        
        bt.setOnClickListener(new Button.OnClickListener() {
			
			
			public void onClick(View arg0) {
				
			  
			  
			  user_name = et1.getText().toString();
		      pass_word = et2.getText().toString();
		      
		      et1.setFocusable(true);
		      et2.setFocusable(true);
		      
		      if(user_name.equals("")){
		    	  MainActivity.this.ShowDialog("username is empty!");
		     }
		      
		      else if(pass_word.equals("")){           //判断字符串为空不能用null用""
		    	  MainActivity.this.ShowDialog("password is empty!");
		    	  
		      }
		      
		      else{
		    	  	settings = getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("username", user_name);
					editor.putString("password", pass_word);
					editor.commit();
					
					if(Socket_Service.isConnect == false){
						ShowDialog("网络连接出错，请检查网络，稍后再试");
					}
					
		    	    login();
		    	  
				
		      }
		    
			}
		});
        
        bf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, Register_UI.class);
				startActivity(intent);
				
			}
		});
		
        

	}
	
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(et1.hasFocus()){
			et1.setHint("");
			et2.setHint(et2.getTag().toString());
			Log.i("tag", "焦点在1");
		}
		
		else if(et2.hasFocus()){
			et2.setHint("");
			et1.setHint(et1.getTag().toString());
			Log.i("tag", "焦点在2");
		}
	
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!settings.getString("username", "").equals("")){
			user_name = settings.getString("username", "");
			pass_word = settings.getString("password", "");
			et1.setText(user_name);
			et2.setText(pass_word);
			
			new MyASyncTaskAutoLogin().execute();
			Log.v("Login", "begin!!");
		}
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		
//		switch(resultCode){
//		case RESULT_OK:
//			Bundle bundle = data.getExtras();
//			et1.setText(bundle.getString("user_name"));
//			et2.setText(bundle.getString("pass_word"));
//			break;
//		default:
//			break;
//		
//		}
//	}

	   private void login() {
		   bt.setVisibility(View.GONE);
		   
		   JSONObject json = new JSONObject();
	    	 
			try {
				
				json.put("user_name", user_name);
				json.put("pass_word", pass_word);
				Socket_Service.out.write("0\n");
				Socket_Service.out.flush();
				Thread.sleep(1000);
				Socket_Service.out.write(json.toString()+"\n");
				Socket_Service.out.flush();
				
				new MyASyncTaskLogin().execute();
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	   
	   public void loginResult() {                    //判断中新起一个线程将会使得UI线程崩溃
			if(loginResult){
				//绑定推送账号
				Context context =  getApplicationContext();
				XGPushManager.registerPush(context,user_name,new XGIOperateCallback() {
					
					@Override
					public void onSuccess(Object data, int flag) {
						Log.v("TPush", "注册成功，设备token为：" + data);
						
					}
					
					@Override
					public void onFail(Object data, int errCode, String msg) {
						Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
					}
				});
				
				Intent service = new Intent(context, XGPushService.class);
				startService(service);
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, Main_UI.class);
				intent.putExtra("username", user_name);
				startActivity(intent);
				finish();  //finish the activity 
				  
//						  Bundle bd = new Bundle();
//						  bd.putString("user_name", user_name);
//						  bd.putString("pass_word", pass_word);
//						  
//						  intent.putExtras(bd);
//						  startActivityForResult(intent,0);
//						  MainActivity.this.in.close();
//						  MainActivity.this.out.close();
//						  MainActivity.this.socket.close();//后面一个参数是requestcode。
				  }
				else{
				  MainActivity.this.ShowDialog("账号或密码错误，请重新输入！");
				}
	    }
				
	    
	   public void ShowDialog(String msg) {
	        new AlertDialog.Builder(this).setTitle("notification").setMessage(msg)
	                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	
	                    	bt.setVisibility(View.VISIBLE);
	                    	mProgressBar.setVisibility(View.GONE);
	                    	
							dialog.cancel();
							et1.requestFocus();
	                    }
	                }).show();
	    }

	   
	   //  AsyncTask实现耗时异步操作
	   
	   class MyASyncTaskLogin extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			mProgressBar.setVisibility(View.VISIBLE);
			Log.v("progressBar", "here!");
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			
			while(result.equals("00")){
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(result.equals("11")){
				result = "00";
				return true;
			}
			else{
				result = "00";
				return false;
				
			}
			
			
		}
		   
		   @Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			loginResult = result;
			loginResult();
		}
	   }
	   
	   
	   class MyASyncTaskAutoLogin extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... arg0) {
			int count = 0;
			while(Socket_Service.isConnect==false){
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				count++;
				if(count>20){
					
					return false;
				}
			}
			
			return true;
		}
		   
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				
				login();
			}
			else{
				
				ShowDialog("网络连接出错");
			}
		}
		   
	   }

//	@Override
//	protected void onDestroy() {
//		try {
//			socket.shutdownInput();
//			socket.shutdownOutput();
//			socket.close();
//			in.close();
//			out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	   
}
