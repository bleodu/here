package com.kenfestoche.smartcoder.kenfestoche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class CGUActivity extends AppCompatActivity {

    TextView txCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgu);
        txCondition= (TextView) findViewById(R.id.txTextCondition);

        txCondition.setText(Html.fromHtml(getString(R.string.condition)));

    }
}
