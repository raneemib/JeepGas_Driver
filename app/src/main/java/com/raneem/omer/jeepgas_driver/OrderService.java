package com.raneem.omer.jeepgas_driver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OrderService extends Service {


    private DBHelper db;
    private OrderCustomAdapter orderCustomAdapter;
    private Map<String, Map<String, String>> clients_hashmap;
    private Cursor c;
    private String DriverID;
    private DataSnapshot LastSnapshot;
    private int oldcount;
    private int newcount;
    private int reset=0;
    private boolean first = false;

    public OrderService() {
        Log.d("testing", "OrderService");

    }


    /** indicates how to behave if the service is killed */
    int mStartMode;

    /** interface for clients that bind */
    IBinder mBinder;

    /** indicates whether onRebind should be used */
    boolean mAllowRebind;

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        Log.d("testing", "onCreate");

        db = new DBHelper(getApplicationContext());
        newcount =0;
        c = db.getOrders();
        orderCustomAdapter = new OrderCustomAdapter(getApplicationContext(), c);

        int driveridIndex = db.getDriverID().getColumnIndex("Driver_ID");
        DriverID = db.getDriverID().getString(driveridIndex);

        Log.d("Befor DB REF", DriverID);

        //FireBase
        final DatabaseReference firebaseRef_Driver =  FirebaseDatabase.getInstance().getReference().child("Orders").child(DriverID);
        Log.d("After DB REF", "  ");
        //DatabaseReference mDataBaseRef= FirebaseDatabase.getInstance().getReference();
        //        mDataBaseRef.child("Test").removeValue(); // to remove a value
        clients_hashmap = new HashMap<>();
        firebaseRef_Driver.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                db.emptyOrder();

                Log.d("Snapshot", dataSnapshot.toString());
                //LastSnapshot = dataSnapshot;
                clients_hashmap = (Map<String, Map<String, String>>) dataSnapshot.getValue();
                if(clients_hashmap!=null) {
                    newcount = (clients_hashmap.size());
                    Log.d("newcount is ", String.valueOf(newcount));
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
                        String clientstatus = clients_hashmap.get(i).get("STATUS");
                        //db.emptyOrder(); // clear the database drivers befor updaiting new ones
                        String deliver = clients_hashmap.get(i).get("DELIVER");
                        String repair = clients_hashmap.get(i).get("REPAIR");
                        String service = "3";
                        if (deliver.equals("1") && repair.equals("0") ){
                            service = "0";
                        }
                        if (deliver.equals("0") && repair.equals("1")) {
                            service = "1";
                        }
                        if (deliver.equals("1") && repair.equals("1")) {
                            service = "2";
                        }

                        Log.d("Service = lat , lng ", clientlat +"  "+ clientlng);
                        //clientid = clientid.replace("-","");
                        //db.insertOrder(clientid, clientname, clientphone, clientaddress, clientlat, clientlng, service, clientstatus);

                        Log.e("JeepGas.Service", "   " + i);
                        String temp="";
                        if (service.equals("0"))
                            temp="Gas";
                        if (service.equals("1"))
                            temp="Repair";
                        if (service.equals("2"))
                            temp="Gas & Repair";

                        String clienttime = clients_hashmap.get(i).get("TIME");

                        //Log.d("checking result ", String.valueOf((db.insertOrder(clientid, clientname, clientphone, clientaddress, clientlat, clientlng, service, clientstatus))));
                        db.insertOrder(clientid, clientname, clientphone, clientaddress, clientlat, clientlng, service, clientstatus, clienttime);

                        c = db.getOrders();
                        orderCustomAdapter.changeCursor(c);



                    }
                }else{
                    oldcount = newcount - reset;
                    newcount = 0;
                    reset=0;
                    db.emptyOrder();
                    c = db.getOrders();
                    orderCustomAdapter.changeCursor(c);

                }

                if(first) {
                    oldcount = oldcount - reset;
                    Log.d("newcount // oldcount ", String.valueOf(newcount) + "   " + String.valueOf(oldcount) + "  Reset is : " + String.valueOf(reset));
                    if (oldcount < newcount) {
                        SendNotificaiton();
                    } else if (oldcount > newcount) {
                        SendCancelNotificaiton();
                    } else {
                        Log.d("NOTHING HAPPEN", "damit");

                    }

                    oldcount = newcount - reset;
                    reset = 0;
                }
                first = true;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Firebase Error", databaseError.toString());
            }
        });

    }

    public void Countreset(){
        Log.d("count reset Active"," Now");
        reset=1;
    }

    private void SendNotificaiton(){
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        Notification mBuilder = new Notification.Builder(this)

                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setContentTitle("JeebGas-Drivers")
                .setContentText("A New Order Has Arrived")
                .build();

        mBuilder.flags |= Notification.FLAG_AUTO_CANCEL;
        mBuilder.defaults |= Notification.DEFAULT_SOUND;
        mNotificationManager.notify(1, mBuilder);
    }

    private void SendCancelNotificaiton(){
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        Notification mBuilder = new Notification.Builder(this)

                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setContentTitle("JeebGas-Drivers")
                .setContentText("An Order Has Been Canceled")
                .build();

        mBuilder.flags |= Notification.FLAG_AUTO_CANCEL;
        mBuilder.defaults |= Notification.DEFAULT_SOUND;
        mNotificationManager.notify(1, mBuilder);
    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("testing", "onStartCommand");
        // Let it continue running until it is stopped.
        return START_STICKY;
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {


    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*@Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
}
