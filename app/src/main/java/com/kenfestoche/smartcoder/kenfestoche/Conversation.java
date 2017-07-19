package com.kenfestoche.smartcoder.kenfestoche;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.text.SimpleDateFormat;
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
    TextView btEnvoyerKo;
    TextView btLocaliser;
    TextView btJeVeux;
    TextView btVeuxPas;
    JSONObject UserKiffs;
    TextView txResteMessage;
    Bitmap bitmap;
    String id_kiffs;
    int convprive;
    int amis;
    int id_user;
    int localiser;
    GridView gridPhotos;
    RelativeLayout imPopUpLocaliser;
    RelativeLayout imPopUpMessages;
    LinearLayout rootView;
    Boolean bmessageMax;
    LinearLayout lnRoot;
    LinearLayout lnparent;
    RelativeLayout rlvRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view_conversation);

        lstChat = (ListView) findViewById(R.id.lstChat);
        detailconv =  new ArrayList<HashMap<String,Object>>();
        edtSendMessage = (TextView) findViewById(R.id.txSend);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        imFlecheGauche = (ImageView) findViewById(R.id.imFlecheGaucheConversation);
        nomConversation = (TextView) findViewById(R.id.txNomConversation);
        imPhotoConversation = (ImageView) findViewById(R.id.imPhotoConversation);
        //btEnvoyerKo = (TextView) findViewById(R.id.txSend);
        btLocaliser = (TextView) findViewById(R.id.txlocaliser);
        btJeVeux = (TextView) findViewById(R.id.txJeVeux);
        btVeuxPas = (TextView) findViewById(R.id.txVeuxPas);
        txResteMessage = (TextView) findViewById(R.id.txVousReste);
        gridPhotos = (GridView) findViewById(R.id.gridPhotoConversation);
        imPopUpLocaliser = (RelativeLayout) findViewById(R.id.rlvUpLocaliser);
        imPopUpMessages = (RelativeLayout) findViewById(R.id.rlvpopupmessage);
        rootView = (LinearLayout) findViewById(R.id.activity_chat_view_conversation);
        lnRoot = (LinearLayout) findViewById(R.id.lnroot);
        lnparent = (LinearLayout) findViewById(R.id.lnParent);
        rlvRoot = (RelativeLayout) findViewById(R.id.rlvRoot);
        bmessageMax=false;

        WebService WS = new WebService(getBaseContext());
        WS.SetStatutConversation(idconv,1);
        //btVeuxPas.setImageResource(R.drawable.btveuxpasselect);
        //btJeVeux.setImageResource(R.drawable.btvoudraisselect);


        txResteMessage.setVisibility(View.INVISIBLE);
        User = FragmentsSliderActivity.User;
        URL pictureURL;
        String Url = null;


        convprive = getIntent().getIntExtra("prive",0);
        amis = getIntent().getIntExtra("amis",0);





        if(convprive==1){
            gridPhotos.setVisibility(View.GONE);
            id_user=getIntent().getIntExtra("id_user",0);
            id_kiffs=getIntent().getStringExtra("id_kiffs");
            WS = new WebService(getBaseContext());
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
                    try {
                        nomconv=UserKiffs.getString("pseudo");
                        try {
                            Url = UserKiffs.getString("photo").replace(" ", "%20");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        localiser = UserKiffs.getInt("localiser");

                        if(localiser==0 && User.sexe==0){
                            btLocaliser.setBackgroundResource(R.drawable.my_bordersendko);
                            btLocaliser.setTextColor(getResources().getColor(R.color.rosedeselect));
                        }

                        if(User.popupmessage==0 && User.sexe==1){
                            imPopUpLocaliser.setVisibility(View.VISIBLE);

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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if(User.sexe==1 && amis==0){
                btJeVeux.setVisibility(View.VISIBLE);
                btVeuxPas.setVisibility(View.VISIBLE);
                //btLocaliser.setVisibility(View.VISIBLE);
            }else{
                btJeVeux.setVisibility(View.INVISIBLE);
                btVeuxPas.setVisibility(View.INVISIBLE);
                btLocaliser.setVisibility(View.VISIBLE);
                btLocaliser.setEnabled(true);
                btLocaliser.setBackgroundResource(R.drawable.my_bordersend);
                btLocaliser.setTextColor(getResources().getColor(R.color.blanc));
            }



        }else{

            idconv = getIntent().getStringExtra("idconv");
            nomconv = getIntent().getStringExtra("nomconv");


            imPhotoConversation.setVisibility(View.INVISIBLE);
            gridPhotos.setVisibility(View.VISIBLE);
            JSONArray JsonArrayPhotos = WS.GetUsersConversation(idconv);
            ArrayList<String> lstphotos = new ArrayList<String>();

            if (JsonArrayPhotos != null) {


                for (int j = 0; j < JsonArrayPhotos.length(); j++) {
                    JSONObject LeUser = null;


                    try {
                        LeUser = JsonArrayPhotos.getJSONObject(j);
                        Url = LeUser.getString("photo").replace(" ","%20");

                        lstphotos.add(Url);




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
            //btLocaliser.setVisibility(View.INVISIBLE);


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


        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imPopUpMessages.getVisibility()==View.VISIBLE){
                    imPopUpMessages.setVisibility(View.GONE);
                }

                if(imPopUpLocaliser.getVisibility()==View.VISIBLE){
                    imPopUpLocaliser.setVisibility(View.GONE);
                    imPopUpMessages.setVisibility(View.VISIBLE);
                    User.popupmessage=1;
                    User.save();
                    WebService WS = new WebService(getBaseContext());
                    WS.SaveUser(User);
                    FragmentsSliderActivity.User=User;
                }


            }
        });

        rlvRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imPopUpMessages.getVisibility()==View.VISIBLE){
                    imPopUpMessages.setVisibility(View.GONE);
                }

                if(imPopUpLocaliser.getVisibility()==View.VISIBLE){
                    imPopUpLocaliser.setVisibility(View.GONE);
                    imPopUpMessages.setVisibility(View.VISIBLE);
                    User.popupmessage=1;
                    User.save();
                    WebService WS = new WebService(getBaseContext());
                    WS.SaveUser(User);
                    FragmentsSliderActivity.User=User;
                }


            }
        });

        lnparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imPopUpMessages.getVisibility()==View.VISIBLE){
                    imPopUpMessages.setVisibility(View.GONE);
                }

                if(imPopUpLocaliser.getVisibility()==View.VISIBLE){
                    imPopUpLocaliser.setVisibility(View.GONE);
                    imPopUpMessages.setVisibility(View.VISIBLE);
                    User.popupmessage=1;
                    User.save();
                    WebService WS = new WebService(getBaseContext());
                    WS.SaveUser(User);
                    FragmentsSliderActivity.User=User;
                }


            }
        });

        lnRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imPopUpMessages.getVisibility()==View.VISIBLE){
                    imPopUpMessages.setVisibility(View.GONE);
                }

                if(imPopUpLocaliser.getVisibility()==View.VISIBLE){
                    imPopUpLocaliser.setVisibility(View.GONE);
                    imPopUpMessages.setVisibility(View.VISIBLE);
                    User.popupmessage=1;
                    User.save();
                    WebService WS = new WebService(getBaseContext());
                    WS.SaveUser(User);
                    FragmentsSliderActivity.User=User;
                }


            }
        });


        lstChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                imPopUpMessages.setVisibility(View.GONE);
                imPopUpLocaliser.setVisibility(View.GONE);
            }
        });

        imPopUpMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imPopUpMessages.setVisibility(View.GONE);
            }
        });

        nomConversation.setText(nomconv);

        pref = getSharedPreferences("EASER", MODE_PRIVATE);

        editor = pref.edit();
        User = FragmentsSliderActivity.User;





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
                    valeur.put("url", LaConv.getString("photo").replace(" ", "%20"));
                    //Url = LaConv.getString("photo").replace(" ", "%20");





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

        /*this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                GetListConversation();
            }
        });*/


        btJeVeux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService(getBaseContext());
                WS.SetStatutConversation(idconv,2);
                edtSendMessage.setVisibility(View.VISIBLE);
                edtSendMessage.setBackgroundResource(R.drawable.my_bordersend);
                edtSendMessage.setTextColor(getResources().getColor(R.color.blanc));
                edtSendMessage.setVisibility(View.VISIBLE);
                edtSendMessage.setEnabled(true);
                //btVeuxPas.setImageResource(R.drawable.btveuxpasdeselect);
                btVeuxPas.setBackgroundResource(R.color.rosedeselect);
                btJeVeux.setBackgroundResource(R.color.bleueaser);
                //btJeVeux.setImageResource(R.drawable.btvoudrais);
                imPopUpMessages.setVisibility(View.GONE);
            }
        });

        btVeuxPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService(getBaseContext());
                WS.SetStatutConversation(idconv,1);
                //Toast.makeText(getBaseContext(),"Vous êtes bloqué, vous pourrez continuer si elle autorise de nouveau la conversation",Toast.LENGTH_SHORT).show();
                btVeuxPas.setBackgroundResource(R.color.roseselect);
                btJeVeux.setBackgroundResource(R.color.bleudeselect);
                edtSendMessage.setEnabled(false);
                edtSendMessage.setVisibility(View.VISIBLE);
                edtSendMessage.setBackgroundResource(R.drawable.my_bordersendko);
                edtSendMessage.setTextColor(getResources().getColor(R.color.roseselect));
            }
        });

        edtSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebService WS = new WebService(getBaseContext());
                WS.AddChatMessage(User,edtMessage.getText().toString(),idconv);

                HashMap<String, Object> valeur = new HashMap<String, Object>();
                valeur.put("id", 0);
                valeur.put("texte", edtMessage.getText().toString());
                valeur.put("id_user", User.id_user);
                Date myDate = new Date();
                valeur.put("date_create", new SimpleDateFormat("dd/MM/yyyy").format(myDate));

                detailconv.add(valeur);
                DetailConversationArray.notifyDataSetChanged();
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
                if(localiser==1 || User.sexe==1 || amis==1){
                    try {
                        if(UserKiffs.getInt("derniereposition")<6)
                        {
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
                        }else{

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Conversation.this);
                            builder1.setTitle("Pas de position");
                            builder1.setMessage("L'utilisateur n'est plus connecté. Position introuvable");
                            builder1.setCancelable(true);
                            builder1.setNeutralButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
            DetailConversationArray.notifyDataSetChanged();


            if(NewMess==true){

                lstChat.setSelection(DetailConversationArray.getCount() - 1);
                lstChat.smoothScrollToPosition(DetailConversationArray.getCount() - 1);
            }

            WebService WS=new WebService(getBaseContext());
            if(convprive==1 && amis==0){
                JSONArray resultList = WS.GetStatutConversation(idconv,id_user);

                String statut= null;
                try {
                    statut = resultList.getJSONObject(0).getString("statut");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(statut.equals("1")){
                    //txResteMessage.setVisibility(View.VISIBLE);
                    //btEnvoyerKo.setVisibility(View.VISIBLE);
                    //edtSendMessage.setVisibility(View.INVISIBLE);
                    btVeuxPas.setBackgroundResource(R.color.roseselect);
                    btJeVeux.setBackgroundResource(R.color.bleudeselect);

                    if(User.sexe==1){
                        txResteMessage.setVisibility(View.VISIBLE);
                        //btVeuxPas.setImageResource(R.drawable.btveuxpasdeselect);
                        //btJeVeux.setImageResource(R.drawable.btvoudraisselect);
                        //btEnvoyerKo.setVisibility(View.INVISIBLE);
                        //edtSendMessage.setVisibility(View.VISIBLE);
                        //txResteMessage.setVisibility(View.INVISIBLE);
                        try {
                            if(resultList.getJSONObject(0).getInt("nbMess")>5){
                                if(bmessageMax==false){
                                    //imPopUpMessages.setImageResource(R.drawable.popupmaxmessage);
                                    imPopUpMessages.setVisibility(View.VISIBLE);
                                    //edtSendMessage.setVisibility(View.INVISIBLE);
                                    edtSendMessage.setEnabled(false);
                                    //edtSendMessage.setEnabled(false);
                                    edtSendMessage.setBackgroundResource(R.drawable.my_bordersendko);
                                    edtSendMessage.setTextColor(getResources().getColor(R.color.roseselect));
                                    //btEnvoyerKo.setVisibility(View.VISIBLE);
                                    //btEnvoyerKo.setImageResource(R.drawable.btenvoyerko);
                                    txResteMessage.setText(getResources().getString(R.string.contact_nomesssage));
                                    bmessageMax=true;
                                }
                            }else{
                                int nbMess = 6 - resultList.getJSONObject(0).getInt("nbMess");
                                txResteMessage.setText(getResources().getString(R.string.contact_nbmessage) + " " + nbMess + " " +  getResources().getString(R.string.contact_message));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(statut.equals("0")){
                            txResteMessage.setVisibility(View.VISIBLE);


                            try {
                                if(resultList.getJSONObject(0).getInt("nbMess")>5){
                                    if(bmessageMax==false){
                                        //edtSendMessage.setVisibility(View.INVISIBLE);
                                        edtSendMessage.setEnabled(false);
                                        edtSendMessage.setVisibility(View.VISIBLE);
                                        edtSendMessage.setBackgroundResource(R.drawable.my_bordersendko);
                                        edtSendMessage.setTextColor(getResources().getColor(R.color.roseselect));
                                        txResteMessage.setText(getResources().getString(R.string.contact_nomesssage));
                                        Toast.makeText(getBaseContext(),getResources().getString(R.string.contact_popupnomesssage),Toast.LENGTH_SHORT).show();
                                        bmessageMax=true;
                                    }
                                }else{
                                    int nbMess = 6 - resultList.getJSONObject(0).getInt("nbMess");
                                    txResteMessage.setText(getResources().getString(R.string.contact_nbmessage) + " " + nbMess + " " + getResources().getString(R.string.contact_message));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    //txResteMessage.setText("Vous ne pouvez plus envoyer de messages");
                    /*try {
                        if(User.sexe==1 && resultList.getJSONObject(0).getInt("nbMess")>5){
                            if(bmessageMax==false){
                                imPopUpMessages.setImageResource(R.drawable.popupmaxmessage);
                                imPopUpMessages.setVisibility(View.VISIBLE);
                                bmessageMax=true;
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }else if(statut.equals("2")){
                    btVeuxPas.setBackgroundResource(R.color.rosedeselect);
                    btJeVeux.setBackgroundResource(R.color.bleueaser);

                    edtSendMessage.setVisibility(View.VISIBLE);
                    edtSendMessage.setBackgroundResource(R.drawable.my_bordersend);
                    edtSendMessage.setTextColor(getResources().getColor(R.color.blanc));
                    edtSendMessage.setVisibility(View.VISIBLE);
                    edtSendMessage.setEnabled(true);
                    txResteMessage.setVisibility(View.INVISIBLE);
                }else{

                    if(User.sexe==1){
                        txResteMessage.setVisibility(View.VISIBLE);
                        btVeuxPas.setBackgroundResource(R.color.rosedeselect);
                        btJeVeux.setBackgroundResource(R.color.bleudeselect);
                        //btEnvoyerKo.setVisibility(View.INVISIBLE);
                        //edtSendMessage.setVisibility(View.VISIBLE);
                        //txResteMessage.setVisibility(View.INVISIBLE);
                        try {
                            if(resultList.getJSONObject(0).getInt("nbMess")>5){
                                if(bmessageMax==false){
                                    //imPopUpMessages.setImageResource(R.drawable.popupmaxmessage);
                                    imPopUpMessages.setVisibility(View.VISIBLE);
                                    //edtSendMessage.setEnabled(false);
                                    edtSendMessage.setBackgroundResource(R.drawable.my_bordersendko);
                                    edtSendMessage.setTextColor(getResources().getColor(R.color.roseselect));
                                    edtSendMessage.setVisibility(View.VISIBLE);
                                    edtSendMessage.setEnabled(false);
                                    txResteMessage.setText(getResources().getString(R.string.contact_nomesssage));
                                    bmessageMax=true;
                                }
                            }else{
                                int nbMess = 6 - resultList.getJSONObject(0).getInt("nbMess");
                                txResteMessage.setText(getResources().getString(R.string.contact_nbmessage) + " " + nbMess + " " +  getResources().getString(R.string.contact_message));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        //if(statut.equals("0")){
                            txResteMessage.setVisibility(View.VISIBLE);


                            try {
                                if(resultList.getJSONObject(0).getInt("nbMess")>5){
                                    if(bmessageMax==false){
                                        edtSendMessage.setEnabled(false);
                                        edtSendMessage.setBackgroundResource(R.drawable.my_bordersendko);
                                        edtSendMessage.setTextColor(getResources().getColor(R.color.roseselect));
                                        edtSendMessage.setVisibility(View.VISIBLE);
                                        edtSendMessage.setEnabled(false);
                                        txResteMessage.setText(getResources().getString(R.string.contact_nomesssage));
                                        Toast.makeText(getBaseContext(),getResources().getString(R.string.contact_popupnomesssage),Toast.LENGTH_SHORT).show();
                                        bmessageMax=true;
                                    }
                                }else{
                                    int nbMess = 6 - resultList.getJSONObject(0).getInt("nbMess");
                                    txResteMessage.setText(getResources().getString(R.string.contact_nbmessage) + " " + nbMess + " " + getResources().getString(R.string.contact_message));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                       // }

                    }
                }
            }

            JSONArray Result = WS.GetinfoUser(id_kiffs);

            if(Result!=null && amis==0){
                try {
                    localiser = Result.getJSONObject(0).getInt("localiser");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(localiser==0 && User.sexe==0){
                    btLocaliser.setBackgroundResource(R.drawable.my_bordersendko);
                    btLocaliser.setTextColor(getResources().getColor(R.color.rosedeselect));

                    btLocaliser.setEnabled(false);
                }else{
                    btLocaliser.setEnabled(true);
                    btLocaliser.setBackgroundResource(R.drawable.my_bordersend);
                    btLocaliser.setTextColor(getResources().getColor(R.color.blanc));
                }
            }else{
                btLocaliser.setEnabled(true);
                btLocaliser.setBackgroundResource(R.drawable.my_bordersend);
                btLocaliser.setTextColor(getResources().getColor(R.color.blanc));
            }


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            int boucle=1;
            while(boucle==1) {
                try {

                    if(this.isCancelled())
                    {
                        boucle=999;
                    }


                    Thread.sleep(1000);
                    WebService WS = new WebService(getBaseContext());


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



                        }

                        publishProgress(0);

                    }

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
