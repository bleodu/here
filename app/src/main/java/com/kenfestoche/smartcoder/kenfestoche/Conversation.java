package com.kenfestoche.smartcoder.kenfestoche;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kenfestoche.smartcoder.kenfestoche.model.AdapterConversation;
import com.kenfestoche.smartcoder.kenfestoche.model.AdapterDetailConversation;
import com.kenfestoche.smartcoder.kenfestoche.model.AdapterGridPhotos;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Conversation extends AppCompatActivity {

    ListView lstChat;
    ArrayList<HashMap<String, Object>> detailconv;
    SimpleAdapter mSchedule;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    AdapterDetailConversation DetailConversationArray;
    Utilisateur User;
    TextView edtSendMessage;
    EditText edtMessage;
    String idconv;
    String nomconv;
    ImageView imFlecheGauche;
    TextView nomConversation;
    ImageView imPhotoConversation;
    ListConversation listconv;
    ImageView btEnvoyerKo;
    ImageView btLocaliser;
    ImageView btJeVeux;
    ImageView btVeuxPas;
    JSONObject UserKiffs;
    TextView txResteMessage;
    Bitmap bitmap;
    String id_kiffs;
    int convprive;
    int id_user;
    int localiser;
    GridView gridPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view_conversation);

        lstChat = (ListView) findViewById(R.id.lstChat);
        detailconv =  new ArrayList<HashMap<String,Object>>();
        edtSendMessage = (TextView) findViewById(R.id.edtSendMessage);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        imFlecheGauche = (ImageView) findViewById(R.id.imFlecheGaucheConversation);
        nomConversation = (TextView) findViewById(R.id.txNomConversation);
        imPhotoConversation = (ImageView) findViewById(R.id.imPhotoConversation);
        btEnvoyerKo = (ImageView) findViewById(R.id.btenvoyerko);
        btLocaliser = (ImageView) findViewById(R.id.btLocaliser);
        btJeVeux = (ImageView) findViewById(R.id.btvoudrais);
        btVeuxPas = (ImageView) findViewById(R.id.btvoudraispas);
        txResteMessage = (TextView) findViewById(R.id.txVousReste);
        gridPhotos = (GridView) findViewById(R.id.gridPhotoConversation);


        WebService WS = new WebService();
        WS.SetStatutConversation(idconv,1);
        btVeuxPas.setImageResource(R.drawable.btveuxpasselect);
        btJeVeux.setImageResource(R.drawable.btvoudraisselect);


        txResteMessage.setVisibility(View.INVISIBLE);
        User = FragmentsSliderActivity.User;
        URL pictureURL;
        String Url = null;


        convprive = getIntent().getIntExtra("prive",0);




        if(convprive==1){
            gridPhotos.setVisibility(View.GONE);
            id_user=getIntent().getIntExtra("id_user",0);
            id_kiffs=getIntent().getStringExtra("id_kiffs");
            WS = new WebService();
            JSONArray Result = WS.AddConversationPrive(String.valueOf(id_user),id_kiffs);

            try {
                JSONObject Conv=Result.getJSONObject(0);
                idconv=Conv.getString("id");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Result = WS.GetinfoUser(id_kiffs);
            if(Result!=null){
                try {
                    UserKiffs=Result.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            try {
                nomconv=UserKiffs.getString("pseudo");
                try {
                    Url = UserKiffs.getString("photo").replace(" ", "%20");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                localiser = UserKiffs.getInt("localiser");

                if(localiser==0 && User.sexe==0){
                    btLocaliser.setImageResource(R.drawable.localisernok);
                }

                pictureURL = null;

                try {
                    pictureURL = new URL(Url);
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                    imPhotoConversation.setImageBitmap(bitmap);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(User.sexe==1){
                btJeVeux.setVisibility(View.VISIBLE);
                btVeuxPas.setVisibility(View.VISIBLE);
                //btLocaliser.setVisibility(View.VISIBLE);
            }else{
                btJeVeux.setVisibility(View.INVISIBLE);
                btVeuxPas.setVisibility(View.INVISIBLE);
                //btLocaliser.setVisibility(View.INVISIBLE);
            }



        }else{

            idconv = getIntent().getStringExtra("idconv");
            nomconv = getIntent().getStringExtra("nomconv");


            imPhotoConversation.setVisibility(View.INVISIBLE);
            gridPhotos.setVisibility(View.VISIBLE);
            JSONArray JsonArrayPhotos = WS.GetUsersConversation(idconv);
            ArrayList<Bitmap> lstphotos = new ArrayList<Bitmap>();

            if (JsonArrayPhotos != null) {


                for (int j = 0; j < JsonArrayPhotos.length(); j++) {
                    JSONObject LeUser = null;


                    try {
                        LeUser = JsonArrayPhotos.getJSONObject(j);
                        Url = LeUser.getString("photo").replace(" ","%20");



                        pictureURL = null;

                        try {
                            pictureURL = new URL(Url);
                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        Bitmap bitmap=null;
                        try {
                            //bitmap = ModuleSmartcoder.getbitmap(Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));

                            if(bitmap==null){
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 2;
                                bitmap = BitmapFactory.decodeStream(pictureURL.openStream(),null,options);

                                lstphotos.add(bitmap);
                                //valeur.put("LOGO", bitmap);
                                //File fichier = ModuleSmartcoder.savebitmap(bitmap,Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));
                            }
                            //bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                }
            }


            if(lstphotos != null){
                AdapterGridPhotos listPhotos = new AdapterGridPhotos(getBaseContext(), lstphotos);
                gridPhotos.setAdapter(listPhotos);
            }


            btJeVeux.setVisibility(View.INVISIBLE);
            btVeuxPas.setVisibility(View.INVISIBLE);
            btLocaliser.setVisibility(View.INVISIBLE);


            JSONArray resultList = WS.GetProfilPhoto(User);

            try {
                Url = resultList.getJSONObject(0).getString("photo").replace(" ", "%20");
            } catch (JSONException e) {
                e.printStackTrace();
            }



            pictureURL = null;

            try {
                pictureURL = new URL(Url);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                imPhotoConversation.setImageBitmap(bitmap);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }



        nomConversation.setText(nomconv);

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));





        JSONArray resultList = WS.GetDetailConversation(idconv,User);

        if (resultList != null) {


            for (int i = 0; i < resultList.length(); i++) {
                JSONObject LaConv = null;

                HashMap<String, Object> valeur = new HashMap<String, Object>();
                try {
                    LaConv = resultList.getJSONObject(i);
                    valeur.put("id", LaConv.getString("id"));
                    valeur.put("texte", LaConv.getString("texte"));
                    valeur.put("id_user", LaConv.getString("id_user"));
                    valeur.put("date_create", LaConv.getString("date_create"));
                    Url = LaConv.getString("photo").replace(" ", "%20");


                    pictureURL = null;

                    try {
                        pictureURL = new URL(Url);
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    try {
                        //bitmap = ModuleSmartcoder.getbitmap(Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));

                        if(bitmap==null){
                            bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                            //valeur.put("LOGO", bitmap);
                            //File fichier = ModuleSmartcoder.savebitmap(bitmap,Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    valeur.put("photo", bitmap);



                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                detailconv.add(valeur);
            }
        }

        DetailConversationArray = new AdapterDetailConversation(getBaseContext(), detailconv, R.layout.lignechatview, new String[]{"texte", "photo"}, new int[]{R.id.txTexteChat, R.id.imPhotoChat},User);

        //mSchedule.setViewBinder(new MyViewBinder());
        lstChat.setAdapter(DetailConversationArray);

        lstChat.setSelection(DetailConversationArray.getCount() - 1);
        lstChat.smoothScrollToPosition(DetailConversationArray.getCount() - 1);


        listconv=new ListConversation();
        listconv.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        btJeVeux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService();
                WS.SetStatutConversation(idconv,0);
                btVeuxPas.setImageResource(R.drawable.btveuxpasdeselect);
                btJeVeux.setImageResource(R.drawable.btvoudrais);
            }
        });

        btVeuxPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService();
                WS.SetStatutConversation(idconv,1);
                btVeuxPas.setImageResource(R.drawable.btveuxpasselect);
                btJeVeux.setImageResource(R.drawable.btvoudraisselect);
            }
        });

        edtSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService();
                WS.AddChatMessage(User,edtMessage.getText().toString(),idconv);
                edtMessage.setText("");
                //InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                lstChat.setSelection(DetailConversationArray.getCount() - 1);
                lstChat.smoothScrollToPosition(DetailConversationArray.getCount() - 1);

            }
        });

        edtMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //got focus
                    lstChat.setSelection(DetailConversationArray.getCount() - 1);
                    lstChat.smoothScrollToPosition(DetailConversationArray.getCount() - 1);
                } else {
                    //lost focus
                    lstChat.setSelection(DetailConversationArray.getCount() - 1);
                    lstChat.smoothScrollToPosition(DetailConversationArray.getCount() - 1);
                }
            }
        });


        btLocaliser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(localiser==1 || User.sexe==1){
                    Intent i = new Intent(getApplicationContext(),FragmentsSliderActivity.class);
                    i.putExtra("Localiser",true);
                    try {
                        i.putExtra("Latitude", UserKiffs.getString("latitude"));
                        i.putExtra("Longitude", UserKiffs.getString("longitude"));
                        i.putExtra("Pseudo", UserKiffs.getString("pseudo"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(i);
                }


            }
        });



        imFlecheGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        listconv.cancel(true);
    }

    private class ListConversation extends AsyncTask<Void, Integer, Void>
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
            //mProgressBar.setProgress(values[0]);
            //lstChat.setAdapter(DetailConversationArray);
            //mSchedule.setViewBinder(new MyViewBinder());
            WebService WS=new WebService();
            if(convprive==1){
                JSONArray resultList = WS.GetStatutConversation(idconv,id_user);

                String statut= null;
                try {
                    statut = resultList.getJSONObject(0).getString("statut");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(statut.equals("1")){
                    txResteMessage.setVisibility(View.VISIBLE);
                    try {
                        if(resultList.getJSONObject(0).getInt("nbMess")>9){
                            btEnvoyerKo.setVisibility(View.VISIBLE);
                            edtSendMessage.setVisibility(View.INVISIBLE);
                            txResteMessage.setText("Vous ne pouvez plus envoyer de messages");
                        }else{
                            int ResteMessage = 10 - resultList.getJSONObject(0).getInt("nbMess");
                            txResteMessage.setText("Vous pouvez envoyer " + ResteMessage + " messages");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    btEnvoyerKo.setVisibility(View.INVISIBLE);
                    edtSendMessage.setVisibility(View.VISIBLE);
                    txResteMessage.setVisibility(View.INVISIBLE);
                }
            }

            DetailConversationArray.notifyDataSetChanged();
            if(NewMess==true){

                lstChat.setSelection(DetailConversationArray.getCount() - 1);
                lstChat.smoothScrollToPosition(DetailConversationArray.getCount() - 1);
            }

            JSONArray Result = WS.GetinfoUser(id_kiffs);

            if(Result!=null){
                try {
                    localiser = Result.getJSONObject(0).getInt("localiser");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(localiser==0){
                    btLocaliser.setImageResource(R.drawable.localisernok);
                }else{
                    btLocaliser.setImageResource(R.drawable.btlocaliser);
                }
            }


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            int boucle=1;
            while(boucle==1) {
                try {

                    Thread.sleep(1000);
                    WebService WS = new WebService();


                    NewMess=false;
                    JSONArray resultList = WS.GetNewDetailConversation(idconv,User);

                    if (resultList != null) {
                        NewMess=true;
                        ArrayList<HashMap<String, Object>> detailconvtemp = null;
                        detailconvtemp =  new ArrayList<HashMap<String,Object>>();
                        String Url = null;

                        for (int i = 0; i < resultList.length(); i++) {
                            JSONObject LaConv = null;

                            HashMap<String, Object> valeur = new HashMap<String, Object>();
                            try {
                                LaConv = resultList.getJSONObject(i);
                                valeur.put("id", LaConv.getString("id"));
                                valeur.put("texte", LaConv.getString("texte"));
                                valeur.put("id_user", LaConv.getString("id_user"));
                                valeur.put("date_create", LaConv.getString("date_create"));
                                Url = LaConv.getString("photo").replace(" ", "%20");

                                URL pictureURL;

                                pictureURL = null;

                                try {
                                    pictureURL = new URL(Url);
                                } catch (MalformedURLException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                                Bitmap bitmap = null;
                                try {
                                    bitmap = BitmapFactory.decodeStream(pictureURL.openStream());

                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                valeur.put("photo", bitmap);


                            } catch (JSONException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }

                            detailconv.add(valeur);
                            publishProgress(0);


                        }
                        //detailconv.clear();
                        //detailconv.addAll(detailconvtemp);

                        //DetailConversationArray = new AdapterDetailConversation(getBaseContext(), detailconvtemp, R.layout.lignechatview, new String[]{"texte", "photo"}, new int[]{R.id.txTexteChat, R.id.imPhotoChat},User);

                        //Thread.sleep(1000);
                        //DetailConversationArray = new AdapterDetailConversation(getBaseContext(), detailconv, R.layout.lignechatview, new String[]{"texte", "photo"}, new int[]{R.id.txTexteChat, R.id.imPhotoChat},User);
                        //DetailConversationArray.notifyDataSetChanged();
                        //mSchedule.setViewBinder(new MyViewBinder());
                        //lstChat.setAdapter(DetailConversationArray);
                    }
                    //DetailConversationArray = new AdapterDetailConversation(getBaseContext(), detailconv, R.layout.lignechatview, new String[]{"texte", "photo"}, new int[]{R.id.txTexteChat, R.id.imPhotoChat},User);
                    //lstChat.setAdapter(DetailConversationArray);
                    //DetailConversationArray.notifyDataSetChanged();
                    //publishProgress(0);
                    //return null;
                    publishProgress(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            DetailConversationArray.notifyDataSetChanged();
            //Toast.makeText(getApplicationContext(), "Le traitement asynchrone est terminé", Toast.LENGTH_LONG).show();
        }


    }
}
