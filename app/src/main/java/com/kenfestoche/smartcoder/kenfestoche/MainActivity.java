package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ImageButton bttel;
    ImageButton btfacebook;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        Profile profile = Profile.getCurrentProfile();

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

        if (profile != null) {
            startActivity(new Intent(getApplicationContext(),UserProfil.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        //nextActivity(profile);
    }
}
