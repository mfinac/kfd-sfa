package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Cost;
import com.datamation.kfdupgradesfa.model.Route;
import com.datamation.kfdupgradesfa.model.Supplier;

import java.util.ArrayList;

/*
    create by kaveesha 23-11-2021
 */

public class SupplierController {

    Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase dB;
    private String TAG = "SupplierController";

    public SupplierController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void CreateOrUpdateSupplier(ArrayList<Supplier> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_SUPPLIER +
                    " (SupCode,SupName) " + " VALUES (?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (Supplier supplier : list) {
                stmt.bindString(1, supplier.getFSUP_CODE());
                stmt.bindString(2, supplier.getFSUP_NAME());

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

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_SUPPLIER, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_SUPPLIER, null, null);
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

    //----------------------------getAllSupplier---------------------------------------
    public ArrayList<Supplier> getAllSuppliers() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Supplier> list = new ArrayList<Supplier>();

        String selectQuery = "select * from " + ValueHolder.TABLE_SUPPLIER ;
        Cursor cursor = null;
        try {
            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Supplier splr = new Supplier();
                splr.setFSUP_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.SUP_CODE)));
                splr.setFSUP_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.SUP_NAME)));

                list.add(splr);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
