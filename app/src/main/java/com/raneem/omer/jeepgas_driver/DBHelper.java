package com.raneem.omer.jeepgas_driver;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    private static final  DatabaseReference mDataBaseRef = FirebaseDatabase.getInstance().getReference();
    private static final String DATABASE_NAME = "Driverdb";
    private static final int DATABASE_VERSION = 2;

    // the driver unquie ID    private static String DriverID = mDataBaseRef.child("Driver").push().getKey();
    private static String DriverID;

    private static final String TABLE_DRIVER = "Driver";
    private static final String DRIVERNAME = "companyname";
    private static final String DRIVERPHONE = "phone";
    private static final String WORKINGAREA = "workingarea";
    private static final String WORKINGHOURS = "workinghours";
    private static final String WORKINGHOURSFROM = "workinghoursFrom";
    private static final String WORKINGHOURSTILL = "workinghoursTill";
    private static final String SERVICETYPE = "servicetype";
    private static final String DELIVER = "deliver";
    private static final String REPAIR = "repair";
    private static final String GASPRICE = "gasprice";
    private static final String GASBIG = "gasbig";
    private static final String GASSMALL= "gassmall";

    private static final String TABLE_ORDER = "_Order";
    private static final String CLIENT_NAME = "Name";
    private static final String CLIENT_PHONE = "Phone";
    private static final String CLIENT_AREA = "Area";
    private static final String CLIENT_SERVICE = "Service";
    private static final String CLIENT_STATUS = "Status";
    private static final String CLIENT_ID = "ClientID";
    private static final String CLIENT_LAT = "ClientLAT";
    private static final String CLIENT_LNG = "ClientLNG";

    private static final String TABLE_DRIVERID = "_DriverID";
    private static final String DRIVER_ID =  "Driver_ID";


    private String CREATE_DRIVER_TABLE = "create table if not exists " + TABLE_DRIVER +
            " (_id integer primary key AUTOINCREMENT, "
            + DRIVERNAME + " text, "
            + DRIVERPHONE + " integer, "
            + WORKINGAREA + " text, "
            + WORKINGHOURSFROM + " text, "
            + WORKINGHOURSTILL + " text, "
            + GASBIG + " integer, "
            + GASSMALL + " integer, "
            + DELIVER + " integer, "
            + REPAIR + " integer)";

    private String CREATE_KEY_TABLE = "create table if not exists " + TABLE_DRIVERID
            + " (_id integer primary key AUTOINCREMENT, "
            + DRIVER_ID + " text)";


    private String CREATE_ORDER_TABLE = "create table if not exists " + TABLE_ORDER +
            " (_id integer primary key AUTOINCREMENT, "
            + CLIENT_ID + " text unique, "
            + CLIENT_NAME + " text, "
            + CLIENT_PHONE + " text, "
            + CLIENT_AREA + " text, "
            + CLIENT_LAT + " text, "
            + CLIENT_LNG + " text, "
            + CLIENT_SERVICE + " text, "
            + CLIENT_STATUS + " text)";





/*    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE, null, 1);
    }*/

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        /**TODO
         * 1- Check if the driver has his own key in the database
         * 2- If the driver doesnt have any key => create a key, insert into database, assign the variable to this key
         * 3- If the driver does have a key => assign the variable to this key
         */

        Cursor c = getDriverID();
        if(c != null && c.getCount() > 0) {
            int driverID_Index = c.getColumnIndex(DRIVER_ID);
            DriverID = c.getString(driverID_Index);
        } else {
            insertDriverID();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DRIVER_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_KEY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_DRIVER + "';");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ORDER + "';");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_DRIVERID + "';");
        db.execSQL(CREATE_DRIVER_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_KEY_TABLE);
    }

    public boolean insertDriverID() {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {
            String DriverID = mDataBaseRef.child("Driver").push().getKey();
            Log.d("DriverID", DriverID);
            contentValues.put(DRIVER_ID, DriverID);
            db.insert(TABLE_DRIVERID, null, contentValues);
            return true;
        } catch (Exception e) {
            Log.e("InsertDriverID", e.toString());
            return false;
        }
    }

    public Cursor getDriverID() {

        String selectQuery = "SELECT * FROM " + TABLE_DRIVERID + " LIMIT 1;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor;
    }

    public boolean insertDriver_bk(JeebGasDrivers jeebgasdriver) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {

            String deleteQuery = "delete from " + TABLE_DRIVER + ";";
            getWritableDatabase().execSQL(deleteQuery);

            contentValues.put(DRIVERNAME, jeebgasdriver.getDrivername());
            contentValues.put(DRIVERPHONE, jeebgasdriver.getDriverphone());
            contentValues.put(WORKINGAREA, jeebgasdriver.getWorkingarea());
            contentValues.put(WORKINGHOURS, jeebgasdriver.getWorkinghours());
            contentValues.put(GASPRICE, jeebgasdriver.getGasprice());
            contentValues.put(SERVICETYPE, jeebgasdriver.getServicetype());
            db.insert(TABLE_DRIVER, null, contentValues);
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public boolean insertDriver(String name, String phone, String area, String hours_from, String hours_till, String price_big, String price_small, String service) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {

            String deleteQuery = "delete from " + TABLE_DRIVER + ";";
            getWritableDatabase().execSQL(deleteQuery);

            /*
             private static final String DRIVERNAME = "companyname";
    private static final String DRIVERPHONE = "phone";
    private static final String WORKINGAREA = "workingarea";
    private static final String WORKINGHOURS = "workinghours";
    private static final String WORKINGHOURSFROM = "workinghoursFrom";
    private static final String WORKINGHOURSTILL = "workinghoursTill";
    private static final String SERVICETYPE = "servicetype";
    private static final String DELIVER = "deliver";
    private static final String REPAIR = "repair";
    private static final String GASPRICE = "gasprice";
    private static final String GASBIG = "gasbig";
    private static final String GASSMALL= "gassmall";
             */

            //save in sqlite
            contentValues.put(DRIVERNAME, name);
            contentValues.put(DRIVERPHONE, phone);
            contentValues.put(WORKINGAREA, area);
            contentValues.put(WORKINGHOURSFROM, hours_from);
            contentValues.put(WORKINGHOURSTILL, hours_till);
            if(service.equals("0")) {
                contentValues.put(DELIVER, 1);
                contentValues.put(REPAIR, 0);
            }
            if(service.equals("1")) {
                contentValues.put(REPAIR, 1);
                contentValues.put(DELIVER, 0);
            }
            if(service.equals("2")) {
                contentValues.put(REPAIR, 1);
                contentValues.put(DELIVER, 1);
            }
            contentValues.put(GASBIG, price_big);
            contentValues.put(GASSMALL, price_small);
            db.insert(TABLE_DRIVER, null, contentValues);



            //Save in firebase
            Map<String, String> FBmap = new HashMap<String, String>();


            FBmap.put("DRIVERNAME",name);
            FBmap.put("DRIVERPHONE",phone);
            FBmap.put("WORKINGAREA",area);
            FBmap.put("WORKINGHOURSFROM",hours_from);
            FBmap.put("WORKINGHOURSTILL",hours_till);

            if(service.equals("0")) {
                FBmap.put("DELIVER","1");
                FBmap.put("REPAIR","0");
            }
            if(service.equals("1")) {
                FBmap.put("DELIVER","0");
                FBmap.put("REPAIR","1");
            }
            if(service.equals("2")) {
                FBmap.put("DELIVER","1");
                FBmap.put("REPAIR","1");
            }

            FBmap.put("GASSMALL",price_small);
            FBmap.put("GASBIG",price_big);


            mDataBaseRef.child("Driver").child(DriverID).setValue(FBmap);



            return true;
        } catch (Exception e) {
            Log.e("InsertDriver", e.toString());
            return false;
        }
    }

    public Cursor getDriver() {
        String selectQuery = "SELECT * FROM " + TABLE_DRIVER + " LIMIT 1;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor;
    }

    public void updateStatus(String status,long id){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("I REACHED UPDATESTATUS"," ");
        /*String strSQL = "UPDATE "+TABLE_ORDER+" SET "+CLIENT_STATUS+" = "+status+" WHERE columnId = "+ id;
        db.execSQL(strSQL);*/
        ContentValues args = new ContentValues();
        args.put(CLIENT_STATUS,status);
        db.update(TABLE_ORDER, args, "_id" + "='" + id
                + "'", null);

        Cursor ordercursor;
        ordercursor = getOrder(id);
        String orderid = ordercursor.getString( ordercursor.getColumnIndex("ClientID") );
        mDataBaseRef.child("Archive").child(DriverID).child(orderid).child("STATUS").setValue(status);
        mDataBaseRef.child("Orders").child(DriverID).child(orderid).child("STATUS").setValue(status);

    }


    //TODO : change insert params to OBJECT type Order
    // Order Queries
    public void emptyOrder() {

        String deleteQuery = "delete from " + TABLE_ORDER + ";";
        getWritableDatabase().execSQL(deleteQuery);
    }


    public void deleteOrder(long id) {
        String deleteQuery = "delete from " + TABLE_ORDER + " WHERE _id = " + id + ";";
        getWritableDatabase().execSQL(deleteQuery);
    }

    public void deleteOrderArchive(long id) {

        //client unquie id
        Cursor ordercursor;
        ordercursor = getOrder(id);
        String orderid = ordercursor.getString( ordercursor.getColumnIndex("ClientID") );

        Log.d("Archive Results ",orderid + DriverID);
        //remove the order that is done from the current driver list
        mDataBaseRef.child("Archive").child(DriverID).child(orderid).child("STATUS").setValue("Done");
        DatabaseReference deletetoarchive = mDataBaseRef.child("Orders").child(DriverID).child(orderid).getRef();
        Log.d("Archive REF ", deletetoarchive.toString());
        deletetoarchive.removeValue();


        String deleteQuery = "delete from " + TABLE_ORDER + " WHERE _id = " + id + ";";
        getWritableDatabase().execSQL(deleteQuery);
    }

    public boolean insertOrder(String clientid, String name, String phone, String area, String lat, String lng, String service, String status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {

            contentValues.put(CLIENT_NAME, name);
            contentValues.put(CLIENT_PHONE, phone);
            contentValues.put(CLIENT_AREA, area);
            contentValues.put(CLIENT_SERVICE, service);
            contentValues.put(CLIENT_STATUS, status);
            contentValues.put(CLIENT_ID, clientid);
            contentValues.put(CLIENT_LAT, lat);
            contentValues.put(CLIENT_LNG, lng);
            db.insert(TABLE_ORDER, null, contentValues);

            return true;
        } catch (Exception e) {
            Log.e("InsertOrder", e.toString());
            return false;
        }
    }

    // Getting a specific order
    public Cursor getOrder(long id) {

        String driversList = "SELECT * FROM " + TABLE_ORDER + " WHERE _id = " + id + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(driversList, null);
        cursor.moveToFirst();
        return cursor;
    }

    // Getting all orders
    public Cursor getOrders() {

        String driversList = "SELECT * FROM " + TABLE_ORDER + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(driversList, null);
        cursor.moveToFirst();
        return cursor;

    }

}



