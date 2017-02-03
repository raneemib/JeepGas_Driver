package com.raneem.omer.jeepgas_driver;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.Frame;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Marker currentMarker;
    // Location Objects
    private GoogleApiClient googleApiClient; // Object that is used to connect to google maps API, must be built to use fused location
    private LocationRequest locationRequest; //Object that requests a quality of service for location updates from fused location

    private FrameLayout frameLayout;

    private Cursor c;
    private DBHelper db;
    private Map<String, Map<String, String>> clients_hashmap;
    private double lat;
    private double lng;
    private String name;
    private String DriverID;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        frameLayout = (FrameLayout) findViewById(R.id.turnLocationOff);

        //build google api client for fused location method
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(googleApiClient.isConnected()) {
                    googleApiClient.disconnect();
                    Log.d("Connect Map", "Disconnected");
                }
                return false;
            }
        });
        /*
        db = new DBHelper(getApplicationContext());
        c=db.getOrders();

        int nameIndex = c.getColumnIndex("Name");
        int lngIndex = c.getColumnIndex("ClientLAT");
        int latIndex = c.getColumnIndex("ClientLNG");

        Log.d("MAP Test BEFOR", name + lat + lng);

        name = c.getString(nameIndex);
        lat = c.getDouble(latIndex);
        lng = c.getDouble(lngIndex);

        Log.d("MAP Test AFTER", name +"   "+ lat +"   "+ lng);
        */
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //test custom marker

        db = new DBHelper(getApplicationContext());

        int driveridIndex = db.getDriverID().getColumnIndex("Driver_ID");
        DriverID = db.getDriverID().getString(driveridIndex);

        //FireBase
        final DatabaseReference firebaseRef_Driver =  FirebaseDatabase.getInstance().getReference().child("Orders").child(DriverID);
        Log.d("After DB REF", "  ");
        //DatabaseReference mDataBaseRef= FirebaseDatabase.getInstance().getReference();
        //        mDataBaseRef.child("Test").removeValue(); // to remove a value
        clients_hashmap = new HashMap<>();
        firebaseRef_Driver.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {


                Log.d("Snapshot", dataSnapshot.toString());
                clients_hashmap = (Map<String, Map<String, String>>) dataSnapshot.getValue();
                if (clients_hashmap != null) {
                    Set<String> keys = clients_hashmap.keySet();
                    for (String i : keys) {
                        Log.d("INSIDE", i);
                        //TODO need to sync better

                        String clientid = i;
                        String clientname = clients_hashmap.get(i).get("NAME");
                        String clientaddress = clients_hashmap.get(i).get("ADDRESS");
                        String clientphone = clients_hashmap.get(i).get("PHONE");
                        String clientlat = clients_hashmap.get(i).get("LAT");
                        String clientlng = clients_hashmap.get(i).get("LNG");

                        Log.d("client lat,lng", clientlat + "," + clientlng);
                        //db.emptyOrder(); // clear the database drivers befor updaiting new ones
                        String deliver = clients_hashmap.get(i).get("DELIVER");
                        String repair = clients_hashmap.get(i).get("REPAIR");
                        String service = "3";
                        if (deliver.equals("1") && repair.equals("0")) {
                            service = "0";
                        }
                        if (deliver.equals("0") && repair.equals("1")) {
                            service = "1";
                        }
                        if (deliver.equals("1") && repair.equals("1")) {
                            service = "2";
                        }

                        //db.insertOrder(clientid, clientname, clientphone, clientaddress, clientlat, clientlng, service, "Pending");

                        //c = db.getOrders();
                        // BitmapDescriptor gasicon = BitmapDescriptorFactory.fromResource(R.drawable.mapmarkergasicon);
                        // BitmapDescriptor fixicon = BitmapDescriptorFactory.fromResource(R.drawable.mapmarkerfixicon);
                        // BitmapDescriptor icon ;
                        try {
                            lat = Double.parseDouble(clientlat);
                            lng = Double.parseDouble(clientlng);
                        } catch (NumberFormatException e) {
                            // p did not contain a valid double
                            Log.d("worng lat/lng cords"," ");
                        }
                        LatLng currentLocation_tmp1 = new LatLng(lat, lng);
                        Log.d("the lat,lng"," "+currentLocation_tmp1);
                        BitmapDescriptor gasicon = BitmapDescriptorFactory.fromResource(R.drawable.mapmarkergasicon);
                        BitmapDescriptor fixicon = BitmapDescriptorFactory.fromResource(R.drawable.mapmarkerfixicon);
                        if(service.equals("0"))
                            mMap.addMarker(new MarkerOptions().position(currentLocation_tmp1).title(clientname).icon(gasicon));

                        else
                            mMap.addMarker(new MarkerOptions().position(currentLocation_tmp1).title(clientname).icon(fixicon));

                        Log.e("JeepGas.Service", "   " + i);

                    }
                } else {

                    // toast ------> to tell the driver there is no orders:

                    Context context = getApplicationContext();
                    CharSequence text = "You Have No Orders";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, text, duration).show();

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Firebase Error", databaseError.toString());
            }
        });


        /*
        c=db.getOrders();

        int nameIndex = c.getColumnIndex("Name");
        int lngIndex = c.getColumnIndex("ClientLat");
        int latIndex = c.getColumnIndex("ClientLng");

        name = c.getString(nameIndex);
        lat = c.getDouble(latIndex);
        lng = c.getDouble(lngIndex);

        Log.d(" no idea testing", name + lat + lng);
        */
        /**
         *MOCK DATA
         *TODO: Remove mockdata and make clients orders locations dynamic
         **/
        BitmapDescriptor gasicon = BitmapDescriptorFactory.fromResource(R.drawable.mapmarkergasicon);
        BitmapDescriptor fixicon = BitmapDescriptorFactory.fromResource(R.drawable.mapmarkerfixicon);

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

    public void startUpdate(View v) {
        if(!googleApiClient.isConnected()) {

            googleApiClient.connect();
            Log.d("Connect Map", "Connected");
        }
    }


    //  Disconnect apiClient and close cursor

    protected void onResume() {

        super.onResume();

        //Cursor c = db.getOrders();
       // orderCustomAdapter.changeCursor(c);

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


