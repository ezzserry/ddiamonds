package com.ddiamonds;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class ContactUS extends Activity {

	ImageView btn_send;
	EditText ed_name, ed_email, ed_phone, ed_subject, ed_message;
	ImageView img_home;
	AsyncHttpClient client;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_us);

		client = new AsyncHttpClient();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		img_home = (ImageView) findViewById(R.id.img_home);
		btn_send = (ImageView) findViewById(R.id.btn_send);
		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_email = (EditText) findViewById(R.id.ed_email);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		ed_subject = (EditText) findViewById(R.id.ed_subject);
		ed_message = (EditText) findViewById(R.id.ed_message);

		img_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(ContactUS.this, Home.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
			}
		});

		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String Name = ed_name.getText().toString();
				String Phone = ed_phone.getText().toString();
				String Email = ed_email.getText().toString();
				String Subject = ed_subject.getText().toString();
				String Message = ed_message.getText().toString();

				boolean ValidEmail = isEmailValid(Email);

				if (Name.equals("") || Phone.equals("") || Email.equals("")
						|| Subject.equals("") || Message.equals("")
						|| !ValidEmail) {

					if (Name.equals("")) {
						showAlert(getString(R.string.please_enter_name));
					} else if (Phone.equals("")) {
						showAlert(getString(R.string.please_enter_phone));
					} else if (Email.equals("")) {
						showAlert(getString(R.string.please_enter_email));
					} else if (Subject.equals("")) {
						showAlert(getString(R.string.please_enter_subject));
					} else if (Message.equals("")) {
						showAlert(getString(R.string.please_enter_message));
					} else if (!ValidEmail) {
						showAlert(getString(R.string.please_enter_valid_email));
					}
				} else {
					ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo info = cm.getActiveNetworkInfo();

					if (info == null) {
						showAlert(getString(R.string.check_connection));
					} else if (info != null) {
						if (!info.isConnected()) {
							showAlert(getString(R.string.check_connection));
						}

						// if positive, fetch the articles in background
						else {
							progressDialog = new ProgressDialog(ContactUS.this);
							progressDialog.setMessage("Loading ...");
							progressDialog.setIndeterminate(false);
							progressDialog.setCancelable(false);
							progressDialog.show();

							RequestParams params = new RequestParams();

							params.put("name", Name);
							params.put("email", Email);
							params.put("phone", Phone);
							params.put("subject", Subject);
							params.put("msg", Message);
							params.put("username", Constants.App_Username);
			    			params.put("password", Constants.App_Pass);

							client.get(Constants.ContactURL, params,new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(org.json.JSONObject arg0)
								{
									progressDialog.dismiss();
									
									String status;
									try {
										status = arg0.getString("status");
									
									
										if(status.equals("1"))
										{
											ed_name.setText("");
											ed_phone.setText("");
											ed_email.setText("");
											ed_subject.setText("");
											ed_message.setText("");
											
											showAlert(getString(R.string.message_sent_successfully));
										}
										else
										{
											showAlert(getString(R.string.something_wrong));
										}
									
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

								public void onFailure(Throwable arg0,org.json.JSONArray arg1) {

									progressDialog.dismiss();
								};

								@Override
								public void onFailure(Throwable arg0,String arg1) {
									
									progressDialog.dismiss();
								};
							});

						}
					}
				}

			}
		});
	}

	public void showAlert(String Message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Message).setTitle(R.string.alert_title)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do things
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
