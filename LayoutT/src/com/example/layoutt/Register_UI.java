package com.example.layoutt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLInput;
import java.util.ArrayList;
import java.util.List;














import org.json.JSONObject;

import com.gc.materialdesign.views.ButtonFlat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class Register_UI extends Activity {
	
	private EditText ed1,ed2,ed3,ed4;
	private Spinner sp1,sp2;
	private ButtonFlat bf;
	private String username;
	private String password;
	private String teleNumber;
	private String PREFS_NAME = "com.example.layoutt";
	
	public volatile static String result = "0";
	
	private File f;
	
	private List<String> proset=new ArrayList<String>();//省份集合
	private List<String> citset=new ArrayList<String>();//城市集合
	
	private int pro_id;
	
	private String TagCity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		init();
	}

	private void init() {
		ed1 = (EditText) findViewById(R.id.editText__registerName);
		ed2 = (EditText) findViewById(R.id.editText_registerPassword);
		ed3 = (EditText) findViewById(R.id.editText_registerCommitPassword);
		ed4 = (EditText) findViewById(R.id.editText_registerPhoneNumber);
		
		f = getDBFile();
		
		sp1 = (Spinner) findViewById(R.id.spinner_registerProSelect);
		ArrayAdapter<String> pro_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getProSet());
		sp1.setAdapter(pro_adapter);
		sp1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position,
					long id) {
				pro_id = position;
				sp2.setAdapter(getCityAdapter());
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		sp2 = (Spinner) findViewById(R.id.spinner_registerCitySelect);
		sp2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				TagCity = parent.getItemAtPosition(position).toString();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		bf = (ButtonFlat) findViewById(R.id.button_register);
		bf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				username = ed1.getText().toString();
				password = ed2.getText().toString();
				String password_Commit = ed3.getText().toString();
				teleNumber = ed4.getText().toString();
				//判断两次密码是否一致
				if(!password.equals(password_Commit)){
					Register_UI.this.ShowDialog("两次输入的密码不一致，请重新数入");
				}
				//判断输入电话号码格式是否正确
				else if(teleNumber.length()!=11 || !(teleNumber.startsWith("13")||
						teleNumber.startsWith("15")||teleNumber.startsWith("18")))
				{
					Register_UI.this.ShowDialog("预设号码格式错误，请重新输入");
					
				}else if(username.equals("")||password.equals("")||teleNumber.equals("")){
					
					Register_UI.this.ShowDialog("输入不得为空，请重新输入");
					
				}else{
				
				register();
				}
			}

			
		});
		
			
	}
	
	@SuppressWarnings("finally")
	private File getDBFile(){
		File file = new File(getFilesDir(),"db_weather.db");
		if(file.exists()){
			return file;
		}
		else{
			try {
				InputStream is = getResources().getAssets().open("db_weather.db");
				FileOutputStream fos = new FileOutputStream(file);
				//输出流进行复制文件的方法！！！ 应记住
				
				byte[] bt = new byte[8192];
				int len = -1;
				while((len = is.read(bt)) != -1){
				    fos.write(bt, 0, len);
				}
				
				is.close();
				fos.close();
				return file;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				return file;
			}
			
			
			
		}
		
		
		
	}
	
	private SpinnerAdapter getCityAdapter() {
		ArrayAdapter<String> city_adapter = new ArrayAdapter<String>
		(this, android.R.layout.simple_spinner_item,getCitySet(pro_id));
		return city_adapter;
	}
	
	private List<String> getCitySet(int pro_id) {
		citset.clear();
		SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(f, null);
		Cursor cursor = db1.query("citys", null, "province_id="+pro_id, null, null, null, null);
		while(cursor.moveToNext()){
			citset.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
		}
		cursor.close();
		db1.close();
		return citset;
		
	}

	private List<String> getProSet() {
		
		SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(f, null);
		Cursor cursor = db1.query("provinces", null, null, null, null, null, null);
		
		while(cursor.moveToNext()){
			proset.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
		}
		cursor.close();
		db1.close();
		return proset;
		
	}
	   public void ShowDialog(String msg) {
	        new AlertDialog.Builder(this).setTitle("notification").setMessage(msg)
	                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							ed3.requestFocus();
	                    }
	                }).show();
	    }
	   
	   private void register() {
			JSONObject json = new JSONObject();
			try {
				json.put("username",username);
				json.put("password",password);
				json.put("TAG",TagCity);
				Log.v("city", TagCity);
				Socket_Service.out.write("2\n");
				Socket_Service.out.flush();
				Thread.sleep(1000);
				Socket_Service.out.write(json.toString() + "\n");
				Socket_Service.out.flush();
				Thread.sleep(1000);
				registerResult();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	   private void registerResult() {
		   if(result.equals("2")){
			   SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
			   SharedPreferences.Editor editor = setting.edit();
			   editor.putString("Telephone" + username, teleNumber);
			   editor.commit();
			  
			   new AlertDialog.Builder(this).setTitle("注册成功")//
			   				  .setMessage("恭喜您，注册成功,请完善您的求救人账号名单！")
			   				  .setPositiveButton("ok",new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									Intent intent = new Intent();
									intent.setClass(Register_UI.this, Register_UI_2.class);
									startActivity(intent);
									finish();
								}
							}).show();
			  
			  
		 }
		   else{
			   
			   ShowDialog("注册失败，用户名已被注册或者网络连接出错，请重新尝试！");
		   }
		}
}
