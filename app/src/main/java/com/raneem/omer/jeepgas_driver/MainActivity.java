package com.raneem.omer.jeepgas_driver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService();
    }

    public void startService() {
        startService(new Intent(getBaseContext(), OrderService.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), OrderService.class));
    }

    public void gotoMaps(View v) {

            Log.e("gotoMaps:", "Clicked");
            // Navigate directly to maps activity
            Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(mapIntent);
        }

    public void ClickOrderList(View v) {


        Intent orderlistclicked = new Intent(this, PressOrderList.class);
        startActivity(orderlistclicked);
    }

    public void ClickUpdateAccount(View v) {


        Intent updateaccountclicked = new Intent(this, PressUpdateAccount.class);
        startActivity(updateaccountclicked);
    }


    public void onDestroy() {

        super.onDestroy();
        stopService();

    }

    }

