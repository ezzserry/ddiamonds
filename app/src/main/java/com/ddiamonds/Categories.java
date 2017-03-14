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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Categories extends Activity implements OnClickListener{
	
	String brand_id;
	
	private ProgressDialog progressDialog;
	AsyncHttpClient client;
	ArrayList<HashMap<String, String>> catList;
	
	LinearLayout linear_menu;
	ImageView img_home, img_pre;
	ImageView img_help, img_help_text;
	boolean isHelpShown = false;
	
	LinearLayout linear_Rings, linear_Earrings, linear_Bracletes , linear_Necklaces ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories);
		
		Bundle extras = getIntent().getExtras();
		if (extras == null)
		{
				return;
		}
		// Get data via the key
		brand_id = extras.getString("brand_id");
//		String brand_name =  extras.getString("brand_name");
		
		
//		client = new AsyncHttpClient();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		linear_menu = (LinearLayout) findViewById(R.id.linear_menu);
		img_home = (ImageView) findViewById(R.id.img_home);
		img_pre = (ImageView) findViewById(R.id.img_pre);
		
		img_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				finish();
			}
		});
		
		img_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(Categories.this, Home.class);
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
		
		linear_Rings = (LinearLayout) findViewById(R.id.linear_Rings);
		linear_Earrings = (LinearLayout) findViewById(R.id.linear_Earrings);
		linear_Bracletes = (LinearLayout) findViewById(R.id.linear_Bracletes);
		linear_Necklaces = (LinearLayout) findViewById(R.id.linear_Necklaces);
		
		linear_Rings.setOnClickListener(this);
		linear_Earrings.setOnClickListener(this);
		linear_Bracletes.setOnClickListener(this);
		linear_Necklaces.setOnClickListener(this);
		
//		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//    	NetworkInfo info = cm.getActiveNetworkInfo();
//    	
//    	if(info == null) 
//    	{
//    		showAlert(getString(R.string.check_connection));
//    	}
//    	else if(info != null) 
//    	{
//    	    if (!info.isConnected()) 
//    	    {
//    	    	showAlert(getString(R.string.check_connection));
//    	    }
//    	  //if positive, fetch the articles in background
//    	    else
//	    	{
//    	    	progressDialog = new ProgressDialog(Categories.this);
//    	        progressDialog.setMessage("Loading ...");
//    	        progressDialog.setIndeterminate(false);
//    	        progressDialog.setCancelable(false);
//    	        progressDialog.show();
//    	        
//    	        RequestParams params = new RequestParams();
//    			params.put("username", Constants.App_Username);
//    			params.put("password", Constants.App_Pass);
//    			
//    			params.put("brand_id", brand_id);
//    			
//    			client.get(Constants.CatURL, params, new JsonHttpResponseHandler()
//    	        {
//    	        	@Override
//    	        	public void onSuccess(org.json.JSONObject arg0) 
//    	        	{
//    	        		progressDialog.dismiss();
//    	        		
//    	        		catList = new ArrayList<HashMap<String, String>>();
//    	        		
//    	        		try {
//							
//	    	        			String status = arg0.getString("status");
//    	        				if(status.equals("1"))
//    	        				{
//    	        					JSONArray response_arr = arg0.getJSONArray("response");
//    	        					
//    	        					for(int i=0; i < response_arr.length(); i++)
//    	        					{
//    	        						JSONObject brand_obj = response_arr.getJSONObject(i);
//    	                            	
//    	                           	 	String cat_id = brand_obj.getString("cat_id");
//    	                                String cat_name = brand_obj.getString("cat_name");    	                                
//    	                                
//    	                                HashMap<String, String> map = new HashMap<String, String>();
//    	                                map.put("cat_id", cat_id);
//    	                                map.put("cat_name", cat_name);
//    	                              
//    	                                catList.add(map);
//
//    	        					}
//    	        					
//    	        					if(catList.size() > 0)
//    	        					{
//    	        						Log.d("brandList","brandList: "+ catList.size());
//    	        						
//    	        						for(int i=0; i < catList.size(); i++)
//    	        						{
//    	        							final HashMap<String, String> hsagcat = catList.get(i);
//    	        							String catName = hsagcat.get("cat_name");
//    	        							final String cat_id = hsagcat.get("cat_id");
//    	        							
//    	        							final LinearLayout linear_Cat = new LinearLayout(Categories.this);
//    	        	                		LinearLayout.LayoutParams linear_CatParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.FILL_PARENT);
//    	        	                		linear_Cat.setLayoutParams(linear_CatParams);
//    	        	                		
//    	        	                		ImageView cat_image = new ImageView(Categories.this);
//    	        	                		LinearLayout.LayoutParams cat_imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//    	        	                		cat_imageParams.setMargins(15, 30, 15, 30);
//    	        	                		cat_image.setLayoutParams(cat_imageParams);
////    	        	                		cat_image.setImageDrawable(getResources().getDrawable(R.drawable.pinkrose_icon));
//    	        	                		
//    	        	                		cat_image.setImageResource(getResources().getIdentifier(catName.toLowerCase(),"drawable","com.ddiamonds"));
//
//    	        	                		
//    	        	                		linear_Cat.addView(cat_image);
//    	        	                		linear_menu.addView(linear_Cat);
//    	        	                		
//    	        	                		linear_Cat.setOnClickListener(new OnClickListener() {
//												
//												@Override
//												public void onClick(View v) { 
//													
//													linear_Cat.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
//
//													Intent in = new Intent(getApplicationContext(), ProductList.class);
//						                            in.putExtra("cat_id", cat_id);
//						                            in.putExtra("brand_id", brand_id);
//						                            startActivity(in);
//													
//												}
//											});
//    	        	                		
//    	        						}
//    	        					}
//    	        				}
//    	        				else
//    	        				{
//    	        					
//    	        				}
//    	        		}catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//    	        	}
//    	        	
//    	        	@Override
//    	        	public void onFailure(Throwable arg0, org.json.JSONObject arg1) 
//    	        	{
//    	        		progressDialog.dismiss();
//    	        	};
//    	        	
//    	        	@Override
//    	        	public void onFailure(Throwable arg0, String arg1) 
//    	        	{
//    	        		progressDialog.dismiss();
//    	        	};
//    	        });
//	    	}
//    	}

	}
	
	@Override
	public void onClick(View v) 
	{
		int cat_id = 0;
		
		if(v == linear_Rings)
		{
			cat_id = Constants.catRings;
			linear_Rings.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
			linear_Earrings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Bracletes.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Necklaces.setBackgroundColor(getResources().getColor(android.R.color.transparent));

			
		}
		else if(v == linear_Earrings)
		{
			cat_id = Constants.catEarrings;
			linear_Earrings.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
			linear_Rings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Bracletes.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Necklaces.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		}
		else if(v == linear_Bracletes)
		{
			cat_id = Constants.catBracletes;
			linear_Bracletes.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
			linear_Rings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Earrings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Necklaces.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		}
		else if(v == linear_Necklaces)
		{
			cat_id = Constants.catNecklaces;
			linear_Necklaces.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
			linear_Rings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Earrings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Bracletes.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		}
		/*else if(v == linear_Pendants)
		{
			cat_id = Constants.catPendants;
			linear_Pendants.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
			linear_Rings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Earrings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Bracletes.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Wedding_Rings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Twins.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		}
		else if(v == linear_Twins)
		{
			cat_id = Constants.catTwins;
			linear_Twins.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
			linear_Rings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Earrings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Pendants.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Wedding_Rings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Bracletes.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		}
		else if(v == linear_Wedding_Rings)
		{
			cat_id = Constants.catWeddingRings;
			linear_Wedding_Rings.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
			linear_Rings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Earrings.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Pendants.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Bracletes.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			linear_Twins.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		}*/
		
		Intent in = new Intent(getApplicationContext(), ProductList.class);
        in.putExtra("cat_id", String.valueOf(cat_id));
        in.putExtra("brand_id", brand_id);
        startActivity(in);
			
		
	}
	
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
