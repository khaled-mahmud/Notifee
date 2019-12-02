package com.pgdit101.iit.notifee;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class ListProductAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    //public ImageLoader imageLoader;

    public ListProductAdapter(Activity a, ArrayList<HashMap<String, String>> d){
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListProductViewHolder holder = null;

        //View vi = convertView;
        if(convertView == null){
            holder = new ListProductViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_row, parent, false);

            holder.productName = convertView.findViewById(R.id.title); // title
            holder.productDescription = convertView.findViewById(R.id.description); // description
            holder.productDate = convertView.findViewById(R.id.duration); // expiry time
            //holder.productImage = convertView.findViewById(R.id.list_image); // thumb image

            convertView.setTag(holder);
        }else{
            holder = (ListProductViewHolder) convertView.getTag();
        }

        holder.productName.setId(position);
        holder.productDescription.setId(position);
        holder.productDate.setId(position);
        //holder.productImage.setId(position);

        HashMap<String, String> product = new HashMap<String, String>();
        product = data.get(position);

        try {
            holder.productName.setText(product.get(ListViewActivity.KEY_PRODUCT_NAME));
            holder.productDescription.setText(product.get(ListViewActivity.KEY_DESCRIPTION));
            holder.productDate.setText(product.get(ListViewActivity.KEY_DATE));
            //holder.productImage.setText(product.get(ListViewActivity.KEY__));


            /* Image */
            /*ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            holder.productImage.setText(color);
            holder.productImage.setText(Html.fromHtml("&#11044;"));
            /* Image */
        }catch (Exception e){

        }

        return convertView;
    }
}


class ListProductViewHolder {
    TextView productName;
    TextView productDescription;
    TextView productDate;
    //TextView productImage;

}