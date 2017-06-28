package com.raneem.omer.jeepgas_driver;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PressUpdateAccount extends AppCompatActivity {

    EditText drivernametf;
    EditText phonetf;
    EditText workingareatf;
    EditText workinghoursf;
    EditText workinghourst;
    EditText gaspriceBig;
    EditText gaspriceSmall;
    Spinner spinner;

    Button savebt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_update_account);

        drivernametf = (EditText) findViewById(R.id.drivernametf);
        phonetf = (EditText) findViewById(R.id.phonetf);
        workingareatf = (EditText) findViewById(R.id.workingareatf);
        workinghoursf = (EditText) findViewById(R.id.workinghoursf);
        workinghourst = (EditText) findViewById(R.id.workinghourst);
        gaspriceBig = (EditText) findViewById(R.id.gaspriceBig);
        gaspriceSmall = (EditText) findViewById(R.id.gaspriceSmall);
        spinner = (Spinner) findViewById(R.id.spinner);


        savebt = (Button) findViewById(R.id.savebt);


//        JeebGasDrivers jeebGasDriver = new JeebGasDrivers(getApplicationContext());
        DBHelper db = new DBHelper(getApplicationContext());
        Cursor driverCursor = db.getDriver();



        if (driverCursor.moveToFirst()) {
            String name = driverCursor.getString(driverCursor.getColumnIndex("companyname"));
            String phone = driverCursor.getString(driverCursor.getColumnIndex("phone"));
            String area = driverCursor.getString(driverCursor.getColumnIndex("workingarea"));
            String hours_from = driverCursor.getString(driverCursor.getColumnIndex("workinghoursFrom"));
            String hours_till = driverCursor.getString(driverCursor.getColumnIndex("workinghoursTill"));
            int deliver = driverCursor.getInt(driverCursor.getColumnIndex("deliver"));
            int repair = driverCursor.getInt(driverCursor.getColumnIndex("repair"));
            int gasbig = driverCursor.getInt(driverCursor.getColumnIndex("gasbig"));
            int gassmall = driverCursor.getInt(driverCursor.getColumnIndex("gassmall"));

            drivernametf.setText(name);
            phonetf.setText(phone);
            workingareatf.setText(area);
            workinghoursf.setText(hours_from);
            workinghourst.setText(hours_till);
            gaspriceBig.setText(gasbig + "");
            gaspriceSmall.setText(gassmall + "");


            if (deliver == 1 && repair != 1) {
                spinner.setSelection(0);
            } else if (deliver != 1 && repair == 1) {
                spinner.setSelection(1);
            } else if (deliver == 1 && repair == 1) {
                spinner.setSelection(2);
            }

        }
    }



    // "Save button" Functionality
    public void ClickSave(View v) {

        SaveAccInfo();


        // toast ------> to save or update your info:

        Context context = getApplicationContext();
        CharSequence text = "Your Info Have Been Saved";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration) .show();





        Intent saveclicked = new Intent(this, MainActivity.class);
        startActivity(saveclicked);
        finish();


    }
    private void SaveAccInfo(){
        DBHelper db = new DBHelper(this);


        String name = drivernametf.getText().toString();
        String phone = phonetf.getText().toString();
        String area = workingareatf.getText().toString();
        String hours_from = workinghoursf.getText().toString();
        String hours_till = workinghourst.getText().toString();
        String price_big = gaspriceBig.getText().toString();
        String price_small = gaspriceSmall.getText().toString();
        String service = spinner.getSelectedItemPosition() + "";

        db.insertDriver(name, phone, area, hours_from, hours_till, price_big, price_small, service);
    }

}
