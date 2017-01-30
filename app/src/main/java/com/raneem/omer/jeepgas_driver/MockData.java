package com.raneem.omer.jeepgas_driver;

import android.content.Context;

/**
 * Created by Omer on 1/13/2017.
 * This Class Contains All The MetaData That Will Be Used For Testing
 * Untill The Data Become Dynamic From The SERVER!!!
 */
public class MockData {

    private DBHelper db;
    private Context context;

    public MockData(Context context) {
        this.context = context;
        db = new DBHelper(context);
    }

    public void populateMockData() {
        db.emptyOrder();
        db.insertOrder("-325j3igftj3i4tg4ffergt", "Steven", "9724569987", "Jerusalem", "Deliver", "Pending");
        db.insertOrder("REG345t34weg4343g", "Omer", "972543459456", "Jerusalem", "Repair", "Pending");
        db.insertOrder("-L435tfgdew325", "Raneem", "972526595612", "Jerusalem", "Deliver", "Pending");
    }
}
