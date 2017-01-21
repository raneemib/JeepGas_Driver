package com.raneem.omer.jeepgas_driver;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Marker currentMarker;
    // Location Objects
    private GoogleApiClient googleApiClient; // Object that is used to connect to google maps API, must be built to use fused location
    private LocationRequest locationRequest; //Object that requests a quality of service for location updates from fused location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //build google api client for fused location method
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //test custom marker
        BitmapDescriptor gasicon = BitmapDescriptorFactory.fromResource(R.drawable.mapmarkergasicon);
        BitmapDescriptor fixicon = BitmapDescriptorFactory.fromResource(R.drawable.mapmarkerfixicon);
        /**
         *MOCK DATA
         *TODO: Remove mockdata and make clients orders locations dynamic
         **/

        LatLng currentLocation_tmp1 = new LatLng(31.822307, 35.235999);
        mMap.addMarker(new MarkerOptions().position(currentLocation_tmp1).title("Steve").icon(gasicon));

        LatLng currentLocation_tmp2 = new LatLng(31.815894, 35.205624);
        mMap.addMarker(new MarkerOptions().position(currentLocation_tmp2).title("Omer").icon(fixicon));

        LatLng currentLocation_tmp3 = new LatLng(31.769881, 35.194280);
        mMap.addMarker(new MarkerOptions().position(currentLocation_tmp3).title("Raneem").icon(gasicon));

    }

    public void gotoLocation(Location location) {
        BitmapDescriptor drivericon = BitmapDescriptorFactory.fromResource(R.drawable.mapmarkertruckdriver);
        if (currentMarker != null) {
            currentMarker.remove();
        }
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        //currentMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));
        currentMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(currentLocation).title("You're Here").icon(drivericon));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);
        mMap.animateCamera(update);
    }

    @Override
    public void onConnected(Bundle bundle) {

        locationRequest = locationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Search for new location each 5 seconds
        locationRequest.setFastestInterval(5000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    //  Disconnect apiClient and close cursor

    protected void onResume() {

        super.onResume();
        googleApiClient.connect();
    }

    protected void onPause() {

        super.onPause();
        googleApiClient.disconnect();
    }

    protected void onStop() {

        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double current_lat = location.getLatitude();
        double current_lng = location.getLongitude();

        LatLng currentLocation_tmp2 = new LatLng(current_lat, current_lng);
        Log.d("Location", current_lat + " / " + current_lng);
        gotoLocation(location);
    }
}


