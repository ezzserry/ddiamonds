package com.ddiamonds;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Toast;

public class SignUp extends Activity {

	EditText ed_name, ed_user_name, ed_email, ed_password, ed_confirm_password;
	Button btn_sign_up, btn_sign_in, btn_facebook;
	AsyncHttpClient client;
	private ProgressDialog progressDialog;
	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		client = new AsyncHttpClient();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		pref  = getSharedPreferences(Constants.App_Pref, 0);
		
		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_user_name = (EditText) findViewById(R.id.ed_user_name);
		ed_email = (EditText) findViewById(R.id.ed_email);
		ed_password = (EditText) findViewById(R.id.ed_password);
		ed_confirm_password = (EditText) findViewById(R.id.ed_confirm_password);

		btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
		btn_sign_in = (Button) findViewById(R.id.btn_sign_in);

		btn_sign_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String Name = ed_name.getText().toString();
				String UserName = ed_user_name.getText().toString();
				String Email = ed_email.getText().toString();
				String Password = ed_password.getText().toString();
				String ConfirmPass = ed_confirm_password.getText().toString();
				
				boolean ValidEmail = isEmailValid(Email);

				if (Name.equals("") || UserName.equals("") || Email.equals("")
						|| Password.equals("") || ConfirmPass.equals("") || !ValidEmail) {

					if (Name.equals("")) 
					{
						showAlert(getString(R.string.please_enter_your_name));
					} 
					else if (UserName.equals("")) 
					{
						showAlert(getString(R.string.please_enter_user_name));
					} 
					else if (Email.equals("")) 
					{
						showAlert(getString(R.string.please_enter_your_email));
					} 
					else if (Password.equals("")) 
					{
						showAlert(getString(R.string.please_enter_your_pass));
					} 
					else if (ConfirmPass.equals("")) 
					{
						showAlert(getString(R.string.please_enter_confirm_pass));
					}
					else if(!ValidEmail)
					{
						showAlert(getString(R.string.please_enter_valid_email));
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
			    	    	progressDialog = new ProgressDialog(SignUp.this);
			    	        progressDialog.setMessage("Loading ...");
			    	        progressDialog.setIndeterminate(false);
			    	        progressDialog.setCancelable(false);
			    	        progressDialog.show();
			    	        
			    	        RequestParams params = new RequestParams();
			    			params.put("username", Constants.App_Username);
			    			params.put("password", Constants.App_Pass);
			    			
			    			params.put("name", Name);
			    			params.put("username_login", UserName);
			    			params.put("password_login", Password);
			    			params.put("email", Email);

			    			
			    	        client.get(Constants.RegistrationURL, params, new JsonHttpResponseHandler()
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
			    	        					
			    	        					Intent intent = new Intent(SignUp.this, Home.class);
			    	        					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			    	        					startActivity(intent);
			    	        				}
		    	        				}
		    	        				else if(status.equals("0"))
		    	        				{
			    	        				String msg = arg0.getString("msg");
			    	        				if(msg.equals("Username exists"))
			    	        				{
			    	        					showAlert(getString(R.string.user_name_already_exists));
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
		
		btn_sign_in.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(SignUp.this, Login.class);
				startActivity(intent);
				
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
	
	public boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
}
