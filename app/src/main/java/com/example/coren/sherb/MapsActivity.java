package com.example.coren.sherb;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends Main implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {


    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);

    private Marker mSydney;
    private Marker mBrisbane;
    private GoogleMap mMap;
    private boolean host;
    private ArrayList<Party> parties;

    /**
     * This shows how to create a simple activity with a map and a marker on the map.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        host = getIntent().getBooleanExtra("key_one",false);
        parties = (ArrayList<Party>) getIntent().getSerializableExtra("parties");

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        Button create_button = findViewById(R.id.create_button);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HostPartyActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        /*SEARCH ALL THE PARTIES IN THE SERVER AND CREATE A MARKER FOR EACH OF THEM*/
        for(Party p : parties){
            Marker currentMarker;
            if(false/*Condition creer une party à ma position*/){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //mMap.setMyLocationEnabled(true);

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title(p.getTitre())
                        .snippet(p.getDescription())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                        .draggable(false));
                currentMarker.setTag(0);
            } else {
                // Show rationale and request permission.
                Toast.makeText(this,
                        "You must agreed with the localisation permission",
                        Toast.LENGTH_SHORT).show();
            } } else{

            currentMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                    .title(p.getTitre())
                    .snippet(p.getDescription())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .draggable(false));
            currentMarker.setTag(0);
            }
        }

        mBrisbane = mMap.addMarker(new MarkerOptions()
                .position(BRISBANE)
                .title("Brisbane")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));
        mBrisbane.setTag(0);

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        // Set a listener for info window events.
        mMap.setOnInfoWindowClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
            marker.showInfoWindow();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    /** Called when the user clicks a Info Window. */
    @Override
    public void onInfoWindowClick(Marker marker) {
        //Create a new party is not the best way, we need to find a way to send the current party to the next activity
        Party p = new Party("idhost",getType(), marker.getTitle(),marker.getSnippet(), getPrice(),getMax_guest(), getSchedule(),marker.getPosition().latitude,marker.getPosition().longitude);
        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("party",p);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private String getType() {
        return "anniversaire";
    }

    private int getPrice() {
        return 20;
    }

    private boolean getStatus(){
        return true;
    }

    private int getMax_guest() {
        return 1;
    }

    private String getSchedule(){
        return "lala";
    }
}
