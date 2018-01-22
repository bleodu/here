package com.kenfestoche.smartcoder.kenfestoche.model;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.kenfestoche.smartcoder.kenfestoche.FragmentsSliderActivity;
import com.kenfestoche.smartcoder.kenfestoche.R;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smartcoder on 25/07/16.
 */
public class ImagesFacebook extends BaseAdapter {


    int mGalleryItemBackground;
    private Context mContext;
    public final LayoutInflater mInflater;
    File[] images;
    File[] files;
    List<String> ListPhotos;
    Holder holder;
    Utilisateur User;
    JSONObject photo;
    AccessToken token;
    ContentResolver contentresolv;

    public ImagesFacebook(Context c, int folderID, Utilisateur user, ContentResolver contentResolv) {
        mContext = c;
        mInflater = LayoutInflater.from(c);
        User = user;

        ListPhotos = new ArrayList<String>();
        contentresolv=contentResolv;

        FacebookSdk.sdkInitialize(c);
        AppEventsLogger.activateApp(c);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        token = AccessToken.getCurrentAccessToken();

        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+token.getUserId()+"+/user_photos",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                    }
                }
        ).executeAsync();

        /* make the API call */
        GraphRequest request =  new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+token.getUserId()+"/albums",
                null,
                HttpMethod.GET);

        try {
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email,birthday,picture,last_name,first_name,name,gender,cover,albums");
            request.setParameters(parameters);
            JSONArray data = request.executeAndWait().getJSONObject().getJSONArray("data");
            for(int i=0; i<data.length(); i++){
                JSONObject Album = data.getJSONObject(i);
                String id_album=Album.getString("id");

                GraphRequest requete = new  GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/"+id_album+"/photos?fields=link,images",
                        null,
                        HttpMethod.GET
                );

                JSONArray donnee = requete.executeAndWait().getJSONObject().getJSONArray("data");

                //JSONObject images = donnee.getJSONObject(1);

                for(int j=0; j<donnee.length(); j++){
                    JSONArray Photo = donnee.getJSONObject(j).getJSONArray("images");
                    String linkphoto=Photo.getJSONObject(0).getString("source");


                    ListPhotos.add(linkphoto);


                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public int getCount() {
        return ListPhotos.size();
    }

    @Override
    public Object getItem(int i) {
        return ListPhotos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder
    {
        //TextView tv;
        ImageView img;
        ImageView imgdelete;
        TextView txID;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView finalImageView = null;


        View rowView;

        holder=new Holder();

        rowView = mInflater.inflate(R.layout.rowphotoprofil, null);
        holder.img=(ImageView) rowView.findViewById(R.id.imphotoprofil);

        holder.imgdelete=(ImageView) rowView.findViewById(R.id.imdeletephoto);
        holder.txID = (TextView) rowView.findViewById(R.id.txID);
        String photo = ListPhotos.get(i);
        holder.imgdelete.setVisibility(View.INVISIBLE);
        Picasso.with(mContext)
                .load(photo)
                .into(holder.img);
                        //JSONObject photo = null;
        holder.img.setTag(photo);
        //holder.txID.setText( photo.getString("ID"));



        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ImageView img = (ImageView) view;
                URL url = null;
                try {
                    url = new URL(img.getTag().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Bitmap image = null;
                try {
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri tempUri = getImageUri(mContext, image);

                Intent myCropIntent = new Intent("com.android.camera.action.CROP");

                myCropIntent.setDataAndType(tempUri, "image/*");
                myCropIntent.putExtra("crop", "true");
                myCropIntent.putExtra("aspectX", 1);
                myCropIntent.putExtra("aspectY", 1);
                myCropIntent.putExtra("outputX", 300);
                myCropIntent.putExtra("outputY", 300);
                myCropIntent.putExtra("return-data", true);
                ((Activity)mContext).startActivityForResult(myCropIntent, 4);
                //((Activity)mContext).finish();
                //Uri tempUri = Uri.parse("android.resource://com.kenfestoche.smartcoder.kenfestoche/" + R.drawable.your_image_id);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                /*File finalFile = new File(getRealPathFromURI(tempUri));
                //mImageCaptureUri= (Uri) data.getExtras().get("URI");
                String picturePath = finalFile.getPath();
                WebService WS = new WebService(mContext,false);
                WS.UploadImage(picturePath, FragmentsSliderActivity.User);
               */
            }
        });



        //return imageView;

        return rowView;
    }



    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = contentresolv.query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);

    }



}
