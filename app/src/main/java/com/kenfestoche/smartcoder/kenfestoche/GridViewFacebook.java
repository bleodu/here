package com.kenfestoche.smartcoder.kenfestoche;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.kenfestoche.smartcoder.kenfestoche.model.ImagesFacebook;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;

public class GridViewFacebook extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_facebook);

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        Utilisateur User = FragmentsSliderActivity.User;

        GridView gridview = (GridView) findViewById(R.id.gdfacebook);
        gridview.setAdapter(new ImagesFacebook(this,1,User,getContentResolver()));



    }
}
