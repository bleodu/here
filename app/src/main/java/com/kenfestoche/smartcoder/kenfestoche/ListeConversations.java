package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kenfestoche.smartcoder.kenfestoche.model.AdapterAmis;
import com.kenfestoche.smartcoder.kenfestoche.model.AdapterConversation;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ListeConversations extends AppCompatActivity {

    ListView lstConversations;
    ArrayList<HashMap<String, Object>> conversations;
    SimpleAdapter mSchedule;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Utilisateur User;
    TextView txNewConversation;
    RelativeLayout rlvNewConversation;
    AdapterConversation ConversationsArray;
    ImageView imFleche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_conversations);

        lstConversations = (ListView) findViewById(R.id.lstconversations);

        conversations =  new ArrayList<HashMap<String,Object>>();

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));

        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");

        txNewConversation = (TextView) findViewById(R.id.txNewConversation);
        rlvNewConversation = (RelativeLayout) findViewById(R.id.rlvNewConversation);
        imFleche = (ImageView) findViewById(R.id.imFlecheGaucheListConversations);


        txNewConversation.setTypeface(face);


        rlvNewConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddConversation.class);
                startActivity(i);
            }
        });

        imFleche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(User!=null) {
            WebService WS = new WebService();

            JSONArray ListConversations = WS.GetListConversations(User);

            if (ListConversations != null) {

                String Url = null;

                for (int i = 0; i < ListConversations.length(); i++) {
                    JSONObject LaConv = null;

                    HashMap<String, Object> valeur = new HashMap<String, Object>();
                    try {
                        LaConv = ListConversations.getJSONObject(i);
                        valeur.put("id", LaConv.getString("id"));
                        valeur.put("conversation", LaConv.getString("conversation"));



                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    conversations.add(valeur);
                }
            }

        }

        ConversationsArray = new AdapterConversation(getBaseContext(), conversations, R.layout.compositionlignecontact, new String[]{"conversation", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},false);

        //mSchedule.setViewBinder(new MyViewBinder());
        lstConversations.setAdapter(ConversationsArray);

        lstConversations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        conversations.clear();
        if(User!=null) {
            WebService WS = new WebService();

            JSONArray ListConversations = WS.GetListConversations(User);

            if (ListConversations != null) {

                String Url = null;

                for (int i = 0; i < ListConversations.length(); i++) {
                    JSONObject LaConv = null;

                    HashMap<String, Object> valeur = new HashMap<String, Object>();
                    try {
                        LaConv = ListConversations.getJSONObject(i);
                        valeur.put("id", LaConv.getString("id"));
                        valeur.put("conversation", LaConv.getString("conversation"));



                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    conversations.add(valeur);
                }
            }

        }

        //ConversationsArray. = new AdapterConversation(getBaseContext(), conversations, R.layout.compositionlignecontact, new String[]{"conversation", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},false);
        ConversationsArray.notifyDataSetChanged();
    }
}
