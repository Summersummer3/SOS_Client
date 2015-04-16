package com.example.layoutt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Socket_Service extends Service {

	private final int PORT = 8888;
	private final String IP = "192.168.1.106";
	volatile static Socket socket = null;   //该socket需要写成静态参数供多条线程同时调用
	private final String TAG = "Socket Service";
	public volatile static BufferedReader in;
	public volatile static BufferedWriter out;
	
	protected volatile static boolean isConnect = false;
	@Override
	public void onCreate() {
		Log.v(TAG, "service creates");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.v(TAG, "service starts");
		new Thread(){
			private String result;

			@Override
			public void run() {
				if(isConnect==false){
					
					initSocket();
				
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
			isConnect = true;
			
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		    new Thread(){
		    	
		    	public void run() {
		    		try {
		    			Log.v("test","thread_start");
		    			
		    			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    			while(!(MainActivity.result = Socket_Service.in.readLine()).equals("1")){
		    				Log.v("test",MainActivity.result);
		    			}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		
		    	};
		    }.start();
			Log.v(TAG, "instream and outstream is ready!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
