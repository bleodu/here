package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kenfestoche.smartcoder.kenfestoche.model.AdapterAmis;
import com.kenfestoche.smartcoder.kenfestoche.model.AdapterConversation;
import com.kenfestoche.smartcoder.kenfestoche.model.AdapterGridPhotos;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.squareup.picasso.Picasso;

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
    SwipeRefreshLayout swipeRefreshLayout;
    JSONArray ListConversations;
    TextView txHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_conversations);

        lstConversations = (ListView) findViewById(R.id.lstconversations);

        conversations =  new ArrayList<HashMap<String,Object>>();

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        User = FragmentsSliderActivity.User;

        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/weblysleekuil.ttf");
        Typeface faceGenerica=Typeface.createFromAsset(getAssets(),"Generica.otf");


        txNewConversation = (TextView) findViewById(R.id.txNewConversation);
        txHeader = (TextView) findViewById(R.id.txHeader);
        rlvNewConversation = (RelativeLayout) findViewById(R.id.rlvNewConversation);
        imFleche = (ImageView) findViewById(R.id.imFlecheGaucheListConversations);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        txNewConversation.setTypeface(face);
        txHeader.setTypeface(faceGenerica);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WebService WS = new WebService(getBaseContext());

                ListConversations = WS.GetListConversations(User);
                conversations.clear();

                if (ListConversations != null) {

                    String Url = null;

                    for (int i = 0; i < ListConversations.length(); i++) {
                        JSONObject LaConv = null;

                        HashMap<String, Object> valeur = new HashMap<String, Object>();
                        try {
                            LaConv = ListConversations.getJSONObject(i);
                            valeur.put("id", LaConv.getString("id"));
                            valeur.put("conversation", LaConv.getString("conversation"));
                            valeur.put("nbMess", LaConv.getInt("nbMess"));
                            JSONArray JsonArrayPhotos = WS.GetUsersConversation(LaConv.getString("id"));
                            ArrayList<String> photos = new ArrayList<String>();

                            if (JsonArrayPhotos != null) {


                                for (int j = 0; j < JsonArrayPhotos.length(); j++) {
                                    JSONObject LeUser = null;


                                    try {
                                        LeUser = JsonArrayPhotos.getJSONObject(j);
                                        Url = LeUser.getString("photo").replace(" ","%20");

                                        Bitmap bitmap=null;
                                        photos.add(Url);




                                    } catch (JSONException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }

                                }
                                valeur.put("listphotos", photos);
                            }

                        } catch (JSONException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        conversations.add(valeur);
                    }
                }

                ConversationsArray.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


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
            WebService WS = new WebService(getBaseContext());

            ListConversations = WS.GetListConversations(User);

            if (ListConversations != null) {

                String Url = null;

                for (int i = 0; i < ListConversations.length(); i++) {
                    JSONObject LaConv = null;

                    HashMap<String, Object> valeur = new HashMap<String, Object>();
                    try {
                        LaConv = ListConversations.getJSONObject(i);
                        valeur.put("id", LaConv.getString("id"));
                        valeur.put("conversation", LaConv.getString("conversation"));
                        valeur.put("nbMess", LaConv.getInt("nbMess"));
                        JSONArray JsonArrayPhotos = WS.GetUsersConversation(LaConv.getString("id"));
                        ArrayList<String> photos = new ArrayList<String>();

                        if (JsonArrayPhotos != null) {


                            for (int j = 0; j < JsonArrayPhotos.length(); j++) {
                                JSONObject LeUser = null;


                                try {
                                    LeUser = JsonArrayPhotos.getJSONObject(j);
                                    Url = LeUser.getString("photo").replace(" ","%20");

                                    Bitmap bitmap=null;
                                    photos.add(Url);




                                } catch (JSONException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }

                            }
                            valeur.put("listphotos", photos);
                        }

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
                try {
                    JSONObject LaConv  = ListConversations.getJSONObject(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onResume() {

        super.onResume();

        conversations.clear();
        if(User!=null) {
            WebService WS = new WebService(getBaseContext());

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
                        valeur.put("nbMess", LaConv.getInt("nbMess"));
                        JSONArray JsonArrayPhotos = WS.GetUsersConversation(LaConv.getString("id"));
                        ArrayList<String> photos = new ArrayList<String>();

                        if (JsonArrayPhotos != null) {


                            for (int j = 0; j < JsonArrayPhotos.length(); j++) {
                                JSONObject LeUser = null;


                                try {
                                    LeUser = JsonArrayPhotos.getJSONObject(j);
                                    Url = LeUser.getString("photo").replace(" ","%20");

                                    Bitmap bitmap=null;
                                    photos.add(Url);




                                } catch (JSONException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }

                            }
                            valeur.put("listphotos", photos);
                        }

                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    conversations.add(valeur);

                }
            }

        }

        ConversationsArray.notifyDataSetChanged();

       /* mLoadConv =new GetListConversation();

        mLoadConv.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/

    }

    private class GetListConversation extends AsyncTask<Void, Integer, Void>
    {
        Boolean NewMess=false;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            //Toast.makeText(getApplicationContext(), "Début du traitement asynchrone", Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            // Mise à jour de la ProgressBar
            ConversationsArray.notifyDataSetChanged();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            conversations.clear();
            if(User!=null) {
                WebService WS = new WebService(getBaseContext());

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

                            JSONArray JsonArrayPhotos = WS.GetUsersConversation(LaConv.getString("id"));
                            ArrayList<String> photos = new ArrayList<String>();

                            if (JsonArrayPhotos != null) {


                                for (int j = 0; j < JsonArrayPhotos.length(); j++) {
                                    JSONObject LeUser = null;


                                    try {
                                        LeUser = JsonArrayPhotos.getJSONObject(j);
                                        Url = LeUser.getString("photo").replace(" ","%20");

                                        Bitmap bitmap=null;
                                        photos.add(Url);




                                    } catch (JSONException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }

                                }
                                valeur.put("listphotos", photos);
                            }

                        } catch (JSONException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        conversations.add(valeur);

                        publishProgress(0);
                    }
                }

            }


            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
}
