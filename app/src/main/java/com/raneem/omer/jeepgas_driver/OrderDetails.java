package com.raneem.omer.jeepgas_driver;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class OrderDetails extends AppCompatActivity {

    private DBHelper db;
    private long id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);
        db = new DBHelper(getApplicationContext());
        if(id >= 0) {

            Cursor cursor = db.getOrder(id);
            TextView tv_companyName1 = (TextView) findViewById(R.id.tv_companyName1);
            TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
            TextView tv_address = (TextView) findViewById(R.id.tv_address);
            TextView tv_type = (TextView) findViewById(R.id.tv_type);
            TextView tv_status = (TextView) findViewById(R.id.tv_status);

            int nameIndex = cursor.getColumnIndex("Name");
            int areaIndex = cursor.getColumnIndex("Area");
            int serviceIndex = cursor.getColumnIndex("Service");
            int phoneIndex = cursor.getColumnIndex("Phone");
            int statusIndex = cursor.getColumnIndex("Status");

            String name = cursor.getString(nameIndex);
            String area = cursor.getString(areaIndex);
            String service = cursor.getString(serviceIndex);
            String phone = cursor.getString(phoneIndex);
            String status = cursor.getString(statusIndex);

            tv_companyName1.setText(name);
            tv_address.setText(area);
            tv_phone.setText(phone);
            tv_status.setText(status);
            if(service.equals("0")){

                service = "Deliver";

            }else if(service.equals("1")){

                service = "Repair";

            }else
                service = "All";

            tv_type.setText(service);
        }

    }

    public void archiveOrder(View v) {

        db.deleteOrder(id);
        Toast.makeText(getApplicationContext(), "Order Archived", Toast.LENGTH_SHORT).show();
        finish();
    }
}
