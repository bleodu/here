package com.kenfestoche.smartcoder.kenfestoche.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kenfestoche.smartcoder.kenfestoche.ChatViewConversation;
import com.kenfestoche.smartcoder.kenfestoche.R;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by smartcoder on 12/10/2016.
 */

public class AdapterConversation extends SimpleAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String, Object>> arrayList;
    Typeface face;
    ImageView imAjoutAmis;
    ImageView imRefuseAmis;
    ImageView imSignaler;
    ImageView imSuppKiffs;

    boolean gestionAjout;
    HashMap<String, Object> ami;

    public AdapterConversation(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to, boolean GestionAjout) {
        super(context, data, resource, from, to);
        this.context=context;
        this.arrayList=data;

        inflater.from(context);

        face=Typeface.createFromAsset(context.getAssets(),"fonts/weblysleekuil.ttf");
        gestionAjout=GestionAjout;

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

        TextView txPseudo= (TextView) view.findViewById(R.id.txPseudoLigne);
        ImageView imPhotoKiffs=(ImageView) view.findViewById(R.id.imgPhotoKiffs);

        imPhotoKiffs.setImageResource(R.drawable.ajoutphoto);
        RelativeLayout ligneContact = (RelativeLayout) view.findViewById(R.id.ligneContact);

        txPseudo.setTypeface(face);

        return view;


    }

    @Override
    public int getCount() {
        int count=arrayList.size(); //counts the total number of elements from the arrayList.
        return count;//returns the total count to adapter
    }


}
