package com.example.layoutt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class Socket_Service extends Service {

	private final int PORT = 8000;
	private final String IP = "192.168.23.1";
	volatile static Socket socket = null;   //该socket需要写成静态参数供多条线程同时调用
	private final String TAG = "Socket Service";
	private String PREFS_NAME = "com.example.layoutt";
	public volatile static BufferedReader in;
	public volatile static BufferedWriter out;
	private String result = "";
	
	protected volatile static boolean isConnect = false;
	@Override
	public void onCreate() {
		Log.v(TAG, "service creates");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.v(TAG, "service starts");
		new Thread(){
			

			@Override
			public void run() {
				if(isConnect==false){
					
					initSocket();
					
					SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
					String user_name = setting.getString("username", "");
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
				
					}
			 }
		}.start();
		
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void initSocket(){
		try {
			socket = new Socket();
			SocketAddress mSocketAddress = new InetSocketAddress(IP, PORT);
			Log.v(TAG, "socket connect start!");
			socket.connect(mSocketAddress,10000);
			Log.v(TAG, "socket connect success!");
			
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"GBK"));
		    new Thread(){
		    	
		    	public void run() {
		    		try {
		    			Log.v("test","thread_start");
		    			
		    			in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));
		    			while((result = Socket_Service.in.readLine())!=null){
		    				if(result.equals("1")){
		    					MainActivity.result = result;
		    				}
		    				if(result.equals("2")){
		    					Register_UI.result = result;
		    				}
		    				if(result.equals("11")||result.equals("12")||result.equals("13")||result.equals("3")){
		    					Register_UI_2.result = result;
		    				}
		    			}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		
		    	};
		    }.start();
		    
			Log.v(TAG, "instream and outstream is ready!");
			isConnect = true;
			
			
		} catch (IOException e) {
			isConnect = false;
			Log.v("error", "连接断开...");
			
			e.printStackTrace();
		}
	}
	
  @Override
  	public void onDestroy() {
	  
	  super.onDestroy();
	  try {
		  in.close();
		  out.close();
		  socket.shutdownOutput();
		  socket.close();
		  Log.v("destroy", "here!");
		  
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  initSocket();
	  
	  
  	}
}
