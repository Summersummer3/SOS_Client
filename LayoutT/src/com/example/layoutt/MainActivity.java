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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity implements OnFocusChangeListener{

	
	private Button bt;
	private EditText et1,et2;
	private final int PORT = 8888;
	private final String IP = "192.168.1.106";
	private volatile Socket socket;
	private volatile BufferedReader in;
	private volatile BufferedWriter out;
    private String user_name;
    private String pass_word;
    
    private Thread t1 = new Thread(){
    	
    	public void run() {

    		try {
    			socket = new Socket();
    			SocketAddress socketAddress = new InetSocketAddress(IP,PORT);
    			socket.connect(socketAddress,8000);
    			
    			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    		} catch (UnknownHostException e1) {
    			MainActivity.this.ShowDialog("login exception" + e1.getMessage());
    			e1.printStackTrace();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    			MainActivity.this.ShowDialog("login exception" + e1.getMessage());
    		}
    		
    		
    	};
    	
    };
    
    private Thread t2 = new Thread(){
		

    	public void run() {
			  try {
					if(in.readLine().equals("1")){
					  Intent intent = new Intent();
					  intent.setClass(MainActivity.this, Table1.class);
					  Bundle bd = new Bundle();
					  bd.putString("user_name", user_name);
					  bd.putString("pass_word", pass_word);
					  
					  intent.putExtras(bd);
					  startActivityForResult(intent,0);
					  MainActivity.this.in.close();
					  MainActivity.this.out.close();
					  MainActivity.this.socket.close();//后面一个参数是requestcode。
					  }
					else{
						MainActivity.this.ShowDialog("username or password wrong!");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
		  	
			
		};
	  		};
	
    
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		t1.start();
		
		bt = (Button) findViewById(R.id.button1);
		
		et1 = (EditText) findViewById(R.id.editText1);
		et2 = (EditText) findViewById(R.id.editText2);
		
		et1.setTag(et1.getHint().toString());
		et2.setTag(et2.getHint().toString());
		
		et1.setOnFocusChangeListener(this);
        et2.setOnFocusChangeListener(this);
		
        et1.requestFocus();
        
		bt.setOnClickListener(new Button.OnClickListener() {
			
			
			public void onClick(View arg0) {
			  String user_name = et1.getText().toString();
		      String pass_word = et2.getText().toString();
		      
		      et1.setFocusable(true);
		      et2.setFocusable(true);
		      
		      if(user_name.equals("")){
		    	  MainActivity.this.ShowDialog("username is empty!");
		     }
		      
		      else if(pass_word.equals("")){           //判断字符串为空不能用null用""
		    	  MainActivity.this.ShowDialog("password is empty!");
		    	  
		      }
		      
		      else{
		    	  	JSONObject json = new JSONObject();
		    	 
					try {
						json.put("user_name", user_name);
						json.put("pass_word", pass_word);
						out.write(json.toString()+"\n");
						out.flush();
						MainActivity.this.t2.start();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
		      }
		    
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch(resultCode){
		case RESULT_OK:
			Bundle bundle = data.getExtras();
			et1.setText(bundle.getString("user_name"));
			et2.setText(bundle.getString("pass_word"));
			break;
		default:
			break;
		
		}
	}

	
	   public void ShowDialog(String msg) {
	        new AlertDialog.Builder(this).setTitle("notification").setMessage(msg)
	                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							et1.requestFocus();
	                    }
	                }).show();
	    }


}
