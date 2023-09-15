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

import java.util.ArrayList;

public class RouteController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "RouteController";

    public RouteController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateFRoute(ArrayList<Route> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_ROUTE +
                    " (RepCode," +
                    "RouteCode," +
                    "RouteName," +
                    "AreaCode," +
                    "DealCode," +
                    "FreqNo," +
                    "Km," +
                    "MinProcall," +
                    "Remarks," +
                    "Status," +
                    "RDAloRate) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (Route route : list) {

                stmt.bindString(1, route.getFROUTE_REPCODE());
                stmt.bindString(2, route.getFROUTE_ROUTECODE());
                stmt.bindString(3, route.getFROUTE_ROUTE_NAME());
                stmt.bindString(4, route.getFROUTE_AREACODE());
                stmt.bindString(5, route.getFROUTE_DEALCODE());
                stmt.bindString(6, route.getFROUTE_FREQNO());
                stmt.bindString(7, route.getFROUTE_KM());
                stmt.bindString(8, route.getFROUTE_MINPROCALL());
                stmt.bindString(9, route.getFROUTE_REMARKS());
                stmt.bindString(10, route.getFROUTE_STATUS());
                stmt.bindString(11, route.getFROUTE_RDALORATE());

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

    /*
     * delete code
     */
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

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ROUTE, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_ROUTE, null, null);
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


    @SuppressWarnings("static-access")
    public String getRouteNameByCode(String code) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ROUTE + " WHERE " + ValueHolder.ROUTECODE + "='" + code + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        String routeNm = "";
        while (cursor.moveToNext()) {

            routeNm = cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_NAME));

        }

        return routeNm;
    }

    //----------------------------getAllRoute---------------------------------------
    public ArrayList<Route> getRoute() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Route> list = new ArrayList<Route>();

        String selectQuery = "select * from TblRoute";
        Cursor cursor = null;
        try {
            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Route route = new Route();
                route.setFROUTE_ROUTECODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE)));
                route.setFROUTE_ROUTE_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_NAME)));

                list.add(route);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public String getAreaCodeByRouteCode(String routecode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ROUTE +  " WHERE "  + ValueHolder.ROUTE_CODE + "='" + routecode + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(ValueHolder.AREACODE));

        }

        return "";
    }


    //----------------------------getAllRouteCount---------------------------------------
    public int getRouteCount() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        int count = 0;

        String selectQuery = "select * from TblRoute";
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
