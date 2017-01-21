package com.raneem.omer.jeepgas_driver;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class PressOrderList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DBHelper db;
    private ListView lv;
    private OrderCustomAdapter orderCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_order_list);

        lv = (ListView) findViewById(R.id.ListView);
        db = new DBHelper(getApplicationContext());

        Cursor c = db.getOrders();
        orderCustomAdapter = new OrderCustomAdapter(getApplicationContext(), c);
        lv.setAdapter(orderCustomAdapter);
        lv.setOnItemClickListener(this);
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
