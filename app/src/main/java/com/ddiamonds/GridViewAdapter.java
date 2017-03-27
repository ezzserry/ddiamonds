package com.ddiamonds;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

@SuppressLint("ResourceAsColor")
public class GridViewAdapter extends ArrayAdapter<Product> {

    public int list_layout;
    public ImageLoader imageLoader;
    private Context mContext;
    List<Product> myList;


    public GridViewAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);

        mContext = context;
        myList = objects;
        imageLoader = new ImageLoader(mContext);
        list_layout = resource;
    }

    public int getCount() {
        return myList.size();
    }

    public Product getItem(int position) {
        return myList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View MyView = convertView;

        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        MyView = li.inflate(list_layout, null);

        if (myList.size() != 0) {
            Product sel_pro = myList.get(position);

            TextView tv = (TextView) MyView.findViewById(R.id.txt_item_name);
            tv.setText(sel_pro.getTitle());

            List<String> pro_images = sel_pro.getImages();

            final ImageView iv = (ImageView) MyView.findViewById(R.id.grid_item_image);
            final ProgressBar itemProgressBar = (ProgressBar) MyView.findViewById(R.id.newProgressBar);
//        imageLoader.DisplayImage(pro_images.get(0), iv);
            if (pro_images.size()!=0) {
                Picasso.with(mContext).load(pro_images.get(0)).into(iv,  new Callback() {
                    @Override
                    public void onSuccess() {
                        iv.setVisibility(View.VISIBLE);
                        itemProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        iv.setVisibility(View.VISIBLE);
                        itemProgressBar.setVisibility(View.GONE);
                        iv.setImageResource(R.drawable.image_default_grid);
                    }
                });
            }
            else{
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.image_default_grid);
                itemProgressBar.setVisibility(View.GONE);
            }
        }
        else
            Toast.makeText(mContext, "no products found", Toast.LENGTH_SHORT).show();
        return MyView;
    }

}
