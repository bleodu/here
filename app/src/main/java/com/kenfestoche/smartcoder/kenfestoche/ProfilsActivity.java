package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.kenfestoche.smartcoder.kenfestoche.model.SlidingImgProfil;
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

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.imFlecheDroite;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.imFlecheGauche;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.pagerProfil;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.txNbKiffs;

public class ProfilsActivity extends Fragment {

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
    ImageView imPhotoProfil;
    ImageView boutonBeurk;
    ImageView boutonKiffe;
    //TextView txAge;
    //TextView txNom;
    String idUserKiff;
    /*ImageButton rbcalme1;
    ImageButton rbcalme2;
    ImageButton rbcalme3;
    ImageButton rbcalme4;
    ImageButton rbcalme5;*/
    int positionprofil;
    //private OkHttpClient okHttpClient;
    RelativeLayout rltProfil;
    int nbKiffs;
    ImageView pbKiffs;

    TextView txDistanceKiffs;

    /*ImageButton rbverre1;
    ImageButton rbverre2;
    ImageButton rbverre3;
    ImageButton rbverre4;
    ImageButton rbverre5;*/

    ArrayList<HashMap<String, Object>> profils;
    private static ViewPager mPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pager = (ViewPager) container;

        return inflater.inflate(R.layout.activity_profils, container, false);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_profil);
        super.onActivityCreated(savedInstanceState);
        View v = getView();

        nbKiffs=1;



        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        //sleep for 1s in background...
        gps = new GPSTracker(v.getContext());
// create class object
        pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

        editor = pref.edit();
        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));
        if(User!=null){
            User.tokenFirebase = refreshedToken;
            WebService WS = new WebService();
            WS.SaveUser(User);

        }

        new RefreshTask(v.getContext()).execute();

        BanniereContact = (ImageView) v.findViewById(R.id.imbanniereprofil);
        BanniereContact.setImageResource(R.drawable.banniereprofilkiff);
        imFlecheGauche = (ImageView) v.findViewById(R.id.imFlecheGauche);
        imFlecheDroite = (ImageView) v.findViewById(R.id.imFlecheDroite);
        boutonBeurk =  (ImageView) v.findViewById(R.id.imBeurk);
        boutonKiffe =  (ImageView) v.findViewById(R.id.imKiffe);
        pbKiffs =  (ImageView) v.findViewById(R.id.pbKiffe);
        txDistanceKiffs = (TextView) v.findViewById(R.id.txNbKiffs);


        switch (nbKiffs){
            case 1 :
                pbKiffs.setImageResource(R.drawable.barrepos1);
                break;
            case 2 :
                pbKiffs.setImageResource(R.drawable.barrepos2);
                break;
            case 3 :
                pbKiffs.setImageResource(R.drawable.barrepos3);
                break;
            case 4 :
                pbKiffs.setImageResource(R.drawable.barrepos4);
                break;
            case 5 :
                pbKiffs.setImageResource(R.drawable.barrepos5);
                break;
            case 6 :
                pbKiffs.setImageResource(R.drawable.barrepos6);
                break;
            case 7 :
                pbKiffs.setImageResource(R.drawable.barrepos7);
                break;
            case 8 :
                pbKiffs.setImageResource(R.drawable.barrepos8);
                break;
            case 9 :
                pbKiffs.setImageResource(R.drawable.barrepos9);
                break;
            case 10 :
                pbKiffs.setImageResource(R.drawable.barrepos10);
                break;
        }

        rltProfil = (RelativeLayout) v.findViewById(R.id.rltProfil);

        Typeface face=Typeface.createFromAsset(getActivity().getAssets(),"fonts/weblysleekuil.ttf");

        /*rbcalme1 = (ImageButton) v.findViewById(R.id.rdcoeur1);
        rbcalme2 = (ImageButton) v.findViewById(R.id.rdcoeur2);
        rbcalme3 = (ImageButton) v.findViewById(R.id.rdcoeur3);
        rbcalme4 = (ImageButton) v.findViewById(R.id.rdcoeur4);
        rbcalme5 = (ImageButton) v.findViewById(R.id.rdcoeur5);


        rbverre1 = (ImageButton) v.findViewById(R.id.rdverre1);
        rbverre2 = (ImageButton) v.findViewById(R.id.rdverre2);
        rbverre3 = (ImageButton) v.findViewById(R.id.rdverre3);
        rbverre4 = (ImageButton) v.findViewById(R.id.rdverre4);
        rbverre5 = (ImageButton) v.findViewById(R.id.rdverre5);*/

        /*txNom = (TextView) v.findViewById(R.id.txNomProfil);
        txAge = (TextView) v.findViewById(R.id.txAge);

        txAge.setTypeface(face);
        txNom.setTypeface(face);*/

        txDistanceKiffs.setTypeface(face);
        boutonBeurk.setImageResource(R.drawable.beurk);
        boutonKiffe.setImageResource(R.drawable.kiffe);

        imFlecheDroite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(2);
            }
        });

        imFlecheGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(0);
            }
        });

        profils =  new ArrayList<HashMap<String,Object>>();

        WebService WS = new WebService();
        JSONArray ListProfils = WS.GetListProfils(User);

        if(ListProfils != null)
        {

            String Url=null;

            for (int i = 0; i < ListProfils.length(); i++) {
                JSONObject Amis=null;

                HashMap<String, Object> valeur = new HashMap<String, Object>();
                try {
                    Amis = ListProfils.getJSONObject(i);
                    valeur.put("pseudo", Amis.getString("pseudo"));
                    valeur.put("id_user", Amis.getString("id_user"));
                    valeur.put("age", Amis.getString("age"));
                    valeur.put("calme", Amis.getString("calme"));
                    valeur.put("affinity", Amis.getString("affinity"));

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

        mPager = (ViewPager) v.findViewById(R.id.pagerProfil);

        mPager.setAdapter(new SlidingImgProfil(getActivity().getBaseContext(),profils,false));

        if(profils.size()>0){
            final HashMap<String, Object> profil = profils.get(0);
            idUserKiff= (String) profil.get("id_user");

        }

        mPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ProfilDetailActivity.class);

                i.putExtra("id_user",idUserKiff);
                startActivity(i);
            }
        });

        rltProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ProfilDetailActivity.class);

                i.putExtra("id_user",idUserKiff);
                startActivity(i);
            }
        });


        /*final JSONArray[] InfoUser = {WS.GetinfoUser((String) profil.get("id_user"))};
        try {
            JSONObject userobj = InfoUser[0].getJSONObject(0);
            txAge.setText(userobj.getString("age") + " Ans");
            txNom.setText(userobj.getString("pseudo"));

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
        }*/

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

                HashMap<String, Object> profil = profils.get(positionprofil);
                profils.remove(profil);
                mPager.getAdapter().notifyDataSetChanged();
                mPager.setCurrentItem(mPager.getCurrentItem()+1);
                nbKiffs= nbKiffs+1;

                switch (nbKiffs){
                    case 1 :
                        pbKiffs.setImageResource(R.drawable.barrepos1);
                        break;
                    case 2 :
                        pbKiffs.setImageResource(R.drawable.barrepos2);
                        break;
                    case 3 :
                        pbKiffs.setImageResource(R.drawable.barrepos3);
                        break;
                    case 4 :
                        pbKiffs.setImageResource(R.drawable.barrepos4);
                        break;
                    case 5 :
                        pbKiffs.setImageResource(R.drawable.barrepos5);
                        break;
                    case 6 :
                        pbKiffs.setImageResource(R.drawable.barrepos6);
                        break;
                    case 7 :
                        pbKiffs.setImageResource(R.drawable.barrepos7);
                        break;
                    case 8 :
                        pbKiffs.setImageResource(R.drawable.barrepos8);
                        break;
                    case 9 :
                        pbKiffs.setImageResource(R.drawable.barrepos9);
                        break;
                    case 10 :
                        pbKiffs.setImageResource(R.drawable.barrepos10);
                        break;
                }

            }
        });

        boutonBeurk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService();
                WS.KiffUser(String.valueOf(User.id_user),idUserKiff,"4");
                HashMap<String, Object> profil = profils.get(positionprofil);
                profils.remove(profil);
                mPager.getAdapter().notifyDataSetChanged();
                mPager.setCurrentItem(mPager.getCurrentItem()+1);
                nbKiffs= nbKiffs-1;

                switch (nbKiffs){
                    case 1 :
                        pbKiffs.setImageResource(R.drawable.barrepos1);
                        break;
                    case 2 :
                        pbKiffs.setImageResource(R.drawable.barrepos2);
                        break;
                    case 3 :
                        pbKiffs.setImageResource(R.drawable.barrepos3);
                        break;
                    case 4 :
                        pbKiffs.setImageResource(R.drawable.barrepos4);
                        break;
                    case 5 :
                        pbKiffs.setImageResource(R.drawable.barrepos5);
                        break;
                    case 6 :
                        pbKiffs.setImageResource(R.drawable.barrepos6);
                        break;
                    case 7 :
                        pbKiffs.setImageResource(R.drawable.barrepos7);
                        break;
                    case 8 :
                        pbKiffs.setImageResource(R.drawable.barrepos8);
                        break;
                    case 9 :
                        pbKiffs.setImageResource(R.drawable.barrepos9);
                        break;
                    case 10 :
                        pbKiffs.setImageResource(R.drawable.barrepos10);
                        break;
                }

            }
        });


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(profils.size()>0){
                    HashMap<String, Object> profil = profils.get(position);
                    positionprofil=position;
                    WebService WS = new WebService();
                    JSONArray InfoUser = WS.GetinfoUser((String) profil.get("id_user"));
                    idUserKiff= (String) profil.get("id_user");
                    try {
                        JSONObject userobj = InfoUser.getJSONObject(0);
                        if(userobj!=null) {
                            txDistanceKiffs.setText("kiffés " + userobj.getString("nbkiffs") + " fois \n" + "à " + userobj.getString("distance"));
                            //txNom.setText(userobj.getString("pseudo"));

                       /* switch (userobj.getInt("calme")) {
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

                        }*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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

