package com.kenfestoche.smartcoder.kenfestoche;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

public class ParamPseudo extends Activity {


    Button btValid;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Utilisateur User;
    EditText edtPseudo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param_pseudo);

        btValid = (Button) findViewById(R.id.btValidPseudo);
        edtPseudo = (EditText) findViewById(R.id.edtNewPseudo);

        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");

        edtPseudo.setTypeface(face);
        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        User = FragmentsSliderActivity.User;

        edtPseudo.setText(User.login);

        btValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService();
                User.login=edtPseudo.getText().toString();
                WS.SaveUser(User);
                User.save();
                finish();

            }
        });
    }
}
