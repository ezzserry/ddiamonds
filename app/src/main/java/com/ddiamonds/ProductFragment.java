package com.ddiamonds;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ProductFragment extends Fragment {

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final Fragment newInstance(String message) {
        ProductFragment f = new ProductFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ImageLoader imageLoader = new ImageLoader(getActivity());

        String img_url = getArguments().getString(EXTRA_MESSAGE);
        View v = inflater.inflate(R.layout.product_fragment, container, false);
        ImageView imgView = (ImageView) v.findViewById(R.id.imgView);
        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
//        imageLoader.DisplayImage(img_url, imgView);
        Picasso.with(getActivity()).load(img_url).into(imgView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });

        return v;

    }

}
