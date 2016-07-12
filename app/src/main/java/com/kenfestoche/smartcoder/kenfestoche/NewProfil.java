package com.kenfestoche.smartcoder.kenfestoche;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    RadioButton radioSexeButton;
    RadioButton radioTendanceButton;
    RadioGroup radioSexeGroup;
    RadioGroup radioTendanceSexe;
    private View mProgressView;
    private View mCreateUserView;

    private UserCreateTask mCreateUserTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profil);

        Valider = (Button) findViewById(R.id.btValidCompte);
        Pseudo = (EditText) findViewById(R.id.edtPseudo);
        Pass = (EditText) findViewById(R.id.edtPass);
        Age = (EditText) findViewById(R.id.edtAge);
        radioSexeGroup = (RadioGroup) findViewById(R.id.radioSexeGroup);
        radioTendanceSexe = (RadioGroup) findViewById(R.id.radioTendanceGroup);
        mProgressView = (View) findViewById(R.id.createuser_progress);
        mCreateUserView = (View) findViewById(R.id.createuser_form);

        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Utilisateur User = new Utilisateur();
                User.login = Pseudo.getText().toString();
                User.password = Pass.getText().toString();
                User.age = Integer.parseInt(Age.getText().toString());

                int selectedId=radioSexeGroup.getCheckedRadioButtonId();
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


                //creation de l'utilisateur en base de données. Si retour ok on passe à la vérification du code par sms
                //WebService WS = new WebService();
                //WS.CreateUser(User);
                showProgress(true);
                mCreateUserTask = new UserCreateTask(User);
                mCreateUserTask.execute((Void) null);


            }
        });

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

            try {
                JSONObject User = Retour.getJSONObject(0);
                String RetourConnect = User.getString("statut");

                if(RetourConnect=="1") //EN CREATION DE COMPTE
                {
                    Uti.save();
                    return true;
                }else if (RetourConnect=="2"){ //COMPTE ACTIVE
                    return true;
                }
                else{
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            showProgress(false);

            if (success) {
                Intent i = new Intent(getApplicationContext(), VerifSmsCode.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(),"Erreur lors de la creation de l'utilisateur",Toast.LENGTH_LONG);

            }
        }

        @Override
        protected void onCancelled() {
            mCreateUserTask = null;
            showProgress(false);
        }
    }
}
