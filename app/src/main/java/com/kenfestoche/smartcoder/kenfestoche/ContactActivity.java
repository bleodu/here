package com.kenfestoche.smartcoder.kenfestoche;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kenfestoche.smartcoder.kenfestoche.model.AdapterAmis;
import com.kenfestoche.smartcoder.kenfestoche.model.MyViewBinder;
import com.kenfestoche.smartcoder.kenfestoche.model.SlidingImgProfil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactActivity extends Fragment {

    Activity MonActivity;
    ListView lstKiffs;
    ListView lstAmis;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    ArrayList<HashMap<String, Object>> kiffs;
    ArrayList<HashMap<String, Object>> kiffssauv;
    ArrayList<HashMap<String, Object>> amis;
    SimpleAdapter mSchedule;
    SimpleAdapter mScheduleAmis;
    TextView txMesKiffs;
    TextView txAmis;
    TextView txPseudoLigne;
    ImageView BanniereContact;
    ImageView imPlusAmis;
    ImageView imBulle;
    Utilisateur User;
    ImageView imFlecheGauche;
    ViewPager pager;
    ImageView imFlecheDroite;


    LinearLayout panelKiffs;
    RelativeLayout panelAmis;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pager = (ViewPager) container;


        return inflater.inflate(R.layout.activity_contact, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_profil);
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        MonActivity = getActivity();

        pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

        editor = pref.edit();
        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));
        Typeface face=Typeface.createFromAsset(getActivity().getAssets(),"fonts/weblysleekuil.ttf");

        txMesKiffs = (TextView) v.findViewById(R.id.txMesKiffs);
        txAmis = (TextView) v.findViewById(R.id.txAmis);
        //txPseudoLigne = (TextView) v.findViewById(R.id.txPseudoLigne);

        BanniereContact = (ImageView) v.findViewById(R.id.imBanniereContact);
        BanniereContact.setImageResource(R.drawable.bannierecontact);
        imPlusAmis = (ImageView) v.findViewById(R.id.imPlusAmis);
        imPlusAmis.setImageResource(R.drawable.plus);

        imFlecheDroite = (ImageView) v.findViewById(R.id.imFlecheDroiteContact);
        imFlecheGauche = (ImageView) v.findViewById(R.id.imFlecheGaucheContact);

        panelAmis = (RelativeLayout) v.findViewById(R.id.panelamis);
        panelKiffs = (LinearLayout) v.findViewById(R.id.panelkiffs);

        imBulle = (ImageView) v.findViewById(R.id.imBulle);
        imBulle.setImageResource(R.drawable.bulle);




        txMesKiffs.setTypeface(face);
        txAmis.setTypeface(face);
        //txPseudoLigne.setTypeface(face);

        txMesKiffs.setText("mes kiffs");
        txAmis.setText("mes amis");

        lstKiffs = (ListView) v.findViewById(R.id.lstkiffs);
        lstAmis = (ListView) v.findViewById(R.id.lstamis);

        kiffs =  new ArrayList<HashMap<String,Object>>();
        kiffssauv =  new ArrayList<HashMap<String,Object>>();
        amis =  new ArrayList<HashMap<String,Object>>();

        imFlecheDroite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(3);
            }
        });

        imFlecheGauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1);
            }
        });

        if(User!=null){
            WebService WS = new WebService();

            JSONArray ListKiffs = WS.GetListKiffs(User);
            JSONArray ListAmis = WS.GetListAmis(User);

            if(ListKiffs != null)
            {

                String Url=null;

                for (int i = 0; i < ListKiffs.length(); i++) {
                    JSONObject LeKiff=null;

                    HashMap<String, Object> valeur = new HashMap<String, Object>();
                    try {
                        LeKiff = ListKiffs.getJSONObject(i);
                        valeur.put("pseudo", LeKiff.getString("pseudo"));
                        valeur.put("id_kiff", LeKiff.getString("id_user_kiff"));
                        valeur.put("user", User);
                        Url = LeKiff.getString("photo").replace(" ","%20");

                        URL pictureURL;

                        pictureURL = null;

                        try {
                            pictureURL = new URL(Url);
                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        Bitmap bitmap=null;
                        try {
                            bitmap = ModuleSmartcoder.getbitmap(Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));

                            if(bitmap==null){
                                bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                                //valeur.put("LOGO", bitmap);
                                File fichier = ModuleSmartcoder.savebitmap(bitmap,Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));
                            }
                            //bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        valeur.put("photo", bitmap);


                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    kiffs.add(valeur);
                    kiffssauv.add(valeur);
                }
            }

            if(ListAmis != null)
            {

                String Url=null;

                for (int i = 0; i < ListAmis.length(); i++) {
                    JSONObject Amis=null;

                    HashMap<String, Object> valeur = new HashMap<String, Object>();
                    try {
                        Amis = ListAmis.getJSONObject(i);
                        valeur.put("pseudo", Amis.getString("pseudo"));
                        valeur.put("statut", Amis.getString("statut"));
                        valeur.put("id_ami", Amis.getString("id_useramis"));
                        valeur.put("localiser", Amis.getString("localiser"));
                        valeur.put("user", User);


                        Url = Amis.getString("photo").replace(" ","%20");

                        URL pictureURL;

                        pictureURL = null;

                        try {
                            pictureURL = new URL(Url);
                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        Bitmap bitmap=null;
                        try {
                            bitmap = ModuleSmartcoder.getbitmap(Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));

                            if(bitmap==null){
                                bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                                //valeur.put("LOGO", bitmap);
                                File fichier = ModuleSmartcoder.savebitmap(bitmap,Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));
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

                    amis.add(valeur);
                }
            }




            /*mPager.addOnAdapterChangeListener(new ViewPager.OnPageChangeListener(){


                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });*/


            final AdapterAmis KiffsArray = new AdapterAmis(this.MonActivity.getBaseContext(), kiffs, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},false);

            //mSchedule.setViewBinder(new MyViewBinder());
            lstKiffs.setAdapter(KiffsArray);

            final AdapterAmis Amisrray = new AdapterAmis(this.MonActivity.getBaseContext(), amis, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},false);

            //mSchedule.setViewBinder(new MyViewBinder());
            lstAmis.setAdapter(Amisrray);

            panelAmis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(lstAmis.getVisibility()==View.VISIBLE)
                    {

                        lstAmis.setVisibility(View.INVISIBLE);
                    }else{
                        lstAmis.setVisibility(View.VISIBLE);
                    }

                    setListViewHeightBasedOnChildren(lstKiffs);
                    setListViewHeightBasedOnChildren(lstAmis);

                }
            });

            panelKiffs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(lstKiffs.getVisibility()==View.VISIBLE)
                    {
                        kiffs.clear();
                        KiffsArray.notifyDataSetChanged();

                        lstKiffs.setVisibility(View.INVISIBLE);
                    }else{
                        lstKiffs.setVisibility(View.VISIBLE);
                        for (int j = 0; j < kiffssauv.size(); j++) {
                            HashMap<String, Object> valeur = new HashMap<String, Object>();
                            valeur=kiffssauv.get(j);
                            kiffs.add(valeur);

                        }

                        KiffsArray.notifyDataSetChanged();
                    }

                    setListViewHeightBasedOnChildren(lstKiffs);
                    setListViewHeightBasedOnChildren(lstAmis);

                }
            });



            imPlusAmis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(),AjoutAmis.class);
                    startActivity(i);
                }
            });

            imBulle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(),ListeConversations.class);
                    startActivity(i);
                }
            });


            lstKiffs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Amisrray.notifyDataSetChanged();
                }
            });

            setListViewHeightBasedOnChildren(lstKiffs);
            setListViewHeightBasedOnChildren(lstAmis);


        }

        v.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event ) {
                // here your code
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    pager.setCurrentItem(1);
                    return true;
                }

                return true;
            }
        });


    }




    @Override
    public void onResume() {
        super.onResume();
        WebService WS = new WebService();

        kiffs.clear();
        amis.clear();

        JSONArray ListKiffs = WS.GetListKiffs(User);
        JSONArray ListAmis = WS.GetListAmis(User);

        if(ListKiffs != null)
        {

            String Url=null;

            for (int i = 0; i < ListKiffs.length(); i++) {
                JSONObject LeKiff=null;

                HashMap<String, Object> valeur = new HashMap<String, Object>();
                try {
                    LeKiff = ListKiffs.getJSONObject(i);
                    valeur.put("pseudo", LeKiff.getString("pseudo"));
                    valeur.put("id_kiff", LeKiff.getString("id_user_kiff"));
                    valeur.put("user", User);
                    Url = LeKiff.getString("photo").replace(" ","%20");

                    URL pictureURL;

                    pictureURL = null;

                    try {
                        pictureURL = new URL(Url);
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    Bitmap bitmap=null;
                    try {
                        bitmap = ModuleSmartcoder.getbitmap(Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));

                        if(bitmap==null){
                            bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                            //valeur.put("LOGO", bitmap);
                            File fichier = ModuleSmartcoder.savebitmap(bitmap,Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));
                        }
                        //bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    valeur.put("photo", bitmap);


                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                kiffs.add(valeur);
            }
        }

        if(ListAmis != null)
        {

            String Url=null;

            for (int i = 0; i < ListAmis.length(); i++) {
                JSONObject Amis=null;

                HashMap<String, Object> valeur = new HashMap<String, Object>();
                try {
                    Amis = ListAmis.getJSONObject(i);
                    valeur.put("pseudo", Amis.getString("pseudo"));
                    valeur.put("statut", Amis.getString("statut"));
                    valeur.put("id_ami", Amis.getString("id_useramis"));
                    valeur.put("localiser", Amis.getString("localiser"));
                    valeur.put("user", User);


                    Url = Amis.getString("photo").replace(" ","%20");

                    URL pictureURL;

                    pictureURL = null;

                    try {
                        pictureURL = new URL(Url);
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    Bitmap bitmap=null;
                    try {
                        bitmap = ModuleSmartcoder.getbitmap(Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));

                        if(bitmap==null){
                            bitmap = BitmapFactory.decodeStream(pictureURL.openStream());
                            //valeur.put("LOGO", bitmap);
                            File fichier = ModuleSmartcoder.savebitmap(bitmap,Url.replace("http://www.smartcoder-dev.fr/ZENAPP/webservice/imgprofil/",""));
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

                amis.add(valeur);
            }
        }

        AdapterAmis KiffsArray = new AdapterAmis(this.MonActivity.getBaseContext(), kiffs, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},false);

        //mSchedule.setViewBinder(new MyViewBinder());
        lstKiffs.setAdapter(KiffsArray);

        final AdapterAmis Amisrray = new AdapterAmis(this.MonActivity.getBaseContext(), amis, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},false);

        //mSchedule.setViewBinder(new MyViewBinder());
        lstAmis.setAdapter(Amisrray);

        setListViewHeightBasedOnChildren(lstKiffs);
        setListViewHeightBasedOnChildren(lstAmis);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
