package com.kenfestoche.smartcoder.kenfestoche.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kenfestoche.smartcoder.kenfestoche.AddConversation;
import com.kenfestoche.smartcoder.kenfestoche.Conversation;
import com.kenfestoche.smartcoder.kenfestoche.FragmentsSliderActivity;
import com.kenfestoche.smartcoder.kenfestoche.ProfilsActivity;
import com.kenfestoche.smartcoder.kenfestoche.R;
import com.kenfestoche.smartcoder.kenfestoche.Tag;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by smartcoder on 12/10/2016.
 */

public class AdapterAmis extends SimpleAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String, Object>> arrayList;
    Typeface face;
    Typeface facebold;
    TextView imAjoutAmis;
    TextView imRefuseAmis;
    ImageView imSignaler;
    ImageView imSuppKiffs;
    ViewPager pager;

    boolean gestionAjout;
    HashMap<String, Object> ami;

    public AdapterAmis(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to, boolean GestionAjout, boolean amis, boolean datatransmis, ViewPager pag) {
        super(context, data, resource, from, to);

        this.arrayList=data;
        this.context=context;
        inflater.from(context);
        this.pager=pag;
        /*if(datatransmis){
            this.arrayList=data;
        }else{
            WebService WS = new WebService(context);

            arrayList =  new ArrayList<HashMap<String,Object>>();
            //this.arrayList.clear();

            JSONArray ListKiffs = WS.GetListKiffs(FragmentsSliderActivity.User,"");
            JSONArray ListAmis = WS.GetListAmis(FragmentsSliderActivity.User,"");

            if(amis){
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
                            valeur.put("nbMess", Amis.getInt("nbMess"));
                            valeur.put("user", FragmentsSliderActivity.User);

                            //amisStringId = amisStringId + Amis.getString("id_useramis") + ",";
                            Url = Amis.getString("photo").replace(" ","%20");
                            valeur.put("url",Url);




                        } catch (JSONException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        arrayList.add(valeur);
                        //Amisrray.notifyDataSetChanged();


                    }
                }
            }else{
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
                            valeur.put("user", FragmentsSliderActivity.User);
                            Url = LeKiff.getString("photo").replace(" ","%20");
                            valeur.put("url",Url);

                            //kiffsStringId = kiffsStringId + LeKiff.getString("id_user_kiff") + ",";


                        } catch (JSONException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        arrayList.add(valeur);
                    }
                }
            }
        }*/


        face=Typeface.createFromAsset(context.getAssets(),"fonts/weblysleekuil.ttf");
        facebold=Typeface.createFromAsset(context.getAssets(),"weblysleekuisb.ttf");
        gestionAjout=GestionAjout;

    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //View view=null;
            View view = super.getView(position,convertView,parent);

            inflater =LayoutInflater.from(context);
            ami = arrayList.get(position);
            Log.e("LOGGDS", String.valueOf(position));

            //if(view==null){
                final ImageView imPhotoKiffs=(ImageView) view.findViewById(R.id.imgPhotoKiffs);
                imAjoutAmis= (TextView) view.findViewById(R.id.txAccepter);
                imRefuseAmis= (TextView) view.findViewById(R.id.txRefuser);
                imSuppKiffs=(ImageView) view.findViewById(R.id.imSuppKiffs);
                imSignaler=(ImageView) view.findViewById(R.id.imSignalerKiffs);
                ImageView imWaitAmis=(ImageView) view.findViewById(R.id.imsablier);
                ImageView imLocaliser=(ImageView) view.findViewById(R.id.imlocaliser);
                final TextView txPseudo= (TextView) view.findViewById(R.id.txPseudoLigne);
                RelativeLayout ligneContact = (RelativeLayout) view.findViewById(R.id.ligneContact);

                txPseudo.setTypeface(face);




                imAjoutAmis.setTag(String.valueOf(position));
                imRefuseAmis.setTag(String.valueOf(position));
                imSuppKiffs.setTag(String.valueOf(position));
                imSignaler.setTag(String.valueOf(position));
                ligneContact.setTag(String.valueOf(position));
                imLocaliser.setTag(String.valueOf(position));
                imRefuseAmis.setVisibility(View.GONE);
                imAjoutAmis.setVisibility(View.GONE);

                //photo = (Bitmap)  ami.get("photo");

                //new ImageDownloaderTask(imPhotoKiffs).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(String) ami.get("url"));
                String urlPhoto = (String) ami.get("url");
                Picasso.with(context).load(urlPhoto).resize(200,200).into(imPhotoKiffs);
                //new MyAsyncTask(position,imPhotoKiffs).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



                final Utilisateur User = (Utilisateur) ami.get("user");




                //imPhotoKiffs.setImageBitmap(photo);

                if(ami.containsKey("id_kiff")){

                    /*
                    new AsyncTask<Void, Void, Void>() {

                        JSONArray ListMessages=null;
                        @Override
                        protected Void doInBackground( Void... voids ) {

                            WebService WS = new WebService(context,false);
                            ListMessages =  WS.GetMessageNonLu(User.id_user, (String) ami.get("id_kiff"),"1");

                            return null;
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();


                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if(ListMessages!=null){
                                txPseudo.setTypeface(facebold);
                            }else{
                                txPseudo.setTypeface(face);
                            }



                        }
                    }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);*/

                    if(ami.containsKey("nbMess")){
                        if((int)(ami.get("nbMess"))>0){
                            txPseudo.setTypeface(facebold);
                        }
                    }


                    if((int) ami.get("vu")==0){
                        txPseudo.setTypeface(facebold);
                    }

                    ligneContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos=Integer.parseInt(view.getTag().toString());
                            ami = arrayList.get(pos);
                            txPseudo.setTypeface(face);
                            WebService WS = new WebService(context,false);
                            WS.SetKiffOpen(User,(String) ami.get("id_kiff"));

                            ami.put("vu",1);
                            ami.put("nbMess",0);

                            Intent i = new Intent(view.getContext(), Conversation.class);
                            Utilisateur User = (Utilisateur) ami.get("user");
                            i.putExtra("id_kiffs",(String) ami.get("id_kiff"));
                            i.putExtra("id_user",User.id_user);
                            i.putExtra("prive",1);
                            i.putExtra("amis",0);
                            view.getContext().startActivity(i);


                        }
                    });

                    ligneContact.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {


                            imSuppKiffs=(ImageView) view.findViewById(R.id.imSuppKiffs);
                            imSignaler=(ImageView) view.findViewById(R.id.imSignalerKiffs);
                            imSignaler.setImageResource(R.drawable.signaler);
                            imSignaler.setVisibility(View.VISIBLE);
                            //imSuppKiffs.setImageResource(R.drawable.supprimerkiff);
                            imSuppKiffs.setVisibility(View.VISIBLE);

                            imSuppKiffs.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int pos=Integer.parseInt(view.getTag().toString());
                                    ami = arrayList.get(pos);
                                    //imSuppKiffs=(ImageView) view.findViewById(R.id.imSuppKiffs);
                                    //imSignaler=(ImageView) view.findViewById(R.id.imSignalerKiffs);
                                    WebService WS = new WebService(context,false);
                                    final Utilisateur User = (Utilisateur) ami.get("user");
                                    String id_ami = (String) ami.get("id_kiff");
                                    Toast.makeText(view.getContext(),"Kiff supprimé",Toast.LENGTH_SHORT).show();
                                    WS.DeleteKiffs(User,id_ami);
                                    arrayList.remove(ami);
                                    imSignaler.setVisibility(View.GONE);
                                    imSuppKiffs.setVisibility(View.GONE);

                                    notifyDataSetChanged();
                                }
                            });

                            imSignaler.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int pos=Integer.parseInt(view.getTag().toString());
                                    ami = arrayList.get(pos);
                                    //imSuppKiffs=(ImageView) view.findViewById(R.id.imSuppKiffs);
                                    //imSignaler=(ImageView) view.findViewById(R.id.imSignalerKiffs);
                                    WebService WS = new WebService(context,false);
                                    final Utilisateur User = (Utilisateur) ami.get("user");
                                    String id_ami = (String) ami.get("id_kiff");

                                    WS.SignalerKiffs(User,id_ami);
                                    Toast.makeText(view.getContext(),"Signalement envoyé",Toast.LENGTH_SHORT).show();
                                    imSignaler.setVisibility(View.GONE);
                                    imSuppKiffs.setVisibility(View.GONE);

                                }
                            });


                            return true;
                        }
                    });
                }else{
                    if(ami.containsKey("id_ami")){

                        if(gestionAjout==false){
                            //imAjoutAmis.setVisibility(View.INVISIBLE);
                            if(ami.get("statut").equals("0")){
                                imAjoutAmis.setVisibility(View.VISIBLE);
                                imLocaliser.setVisibility(View.INVISIBLE);
                                //imAjoutAmis.setImageResource(R.drawable.btajouter);
                                imRefuseAmis.setVisibility(View.VISIBLE);
                                //imRefuseAmis.setImageResource(R.drawable.btrefuser);
                            }else if(ami.get("statut").equals("1")){
                                imWaitAmis.setImageResource(R.drawable.sablier);
                                imWaitAmis.setVisibility(View.VISIBLE);
                                imLocaliser.setVisibility(View.INVISIBLE);
                            }else{
                                if(ami.containsKey("localiser")) {
                                    if (ami.get("localiser").equals("0")) {
                                        imLocaliser.setImageResource(R.drawable.localiser);
                                        imLocaliser.setVisibility(View.VISIBLE);
                                        imLocaliser.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int pos=Integer.parseInt(view.getTag().toString());
                                                ami = arrayList.get(pos);
                                                FragmentsSliderActivity.longitude= (String) ami.get("longitude");
                                                FragmentsSliderActivity.latitude= (String) ami.get("latitude");
                                                FragmentsSliderActivity.Localiser=true;
                                                pager.setCurrentItem(0);
                                            }
                                        });

                                    }
                                }else{
                                    if(!ami.containsKey("id_kiff")){
                                        imAjoutAmis.setText(view.getResources().getString(R.string.ajout));
                                        imAjoutAmis.setVisibility(View.VISIBLE);
                                    }

                                }
                            }
                        }else{
                            //imAjoutAmis.setImageResource(R.drawable.btajout);
                            //imAjoutAmis.setText(view.getResources().getString(R.string.ajout));
                            imAjoutAmis.setVisibility(View.VISIBLE);
                            imAjoutAmis.setText(view.getResources().getString(R.string.ajout));
                        }

                        if(ami.containsKey("nbMess")){
                            if((int)(ami.get("nbMess"))>0){
                                txPseudo.setTypeface(facebold);
                            }
                        }

                        /*new AsyncTask<Void, Void, Void>() {

                            JSONArray ListMessages=null;
                            @Override
                            protected Void doInBackground( Void... voids ) {

                                WebService WS = new WebService(context,false);
                                ListMessages =  WS.GetMessageNonLu(User.id_user, (String) ami.get("id_ami"),"1");

                                return null;
                            }

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();


                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                if(ListMessages!=null){
                                    txPseudo.setTypeface(facebold);
                                }else{
                                    txPseudo.setTypeface(face);
                                }

                            }
                        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);*/



                        ligneContact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int pos=Integer.parseInt(view.getTag().toString());
                                ami = arrayList.get(pos);
                                txPseudo.setTypeface(face);

                                //Si le statut est accepté entre deux amis
                                if(ami.get("statut").equals("2")){
                                    Intent i = new Intent(view.getContext(), Conversation.class);
                                    Utilisateur User = (Utilisateur) ami.get("user");
                                    i.putExtra("id_kiffs",(String) ami.get("id_ami"));
                                    i.putExtra("id_user",User.id_user);
                                    i.putExtra("prive",1);
                                    i.putExtra("amis",1);
                                    view.getContext().startActivity(i);
                                }


                            }
                        });
                    }else if(ami.containsKey("id_useramis")){
                        imAjoutAmis.setVisibility(View.VISIBLE);
                        imAjoutAmis.setText(view.getResources().getString(R.string.ajout));


                    }

                }


                imAjoutAmis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos=Integer.parseInt(view.getTag().toString());
                        View viewLigne = parent.getChildAt(pos);
                        imAjoutAmis= (TextView) viewLigne.findViewById(R.id.txAccepter);
                        imRefuseAmis= (TextView) viewLigne.findViewById(R.id.txRefuser);

                        if(ami.containsKey("id_useramis")){

                            ami = arrayList.get(pos);
                            AddConversation.listusers=AddConversation.listusers+";"+ami.get("id_useramis");
                            AddConversation.chipList.add(new Tag((String) ami.get("pseudo")));
                            AddConversation.chipDefault.setChipList(AddConversation.chipList);
                            AddConversation.edtAmis.setText("");
                            Toast.makeText(view.getContext(),ami.get("pseudo")+" ajouté",Toast.LENGTH_SHORT).show();
                            arrayList.remove(ami);
                            notifyDataSetChanged();
                        }else{
                            if(gestionAjout){
                                // int pos=Integer.parseInt(view.getTag().toString());
                                ami = arrayList.get(pos);
                                final Utilisateur User = (Utilisateur) ami.get("user");

                                String id_ami = (String) ami.get("id_ami");

                                WebService WS = new WebService(context,false);
                                WS.SetNewFriend(User,id_ami);
                                imAjoutAmis.setVisibility(View.GONE);
                                imRefuseAmis.setVisibility(View.GONE);
                                arrayList.remove(ami);
                                notifyDataSetChanged();
                                Toast.makeText(view.getContext(),"Invitation envoyée",Toast.LENGTH_SHORT).show();
                            }else{
                                // int pos=Integer.parseInt(view.getTag().toString());

                                imAjoutAmis = (TextView) view;
                                //View ligne = getView(pos,convertView,parent);
                                //imRefuseAmis = (ImageView) ligne.findViewById(R.id.imbtrefuseramis);
                                ami = arrayList.get(pos);
                                final Utilisateur User = (Utilisateur) ami.get("user");
                                ami.put("statut","2");
                                String id_ami = (String) ami.get("id_ami");

                                WebService WS = new WebService(context,false);
                                WS.AcceptRefuseFriend(User,id_ami,"2");

                                imAjoutAmis.setVisibility(View.GONE);
                                imRefuseAmis.setVisibility(View.GONE);

                                Toast.makeText(view.getContext(),"Invitation accepté",Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });

                imRefuseAmis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final Utilisateur User = (Utilisateur) ami.get("user");
                        int pos=Integer.parseInt(view.getTag().toString());
                        View viewLigne = parent.getChildAt(pos);
                        imAjoutAmis= (TextView) viewLigne.findViewById(R.id.txAccepter);
                        ami = arrayList.get(pos);

                        String id_ami = (String) ami.get("id_ami");

                        WebService WS = new WebService(context,false);
                        WS.AcceptRefuseFriend(User,id_ami,"3");
                        ami.put("statut","3");
                        imRefuseAmis = (TextView) view;
                        Toast.makeText(view.getContext(),"Invitation refusée",Toast.LENGTH_SHORT).show();

                        imAjoutAmis.setVisibility(View.GONE);
                        imRefuseAmis.setVisibility(View.GONE);

                        arrayList.remove(pos);
                        notifyDataSetChanged();


                    }
                });
           /* }else{
                final TextView txPseudo= (TextView) view.findViewById(R.id.txPseudoLigne);
                new AsyncTask<Void, Void, Void>() {

                    JSONArray ListMessages=null;
                    @Override
                    protected Void doInBackground( Void... voids ) {

                        WebService WS = new WebService(context,false);
                        ListMessages =  WS.GetMessageNonLu(FragmentsSliderActivity.User.id_user, (String) ami.get("id_kiff"),"1");

                        return null;
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();


                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if(ListMessages!=null){
                            txPseudo.setTypeface(facebold);
                        }



                    }
                }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }*/


        return view;


    }

    @Override
    public int getCount() {
        int count=arrayList.size(); //counts the total number of elements from the arrayList.
        return count;//returns the total count to adapter
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


}
