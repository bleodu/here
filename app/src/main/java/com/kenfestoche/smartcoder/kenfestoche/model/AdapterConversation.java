package com.kenfestoche.smartcoder.kenfestoche.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kenfestoche.smartcoder.kenfestoche.ChatViewConversation;
import com.kenfestoche.smartcoder.kenfestoche.Conversation;
import com.kenfestoche.smartcoder.kenfestoche.R;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    HashMap<String, Object> valeur;
    boolean gestionAjout;
    HashMap<String, Object> ami;
    AdapterGridPhotos listPhotos;

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
        //valeur = new HashMap<String, Object>();
        //valeur=arrayList.get(position);


        TextView txPseudo= (TextView) view.findViewById(R.id.txPseudoLigne);
        ImageView imPhotoKiffs=(ImageView) view.findViewById(R.id.imgPhotoKiffs);
        GridView gridPhotos = (GridView) view.findViewById(R.id.gridimgkiff);

        //int pos=Integer.parseInt(view.getTag().toString());
        valeur=arrayList.get(position);
        String idConv = (String) valeur.get("id");

        ArrayList<Bitmap> photos = new ArrayList<Bitmap>();
        photos= (ArrayList<Bitmap>) valeur.get("listphotos");

        if(photos != null){
            listPhotos = new AdapterGridPhotos(view.getContext(), photos);
            gridPhotos.setVisibility(View.VISIBLE);
            gridPhotos.setAdapter(listPhotos);
        }


        //imPhotoKiffs.setImageResource(R.drawable.ajoutphoto);
        imPhotoKiffs.setVisibility(View.INVISIBLE);
        RelativeLayout ligneContact = (RelativeLayout) view.findViewById(R.id.ligneContact);
        ligneContact.setTag(String.valueOf(position));

        ligneContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=Integer.parseInt(view.getTag().toString());
                valeur=arrayList.get(pos);
                String idConv = (String) valeur.get("id");
                ArrayList<Bitmap> lstphotos= (ArrayList<Bitmap>) valeur.get("listphotos");
                Intent intent = new Intent(context.getApplicationContext(),Conversation.class);
                intent.putExtra("idconv",idConv);
                intent.putExtra("nomconv",(String) valeur.get("conversation"));
                //intent.putExtra("listphotos",lstphotos);
                view.getContext().startActivity(intent);
            }
        });

        txPseudo.setTypeface(face);

        return view;


    }

    @Override
    public int getCount() {
        int count=arrayList.size(); //counts the total number of elements from the arrayList.
        return count;//returns the total count to adapter
    }


}
