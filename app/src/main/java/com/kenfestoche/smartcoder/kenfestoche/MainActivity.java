package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ImageButton bttel;
    ImageButton btfacebook;
    private CallbackManager callbackManager;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Utilisateur User=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();

        //User = new Utilisateur();
        //User.save();

        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));

        if(User != null)
        {
            if(User.connecte==true && (User.statut>1 || User.id_facebook!="")){
                WebService WS = new WebService();
                User=WS.SaveUser(User);
                editor = pref.edit();
                editor.putLong("UserId", User.getId());
                editor.commit();
                finish();
                startActivity(new Intent(getApplicationContext(),FragmentsSliderActivity.class));

            }else if(User.connecte==true && User.statut==1){
                WebService WS = new WebService();
                User=WS.SaveUser(User);
                editor = pref.edit();
                editor.putLong("UserId", User.getId());
                editor.commit();
                finish();
                startActivity(new Intent(getApplicationContext(),VerifSmsCode.class));
            }
        }

        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        bttel= (ImageButton) findViewById(R.id.BtimTel);
        btfacebook= (ImageButton) findViewById(R.id.btimFacebook);


        bttel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginFacebook.class);
                startActivity(i);
                finish();
            }
        });



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
