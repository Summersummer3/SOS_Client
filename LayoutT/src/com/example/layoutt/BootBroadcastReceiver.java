package com.example.layoutt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
	
	
	//����������Ϊ����������
	
	@Override
	public void onReceive(Context context, Intent arg1) {
		
		Intent serviceIntent = new Intent();
		serviceIntent.setClass(context,Socket_Service.class);
		context.startService(serviceIntent);
		Log.v("TAG", "�����Զ������Զ�����.....");  

	}

}
