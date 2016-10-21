package com.kenfestoche.smartcoder.kenfestoche;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
    ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        pager = (ViewPager) container;

        View rootView = inflater.inflate(R.layout.activity_map, container, false);

        imLegAmis = (ImageView) rootView.findViewById(R.id.imlegamis);
        imLegKiffs = (ImageView) rootView.findViewById(R.id.imlegmatch);
        imLegInconnu = (ImageView) rootView.findViewById(R.id.imleginconnu);
        imFlecheMap = (ImageView) rootView.findViewById(R.id.imFlecheDroiteMap);

        imLegAmis.setImageResource(R.drawable.legendeamis);
        imLegKiffs.setImageResource(R.drawable.legendematch);
        imLegInconnu.setImageResource(R.drawable.legendeinconnu);



        imFlecheMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1);
            }
        });


        imLegAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imLegAmis.setImageResource(R.drawable.legendeamisbarre);
            }
        });

        imLegKiffs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imLegKiffs.setImageResource(R.drawable.legendematchsbarre);
            }
        });

        imLegInconnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imLegInconnu.setImageResource(R.drawable.legendeinconnusbarre);
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

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;



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

                    googleMap.addMarker(new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi)));

                    if(User!=null){
                        WebService WS = new WebService();
                        JSONArray ListPositions = WS.GetLastPositionUser(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                googleMap.addMarker(new MarkerOptions().position(moi).title(Position.getString("pseudo")).snippet(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }


                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(moi).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }




            }
        });

        return rootView;

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
