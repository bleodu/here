package com.kenfestoche.smartcoder.kenfestoche.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kenfestoche.smartcoder.kenfestoche.AddConversation;
import com.kenfestoche.smartcoder.kenfestoche.Conversation;
import com.kenfestoche.smartcoder.kenfestoche.R;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;

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
    ImageView imAjoutAmis;
    ImageView imRefuseAmis;
    ImageView imSignaler;
    ImageView imSuppKiffs;

    boolean gestionAjout;
    HashMap<String, Object> ami;

    public AdapterAmis(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to, boolean GestionAjout) {
        super(context, data, resource, from, to);
        this.context=context;
        this.arrayList=data;

        inflater.from(context);

        face=Typeface.createFromAsset(context.getAssets(),"fonts/weblysleekuil.ttf");
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
    public View getView(int position, final View convertView, final ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ImageView imPhotoKiffs=(ImageView) view.findViewById(R.id.imgPhotoKiffs);
        imAjoutAmis=(ImageView) view.findViewById(R.id.imbtajoutamis);
        imRefuseAmis=(ImageView) view.findViewById(R.id.imbtrefuseramis);
        imSuppKiffs=(ImageView) view.findViewById(R.id.imSuppKiffs);
        imSignaler=(ImageView) view.findViewById(R.id.imSignalerKiffs);
        ImageView imWaitAmis=(ImageView) view.findViewById(R.id.imsablier);
        ImageView imLocaliser=(ImageView) view.findViewById(R.id.imlocaliser);
        TextView txPseudo= (TextView) view.findViewById(R.id.txPseudoLigne);
        RelativeLayout ligneContact = (RelativeLayout) view.findViewById(R.id.ligneContact);

        txPseudo.setTypeface(face);

        ami = arrayList.get(position);
        Bitmap photo;

        imAjoutAmis.setTag(String.valueOf(position));
        imRefuseAmis.setTag(String.valueOf(position));
        imSuppKiffs.setTag(String.valueOf(position));
        imSignaler.setTag(String.valueOf(position));
        ligneContact.setTag(String.valueOf(position));
        imRefuseAmis.setVisibility(View.INVISIBLE);
        imAjoutAmis.setVisibility(View.INVISIBLE);

        photo = (Bitmap)  ami.get("photo");



        final Utilisateur User = (Utilisateur) ami.get("user");


        if(gestionAjout==false){
            //imAjoutAmis.setVisibility(View.INVISIBLE);
            if(ami.get("statut")=="0"){
                imAjoutAmis.setVisibility(View.VISIBLE);
                imAjoutAmis.setImageResource(R.drawable.btajouter);
                imRefuseAmis.setVisibility(View.VISIBLE);
                imRefuseAmis.setImageResource(R.drawable.btrefuser);
            }else if(ami.get("statut")=="1"){
                imWaitAmis.setImageResource(R.drawable.sablier);
                imWaitAmis.setVisibility(View.VISIBLE);
            }else{
                if(ami.containsKey("localiser")) {
                    if (ami.get("localiser") == "1") {
                        imLocaliser.setImageResource(R.drawable.localiser);
                        imLocaliser.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(!ami.containsKey("id_kiff")){
                        imAjoutAmis.setImageResource(R.drawable.btajout);
                        imAjoutAmis.setVisibility(View.VISIBLE);
                    }

                }
            }
        }else{
            imAjoutAmis.setImageResource(R.drawable.btajout);
            imAjoutAmis.setVisibility(View.VISIBLE);
        }

        imPhotoKiffs.setImageBitmap(photo);

        if(ami.containsKey("id_kiff")){

            WebService WS = new WebService();
            JSONArray ListMessages =  WS.GetMessageNonLu(User.id_user, (String) ami.get("id_kiff"));

            if(ListMessages!=null){
                txPseudo.setTypeface(face,Typeface.BOLD);
            }

            ligneContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=Integer.parseInt(view.getTag().toString());
                    ami = arrayList.get(pos);



                    Intent i = new Intent(view.getContext(), Conversation.class);
                    Utilisateur User = (Utilisateur) ami.get("user");
                    i.putExtra("id_kiffs",(String) ami.get("id_kiff"));
                    i.putExtra("id_user",User.id_user);
                    i.putExtra("prive",1);
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
                    imSuppKiffs.setImageResource(R.drawable.supprimerkiff);
                    imSuppKiffs.setVisibility(View.VISIBLE);

                    imSuppKiffs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos=Integer.parseInt(view.getTag().toString());
                            ami = arrayList.get(pos);
                            //imSuppKiffs=(ImageView) view.findViewById(R.id.imSuppKiffs);
                            //imSignaler=(ImageView) view.findViewById(R.id.imSignalerKiffs);
                            WebService WS = new WebService();
                            final Utilisateur User = (Utilisateur) ami.get("user");
                            String id_ami = (String) ami.get("id_kiff");
                            Toast.makeText(view.getContext(),"Kiff supprimé",Toast.LENGTH_SHORT).show();
                            WS.DeleteKiffs(User,id_ami);
                            arrayList.remove(ami);
                            imSignaler.setVisibility(View.INVISIBLE);
                            imSuppKiffs.setVisibility(View.INVISIBLE);

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
                            WebService WS = new WebService();
                            final Utilisateur User = (Utilisateur) ami.get("user");
                            String id_ami = (String) ami.get("id_kiff");

                            WS.SignalerKiffs(User,id_ami);
                            Toast.makeText(view.getContext(),"Signalement envoyé",Toast.LENGTH_SHORT).show();
                            imSignaler.setVisibility(View.INVISIBLE);
                            imSuppKiffs.setVisibility(View.INVISIBLE);

                        }
                    });


                    return true;
                }
            });
        }


        imAjoutAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=Integer.parseInt(view.getTag().toString());
                View viewLigne = parent.getChildAt(pos);
                imAjoutAmis=(ImageView) viewLigne.findViewById(R.id.imbtajoutamis);
                imRefuseAmis=(ImageView) viewLigne.findViewById(R.id.imbtrefuseramis);

                if(ami.containsKey("id_useramis")){

                    ami = arrayList.get(pos);
                    AddConversation.listusers=AddConversation.listusers+";"+ami.get("id_useramis");
                    Toast.makeText(view.getContext(),ami.get("pseudo")+" ajouté",Toast.LENGTH_SHORT).show();
                    arrayList.remove(ami);
                    notifyDataSetChanged();
                }else{
                    if(gestionAjout){
                       // int pos=Integer.parseInt(view.getTag().toString());
                        ami = arrayList.get(pos);
                        final Utilisateur User = (Utilisateur) ami.get("user");

                        String id_ami = (String) ami.get("id_ami");

                        WebService WS = new WebService();
                        WS.SetNewFriend(User,id_ami);
                        imAjoutAmis.setVisibility(View.INVISIBLE);
                        imRefuseAmis.setVisibility(View.INVISIBLE);
                        arrayList.remove(ami);
                        notifyDataSetChanged();
                        Toast.makeText(view.getContext(),"Invitation envoyée",Toast.LENGTH_SHORT).show();
                    }else{
                       // int pos=Integer.parseInt(view.getTag().toString());

                        imAjoutAmis = (ImageView) view;
                        //View ligne = getView(pos,convertView,parent);
                        //imRefuseAmis = (ImageView) ligne.findViewById(R.id.imbtrefuseramis);
                        ami = arrayList.get(pos);
                        final Utilisateur User = (Utilisateur) ami.get("user");

                        String id_ami = (String) ami.get("id_ami");

                        WebService WS = new WebService();
                        WS.AcceptRefuseFriend(User,id_ami,"2");

                        imAjoutAmis.setVisibility(View.INVISIBLE);
                        imRefuseAmis.setVisibility(View.INVISIBLE);

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
                imAjoutAmis=(ImageView) viewLigne.findViewById(R.id.imbtajoutamis);
                ami = arrayList.get(pos);

                String id_ami = (String) ami.get("id_ami");

                WebService WS = new WebService();
                WS.AcceptRefuseFriend(User,id_ami,"3");
                imRefuseAmis = (ImageView) view;
                Toast.makeText(view.getContext(),"Invitation refusée",Toast.LENGTH_SHORT).show();

                imAjoutAmis.setVisibility(View.INVISIBLE);
                imRefuseAmis.setVisibility(View.INVISIBLE);

                arrayList.remove(pos);
                notifyDataSetChanged();


            }
        });

        return view;


    }

    @Override
    public int getCount() {
        int count=arrayList.size(); //counts the total number of elements from the arrayList.
        return count;//returns the total count to adapter
    }


}
