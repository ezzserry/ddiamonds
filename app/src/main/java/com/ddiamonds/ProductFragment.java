package com.ddiamonds;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductFragment extends Fragment {
	
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

	public static final Fragment newInstance(String message)
	{
		ProductFragment f = new ProductFragment();
		Bundle bdl = new Bundle(1);
		bdl.putString(EXTRA_MESSAGE, message);
		f.setArguments(bdl);
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		ImageLoader imageLoader=new ImageLoader(getActivity());

		String img_url = getArguments().getString(EXTRA_MESSAGE);
	    View v = inflater.inflate(R.layout.product_fragment, container, false);
	    ImageView imgView = (ImageView)v.findViewById(R.id.imgView);
	    
	    imageLoader.DisplayImage(img_url, imgView);

	   return v;

	}

}
