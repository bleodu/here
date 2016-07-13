package com.kenfestoche.smartcoder.kenfestoche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SendMotPasse extends AppCompatActivity {

    Button ValidSmsCode;
    EditText NumTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mot_passe);

        NumTel = (EditText) findViewById(R.id.edtPhoneSms);

        ValidSmsCode = (Button) findViewById(R.id.btValidSmsCode);


        ValidSmsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NumTel.setError(null);

                View focusView = null;
                boolean cancel = false;

                //check si le pseudo est renseigné
                if (TextUtils.isEmpty(NumTel.getText().toString())) {
                    NumTel.setError("Merci de renseigner votre numéro de téléphone");
                    focusView = NumTel;
                    cancel = true;
                }

                if(cancel==false){
                    WebService WS = new WebService();
                    Utilisateur User = new Utilisateur();
                    User.phone =NumTel.getText().toString();

                    JSONArray Result =WS.GetSmsCode(User,false,true);
                    try {
                        JSONObject Code = Result.getJSONObject(0);

                        if(Code.getString("statut").equals("0")){
                            Toast.makeText(getApplicationContext(),Code.getString("message"),Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Code envoyé par SMS.",Toast.LENGTH_LONG).show();
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }
}
