package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
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
        CodeSms = (EditText) findViewById(R.id.edtCodeSms);

        Utilisateur User = Utilisateur.findById(Utilisateur.class, 1);

        WebService WS = new WebService();

        JSONArray Result = WS.GetSmsCode(User);

        try {
            Code = Result.getJSONObject(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }


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
