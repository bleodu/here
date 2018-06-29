package com.kenfestoche.smartcoder.kenfestoche;

import android.*;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kenfestoche.smartcoder.kenfestoche.model.AdapterAmis;
import com.kenfestoche.smartcoder.kenfestoche.model.ImagesProfil;
import com.kenfestoche.smartcoder.kenfestoche.model.MyViewBinder;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ListAmisAjout extends AppCompatActivity {

    ListView lstUsers;
    EditText edtPhone;
    ArrayList<HashMap<String, Object>> amis;
    SimpleAdapter mSchedule;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Utilisateur User;
    RelativeLayout RlvMonRep;
    RelativeLayout RlvMonPhone;
    ImageView imFlecheGauche;
    RelativeLayout RlvRechTel;
    int TypeRecherche;
    AdapterAmis AmisArray;
    TextView txHeader;
    Typeface face;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_amis_ajout);


        lstUsers = (ListView) findViewById(R.id.lstamis);
        edtPhone = (EditText) findViewById(R.id.edttelrech);
        RlvMonRep = (RelativeLayout) findViewById(R.id.rlvMonRep);
        RlvMonPhone = (RelativeLayout) findViewById(R.id.rlvTelPhone);
        RlvRechTel = (RelativeLayout) findViewById(R.id.rlvRechTel);
        txHeader= (TextView) findViewById(R.id.txHeader);
        imFlecheGauche = (ImageView) findViewById(R.id.imFlecheGaucheListAmis);
        face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");
        Typeface faceGenerica=Typeface.createFromAsset(getAssets(),"Generica.otf");

        txHeader.setTypeface(faceGenerica);

        amis =  new ArrayList<HashMap<String,Object>>();

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        User = FragmentsSliderActivity.User;
        AmisArray = new AdapterAmis(getBaseContext(), amis, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},true,true,true,null);

        TypeRecherche= getIntent().getExtras().getInt("TypeRech");
        WebService WS = new WebService(getBaseContext());

        imFlecheGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RlvMonRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permission = ActivityCompat.checkSelfPermission(ListAmisAjout.this, Manifest.permission.READ_CONTACTS);

                if (permission != PackageManager.PERMISSION_GRANTED) {


                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // We don't have permission so prompt the user
                        ActivityCompat.requestPermissions(
                                ListAmisAjout.this,
                                new String[]{android.Manifest.permission.READ_CONTACTS},
                                1
                        );
                    }
                }else{
                    amis.clear();
                    AmisArray.notifyDataSetChanged();
                    TypeRecherche=2;
                    RlvRechTel.setVisibility(View.INVISIBLE);
                    RechercherRepertoire();
                }

            }
        });

        RlvMonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amis.clear();
                AmisArray.notifyDataSetChanged();
                TypeRecherche=1;
                RlvRechTel.setVisibility(View.VISIBLE);
            }
        });


        if(TypeRecherche==2){
            int permission = ActivityCompat.checkSelfPermission(ListAmisAjout.this, Manifest.permission.READ_CONTACTS);

            if (permission != PackageManager.PERMISSION_GRANTED) {


                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            ListAmisAjout.this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            1
                    );
                }
            }else{
                RlvRechTel.setVisibility(View.INVISIBLE);
                RechercherRepertoire();
            }

        }else{
            RlvRechTel.setVisibility(View.VISIBLE);
        }

        //GDS TEST

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edtPhone.length()==10){
                    amis.clear();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            WebService WS = new WebService(getBaseContext());
                            JSONArray ListUsers = WS.GetListUserByPhone(edtPhone.getText().toString(),FragmentsSliderActivity.User);

                            if (ListUsers != null) {

                                String Url = null;

                                for (int i = 0; i < ListUsers.length(); i++) {
                                    JSONObject Amis = null;

                                    HashMap<String, Object> valeur = new HashMap<String, Object>();
                                    try {
                                        Amis = ListUsers.getJSONObject(i);
                                        valeur.put("pseudo", Amis.getString("pseudo"));

                                        Url = Amis.getString("photo").replace(" ", "%20");

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
                                            //bitmap = ModuleSmartcoder.getbitmap(Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));

                                            if(bitmap==null){
                                                bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                                                //valeur.put("LOGO", bitmap);
                                                //File fichier = ModuleSmartcoder.savebitmap(bitmap,Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));
                                            }
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }*/

                                        valeur.put("url", Url);

                                        //valeur.put("photo", bitmap);
                                        valeur.put("user", User);

                                        valeur.put("id_ami", Amis.getString("iduser"));



                                    } catch (JSONException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }

                                    amis.add(valeur);
                                }
                            }

                             AmisArray = new AdapterAmis(getBaseContext(), amis, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},true,true,true,null);


                            //mSchedule.setViewBinder(new MyViewBinder());
                            lstUsers.setAdapter(AmisArray);

                        }

                    });


                }else{
                    amis.clear();
                }
            }
        });



    }

    public void RechercherRepertoire(){
        WebService WS = new WebService(getBaseContext());
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        JSONArray ListUsers = WS.GetListUserByPhones(FragmentsSliderActivity.User);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            for (int i = 0; i < ListUsers.length(); i++) {
                try {
                    JSONObject Amis = ListUsers.getJSONObject(i);
                    if(Amis.getString("phone").trim().equals(phoneNumber.replace(" ","")) || Amis.getString("phone").trim().equals(phoneNumber.replace(" ","").replace("+336","06"))){
                        HashMap<String, Object> valeur = new HashMap<String, Object>();
                        try {
                            Amis = ListUsers.getJSONObject(i);
                            valeur.put("pseudo", Amis.getString("pseudo"));

                            String Url = Amis.getString("photo").replace(" ", "%20");

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
                                //bitmap = ModuleSmartcoder.getbitmap(Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));

                                if(bitmap==null){
                                    bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                                    //valeur.put("LOGO", bitmap);
                                    //File fichier = ModuleSmartcoder.savebitmap(bitmap,Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));
                                }
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            valeur.put("photo", bitmap);*/
                            valeur.put("user", User);
                            valeur.put("url", Url);

                            valeur.put("id_ami", Amis.getString("iduser"));



                        } catch (JSONException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        HashMap<String, Object> val;



                        if(amis.contains(valeur)==false){
                            amis.add(valeur);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
        phones.close();

        AmisArray = new AdapterAmis(getBaseContext(), amis, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},true,true,true,null);


        //mSchedule.setViewBinder(new MyViewBinder());
        lstUsers.setAdapter(AmisArray);
    }
}
