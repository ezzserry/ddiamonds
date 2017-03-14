package com.ddiamonds;

import org.json.JSONException;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

public class ForgotPassword extends Activity{
	
	
	EditText ed_user_name, ed_password, ed_confirm_password;
	Button btn_get_new_password;
	
	private ProgressDialog progressDialog;
	AsyncHttpClient client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
		
		client = new AsyncHttpClient();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		ed_user_name = (EditText) findViewById(R.id.ed_user_name);
		ed_password = (EditText) findViewById(R.id.ed_password);
		ed_confirm_password = (EditText) findViewById(R.id.ed_confirm_password);
		
		btn_get_new_password = (Button) findViewById(R.id.btn_get_new_password);
		
		btn_get_new_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String UserName = ed_user_name.getText().toString();
				String Password = ed_password.getText().toString();
				String ConfirmPass = ed_confirm_password.getText().toString();
				
				if (UserName.equals("") || Password.equals("") || ConfirmPass.equals(""))
				{
					if (UserName.equals("")) 
					{
						showAlert(getString(R.string.please_enter_user_name));
					} 
					else if (Password.equals("")) 
					{
						showAlert(getString(R.string.please_enter_your_pass));
					} 
					else if (ConfirmPass.equals("")) 
					{
						showAlert(getString(R.string.please_enter_confirm_pass));
					}
					else if(Password.equals(ConfirmPass))
					{
						showAlert(getString(R.string.pass_and_confirm_not_matched));
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
			    	    	progressDialog = new ProgressDialog(ForgotPassword.this);
			    	        progressDialog.setMessage("Loading ...");
			    	        progressDialog.setIndeterminate(false);
			    	        progressDialog.setCancelable(false);
			    	        progressDialog.show();
			    	        
			    	        RequestParams params = new RequestParams();
			    			params.put("username", Constants.App_Username);
			    			params.put("password", Constants.App_Pass);
			    			
			    			params.put("username_login", UserName);
			    			params.put("new_password", Password);
			    			
			    			client.get(Constants.ForgetPassURL, params, new JsonHttpResponseHandler()
			    	        {
			    	        	@Override
			    	        	public void onSuccess(org.json.JSONObject arg0) 
			    	        	{
			    	        		progressDialog.dismiss();
			    	        		
			    	        		try {
			    	        				String msg = arg0.getString("msg");
			    	        				String status = arg0.getString("status");
			    	        				if(status.equals("1"))
			    	        				{
			    	        					
			    	        					AlertDialog.Builder mybuilder = new AlertDialog.Builder(ForgotPassword.this);
			    	        					mybuilder.setMessage(getString(R.string.your_new_password_saved))
			    	        					.setTitle(R.string.alert_title)
			    	        					       .setCancelable(false)
			    	        					       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    	        					           public void onClick(DialogInterface dialog, int id) {

			    	        					        	   Intent intent = new Intent(ForgotPassword.this, Login.class);
			   			    	        					   intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
			   			    	        					   startActivity(intent);
			    	        					           }
			    	        					       });
			    	        					AlertDialog myAlert = mybuilder.create();
			    	        					myAlert.show();
			    	        					
			    	        					
			    	        				}
			    	        				else
			    	        				{
			    	        					if(msg.equals("invalid parameters"))
				    	        				{
				    	        					showAlert(getString(R.string.please_enter_all_data));
				    	        				}
				    	        				else if(msg.equals("Invalid User Name"))
				    	        				{
				    	        					showAlert(getString(R.string.user_name_not_correct));
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
