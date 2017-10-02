package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AjoutAmis extends AppCompatActivity {

    TextView txNewAmi;
    TextView txAPartir;
    RelativeLayout RelRep;
    ImageView imFlecheGauche;
    RelativeLayout RelPhone;
    TextView txTel;
    TextView txHeader;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_amis);

        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");
        Typeface faceGenerica=Typeface.createFromAsset(getAssets(),"Generica.otf");

        txNewAmi = (TextView) findViewById(R.id.txnewAmi);
        txAPartir = (TextView) findViewById(R.id.txAPartir);
        txHeader = (TextView) findViewById(R.id.txHeader);
        txTel = (TextView) findViewById(R.id.txTel);
        imFlecheGauche = (ImageView) findViewById(R.id.imFlecheGaucheListAmis);
        RelPhone = (RelativeLayout) findViewById(R.id.RelLayPhone);
        RelRep = (RelativeLayout) findViewById(R.id.RelLayRep);

        txNewAmi.setTypeface(face);
        txAPartir.setTypeface(face);
        txHeader.setTypeface(faceGenerica);

        imFlecheGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RelPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ListAmisAjout.class);
                //recherche par numéro de téléphone
                i.putExtra("TypeRech",1);
                startActivity(i);
            }
        });

        txTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ListAmisAjout.class);
                //recherche par numéro de téléphone
                i.putExtra("TypeRech",1);
                startActivity(i);
            }
        });

        RelRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ListAmisAjout.class);
                //recherche dans le répertoire
                i.putExtra("TypeRech",2);
                startActivity(i);
            }
        });




    }
}
