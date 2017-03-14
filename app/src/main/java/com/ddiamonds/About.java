package com.ddiamonds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class About extends FragmentActivity {

	private ProgressDialog progressDialog;
	AsyncHttpClient client;
	ImageView img_home;
	TextView txt_title, txt_desc;

	ProductImageAdapter pageAdapter;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		client = new AsyncHttpClient();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		img_home = (ImageView) findViewById(R.id.img_home);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_desc = (TextView) findViewById(R.id.txt_desc);
		txt_desc.setMovementMethod(new ScrollingMovementMethod());

		img_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(About.this, Home.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();

		if (info == null) {
			showAlert(getString(R.string.check_connection));
		} else if (info != null) {
			if (!info.isConnected()) {
				showAlert(getString(R.string.check_connection));
			}
			//if positive, fetch the articles in background
			else {
				progressDialog = new ProgressDialog(About.this);
				progressDialog.setMessage("Loading ...");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();

				RequestParams params = new RequestParams();
				params.put("username", Constants.App_Username);
				params.put("password", Constants.App_Pass);

				client.get(Constants.AboutURL, params, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject arg0) {
						progressDialog.dismiss();


						try {

							String status = arg0.getString("status");
							if (status.equals("1")) {
								JSONObject response = arg0.getJSONObject("response");
								String title = response.getString("title");
								String description = response.getString("description");
								JSONArray images = response.getJSONArray("images");

//    	        					Log.i("About","title: "+ title + " ,description: "+ description + " ,images: "+ images.length());


								List<Fragment> fragments = new ArrayList<Fragment>();

								for (int i = 0; i < images.length(); i++) {
									fragments.add(ProductFragment.newInstance(images.getString(i)));
								}

								pageAdapter = new ProductImageAdapter(getSupportFragmentManager(), fragments);
								ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
								pager.setAdapter(pageAdapter);

								txt_title.setText(title);
								txt_desc.setText(description);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable arg0, JSONObject arg1) {
						progressDialog.dismiss();
					}

					;

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						progressDialog.dismiss();
					}

					;
				});
			}
		}
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	public void showAlert(String Message) {
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

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client2.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"About Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.ddiamonds/http/host/path")
		);
		AppIndex.AppIndexApi.start(client2, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"About Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.ddiamonds/http/host/path")
		);
		AppIndex.AppIndexApi.end(client2, viewAction);
		client2.disconnect();
	}
}
