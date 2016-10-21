package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class VerifSmsCode extends AppCompatActivity {


    Button Valider;
    TextView SendSms;
    EditText CodeSms;
    JSONObject Code;
    Utilisateur User;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif_sms_code);

        preferences = getSharedPreferences("EASER", MODE_PRIVATE);

        SharedPreferences.Editor edt = preferences.edit();

        Valider = (Button) findViewById(R.id.btValidSmsCode);
        SendSms = (TextView) findViewById(R.id.btSendNewSms);
        CodeSms = (EditText) findViewById(R.id.edtCodeSms);

        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");

        Valider.setTypeface(face);
        SendSms.setTypeface(face);
        CodeSms.setTypeface(face);

        TextView txtSendSms = (TextView) findViewById(R.id.txSaisieCode);
        //txtSendSms.setText(R.string.renvoiecode);
        txtSendSms.setTypeface(face);


        SendSms.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);



        Bundle extras = getIntent().getExtras();
        User = Utilisateur.findById(Utilisateur.class,preferences.getLong("UserId", 0));


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



                Utilisateur User = Utilisateur.findById(Utilisateur.class, preferences.getLong("UserId",0));

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
                        User.statut=2;
                        User.save();
                        WebService WS = new WebService();
                        User = WS.SaveUser(User);
                        editor = preferences.edit();
                        editor.putLong("UserId",User.getId());
                        editor.commit();
                        Intent i = new Intent(getApplicationContext(),UserProfil.class);
                        finish();
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
