package com.kenfestoche.smartcoder.kenfestoche;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.eftimoff.viewpagertransformers.RotateUpTransformer;
import com.eftimoff.viewpagertransformers.StackTransformer;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import java.util.List;
import java.util.Vector;

public class FragmentsSliderActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    public static Utilisateur User;
    public static boolean Localiser=false;
    public static boolean MatchLocaliser=false;
    public static String UserMap;
    public static String IdUserMap;
    public static String latitude;
    public static String longitude;
    public static boolean ClicAmis=false;
    public static boolean ClicInconnu=false;
    public static boolean ClicMatch=false;
    public static boolean ClicSoiree=false;
    int newuser=0;
    ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_fragments_slider);

        if(getIntent().getExtras()!=null){
            Localiser = getIntent().getExtras().getBoolean("Localiser",false);
            MatchLocaliser = getIntent().getExtras().getBoolean("MatchLocaliser",false);
            latitude = getIntent().getExtras().getString("Latitude");
            longitude  = getIntent().getExtras().getString("Longitude");
            IdUserMap  = getIntent().getExtras().getString("IdUserMap");
            UserMap  = getIntent().getExtras().getString("UserMap");
            newuser  = getIntent().getExtras().getInt("newuser",0);
        }


        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        WebService WS = new WebService(getApplicationContext());
        //User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));
        if(!pref.getString("idfb","").equals("")){
            User = WS.GetUserFacebook(pref.getString("idfb",""));
            User.connecte=true;
        }else if(!pref.getString("phone","").equals("")){
            User=WS.Connect(pref.getString("phone",""),pref.getString("pass",""));
            User.connecte=true;
        }

        // Création de la liste de Fragments que fera défiler le PagerAdapter
        List fragments = new Vector();


        // Ajout des Fragments dans la liste
        fragments.add(Fragment.instantiate(this,MapActivity.class.getName()));
        fragments.add(Fragment.instantiate(this,ProfilsActivity.class.getName()));
        fragments.add(Fragment.instantiate(this,ContactActivity.class.getName()));
        fragments.add(Fragment.instantiate(this,UserProfil.class.getName()));

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

        pager = (ViewPager) super.findViewById(R.id.viewpager);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);
        //pager.setPageTransformer(true, new StackTransformer());

        if(Localiser){
            pager.setCurrentItem(0);
        }else if (newuser==1)
        {
            pager.setCurrentItem(3);
        }else{
            pager.setCurrentItem(1);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK && pager.getCurrentItem()!=1){
            pager.setCurrentItem(1);
            return false;
        }else{
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pager.setCurrentItem(1);
    }
}