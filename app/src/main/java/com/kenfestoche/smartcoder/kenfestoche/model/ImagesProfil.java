package com.kenfestoche.smartcoder.kenfestoche.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import java.lang.Object;

import java.io.File;

/**
 * Created by smartcoder on 25/07/16.
 */
public class ImagesProfil extends BaseAdapter {


    int mGalleryItemBackground;
    private Context mContext;
    File[] images;
    File[] files;
    public ImagesProfil(Context c, int folderID) {
        mContext = c;

        File dir = new File(Environment.getExternalStorageDirectory() + "/imageseaser");
        files = dir.listFiles();
        images = files[folderID].listFiles();

    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return images[i].getAbsolutePath();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        //Bitmap bm = BitmapFactory
        //      .decodeFile(images[position].getAbsolutePath());
        if (view == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(5, 10, 5, 10);
        } else {
            imageView = (ImageView) view;
        }
        imageView.setImageBitmap(BitmapFactory.decodeFile(images[i].getAbsolutePath()));
        return imageView;
    }
}
