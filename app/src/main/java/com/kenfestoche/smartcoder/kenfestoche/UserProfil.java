package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class UserProfil extends AppCompatActivity {

    RadioButton rbcalme1;
    RadioButton rbcalme2;
    RadioButton rbcalme3;
    RadioButton rbcalme4;
    RadioButton rbcalme5;

    RadioButton rbverre1;
    RadioButton rbverre2;
    RadioButton rbverre3;
    RadioButton rbverre4;
    RadioButton rbverre5;

    Button Parametre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profil);

        rbcalme1 = (RadioButton) findViewById(R.id.rdcalme1);
        rbcalme2 = (RadioButton) findViewById(R.id.rdcalme2);
        rbcalme3 = (RadioButton) findViewById(R.id.rdcalme3);
        rbcalme4 = (RadioButton) findViewById(R.id.rdcalme4);
        rbcalme5 = (RadioButton) findViewById(R.id.rdcalme5);

        rbverre1 = (RadioButton) findViewById(R.id.rdverre1);
        rbverre2 = (RadioButton) findViewById(R.id.rdverre2);
        rbverre3 = (RadioButton) findViewById(R.id.rdverre3);
        rbverre4 = (RadioButton) findViewById(R.id.rdverre4);
        rbverre5 = (RadioButton) findViewById(R.id.rdverre4);

        Parametre = (Button) findViewById(R.id.btParametre);

        Parametre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SettingsEASER.class);
                startActivity(i);

            }
        });

        rbcalme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbcalme1.setChecked(true);
                rbcalme2.setChecked(false);
                rbcalme3.setChecked(false);
                rbcalme4.setChecked(false);
                rbcalme5.setChecked(false);
            }
        });

        rbcalme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbcalme1.setChecked(true);
                rbcalme2.setChecked(true);
                rbcalme3.setChecked(false);
                rbcalme4.setChecked(false);
                rbcalme5.setChecked(false);
            }
        });

        rbcalme3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbcalme1.setChecked(true);
                rbcalme2.setChecked(true);
                rbcalme3.setChecked(true);
                rbcalme4.setChecked(false);
                rbcalme5.setChecked(false);
            }
        });

        rbcalme4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbcalme1.setChecked(true);
                rbcalme2.setChecked(true);
                rbcalme3.setChecked(true);
                rbcalme4.setChecked(true);
                rbcalme5.setChecked(false);
            }
        });

        rbcalme5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbcalme1.setChecked(true);
                rbcalme2.setChecked(true);
                rbcalme3.setChecked(true);
                rbcalme4.setChecked(true);
                rbcalme5.setChecked(true);
            }
        });

        rbverre1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbverre1.setChecked(true);
                rbverre2.setChecked(false);
                rbverre3.setChecked(false);
                rbverre4.setChecked(false);
                rbverre5.setChecked(false);
            }
        });

        rbverre2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbverre1.setChecked(true);
                rbverre2.setChecked(true);
                rbverre3.setChecked(false);
                rbverre4.setChecked(false);
                rbverre5.setChecked(false);
            }
        });

        rbverre3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbverre1.setChecked(true);
                rbverre2.setChecked(true);
                rbverre3.setChecked(true);
                rbverre4.setChecked(false);
                rbverre5.setChecked(false);
            }
        });

        rbverre4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbverre1.setChecked(true);
                rbverre2.setChecked(true);
                rbverre3.setChecked(true);
                rbverre4.setChecked(true);
                rbverre5.setChecked(false);
            }
        });

        rbverre5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbverre1.setChecked(true);
                rbverre2.setChecked(true);
                rbverre3.setChecked(true);
                rbverre4.setChecked(true);
                rbverre5.setChecked(true);
            }
        });
    }
}
