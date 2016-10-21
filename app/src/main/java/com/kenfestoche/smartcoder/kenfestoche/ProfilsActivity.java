package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.imFlecheDroite;
import static com.kenfestoche.smartcoder.kenfestoche.R.id.imFlecheGauche;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pager = (ViewPager) container;

        return inflater.inflate(R.layout.activity_profils, container, false);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_profil);
        super.onActivityCreated(savedInstanceState);
        View v = getView();

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        //sleep for 1s in background...
        gps = new GPSTracker(v.getContext());
// create class object
        pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

        editor = pref.edit();
        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));
        if(User!=null){
            User.tokenFirebase = refreshedToken;
            WebService WS = new WebService();
            WS.SaveUser(User);

        }

        new RefreshTask(v.getContext()).execute();

        BanniereContact = (ImageView) v.findViewById(R.id.imbanniereprofil);
        BanniereContact.setImageResource(R.drawable.banniereprofilkiff);
        imFlecheGauche = (ImageView) v.findViewById(R.id.imFlecheGauche);
        imFlecheDroite = (ImageView) v.findViewById(R.id.imFlecheDroite);

        imFlecheDroite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(2);
            }
        });

        imFlecheGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(0);
            }
        });


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

                            WebService WS = new WebService();
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
}

