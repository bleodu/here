package com.kenfestoche.smartcoder.kenfestoche.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.kenfestoche.smartcoder.kenfestoche.R;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.Object;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by smartcoder on 25/07/16.
 */
public class ImagesProfil extends BaseAdapter {


    int mGalleryItemBackground;
    private Context mContext;
    public final LayoutInflater mInflater;
    File[] images;
    File[] files;
    JSONArray ListPhotos;
    public ImagesProfil(Context c, int folderID,Utilisateur user) {
        mContext = c;
        mInflater = LayoutInflater.from(c);

        /*File dir = new File(Environment.getExternalStorageDirectory() + "/imageseaser");
        files = dir.listFiles();
        images = files[folderID].listFiles();*/

        WebService WS = new WebService();
        ListPhotos =  WS.GetListPhotos(user);



    }

    @Override
    public int getCount() {
        return ListPhotos.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return ListPhotos.get(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = null;
        JSONObject photo = null;
        try {
            photo = ListPhotos.getJSONObject(i);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*if (view == null) {
            view = mInflater.inflate(R.layout.griditemphoto, viewGroup, false);
            view.setTag(R.id.photoitem, view.findViewById(R.id.photoitem));
            imageView = (ImageView) view.getTag(R.id.photoitem);
            //view.setTag(R.id.text, v.findViewById(R.id.text));
        }*/
        //Bitmap bm = BitmapFactory
        //      .decodeFile(images[position].getAbsolutePath());
        if (view == null) {
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(100, 100));

            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //imageView.setPadding(5, 10, 5, 10);
        } else {
            imageView = (ImageView) view;
        }
        URL url = null;
        try {
            url = new URL("http://www.smartcoder-dev.fr/ZENAPP/webservice/" +  photo.getString("photo"));
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return imageView;
    }
}
