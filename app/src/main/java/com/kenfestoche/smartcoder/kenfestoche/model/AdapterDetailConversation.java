package com.kenfestoche.smartcoder.kenfestoche.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kenfestoche.smartcoder.kenfestoche.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by smartcoder on 12/10/2016.
 */

public class AdapterDetailConversation extends SimpleAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String, Object>> arrayList;
    Typeface face;
    ImageView imAjoutAmis;
    ImageView imRefuseAmis;
    ImageView imSignaler;
    ImageView imSuppKiffs;
    Utilisateur MonUser;
    HashMap<String, Object> ami;

    public AdapterDetailConversation(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to,Utilisateur User) {
        super(context, data, resource, from, to);
        this.context=context;
        this.arrayList=data;
        this.MonUser=User;

        inflater.from(context);

        face=Typeface.createFromAsset(context.getAssets(),"fonts/weblysleekuil.ttf");

    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        try{

            //if(view != null && arrayList!=null) {
                //if (arrayList.size() > 0) {
                    HashMap<String, Object> valeur = arrayList.get(position);
                    if (MonUser.id_user == Integer.parseInt(valeur.get("id_user").toString())) {
                        view = LayoutInflater.from(context).inflate(R.layout.lignechatviewright, null, false);
                        TextView txChat = (TextView) view.findViewById(R.id.txTexteChatRight);
                        TextView txDateRight = (TextView) view.findViewById(R.id.txDateRight);
                        ImageView imPointeBleu = (ImageView) view.findViewById(R.id.imPointeBleu);
                        txChat.setText(valeur.get("texte").toString());
                        txDateRight.setText(valeur.get("date_create").toString());
                        imPointeBleu.setImageResource(R.drawable.pointemessbleu);
                        //ImageView imPhotoKiffs=(ImageView) view.findViewById(R.id.imPhotoChat);
                        //imPhotoKiffs.setImageResource(R.drawable.ajoutphoto);
                        txDateRight.setTypeface(face);
                        txChat.setTypeface(face);
                    } else {
                        view = LayoutInflater.from(context).inflate(R.layout.lignechatview, null, false);
                        TextView txChat = (TextView) view.findViewById(R.id.txTexteChat);
                        TextView txDateRight = (TextView) view.findViewById(R.id.txDate);
                        ImageView imPhotoKiffs = (ImageView) view.findViewById(R.id.imPhotoChat);
                        ImageView imPointeGrise = (ImageView) view.findViewById(R.id.imPointeGrise);
                        imPointeGrise.setImageResource(R.drawable.pointegrisemess);
                        txDateRight.setText(valeur.get("date_create").toString());
                        txChat.setText(valeur.get("texte").toString());
                        //Bitmap photo = (Bitmap) valeur.get("photo");
                        Picasso.with(context).load((String) valeur.get("url")).resize(300,300).into(imPhotoKiffs);
                        //imPhotoKiffs.setImageBitmap(photo);
                        txChat.setTypeface(face);
                        txDateRight.setTypeface(face);
                    }
                //}
            //}


            //return view;
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return view;

    }

    @Override
    public int getCount() {
        int count=arrayList.size(); //counts the total number of elements from the arrayList.
        return count;//returns the total count to adapter
    }


}
