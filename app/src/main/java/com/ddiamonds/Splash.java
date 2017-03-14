package com.ddiamonds;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity{
	
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		pref  = getSharedPreferences(Constants.App_Pref, 0);
	}
	
	  @Override
	    protected void onResume()
	    {
	        super.onResume();
	        
	        new Handler().postDelayed(new Runnable()
	        {
	            
	            public void run()
	            {
	                //Finish the splash activity so it can't be returned to.
	                Splash.this.finish();
	                // Create an Intent that will start the main activity.
	                
	                int UserID = pref.getInt(Constants.App_Pref_UserID, 0);
	                
	                if(UserID > 0)
	                {
	                	Intent mainIntent = new Intent(Splash.this, Home.class);
		                Splash.this.startActivity(mainIntent);
	                }
	                else
	                {
	                	Intent mainIntent = new Intent(Splash.this, Login.class);
		                Splash.this.startActivity(mainIntent);
	                }
 
	            }
	        }, Constants.SPLASH_DISPLAY_LENGTH);
	    
	       
	    }
	    
	    @Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			
		}

}
