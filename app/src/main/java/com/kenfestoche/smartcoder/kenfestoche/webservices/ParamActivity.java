package com.kenfestoche.smartcoder.kenfestoche.webservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kenfestoche.smartcoder.kenfestoche.CGUActivity;
import com.kenfestoche.smartcoder.kenfestoche.FragmentsSliderActivity;
import com.kenfestoche.smartcoder.kenfestoche.LoginActivity;
import com.kenfestoche.smartcoder.kenfestoche.ParamPseudo;
import com.kenfestoche.smartcoder.kenfestoche.R;
import com.kenfestoche.smartcoder.kenfestoche.VerifSmsCode;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;

import java.util.Locale;

public class ParamActivity extends Activity {

    ImageView rdActivnotif;
    ImageView rdProfilSaufKiff;
    ImageView rdPositionAmis;
    ImageView rdPositionTous;
    ImageView rdInclusFb;

    ImageView imNomUtilisateur;

    ImageView imFleche;
    ImageView imNewLangue;
    ImageView imVerifProfil;

    TextView SuppCompte;
    TextView Politique;
    TextView Condition;
    TextView txModifUser;
    TextView txVerifProfil;
    TextView txActivNotif;
    TextView txCacherProfilKiffs;
    TextView txCacherPosAmis;
    TextView txCacherPosTous;
    TextView txInclusFB;
    TextView txNewLangue;
    TextView txHeader;

    LinearLayout lstVerifProfil;


    SharedPreferences.Editor editor;
    SharedPreferences pref;

    Utilisateur User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        User = FragmentsSliderActivity.User;

        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");
        Typeface faceGenerica=Typeface.createFromAsset(getAssets(),"Generica.otf");

        rdActivnotif = (ImageView) findViewById(R.id.rdnotif);
        rdProfilSaufKiff = (ImageView) findViewById(R.id.rdsaufkiffs);
        rdPositionAmis = (ImageView) findViewById(R.id.rdposamis);
        rdPositionTous = (ImageView) findViewById(R.id.rdpostous);
        rdInclusFb = (ImageView) findViewById(R.id.rdamisfb);

        lstVerifProfil = (LinearLayout) findViewById(R.id.lstVerifProfil);

        if(User.statut>1){
            lstVerifProfil.setVisibility(View.GONE);
        }

        txModifUser = (TextView) findViewById(R.id.txPseudo);
        txVerifProfil = (TextView) findViewById(R.id.txprofil);
        txActivNotif = (TextView) findViewById(R.id.txactivnotif);
        txCacherProfilKiffs = (TextView) findViewById(R.id.txsaufkiffs);
        txCacherPosAmis = (TextView) findViewById(R.id.txpositionamis);
        txCacherPosTous = (TextView) findViewById(R.id.txpositiontous);
        txInclusFB = (TextView) findViewById(R.id.txamisfb);
        txNewLangue = (TextView) findViewById(R.id.txLangue);
        txHeader = (TextView) findViewById(R.id.txHeader);

        txNewLangue.setTypeface(face);
        txModifUser.setTypeface(face);
        txVerifProfil.setTypeface(face);
        txActivNotif.setTypeface(face);
        txCacherProfilKiffs.setTypeface(face);
        txCacherPosAmis.setTypeface(face);
        txCacherPosTous.setTypeface(face);
        txInclusFB.setTypeface(face);

        imNomUtilisateur = (ImageView) findViewById(R.id.imNewPseudo);
        imVerifProfil= (ImageView) findViewById(R.id.imflecheprofil);
        imNewLangue = (ImageView) findViewById(R.id.imNewLangue);


        imFleche = (ImageView) findViewById(R.id.imFlecheGauche);
        Politique = (TextView) findViewById(R.id.txPolitique);
        Condition = (TextView) findViewById(R.id.txCondition);
        SuppCompte = (TextView) findViewById(R.id.btSupprimeCompte);

        SuppCompte.setTypeface(face);
        Politique.setTypeface(face);
        Condition.setTypeface(face);
        txHeader.setTypeface(faceGenerica);

        imNewLangue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLangue();
            }
        });

        txNewLangue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLangue();
            }
        });

        imVerifProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParamActivity.this, VerifSmsCode.class);
                startActivity(i);
            }
        });

        txVerifProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParamActivity.this, VerifSmsCode.class);
                startActivity(i);
            }
        });

        txModifUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParamActivity.this, ParamPseudo.class);
                startActivity(i);
            }
        });

        imNomUtilisateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParamActivity.this, ParamPseudo.class);
                startActivity(i);
            }
        });

        if(User.activnotif==1)
        {
            rdActivnotif.setImageResource(R.drawable.carreselect);
            rdActivnotif.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if(User.profilsaufkiffs==1)
        {
            rdProfilSaufKiff.setImageResource(R.drawable.carreselect);
            rdProfilSaufKiff.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if(User.positionamis==1)
        {
            rdPositionAmis.setImageResource(R.drawable.carreselect);
            rdPositionAmis.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if(User.positiontous==1)
        {
            rdPositionTous.setImageResource(R.drawable.carreselect);
            rdPositionTous.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if(User.inclusfb==1)
        {
            rdInclusFb.setImageResource(R.drawable.carreselect);
            rdInclusFb.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        imFleche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txActivNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.activnotif==0){
                    rdActivnotif.setImageResource(R.drawable.carreselect);
                    rdActivnotif.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.activnotif=1;
                }else{
                    rdActivnotif.setImageResource(R.drawable.carre);
                    rdActivnotif.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.activnotif=0;
                }
                User.errormess="okpourmoi";
                User.save();
            }
        });


        rdActivnotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.activnotif==0){
                    rdActivnotif.setImageResource(R.drawable.carreselect);
                    rdActivnotif.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.activnotif=1;
                }else{
                    rdActivnotif.setImageResource(R.drawable.carre);
                    rdActivnotif.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.activnotif=0;
                }
                User.errormess="okpourmoi";
                User.save();

            }

        });

        txCacherProfilKiffs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.profilsaufkiffs==0){
                    rdProfilSaufKiff.setImageResource(R.drawable.carreselect);
                    rdProfilSaufKiff.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.profilsaufkiffs=1;
                }else{
                    rdProfilSaufKiff.setImageResource(R.drawable.carre);
                    rdProfilSaufKiff.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.profilsaufkiffs=0;
                }

                User.save();
            }
        });


        rdProfilSaufKiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.profilsaufkiffs==0){
                    rdProfilSaufKiff.setImageResource(R.drawable.carreselect);
                    rdProfilSaufKiff.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.profilsaufkiffs=1;
                }else{
                    rdProfilSaufKiff.setImageResource(R.drawable.carre);
                    rdProfilSaufKiff.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.profilsaufkiffs=0;
                }

                User.save();
            }
        });

        txCacherPosAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.positionamis==0){
                    rdPositionAmis.setImageResource(R.drawable.carreselect);
                    rdPositionAmis.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.positionamis=1;
                }else{
                    rdPositionAmis.setImageResource(R.drawable.carre);
                    rdPositionAmis.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.positionamis=0;
                }

                User.save();
            }
        });

        rdPositionAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.positionamis==0){
                    rdPositionAmis.setImageResource(R.drawable.carreselect);
                    rdPositionAmis.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.positionamis=1;
                }else{
                    rdPositionAmis.setImageResource(R.drawable.carre);
                    rdPositionAmis.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.positionamis=0;
                }

                User.save();
            }
        });

        txCacherPosTous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.positiontous==0){
                    rdPositionTous.setImageResource(R.drawable.carreselect);
                    rdPositionTous.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.positiontous=1;
                }else{
                    rdPositionTous.setImageResource(R.drawable.carre);
                    rdPositionTous.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.positiontous=0;
                }

                User.save();
            }
        });

        rdPositionTous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.positiontous==0){
                    rdPositionTous.setImageResource(R.drawable.carreselect);
                    rdPositionTous.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.positiontous=1;
                }else{
                    rdPositionTous.setImageResource(R.drawable.carre);
                    rdPositionTous.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.positiontous=0;
                }

                User.save();
            }
        });

        txInclusFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.inclusfb==0){
                    rdInclusFb.setImageResource(R.drawable.carreselect);
                    rdInclusFb.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.inclusfb=1;
                }else{
                    rdInclusFb.setImageResource(R.drawable.carre);
                    rdInclusFb.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.inclusfb=0;
                }

                User.save();
            }
        });

        rdInclusFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.inclusfb==0){
                    rdInclusFb.setImageResource(R.drawable.carreselect);
                    rdInclusFb.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.inclusfb=1;
                }else{
                    rdInclusFb.setImageResource(R.drawable.carre);
                    rdInclusFb.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.inclusfb=0;
                }

                User.save();
            }
        });


        Politique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParamActivity.this, CGUActivity.class);
                startActivity(i);

            }
        });

        Condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParamActivity.this, CGUActivity.class);
                startActivity(i);

            }
        });

        SuppCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService(getBaseContext());
                WS.DeleteUser(User);
                editor.putLong("UserId", 0);
                editor.commit();
                Intent i = new Intent(ParamActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });



    }

    private void selectLangue() {

        final CharSequence[] items = { "Français", "Anglais", "Breton" };

        TextView title = new TextView(ParamActivity.this.getApplicationContext());
        title.setText("Sélectionner une langue");
        title.setBackgroundColor(Color.WHITE);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(
                ParamActivity.this);



        builder.setCustomTitle(title);

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Français")) {

                    editor.putString("Langue","Français");
                    editor.putString("codeLangue","fr");
                    editor.commit();
                    //txNewLangue.setText("Langue : Français");
                    setLanguageForApp("fr");
                    dialog.dismiss();

                }else if (items[item].equals("Breton")) {
                    editor.putString("Langue","Breton");
                    editor.putString("codeLangue","br");
                    //txNewLangue.setText("Langue : Breton");
                    setLanguageForApp("br");
                    editor.commit();

                    dialog.dismiss();
                }
                else {

                    editor.putString("Langue","Anglais");
                    editor.putString("codeLangue","en");
                    //txNewLangue.setText("Langue : Anglais");
                    setLanguageForApp("en");
                    editor.commit();

                    dialog.dismiss();
                }
            }
        });
        builder.show();




    }
    private void setLanguageForApp(String languageToLoad){
        Locale locale;
        if(languageToLoad.equals("not-set")){ //use any value for default
            locale = Locale.getDefault();
        }
        else {
            locale = new Locale(languageToLoad);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onStop() {
        super.onStop();
        WebService WS = new WebService(getBaseContext());
        WS.SaveUser(User);
    }
}
