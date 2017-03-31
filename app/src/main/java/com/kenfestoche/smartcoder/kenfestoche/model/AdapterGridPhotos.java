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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by smartcoder on 31/03/2017.
 */

public class AdapterGridPhotos extends BaseAdapter {

    private Context mContext;
    private ArrayList<Bitmap> listPhotos;
    LayoutInflater inflater;
    // Constructor
public AdapterGridPhotos(Context context,  ArrayList<Bitmap> listePhotos) {

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
            imageView.setLayoutParams(new GridView.LayoutParams(80, 80));
        }else if(listPhotos.size()>1){
            imageView.setLayoutParams(new GridView.LayoutParams(40, 40));
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(listPhotos.get(i));
        //imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
        return imageView;

    }
}
