package com.ddiamonds;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

public class Login extends Activity{
	
	TextView txt_forgetpass,txt_create_account;
	EditText ed_user_name, ed_password;
	Button btn_sign_in;
	
	private ProgressDialog progressDialog;
	AsyncHttpClient client;
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		client = new AsyncHttpClient();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		pref  = getSharedPreferences(Constants.App_Pref, 0);
		
		txt_forgetpass = (TextView) findViewById(R.id.txt_forgetpass);
		txt_create_account = (TextView) findViewById(R.id.txt_create_account);
		ed_user_name = (EditText) findViewById(R.id.ed_user_name);
		ed_password = (EditText) findViewById(R.id.ed_password);
		btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
		
		txt_create_account.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Login.this, SignUp.class);
				startActivity(intent);
			}
		});
		
		txt_forgetpass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login.this, ForgotPassword.class);
				startActivity(intent);
				
			}
		});
		
		btn_sign_in.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String UserName = ed_user_name.getText().toString();
				String Password = ed_password.getText().toString();
				
				if (UserName.equals("") || Password.equals("")) 
				{
					if (UserName.equals("")) 
					{
						showAlert(getString(R.string.please_enter_user_name));
					} 
					else if (Password.equals("")) 
					{
						showAlert(getString(R.string.please_enter_your_pass));
					} 
				}
				
				else
				{
					ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    	NetworkInfo info = cm.getActiveNetworkInfo();
			    	
			    	if(info == null) 
			    	{
			    		showAlert(getString(R.string.check_connection));
			    	}
			    	else if(info != null) 
			    	{
			    	    if (!info.isConnected()) 
			    	    {
			    	    	showAlert(getString(R.string.check_connection));
			    	    }
			    	    
			    	    //if positive, fetch the articles in background
			    	    else
		    	    	{
			    	    	progressDialog = new ProgressDialog(Login.this);
			    	        progressDialog.setMessage("Loading ...");
			    	        progressDialog.setIndeterminate(false);
			    	        progressDialog.setCancelable(false);
			    	        progressDialog.show();
			    	        
			    	        RequestParams params = new RequestParams();
			    			params.put("username", Constants.App_Username);
			    			params.put("password", Constants.App_Pass);

			    			params.put("username_login", UserName);
			    			params.put("password_login", Password);
			    			
			    			client.get(Constants.LoginURL, params, new JsonHttpResponseHandler()
			    	        {
			    	        	@Override
			    	        	public void onSuccess(org.json.JSONObject arg0) 
			    	        	{
			    	        		progressDialog.dismiss();
			    	        		
			    	        		try {
										
				    	        			String status = arg0.getString("status");
			    	        				if(status.equals("1"))
			    	        				{
			    	        					String response = arg0.getString("response");
			    	        					if(TextUtils.isDigitsOnly(response))
				    	        				{
				    	        					SharedPreferences.Editor editor = pref.edit();
				    	        					editor.putInt(Constants.App_Pref_UserID, Integer.parseInt(response));
				    	        					editor.commit();
				    	        					
				    	        					Intent intent = new Intent(Login.this, Home.class);
				    	        					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				    	        					startActivity(intent);
				    	        				}
			    	        				}
			    	        				else
			    	        				{
			    	        					String msg = arg0.getString("msg");
			    	        					if(msg.equals("incorrect"))
				    	        				{
				    	        					showAlert(getString(R.string.wrong_pass_or_user_name));
				    	        				}
				    	        				else if(msg.equals("invalid parameters"))
				    	        				{
				    	        					showAlert(getString(R.string.please_enter_all_data));
				    	        				}
			    	        				}
										
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
			    	        		
			    	        	};
			    	        	
			    	        	@Override
			    	        	public void onFailure(Throwable arg0, org.json.JSONObject arg1) 
			    	        	{
			    	        		progressDialog.dismiss();
			    	        	};
			    	        	
			    	        	@Override
			    	        	public void onFailure(Throwable arg0, String arg1) 
			    	        	{

									progressDialog.dismiss();
			    	        	};

			    	        });

		    	    	}
			    	}
				}
				
			}
		});
		
		
	}
	
	public void showAlert(String Message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Message)
		.setTitle(R.string.alert_title)
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}

}
