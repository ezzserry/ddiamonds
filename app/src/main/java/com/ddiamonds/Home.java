package com.ddiamonds;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Home extends Activity{
	
	LinearLayout linear_about, linear_location, linear_contact,linear_product, linear_fav;
	ImageView img_help, img_help_text;
	boolean isHelpShown = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.home);
		
		linear_location = (LinearLayout) findViewById(R.id.linear_location);
		linear_contact= (LinearLayout) findViewById(R.id.linear_contact);
		linear_product= (LinearLayout) findViewById(R.id.linear_product);
		linear_about= (LinearLayout) findViewById(R.id.linear_about);
		linear_fav= (LinearLayout) findViewById(R.id.linear_fav);
		
		linear_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				linear_location.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
				linear_contact.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_product.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_about.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_fav.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				
				Intent intent = new Intent(Home.this, StoreLocation.class);
				startActivity(intent);
				
			}
		});
		
		linear_contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				linear_contact.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
				linear_location.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_product.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_about.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_fav.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				
				Intent intent = new Intent(Home.this, ContactUS.class);
				startActivity(intent);
				
			}
		});

		linear_product.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
		
				linear_product.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
				linear_contact.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_location.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_about.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_fav.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				
				// go to brands
				Intent intent = new Intent(Home.this, Brands.class);
				startActivity(intent);

			}
		});

		linear_about.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View arg0) {
		
				linear_about.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
				linear_contact.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_product.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_location.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_fav.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				
				Intent intent = new Intent(Home.this, About.class);
				startActivity(intent);
				
			}
		});

		linear_fav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
		
				linear_fav.setBackgroundColor(getResources().getColor(R.color.menu_frame_selected_gray));
				linear_contact.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_product.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_about.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				linear_location.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				
				Intent intent = new Intent(Home.this, FavList.class);
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
