package com.example.layoutt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
	
	
	//将服务设置为开机自启动
	
	@Override
	public void onReceive(Context context, Intent arg1) {
		
		Intent serviceIntent = new Intent();
		serviceIntent.setClass(context,Socket_Service.class);
		context.startService(serviceIntent);
		Log.v("TAG", "开机自动服务自动启动.....");  

	}

}
