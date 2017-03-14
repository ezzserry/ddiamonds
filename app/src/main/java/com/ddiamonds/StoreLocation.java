package com.ddiamonds;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import android.widget.TextView;
import android.widget.Toast;

public class StoreLocation extends Activity{
	
	ImageView img_home;
	private ProgressDialog progressDialog;
	AsyncHttpClient client;
	ArrayList<HashMap<String, String>> LocationList;
	
	private GoogleMap map;
	
	TextView txt_address,txt_phone;
	
	LinearLayout lin_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_location);
		
		client = new AsyncHttpClient();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap googleMap) {

			}
		});
		
		txt_address = (TextView) findViewById(R.id.txt_address);
		txt_phone = (TextView) findViewById(R.id.txt_phone);
		lin_address = (LinearLayout) findViewById(R.id.lin_address);
		
		img_home = (ImageView) findViewById(R.id.img_home);		
		img_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(StoreLocation.this, Home.class);
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
    	    	progressDialog = new ProgressDialog(StoreLocation.this);
    	        progressDialog.setMessage("Loading ...");
    	        progressDialog.setIndeterminate(false);
    	        progressDialog.setCancelable(false);
    	        progressDialog.show();
    	        
    	        RequestParams params = new RequestParams();
    			params.put("username", Constants.App_Username);
    			params.put("password", Constants.App_Pass);
    			
    			client.get(Constants.StoreLocationURL, params, new JsonHttpResponseHandler()
    	        {
    	        	@Override
    	        	public void onSuccess(org.json.JSONObject arg0) 
    	        	{
    	        		progressDialog.dismiss();
    	        		
    	        		LocationList = new ArrayList<HashMap<String, String>>();
    	        		
    	        		try {
							
	    	        			String status = arg0.getString("status");
    	        				if(status.equals("1"))
    	        				{
    	        					JSONArray response_arr = arg0.getJSONArray("response");
    	        					
    	        					for(int i=0; i < response_arr.length(); i++)
    	        					{
    	        						JSONObject location_obj = response_arr.getJSONObject(i);
    	        						String address = location_obj.getString("address");
    	                                String longitude = location_obj.getString("longitude");  
    	                                String latitude = location_obj.getString("latitude"); 
    	                                String city = location_obj.getString("city"); 
    	                                String country = location_obj.getString("country"); 
    	                                String phone = location_obj.getString("phone"); 
    	                                
    	                                HashMap<String, String> map = new HashMap<String, String>();
    	                                map.put("address", address);
    	                                map.put("longitude", longitude);
    	                                map.put("latitude", latitude);
    	                                map.put("city", city);
    	                                map.put("country", country);
    	                                map.put("phone", phone);
    	                              
    	                                LocationList.add(map);
    	        					}
    	        					
    	        					for(int i=0; i < LocationList.size(); i++)
    	        					{
    	        						final HashMap<String, String> hsh_location = LocationList.get(i);
    	        						String lat = hsh_location.get("latitude");
    	        						String lng = hsh_location.get("longitude");
    	        						
    	        						LatLng storLatlng = new LatLng(Double.valueOf(lat),Double.valueOf(lng));
    	        						
    	        						if(!lat.equals("") && !lng.equals("") && !lat.equals("0") && !lng.equals("0"))
    	        						{
    	        							String Address = hsh_location.get("address") + ", "+ hsh_location.get("city")+ ", " + hsh_location.get("country");
    	        							
    	        							map.addMarker(new MarkerOptions()
    	        							.position(storLatlng)
    	        					        .title(Address)
    	        					        .snippet(hsh_location.get("phone")));
    	        							
    	        							map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
												
												@Override
												public void onInfoWindowClick(Marker marker) {
													
													lin_address.setVisibility(View.VISIBLE);
													txt_address.setText(marker.getTitle());
													txt_phone.setText(marker.getSnippet());
													
												}
											});
    	        		
			    	        			    // Move the camera instantly to hamburg with a zoom of 15.
			    	        			    map.moveCamera(CameraUpdateFactory.newLatLngZoom(storLatlng, 5));
			    	        			    // Zoom in, animating the camera.
			    	        			    map.animateCamera(CameraUpdateFactory.zoomTo(3), 2000, null);
    	        						}
    	        					
    	        					}
    	        					
    	        				}
    	        		}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    	        	}
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
