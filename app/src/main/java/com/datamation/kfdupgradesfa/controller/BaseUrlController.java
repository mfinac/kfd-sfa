package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.BaseURL;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.Payment;
import com.google.android.gms.fitness.data.Value;

import java.util.ArrayList;

public class BaseUrlController {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "BaseUrlController";
    private static SharedPref pref;

    public BaseUrlController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int CreateOrUpdateBaseUrl(ArrayList<BaseURL> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
//        BaseURL baseURL = new BaseURL();

        try {

            for (BaseURL baseURL : list) {

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_CONNECTION;
                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();


                values.put(ValueHolder.CONN_BASE_URL, baseURL.getBASE_URL_URL());
                values.put(ValueHolder.CONN_NAME, baseURL.getBASE_URL_NAME());
                values.put(ValueHolder.CONN_STATUS, baseURL.getBASE_URL_STATUS());

                int cn = cursor.getCount();
                if (cn > 0) {
                    //count = dB.update(ValueHolder.TABLE_CONNECTION,);
                } else {
                    count = (int) dB.insert(ValueHolder.TABLE_CONNECTION, null, values);
                }
                if (count <= 0) {
                    cn = 0;
                }
            }

        }
        catch (Exception e)
        {
            Log.v(TAG + " Exception", e.toString());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
            dB.close();
        }
        count++;
        return count;
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
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_CONNECTION, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_CONNECTION, null, null);
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

    public String getActiveURL(Context context){
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_CONNECTION ;
        Cursor cursor = dB.rawQuery(selectQuery, null);

        while(cursor.moveToNext()){
            return cursor.getString(cursor.getColumnIndex(ValueHolder.CONN_BASE_URL));
        }

        pref = SharedPref.getInstance(context);
        String domain = pref.getBaseURL();

        return domain;

    }

    public String getActiveConnectionName(Context context){
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_CONNECTION ;
        Cursor cursor = dB.rawQuery(selectQuery, null);

        while(cursor.moveToNext()){
            return cursor.getString(cursor.getColumnIndex(ValueHolder.CONN_NAME));
        }

        return "";

    }

}
