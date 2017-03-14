package com.ddiamonds;

import java.util.ArrayList;
import java.util.List;

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
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetails extends FragmentActivity {
	
	Product product;
	ImageView img_home, img_pre;
	ImageView img_help, img_help_text;
	boolean isHelpShown = false;
	TextView txt_productName;
	
	TextView txt_fav, txt_try;
	
	ProductImageAdapter pageAdapter;
	
	private ProgressDialog progressDialog;
	AsyncHttpClient client;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_details);
		
		client = new AsyncHttpClient();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		Bundle extras = getIntent().getExtras();
		if (extras == null)
		{
				return;
		}
		
		product = (Product) getIntent().getSerializableExtra("Product_Obj");
		
		img_home = (ImageView) findViewById(R.id.img_home);
		img_pre = (ImageView) findViewById(R.id.img_pre);
		txt_productName = (TextView) findViewById(R.id.txt_productName);
		img_help = (ImageView) findViewById(R.id.img_help);
		img_help_text = (ImageView) findViewById(R.id.img_help_text);
		
		txt_fav = (TextView) findViewById(R.id.txt_fav);
		txt_try = (TextView) findViewById(R.id.txt_try);
		
		txt_try.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(ProductDetails.this, TryIt.class);
				List<String> all_images = product.getImages();
				intent.putExtra("product_url", all_images.get(0));
				startActivity(intent);
				
			}
		});
		
		img_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				finish();
			}
		});
		
		img_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(ProductDetails.this, Home.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
			}
		});
		
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
		
		txt_productName.setText(product.getTitle());
		
		List<Fragment> fragments = getFragments();
	    pageAdapter = new ProductImageAdapter(getSupportFragmentManager(), fragments);
	    ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
	    pager.setAdapter(pageAdapter);
	    
	    txt_fav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
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
		    	    	progressDialog = new ProgressDialog(ProductDetails.this);
		    	        progressDialog.setMessage("Loading ...");
		    	        progressDialog.setIndeterminate(false);
		    	        progressDialog.setCancelable(false);
		    	        progressDialog.show();
		    	        
		    	        SharedPreferences pref  = getSharedPreferences(Constants.App_Pref, 0);
		    	        String UserID = String.valueOf(pref.getInt(Constants.App_Pref_UserID, 0)) ;
		    	        String DeviceID = Secure.getString(ProductDetails.this.getContentResolver(),Secure.ANDROID_ID);

		    	        RequestParams params = new RequestParams();
		    			params.put("username", Constants.App_Username);
		    			params.put("password", Constants.App_Pass);
		    			params.put("action", "add");
		    			params.put("user_id", UserID);
		    			params.put("phone_identifer", DeviceID);
		    			params.put("phone_type", "android");
		    			params.put("item_id", product.getId());
		    			
		    			client.get(Constants.FavURL, params, new JsonHttpResponseHandler()
		    	        {
		    	        	@Override
		    	        	public void onSuccess(org.json.JSONObject arg0) 
		    	        	{
		    	        		progressDialog.dismiss();
		    	        		
		    	        		try {
	    	        				String status = arg0.getString("status");
	    	        				
	    	        				if(status.equals("1"))
	    	        				{
	    	        					showAlert(getString(R.string.fave_success_add));
	    	        				}
	    	        				else
	    	        				{
	    	        					showAlert(getString(R.string.fave_fail_add));
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
		});

	}
	
	private List<Fragment> getFragments(){

		  List<Fragment> fList = new ArrayList<Fragment>();
		  
		  for(int i=0; i < product.getImages().size(); i++)
		  {
			  fList.add(ProductFragment.newInstance(product.getImages().get(i)));
		  }

		  return fList;

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
