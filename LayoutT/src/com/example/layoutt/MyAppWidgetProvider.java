package com.example.layoutt;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("this!", "Receive");
		if(intent.getAction().equals("Call110")){
			Log.v("widget", "ReceiveAction1");
			Intent dial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+110));
			
			dial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在非activity类中使用intent必须加上一个flag 
			
			Toast.makeText(context.getApplicationContext(), "拨打110", 0).show();
			context.startActivity(dial);
			
		}
		
		if(intent.getAction().equals("Call119")){
			Log.v("widget", "ReceiveAction2");
			Intent dial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+119));
			dial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Toast.makeText(context.getApplicationContext(), "拨打119", 0).show();
			
			context.startActivity(dial);
		}
		
		if(intent.getAction().equals("Call120")){
			Log.v("widget", "ReceiveAction3");
			Intent dial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+120));
			dial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Toast.makeText(context.getApplicationContext(), "拨打120", 0).show();
			context.startActivity(dial);
		}
		
		if(intent.getAction().equals("CallUser")){
			Log.v("widget", "ReceiveAction4");
			Intent dial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+Main_UI.getTel()));
			dial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Toast.makeText(context.getApplicationContext(), "拨打"+Main_UI.getTel(), 0).show();
			context.startActivity(dial);
			
		}
		
		if(intent.getAction().equals("Location")){
			Log.v("widget", "ReceiveAction5");
			Main_UI.ib3.performClick();
			Intent intent_To_UI = new Intent();
			intent_To_UI .setClass(context, Main_UI.class);
			intent_To_UI.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent_To_UI);
		}
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.v("this!", "Update");
		int N = appWidgetIds.length;
		for(int i=0;i<N;i++){
			
			int appWidgetID = appWidgetIds[i];
			
			Intent btIntent_1 = new Intent("Call110");
			Intent btIntent_2 = new Intent("Call119");
			Intent btIntent_3 = new Intent("Call120");
			Intent btIntent_4 = new Intent("CallUser");
			Intent btIntent_5 = new Intent("Location");
			
			PendingIntent pI_1 = PendingIntent.getBroadcast(context, 0, btIntent_1, 0);
			PendingIntent pI_2 = PendingIntent.getBroadcast(context, 0, btIntent_2, 0);
			PendingIntent pI_3 = PendingIntent.getBroadcast(context, 0, btIntent_3, 0);
			PendingIntent pI_4 = PendingIntent.getBroadcast(context, 0, btIntent_4, 0);
			PendingIntent pI_5 = PendingIntent.getBroadcast(context, 0, btIntent_5, 0);
			
			RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);
			
			mRemoteViews.setOnClickPendingIntent(R.id.imageButton_appwidget_1, pI_1);
			mRemoteViews.setOnClickPendingIntent(R.id.imageButton_appwidget_2, pI_2);
			mRemoteViews.setOnClickPendingIntent(R.id.imageButton_appwidget_3, pI_3);
			mRemoteViews.setOnClickPendingIntent(R.id.imageButton_appwidget_4, pI_4);
			mRemoteViews.setOnClickPendingIntent(R.id.imageButton_appwidget_5, pI_5);
			
			AppWidgetManager myAppWidgetManager = AppWidgetManager.getInstance(context);
	        myAppWidgetManager.updateAppWidget(appWidgetID, mRemoteViews);
			
			
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	

}
