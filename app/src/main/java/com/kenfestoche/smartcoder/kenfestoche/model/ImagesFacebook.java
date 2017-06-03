package com.kenfestoche.smartcoder.kenfestoche.model;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.kenfestoche.smartcoder.kenfestoche.R;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

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
        GraphRequest request =  new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+token.getUserId()+"/albums",
                null,
                HttpMethod.GET);

        try {
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
        ImageView imageView = null;


        View rowView;

        holder=new Holder();

        rowView = mInflater.inflate(R.layout.rowphotoprofil, null);
        holder.img=(ImageView) rowView.findViewById(R.id.imphotoprofil);

        holder.imgdelete=(ImageView) rowView.findViewById(R.id.imdeletephoto);
        holder.txID = (TextView) rowView.findViewById(R.id.txID);

        holder.imgdelete.setVisibility(View.INVISIBLE);

        //JSONObject photo = null;
        String photo = ListPhotos.get(i);
        //holder.txID.setText( photo.getString("ID"));

        URL url = null;
        try {
            url = new URL(photo);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.img.setImageBitmap(image);
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(mContext, image);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));
            //mImageCaptureUri= (Uri) data.getExtras().get("URI");
            String picturePath = finalFile.getPath();
            //String picturePath = mImageCaptureUri.getPath();
            holder.img.setTag(picturePath);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }



        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = (ImageView) view;

                WebService WS = new WebService(mContext,false);
                WS.UploadImage(img.getTag().toString(),User);
                ((Activity)mContext).finish();
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
