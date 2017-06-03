package com.kenfestoche.smartcoder.kenfestoche.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.kenfestoche.smartcoder.kenfestoche.R;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by smartcoder on 31/03/2017.
 */

public class AdapterGridPhotos extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> listPhotos;
    LayoutInflater inflater;
    // Constructor
public AdapterGridPhotos(Context context,  ArrayList<String> listePhotos) {

        listPhotos=listePhotos;
        mContext = context;
        inflater.from(mContext);

    }


    @Override
    public int getCount() {
        return listPhotos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        //View view = super.getView(i, convertView, parent);
        ImageView imageView = new ImageView(mContext);
        if(listPhotos.size()<=1) {
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            Picasso.with(imageView.getContext())
                    .load(listPhotos.get(i))
                    .resize(300, 300)
                    .centerCrop()
                    .into(imageView);
        }else if(listPhotos.size()>1){
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            Picasso.with(imageView.getContext())
                    .load(listPhotos.get(i))
                    .resize(150, 150)
                    .centerCrop()
                    .into(imageView);
        }


        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setImageBitmap();
        //imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
        return imageView;

    }
}
