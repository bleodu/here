package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
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
import java.util.List;


public class LoginFacebook extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView info;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    TextView txFacebook;

    ImageButton imHomo;
    ImageButton imHetero;
    ImageButton imBi;
    int tendancesexe=-1;
    Utilisateur user;
    RelativeLayout btfacebook;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayout viewBi;
    LinearLayout viewHomo;
    LinearLayout viewHete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login_facebook);

        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");

        preferences = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = preferences.edit();


        TextView txEnregistrer = (TextView) findViewById(R.id.txEnregistrer);
        TextView txBi = (TextView) findViewById(R.id.txBi);
        TextView txHomo = (TextView) findViewById(R.id.txHomo);
        TextView txHetero = (TextView) findViewById(R.id.txhetero);
        txFacebook = (TextView) findViewById(R.id.txFacebook);
        viewBi = (LinearLayout) findViewById(R.id.viewbi);
        viewHomo = (LinearLayout) findViewById(R.id.viewhomo);
        viewHete = (LinearLayout) findViewById(R.id.viewhete);

        txBi.setTypeface(face);
        txHomo.setTypeface(face);
        txHetero.setTypeface(face);
        txEnregistrer.setTypeface(face);
        txFacebook.setTypeface(face);

        imBi = (ImageButton) findViewById(R.id.imBi);
        imHetero = (ImageButton) findViewById(R.id.imhete);
        imHomo = (ImageButton) findViewById(R.id.imHomo);
        btfacebook = (RelativeLayout) findViewById(R.id.btconnextfacebook);
        List<String> permissionNeeds= Arrays.asList("user_photos", "friends_photos", "email", "user_birthday", "user_friends");

        btfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tendancesexe>=0) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginFacebook.this, Arrays.asList("public_profile", "email","user_photos"));
                }else{
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.tendance),Toast.LENGTH_LONG).show();
                }

            }
        });

        imBi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imBi.setBackgroundColor(Color.parseColor("#2c2954"));
                imHetero.setBackgroundColor(Color.parseColor("#d2d2db"));
                imHomo.setBackgroundColor(Color.parseColor("#d2d2db"));
                tendancesexe=0;
            }
        });

        imHetero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imHetero.setBackgroundColor(Color.parseColor("#2c2954"));
                imHomo.setBackgroundColor(Color.parseColor("#d2d2db"));
                imBi.setBackgroundColor(Color.parseColor("#d2d2db"));
                tendancesexe=1;
            }
        });

        imHomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imHomo.setBackgroundColor(Color.parseColor("#2c2954"));
                imHetero.setBackgroundColor(Color.parseColor("#d2d2db"));
                imBi.setBackgroundColor(Color.parseColor("#d2d2db"));
                tendancesexe=2;
            }
        });

        viewBi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imBi.setBackgroundColor(Color.parseColor("#2c2954"));
                imHetero.setBackgroundColor(Color.parseColor("#d2d2db"));
                imHomo.setBackgroundColor(Color.parseColor("#d2d2db"));
                tendancesexe=0;
            }
        });

        viewHete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imHetero.setBackgroundColor(Color.parseColor("#2c2954"));
                imHomo.setBackgroundColor(Color.parseColor("#d2d2db"));
                imBi.setBackgroundColor(Color.parseColor("#d2d2db"));
                tendancesexe=1;
            }
        });

        viewHomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imHomo.setBackgroundColor(Color.parseColor("#2c2954"));
                imHetero.setBackgroundColor(Color.parseColor("#d2d2db"));
                imBi.setBackgroundColor(Color.parseColor("#d2d2db"));
                tendancesexe=2;
            }
        });




        //loginButton = (LoginButton)findViewById(R.id.login_button);
        /*loginButton.setBackgroundResource(R.drawable.boutonfacebook);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setCompoundDrawablePadding(0);
        loginButton.setPadding(0, 0, 0, 0);*/
        //loginButton.setText(" avec Facebook");
        //info = (TextView)findViewById(R.id.txRetour);



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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(Uti == null)
                {

                    user=new Utilisateur();
                }else{
                    user=Uti;
                }
                try {
                    if(object.has("last_name")){
                        user.login=object.getString("last_name");
                    }

                    user.email=object.getString("email");
                    user.id_facebook=object.getString("id");
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

                    user.tendancesexe=tendancesexe;
                    user.save();

                    editor = preferences.edit();
                    editor.putLong("UserId", user.getId());
                    editor.commit();



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                finish();
                startActivity(new Intent(getApplicationContext(),FragmentsSliderActivity.class));


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
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        //Profile profile = Profile.getCurrentProfile();


        //nextActivity(profile);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //LoginManager.getInstance().logOut();
        //Facebook login
        //accessTokenTracker.stopTracking();
        //profileTracker.stopTracking();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
