package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView bttel;
    RelativeLayout btfacebook;
    TextView txFacebook;
    private CallbackManager callbackManager;
    SharedPreferences.Editor editor;
    Utilisateur user;
    SharedPreferences pref;
    public static Utilisateur User=null;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        WebService WS = new WebService(getBaseContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();

        //User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));

        if(!pref.getString("idfb","").equals("")){
            User = WS.GetUserFacebook(pref.getString("idfb",""));
            User.connecte=true;
        }else if(!pref.getString("phone","").equals("")){
            User=WS.Connect(pref.getString("phone",""),pref.getString("pass",""));
            User.connecte=true;
        }else{
            User=null;
        }

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
                if((User.connecte==true && User.statut>1) || (User.id_facebook!=null && User.statut>1)){
                    //WebService WS = new WebService(getBaseContext());
                    //User=WS.SaveUser(User);
                    User=WS.Connect(User.phone,User.password);
                    User.save();
                    editor = pref.edit();
                    editor.putLong("UserId", User.getId());
                    editor.putString("phone",User.phone);
                    editor.putString("pass",User.password);
                    editor.commit();

                    finish();
                    startActivity(new Intent(getApplicationContext(),FragmentsSliderActivity.class));

                }else if(User.connecte==true && User.statut==1){
                    //WebService WS = new WebService(getBaseContext());
                    //User=WS.SaveUser(User);
                    editor = pref.edit();
                    editor.putLong("UserId", User.getId());
                    editor.putString("phone",User.phone);
                    editor.putString("pass",User.password);
                    editor.commit();
                    finish();
                    Intent i = new Intent(getApplicationContext(),SendMotPasse.class);
                    i.putExtra("idfacebook",pref.getString("idfb",""));
                    startActivity(i);
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

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                StringBuilder builder = new StringBuilder();
                GraphRequest request = new  GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me?fields=email,gender,picture,last_name,first_name,name,cover,birthday,albums",
                        null,
                        HttpMethod.GET
                );

                /* make the API call */
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/"+accessToken.getUserId()+"+/user_photos",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
            /* handle the result */
                            }
                        }
                ).executeAsync();

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,birthday,last_name,first_name,name,gender,cover,albums");
                request.setParameters(parameters);
                JSONObject object = request.executeAndWait().getJSONObject();


                WebService WS = new WebService(getBaseContext());
                Utilisateur Uti = null;
                try {
                    Uti = WS.GetUserFacebook(object.getString("id"));
                    if(object.has("first_name")){
                        Uti.login=object.getString("first_name");
                    }else{
                        Uti.login=object.getString("name");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(Uti == null || Uti.statut==0)
                {
                    user=new Utilisateur();
                    try {
                        if(object.has("first_name")){
                            user.login=object.getString("first_name");
                        }else{
                            user.login=object.getString("name");
                        }

                        user.email=object.getString("email");
                        user.id_facebook=object.getString("id");
                        editor.putString("idfb",object.getString("id"));
                        editor.commit();

                        if(object.has("gender")){
                            switch (object.getString("gender"))
                            {
                                case "male" /*Argument*/:
                                    user.sexe=0;
                                    break;
                                default:user.sexe=1;

                            }
                        }

                        if(object.has("birthday")){
                            SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
                            DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
                            Date date2=null;
                            try {
                                date2 = dateformat.parse(object.getString("birthday"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            int Age= Integer.parseInt(simpleDateformat.format(new Date()))- Integer.parseInt(simpleDateformat.format(date2));
                            user.age=Age;
                        }

                        //user.tendancesexe=tendancesexe;
                        user.save();

                        editor = pref.edit();
                        editor.putLong("UserId", user.getId());
                        editor.commit();
                        finish();
                        startActivity(new Intent(getApplicationContext(),LoginFacebook.class));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    user=Uti;
                    user.save();
                    editor = pref.edit();
                    editor.putLong("UserId", user.getId());
                    try {
                        editor.putString("idfb",object.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.commit();
                    editor.commit();
                    finish();
                    startActivity(new Intent(getApplicationContext(),FragmentsSliderActivity.class));

                }


                /*info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );*/
            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(),getResources().getString(R.string.annulfb),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.erreurfb),Toast.LENGTH_LONG).show();
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
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email","user_photos"));
                }

            }
        });






    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
