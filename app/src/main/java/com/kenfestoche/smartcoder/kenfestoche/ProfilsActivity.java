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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eftimoff.viewpagertransformers.AccordionTransformer;
import com.eftimoff.viewpagertransformers.DrawFromBackTransformer;
import com.eftimoff.viewpagertransformers.RotateDownTransformer;
import com.eftimoff.viewpagertransformers.RotateUpTransformer;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kenfestoche.smartcoder.kenfestoche.model.ImagesProfil;
import com.kenfestoche.smartcoder.kenfestoche.model.SlidingImgProfil;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.picasso.Picasso;

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
import static com.kenfestoche.smartcoder.kenfestoche.R.id.txAge;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.txNbKiffs;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.txPseudo;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.vertical;

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
    ImageView imKiffBeurk;
    ImageView imPopupKiffs;
    String idUserKiff;
    int positionprofil;
    RelativeLayout rltProfil;
    int nbKiffs;
    ImageView pbKiffs;
    HideKiffsBeurk mHideKiffs;
    String IdUserKiffsPrecedent;
    TextView txNewKiff;
    TextView txKiff;
    ProgressBar pgChargement;
    RelativeLayout rlvProfilAll;


    ImageView btIgnorer;
    ImageView btAllerVoir;

    TextView txDistanceKiffs;
    TextView txRechercheProche;

    GetListProfils mLoadProfils;
    SlidingImgProfil ArrayProfils;
    public static ViewHolder viewHolder;

    RelativeLayout rlvNewKiff;
    public static boolean KiffValid;
    public static boolean kiffvalue;

    ArrayList<HashMap<String, Object>> profils;
    //private static ViewPager mPager;
    private SwipeFlingAdapterView flingContainer;
    MyAppAdapter myAppAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

        editor = pref.edit();
        User = FragmentsSliderActivity.User;


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_profils, container, false);


        profils =  new ArrayList<HashMap<String,Object>>();

        pager = (ViewPager) container;
        nbKiffs=1;
        imKiffBeurk = (ImageView) v.findViewById(R.id.imBeurkKiff);
        pgChargement = (ProgressBar) v.findViewById(R.id.pgChargement);
        pgChargement.setVisibility(View.INVISIBLE);
        imKiffBeurk.setVisibility(View.INVISIBLE);

        kiffvalue=false;
        KiffValid=false;

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        User.tokenFirebase=refreshedToken;
        WebService WS = new WebService(getContext());
        WS.SaveUser(User);

        //sleep for 1s in background...
        gps = new GPSTracker(v.getContext());
        pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

        new RefreshTask(v.getContext()).execute();

        BanniereContact = (ImageView) v.findViewById(R.id.imbanniereprofil);
        BanniereContact.setImageResource(R.drawable.banniereprofilkiff);
        imFlecheGauche = (ImageView) v.findViewById(R.id.imFlecheGauche);
        imFlecheDroite = (ImageView) v.findViewById(R.id.imFlecheDroite);
        boutonBeurk =  (ImageView) v.findViewById(R.id.imBeurk);
        boutonKiffe =  (ImageView) v.findViewById(R.id.imKiffe);
        btAllerVoir = (ImageView) v.findViewById(R.id.imAllerVoir);
        btIgnorer = (ImageView) v.findViewById(R.id.imIgnorer);
        pbKiffs =  (ImageView) v.findViewById(R.id.pbKiffe);
        txDistanceKiffs = (TextView) v.findViewById(R.id.txNbKiffs);
        txRechercheProche = (TextView) v.findViewById(R.id.txRechercheProfil);
        flingContainer = (SwipeFlingAdapterView) v.findViewById(R.id.frameProfil);
        rlvNewKiff = (RelativeLayout) v.findViewById(R.id.rlvNewKiff);
        rlvProfilAll = (RelativeLayout) v.findViewById(R.id.rlvprofilall);
        imPopupKiffs = (ImageView) v.findViewById(R.id.imPopUpKiffs);

        txNewKiff = (TextView) v.findViewById(R.id.txNewKiff);
        txKiff = (TextView) v.findViewById(R.id.txKiff);

        imPopupKiffs.setVisibility(View.INVISIBLE);



        txDistanceKiffs.setVisibility(View.INVISIBLE);
        boutonBeurk.setVisibility(View.INVISIBLE);
        boutonKiffe.setVisibility(View.INVISIBLE);
        rlvNewKiff.setVisibility(View.INVISIBLE);

        if(User.nbKiffs>10){
            boutonKiffe.setVisibility(View.INVISIBLE);
            boutonBeurk.setVisibility(View.INVISIBLE);
        }

        if(User.sexe==0 && User.tendancesexe==1) {
            txRechercheProche.setText("Recherche de filles proches...");
        }else if(User.sexe==1 && User.tendancesexe==1){
            txRechercheProche.setText("Recherche d'hommes proches...");
        }
        else if(User.sexe==1 && User.tendancesexe==2){
            txRechercheProche.setText("Recherche de filles proches...");
        }
        else if(User.sexe==0 && User.tendancesexe==2){
            txRechercheProche.setText("Recherche d'hommes proches...");
        }
        else if(User.tendancesexe==0){
            txRechercheProche.setText("Recherche d'hommes et de femmes proches...");
        }



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

        txRechercheProche.setTypeface(face);
        txNewKiff.setTypeface(face);
        txKiff.setTypeface(face);
        txDistanceKiffs.setTypeface(face);
        boutonBeurk.setImageResource(R.drawable.beurk);
        boutonKiffe.setImageResource(R.drawable.kiffe);

        flingContainer.setVisibility(View.VISIBLE);


        imFlecheDroite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imPopupKiffs.setVisibility(View.INVISIBLE);
                pager.setCurrentItem(2);
            }
        });

        imFlecheGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imPopupKiffs.setVisibility(View.INVISIBLE);
                pager.setCurrentItem(0);
            }
        });




        if(profils.size()>0){
            final HashMap<String, Object> profil = profils.get(0);
            idUserKiff= (String) profil.get("id_user");
        }else{
            txRechercheProche.setVisibility(View.VISIBLE);
        }

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                if(profils.size()>0){
                    Intent i = new Intent(getApplicationContext(),ProfilDetailActivity.class);
                    idUserKiff= (String) profils.get(0).get("id_user");
                    i.putExtra("id_user",idUserKiff);
                    startActivity(i);
                }
            }


        });

        rltProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(profils.size()>0){
                    Intent i = new Intent(getApplicationContext(),ProfilDetailActivity.class);
                    i.putExtra("id_user",idUserKiff);
                    startActivity(i);
                }

            }
        });

        btAllerVoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imPopupKiffs.setVisibility(View.INVISIBLE);
                Intent i = new Intent(getApplicationContext(),ProfilDetailActivity.class);
                rlvNewKiff.setVisibility(View.INVISIBLE);
                i.putExtra("id_user",IdUserKiffsPrecedent);
                i.putExtra("new_match",true);
                startActivity(i);
                pager.setCurrentItem(2);
            }
        });


        boutonKiffe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imPopupKiffs.setVisibility(View.INVISIBLE);
                if(profils.size()>0){

                    flingContainer.getTopCardListener().selectRight();



                }else{
                    txRechercheProche.setVisibility(View.VISIBLE);
                    //mPager.setVisibility(View.INVISIBLE);
                    txDistanceKiffs.setVisibility(View.INVISIBLE);
                }




                switch (User.nbKiffs){
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
                imPopupKiffs.setVisibility(View.INVISIBLE);
                if(profils.size()>0){

                    flingContainer.getTopCardListener().selectLeft();

                }



                boutonKiffe.setImageResource(R.drawable.kiffe);
                boutonKiffe.setEnabled(true);

            }
        });


        btIgnorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlvNewKiff.setVisibility(View.INVISIBLE);
            }
        });


        switch (User.nbKiffs){
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


        rlvProfilAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imPopupKiffs.setVisibility(View.INVISIBLE);
            }
        });



        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }


            @Override
            public void onLeftCardExit(Object dataObject) {
                imPopupKiffs.setVisibility(View.INVISIBLE);
                imKiffBeurk.setVisibility(View.INVISIBLE);
                //ShowBeurk show = new ShowBeurk();
                //show.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                if(mHideKiffs!=null){
                    mHideKiffs.cancel(true);
                }

                //mHideKiffs =new HideKiffsBeurk();
                idUserKiff= (String) profils.get(0).get(("id_user"));
                IdUserKiffsPrecedent = idUserKiff;
                //mHideKiffs.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                WebService WS = new WebService(getContext());
                WS.KiffUser(String.valueOf(User.id_user),idUserKiff,"4");
                User.nbKiffs= User.nbKiffs-1;
                //User.nbKiffs=nbKiffs;

                User.save();

                if(User.nbKiffs<0){
                    User.nbKiffs=0;
                    User.save();
                }


                switch (User.nbKiffs){
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


                profils.remove(0);
                myAppAdapter.notifyDataSetChanged();
                if(profils.size()>0){
                    JSONArray InfoUser = WS.GetinfoKiff((String) profils.get(0).get("id_user"),User.id_user);
                    if(InfoUser!=null){
                        idUserKiff= (String) profils.get(0).get("id_user");
                        try {
                            JSONObject userobj = InfoUser.getJSONObject(0);
                            if(userobj!=null) {
                                txDistanceKiffs.setText("kiffé " + userobj.getString("nbkiffs") + " fois \n" + "à " + userobj.getString("distance"));
                                //txNom.setText(userobj.getString("pseudo"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                boutonKiffe.setImageResource(R.drawable.kiffe);
                boutonKiffe.setEnabled(true);

                if(profils.size()>0){
                    txRechercheProche.setVisibility(View.INVISIBLE);
                    //mPager.setVisibility(View.VISIBLE);
                    txDistanceKiffs.setVisibility(View.VISIBLE);
                    boutonBeurk.setVisibility(View.VISIBLE);
                    boutonKiffe.setVisibility(View.VISIBLE);
                }else{
                    //mPager.setVisibility(View.INVISIBLE);
                    txRechercheProche.setVisibility(View.VISIBLE);
                    txDistanceKiffs.setVisibility(View.INVISIBLE);
                    boutonBeurk.setVisibility(View.INVISIBLE);
                    boutonKiffe.setVisibility(View.INVISIBLE);
                }

                ShowBeurk show = new ShowBeurk();
                show.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                HideKiffsAndBeurk hide = new HideKiffsAndBeurk();
                hide.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }


            @Override
            public void onRightCardExit(Object dataObject) {
                imPopupKiffs.setVisibility(View.INVISIBLE);
                if(User.nbKiffs<10){
                    imKiffBeurk.setVisibility(View.INVISIBLE);
                   /* ShowKiffs show = new ShowKiffs();
                    show.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
                    myAppAdapter.notifyDataSetChanged();

                    idUserKiff= (String) profils.get(0).get(("id_user"));
                    IdUserKiffsPrecedent = idUserKiff;
                    WebService WS = new WebService(getContext());
                    JSONArray Rep = WS.KiffUser(String.valueOf(User.id_user),idUserKiff,"1");
                    try {
                        JSONObject Retour=Rep.getJSONObject(0);
                        if(Retour.getString("NEWMATCH")=="1"){
                            Toast.makeText(getApplicationContext(), "Vous avez un nouveau match", Toast.LENGTH_LONG).show();
                            rlvNewKiff.setVisibility(View.VISIBLE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(User.nbKiffs<0){
                        User.nbKiffs=0;
                        User.save();
                    }



                    if(mHideKiffs!=null){
                        mHideKiffs.cancel(true);
                    }
                    profils.remove(0);

                    if(profils.size()>0){
                        JSONArray InfoUser = WS.GetinfoKiff((String) profils.get(0).get("id_user"),User.id_user);
                        if(InfoUser!=null){
                            idUserKiff= (String) profils.get(0).get("id_user");
                            try {
                                JSONObject userobj = InfoUser.getJSONObject(0);
                                if(userobj!=null) {
                                    txDistanceKiffs.setText("kiffé " + userobj.getString("nbkiffs") + " fois \n" + "à " + userobj.getString("distance"));
                                    //txNom.setText(userobj.getString("pseudo"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }



                    if(User.nbKiffs<10){
                        User.nbKiffs= User.nbKiffs+1;
                        User.save();
                        if(User.nbKiffs>10){
                            boutonKiffe.setImageResource(R.drawable.kiffgrise);
                            boutonKiffe.setEnabled(false);
                        }
                        //User.nbKiffs=nbKiffs;
                    }else{
                        boutonKiffe.setImageResource(R.drawable.kiffgrise);
                        boutonKiffe.setEnabled(false);
                        //boutonBeurk.setVisibility(View.INVISIBLE);

                    }

                    if(User.nbKiffs==2 && User.popupprofils==0){
                        imPopupKiffs.setVisibility(View.VISIBLE);
                        User.popupprofils=1;
                        User.save();
                        WS.SaveUser(User);
                    }


                    if(User.nbKiffs>9){
                        int nbKiffsRestant=10 - User.nbKiffs;
                        Toast.makeText(getContext(),"Il vous reste un seul kiffe; beurkez plus !",Toast.LENGTH_SHORT).show();
                        if(nbKiffsRestant<=0){
                            boutonKiffe.setImageResource(R.drawable.kiffgrise);
                            boutonKiffe.setEnabled(false);
                        }
                    }

                    switch (User.nbKiffs){
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

                    if(profils.size()>0){
                        txRechercheProche.setVisibility(View.INVISIBLE);
                        //mPager.setVisibility(View.VISIBLE);
                        txDistanceKiffs.setVisibility(View.VISIBLE);
                        boutonBeurk.setVisibility(View.VISIBLE);
                        boutonKiffe.setVisibility(View.VISIBLE);
                    }else{
                        //mPager.setVisibility(View.INVISIBLE);
                        txRechercheProche.setVisibility(View.VISIBLE);
                        txDistanceKiffs.setVisibility(View.INVISIBLE);
                        boutonBeurk.setVisibility(View.INVISIBLE);
                        boutonKiffe.setVisibility(View.INVISIBLE);
                    }

                    ShowKiffs show = new ShowKiffs();
                    show.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    HideKiffsAndBeurk hide = new HideKiffsAndBeurk();
                    hide.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }else{
                    boutonKiffe.setImageResource(R.drawable.kiffgrise);
                    boutonKiffe.setEnabled(false);
                    //Toast.makeText(getContext(),"Attention, vous ne pouvez plus kiffés.",Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {


                if(scrollProgressPercent>-0.30 && scrollProgressPercent<0.30){
                    HideKiffsBeurk hide = new HideKiffsBeurk();
                    hide.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    if(scrollProgressPercent<0){
                        ShowBeurk show = new ShowBeurk();
                        show.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    }else{
                        ShowKiffs show = new ShowKiffs();
                        show.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
                View view = flingContainer.getSelectedView();
                TextView txPseudo = (TextView) view.findViewById(R.id.txNomProfil);
                txPseudo.setText(profils.get(0).get("pseudo").toString());
                TextView txAge = (TextView) view.findViewById(R.id.txAge);
                txAge.setText(profils.get(0).get("age").toString());

            }
        });

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                LoadProfils();

            }
        });

        return v;
    }


    private class GetListProfils extends AsyncTask<Void, Integer, Void>
    {

        Boolean NewMess=false;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pgChargement.setVisibility(View.VISIBLE);
            //Toast.makeText(getApplicationContext(), "Début du traitement asynchrone", Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            // Mise à jour de la ProgressBar

            myAppAdapter.notifyDataSetChanged();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            WebService WS = new WebService(getContext());
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

                        valeur.put("Url", Url);


                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    profils.add(valeur);

                    publishProgress(0);
                    //ArrayProfils.notifyDataSetChanged();

                }
            }




            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            //mPager.setAdapter(ArrayProfils);
            //mPager.getAdapter().notifyDataSetChanged();

            pgChargement.setVisibility(View.INVISIBLE);
            flingContainer.setAdapter(myAppAdapter);


            if(profils.size()>0){
                txRechercheProche.setVisibility(View.INVISIBLE);
                //mPager.setVisibility(View.VISIBLE);
                txDistanceKiffs.setVisibility(View.VISIBLE);
                boutonBeurk.setVisibility(View.VISIBLE);
                boutonKiffe.setVisibility(View.VISIBLE);
                WebService WS = new WebService(getContext());
                JSONArray InfoUser = WS.GetinfoKiff((String) profils.get(0).get("id_user"),User.id_user);
                if(InfoUser!=null){
                    idUserKiff= (String) profils.get(0).get("id_user");
                    try {
                        JSONObject userobj = InfoUser.getJSONObject(0);
                        if(userobj!=null) {
                            txDistanceKiffs.setText("kiffé " + userobj.getString("nbkiffs") + " fois \n" + "à " + userobj.getString("distance"));
                            //txNom.setText(userobj.getString("pseudo"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                //mPager.setVisibility(View.INVISIBLE);
                txDistanceKiffs.setVisibility(View.INVISIBLE);
                boutonBeurk.setVisibility(View.INVISIBLE);
                boutonKiffe.setVisibility(View.INVISIBLE);

            }

        }


    }

    @Override
    public void onResume() {
        super.onResume();

        if(KiffValid)
        {
            if(kiffvalue)
            {
                flingContainer.getTopCardListener().selectRight();
            }else{
                flingContainer.getTopCardListener().selectLeft();
            }
            kiffvalue=false;
            KiffValid=false;
        }



    }



    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {

            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    LoadProfils();

                }
            });


        }
    }

    public void LoadProfils(){

        WebService WS = new WebService(getContext());
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

                    valeur.put("Url", Url);



                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                profils.add(valeur);



            }
        }

        myAppAdapter = new MyAppAdapter(profils, getActivity().getBaseContext());


        pgChargement.setVisibility(View.INVISIBLE);
        flingContainer.setAdapter(myAppAdapter);


        if(profils.size()>0){
            txRechercheProche.setVisibility(View.INVISIBLE);
            //mPager.setVisibility(View.VISIBLE);
            txDistanceKiffs.setVisibility(View.VISIBLE);
            boutonBeurk.setVisibility(View.VISIBLE);
            boutonKiffe.setVisibility(View.VISIBLE);

            JSONArray InfoUser = WS.GetinfoKiff((String) profils.get(0).get("id_user"),User.id_user);
            if(InfoUser!=null){
                idUserKiff= (String) profils.get(0).get("id_user");
                try {
                    JSONObject userobj = InfoUser.getJSONObject(0);
                    if(userobj!=null) {
                        txDistanceKiffs.setText("kiffé " + userobj.getString("nbkiffs") + " fois \n" + "à " + userobj.getString("distance"));
                        //txNom.setText(userobj.getString("pseudo"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            //mPager.setVisibility(View.INVISIBLE);
            txDistanceKiffs.setVisibility(View.INVISIBLE);
            boutonBeurk.setVisibility(View.INVISIBLE);
            boutonKiffe.setVisibility(View.INVISIBLE);

        }

    }

    class HideKiffsAndBeurk extends AsyncTask {



        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected Void doInBackground(Object... params) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            imKiffBeurk.setVisibility(View.INVISIBLE);
            //imKiffBeurk.setImageResource(R.drawable.kiffegros);
        }
    }


    class ShowKiffs extends AsyncTask {



        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected Void doInBackground(Object... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            imKiffBeurk.setVisibility(View.VISIBLE);
            imKiffBeurk.setImageResource(R.drawable.kiffegros);
        }
    }


    class ShowBeurk extends AsyncTask {



        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected Void doInBackground(Object... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            imKiffBeurk.setVisibility(View.VISIBLE);
            imKiffBeurk.setImageResource(R.drawable.beurkgros);
        }
    }

    class HideKiffsBeurk extends AsyncTask {



        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected Void doInBackground(Object... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            imKiffBeurk.setVisibility(View.INVISIBLE);
        }
    }

    class HideNewKiff extends AsyncTask {



        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected Void doInBackground(Object... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            rlvNewKiff.setVisibility(View.INVISIBLE);
        }
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



                            WebService WS = new WebService(getContext());
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

    public static class ViewHolder {
        public static FrameLayout background;
        public LinearLayout lnAffinite;
        public LinearLayout lnBoisson;
        public TextView txNom;
        public TextView txAge;
        public ImageView cardImage;
        ImageButton rbcalme1;
        ImageButton rbcalme2;
        ImageButton rbcalme3;
        ImageButton rbcalme4;
        ImageButton rbcalme5;

        ImageButton rbverre1;
        ImageButton rbverre2;
        ImageButton rbverre3;
        ImageButton rbverre4;
        ImageButton rbverre5;


    }

    public class MyAppAdapter extends BaseAdapter {


        public ArrayList<HashMap<String, Object>> profilsList;
        public Context context;
        LayoutInflater inflater;
        private MyAppAdapter(ArrayList<HashMap<String, Object>> data, Context context) {
            this.profilsList = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return profilsList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            if (rowView == null) {

                inflater =LayoutInflater.from(context);

                rowView = inflater.inflate(R.layout.slideimgprofil, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.txNom = (TextView) rowView.findViewById(R.id.txNomProfil);
                viewHolder.txAge = (TextView) rowView.findViewById(R.id.txAge);
                viewHolder.lnBoisson = (LinearLayout) rowView.findViewById(R.id.lnboisson);
                viewHolder.lnAffinite = (LinearLayout) rowView.findViewById(R.id.lnaffinite);
                viewHolder.rbcalme1 = (ImageButton) rowView.findViewById(R.id.rdcoeur1);
                viewHolder.rbcalme2 = (ImageButton) rowView.findViewById(R.id.rdcoeur2);
                viewHolder.rbcalme3 = (ImageButton) rowView.findViewById(R.id.rdcoeur3);
                viewHolder.rbcalme4 = (ImageButton) rowView.findViewById(R.id.rdcoeur4);
                viewHolder.rbcalme5 = (ImageButton) rowView.findViewById(R.id.rdcoeur5);

                viewHolder.rbverre1 = (ImageButton) rowView.findViewById(R.id.rdverre1);
                viewHolder.rbverre2 = (ImageButton) rowView.findViewById(R.id.rdverre2);
                viewHolder.rbverre3 = (ImageButton) rowView.findViewById(R.id.rdverre3);
                viewHolder.rbverre4 = (ImageButton) rowView.findViewById(R.id.rdverre4);
                viewHolder.rbverre5 = (ImageButton) rowView.findViewById(R.id.rdverre5);


                switch (Integer.parseInt(profilsList.get(position).get("calme").toString()))
                {
                    case 1:
                        viewHolder.rbcalme1.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme2.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme2.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbcalme3.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbcalme4.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbcalme5.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                        break;
                    case 2:
                        viewHolder.rbcalme1.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme2.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme3.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbcalme4.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbcalme5.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                        break;
                    case 3:
                        viewHolder.rbcalme1.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme2.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme3.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme4.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbcalme5.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                        break;
                    case 4:
                        viewHolder.rbcalme1.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme2.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme3.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme4.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme4.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme5.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                        break;
                    case 5:
                        viewHolder.rbcalme1.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme2.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme3.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme4.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme4.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbcalme5.setImageResource(R.drawable.carreselect);
                        viewHolder.rbcalme5.setBackgroundColor(Color.parseColor("#2c2954"));
                        break;
                    default:
                        viewHolder.rbcalme1.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme1.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbcalme2.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme2.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbcalme3.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbcalme4.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbcalme5.setImageResource(R.drawable.carre);
                        viewHolder.rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));

                }

                switch (Integer.parseInt(profilsList.get(position).get("affinity").toString()))
                {
                    case 1:
                        viewHolder.rbverre1.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre2.setImageResource(R.drawable.carre);
                        viewHolder.rbverre2.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbverre3.setImageResource(R.drawable.carre);
                        viewHolder.rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbverre4.setImageResource(R.drawable.carre);
                        viewHolder.rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbverre5.setImageResource(R.drawable.carre);
                        viewHolder.rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                        break;
                    case 2:
                        viewHolder.rbverre1.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre2.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre3.setImageResource(R.drawable.carre);
                        viewHolder.rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbverre4.setImageResource(R.drawable.carre);
                        viewHolder.rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbverre5.setImageResource(R.drawable.carre);
                        viewHolder.rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                        break;
                    case 3:
                        viewHolder.rbverre1.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre2.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre3.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre4.setImageResource(R.drawable.carre);
                        viewHolder.rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbverre5.setImageResource(R.drawable.carre);
                        viewHolder.rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                        break;
                    case 4:
                        viewHolder.rbverre1.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre2.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre3.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre4.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre4.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre5.setImageResource(R.drawable.carre);
                        viewHolder.rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                        break;
                    case 5:
                        viewHolder.rbverre1.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre2.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre3.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre4.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre4.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre5.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre5.setBackgroundColor(Color.parseColor("#2c2954"));
                        break;
                    default:
                        viewHolder.rbverre1.setImageResource(R.drawable.carreselect);
                        viewHolder.rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                        viewHolder.rbverre2.setImageResource(R.drawable.carre);
                        viewHolder.rbverre2.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbverre3.setImageResource(R.drawable.carre);
                        viewHolder.rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbverre4.setImageResource(R.drawable.carre);
                        viewHolder.rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                        viewHolder.rbverre5.setImageResource(R.drawable.carre);
                        viewHolder.rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));

                }
                //viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.image);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if(position<=0){
                viewHolder.txNom.setText(profilsList.get(position).get("pseudo").toString());
                viewHolder.txAge.setText(profilsList.get(position).get("age").toString());
                viewHolder.lnAffinite.setVisibility(View.VISIBLE);
                viewHolder.lnBoisson.setVisibility(View.VISIBLE);
            }else{
                viewHolder.txNom.setText("");
                viewHolder.txAge.setText("");
            }


            Picasso.with(context).load(profilsList.get(position).get("Url").toString()).into(viewHolder.cardImage);

            //Glide.with(ProfilsActivity.this).load(pictureURL).into(viewHolder.cardImage);

            return rowView;
        }
    }
}

