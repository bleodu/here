package com.kenfestoche.smartcoder.kenfestoche;

import android.*;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kenfestoche.smartcoder.kenfestoche.model.ImagesProfil;
import com.kenfestoche.smartcoder.kenfestoche.model.MyGridPhoto;
import com.kenfestoche.smartcoder.kenfestoche.model.SwipeGestureDetector;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.ParamActivity;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;
//import com.squareup.okhttp.OkHttpClient;


import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.acl.Permission;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.kenfestoche.smartcoder.kenfestoche.R.id.btDeconnect;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.edtqqmot;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.txHeader;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.txaffinite;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.txverre;


public class UserProfil extends Fragment {

    ImageButton rbcalme1;
    ImageButton rbcalme2;
    ImageButton rbcalme3;
    ImageButton rbcalme4;
    ImageButton rbcalme5;

    //private OkHttpClient okHttpClient;


    ImageButton rbverre1;
    ImageButton rbverre2;
    ImageButton rbverre3;
    ImageButton rbverre4;
    ImageButton rbverre5;

    private View mProgressView;
    private View mCreateUserView;
    private String pictureImagePath = "";

    Button Parametre;
    ImagesProfil adapterPhoto;

    LoadImage mLoadImage;
    Button Valider;

    MyGridPhoto gridPhotos;
    Uri outputFileUri;
    EditText Edtqqmot;

    ImageView imgAdd;

    Utilisateur User;

    TextView txCalme;
    TextView txFetard;
    TextView txAmis;
    TextView txAmour;
    TextView txPhotos;
    TextView txPres;
    TextView txDeconnexion;
    TextView txPlutot;
    ImageButton btParam;
    AccessToken token;
    TextView TxNbCaract;
    boolean newUser;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    String picturePath;
    private GestureDetector mGestureDetector;
    private Uri mImageCaptureUri;
    Activity MonActivity;
    ImageView imLignePlutot;
    ImageView imLignePhotos;
    ImageView imLignePres;
    ViewPager pager;
    ImageView imFlecheGauche;
    TextView txHeader;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pager = (ViewPager) container;


        return inflater.inflate(R.layout.activity_user_profil, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MonActivity = getActivity();

        pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

        editor = pref.edit();
        User = FragmentsSliderActivity.User;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        gridPhotos = (MyGridPhoto) MonActivity.findViewById(R.id.gridphotos);

        //mLoadImage =new LoadImage();

        //mLoadImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_profil);
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        MonActivity = getActivity();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        User = FragmentsSliderActivity.User;
        Typeface face=Typeface.createFromAsset(getActivity().getAssets(),"fonts/weblysleekuil.ttf");

        newUser=false;

        Typeface faceGenerica=Typeface.createFromAsset(getActivity().getAssets(),"Generica.otf");

        txHeader = (TextView) v.findViewById(R.id.txHeader);
        txHeader.setTypeface(faceGenerica);

        rbcalme1 = (ImageButton) v.findViewById(R.id.rdcalme1);
        rbcalme2 = (ImageButton) v.findViewById(R.id.rdcalme2);
        rbcalme3 = (ImageButton) v.findViewById(R.id.rdcalme3);
        rbcalme4 = (ImageButton) v.findViewById(R.id.rdcalme4);
        rbcalme5 = (ImageButton) v.findViewById(R.id.rdcalme5);


        rbverre1 = (ImageButton) v.findViewById(R.id.rdverre1);
        rbverre2 = (ImageButton) v.findViewById(R.id.rdverre2);
        rbverre3 = (ImageButton) v.findViewById(R.id.rdverre3);
        rbverre4 = (ImageButton) v.findViewById(R.id.rdverre4);
        rbverre5 = (ImageButton) v.findViewById(R.id.rdverre5);

        imLignePlutot = (ImageView) v.findViewById(R.id.imLignePLutot);
        imLignePhotos = (ImageView) v.findViewById(R.id.imLignePhoto);
        imLignePres = (ImageView) v.findViewById(R.id.imLignePres);

        imLignePlutot.setImageResource(R.drawable.ligne);
        imLignePhotos.setImageResource(R.drawable.ligne);
        imLignePres.setImageResource(R.drawable.ligne);

        imFlecheGauche = (ImageView) v.findViewById(R.id.imFlecheGaucheUser);


        mProgressView = (View) v.findViewById(R.id.userprofil_progress);
        mCreateUserView = (View) v.findViewById(R.id.vueuserprofil);

        gridPhotos = (MyGridPhoto) v.findViewById(R.id.gridphotos);


        btParam = (ImageButton) v.findViewById(R.id.imParam);

        Valider = (Button) v.findViewById(R.id.btValidProfil);

        Edtqqmot = (EditText) v.findViewById(R.id.edtqqmot);

        txCalme = (TextView) v.findViewById(R.id.txcalme);
        txFetard = (TextView) v.findViewById(R.id.txfetard);
        txAmis = (TextView) v.findViewById(R.id.txverre);
        txAmour = (TextView) v.findViewById(R.id.txaffinite);
        txPhotos = (TextView) v.findViewById(R.id.txPhotos);
        txPlutot = (TextView) v.findViewById(R.id.txPlutot);
        txPres = (TextView) v.findViewById(R.id.txPresentation);
        imgAdd = (ImageView) v.findViewById(R.id.imAdd);
        TxNbCaract = (TextView) v.findViewById(R.id.txNbCaract);

        txDeconnexion = (TextView) v.findViewById(R.id.btDeconnect);

        Edtqqmot.setTypeface(face);
        Valider.setTypeface(face);
        txCalme.setTypeface(face);
        txFetard.setTypeface(face);
        txAmis.setTypeface(face);
        txAmour.setTypeface(face);
        txPhotos.setTypeface(face);
        txPlutot.setTypeface(face);
        txPres.setTypeface(face);
        txDeconnexion.setTypeface(face);

        // Create an object of the Android_Gesture_Detector  Class
        SwipeGestureDetector  android_gesture_detector  =  new SwipeGestureDetector();
// Create a GestureDetector

        mGestureDetector = new GestureDetector(getActivity(), android_gesture_detector);


        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity());


        Bundle extras = getActivity().getIntent().getExtras();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final Bundle params = new Bundle();
        params.putString("fields", "id,email,birthday,user_photos,last_name,first_name,name,gender,cover,picture.type(large)");
        token = AccessToken.getCurrentAccessToken();

        if(token != null){

            GraphRequest request = new  GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me?fields=email,gender,picture,last_name,first_name,name,cover,birthday",
                    null,
                    HttpMethod.GET
            );

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email,birthday,last_name,first_name,name,gender,cover,picture.type(large)");
            //request.setParameters(parameters);
            JSONObject object = request.executeAndWait().getJSONObject();


            try {

                //recherche si l'utilisateur facebbok est déjà présent dans la base de données
                WebService WS = new WebService(getContext());
                Utilisateur Uti = WS.GetUserFacebook(object.getString("id"));
                if(Uti == null)
                {
                    newUser=true;
                    User=new Utilisateur();
                }else{
                    User=Uti;
                }

                if(object.has("first_name")){
                    Uti.login=object.getString("first_name");
                }else{
                    Uti.login=object.getString("name");
                }
                User.email = object.getString("email");
                User.id_facebook = object.getString("id");

                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                if(refreshedToken!=null){
                    User.tokenFirebase=refreshedToken;
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
                    User.age=Age;
                }
                User.connecte=true;
                User.save();
                //WS = new WebService(getContext());
                WS.SaveUser(User);

                /*if (object.has("picture")) {

                    Bitmap bm = null;
                    try {

                        URL aURL = new URL("http://graph.facebook.com/" + token.getUserId() + "/picture?type=large");
                        URLConnection conn = aURL.openConnection();
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setUseCaches(false);
                        conn.connect();
                        //InputStream is = conn.getInputStream();
                        //BufferedInputStream bis = new BufferedInputStream(is);
                        //Uri imageUri = Profile.getCurrentProfile().getProfilePictureUri(200,200);
                        URL url = new URL(Profile.getCurrentProfile().getProfilePictureUri(150, 150).toString());
                        bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(getApplicationContext(), bm);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        WebService WebSer = new WebService(getContext());
                        WebSer.UploadImage(finalFile.getPath(), User);
                        adapterPhoto = new ImagesProfil(getApplicationContext(), 1, User);

                        gridPhotos.setAdapter(adapterPhoto);
                        //bis.close();
                        //is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // set profilePic bitmap to imageview
                }*/
                if(object.has("gender")){
                    switch (object.getString("gender")) {
                        case "male" /*Argument*/:
                            User.sexe = 0;
                            break;
                        default:
                            User.sexe = 1;

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            if(newUser==true){
                /* make the API call */
                request =  new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/"+token.getUserId()+"/albums",
                        null,
                        HttpMethod.GET);

                try {
                    JSONArray data = request.executeAndWait().getJSONObject().getJSONArray("data");
                    for(int i=0; i<1; i++){
                        JSONObject Album = data.getJSONObject(i);
                        String id_album=Album.getString("id");

                        GraphRequest requete = new  GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/"+id_album+"/photos?fields=link,images",
                                null,
                                HttpMethod.GET
                        );

                        JSONArray donnee = requete.executeAndWait().getJSONObject().getJSONArray("data");

                        //JSONObject images = donnee.getJSONObject(1);
                        int totimg = donnee.length();
                        if(totimg>4)
                        {
                            totimg=4;
                        }
                        for(int j=0; j<totimg; j++){
                            JSONArray Photo = donnee.getJSONObject(j).getJSONArray("images");
                            String linkphoto=Photo.getJSONObject(0).getString("source");


                            URL url = new URL(linkphoto);
                                        /*okHttpClient = new OkHttpClient();
                                        picasso = new Picasso.Builder(getApplicationContext())
                                                .downloader(new OkHttpDownloader(okHttpClient))
                                                .build();

                                        picasso.with(getApplicationContext()).load(linkphoto).into(imgAdd);*/
                            //Bitmap bitmap = ((BitmapDrawable)imgAdd.getDrawable()).getBitmap();

                                        /*HttpsURLConnection.setFollowRedirects(true);
                                        conn1.setInstanceFollowRedirects(true);
                                        ImageView img = null;*/
                            Bitmap bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                            Uri tempUri = getImageUri(getActivity().getApplicationContext(), bm);

                            // CALL THIS METHOD TO GET THE ACTUAL PATH
                            File finalFile = new File(getRealPathFromURI(tempUri));
                            WebService WebSer = new WebService(getContext());
                            WebSer.UploadImage(finalFile.getPath(),User);
                            //WebService WebSer = new WebService(getContext());

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }else{
            //User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));
            User.connecte=true;
            //User.statut=2;
            //User.save();
            WebService WS = new WebService(getContext());
            WS.SaveUser(User);
        }


        //User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));




        imFlecheGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(2);
            }
        });

        txDeconnexion.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


        gridPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView photo= (ImageView) view.findViewById(R.id.imphotoprofil);
                //Integer IDphoto = photo.getId();
                if(photo==null){
                    selectImage();
                }else{

                }

            }
        });

        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.description=Edtqqmot.getText().toString();
                WebService WS = new WebService(getContext());
                WS.SaveUser(User);
                Toast.makeText(getActivity().getApplicationContext(),"Profil enregistré",Toast.LENGTH_LONG).show();
                pager.setCurrentItem(1);
            }
        });

        txDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessToken token = AccessToken.getCurrentAccessToken();
                if(token != null){
                    User.connecte=false;
                    LoginManager.getInstance().logOut();
                }else{
                    User.connecte=false;
                }
                User.statut=2;
                User.save();
                User.delete();

                editor = pref.edit();
                editor.putString("phone","");
                editor.putString("pass","");
                editor.putString("idfb","");
                editor.putLong("UserId", 0);
                editor.commit();
                Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        btParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(),ParamActivity.class);
                startActivity(i);

            }
        });

        rbcalme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                User.calme=1;
            }
        });

        rbcalme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                User.calme=2;
            }
        });

        rbcalme3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                User.calme=3;
            }
        });

        rbcalme4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                User.calme=4;
            }
        });

        rbcalme5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                User.calme=5;
            }
        });

        rbverre1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                User.affinity=1;
            }
        });

        rbverre2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                User.affinity=2;
            }
        });

        rbverre3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                User.affinity=3;
            }
        });

        rbverre4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                User.affinity=4;
            }
        });

        rbverre5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                User.affinity=5;
            }
        });


        if(User != null){
            Edtqqmot.setText(User.description);
            int NbCaract = 150 - Edtqqmot.length();
            TxNbCaract.setText(String.valueOf(NbCaract));
            switch (User.calme)
            {
                case 1:
                    rbcalme1.performClick();
                    break;
                case 2:
                    rbcalme2.performClick();
                    break;
                case 3:
                    rbcalme3.performClick();
                    break;
                case 4:
                    rbcalme4.performClick();
                    break;
                case 5:
                    rbcalme5.performClick();
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

            switch (User.affinity)
            {
                case 1:
                    rbverre1.performClick();
                    break;
                case 2:
                    rbverre2.performClick();
                    break;
                case 3:
                    rbverre3.performClick();
                    break;
                case 4:
                    rbverre4.performClick();
                    break;
                case 5:
                    rbverre5.performClick();
                    break;
                default:
                    rbverre1.setImageResource(R.drawable.carre);
                    rbverre1.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre2.setImageResource(R.drawable.carre);
                    rbverre2.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre3.setImageResource(R.drawable.carre);
                    rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre4.setImageResource(R.drawable.carre);
                    rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre5.setImageResource(R.drawable.carre);
                    rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
            }
        }

        Edtqqmot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int NbCaract = 150 - Edtqqmot.length();
                TxNbCaract.setText(String.valueOf(NbCaract));
            }
        });

        MonActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                adapterPhoto=new ImagesProfil(MonActivity.getApplicationContext(),1,User,MonActivity);
                //adapterPhoto.notifyDataSetChanged();
                gridPhotos.setAdapter(adapterPhoto);

            }
        });




    }


    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return super.getActivity().onTouchEvent(event);
        // Return true if you have consumed the event, false if you haven't.
        // The default implementation always returns false.
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.getActivity().dispatchTouchEvent(ev);
        mGestureDetector.onTouchEvent(ev);
        return super.getActivity().onTouchEvent(ev);
    }*/

    @Override
    public void onPause() {
        super.onPause();
        User.description=Edtqqmot.getText().toString();
        WebService WS = new WebService(getContext());
        WS.SaveUser(User);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        User.description=Edtqqmot.getText().toString();
        WebService WS = new WebService(getContext());
        WS.SaveUser(User);
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        /*intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);*/
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        if (requestCode == 0) { //PHOTO DANS LA GALERIE
            Uri selectedImage = data.getData();
            File finalFile = new File(getRealPathFromURI(selectedImage));
            //mImageCaptureUri= (Uri) data.getExtras().get("URI");
            picturePath = finalFile.getPath();
            /*String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getActivity().getContentResolver().query(
                    selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            picturePath = c.getString(columnIndex);
            c.close();*/


            SharedPreferences pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

            SharedPreferences.Editor edt = pref.edit();

            try {
                //String picUri=\"your irl";

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = timeStamp + ".jpg";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                String ImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                File file = new File(ImagePath);
                outputFileUri = Uri.fromFile(file);

                /*UCrop.Options options = new UCrop.Options();
                options.setCompressionQuality(100);
                options.setActiveWidgetColor(Color.parseColor("#2c2954"));
                options.setToolbarColor(Color.parseColor("#2c2954"));
                options.setCropFrameColor(Color.parseColor("#2c2954"));
                options.setStatusBarColor(Color.parseColor("#2c2954"));
                options.setToolbarTitle("Photo");
                UCrop.of(selectedImage, Uri.fromFile(new File(getActivity().getCacheDir(), imageFileName)))
                        .withAspectRatio(1, 1)
                        .withOptions(options)
                        .withMaxResultSize(800, 800)
                        .start(getContext(), this, UCrop.REQUEST_CROP);*/

                Intent i = new Intent(getActivity(),CropGDSActivity.class);
                i.putExtra("PathUri",selectedImage);
                getActivity().startActivity(i);
            }

            catch (ActivityNotFoundException e) {

                String errorMessage = "No Activity found";
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                WebService WS = new WebService(getContext());
                WS.UploadImage(picturePath,User);
                //notifyDataSetChanged here or update your UI on different thread
                adapterPhoto=new ImagesProfil(MonActivity.getApplicationContext(),1,User,MonActivity);
                //adapterPhoto.notifyDataSetChanged();
                gridPhotos.setAdapter(adapterPhoto);
            }


           /* MonActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    WebService WS = new WebService(getContext());
                    WS.UploadImage(picturePath,User);
                    //notifyDataSetChanged here or update your UI on different thread
                    adapterPhoto=new ImagesProfil(MonActivity.getApplicationContext(),1,User,MonActivity);
                    //adapterPhoto.notifyDataSetChanged();
                    gridPhotos.setAdapter(adapterPhoto);
                }
            });*/



        }else if(requestCode == 1){ //TAKE A PHOTO
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap photo=null;
            try {
                photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), outputFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // outputFileUri = (Uri) data.getExtras().get(MediaStore.EXTRA_OUTPUT);

            Uri tempUri = null;
            //tempUri = data.getData();
            tempUri = getImageUri(getActivity().getApplicationContext(), photo);
            File finalFile = new File(getRealPathFromURI(tempUri));
            //mImageCaptureUri= (Uri) data.getExtras().get("URI");
            picturePath = finalFile.getPath();
            //tempUri = getImageUri(getActivity().getApplicationContext(), photo);
            /*File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                tempUri = getImageUri(getActivity().getApplicationContext(), myBitmap);
               // ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                //myImage.setImageBitmap(myBitmap);

            }*/

        
            
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        
            //Uri tempUri = getImageUri(getActivity().getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            //File finalFile = new File(getRealPathFromURI(tempUri));
            //mImageCaptureUri= (Uri) data.getExtras().get("URI");
            //picturePath = finalFile.getPath();
                    //String picturePath = mImageCaptureUri.getPath();
            /*MonActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    WebService WS = new WebService(getContext());
                    WS.UploadImage(picturePath,User);
                    //notifyDataSetChanged here or update your UI on different thread
                    adapterPhoto=new ImagesProfil(MonActivity.getApplicationContext(),1,User,MonActivity);
                    //adapterPhoto.notifyDataSetChanged();
                    gridPhotos.setAdapter(adapterPhoto);
                }
            });*/

            try {
                //String picUri=\"your irl";


                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = timeStamp + ".jpg";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                String ImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                File file = new File(ImagePath);
                outputFileUri = Uri.fromFile(file);


                /*UCrop.Options options = new UCrop.Options();
                options.setCompressionQuality(100);

                options.setActiveWidgetColor(Color.parseColor("#2c2954"));
                options.setToolbarColor(Color.parseColor("#2c2954"));
                options.setCropFrameColor(Color.parseColor("#2c2954"));
                options.setStatusBarColor(Color.parseColor("#2c2954"));
                options.setToolbarTitle("Photo");
                UCrop.of(tempUri, Uri.fromFile(new File(getActivity().getCacheDir(), imageFileName)))
                        .withAspectRatio(1, 1)
                        .withOptions(options)
                        .withMaxResultSize(800, 800)
                        .start(getContext(), this, UCrop.REQUEST_CROP);*/


                Intent i = new Intent(getActivity(),CropGDSActivity.class);
                i.putExtra("PathUri",tempUri);
                getActivity().startActivity(i);

                /*CropImage.activity(outputFileUri)
                        //.setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setMaxCropResultSize(800,800)
                        .start(getContext(), this);*/

            }

            catch (ActivityNotFoundException e) {

                String errorMessage = "No Activity found";
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                MonActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        WebService WS = new WebService(getContext());
                        WS.UploadImage(picturePath,User);
                        //notifyDataSetChanged here or update your UI on different thread
                        adapterPhoto=new ImagesProfil(MonActivity.getApplicationContext(),1,User,MonActivity);
                        //adapterPhoto.notifyDataSetChanged();
                        gridPhotos.setAdapter(adapterPhoto);
                    }
                });
            }

           /* Intent i = new Intent(getActivity().getApplicationContext(),ActivityAdjustPhoto.class);

            i.putExtra("urlPhoto",tempUri);

            getActivity().startActivity(i);*/

        }else if(requestCode == 3) { //CROP IMAGE
            try{

                Uri tempUri = null;

                if(data.getExtras()!=null){
                    Bitmap photo = data.getExtras().getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    tempUri = getImageUri(getActivity().getApplicationContext(), photo);

                    File finalFile = new File(getRealPathFromURI(tempUri));
                    //mImageCaptureUri= (Uri) data.getExtras().get("URI");
                    picturePath = finalFile.getPath();
                }else{
                    Uri selectedImage = data.getData();
                    File finalFile = new File(getRealPathFromURI(selectedImage));
                    //mImageCaptureUri= (Uri) data.getExtras().get("URI");
                    picturePath = finalFile.getPath();
                }





                /*Uri selectedImage = data.getData();
                File finalFile = new File(getRealPathFromURI(selectedImage));
                //mImageCaptureUri= (Uri) data.getExtras().get("URI");
                picturePath = finalFile.getPath();*/
                //String picturePath = mImageCaptureUri.getPath();
                /*String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getActivity().getContentResolver().query(
                        selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();*/


                SharedPreferences pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

                SharedPreferences.Editor edt = pref.edit();

                MonActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        WebService WS = new WebService(getContext());
                        WS.UploadImage(picturePath,User);
                        //notifyDataSetChanged here or update your UI on different thread
                        adapterPhoto=new ImagesProfil(MonActivity.getApplicationContext(),1,User,MonActivity);
                        //adapterPhoto.notifyDataSetChanged();
                        gridPhotos.setAdapter(adapterPhoto);
                    }
                });
            }catch (Exception e){
                Toast.makeText(getContext(),e.getCause().getMessage(),Toast.LENGTH_LONG).show();
            }

        }else if (resultCode == getActivity().RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            /*File finalFile = new File(getRealPathFromURI(resultUri));
            //mImageCaptureUri= (Uri) data.getExtras().get("URI");
            picturePath = finalFile.getPath();*/
            SharedPreferences pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

            SharedPreferences.Editor edt = pref.edit();

            MonActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    WebService WS = new WebService(getContext());
                    WS.UploadImage(resultUri.getPath(),User);
                    //notifyDataSetChanged here or update your UI on different thread
                    adapterPhoto=new ImagesProfil(MonActivity.getApplicationContext(),1,User,MonActivity);
                    //adapterPhoto.notifyDataSetChanged();
                    gridPhotos.setAdapter(adapterPhoto);
                }
            });


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }else if(resultCode==99){

            int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

            if (permission != PackageManager.PERMISSION_GRANTED) {


                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    requestPermissions(

                            new String[]{Manifest.permission.CAMERA},
                            99
                    );
                }
            }else{

                permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {


                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // We don't have permission so prompt the user
                        requestPermissions(

                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                99
                        );
                    }

                }else{
                    permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED) {


                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            // We don't have permission so prompt the user
                            requestPermissions(

                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    99
                            );
                        }

                    }else{
                                    /*Intent intent = new Intent(
                                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, 1);*/
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = timeStamp + ".jpg";
                        File storageDir = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES);
                        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                        File file = new File(pictureImagePath);
                        outputFileUri = Uri.fromFile(file);
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(cameraIntent, 1);
                    }

                }

            }

        }else{

            MonActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    adapterPhoto=new ImagesProfil(MonActivity.getApplicationContext(),1,User,MonActivity);
                    //adapterPhoto.notifyDataSetChanged();
                    gridPhotos.setAdapter(adapterPhoto);
                }
            });


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        /*int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {


            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        90
                );
            }
        }else{

            permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {


                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            99
                    );
                }

            }else{
                permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {


                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // We don't have permission so prompt the user
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                99
                        );
                    }

                }else{
                    if(requestCode==90){
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = timeStamp + ".jpg";
                        File storageDir = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES);
                        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                        File file = new File(pictureImagePath);
                        outputFileUri = Uri.fromFile(file);
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(cameraIntent, 1);
                    }

                }

            }

        }

        if(requestCode==98){
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto ,0);
        }*/
    }


    public class LoadImage extends AsyncTask<Void, Void, Boolean> {


        LoadImage() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            // TODO: register the new account here.

            adapterPhoto=new ImagesProfil(MonActivity.getApplicationContext(),1,User,MonActivity);

            adapterPhoto.notifyDataSetChanged();
            //gridPhotos.setAdapter(adapterPhoto);
            return true;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //adapterPhoto=new ImagesProfil(getApplicationContext(),1,User,this);
            gridPhotos.setAdapter(adapterPhoto);
            showProgress(false);
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }


    public class UserUploadTask extends AsyncTask<Void, Void, Boolean> {


        UserUploadTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            // TODO: register the new account here.

            WebService WS = new WebService(getContext());
            WS.UploadImage(picturePath,User);

            adapterPhoto.notifyDataSetChanged();
            //gridPhotos.setAdapter(adapterPhoto);
            return true;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //adapterPhoto=new ImagesProfil(getApplicationContext(),1,User,this);
            gridPhotos.setAdapter(adapterPhoto);
            showProgress(false);
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);

    }

    @Override
    public void onStop() {
        super.onStop();

        User.description=Edtqqmot.getText().toString();
        WebService WS = new WebService(getContext());
        WS.SaveUser(User);

        pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);




    }


    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {

            MonActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    LoadProfils();

                }
            });



        }
    }


    public void LoadProfils(){


        adapterPhoto=new ImagesProfil(MonActivity.getApplicationContext(),1,User,MonActivity);

        adapterPhoto.notifyDataSetChanged();

        gridPhotos.setAdapter(adapterPhoto);


    }

    @Override
    public void onResume() {
        super.onResume();

        //mLoadImage =new LoadImage();
        txAmis.setText(getResources().getString(R.string.amis));
        txPlutot.setText(getResources().getString(R.string.jesuisplutot));
        txCalme.setText(getResources().getString(R.string.calme));
        txFetard.setText(getResources().getString(R.string.fetard));
        txAmour.setText(getResources().getString(R.string.amour));
        txPres.setText(getResources().getString(R.string.pres));
        txPhotos.setText(getResources().getString(R.string.photo));
        Edtqqmot.setHint(getResources().getString(R.string.jesuis));
        txDeconnexion.setText(getResources().getString(R.string.deconnect));
        Valider.setText(getResources().getString(R.string.action_sign_in));
        MonActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                LoadProfils();

            }
        });
        //mLoadImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void selectImage() {
        //SyncStateContract.Constants.iscamera = true;

        if(token != null){
            final CharSequence[] items = { "Prendre une photo", "Choisir dans la bibliothèque", "Se connecter à facebook",
                    "Annuler" };

            TextView title = new TextView(getActivity().getApplicationContext());
            title.setText("Ajouter une photo");
            title.setBackgroundColor(Color.BLACK);
            title.setPadding(10, 15, 15, 10);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setTextSize(22);


            AlertDialog.Builder builder = new AlertDialog.Builder(MonActivity);



            builder.setCustomTitle(title);

            // builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Prendre une photo")) {

                        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

                        if (permission != PackageManager.PERMISSION_GRANTED) {


                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // We don't have permission so prompt the user
                                requestPermissions(
                                        new String[]{Manifest.permission.CAMERA},
                                        90
                                );
                            }
                            permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permission != PackageManager.PERMISSION_GRANTED) {


                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    // We don't have permission so prompt the user
                                    requestPermissions(
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            99
                                    );
                                }

                            }

                            permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                            if (permission != PackageManager.PERMISSION_GRANTED) {


                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    // We don't have permission so prompt the user
                                    ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            99
                                    );
                                }

                            }


                        }else{

                            permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permission != PackageManager.PERMISSION_GRANTED) {


                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    // We don't have permission so prompt the user
                                    ActivityCompat.requestPermissions(getActivity(),           new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            99
                                    );
                                }

                            }else{
                                permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                                if (permission != PackageManager.PERMISSION_GRANTED) {


                                    if (permission != PackageManager.PERMISSION_GRANTED) {
                                        // We don't have permission so prompt the user
                                        ActivityCompat.requestPermissions(getActivity(),
                                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                99
                                        );
                                    }

                                }else{
                                    /*Intent intent = new Intent(
                                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, 1);*/
                                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                    String imageFileName = timeStamp + ".jpg";
                                    File storageDir = Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES);
                                    pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                                    File file = new File(pictureImagePath);
                                    outputFileUri = Uri.fromFile(file);
                                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                                    startActivityForResult(cameraIntent, 1);
                                }

                            }

                        }





                    } else if (items[item].equals("Choisir dans la bibliothèque")) {
                        int permission =  ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permission != PackageManager.PERMISSION_GRANTED) {


                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // We don't have permission so prompt the user
                                requestPermissions(
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        98
                                );
                            }

                        }else{
                            permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                            if (permission != PackageManager.PERMISSION_GRANTED) {


                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    // We don't have permission so prompt the user
                                    requestPermissions(
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            98
                                    );
                                }

                            }else{
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto ,0);
                            }

                        }
                        /*Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 0);*/
                    }else if (items[item].equals("Se connecter à facebook")) {
                            Intent facebook = new Intent(getActivity().getApplicationContext(),
                                    GridViewFacebook.class);

                            startActivityForResult(facebook , 2);
                    } else if (items[item].equals("Annuler")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }else{
            final CharSequence[] items = { "Prendre une photo", "Choisir dans la bibliothèque",
                    "Annuler" };

            TextView title = new TextView(getActivity().getApplicationContext());
            title.setText("Ajouter une photo!");
            title.setBackgroundColor(Color.BLACK);
            title.setPadding(10, 15, 15, 10);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.WHITE);
            title.setTextSize(22);


            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MonActivity);



            builder.setCustomTitle(title);

            // builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Prendre une photo")) {
                        // Intent intent = new
                        // Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

                        if (permission != PackageManager.PERMISSION_GRANTED) {


                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // We don't have permission so prompt the user
                                requestPermissions(

                                        new String[]{Manifest.permission.CAMERA},
                                        1
                                );
                            }

                        }
                        permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permission != PackageManager.PERMISSION_GRANTED) {


                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // We don't have permission so prompt the user
                                requestPermissions(

                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        1
                                );
                            }

                        }
                        permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (permission != PackageManager.PERMISSION_GRANTED) {


                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // We don't have permission so prompt the user
                                requestPermissions(

                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        1
                                );
                            }

                        }else{
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            String imageFileName = timeStamp + ".jpg";
                            File storageDir = Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES);
                            pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                            File file = new File(pictureImagePath);
                            outputFileUri = Uri.fromFile(file);
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                            startActivityForResult(cameraIntent, 1);
                        }




                /*
                 * File photo = new
                 * File(Environment.getExternalStorageDirectory(),
                 * "Pic.jpg"); intent.putExtra(MediaStore.EXTRA_OUTPUT,
                 * Uri.fromFile(photo)); imageUri = Uri.fromFile(photo);
                 */
                        // startActivityForResult(intent,TAKE_PICTURE);

                    /*Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                    intents.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);*/

                        // start the image capture Intent

                    } else if (items[item].equals("Choisir dans la bibliothèque")) {
                        int permission =  ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permission != PackageManager.PERMISSION_GRANTED) {


                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // We don't have permission so prompt the user
                                ActivityCompat.requestPermissions(
                                        getActivity(),
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        1
                                );
                            }

                        }else{
                            permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                            if (permission != PackageManager.PERMISSION_GRANTED) {


                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    // We don't have permission so prompt the user
                                    ActivityCompat.requestPermissions(
                                            getActivity(),
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            1
                                    );
                                }

                            }else{
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto ,0);
                            }

                        }

                    } else if (items[item].equals("Annuler")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }



    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mCreateUserView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCreateUserView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCreateUserView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mCreateUserView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




    public void uploadFileToServer(String pathImg){

    }

}
