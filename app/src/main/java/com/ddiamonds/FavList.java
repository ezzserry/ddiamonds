package com.ddiamonds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FavList extends Activity {

	GridView gridView;
	ImageView img_home, img_pre;

	private ProgressDialog progressDialog;
	AsyncHttpClient client;

	List<Product> productListArr;
	
	GridViewAdapterFav adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fav_list);
		
		client = new AsyncHttpClient();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		gridView = (GridView) findViewById(R.id.grid_view);
		
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

				Intent intent = new Intent(FavList.this, Home.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
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
    	    	progressDialog = new ProgressDialog(FavList.this);
    	        progressDialog.setMessage("Loading ...");
    	        progressDialog.setIndeterminate(false);
    	        progressDialog.setCancelable(false);
    	        progressDialog.show();
    	        
    	        SharedPreferences pref  = getSharedPreferences(Constants.App_Pref, 0);
    	        String UserID = String.valueOf(pref.getInt(Constants.App_Pref_UserID, 0)) ;
    	        String DeviceID = Secure.getString(FavList.this.getContentResolver(),Secure.ANDROID_ID);
    	        
    	        RequestParams params = new RequestParams();
    			params.put("username", Constants.App_Username);
    			params.put("password", Constants.App_Pass);
    			params.put("action", "view");
    			params.put("user_id", UserID);
    			params.put("phone_identifer", DeviceID);
    			params.put("phone_type", "android");
    			
    			client.get(Constants.FavURL, params, new JsonHttpResponseHandler()
    	        {
    	        	@Override
    	        	public void onSuccess(org.json.JSONObject arg0) 
    	        	{
    	        		productListArr = new ArrayList<Product>();
    	        		progressDialog.dismiss();
    	        		
    	        		try {
	        					String status = arg0.getString("status");
	        					if(status.equals("1"))
    	        				{
	        						JSONObject response = arg0.getJSONObject("response");
	        						JSONArray response_arr = response.getJSONArray("favorites");
	        						
	        						for(int i=0; i < response_arr.length(); i++)
		        					{
		        						JSONObject item_obj = response_arr.getJSONObject(i);
		                            	
		                           	 	String id = item_obj.getString("item_id");
		                                String title = item_obj.getString("title");
		                                String description = item_obj.getString("description");
		                                String brand_id = item_obj.getString("brand_id");
		                                String cat_id = item_obj.getString("cat_id");
		                                String fav_id = item_obj.getString("fav_id");
	
		                                List<String> images = new ArrayList<String>();
		                                JSONArray item_images = item_obj.getJSONArray("images");		                               
		                                for(int x=0; x < item_images.length(); x++)
		                                {
		                                	images.add(item_images.getString(x));
		                                }
		                                
		                                Product pro = new Product(fav_id, title, description, cat_id, brand_id, images);
		                                
		                                productListArr.add(pro);
	
		        					}
	        						
	        						adapter = new GridViewAdapterFav(FavList.this,R.layout.fav_item,productListArr);
	        						gridView.setAdapter(adapter);		
	    	        				gridView.setOnItemClickListener(new OnItemClickListener() {

	    	                			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
	    	                				
	    	                				Intent in = new Intent(getApplicationContext(), ProductDetails.class);
	    	                				in.putExtra("Product_Obj", productListArr.get(position));
	    	                				startActivity(in);
	    	                			}

	    	        				});
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
	
	public void removeFav(View v) {

		final View myView = v;
		final Product selectedFav = productListArr.get((Integer) myView.getTag());
		
		 new AlertDialog.Builder(FavList.this)
	     .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(getString(R.string.app_name))
	        .setMessage(getString(R.string.are_u_sure_to_delete_from_fav))
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	progressDialog = new ProgressDialog(FavList.this);
	    	        progressDialog.setMessage("Loading ...");
	    	        progressDialog.setIndeterminate(false);
	    	        progressDialog.setCancelable(false);
	    	        progressDialog.show();
	            	
	            	final int tag2 = (Integer) myView.getTag();
	            	
	            	RequestParams params = new RequestParams();
	     			params.put("username", Constants.App_Username);
	     			params.put("password", Constants.App_Pass);
	     			params.put("action", "delete");
	     			params.put("fav_id", selectedFav.getId());
	     			
	            	client.get(Constants.FavURL, params, new AsyncHttpResponseHandler()
	            	{
	            		@Override
	            		public void onSuccess(String arg0) {
	            			// TODO Auto-generated method stub
	            			super.onSuccess(arg0);
	            			
	            			progressDialog.dismiss();
	            			productListArr.remove(tag2);
	    	            	adapter.notifyDataSetChanged();
	            		}
	            		
	            	
	            		
	            		@Override
	            		public void onFailure(Throwable arg0, String arg1) {
	            			// TODO Auto-generated method stub
	            			super.onFailure(arg0, arg1);
	            			progressDialog.dismiss();
	            			Toast.makeText(getApplicationContext(), R.string.fave_fail_add, Toast.LENGTH_LONG).show();
	            		}
	            	});

	            }

	        })
	        .setNegativeButton("No", null)
	        .show();

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

}
