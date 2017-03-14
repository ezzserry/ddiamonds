package com.ddiamonds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ProductList extends Activity{
	
	GridView gridView ;
	ImageView img_home, img_pre;	
	String brand_id;
	String cat_id;
	
	private ProgressDialog progressDialog;
	AsyncHttpClient client;
	
	List<Product> productListArr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list);
		
		Bundle extras = getIntent().getExtras();
		if (extras == null)
		{
				return;
		}
		// Get data via the key
		brand_id = extras.getString("brand_id");
		cat_id = extras.getString("cat_id");
		
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

				Intent intent = new Intent(ProductList.this, Home.class);
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
    	    	progressDialog = new ProgressDialog(ProductList.this);
    	        progressDialog.setMessage("Loading ...");
    	        progressDialog.setIndeterminate(false);
    	        progressDialog.setCancelable(false);
    	        progressDialog.show();
    	        
    	        RequestParams params = new RequestParams();
    			params.put("username", Constants.App_Username);
    			params.put("password", Constants.App_Pass);
    			
    			params.put("brand_id", brand_id);
    			params.put("cat_id", cat_id);
    			
    			client.get(Constants.ItemsURL, params, new JsonHttpResponseHandler()
    	        {
    	        	@Override
    	        	public void onSuccess(org.json.JSONObject arg0) 
    	        	{
    	        		progressDialog.dismiss();   	        		
    	        		productListArr = new ArrayList<Product>();
    	        		
    	        		try {
    	        				String status = arg0.getString("status");
    	        				
    	        				if(status.equals("1"))
    	        				{
	    	        				JSONArray response_arr = arg0.getJSONArray("response");
	    	        				
	    	        				for(int i=0; i < response_arr.length(); i++)
		        					{
		        						JSONObject item_obj = response_arr.getJSONObject(i);
		                            	
		                           	 	String id = item_obj.getString("id");
		                                String title = item_obj.getString("title");
		                                String description = item_obj.getString("description");
		                                String brand_id = item_obj.getString("brand_id");
		                                String cat_id = item_obj.getString("cat_id");
	
		                                List<String> images = new ArrayList<String>();
		                                JSONArray item_images = item_obj.getJSONArray("Images");		                               
		                                for(int x=0; x < item_images.length(); x++)
		                                {
		                                	images.add(item_images.getString(x));
		                                }
		                                
		                                Product pro = new Product(id, title, description, cat_id, brand_id, images);
		                                
		                                productListArr.add(pro);
	
		        					}
	    	        				
	    	        				gridView.setAdapter(new GridViewAdapter(ProductList.this,R.layout.product_item,productListArr));		
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
