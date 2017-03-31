package com.kenfestoche.smartcoder.kenfestoche;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.kenfestoche.smartcoder.kenfestoche.model.SlidingImgProfil;
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

import me.relex.circleindicator.CircleIndicator;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfilDetailActivity extends AppCompatActivity {

    ImageView BanniereContact;
    // GPSTracker class
    GPSTracker gps;
    boolean someCondition=true;
    Utilisateur User;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    ImageView imFlecheGauche;
    ViewPager pager;
    ImageView imFlecheDroite;
    ImageView imSignalement;
    ImageView boutonBeurk;
    ImageView boutonKiffe;
    TextView txAge;
    TextView txNom;
    TextView txDescription;

    String idUserKiff;
    ImageButton rbcalme1;
    ImageButton rbcalme2;
    ImageButton rbcalme3;
    ImageButton rbcalme4;
    ImageButton rbcalme5;
    int positionprofil;
    //private OkHttpClient okHttpClient;


    ImageButton rbverre1;
    ImageButton rbverre2;
    ImageButton rbverre3;
    ImageButton rbverre4;
    ImageButton rbverre5;

    String id_user;

    ArrayList<HashMap<String, Object>> profils;
    private static ViewPager mPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profildetail);

        id_user = this.getIntent().getExtras().getString("id_user");

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        //sleep for 1s in background...
        gps = new GPSTracker(this.getApplicationContext());
        // create class object
        pref = this.getSharedPreferences("EASER", this.MODE_PRIVATE);

        editor = pref.edit();
        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));
        if(User!=null){
            User.tokenFirebase = refreshedToken;
            WebService WS = new WebService();
            WS.SaveUser(User);

        }


        imFlecheGauche = (ImageView) findViewById(R.id.imFlecheGauche);
        imSignalement = (ImageView) findViewById(R.id.imSignalement);

        boutonBeurk =  (ImageView) findViewById(R.id.imBeurk);
        boutonKiffe =  (ImageView) findViewById(R.id.imKiffe);

        Typeface face=Typeface.createFromAsset(this.getAssets(),"fonts/weblysleekuil.ttf");

        rbcalme1 = (ImageButton) findViewById(R.id.rdcoeur1);
        rbcalme2 = (ImageButton) findViewById(R.id.rdcoeur2);
        rbcalme3 = (ImageButton) findViewById(R.id.rdcoeur3);
        rbcalme4 = (ImageButton) findViewById(R.id.rdcoeur4);
        rbcalme5 = (ImageButton) findViewById(R.id.rdcoeur5);


        rbverre1 = (ImageButton) findViewById(R.id.rdverre1);
        rbverre2 = (ImageButton) findViewById(R.id.rdverre2);
        rbverre3 = (ImageButton) findViewById(R.id.rdverre3);
        rbverre4 = (ImageButton) findViewById(R.id.rdverre4);
        rbverre5 = (ImageButton) findViewById(R.id.rdverre5);

        txNom = (TextView) findViewById(R.id.txNomProfil);
        txAge = (TextView) findViewById(R.id.txAge);
        txDescription = (TextView) findViewById(R.id.txDescription);

        txAge.setTypeface(face);
        txNom.setTypeface(face);
        txDescription.setTypeface(face);

        boutonBeurk.setImageResource(R.drawable.beurk);
        boutonKiffe.setImageResource(R.drawable.kiffe);

        imSignalement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService();
                WS.SignalerKiffs(User,id_user);
                Toast.makeText(view.getContext(),"Signalement envoyé",Toast.LENGTH_SHORT).show();
            }
        });

        imFlecheGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profils =  new ArrayList<HashMap<String,Object>>();

        WebService WS = new WebService();
        JSONArray ListProfils = WS.GetListPhotosProfil(id_user);

        if(ListProfils != null)
        {

            String Url=null;

            for (int i = 0; i < ListProfils.length(); i++) {
                JSONObject Amis=null;

                HashMap<String, Object> valeur = new HashMap<String, Object>();
                try {
                    Amis = ListProfils.getJSONObject(i);

                    Url = Amis.getString("photo").replace(" ","%20");

                    URL pictureURL;

                    pictureURL = null;

                    try {
                        pictureURL = new URL(Url);
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    Bitmap bitmap=null;
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

                    valeur.put("photo", bitmap);


                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                profils.add(valeur);
            }
        }

        mPager = (ViewPager) findViewById(R.id.pagerProfil);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        SlidingImgProfil mAdapter = new SlidingImgProfil(this.getBaseContext(),profils,true);
        mPager.setAdapter(mAdapter);
        indicator.setViewPager(mPager);
        mAdapter.registerDataSetObserver(indicator.getDataSetObserver());

        final JSONArray[] InfoUser = {WS.GetinfoUser(id_user)};
        try {
            JSONObject userobj = InfoUser[0].getJSONObject(0);
            txAge.setText(userobj.getString("age") + " Ans");
            txNom.setText(userobj.getString("pseudo"));
            txDescription.setText(userobj.getString("description"));

            switch (userobj.getInt("calme"))
            {
                case 1:
                    rbcalme1.setImageResource(R.drawable.carreselect);
                    rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme2.setImageResource(R.drawable.carre);
                    rbcalme2.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme3.setImageResource(R.drawable.carre);
                    rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme4.setImageResource(R.drawable.carre);
                    rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme5.setImageResource(R.drawable.carre);
                    rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 2:
                    rbcalme1.setImageResource(R.drawable.carreselect);
                    rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme2.setImageResource(R.drawable.carreselect);
                    rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme3.setImageResource(R.drawable.carre);
                    rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme4.setImageResource(R.drawable.carre);
                    rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme5.setImageResource(R.drawable.carre);
                    rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 3:
                    rbcalme1.setImageResource(R.drawable.carreselect);
                    rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme2.setImageResource(R.drawable.carreselect);
                    rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme3.setImageResource(R.drawable.carreselect);
                    rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme4.setImageResource(R.drawable.carre);
                    rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme5.setImageResource(R.drawable.carre);
                    rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 4:
                    rbcalme1.setImageResource(R.drawable.carreselect);
                    rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme2.setImageResource(R.drawable.carreselect);
                    rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme3.setImageResource(R.drawable.carreselect);
                    rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme4.setImageResource(R.drawable.carreselect);
                    rbcalme4.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme5.setImageResource(R.drawable.carre);
                    rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 5:
                    rbcalme1.setImageResource(R.drawable.carreselect);
                    rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme2.setImageResource(R.drawable.carreselect);
                    rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme3.setImageResource(R.drawable.carreselect);
                    rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme4.setImageResource(R.drawable.carreselect);
                    rbcalme4.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme5.setImageResource(R.drawable.carreselect);
                    rbcalme5.setBackgroundColor(Color.parseColor("#2c2954"));
                    break;
                default:
                    rbcalme1.setImageResource(R.drawable.carre);
                    rbcalme1.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme2.setImageResource(R.drawable.carre);
                    rbcalme2.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme3.setImageResource(R.drawable.carre);
                    rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme4.setImageResource(R.drawable.carre);
                    rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme5.setImageResource(R.drawable.carre);
                    rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));

            }

            switch (userobj.getInt("affinity"))
            {
                case 1:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carre);
                    rbverre2.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre3.setImageResource(R.drawable.carre);
                    rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre4.setImageResource(R.drawable.carre);
                    rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre5.setImageResource(R.drawable.carre);
                    rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 2:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carreselect);
                    rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre3.setImageResource(R.drawable.carre);
                    rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre4.setImageResource(R.drawable.carre);
                    rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre5.setImageResource(R.drawable.carre);
                    rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 3:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carreselect);
                    rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre3.setImageResource(R.drawable.carreselect);
                    rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre4.setImageResource(R.drawable.carre);
                    rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre5.setImageResource(R.drawable.carre);
                    rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 4:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carreselect);
                    rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre3.setImageResource(R.drawable.carreselect);
                    rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre4.setImageResource(R.drawable.carreselect);
                    rbverre4.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre5.setImageResource(R.drawable.carre);
                    rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 5:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carreselect);
                    rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre3.setImageResource(R.drawable.carreselect);
                    rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre4.setImageResource(R.drawable.carreselect);
                    rbverre4.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre5.setImageResource(R.drawable.carreselect);
                    rbverre5.setBackgroundColor(Color.parseColor("#2c2954"));
                    break;
                default:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carre);
                    rbverre2.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre3.setImageResource(R.drawable.carre);
                    rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre4.setImageResource(R.drawable.carre);
                    rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre5.setImageResource(R.drawable.carre);
                    rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boutonKiffe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService();
                JSONArray Rep = WS.KiffUser(String.valueOf(User.id_user),idUserKiff,"1");
                try {
                    JSONObject Retour=Rep.getJSONObject(0);
                    if(Retour.getString("NEWMATCH")=="1"){
                        Toast.makeText(getApplicationContext(), "Vous avez un nouveau match", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finish();
            }
        });

        boutonBeurk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService();
                WS.KiffUser(String.valueOf(User.id_user),idUserKiff,"4");

                finish();
            }
        });


       /* mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                HashMap<String, Object> profil = profils.get(position);
                positionprofil=position;
                WebService WS = new WebService();
                JSONArray InfoUser = WS.GetinfoUser((String) profil.get("id_user"));
                idUserKiff= (String) profil.get("id_user");
                try {
                    JSONObject userobj = InfoUser.getJSONObject(0);
                    if(userobj!=null) {
                        txAge.setText(userobj.getString("age") + " Ans");
                        txNom.setText(userobj.getString("pseudo"));

                        switch (userobj.getInt("calme")) {
                            case 1:
                                rbcalme1.setImageResource(R.drawable.carreselect);
                                rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme2.setImageResource(R.drawable.carre);
                                rbcalme2.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbcalme3.setImageResource(R.drawable.carre);
                                rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbcalme4.setImageResource(R.drawable.carre);
                                rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbcalme5.setImageResource(R.drawable.carre);
                                rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                                break;
                            case 2:
                                rbcalme1.setImageResource(R.drawable.carreselect);
                                rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme2.setImageResource(R.drawable.carreselect);
                                rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme3.setImageResource(R.drawable.carre);
                                rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbcalme4.setImageResource(R.drawable.carre);
                                rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbcalme5.setImageResource(R.drawable.carre);
                                rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                                break;
                            case 3:
                                rbcalme1.setImageResource(R.drawable.carreselect);
                                rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme2.setImageResource(R.drawable.carreselect);
                                rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme3.setImageResource(R.drawable.carreselect);
                                rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme4.setImageResource(R.drawable.carre);
                                rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbcalme5.setImageResource(R.drawable.carre);
                                rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                                break;
                            case 4:
                                rbcalme1.setImageResource(R.drawable.carreselect);
                                rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme2.setImageResource(R.drawable.carreselect);
                                rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme3.setImageResource(R.drawable.carreselect);
                                rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme4.setImageResource(R.drawable.carreselect);
                                rbcalme4.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme5.setImageResource(R.drawable.carre);
                                rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                                break;
                            case 5:
                                rbcalme1.setImageResource(R.drawable.carreselect);
                                rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme2.setImageResource(R.drawable.carreselect);
                                rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme3.setImageResource(R.drawable.carreselect);
                                rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme4.setImageResource(R.drawable.carreselect);
                                rbcalme4.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbcalme5.setImageResource(R.drawable.carreselect);
                                rbcalme5.setBackgroundColor(Color.parseColor("#2c2954"));
                                break;
                            default:
                                rbcalme1.setImageResource(R.drawable.carre);
                                rbcalme1.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbcalme2.setImageResource(R.drawable.carre);
                                rbcalme2.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbcalme3.setImageResource(R.drawable.carre);
                                rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbcalme4.setImageResource(R.drawable.carre);
                                rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbcalme5.setImageResource(R.drawable.carre);
                                rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));

                        }

                        switch (userobj.getInt("affinity")) {
                            case 1:
                                rbverre1.setImageResource(R.drawable.carreselect);
                                rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre2.setImageResource(R.drawable.carre);
                                rbverre2.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbverre3.setImageResource(R.drawable.carre);
                                rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbverre4.setImageResource(R.drawable.carre);
                                rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbverre5.setImageResource(R.drawable.carre);
                                rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                                break;
                            case 2:
                                rbverre1.setImageResource(R.drawable.carreselect);
                                rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre2.setImageResource(R.drawable.carreselect);
                                rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre3.setImageResource(R.drawable.carre);
                                rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbverre4.setImageResource(R.drawable.carre);
                                rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbverre5.setImageResource(R.drawable.carre);
                                rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                                break;
                            case 3:
                                rbverre1.setImageResource(R.drawable.carreselect);
                                rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre2.setImageResource(R.drawable.carreselect);
                                rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre3.setImageResource(R.drawable.carreselect);
                                rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre4.setImageResource(R.drawable.carre);
                                rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbverre5.setImageResource(R.drawable.carre);
                                rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                                break;
                            case 4:
                                rbverre1.setImageResource(R.drawable.carreselect);
                                rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre2.setImageResource(R.drawable.carreselect);
                                rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre3.setImageResource(R.drawable.carreselect);
                                rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre4.setImageResource(R.drawable.carreselect);
                                rbverre4.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre5.setImageResource(R.drawable.carre);
                                rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                                break;
                            case 5:
                                rbverre1.setImageResource(R.drawable.carreselect);
                                rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre2.setImageResource(R.drawable.carreselect);
                                rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre3.setImageResource(R.drawable.carreselect);
                                rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre4.setImageResource(R.drawable.carreselect);
                                rbverre4.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre5.setImageResource(R.drawable.carreselect);
                                rbverre5.setBackgroundColor(Color.parseColor("#2c2954"));
                                break;
                            default:
                                rbverre1.setImageResource(R.drawable.carreselect);
                                rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                                rbverre2.setImageResource(R.drawable.carre);
                                rbverre2.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbverre3.setImageResource(R.drawable.carre);
                                rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbverre4.setImageResource(R.drawable.carre);
                                rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                                rbverre5.setImageResource(R.drawable.carre);
                                rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/


    }


    class RefreshTask extends AsyncTask {

        Context context;
        public RefreshTask(Context c) {
            super();
            context=c;

            // do stuff
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected Void doInBackground(Object... params) {
            while(someCondition) {
                try {

                    // check if GPS enabled
                    if(gps.canGetLocation()){
                        if(User!=null){
                            User.latitude = gps.getLatitude();
                            User.longitude = gps.getLongitude();



                            WebService WS = new WebService();
                            WS.SetLastPosition(User);
                        }



                    }else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        //gps.showSettingsAlert();
                    }
                    Thread.sleep(60000);
                    //and update textview in ui thread
                    publishProgress();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                };

            }
            return null;
        }
    }
}

