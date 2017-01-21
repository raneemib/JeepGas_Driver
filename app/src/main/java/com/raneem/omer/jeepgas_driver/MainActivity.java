package com.raneem.omer.jeepgas_driver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private MockData mockData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mockData = new MockData(getApplicationContext());
        mockData.populateMockData();
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






}

