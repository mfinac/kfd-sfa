package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.RepDebtor;

import java.util.ArrayList;

/*
 create by kaveesha - 24/11/2021
 */

public class RepDebtorController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "RepDebtorController";

    public RepDebtorController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    public void CreateOrUpdateRepDebtor(ArrayList<RepDebtor> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_REPDEBTOR +
                    " (RepCode,DebCode) " + " VALUES (?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (RepDebtor repDebtor : list) {

                stmt.bindString(1, repDebtor.getFREPCODE());
                stmt.bindString(2, repDebtor.getFDEBCODE());

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

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_REPDEBTOR, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_REPDEBTOR, null, null);
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


    public boolean getCheckAllowDebtor(String debcode,String repcode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_REPDEBTOR + " WHERE " + ValueHolder.DEBCODE + "='" + debcode + "' and "+ ValueHolder.REPCODE + "='" + repcode + "'GROUP BY DebCode ";


            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                String AllowChange = cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE));

                if(AllowChange.length()>0){
                    return true;
                }else{
                    return false;
                }


            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return false;

    }
}
