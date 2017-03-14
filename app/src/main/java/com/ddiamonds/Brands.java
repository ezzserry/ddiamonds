package com.ddiamonds;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
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
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Brands extends Activity {

	private ProgressDialog progressDialog;
	AsyncHttpClient client;
	ArrayList<HashMap<String, String>> brandList;

	LinearLayout linear_menu;
	ImageView img_home;
	ImageView img_help, img_help_text;
	boolean isHelpShown = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brands);

		client = new AsyncHttpClient();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		linear_menu = (LinearLayout) findViewById(R.id.linear_menu);
		img_home = (ImageView) findViewById(R.id.img_home);

		img_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(Brands.this, Home.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});

		img_help = (ImageView) findViewById(R.id.img_help);
		img_help_text = (ImageView) findViewById(R.id.img_help_text);

		img_help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if(!isHelpShown)
				{
					img_help_text.setVisibility(View.VISIBLE);
				}
				else
				{
					img_help_text.setVisibility(View.GONE);
				}

				isHelpShown = !isHelpShown;
			}
		});

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
				progressDialog = new ProgressDialog(Brands.this);
				progressDialog.setMessage("Loading ...");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();

				RequestParams params = new RequestParams();
				params.put("username", Constants.App_Username);
				params.put("password", Constants.App_Pass);

				client.get(Constants.BrandsURL, params, new JsonHttpResponseHandler()
				{
					@Override
					public void onSuccess(org.json.JSONObject arg0)
					{
						progressDialog.dismiss();

						brandList = new ArrayList<HashMap<String, String>>();

						try {

							String status = arg0.getString("status");
							if(status.equals("1"))
							{
								JSONArray response_arr = arg0.getJSONArray("response");

								for(int i=0; i < response_arr.length(); i++)
								{
									JSONObject brand_obj = response_arr.getJSONObject(i);

									String brand_id = brand_obj.getString("brand_id");
									String brand_name = brand_obj.getString("brand_name");
									String brand_logo = brand_obj.getString("brand_logo");
//    	                                String address = branche_obj.getString("address");


									HashMap<String, String> map = new HashMap<String, String>();
									map.put("brand_id", brand_id);
									map.put("brand_name", brand_name);
									map.put("brand_logo", brand_logo);
//    	                                map.put("address", address);

									brandList.add(map);

								}

								if(brandList.size() > 0)
								{
									Log.d("brandList","brandList: "+ brandList.size());

									for(int i=0; i < brandList.size(); i++)
									{
										final HashMap<String, String> hsagBrand = brandList.get(i);

										final LinearLayout linear_Brand = new LinearLayout(Brands.this);
										LinearLayout.LayoutParams linear_BrandParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.FILL_PARENT);
										linear_Brand.setLayoutParams(linear_BrandParams);

										ImageView brand_image = new ImageView(Brands.this);
										//brand_image.setMinimumWidth(125);

										//brand_image.setMinimumHeight(50);

										LinearLayout.LayoutParams brand_imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
										 //LinearLayout.LayoutParams brand_imageParams = new LinearLayout.LayoutParams(300,100);
										brand_imageParams.setMargins(30, 80, 30, 80);
										if(hsagBrand.get("brand_name").equals("PINK&ROSE")) {
											brand_image.setImageDrawable(getResources().getDrawable(R.drawable.pink_rose));
											linear_Brand.addView(brand_image);
										}else if(hsagBrand.get("brand_name").equals("Unica")) {
											brand_image.setImageDrawable(getResources().getDrawable(R.drawable.unica));
											linear_Brand.addView(brand_image);
										}else if(hsagBrand.get("brand_name").equals("Nile Diamond")) {
											brand_image.setImageDrawable(getResources().getDrawable(R.drawable.nile_diamond));
											linear_Brand.addView(brand_image);
										}else if(hsagBrand.get("brand_name").equals("LOVERS")) {
											brand_image.setImageDrawable(getResources().getDrawable(R.drawable.lovers));
											linear_Brand.addView(brand_image);
										}else{
											TextView brand_text = new TextView(Brands.this);
											LinearLayout.LayoutParams brand_TxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
											brand_TxtParams.setMargins(20, 60, 20, 60);
											brand_text.setLayoutParams(brand_TxtParams);
											brand_text.setText(hsagBrand.get("brand_name"));
											brand_text.setTextSize(getResources().getDimension(R.dimen.brand_name_text_size));
											brand_text.setTypeface(Typeface.DEFAULT_BOLD);

											linear_Brand.addView(brand_text);
										}

										linear_menu.addView(linear_Brand);

										linear_Brand.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {

												linear_Brand.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));

												Intent in = new Intent(getApplicationContext(), Categories.class);
												in.putExtra("brand_id", hsagBrand.get("brand_id"));
												in.putExtra("brand_name", hsagBrand.get("brand_name"));
												startActivity(in);
											}
										});

									}
								}

							}
							else
							{

							}

						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

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


	/*public void loadBitmap(ImageView img,String url) {

		try{
			Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
			img.setImageBitmap(bitmap);
		} catch (MalformedURLException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
	}*/

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if(linear_menu != null)
		{
			for(int i=0; i < linear_menu.getChildCount(); i++)
			{
				View linear_Brand = linear_menu.getChildAt(i);
				linear_Brand.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			}
		}
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
