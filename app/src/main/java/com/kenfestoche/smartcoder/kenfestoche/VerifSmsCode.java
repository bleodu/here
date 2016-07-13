package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VerifSmsCode extends AppCompatActivity {


    Button Valider;
    Button SendSms;
    EditText CodeSms;
    JSONObject Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif_sms_code);

        Valider = (Button) findViewById(R.id.btValidSmsCode);
        SendSms = (Button) findViewById(R.id.btSendNewSms);
        CodeSms = (EditText) findViewById(R.id.edtCodeSms);

        SharedPreferences pref = getSharedPreferences("EASER", MODE_PRIVATE);

        SharedPreferences.Editor edt = pref.edit();


        Utilisateur User = Utilisateur.findById(Utilisateur.class, pref.getLong("IdUser",0));

        final WebService WS = new WebService();

        JSONArray Result = WS.GetSmsCode(User,false,false);

        try {
            Code = Result.getJSONObject(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = getSharedPreferences("EASER", MODE_PRIVATE);

                SharedPreferences.Editor edt = pref.edit();


                Utilisateur User = Utilisateur.findById(Utilisateur.class, pref.getLong("IdUser",0));

                JSONArray Result = WS.GetSmsCode(User,true,false);

                try {
                    Code = Result.getJSONObject(0);

                    if(Code.getString("statut").equals("0")){
                        Toast.makeText(getApplicationContext(),Code.getString("message"),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Code envoyé par SMS.",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(Code.getString("codesms").toString().equals(CodeSms.getText().toString())){
                        Intent i = new Intent(getApplicationContext(),UserProfil.class);
                        startActivity(i);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),"Code SMS non valide",Toast.LENGTH_LONG);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
