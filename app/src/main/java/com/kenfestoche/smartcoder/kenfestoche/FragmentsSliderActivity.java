package com.kenfestoche.smartcoder.kenfestoche;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eftimoff.viewpagertransformers.RotateUpTransformer;
import com.eftimoff.viewpagertransformers.StackTransformer;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;

import java.util.List;
import java.util.Vector;

public class FragmentsSliderActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    public static Utilisateur User;
    public static boolean Localiser=false;
    public static String latitude;
    public static String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_fragments_slider);

        if(getIntent().getExtras()!=null){
            Localiser = getIntent().getExtras().getBoolean("Localiser",false);
            latitude = getIntent().getExtras().getString("Latitude");
            longitude  = getIntent().getExtras().getString("Longitude");
        }


        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();

        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));
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

        ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);
        //pager.setPageTransformer(true, new StackTransformer());

        if(Localiser){
            pager.setCurrentItem(0);
        }else{
            pager.setCurrentItem(1);
        }

    }
}