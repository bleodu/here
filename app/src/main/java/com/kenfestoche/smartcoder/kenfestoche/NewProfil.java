package com.kenfestoche.smartcoder.kenfestoche;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    TextView txCondition;
    RadioButton radioSexeButton;
    RadioButton radioTendanceButton;
    RadioGroup radioSexeGroup;
    RadioGroup radioTendanceSexe;
    private View mProgressView;
    private View mCreateUserView;
    Double Latitude;
    Double Longitude;

    private UserCreateTask mCreateUserTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profil);



        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        Utilisateur.deleteAll(Utilisateur.class);
        Valider = (Button) findViewById(R.id.btValidCompte);
        Pseudo = (EditText) findViewById(R.id.edtPseudo);
        Pass = (EditText) findViewById(R.id.edtPass);
        Age = (EditText) findViewById(R.id.edtAge);
        Phone = (EditText) findViewById(R.id.edtPhone);
        radioSexeGroup = (RadioGroup) findViewById(R.id.radioSexeGroup);
        radioTendanceSexe = (RadioGroup) findViewById(R.id.radioTendanceGroup);
        mProgressView = (View) findViewById(R.id.createuser_progress);
        mCreateUserView = (View) findViewById(R.id.createuser_form);
        radioTendanceButton = (RadioButton) findViewById(R.id.rdBi);
        radioSexeButton = (RadioButton) findViewById(R.id.radioMale);
        txCondition = (TextView) findViewById(R.id.txCondition);

        txCondition.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        txCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),CGUActivity.class);
                startActivity(i);
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
                    Pseudo.setError("Merci de renseigner un pseudo");
                    focusView = Pseudo;
                    cancel = true;
                }

                int selectedId=radioTendanceSexe.getCheckedRadioButtonId();

                if(selectedId<0){
                    radioTendanceButton.setError("Merci de renseigner votre orientation sexuelle.");
                }

                selectedId=radioSexeGroup.getCheckedRadioButtonId();
                if(selectedId<0){
                    radioSexeButton.setError("Merci de renseigner votre sexe.");
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    //creation de l'utilisateur en base de données. Si retour ok on passe à la vérification du code par sms
                    //WebService WS = new WebService();
                    //WS.CreateUser(User);


                    Utilisateur User = new Utilisateur();
                    User.login = Pseudo.getText().toString();
                    User.password = Pass.getText().toString();
                    User.age = Integer.parseInt(Age.getText().toString());
                    User.phone = Phone.getText().toString();
                    User.latitude=Latitude;
                    User.longitude=Longitude;
                    selectedId=radioSexeGroup.getCheckedRadioButtonId();
                    radioSexeButton=(RadioButton)findViewById(selectedId);

                    switch (radioSexeButton.getText().toString())
                    {
                        case "un homme" :
                            User.sexe=0;
                            break;

                        case "une femme" :
                            User.sexe=1;
                            break;
                    }


                    selectedId=radioTendanceSexe.getCheckedRadioButtonId();
                    radioTendanceButton=(RadioButton)findViewById(selectedId);


                    switch (radioTendanceButton.getText().toString())
                    {
                        case "bi" :
                            User.tendancesexe=0;
                            break;

                        case "hétéro" :
                            User.tendancesexe=1;
                            break;

                        case "homo" :
                            User.tendancesexe=2;
                            break;
                    }

                    showProgress(true);
                    mCreateUserTask = new UserCreateTask(User);
                    mCreateUserTask.execute((Void) null);
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
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.

            WebService WS = new WebService();

            JSONArray Retour = WS.CreateUser(Uti);
            JSONObject User;
            try {
                User = Retour.getJSONObject(0);
                String RetourConnect = User.getString("statut");

                if(RetourConnect.equals("1")) //EN CREATION DE COMPTE
                {

                    Uti.save();
                    Uti.getId();
                    SharedPreferences pref = getSharedPreferences("EASER", MODE_PRIVATE);

                    SharedPreferences.Editor edt = pref.edit();

                    edt.putLong("IdUser",Uti.getId());

                    edt.commit();

                    return true;
                }else if (RetourConnect.equals("2")){ //COMPTE ACTIVE
                    return true;
                }
                else{
                    //publishProgress(User.getString("message").toString());
                    //Toast.makeText(getApplicationContext(),User.getString("message"),Toast.LENGTH_LONG).show();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Le pseudo est déjà utilisé dans notre base de données.",Toast.LENGTH_LONG).show();
                        }
                    });
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return true;
        }


        @Override
        protected void onPostExecute(Boolean success) {

            showProgress(false);

            if (success) {
                Intent i = new Intent(getApplicationContext(), VerifSmsCode.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"Erreur lors de la creation de l'utilisateur",Toast.LENGTH_LONG).show();


            }
        }

        @Override
        protected void onCancelled() {
            mCreateUserTask = null;
            showProgress(false);
        }
    }
}
