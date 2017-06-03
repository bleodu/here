package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kenfestoche.smartcoder.kenfestoche.model.AdapterAmis;
import com.kenfestoche.smartcoder.kenfestoche.model.AdapterConversation;
import com.kenfestoche.smartcoder.kenfestoche.model.MainChipViewAdapter;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.ChipViewAdapter;
import com.plumillonforge.android.chipview.OnChipClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddConversation extends AppCompatActivity {


    ListView lstAmis;
    ArrayList<HashMap<String, Object>> amisconversations;
    ArrayList<HashMap<String, Object>> amisconversationssauv;
    SimpleAdapter mSchedule;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Utilisateur User;
    EditText edtNomConversation;
    public static EditText edtAmis;
    ImageView btAnnuler;
    ImageView btValider;
    ImageView imFleche;
    public static ChipView chipDefault =null;

    public static String listusers="";
    public static List<Chip> chipList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conversation);
        chipList= new ArrayList<>();
        lstAmis = (ListView) findViewById(R.id.lstContactsConversation);
        btAnnuler = (ImageView) findViewById(R.id.imAnnulerContact);
        btValider = (ImageView) findViewById(R.id.imValiderContact);
        amisconversations =  new ArrayList<HashMap<String,Object>>();
        amisconversationssauv =  new ArrayList<HashMap<String,Object>>();
        listusers="";

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));

        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");

        edtNomConversation = (EditText) findViewById(R.id.edtnonconversation);
        edtAmis = (EditText) findViewById(R.id.edtpersonnes);

        edtAmis.setTypeface(face);
        edtNomConversation.setTypeface(face);

        chipDefault = (ChipView) findViewById(R.id.chipview);

        chipDefault.setChipBackgroundColor(getResources().getColor(R.color.gris));

        chipDefault.setOnChipClickListener(new OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip) {
                chipList.remove(chip);
                chipDefault.setChipList(chipList);
                // Action here !
            }
        });


        if(User!=null) {
            WebService WS = new WebService(getBaseContext());

            JSONArray ListAmisConversations = WS.GetListAmis(User,"");

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

                        /*URL pictureURL;

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
                        }*/

                        valeur.put("url", Url);



                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    amisconversations.add(valeur);
                    amisconversationssauv.add(valeur);
                }
            }

        }


        final AdapterAmis ConversationsAmisArray = new AdapterAmis(getBaseContext(), amisconversations, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},false);


        edtAmis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                amisconversations.clear();
                HashMap<String, Object> valeur = new HashMap<String, Object>();
                for (int j = 0; j < amisconversationssauv.size(); j++) {
                    valeur=amisconversationssauv.get(j);

                    if(valeur.get("pseudo").toString().toLowerCase().substring(0,edtAmis.getText().length()).equals(edtAmis.getText().toString().toLowerCase()))
                    {
                        amisconversations.add(amisconversationssauv.get(j));
                    }
                }


                ConversationsAmisArray.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //mSchedule.setViewBinder(new MyViewBinder());
        lstAmis.setAdapter(ConversationsAmisArray);


        btValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService(getBaseContext());
                WS.AddConversation(edtNomConversation.getText().toString(),listusers,User);
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
