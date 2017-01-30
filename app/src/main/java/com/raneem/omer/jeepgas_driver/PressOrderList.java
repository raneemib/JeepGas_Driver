package com.raneem.omer.jeepgas_driver;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PressOrderList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DBHelper db;
    private ListView lv;
    private OrderCustomAdapter orderCustomAdapter;
    private Map<String, Map<String, String>> clients_hashmap;
    private Cursor c;
    private String DriverID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_order_list);

        lv = (ListView) findViewById(R.id.ListView);
        db = new DBHelper(getApplicationContext());

        c = db.getOrders();
        orderCustomAdapter = new OrderCustomAdapter(getApplicationContext(), c);
        lv.setAdapter(orderCustomAdapter);
        lv.setOnItemClickListener(this);

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


                Log.d("Snapshot", dataSnapshot.toString());
                clients_hashmap = (Map<String, Map<String, String>>) dataSnapshot.getValue();
                //TODO need to add null test
                Set<String> keys = clients_hashmap.keySet();
                for (String i : keys) {
                    Log.d("INSIDE", i);


                    String clientid = i;
                    String clientname = clients_hashmap.get(i).get("NAME");
                    String clientaddress = clients_hashmap.get(i).get("ADDRESS");
                    String clientphone = clients_hashmap.get(i).get("PHONE");
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

                    db.insertOrder(clientid, clientname, clientphone, clientaddress, service, "Approved");

                    c = db.getOrders();
                    orderCustomAdapter.changeCursor(c);

                    Log.e("JeepGas.Service", "   " + i);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Firebase Error", databaseError.toString());
            }
        });


    }

    public void onResume() {
        super.onResume();
        Cursor c = db.getOrders();
        orderCustomAdapter.changeCursor(c);
        }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getApplicationContext(), OrderDetails.class);
        Log.d("position", position + "");
        Log.d("id", id + "");
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
