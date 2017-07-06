package com.kenfestoche.smartcoder.kenfestoche;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewProfil extends AppCompatActivity {

    Button Valider;
    EditText Pseudo;
    EditText Pass;
    EditText Age;
    EditText Phone;
    TextView txHetero;
    TextView txHomo;
    TextView txBi;
    TextView txCondition;
    ImageButton radioSexeHomme;
    ImageButton radioSexeFemme;
    ImageButton radioBi;
    ImageButton radioHetero;
    ImageButton radioHomo;
    ImageView imrdmale;
    ImageView imrdfemale;
    RadioGroup radioSexeGroup;
    RadioGroup radioTendanceSexe;
    private View mProgressView;
    private View mCreateUserView;
    Double Latitude;
    Double Longitude;
    Utilisateur User = new Utilisateur();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayout viewBi;
    LinearLayout viewHomo;
    LinearLayout viewHete;
    LinearLayout viewMale;
    LinearLayout viewFemale;

    private UserCreateTask mCreateUserTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profil);

        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");
        preferences = getSharedPreferences("EASER", MODE_PRIVATE);

        SharedPreferences.Editor edt = preferences.edit();
        // Acquire a reference to the system Location Manager
       // LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );


        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Longitude=location.getLongitude();
                Latitude=location.getLatitude();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        //locationManager.requestLocationUpdates(Context.LOCATION_SERVICE, 0, 0, locationListener);

        Utilisateur.deleteAll(Utilisateur.class);
        Valider = (Button) findViewById(R.id.btValidCompte);
        Pseudo = (EditText) findViewById(R.id.edtPseudo);
        Pass = (EditText) findViewById(R.id.edtPass);
        Age = (EditText) findViewById(R.id.edtAge);
        Phone = (EditText) findViewById(R.id.edtPhone);
        //radioSexeGroup = (RadioGroup) findViewById(R.id.radioSexeGroup);
        radioSexeHomme = (ImageButton) findViewById(R.id.radioMale);
        radioSexeFemme = (ImageButton) findViewById(R.id.radioFemale);
        imrdfemale = (ImageView) findViewById(R.id.imrdFemale);
        imrdmale = (ImageView) findViewById(R.id.imrdMale);
        mProgressView = (View) findViewById(R.id.createuser_progress);
        mCreateUserView = (View) findViewById(R.id.createuser_form);
        radioBi = (ImageButton) findViewById(R.id.imnewBi);
        radioHetero = (ImageButton) findViewById(R.id.imnewhete);
        radioHomo = (ImageButton) findViewById(R.id.imnewHomo);
        txCondition = (TextView) findViewById(R.id.txCondition);
        txHetero = (TextView) findViewById(R.id.txnewhetero);
        txHomo = (TextView) findViewById(R.id.txnewHomo);
        txBi = (TextView) findViewById(R.id.txnewBi);

        viewBi = (LinearLayout) findViewById(R.id.viewnewBi);
        viewHomo = (LinearLayout) findViewById(R.id.viewnewHomo);
        viewHete = (LinearLayout) findViewById(R.id.viewnewHete);

        viewMale = (LinearLayout) findViewById(R.id.viewMale);
        viewFemale = (LinearLayout) findViewById(R.id.viewFemale);
        Pseudo.setTypeface(face);
        Pass.setTypeface(face);
        Age.setTypeface(face);
        Phone.setTypeface(face);
        txCondition.setTypeface(face);
        txHetero.setTypeface(face);
        txHomo.setTypeface(face);
        txBi.setTypeface(face);

        User.tendancesexe=-1;
        User.sexe=-1;
        //txCondition.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txCondition.setText(Html.fromHtml(getString(R.string.conditionbouton)));

        txCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),CGUActivity.class);
                startActivity(i);
            }
        });

        radioSexeFemme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioSexeFemme.setBackgroundColor(Color.parseColor("#2c2954"));
                radioSexeHomme.setBackgroundColor(Color.parseColor("#d2d2db"));
                imrdmale.setImageResource(R.drawable.homme);
                imrdfemale.setImageResource(R.drawable.femmecoche);
                User.sexe=1;
            }
        });

        radioSexeHomme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioSexeHomme.setBackgroundColor(Color.parseColor("#2c2954"));
                radioSexeFemme.setBackgroundColor(Color.parseColor("#d2d2db"));
                imrdmale.setImageResource(R.drawable.hommecoche);
                imrdfemale.setImageResource(R.drawable.femme);
                User.sexe=0;

            }
        });

        viewMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioSexeHomme.setBackgroundColor(Color.parseColor("#2c2954"));
                radioSexeFemme.setBackgroundColor(Color.parseColor("#d2d2db"));
                imrdmale.setImageResource(R.drawable.hommecoche);
                imrdfemale.setImageResource(R.drawable.femme);
                User.sexe=0;
            }
        });

        viewFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioSexeFemme.setBackgroundColor(Color.parseColor("#2c2954"));
                radioSexeHomme.setBackgroundColor(Color.parseColor("#d2d2db"));
                imrdmale.setImageResource(R.drawable.homme);
                imrdfemale.setImageResource(R.drawable.femmecoche);
                User.sexe=1;
            }
        });

        radioBi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioBi.setBackgroundColor(Color.parseColor("#2c2954"));
                radioHetero.setBackgroundColor(Color.parseColor("#d2d2db"));
                radioHomo.setBackgroundColor(Color.parseColor("#d2d2db"));
                User.tendancesexe=0;
            }
        });

        radioHetero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioHetero.setBackgroundColor(Color.parseColor("#2c2954"));
                radioHomo.setBackgroundColor(Color.parseColor("#d2d2db"));
                radioBi.setBackgroundColor(Color.parseColor("#d2d2db"));
                User.tendancesexe=1;
            }
        });

        radioHomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioHomo.setBackgroundColor(Color.parseColor("#2c2954"));
                radioHetero.setBackgroundColor(Color.parseColor("#d2d2db"));
                radioBi.setBackgroundColor(Color.parseColor("#d2d2db"));
                User.tendancesexe=2;
            }
        });

        viewBi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioBi.setBackgroundColor(Color.parseColor("#2c2954"));
                radioHetero.setBackgroundColor(Color.parseColor("#d2d2db"));
                radioHomo.setBackgroundColor(Color.parseColor("#d2d2db"));
                User.tendancesexe=0;
            }
        });

        viewHete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioHetero.setBackgroundColor(Color.parseColor("#2c2954"));
                radioHomo.setBackgroundColor(Color.parseColor("#d2d2db"));
                radioBi.setBackgroundColor(Color.parseColor("#d2d2db"));
                User.tendancesexe=1;
            }
        });

        viewHomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioHomo.setBackgroundColor(Color.parseColor("#2c2954"));
                radioHetero.setBackgroundColor(Color.parseColor("#d2d2db"));
                radioBi.setBackgroundColor(Color.parseColor("#d2d2db"));
                User.tendancesexe=2;
            }
        });

        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                // Reset errors.
                Pseudo.setError(null);
                Pass.setError(null);
                Age.setError(null);
                Phone.setError(null);
                View focusView = null;
                boolean cancel = false;

                //check si le pseudo est renseigné
                if (TextUtils.isEmpty(Age.getText().toString())) {
                    Age.setError("Merci de renseigner votre âge");
                    focusView = Age;
                    cancel = true;
                }else if(Integer.parseInt(Age.getText().toString())<18){
                    Age.setError("Vous devez avoir plus de 18 ans.");
                    focusView = Age;
                    cancel = true;
                }

                //check si le pseudo est renseigné
                if (TextUtils.isEmpty(Pass.getText().toString())) {
                    Pass.setError("Merci de renseigner un mot de passe");
                    focusView = Pass;
                    cancel = true;
                }

                //check si le pseudo est renseigné
                if (TextUtils.isEmpty(Phone.getText().toString())) {
                    Phone.setError("Merci de renseigner votre téléphone");
                    focusView = Phone;
                    cancel = true;
                }else if(isValidPhoneNumber(Phone.getText().toString())==false)
                {
                    Phone.setError("Numéro de téléphone non valide");
                    focusView = Phone;
                    cancel = true;
                }

                //check si le pseudo est renseigné
                if (TextUtils.isEmpty(Pseudo.getText().toString())) {
                    Pseudo.setError("Merci de renseigner un prénom");
                    focusView = Pseudo;
                    cancel = true;
                }

                if(User.sexe==-1 && cancel ==false)
                {
                    Toast.makeText (getApplicationContext(),"Merci de renseigner votre sexe.",Toast.LENGTH_LONG).show();
                    cancel = true;
                }

                if(User.tendancesexe==-1 && cancel ==false)
                {
                    Toast.makeText (getApplicationContext(),"Merci de renseigner votre orientation sexuelle.",Toast.LENGTH_LONG).show();
                    cancel = true;
                }

                /*int selectedId=radioTendanceSexe.getCheckedRadioButtonId();

                if(selectedId<0){
                    Toast.makeText (getApplicationContext(),"Merci de renseigner votre orientation sexuelle.",Toast.LENGTH_LONG).show();
                }

                selectedId=radioSexeGroup.getCheckedRadioButtonId();
                if(selectedId<0){
                    Toast.makeText (getApplicationContext(),"Merci de renseigner votre sexe.",Toast.LENGTH_LONG).show();
                }*/

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    //focusView.requestFocus();
                } else {
                    //creation de l'utilisateur en base de données. Si retour ok on passe à la vérification du code par sms
                    //WebService WS = new WebService(getBaseContext());
                    //WS.CreateUser(User);

                    User.login = Pseudo.getText().toString();
                    User.password = Pass.getText().toString();
                    User.age = Integer.parseInt(Age.getText().toString());
                    User.phone = Phone.getText().toString();
                    User.latitude=Latitude;
                    User.longitude=Longitude;
                    User.connecte=true;

                    User.save();

                    WebService WS = new WebService(getBaseContext());

                    User = WS.CreateUser(User);
                    User.activnotif=1;

                    //JSONObject User;
                    // User = Retour.getJSONObject(0);
                    //String RetourConnect = User.getString("statut");

                    if(User.id_user>0) //EN CREATION DE COMPTE
                    {

                        User.save();
                        User.getId();
                        //SharedPreferences pref = getSharedPreferences("EASER", MODE_PRIVATE);

                        editor = preferences.edit();

                        editor.putLong("UserId",User.getId());

                        editor.commit();

                        Intent i = new Intent(getApplicationContext(), VerifSmsCode.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        finish();
                        startActivity(i);


                    }else{
                        Toast.makeText(getApplicationContext(),User.errormess,Toast.LENGTH_LONG).show();
                    }

                    /*showProgress(true);
                    mCreateUserTask = new UserCreateTask(User);
                    mCreateUserTask.execute((Void) null);*/
                }



            }
        });

    }

    public final static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            if (target.length() < 10 || target.length() > 10 || (target.toString().startsWith("06")==false && target.toString().startsWith("07")==false)) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(target).matches();
            }
        }
    }
    /**
     * Shows the progress UI and hides the login form.
     */
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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserCreateTask extends AsyncTask<Void, Void, Boolean> {

        private final Utilisateur Uti;

        UserCreateTask(Utilisateur User) {
            Uti = User;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            WebService WS = new WebService(getBaseContext());

            User = WS.SaveUser(Uti);

            //JSONObject User;
            // User = Retour.getJSONObject(0);
            //String RetourConnect = User.getString("statut");

            if(User.id_user>0) //EN CREATION DE COMPTE
            {

                User.save();
                User.getId();
                //SharedPreferences pref = getSharedPreferences("EASER", MODE_PRIVATE);

                editor = preferences.edit();

                editor.putLong("UserId",User.getId());

                editor.commit();

                return true;
            }else{
                //publishProgress(User.getString("message").toString());
                //Toast.makeText(getApplicationContext(),User.getString("message"),Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),User.errormess,Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }


            //return true;
        }


        @Override
        protected void onPostExecute(Boolean success) {

            showProgress(false);

            if (success) {
                Intent i = new Intent(getApplicationContext(), VerifSmsCode.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //i.putExtra("phone",Phone.getText().toString());

                //editor = preferences.edit();
                //editor.putLong("UserId", User.getId());
                //editor.commit();
                finish();
                startActivity(i);

            }
        }

        @Override
        protected void onCancelled() {
            mCreateUserTask = null;
            showProgress(false);
        }
    }
}
