package com.kenfestoche.smartcoder.kenfestoche;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MapActivity extends Fragment implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    MapView mMapView;
    private GoogleMap googleMap;
    Circle cercleMoi;
    GPSTracker gps;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Utilisateur User;
    LinearLayout imLegAmis;
    LinearLayout imLegInconnu;
    LinearLayout imLegKiffs;
    ImageView imFlecheMap;
    ImageView imGeoloc;
    ImageView imPhotoMap;
    ViewPager pager;
    Boolean clicAmis;
    Boolean clicKiff;
    Boolean clicInconnu;
    Boolean cliclsoiree;
    ImageButton btFetes;
    ImageButton btDebit;
    ImageButton btconcert;
    ImageButton btbar;
    ImageButton btbarnuit;
    ImageButton btboitenuit;
    ImageButton btautres;
    Marker markerPosition;
    boolean RefreshSoiree;
    ProgressBar pgLoad;

    ArrayList<Marker> lstMarkerInconnus;

    ArrayList<MarkerOptions> lstMarkerInconnusOptions;
    ArrayList<Marker> lstMarkerInconnusRemove;
    ArrayList<Marker> lstMarkerSoirees;
    ArrayList<JSONObject> lstMarkerSoireesJS;
    ArrayList<MarkerOptions> lstMarkerSoireeOptions;
    ArrayList<Marker> lstMarkerMatchs;
    ArrayList<MarkerOptions> lstMarkerMatchOptions;
    ArrayList<JSONObject> lstMarkerMatchJS;
    ArrayList<Marker> lstMarkerMatchRemove;
    ArrayList<Marker> lstMarkerAmis;
    ArrayList<Marker> lstMarkerAmisRemove;
    ArrayList<JSONObject> lstMarkerAmisJS;
    ArrayList<MarkerOptions> lstMarkerAmisOptions;
    LocationManager locationManager;

    TextView txFetes;
    TextView txDate;
    TextView txHoraire;
    TextView txConcert;
    TextView txBar;
    TextView txBoiteNuit;
    TextView txBarNuit;
    TextView txAutres;
    TextView txDebit;
    TextView txDateDebit;
    TextView txHoraireDebit;

    TextView txNomSoiree;
    TextView txDescriptif;
    TextView txHoraireSoiree;
    TextView txDateFermeture;
    TextView txAdresse;
    TextView txLienSite;
    TextView txPopUpMessage;
    TextView txTypeSoiree;
    TextView txHeader;
    Marker marker;


    private TimePicker timePicker1;

    private Calendar myCalendar;

    RelativeLayout impopupmap;

    Button Valider;
    Button ValiderDetail;

    LinearLayout rlvFetesSoiree;

    LinearLayout lnDetailSoiree;
    Boolean imSoireeLongClic;

    RelativeLayout rlvMap;

    LinearLayout imSoiree;

    TextView txLegAmis;
    TextView txLegMatch;
    TextView txLegInconnu;
    TextView txLegSoiree;


    RefreshMap refreshMap;

    MarkerOptions MaPosition;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        clicAmis = FragmentsSliderActivity.ClicAmis;
        clicInconnu = FragmentsSliderActivity.ClicInconnu;
        clicKiff = FragmentsSliderActivity.ClicMatch;
        cliclsoiree = FragmentsSliderActivity.ClicSoiree;
        RefreshSoiree=false;
        pager = (ViewPager) container;

        User = FragmentsSliderActivity.User;
        View rootView = inflater.inflate(R.layout.activity_map, container, false);

        imLegAmis = (LinearLayout) rootView.findViewById(R.id.imlegamis);
        imLegKiffs = (LinearLayout) rootView.findViewById(R.id.imlegmatch);
        imLegInconnu = (LinearLayout) rootView.findViewById(R.id.imleginconnu);
        imFlecheMap = (ImageView) rootView.findViewById(R.id.imFlecheDroiteMap);
        imGeoloc = (ImageView) rootView.findViewById(R.id.imgeoloc);
        imPhotoMap = (ImageView) rootView.findViewById(R.id.imPhotoMap);
        imSoiree = (LinearLayout) rootView.findViewById(R.id.imSoiree);

        pgLoad = (ProgressBar) rootView.findViewById(R.id.pgChargement);

        txLegAmis = (TextView) rootView.findViewById(R.id.txAmis);
        txLegMatch = (TextView) rootView.findViewById(R.id.txMatch);
        txLegInconnu = (TextView) rootView.findViewById(R.id.txInconnus);
        txLegSoiree = (TextView) rootView.findViewById(R.id.txSoiree);

        imSoireeLongClic = false;
        if (clicAmis) {
            txLegAmis.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (clicInconnu) {
            txLegInconnu.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (clicKiff) {
            txLegMatch.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (User.bfetes == true || User.bdebit == true) {
            cliclsoiree = true;
        } else if (User.bfetes == false && User.bdebit == false) {
            cliclsoiree = false;
        }

        if (cliclsoiree == false) {
            txLegSoiree.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            if(txLegSoiree.getPaintFlags() == Paint.STRIKE_THRU_TEXT_FLAG){
                txLegSoiree.setPaintFlags(txLegSoiree.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
            }

        }

        lstMarkerSoirees = new ArrayList<Marker>();
        lstMarkerSoireesJS = new ArrayList<JSONObject>();
        lstMarkerSoireeOptions = new ArrayList<MarkerOptions>();

        lstMarkerMatchs = new ArrayList<Marker>();
        lstMarkerMatchRemove = new ArrayList<Marker>();
        lstMarkerMatchJS = new ArrayList<JSONObject>();
        lstMarkerMatchOptions = new ArrayList<MarkerOptions>();

        lstMarkerAmis = new ArrayList<Marker>();
        lstMarkerAmisRemove = new ArrayList<Marker>();
        lstMarkerAmisJS = new ArrayList<JSONObject>();
        lstMarkerAmisOptions = new ArrayList<MarkerOptions>();

        lstMarkerInconnus = new ArrayList<Marker>();
        lstMarkerInconnusRemove = new ArrayList<Marker>();
        lstMarkerInconnusOptions = new ArrayList<MarkerOptions>();

        Typeface facegras = Typeface.createFromAsset(getActivity().getAssets(), "weblysleekuisb.ttf");


        txLegAmis.setTypeface(facegras);
        txLegMatch.setTypeface(facegras);
        txLegInconnu.setTypeface(facegras);
        txLegSoiree.setTypeface(facegras);

        btFetes = (ImageButton) rootView.findViewById(R.id.rdFetes);
        btDebit = (ImageButton) rootView.findViewById(R.id.rdAlcool);
        btconcert = (ImageButton) rootView.findViewById(R.id.rdConcert);
        btbar = (ImageButton) rootView.findViewById(R.id.rdBar);
        btboitenuit = (ImageButton) rootView.findViewById(R.id.rdBoite);
        btbarnuit = (ImageButton) rootView.findViewById(R.id.rdBarNuit);
        btautres = (ImageButton) rootView.findViewById(R.id.rdAutre);
        Valider = (Button) rootView.findViewById(R.id.btValidSoiree);
        ValiderDetail = (Button) rootView.findViewById(R.id.btValidDetail);
        impopupmap = (RelativeLayout) rootView.findViewById(R.id.rlvpopuppmap);
        txPopUpMessage = (TextView) rootView.findViewById(R.id.txPopupMessages);
        lnDetailSoiree = (LinearLayout) rootView.findViewById(R.id.lndetailsoiree);

        Typeface faceGenerica = Typeface.createFromAsset(getActivity().getAssets(), "Generica.otf");

        txHeader = (TextView) rootView.findViewById(R.id.txHeader);
        txHeader.setTypeface(faceGenerica);

        lnDetailSoiree.setVisibility(View.GONE);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weblysleekuil.ttf");


        txFetes = (TextView) rootView.findViewById(R.id.txfetes);
        txDate = (TextView) rootView.findViewById(R.id.txdate);
        txHoraire = (TextView) rootView.findViewById(R.id.txhoraire);
        txConcert = (TextView) rootView.findViewById(R.id.txConcert);
        txBar = (TextView) rootView.findViewById(R.id.txBar);
        txBarNuit = (TextView) rootView.findViewById(R.id.txBarNuit);
        txAutres = (TextView) rootView.findViewById(R.id.txAutre);
        txBoiteNuit = (TextView) rootView.findViewById(R.id.txBoite);


        txNomSoiree = (TextView) rootView.findViewById(R.id.txNomSoiree);
        txDescriptif = (TextView) rootView.findViewById(R.id.txDescriptif);
        txHoraireSoiree = (TextView) rootView.findViewById(R.id.txHoraire);
        txAdresse = (TextView) rootView.findViewById(R.id.txAdresse);
        txDateFermeture = (TextView) rootView.findViewById(R.id.txFermetureExceptionnel);
        txLienSite = (TextView) rootView.findViewById(R.id.txLienSite);
        txTypeSoiree = (TextView) rootView.findViewById(R.id.txTypeSoiree);

        txDebit = (TextView) rootView.findViewById(R.id.txdebitalcool);
        txDateDebit = (TextView) rootView.findViewById(R.id.txdatedebit);
        txHoraireDebit = (TextView) rootView.findViewById(R.id.txhorairedebit);

        txFetes.setTypeface(face);
        txPopUpMessage.setTypeface(face);
        txDate.setTypeface(face);
        txHoraire.setTypeface(face);
        txConcert.setTypeface(face);
        txBar.setTypeface(face);
        txBarNuit.setTypeface(face);
        txAutres.setTypeface(face);
        txBoiteNuit.setTypeface(face);

        txNomSoiree.setTypeface(face);
        txDescriptif.setTypeface(face);
        txHoraireSoiree.setTypeface(face);
        txAdresse.setTypeface(face);
        txDateFermeture.setTypeface(face);
        txLienSite.setTypeface(face);
        txTypeSoiree.setTypeface(face);

        txDebit.setTypeface(face);
        txDateDebit.setTypeface(face);
        txHoraireDebit.setTypeface(face);

        txDateDebit.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txHoraireDebit.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txDate.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txHoraire.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        Valider.setTypeface(face);


        rlvFetesSoiree = (LinearLayout) rootView.findViewById(R.id.rlvSoiree);
        rlvMap = (RelativeLayout) rootView.findViewById(R.id.rlvMap);
        //imLegAmis.setImageResource(R.drawable.amisgras);
        //imLegKiffs.setImageResource(R.drawable.matchgras);
        //imLegInconnu.setImageResource(R.drawable.inconnugras);
        imGeoloc.setImageResource(R.drawable.flechemap);

        myCalendar = Calendar.getInstance();

        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH) + 1;
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);


        imPhotoMap.setVisibility(View.INVISIBLE);



        txDateDebit.setText(getResources().getString(R.string.date) + " : " + day + "/" + month + "/" + year);
        txDate.setText(getResources().getString(R.string.date) + " : " + day + "/" + month + "/" + year);
        User.datedebit = year + "-" + month + "-" + day;
        User.date = year + "-" + month + "-" + day;
        txHoraire.setText(getResources().getString(R.string.horaire) + " : " + hour + ":" + minute);
        txHoraireDebit.setText(getResources().getString(R.string.horaire) + " : " + hour + ":" + minute);
        User.horaire = hour + ":" + minute;
        User.horairedebit = hour + ":" + minute;
        User.save();


        if (User.bfetes) {
            btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if (User.bconcert) {
            btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if (User.bbarnuit) {
            btbarnuit.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if (User.bbar) {
            btbar.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if (User.bboitenuit) {
            btboitenuit.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if (User.bautre) {
            btautres.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if (User.bdebit) {
            btDebit.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        txHoraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendar = Calendar.getInstance();

                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txHoraire.setText(getResources().getString(R.string.horaire) + " : " + hourOfDay + ":" + minute);
                                User.horaire = hourOfDay + ":" + minute;
                                User.save();
                            }
                        }, hour, minute, true);

                timePickerDialog.show();

            }

        });

        txHoraireDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendar = Calendar.getInstance();

                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txHoraireDebit.setText(getResources().getString(R.string.horaire) + " : " + hourOfDay + ":" + minute);
                                User.horairedebit = hourOfDay + ":" + minute;
                                User.save();
                            }
                        }, hour, minute, true);

                timePickerDialog.show();

            }

        });

        txDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myCalendar = Calendar.getInstance();

                int year = myCalendar.get(Calendar.YEAR);
                int month = myCalendar.get(Calendar.MONTH);
                int day = myCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        txDate.setText(getResources().getString(R.string.date) + " : " + i2 + "/" + i1 + "/" + i);
                        User.date = i + "-" + i1 + "-" + i2;
                        User.save();
                    }
                }, year, month, day);
                datePicker.show();


            }
        });

        txDateDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myCalendar = Calendar.getInstance();

                int year = myCalendar.get(Calendar.YEAR);
                int month = myCalendar.get(Calendar.MONTH);
                int day = myCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;

                        txDateDebit.setText(getResources().getString(R.string.date) + " : " + i2 + "/" + i1 + "/" + i);
                        User.datedebit = i + "-" + i1 + "-" + i2;
                        User.save();
                    }
                }, year, month, day);
                datePicker.show();


            }
        });


        ValiderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnDetailSoiree.setVisibility(View.GONE);
            }
        });

        imSoiree.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                impopupmap.setVisibility(View.INVISIBLE);
                rlvFetesSoiree.setVisibility(View.VISIBLE);
                User.popupmap = 1;
                User.save();
                WebService WS = new WebService(getContext());
                WS.SaveUser(User);
                cliclsoiree = true;
                imSoireeLongClic = true;
                return false;
            }
        });

        imSoiree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cliclsoiree){
                    cliclsoiree=false;
                }else{
                    cliclsoiree=true;
                }

                if (cliclsoiree == false) {
                    if(lstMarkerSoirees!=null){
                        for (int i = 0; i < lstMarkerSoirees.size(); i++) {
                            Marker marker = lstMarkerSoirees.get(i);
                            marker.remove();
                        }
                        lstMarkerSoirees.clear();
                        //lstMarkerSoireeOptions.clear();
                    }

                    txLegSoiree.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                }else{
                    if(txLegSoiree.getPaintFlags() == Paint.STRIKE_THRU_TEXT_FLAG){
                        txLegSoiree.setPaintFlags(txLegSoiree.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    if(lstMarkerSoireeOptions != null){
                        for (int j = 0; j < lstMarkerSoireeOptions.size(); j++) {
                            MarkerOptions mkOption = lstMarkerSoireeOptions.get(j);

                            Marker marker = googleMap.addMarker(mkOption);
                            marker.setTag(3);
                            lstMarkerSoirees.add(marker);


                        }

                    }

                }

              // RefreshPositions();
            }
        });

        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlvFetesSoiree.setVisibility(View.INVISIBLE);
                if (User.bfetes == true || User.bdebit == true) {
                    cliclsoiree = true;
                } else if (User.bfetes == false && User.bdebit == false) {
                    cliclsoiree = false;
                }

                if (cliclsoiree == false) {
                    txLegSoiree.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                    for (int i = 0; i < lstMarkerSoirees.size(); i++) {
                        Marker marker = lstMarkerSoirees.get(i);
                        marker.remove();
                    }
                    lstMarkerSoirees.clear();
                    //lstMarkerSoireeOptions.clear();
                }else{
                    if(txLegSoiree.getPaintFlags() == Paint.STRIKE_THRU_TEXT_FLAG){
                        txLegSoiree.setPaintFlags(txLegSoiree.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                    }

                    for (int i = 0; i < lstMarkerSoirees.size(); i++) {
                        Marker marker = lstMarkerSoirees.get(i);
                        marker.remove();
                    }

                    if(lstMarkerSoireeOptions!=null){
                        for (int j = 0; j < lstMarkerSoireeOptions.size(); j++) {
                            MarkerOptions mkOption = lstMarkerSoireeOptions.get(j);

                            Marker marker = googleMap.addMarker(mkOption);
                            marker.setTag(3);
                            lstMarkerSoirees.add(marker);


                        }
                    }


                }
                RefreshSoiree=true;
                pgLoad.setVisibility(View.VISIBLE);

                User.save();
               //RefreshPositions();


            }
        });

        btFetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bfetes) {
                    btFetes.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bfetes = false;

                } else {
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes = true;
                }

                User.save();

            }
        });

        btDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bdebit) {
                    btDebit.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bdebit = false;

                } else {
                    btDebit.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bdebit = true;
                }

                User.save();
            }
        });

        btconcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bconcert) {
                    btconcert.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bconcert = false;

                } else {
                    btconcert.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bconcert = true;
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes = true;
                }

                User.save();
            }
        });

        txConcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bconcert) {
                    btconcert.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bconcert = false;

                } else {
                    btconcert.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bconcert = true;
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes = true;
                }

                User.save();
            }
        });

        btbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bbar) {
                    btbar.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bbar = false;

                } else {
                    btbar.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bbar = true;
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes = true;
                }

                User.save();
            }
        });

        txBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bbar) {
                    btbar.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bbar = false;

                } else {
                    btbar.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bbar = true;
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes = true;
                }

                User.save();
            }
        });

        btboitenuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bboitenuit) {
                    btboitenuit.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bboitenuit = false;

                } else {
                    btboitenuit.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bboitenuit = true;
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes = true;
                }

                User.save();
            }
        });

        txBoiteNuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bboitenuit) {
                    btboitenuit.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bboitenuit = false;

                } else {
                    btboitenuit.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bboitenuit = true;
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes = true;
                }

                User.save();
            }
        });

        btbarnuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bbarnuit) {
                    btbarnuit.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bbarnuit = false;

                } else {
                    btbarnuit.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bbarnuit = true;
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes = true;
                }

                User.save();
            }
        });

        txBarNuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bbarnuit) {
                    btbarnuit.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bbarnuit = false;

                } else {
                    btbarnuit.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bbarnuit = true;
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes = true;
                }

                User.save();
            }
        });

        btautres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bautre) {
                    btautres.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bautre = false;

                } else {
                    btautres.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bautre = true;
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes = true;
                }

                User.save();
            }
        });

        txAutres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.bautre) {
                    btautres.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bautre = false;

                } else {
                    btautres.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bautre = true;
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes = true;
                }

                User.save();
            }
        });

        imGeoloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(moi).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    /*if(cercleMoi==null){
                        cercleMoi= googleMap.addCircle(new CircleOptions()
                                .center(moi)
                                .radius(300)
                                .strokeWidth(2)
                                .strokeColor(Color.rgb(41, 41, 84))
                                .fillColor(Color.TRANSPARENT));


                    }else{
                        cercleMoi.setCenter(moi);
                    }*/




                }
            }
        });


        imFlecheMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1);
            }
        });


        imLegAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clicAmis == false) {
                    //imLegAmis.setImageResource(R.drawable.amisgrasligne);
                    txLegAmis.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    clicAmis = true;
                    FragmentsSliderActivity.ClicAmis = true;

                    for (int i = 0; i < lstMarkerAmis.size(); i++) {
                        Marker marker = lstMarkerAmis.get(i);
                        marker.remove();
                    }
                    lstMarkerAmis.clear();
                    //lstMarkerAmisOptions.clear();



                } else {
                    //imLegAmis.setImageResource(R.drawable.amisgras);
                    txLegAmis.setPaintFlags(txLegAmis.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                    //txLegAmis.setPaintFlags(Paint);
                    clicAmis = false;
                    FragmentsSliderActivity.ClicAmis = false;

                    if(lstMarkerAmisOptions!=null){
                        for (int j = 0; j < lstMarkerAmisOptions.size(); j++) {

                            MarkerOptions mkOption = lstMarkerAmisOptions.get(j);

                            Marker marker = googleMap.addMarker(mkOption);
                            marker.setTag(1);
                            lstMarkerAmis.add(marker);

                        }
                    }

                    //RefreshPositions();
                }
            }
        });

        imLegKiffs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicKiff == false) {
                    //imLegKiffs.setImageResource(R.drawable.matchgrasligne);
                    clicKiff = true;
                    FragmentsSliderActivity.ClicMatch = true;
                    txLegMatch.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    for (int i = 0; i < lstMarkerMatchs.size(); i++) {
                        Marker marker = lstMarkerMatchs.get(i);
                        marker.remove();
                    }
                    lstMarkerMatchs.clear();
                    //lstMarkerMatchOptions.clear();
                    //RefreshPositions();

                } else {
                    //imLegKiffs.setImageResource(R.drawable.matchgras);
                    txLegMatch.setPaintFlags(txLegMatch.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                    clicKiff = false;
                    FragmentsSliderActivity.ClicMatch = false;

                    if(lstMarkerMatchOptions!=null){
                        for (int j = 0; j < lstMarkerMatchOptions.size(); j++) {

                            MarkerOptions mkOption = lstMarkerMatchOptions.get(j);

                            Marker marker = googleMap.addMarker(mkOption);
                            marker.setTag(2);
                            lstMarkerMatchs.add(marker);

                        }
                    }

                    //RefreshPositions();

                }

            }
        });


        imLegInconnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicInconnu == false) {
                    //imLegInconnu.setImageResource(R.drawable.inconnugrasligne);
                    txLegInconnu.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    clicInconnu = true;
                    FragmentsSliderActivity.ClicInconnu = true;

                    for (int i = 0; i < lstMarkerInconnus.size(); i++) {
                        Marker marker = lstMarkerInconnus.get(i);
                        marker.remove();
                    }
                    lstMarkerInconnus.clear();
                    //lstMarkerInconnusOptions.clear();


                }else {
                    //imLegInconnu.setImageResource(R.drawable.inconnugras);
                    txLegInconnu.setPaintFlags(txLegInconnu.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                    clicInconnu = false;
                    FragmentsSliderActivity.ClicInconnu = false;

                    if(lstMarkerInconnusOptions!=null){
                        for (int j = 0; j < lstMarkerInconnusOptions.size(); j++) {

                            MarkerOptions mkOption = lstMarkerInconnusOptions.get(j);

                            Marker marker = googleMap.addMarker(mkOption);
                            marker.setTag(0);
                            lstMarkerInconnus.add(marker);

                        }
                    }


                }
            }
        });


        //sleep for 1s in background...
        gps = new GPSTracker(rootView.getContext());

        if (gps.canGetLocation == false) {
            gps.showSettingsAlert();

        }

        pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

        editor = pref.edit();
        //User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if(lstMarkerInconnus!=null)
                {
                    lstMarkerInconnus.clear();
                }

                if(lstMarkerAmis!=null)
                {
                    lstMarkerAmis.clear();
                }

                if(lstMarkerMatchs!=null)
                {
                    lstMarkerMatchs.clear();
                }

                if(lstMarkerSoirees!=null)
                {
                    lstMarkerSoirees.clear();
                }

                //googleMap.setMyLocationEnabled(true);
                //RefreshPositions();
                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);


                    MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));


                    markerPosition= googleMap.addMarker(MaPosition);




                    // For zooming automatically to the location of the marker
                    if (FragmentsSliderActivity.Localiser) {
                        //RefreshPositions();
                        // For dropping a marker at a point on the Map
                        pgLoad.setVisibility(View.VISIBLE);
                        moi = new LatLng(Double.parseDouble(FragmentsSliderActivity.latitude), Double.parseDouble(FragmentsSliderActivity.longitude));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(moi).zoom(15).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        FragmentsSliderActivity.Localiser = false;
                        if(FragmentsSliderActivity.MatchLocaliser){

                            WebService WS = new WebService(getContext());
                            JSONArray ListPositions = WS.GetLastPositionUserMatch(User);
                            if(lstMarkerMatchs==null){

                                lstMarkerMatchJS = new ArrayList<JSONObject>();
                            }else{
                                lstMarkerMatchJS.clear();
                            }

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;
                                try {
                                    Position = ListPositions.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                lstMarkerMatchJS.add(Position);
                            }


                            marker = googleMap.addMarker(new MarkerOptions().position(moi).title(FragmentsSliderActivity.UserMap).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                            marker.setTag(2);
                            lstMarkerMatchs.add(marker);
                        }else{
                            WebService WS = new WebService(getContext());
                            JSONArray ListPositions = WS.GetLastPositionUserAmis(User);
                            if(lstMarkerAmisJS==null){

                                lstMarkerAmisJS = new ArrayList<JSONObject>();
                            }else{
                                lstMarkerAmisJS.clear();
                            }

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;
                                try {
                                    Position = ListPositions.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                lstMarkerAmisJS.add(Position);
                            }
                            marker = googleMap.addMarker(new MarkerOptions().position(moi).title(FragmentsSliderActivity.UserMap).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                            marker.setTag(1);
                            lstMarkerAmis.add(marker);
                        }
                        pgLoad.setVisibility(View.GONE);


                    } else {
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(moi).zoom(15).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }


                }

                imPhotoMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), Conversation.class);
                        i.putExtra("id_kiffs", (String) imPhotoMap.getTag());
                        i.putExtra("id_user", User.id_user);
                        i.putExtra("prive", 1);
                        startActivity(i);
                    }
                });

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        imPhotoMap.setVisibility(View.GONE);
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        if (marker.getTitle().toString().equals("soirée")) {
                            WebService WS = new WebService(getContext());

                            JSONArray Soiree = WS.GetInfoSoiree(marker.getTag().toString());

                            String photo = "";
                            if (Soiree != null && Soiree.length() > 0) {

                                try {
                                    txNomSoiree.setText(getResources().getString(R.string.nom) + " : " + Soiree.getJSONObject(0).getString("nomsoiree"));
                                    txAdresse.setText(Soiree.getJSONObject(0).getString("adresse"));
                                    txHoraireSoiree.setText(getResources().getString(R.string.horaireouverture) + " : " + Soiree.getJSONObject(0).getString("horairedebut") + " à " + Soiree.getJSONObject(0).getString("horairefin"));
                                    txDescriptif.setText(Soiree.getJSONObject(0).getString("descriptif"));
                                    txLienSite.setText(Soiree.getJSONObject(0).getString("liensite"));

                                    if(txLienSite.getText().toString().trim().equals("")){
                                        txLienSite.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse(txLienSite.getText().toString()));
                                                startActivity(i);
                                            }
                                        });
                                    }else{
                                        txLienSite.setVisibility(View.GONE);
                                    }

                                    if (Soiree.getJSONObject(0).getString("typedebit").equals("1")) {
                                        txLienSite.setText(getResources().getString(R.string.debit) + " : " + Soiree.getJSONObject(0).getString("choixdebit"));
                                        txTypeSoiree.setVisibility(View.GONE);
                                    } else {
                                        switch (Soiree.getJSONObject(0).getString("typesoiree")) {
                                            case "1":
                                                txTypeSoiree.setText(getResources().getString(R.string.typesoiree) + " : " + getResources().getString(R.string.bar));
                                                break;

                                            case "2":
                                                txTypeSoiree.setText(getResources().getString(R.string.typesoiree) + " : " + getResources().getString(R.string.barnuit));
                                                break;

                                            case "3":
                                                txTypeSoiree.setText(getResources().getString(R.string.typesoiree) + " : " + getResources().getString(R.string.boitenuit));
                                                break;

                                            case "4":
                                                txTypeSoiree.setText(getResources().getString(R.string.typesoiree) + " : " + getResources().getString(R.string.concert));
                                                break;

                                            case "0":
                                                txTypeSoiree.setVisibility(View.GONE);
                                                break;


                                        }

                                    }
                                    lnDetailSoiree.setVisibility(View.VISIBLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } else {
                            WebService WS = new WebService(getContext());

                            if(marker.getTag()!=null){
                                if((int) marker.getTag()==1){
                                    //amis
                                    for(int i = 0;i < lstMarkerAmisJS.size(); i++){
                                        JSONObject Obj = lstMarkerAmisJS.get(i);
                                        try {
                                            if(Obj.get("pseudo").equals(marker.getTitle())){
                                                JSONArray UserList = WS.GetinfoUser((String) Obj.get("id_user"));
                                                String photo = "";
                                                if (UserList != null && UserList.length() > 0) {
                                                    try {
                                                        photo = UserList.getJSONObject(0).getString("photo");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Picasso.with(getContext()).load(photo).resize(300, 300).into(imPhotoMap);
                                                    //imPhotoMap.setImageBitmap(bitmap);
                                                    try {
                                                        imPhotoMap.setTag(UserList.getJSONObject(0).getString("id"));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    imPhotoMap.setVisibility(View.VISIBLE);
                                                } else {
                                                    imPhotoMap.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                if((int) marker.getTag()==2){
                                    //amis
                                    for(int i = 0;i < lstMarkerMatchJS.size(); i++){
                                        JSONObject Obj = lstMarkerMatchJS.get(i);
                                        try {
                                            if(Obj.get("pseudo").equals(marker.getTitle())){
                                                JSONArray UserList = WS.GetinfoUser((String) Obj.get("id_user"));
                                                String photo = "";
                                                if (UserList != null && UserList.length() > 0) {
                                                    try {
                                                        photo = UserList.getJSONObject(0).getString("photo");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Picasso.with(getContext()).load(photo).resize(300, 300).into(imPhotoMap);
                                                    //imPhotoMap.setImageBitmap(bitmap);
                                                    try {
                                                        imPhotoMap.setTag(UserList.getJSONObject(0).getString("id"));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    imPhotoMap.setVisibility(View.VISIBLE);
                                                } else {
                                                    imPhotoMap.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                if((int) marker.getTag()==3){
                                    //amis
                                    for(int i = 0;i < lstMarkerSoireesJS.size(); i++){
                                        JSONObject Obj = lstMarkerSoireesJS.get(i);
                                        try {
                                            if(Obj.get("nomsoiree").equals(marker.getTitle())){
                                                JSONArray Soiree = WS.GetInfoSoiree((String) Obj.get("id"));

                                                String photo = "";
                                                if (Soiree != null && Soiree.length() > 0) {

                                                    try {
                                                        txNomSoiree.setText(getResources().getString(R.string.nom) + " : " + Soiree.getJSONObject(0).getString("nomsoiree"));
                                                        txAdresse.setText(Soiree.getJSONObject(0).getString("adresse"));
                                                        txHoraireSoiree.setText(getResources().getString(R.string.horaireouverture) + " : " + Soiree.getJSONObject(0).getString("horairedebut") + " à " + Soiree.getJSONObject(0).getString("horairefin"));
                                                        txDescriptif.setText(Soiree.getJSONObject(0).getString("descriptif"));
                                                        txLienSite.setText( Soiree.getJSONObject(0).getString("liensite"));

                                                        if(!txLienSite.getText().toString().trim().equals("")){
                                                            txLienSite.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                                                            txLienSite.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    String url = txLienSite.getText().toString();
                                                                    if (!url.startsWith("https://") && !url.startsWith("http://")){
                                                                        url = "http://" + url;
                                                                    }
                                                                    Intent i = new Intent(Intent.ACTION_VIEW,
                                                                            Uri.parse(url));
                                                                    getActivity().startActivity(i);
                                                                }

                                                            });
                                                        }else{
                                                            txLienSite.setVisibility(View.GONE);
                                                        }

                                                        if (Soiree.getJSONObject(0).getString("typedebit").equals("1")) {
                                                            txLienSite.setText(getResources().getString(R.string.debit) + " : " + Soiree.getJSONObject(0).getString("choixdebit"));
                                                            txTypeSoiree.setVisibility(View.GONE);
                                                        } else {
                                                            switch (Soiree.getJSONObject(0).getString("typesoiree")) {
                                                                case "1":
                                                                    txTypeSoiree.setText(getResources().getString(R.string.typesoiree) + " : " + getResources().getString(R.string.bar));
                                                                    break;

                                                                case "2":
                                                                    txTypeSoiree.setText(getResources().getString(R.string.typesoiree) + " : " + getResources().getString(R.string.barnuit));
                                                                    break;

                                                                case "3":
                                                                    txTypeSoiree.setText(getResources().getString(R.string.typesoiree) + " : " + getResources().getString(R.string.boitenuit));
                                                                    break;

                                                                case "4":
                                                                    txTypeSoiree.setText(getResources().getString(R.string.typesoiree) + " : " + getResources().getString(R.string.concert));
                                                                    break;

                                                                case "0":
                                                                    txTypeSoiree.setVisibility(View.GONE);
                                                                    break;


                                                            }

                                                        }
                                                        lnDetailSoiree.setVisibility(View.VISIBLE);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            }




                        }
                        //Using position get Value from arraylist
                        return false;
                    }
                });


            }
        });


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return rootView;

    }

    public void LoadSoiree(){
        if (imSoireeLongClic == false) {
            if (cliclsoiree) {
                //cliclsoiree = false;
                FragmentsSliderActivity.ClicSoiree = true;

                lstMarkerSoirees = new ArrayList<Marker>();

                //txLegSoiree.setPaintFlags(txLegSoiree.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                //imSoiree.setImageResource(R.drawable.boutonsoiree);
                if (User != null) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    // For dropping a marker at a point on the Map
                    /*LatLng moi = new LatLng(latitude, longitude);

                    MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));


                    googleMap.addMarker(MaPosition);*/
                    WebService WS = new WebService(getContext());
                    JSONArray ListPositions;

                    ListPositions = WS.GetSoirees(User);

                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;

                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            marker = googleMap.addMarker(new MarkerOptions().position(user).title("soirée").icon(BitmapDescriptorFactory.fromResource(R.drawable.bouteillesoiree)));

                            marker.setTag(Position.getString("id"));


                            lstMarkerSoirees.add(marker);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            } else {
                //cliclsoiree = true;
                FragmentsSliderActivity.ClicSoiree = false;
                //imSoiree.setImageResource(R.drawable.soireebarre);
                //txLegSoiree.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                for (int i = 0; i < lstMarkerSoirees.size(); i++) {
                    Marker marker = lstMarkerSoirees.get(i);
                    marker.remove();

                }


            }
        } else {
            imSoireeLongClic = false;
        }

    }

    public void RefreshPositions()
    {
        double latitude;
        double longitude;

        if (User != null && googleMap!=null) {
            WebService WS = new WebService(getContext());
            JSONArray ListPositions;

            if (cliclsoiree == true && RefreshSoiree) {
                ListPositions = WS.GetSoirees(User);
                RefreshSoiree=false;
                if(lstMarkerSoirees==null){
                    lstMarkerSoirees = new ArrayList<Marker>();
                    lstMarkerSoireesJS = new ArrayList<JSONObject>();
                    lstMarkerSoireeOptions = new ArrayList<MarkerOptions>();
                }
                Boolean bmarkerTrouve=false;
                if (ListPositions != null) {
                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;
                        bmarkerTrouve=false;
                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            for (int j = 0; j < lstMarkerSoireeOptions.size(); j++) {
                                MarkerOptions mkOptions = lstMarkerSoireeOptions.get(j);
                                if(mkOptions.getTitle().equals(Position.getString("nomsoiree"))){
                                    if(mkOptions.getPosition().latitude!=latitude) {
                                        //marker.remove();
                                        lstMarkerSoireeOptions.remove(marker);
                                        lstMarkerSoireesJS.add(Position);
                                        mkOptions = new MarkerOptions().position(user).title(Position.getString("nomsoiree")).icon(BitmapDescriptorFactory.fromResource(R.drawable.bouteillesoiree));
                                        //marker.setTag(Position.getString("id"));
                                        lstMarkerSoireeOptions.add(mkOptions);

                                    }
                                    bmarkerTrouve=true;

                                }
                            }

                            if(bmarkerTrouve==false){
                                lstMarkerSoireesJS.add(Position);
                                MarkerOptions mkOptions = new MarkerOptions().position(user).title(Position.getString("nomsoiree")).icon(BitmapDescriptorFactory.fromResource(R.drawable.bouteillesoiree));
                                //marker.setTag(Position.getString("id"));
                                lstMarkerSoireeOptions.add(mkOptions);
                            }

                           // marker = googleMap.addMarker(new MarkerOptions().position(user).title("soirée").icon(BitmapDescriptorFactory.fromResource(R.drawable.bouteillesoiree)));
                            //marker.setTag(Position.getString("id"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }/*else{
                if(lstMarkerSoirees!=null){
                    for (int i = 0; i < lstMarkerSoirees.size(); i++) {
                        Marker marker = lstMarkerSoirees.get(i);
                        marker.remove();
                    }
                    lstMarkerSoirees.clear();
                }

            }*/

            //if (clicInconnu == false) {
                ListPositions = WS.GetLastPositionUserInconnu(User);

                if(lstMarkerInconnus==null){
                    lstMarkerInconnus = new ArrayList<Marker>();
                    lstMarkerInconnusOptions = new ArrayList<MarkerOptions>();
                }else{
                    lstMarkerInconnusRemove = new ArrayList<Marker>();
                }




                Boolean bmarkerTrouve=false;
                for (int i = 0; i < ListPositions.length(); i++) {
                    JSONObject Position = null;

                    try {
                        Position = ListPositions.getJSONObject(i);

                        latitude = Position.getDouble("latitude");
                        longitude = Position.getDouble("longitude");
                        LatLng user = new LatLng(latitude, longitude);
                        //recherche si présent dans la liste des inconnus
                        for (int j = 0; j < lstMarkerInconnusOptions.size(); j++) {
                            MarkerOptions mk = lstMarkerInconnusOptions.get(j);
                            if(mk.getTitle().equals(Position.getString("pseudo"))){
                                if(mk.getPosition().latitude!=latitude) {
                                    //marker.remove();
                                    /*for (int k=0;k<lstMarkerInconnus.size();k++){
                                        Marker mark = lstMarkerInconnus.get(k);
                                        if(mark.getTitle().equals(Position.getString("pseudo"))){
                                            lstMarkerInconnusRemove.add(mark);
                                            lstMarkerInconnus.remove(mark);
                                        }
                                    }*/
                                    lstMarkerInconnusOptions.remove(mk);
                                    //marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                                    //marker.setTag(0);
                                    //lstMarkerInconnus.add(marker);
                                    MarkerOptions mkOption=new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose));

                                    lstMarkerInconnusOptions.add(mkOption);

                                }
                                bmarkerTrouve=true;

                            }
                        }

                        if(bmarkerTrouve==false){
                            //marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                            //marker.setTag(0);

                            //lstMarkerInconnus.add(marker);
                            MarkerOptions mkOption=new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose));
                            lstMarkerInconnusOptions.add(mkOption);
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
           // }//else{

            //}

           // if (clicKiff == false) {
                bmarkerTrouve=false;
                ListPositions = WS.GetLastPositionUserMatch(User);

                if(lstMarkerMatchs==null){
                    lstMarkerMatchs = new ArrayList<Marker>();
                    lstMarkerMatchOptions = new ArrayList<MarkerOptions>();
                    lstMarkerMatchJS = new ArrayList<JSONObject>();
                }else{
                    lstMarkerMatchRemove = new ArrayList<Marker>();
                }

                for (int i = 0; i < ListPositions.length(); i++) {
                    JSONObject Position = null;

                    try {
                        Position = ListPositions.getJSONObject(i);

                        latitude = Position.getDouble("latitude");
                        longitude = Position.getDouble("longitude");

                        LatLng user = new LatLng(latitude, longitude);

                        for (int j = 0; j < lstMarkerMatchOptions.size(); j++) {
                            MarkerOptions mk = lstMarkerMatchOptions.get(j);
                            if(mk.getTitle().equals(Position.getString("pseudo"))){
                                if(mk.getPosition().latitude!=latitude) {


                                    //mk.getPosition().latitude=latitude;
                                    lstMarkerMatchOptions.remove(marker);
                                    //lstMarkerMatchJS.add(Position);
                                    MarkerOptions mkOption = new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte));
                                    //marker.setTag(Position.getString("id_user"));
                                    lstMarkerMatchOptions.add(mkOption);
                                }


                                bmarkerTrouve=true;
                            }
                        }

                        if(bmarkerTrouve==false){
                            MarkerOptions mkOption = new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte));
                            lstMarkerMatchJS.add(Position);
                            //marker.setTag(Position.getString("id_user"));
                            lstMarkerMatchOptions.add(mkOption);
                        }

                        //marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                        //marker.setTag(Position.getString("id_user"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            //}

            //if (clicAmis == false) {
                ListPositions = WS.GetLastPositionUserAmis(User);
                bmarkerTrouve=false;


                if(lstMarkerAmis==null){
                    lstMarkerAmis = new ArrayList<Marker>();
                    lstMarkerAmisRemove = new ArrayList<Marker>();
                    lstMarkerAmisJS = new ArrayList<JSONObject>();
                    lstMarkerAmisOptions = new ArrayList<MarkerOptions>();
                }else{
                    lstMarkerAmisRemove = new ArrayList<Marker>();
                }

                for (int i = 0; i < ListPositions.length(); i++) {
                    JSONObject Position = null;

                    try {
                        Position = ListPositions.getJSONObject(i);

                        latitude = Position.getDouble("latitude");
                        longitude = Position.getDouble("longitude");

                        LatLng user = new LatLng(latitude, longitude);

                        for (int j = 0; j < lstMarkerAmisOptions.size(); j++) {
                            MarkerOptions marker = lstMarkerAmisOptions.get(j);
                            if(marker.getTitle().equals(Position.getString("pseudo"))){
                                if(marker.getPosition().latitude!=latitude){
                                    if(marker.getPosition().latitude!=latitude) {

                                        lstMarkerAmisOptions.remove(marker);
                                        //lstMarkerAmisJS.add(Position);
                                        marker = new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu));
                                        //marker.set(Position);
                                        ///marker.setTag(Position.getString("id_user"));
                                        lstMarkerAmisOptions.add(marker);
                                    }

                                }



                                bmarkerTrouve=true;
                            }
                        }

                        if(bmarkerTrouve==false){
                            MarkerOptions mkOption = new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu));
                            lstMarkerAmisJS.add(Position);
                            ///marker.setTag(Position.getString("id_user"));
                            lstMarkerAmisOptions.add(mkOption);
                        }

                        //marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                        //marker.setTag(Position.getString("id_user"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            ///

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMap = new RefreshMap();
        refreshMap.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        pgLoad.setVisibility(View.VISIBLE);


    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {

             User.nbaffichemap = User.nbaffichemap + 1;

            pgLoad.setVisibility(View.VISIBLE);

            if (User.nbaffichemap == 3 && User.popupmap == 0) {
                impopupmap.setVisibility(View.VISIBLE);
            }
            if(refreshMap==null){

                refreshMap = new RefreshMap();
                refreshMap.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }else{
                if(refreshMap.isCancelled()){
                    refreshMap = new RefreshMap();
                    refreshMap.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }


            LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().getBaseContext().LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    //Toast.makeText(getActivity().getApplicationContext(),"Changement", Toast.LENGTH_SHORT).show();  //this never appear
                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(location.getLatitude(), location.getLongitude());

                    //googleMap.clear();
                    double latitude = 0;
                    double longitude = 0;



                    if(MaPosition==null){
                        MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));
                        markerPosition = googleMap.addMarker(MaPosition);
                    }else{

                        markerPosition.setPosition(moi);
                        /*if(cercleMoi!=null){
                            cercleMoi.setCenter(moi);
                        }*/
                        //MaPosition.position(moi);
                    }

                    if(User!=null) {
                        WebService WS = new WebService(getContext());

                        User.latitude = location.getLatitude();
                        User.longitude = location.getLongitude();


                        WS.SetLastPosition(User);
                    }

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1
                );

                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        1
                );


                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1.0f, locationListener);




        }else{
            if(refreshMap!=null){
                refreshMap.cancel(true);
            }
            if(locationManager!=null){
                locationManager.removeUpdates(this);
            }


        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // For dropping a marker at a point on the Map
        LatLng moi = new LatLng(location.getLatitude(),location.getLongitude());

        //googleMap.clear();
        double latitude = 0;
        double longitude = 0;

        if(MaPosition==null){
            MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));
            markerPosition = googleMap.addMarker(MaPosition);
        }else{
            markerPosition.setPosition(moi);
            /*if(cercleMoi!=null){
                cercleMoi.setCenter(moi);
            }*/

            //MaPosition.position(moi);
        }




        if(User!=null){
            WebService WS = new WebService(getContext());

            User.latitude = location.getLatitude();
            User.longitude = location.getLongitude();


            WS.SetLastPosition(User);





        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    class RefreshMap extends AsyncTask {


        int boucle=1;
        boolean trouve=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            publishProgress(0);
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            if(googleMap!=null){
                //googleMap.clear();
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                // For dropping a marker at a point on the Map
                LatLng moi = new LatLng(latitude, longitude);


                if(MaPosition==null){
                    MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));
                    markerPosition=googleMap.addMarker(MaPosition);



                    /*cercleMoi = googleMap.addCircle(new CircleOptions()
                            .center(moi)
                            .radius(300)
                            .strokeWidth(2)
                            .strokeColor(Color.rgb(41,41,84))
                            .fillColor(Color.TRANSPARENT));*/

                }else{
                    markerPosition.setPosition(moi);
                    /*if(cercleMoi==null){
                        cercleMoi=googleMap.addCircle(new CircleOptions()
                                .center(moi)
                                .radius(300)
                                .strokeWidth(2)
                                .strokeColor(Color.rgb(41,41,84))
                                .fillColor(Color.TRANSPARENT));
                    }else{
                        cercleMoi.setCenter(moi);
                    }*/

                }

                for (int j = 0; j < lstMarkerInconnusRemove.size(); j++) {
                    Marker mk = lstMarkerInconnusRemove.get(j);
                    mk.remove();

                }

                if(lstMarkerInconnusOptions!=null && clicInconnu==false){
                    for (int j = 0; j < lstMarkerInconnusOptions.size(); j++) {
                        MarkerOptions mkOption = lstMarkerInconnusOptions.get(j);
                        trouve=false;
                        for(int f=0; f < lstMarkerInconnus.size(); f++){
                            Marker mk = lstMarkerInconnus.get(f);
                            if(mkOption.getTitle().toString().equals(mk.getTitle().toString())){
                                mk.setPosition(mkOption.getPosition());
                                trouve=true;
                            }
                        }

                        if(trouve==false){
                            Marker marker = googleMap.addMarker(mkOption);
                            marker.setTag(0);
                            lstMarkerInconnus.add(marker);
                        }

                        //marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));

                    }
                }

                for (int j = 0; j < lstMarkerAmisRemove.size(); j++) {
                    Marker mk = lstMarkerAmisRemove.get(j);
                    mk.remove();

                }

                for (int j = 0; j < lstMarkerAmisRemove.size(); j++) {
                    Marker mk = lstMarkerAmisRemove.get(j);
                    mk.remove();

                }

                if(lstMarkerAmisOptions!=null && clicAmis==false){
                    for (int j = 0; j < lstMarkerAmisOptions.size(); j++) {
                        MarkerOptions mkOption = lstMarkerAmisOptions.get(j);
                        trouve=false;
                        for(int f=0; f < lstMarkerAmis.size(); f++){
                            Marker mk = lstMarkerAmis.get(f);
                            if(mkOption.getTitle().toString().equals(mk.getTitle().toString())){
                                mk.setPosition(mkOption.getPosition());
                                trouve=true;
                            }
                        }

                        if(trouve==false){
                            Marker marker = googleMap.addMarker(mkOption);
                            marker.setTag(1);
                            lstMarkerAmis.add(marker);
                        }

                        //marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));

                    }
                }


                for (int j = 0; j < lstMarkerMatchRemove.size(); j++) {
                    Marker mk = lstMarkerMatchRemove.get(j);
                    mk.remove();

                }

                if(lstMarkerMatchOptions!=null && clicKiff==false){
                    for (int j = 0; j < lstMarkerMatchOptions.size(); j++) {
                        MarkerOptions mkOption = lstMarkerMatchOptions.get(j);
                        trouve=false;
                        for(int f=0; f < lstMarkerMatchs.size(); f++){
                            Marker mk = lstMarkerMatchs.get(f);
                            if(mkOption.getTitle().toString().equals(mk.getTitle().toString())){
                                mk.setPosition(mkOption.getPosition());
                                trouve=true;
                            }
                        }

                        if(trouve==false){
                            Marker marker = googleMap.addMarker(mkOption);
                            marker.setTag(2);
                            lstMarkerMatchs.add(marker);
                        }

                        //marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));

                    }
                }

                if(lstMarkerSoireeOptions!=null && cliclsoiree){
                    for (int j = 0; j < lstMarkerSoireeOptions.size(); j++) {
                        MarkerOptions mkOption = lstMarkerSoireeOptions.get(j);
                        trouve=false;
                        for(int f=0; f < lstMarkerSoirees.size(); f++){
                            Marker mk = lstMarkerSoirees.get(f);
                            if(mkOption.getTitle().toString().equals(mk.getTitle().toString())){
                                mk.setPosition(mkOption.getPosition());
                                trouve=true;
                            }
                        }

                        if(trouve==false){
                            Marker marker = googleMap.addMarker(mkOption);
                            marker.setTag(3);
                            lstMarkerSoirees.add(marker);
                        }

                        //marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));

                    }
                }





            }
            pgLoad.setVisibility(View.GONE);

        }

        @Override
        protected Void doInBackground(Object... params) {
            if(User!=null){

                while(boucle==1){
                    try {
                        Thread.sleep(200);
                        RefreshPositions();
                        if(isCancelled()){
                            boucle=2;
                        }
                        //RefreshPositions();
                        publishProgress(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pgLoad.setVisibility(View.VISIBLE);


        }
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /*private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            // For dropping a marker at a point on the Map
            if(googleMap!=null){
                LatLng moi = new LatLng(location.getLatitude(),location.getLongitude());

                googleMap.clear();
                double latitude = 0;
                double longitude = 0;
                MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));

                googleMap.addMarker(MaPosition);

                if(User!=null){
                    WebService WS = new WebService(getContext());
                    JSONArray ListPositions;

                    if (cliclsoiree == false) {
                        ListPositions = WS.GetSoirees(User);
                        if(ListPositions!=null){
                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title("soirée").icon(BitmapDescriptorFactory.fromResource(R.drawable.bouteillesoiree)));
                                    marker.setTag(Position.getString("id"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }



                    }

                    if (clicInconnu == false) {
                        ListPositions = WS.GetLastPositionUserInconnu(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                                marker.setTag("0");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    if (clicKiff == false) {
                        ListPositions = WS.GetLastPositionUserMatch(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                                marker.setTag(Position.getString("id_user"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    if (clicAmis == false) {
                        ListPositions = WS.GetLastPositionUserAmis(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                                marker.setTag(Position.getString("id_user"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }

            }


        }

        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        public void onProviderEnabled(String s) {

        }

        public void onProviderDisabled(String s) {

        }
    }*/
}


