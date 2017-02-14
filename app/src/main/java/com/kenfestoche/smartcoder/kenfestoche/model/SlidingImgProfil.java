package com.kenfestoche.smartcoder.kenfestoche.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenfestoche.smartcoder.kenfestoche.ProfilDetailActivity;
import com.kenfestoche.smartcoder.kenfestoche.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by smartcoder on 26/01/2017.
 */

public class SlidingImgProfil extends PagerAdapter {

    private ArrayList<HashMap<String, Object>>  IMAGES;
    private LayoutInflater inflater;
    private Context context;
    HashMap<String, Object> profil;

    ImageButton rbcalme1;
    ImageButton rbcalme2;
    ImageButton rbcalme3;
    ImageButton rbcalme4;
    ImageButton rbcalme5;

    TextView txAge;
    TextView txNom;
    //private OkHttpClient okHttpClient;


    ImageButton rbverre1;
    ImageButton rbverre2;
    ImageButton rbverre3;
    ImageButton rbverre4;
    ImageButton rbverre5;

    Boolean detailProfil;

    public SlidingImgProfil(Context context, ArrayList<HashMap<String, Object>> IMAGES,boolean detail) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
        detailProfil=detail;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout;
        if(detailProfil){
             imageLayout = inflater.inflate(R.layout.slideimgprofildetail, view, false);
        }else{
             imageLayout = inflater.inflate(R.layout.slideimgprofil, view, false);
        }

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        profil = IMAGES.get(position);

        if(detailProfil==false){
            rbcalme1 = (ImageButton) imageLayout.findViewById(R.id.rdcoeur1);
            rbcalme2 = (ImageButton) imageLayout.findViewById(R.id.rdcoeur2);
            rbcalme3 = (ImageButton) imageLayout.findViewById(R.id.rdcoeur3);
            rbcalme4 = (ImageButton) imageLayout.findViewById(R.id.rdcoeur4);
            rbcalme5 = (ImageButton) imageLayout.findViewById(R.id.rdcoeur5);

            rbverre1 = (ImageButton) imageLayout.findViewById(R.id.rdverre1);
            rbverre2 = (ImageButton) imageLayout.findViewById(R.id.rdverre2);
            rbverre3 = (ImageButton) imageLayout.findViewById(R.id.rdverre3);
            rbverre4 = (ImageButton) imageLayout.findViewById(R.id.rdverre4);
            rbverre5 = (ImageButton) imageLayout.findViewById(R.id.rdverre5);

            txNom = (TextView) imageLayout.findViewById(R.id.txNomProfil);
            txAge = (TextView) imageLayout.findViewById(R.id.txAge);

            switch (Integer.parseInt(profil.get("calme").toString()))
            {
                case 1:
                    rbcalme1.setImageResource(R.drawable.carreselect);
                    rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme2.setImageResource(R.drawable.carre);
                    rbcalme2.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme3.setImageResource(R.drawable.carre);
                    rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme4.setImageResource(R.drawable.carre);
                    rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme5.setImageResource(R.drawable.carre);
                    rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 2:
                    rbcalme1.setImageResource(R.drawable.carreselect);
                    rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme2.setImageResource(R.drawable.carreselect);
                    rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme3.setImageResource(R.drawable.carre);
                    rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme4.setImageResource(R.drawable.carre);
                    rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme5.setImageResource(R.drawable.carre);
                    rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 3:
                    rbcalme1.setImageResource(R.drawable.carreselect);
                    rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme2.setImageResource(R.drawable.carreselect);
                    rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme3.setImageResource(R.drawable.carreselect);
                    rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme4.setImageResource(R.drawable.carre);
                    rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme5.setImageResource(R.drawable.carre);
                    rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 4:
                    rbcalme1.setImageResource(R.drawable.carreselect);
                    rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme2.setImageResource(R.drawable.carreselect);
                    rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme3.setImageResource(R.drawable.carreselect);
                    rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme4.setImageResource(R.drawable.carreselect);
                    rbcalme4.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme5.setImageResource(R.drawable.carre);
                    rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 5:
                    rbcalme1.setImageResource(R.drawable.carreselect);
                    rbcalme1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme2.setImageResource(R.drawable.carreselect);
                    rbcalme2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme3.setImageResource(R.drawable.carreselect);
                    rbcalme3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme4.setImageResource(R.drawable.carreselect);
                    rbcalme4.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbcalme5.setImageResource(R.drawable.carreselect);
                    rbcalme5.setBackgroundColor(Color.parseColor("#2c2954"));
                    break;
                default:
                    rbcalme1.setImageResource(R.drawable.carre);
                    rbcalme1.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme2.setImageResource(R.drawable.carre);
                    rbcalme2.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme3.setImageResource(R.drawable.carre);
                    rbcalme3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme4.setImageResource(R.drawable.carre);
                    rbcalme4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbcalme5.setImageResource(R.drawable.carre);
                    rbcalme5.setBackgroundColor(Color.parseColor("#d2d2db"));

            }

            switch (Integer.parseInt(profil.get("affinity").toString()))
            {
                case 1:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carre);
                    rbverre2.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre3.setImageResource(R.drawable.carre);
                    rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre4.setImageResource(R.drawable.carre);
                    rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre5.setImageResource(R.drawable.carre);
                    rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 2:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carreselect);
                    rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre3.setImageResource(R.drawable.carre);
                    rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre4.setImageResource(R.drawable.carre);
                    rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre5.setImageResource(R.drawable.carre);
                    rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 3:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carreselect);
                    rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre3.setImageResource(R.drawable.carreselect);
                    rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre4.setImageResource(R.drawable.carre);
                    rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre5.setImageResource(R.drawable.carre);
                    rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 4:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carreselect);
                    rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre3.setImageResource(R.drawable.carreselect);
                    rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre4.setImageResource(R.drawable.carreselect);
                    rbverre4.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre5.setImageResource(R.drawable.carre);
                    rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));
                    break;
                case 5:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carreselect);
                    rbverre2.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre3.setImageResource(R.drawable.carreselect);
                    rbverre3.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre4.setImageResource(R.drawable.carreselect);
                    rbverre4.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre5.setImageResource(R.drawable.carreselect);
                    rbverre5.setBackgroundColor(Color.parseColor("#2c2954"));
                    break;
                default:
                    rbverre1.setImageResource(R.drawable.carreselect);
                    rbverre1.setBackgroundColor(Color.parseColor("#2c2954"));
                    rbverre2.setImageResource(R.drawable.carre);
                    rbverre2.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre3.setImageResource(R.drawable.carre);
                    rbverre3.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre4.setImageResource(R.drawable.carre);
                    rbverre4.setBackgroundColor(Color.parseColor("#d2d2db"));
                    rbverre5.setImageResource(R.drawable.carre);
                    rbverre5.setBackgroundColor(Color.parseColor("#d2d2db"));

            }


            txAge.setText(profil.get("age").toString() + " Ans");
            txNom.setText(profil.get("pseudo").toString());


        }

        imageView.setImageBitmap((Bitmap) profil.get("photo"));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailProfil==false){
                    Intent i = new Intent(view.getContext(), ProfilDetailActivity.class);
                    i.putExtra("id_user",profil.get("id_user").toString());
                    view.getContext().startActivity(i);
                }

            }
        });

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
