package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Area;
import com.datamation.kfdupgradesfa.model.Cost;

import java.util.ArrayList;

/*
    create by kaveesha 23-11-2021
 */

public class CostController {

    Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase dB;
    private String TAG = "CostController";

    public CostController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void CreateOrUpdateCost(ArrayList<Cost> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_COST +
                    " (CostCode,CostName) " + " VALUES (?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (Cost cost : list) {
                stmt.bindString(1, cost.getFCOST_CODE());
                stmt.bindString(2, cost.getFCOST_NAME());

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

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_COST, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_COST, null, null);
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

    public ArrayList<Cost> getAllCostCenters() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Cost> list = new ArrayList<Cost>();

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_COST;
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {
                Cost cost = new Cost();
                cost.setFCOST_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
                cost.setFCOST_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTNAME)));
                list.add(cost);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }


}
