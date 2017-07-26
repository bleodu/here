package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView bttel;
    RelativeLayout btfacebook;
    TextView txFacebook;
    private CallbackManager callbackManager;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    public static Utilisateur User=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        WebService WS = new WebService(getBaseContext());


        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();

        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));

        if(!pref.getString("Langue","").equals("")){
            setLanguageForApp(pref.getString("codeLangue",""));
        }


        if(isOnline()==false){
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Connexion Internet");
            alertDialog.setMessage("Pas de connexion internet disponible. Veuillez vérifier vos paramètres de connexions.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }else{
            if(User != null)
            {
                if(User.connecte==true && (User.statut>1 || User.id_facebook!="")){
                    //WebService WS = new WebService(getBaseContext());
                    //User=WS.SaveUser(User);
                    User=WS.Connect(User.phone,User.password);
                    User.save();
                    editor = pref.edit();
                    editor.putLong("UserId", User.getId());
                    editor.commit();
                    finish();
                    startActivity(new Intent(getApplicationContext(),FragmentsSliderActivity.class));

                }else if(User.connecte==true && User.statut==1){
                    //WebService WS = new WebService(getBaseContext());
                    //User=WS.SaveUser(User);
                    editor = pref.edit();
                    editor.putLong("UserId", User.getId());
                    editor.commit();
                    finish();
                    startActivity(new Intent(getApplicationContext(),VerifSmsCode.class));
                }
            }

        }


        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        bttel= (TextView) findViewById(R.id.BtimTel);
        txFacebook= (TextView) findViewById(R.id.txFacebook);
        btfacebook= (RelativeLayout) findViewById(R.id.btimFacebook);

        bttel.setTypeface(face);
        txFacebook.setTypeface(face);



        bttel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()==false) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Connexion Internet");
                    alertDialog.setMessage("Pas de connexion internet disponible. Veuillez vérifier vos paramètres de connexions.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }else{
                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });

        btfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()==false) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Connexion Internet");
                    alertDialog.setMessage("Pas de connexion internet disponible. Veuillez vérifier vos paramètres de connexions.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }else{
                    Intent i = new Intent(getApplicationContext(),LoginFacebook.class);
                    startActivity(i);
                    finish();
                }

            }
        });



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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AccessToken token = AccessToken.getCurrentAccessToken();*/
        /*if(token != null)
        {
            startActivity(new Intent(getApplicationContext(),UserProfil.class));
        }*/
        //nextActivity(profile);
    }
}
