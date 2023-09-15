package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.SalRep;

import java.util.ArrayList;

/*
 create by kaveesha - 24/11/2021
 */

public class SalRepController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "SalRepController";

    public SalRepController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    public void CreateOrUpdateSalRep(ArrayList<SalRep> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_SALREP +
                    " (RepCode,RepName,CostCode,DealCode,Password,Email,Mobile,RepIdNo,Tele,Prefix,Status,Mac) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (SalRep repDetail : list) {

                stmt.bindString(1, repDetail.getFREP_CODE());
                stmt.bindString(2, repDetail.getFREP_NAME());
                stmt.bindString(3, repDetail.getFREP_COSTCODE());
                stmt.bindString(4, repDetail.getFREP_DEALCODE());
                stmt.bindString(5, repDetail.getFREP_PASSWORD());
                stmt.bindString(6, repDetail.getFREP_EMAIL());
                stmt.bindString(7, repDetail.getFREP_MOB());
                stmt.bindString(8, repDetail.getFREP_REPIDNO());
                stmt.bindString(9, repDetail.getFREP_TELE());
                stmt.bindString(10, repDetail.getFREP_PREFIX());
                stmt.bindString(11, repDetail.getFREP_STATUS());
                stmt.bindString(12, repDetail.getFREP_MACID());

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

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_SALREP, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_SALREP, null, null);
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

    public ArrayList<String> getAllSalesRep() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<String> list = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_SALREP;

        cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                String rep = cursor.getString(1) + "-" + cursor.getString(2);
                list.add(rep);
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return list;
    }

    public String getCurrentRepCode() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT " + ValueHolder.REPCODE + " FROM TblSalRep";
//        String selectQuery = "SELECT " + DatabaseHelper.REPCODE + " FROM " + TABLE_FSALREP;

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE));


            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }


    public SalRep getSaleRepDet(String repCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        SalRep salRep = new SalRep();
        String selectQuery = "SELECT * FROM TblSalRep WHERE "+ValueHolder.REPCODE +" = '"+ repCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                salRep.setFREP_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.REPNAME)));
                salRep.setFREP_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                salRep.setFREP_PREFIX(cursor.getString(cursor.getColumnIndex(ValueHolder.FSALREP_PREFIX)));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return salRep;
    }

    public String getCurrentCostCode() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_SALREP;

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while(cursor.moveToNext()){

            return cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE));

        }

        return "";
    }

    public String getDealCode(){

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_SALREP ;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while(cursor.moveToNext()){

            return cursor.getString(cursor.getColumnIndex(ValueHolder.DEALCODE));

        }

        return "";
    }



    public String getAreaCode(){

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_SALREP ;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while(cursor.moveToNext()){

            return cursor.getString(cursor.getColumnIndex(ValueHolder.AREACODE));

        }

        return "";
    }




}
