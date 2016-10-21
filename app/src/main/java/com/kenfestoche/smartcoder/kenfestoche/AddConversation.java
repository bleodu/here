package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kenfestoche.smartcoder.kenfestoche.model.AdapterAmis;
import com.kenfestoche.smartcoder.kenfestoche.model.AdapterConversation;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AddConversation extends AppCompatActivity {


    ListView lstAmis;
    ArrayList<HashMap<String, Object>> amisconversations;
    SimpleAdapter mSchedule;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Utilisateur User;
    EditText edtNomConversation;
    EditText edtAmis;
    ImageView btAnnuler;
    ImageView btValider;
    ImageView imFleche;
    public static String listusers="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conversation);

        lstAmis = (ListView) findViewById(R.id.lstContactsConversation);
        btAnnuler = (ImageView) findViewById(R.id.imAnnulerContact);
        btValider = (ImageView) findViewById(R.id.imValiderContact);
        amisconversations =  new ArrayList<HashMap<String,Object>>();

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));

        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");

        edtNomConversation = (EditText) findViewById(R.id.edtnonconversation);
        edtAmis = (EditText) findViewById(R.id.edtpersonnes);

        edtAmis.setTypeface(face);
        edtNomConversation.setTypeface(face);


        if(User!=null) {
            WebService WS = new WebService();

            JSONArray ListAmisConversations = WS.GetListAmis(User);

            if (ListAmisConversations != null) {

                String Url = null;

                for (int i = 0; i < ListAmisConversations.length(); i++) {
                    JSONObject Ami = null;

                    HashMap<String, Object> valeur = new HashMap<String, Object>();
                    try {
                        Ami = ListAmisConversations.getJSONObject(i);
                        valeur.put("id_useramis", Ami.getString("id_useramis"));
                        valeur.put("pseudo", Ami.getString("pseudo"));
                        Url = Ami.getString("photo").replace(" ", "%20");

                        URL pictureURL;

                        pictureURL = null;

                        try {
                            pictureURL = new URL(Url);
                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        valeur.put("photo", bitmap);



                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    amisconversations.add(valeur);
                }
            }

        }

        AdapterAmis ConversationsAmisArray = new AdapterAmis(getBaseContext(), amisconversations, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},false);

        //mSchedule.setViewBinder(new MyViewBinder());
        lstAmis.setAdapter(ConversationsAmisArray);


        btValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService();
                WS.AddConversation(edtNomConversation.getText().toString(),listusers);
                finish();
            }
        });

        btAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}
