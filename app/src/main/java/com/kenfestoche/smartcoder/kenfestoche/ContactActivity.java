package com.kenfestoche.smartcoder.kenfestoche;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.arasthel.asyncjob.AsyncJob;
import com.kenfestoche.smartcoder.kenfestoche.model.AdapterAmis;
import com.kenfestoche.smartcoder.kenfestoche.model.ImagesProfil;
import com.kenfestoche.smartcoder.kenfestoche.model.MyGridPhoto;
import com.kenfestoche.smartcoder.kenfestoche.model.MyViewBinder;
import com.kenfestoche.smartcoder.kenfestoche.model.SlidingImgProfil;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.squareup.picasso.Picasso;

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
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactActivity extends Fragment {

    Activity MonActivity;
    ListView lstKiffs;
    ListView lstAmis;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    ArrayList<HashMap<String, Object>> kiffs;
    ArrayList<HashMap<String, Object>> kiffssauv;
    ArrayList<HashMap<String, Object>> amis;
    TextView txMesKiffs;
    TextView txAmis;
    TextView txNbMessage;
    ImageView imPlusAmis;
    ImageView imBulle;
    ImageView imflecheReduction;
    Utilisateur User;
    ImageView imFlecheGauche;
    ViewPager pager;
    ImageView imFlecheDroite;
    AdapterAmis KiffsArray;
    ProgressBar pgLoad;
    String kiffsStringId="";
    String amisStringId="";
    private SwipeRefreshLayout swipeRefreshLayout;
    AdapterAmis Amisrray;
    TextView txHeader;
    AsyncTaskRunner mLoadContact;
    LinearLayout panelKiffs;
    RelativeLayout panelAmis;
    Typeface face;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MonActivity = getActivity();

        User = FragmentsSliderActivity.User;




        /*MonActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                LoadContacts();

            }
        });*/


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pager = (ViewPager) container;
        kiffs =  new ArrayList<HashMap<String,Object>>();
        kiffssauv =  new ArrayList<HashMap<String,Object>>();
        amis =  new ArrayList<HashMap<String,Object>>();

        KiffsArray = new AdapterAmis(getActivity().getBaseContext(), kiffs, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},false,true,false,pager);

        Amisrray = new AdapterAmis(getActivity().getBaseContext(), amis, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},false,true,false,pager);


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

        face=Typeface.createFromAsset(getActivity().getAssets(),"fonts/weblysleekuil.ttf");
        Typeface faceGenerica=Typeface.createFromAsset(getActivity().getAssets(),"Generica.otf");

        txMesKiffs = (TextView) v.findViewById(R.id.txMesKiffs);
        txAmis = (TextView) v.findViewById(R.id.txAmis);
        //txPseudoLigne = (TextView) v.findViewById(R.id.txPseudoLigne);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        //BanniereContact = (ImageView) v.findViewById(R.id.imBanniereContact);
        //BanniereContact.setImageResource(R.drawable.bannierecontact);
        imPlusAmis = (ImageView) v.findViewById(R.id.imPlusAmis);
        imPlusAmis.setImageResource(R.drawable.plus);
        pgLoad = (ProgressBar) v.findViewById(R.id.pgLoad);

        imFlecheDroite = (ImageView) v.findViewById(R.id.imFlecheDroiteContact);
        imFlecheGauche = (ImageView) v.findViewById(R.id.imFlecheGaucheContact);
        imflecheReduction = (ImageView) v.findViewById(R.id.imFlecheHaut);

        panelAmis = (RelativeLayout) v.findViewById(R.id.panelamis);
        panelKiffs = (LinearLayout) v.findViewById(R.id.panelkiffs);

        txHeader = (TextView) v.findViewById(R.id.txHeader);

        imBulle = (ImageView) v.findViewById(R.id.imBulle);
        imBulle.setImageResource(R.drawable.bulle);

        lstKiffs = (ListView)  v.findViewById(R.id.lstkiffs);
        lstAmis = (ListView)  v.findViewById(R.id.lstamis);


        lstKiffs.setAdapter(KiffsArray);
        lstAmis.setAdapter(Amisrray);


        txNbMessage = (TextView) v.findViewById(R.id.txNbMessages);



        txNbMessage.setTypeface(face);
        txNbMessage.setVisibility(View.INVISIBLE);



        txMesKiffs.setTypeface(face);
        txAmis.setTypeface(face);
        txHeader.setTypeface(faceGenerica);

        txMesKiffs.setText(getResources().getString(R.string.kiffs));
        txAmis.setText(getResources().getString(R.string.mesamis));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    LoadContacts();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });




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

            /*panelAmis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(lstAmis.getVisibility()==View.VISIBLE)
                    {
                        lstAmis.setVisibility(View.GONE);
                    }else{
                        lstAmis.setVisibility(View.VISIBLE);
                    }

                    setListViewHeightBasedOnChildren(lstKiffs);
                    setListViewHeightBasedOnChildren(lstAmis);

                }
            });*/

            panelKiffs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(lstKiffs.getVisibility()==View.VISIBLE)
                    {
                        imflecheReduction.setImageResource(R.drawable.flechebas);
                        //kiffs.clear();
                        lstKiffs.setVisibility(View.GONE);
                    }else{
                        imflecheReduction.setImageResource(R.drawable.flechehaut);
                        //kiffs= (ArrayList<HashMap<String, Object>>) kiffssauv.clone();
                        //KiffsArray = new AdapterAmis(getActivity().getBaseContext(), kiffs, R.layout.compositionlignecontact, new String[]{"pseudo", "photo"}, new int[]{R.id.txPseudoLigne, R.id.imgPhotoKiffs},false);
                        //lstKiffs.setAdapter(KiffsArray);
                        lstKiffs.setVisibility(View.VISIBLE);
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

        pgLoad.setVisibility(View.GONE);


    }

    void LoadContacts() throws InterruptedException

    {
        //swipeRefreshLayout.setRefreshing(true);
        WebService WS = new WebService(getContext());

        kiffs.clear();
        amis.clear();

        JSONArray ListKiffs = WS.GetListKiffs(User,"");
        JSONArray ListAmis = WS.GetListAmis(User,"");

        if(ListKiffs != null && ListKiffs.length()>0)
        {

            String Url=null;

            for (int i = 0; i < ListKiffs.length(); i++) {
                JSONObject LeKiff=null;

                HashMap<String, Object> valeur = new HashMap<String, Object>();
                try {
                    LeKiff = ListKiffs.getJSONObject(i);
                    valeur.put("pseudo", LeKiff.getString("pseudo"));
                    valeur.put("id_kiff", LeKiff.getString("id_user_kiff"));
                    valeur.put("vu", LeKiff.getInt("vu"));
                    valeur.put("nbMess", LeKiff.getInt("nbMess"));
                    valeur.put("user", User);
                    Url = LeKiff.getString("photo").replace(" ","%20");
                    valeur.put("url",Url);

                    kiffsStringId = kiffsStringId + LeKiff.getString("id_user_kiff") + ",";


                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                kiffs.add(valeur);
                kiffssauv.add(valeur);


            }
        }
        //Thread.sleep(5000);
        if(ListAmis != null && ListAmis.length()>0)
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
                    valeur.put("latitude", Amis.getString("latitude"));
                    valeur.put("longitude", Amis.getString("longitude"));
                    valeur.put("nbMess", Amis.getInt("nbMess"));
                    valeur.put("user", User);

                    amisStringId = amisStringId + Amis.getString("id_useramis") + ",";
                    Url = Amis.getString("photo").replace(" ","%20");
                    valeur.put("url",Url);




                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                amis.add(valeur);



            }
        }

        KiffsArray.notifyDataSetChanged();
        Amisrray.notifyDataSetChanged();


        setListViewHeightBasedOnChildren(lstKiffs);
        setListViewHeightBasedOnChildren(lstAmis);


        //pgLoad.setVisibility(View.GONE);

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {

            try {
                LoadContacts();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            KiffsArray.notifyDataSetChanged();
            Amisrray.notifyDataSetChanged();


            setListViewHeightBasedOnChildren(lstKiffs);
            setListViewHeightBasedOnChildren(lstAmis);
            pgLoad.setVisibility(View.GONE);

        }


        @Override
        protected void onPreExecute() {
            pgLoad.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            pgLoad.setVisibility(View.VISIBLE);
            // Create a job to run on background
            AsyncJob.OnBackgroundJob job = new AsyncJob.OnBackgroundJob() {
                @Override
                public void doOnBackground() {
                    // Pretend to do some background processing
                    try {
                        LoadContacts();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // This toast should show a difference of 1000ms between calls
                    AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                        @Override
                        public void doInUIThread() {
                            pgLoad.setVisibility(View.GONE);
                        }
                    });
                }
            };

            job.doOnBackground();



           /* MonActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            });*/

           /* mLoadContact =new AsyncTaskRunner();
            mLoadContact.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/



            //mLoadContact =new GetListContacts();
            //mLoadContact.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            if(mLoadContact!=null){
                Log.v("LOGGDS", "Chargement contact annulé");
                mLoadContact.cancel(true);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();




    }

    @Override
    public void onPause() {
        super.onPause();
        /*if(mLoadContact != null){
            mLoadContact.cancel(true);
        }*/

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
