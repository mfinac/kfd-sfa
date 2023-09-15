package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Route;
import com.datamation.kfdupgradesfa.model.RouteDet;

import java.util.ArrayList;

public class RouteDetController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "RouteDetController";

    public RouteDetController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void InsertOrReplaceRouteDet(ArrayList<RouteDet> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_ROUTEDET +
                    " (DebCode," +
                    "RouteCode) " + " VALUES (?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (RouteDet routeDet : list) {

                stmt.bindString(1, routeDet.getFROUTEDET_DEB_CODE());
                stmt.bindString(2, routeDet.getFROUTEDET_ROUTE_CODE());

                stmt.execute();
                stmt.clearBindings();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dB.setTransactionSuccessful();
            dB.endTransaction();
            dB.close();
        }
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    @SuppressWarnings("static-access")
    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ROUTEDET, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_ROUTEDET, null, null);
                Log.v("Success", success + "");
            }
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

    public String getRouteCodeByDebCode(String debCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ROUTEDET +  " WHERE "  + ValueHolder.DEBCODE + "='" + debCode + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE));

        }

        return "";
    }


    //----------------------------getAllRouteDetCount---------------------------------------
    public int getRouteDetCount() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        int count = 0;

        String selectQuery = "select * from " + ValueHolder.TABLE_ROUTEDET ;
        Cursor cursor = null;
        try {
            cursor = dB.rawQuery(selectQuery, null);
            count = cursor.getCount();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}
