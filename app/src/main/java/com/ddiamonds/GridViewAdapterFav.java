package com.ddiamonds;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
@SuppressLint("ResourceAsColor")
public class GridViewAdapterFav extends ArrayAdapter<Product> {
	
	public int list_layout;
    public ImageLoader imageLoader; 
    private Context mContext;
    List<Product> myList;
    
	
    public GridViewAdapterFav(Context context, int resource, List<Product> objects) {
		super(context, resource, objects);

		 mContext = context;
		 myList = objects;
		 imageLoader=new ImageLoader(mContext);
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
            
            Product sel_pro = myList.get(position);
            

            TextView tv = (TextView)MyView.findViewById(R.id.txt_item_name);
            tv.setText(sel_pro.getTitle());
            
            List<String> pro_images = sel_pro.getImages();
    
            ImageView iv = (ImageView)MyView.findViewById(R.id.grid_item_image);
            imageLoader.DisplayImage(pro_images.get(0), iv);
            
            
            TextView img_delete = (TextView) MyView.findViewById(R.id.txt_item_delete);
            img_delete.setTag(position);

         
         return MyView;
    }
 
}
