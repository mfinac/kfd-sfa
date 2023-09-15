package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Cost;
import com.datamation.kfdupgradesfa.model.DayNPrdHed;
import com.datamation.kfdupgradesfa.model.RepGPS;
import com.datamation.kfdupgradesfa.model.RepGpsLoc;

import java.util.ArrayList;

/*
    create by MMS 28-01-2022
 */

public class RepGPSLocationController {

    Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase dB;
    private String TAG = "RepGPSLocationController";

    public static final String TABLE_FDAILY_REPGPS = "TblDailyRepGPS";

    public RepGPSLocationController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }


    public static final String CREATE_FDAILY_REPGPS_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FDAILY_REPGPS + " ( " + ValueHolder.REPCODE + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " + ValueHolder.TXNTIME + " TEXT, " + ValueHolder.LATITUDE + " TEXT, " + ValueHolder.LONGITUDE + " TEXT, "+ValueHolder.IS_SYNC +" TEXT,"+ValueHolder.FBATTERY_ST +" TEXT) ";

    public void AddGPSLocation(RepGPS rgps) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT INTO " + TABLE_FDAILY_REPGPS +
                    " (RepCode,TxnDate,TxnTime,Latitude,Longitude,IsSync,baterry_lvl) " + " VALUES (?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);


            stmt.bindString(1, rgps.getFREP_CODE());
            stmt.bindString(2, rgps.getFTXN_DATE());
            stmt.bindString(3, rgps.getFTXN_TIME());
            stmt.bindString(4, rgps.getFLATITUDE());
            stmt.bindString(5, rgps.getFLONGITUDE());
            stmt.bindString(6, rgps.getFIS_SYNC());
            stmt.bindString(7, rgps.getBATTERY_LVL().toString());

            stmt.execute();
            stmt.clearBindings();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dB.setTransactionSuccessful();
            dB.endTransaction();
            dB.close();
        }

    }


    public ArrayList<RepGpsLoc> getUnSyncedGPSLocData() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<RepGpsLoc> list = new ArrayList<RepGpsLoc>();

        try {

            String selectQuery = "SELECT * FROM " + TABLE_FDAILY_REPGPS + " WHERE " + ValueHolder.IS_SYNC + "='0'";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                RepGpsLoc rGps = new RepGpsLoc();

                rGps.setRepCode(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                rGps.setGpsdate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                rGps.setLongitude(cursor.getDouble(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
                rGps.setLatitude(cursor.getDouble(cursor.getColumnIndex(ValueHolder.LATITUDE)));
                rGps.setBattper(cursor.getInt(cursor.getColumnIndex(ValueHolder.FBATTERY_ST)));
                rGps.setTxnTime(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTIME)));

                list.add(rGps);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;

    }

    public int updateIsSyncedRepGPSLoc(String txnDte,String txntime, String res) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();

            values.put(ValueHolder.IS_SYNC, res);

            count = dB.update(TABLE_FDAILY_REPGPS, values, ValueHolder.TXNDATE + " =? AND " + ValueHolder.TXNTIME + " =?", new String[]{txnDte,txntime});

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }

}
