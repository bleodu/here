package com.kenfestoche.smartcoder.kenfestoche;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class MapActivity extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    MapView mMapView;
    private GoogleMap googleMap;

    GPSTracker gps;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Utilisateur User;
    ImageView imLegAmis;
    ImageView imLegInconnu;
    ImageView imLegKiffs;
    ImageView imFlecheMap;
    ImageView imGeoloc;
    ImageView imPhotoMap;
    ViewPager pager;
    Boolean clicAmis;
    Boolean clicKiff;
    Boolean clicInconnu;

    RefreshMap refreshMap;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        clicAmis=false;
        clicInconnu=false;
        clicKiff=false;
        pager = (ViewPager) container;

        View rootView = inflater.inflate(R.layout.activity_map, container, false);

        imLegAmis = (ImageView) rootView.findViewById(R.id.imlegamis);
        imLegKiffs = (ImageView) rootView.findViewById(R.id.imlegmatch);
        imLegInconnu = (ImageView) rootView.findViewById(R.id.imleginconnu);
        imFlecheMap = (ImageView) rootView.findViewById(R.id.imFlecheDroiteMap);
        imGeoloc = (ImageView) rootView.findViewById(R.id.imgeoloc);
        imPhotoMap = (ImageView) rootView.findViewById(R.id.imPhotoMap);

        imLegAmis.setImageResource(R.drawable.legendeamis);
        imLegKiffs.setImageResource(R.drawable.legendematch);
        imLegInconnu.setImageResource(R.drawable.legendeinconnu);
        imGeoloc.setImageResource(R.drawable.flechemap);
        imPhotoMap.setVisibility(View.INVISIBLE);

        imGeoloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if GPS enabled
                if(gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(moi).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


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
                    imLegAmis.setImageResource(R.drawable.legendeamisbarre);
                    clicAmis = true;

                    googleMap.clear();
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);

                    googleMap.addMarker(new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi)));

                    if (User != null) {
                        WebService WS = new WebService();
                        JSONArray ListPositions;
                        if (clicInconnu == false) {
                            ListPositions = WS.GetLastPositionUserInconnu(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet("0").icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));

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

                                    googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));

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

                                    googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }

                } else {
                    imLegAmis.setImageResource(R.drawable.legendeamis);
                    clicAmis = false;
                    double latitude;
                    double longitude;
                    WebService WS = new WebService();
                    JSONArray ListPositions = WS.GetLastPositionUserAmis(User);

                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;

                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });

        imLegKiffs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clicKiff==false){
                    imLegKiffs.setImageResource(R.drawable.legendematchsbarre);
                    clicKiff=true;

                    googleMap.clear();
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);

                    googleMap.addMarker(new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi)));

                    if(User!=null) {
                        WebService WS = new WebService();
                        JSONArray ListPositions;
                        if (clicInconnu == false) {
                            ListPositions = WS.GetLastPositionUserInconnu(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet("0").icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));

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

                                    googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));

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

                                    googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }
                }else{
                    imLegKiffs.setImageResource(R.drawable.legendematch);
                    clicKiff=false;

                    double latitude;
                    double longitude;
                    WebService WS = new WebService();
                    JSONArray ListPositions = WS.GetLastPositionUserMatch(User);

                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;

                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }

            }
        });

        imLegInconnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clicInconnu==false){
                    imLegInconnu.setImageResource(R.drawable.legendeinconnusbarre);
                    clicInconnu=true;


                    googleMap.clear();
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);

                    googleMap.addMarker(new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi)));

                    if(User!=null) {
                        WebService WS = new WebService();
                        JSONArray ListPositions;
                        if (clicInconnu == false) {
                            ListPositions = WS.GetLastPositionUserInconnu(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet("0").icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));

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

                                    googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));

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

                                    googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }

                }else{
                    imLegInconnu.setImageResource(R.drawable.legendeinconnu);
                    clicInconnu=false;

                    double latitude;
                    double longitude;
                    WebService WS = new WebService();
                    JSONArray ListPositions = WS.GetLastPositionUserInconnu(User);

                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;

                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet("0").icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
                //imLegInconnu.setImageResource(R.drawable.legendeinconnusbarre);
            }
        });



        //sleep for 1s in background...
        gps = new GPSTracker(rootView.getContext());

        if(gps.canGetLocation==false){
            gps.showSettingsAlert();

        }

        pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

        editor = pref.edit();
        User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                //googleMap.setMyLocationEnabled(true);

                // check if GPS enabled
                if(gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);
                    googleMap.addCircle(new CircleOptions()
                            .center(moi)
                            .radius(300)
                            .strokeWidth(2)
                            .strokeColor(Color.rgb(41,41,84))
                            .fillColor(Color.TRANSPARENT));



                    // For showing a move to my location button
                    //googleMap.setMyLocationEnabled(true);

                    googleMap.addMarker(new MarkerOptions().position(moi).title("Ma Position").snippet("").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi)));

                    if(User!=null){
                        WebService WS = new WebService();
                        JSONArray ListPositions = WS.GetLastPositionUserInconnu(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet("0").icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        ListPositions = WS.GetLastPositionUserMatch(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        ListPositions = WS.GetLastPositionUserAmis(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }


                    // For zooming automatically to the location of the marker
                    if(FragmentsSliderActivity.Localiser){
                        // For dropping a marker at a point on the Map
                        moi = new LatLng(Double.parseDouble(FragmentsSliderActivity.latitude),Double.parseDouble(FragmentsSliderActivity.longitude));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(moi).zoom(15).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }else{
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(moi).zoom(15).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }


                }

                imPhotoMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(),Conversation.class);
                        i.putExtra("id_kiffs",(String) imPhotoMap.getTag());
                        i.putExtra("id_user",User.id_user);
                        i.putExtra("prive",1);
                        startActivity(i);
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        WebService WS = new WebService();

                        JSONArray UserList = WS.GetinfoUser(marker.getSnippet());

                        String photo ="";
                        if(UserList!=null && UserList.length()>0){
                            try {
                                photo = UserList.getJSONObject(0).getString("photo");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            URL pictureURL;

                            pictureURL = null;

                            try {
                                pictureURL = new URL(photo);
                            } catch (MalformedURLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Bitmap bitmap=null;
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
                            imPhotoMap.setImageBitmap(bitmap);
                            try {
                                imPhotoMap.setTag(UserList.getJSONObject(0).getString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            imPhotoMap.setVisibility(View.VISIBLE);
                        }else{
                            imPhotoMap.setVisibility(View.INVISIBLE);
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




        refreshMap = new RefreshMap();
        refreshMap.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        return rootView;

    }

    class RefreshMap extends AsyncTask {



        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);

            googleMap.clear();
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // For dropping a marker at a point on the Map
            LatLng moi = new LatLng(latitude, longitude);

            googleMap.addMarker(new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi)));

            if (User != null) {
                WebService WS = new WebService();
                JSONArray ListPositions;
                if (clicInconnu == false) {
                    ListPositions = WS.GetLastPositionUserInconnu(User);

                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;

                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet("0").icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));

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

                            googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));

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

                            googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).snippet(Position.getString("id_user")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }


            }

        }

        @Override
        protected Void doInBackground(Object... params) {
            if(User!=null){

                while(true){
                    try {
                        Thread.sleep(10000);

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
}
