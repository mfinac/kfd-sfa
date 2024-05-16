package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.BaseURL;
import com.datamation.kfdupgradesfa.model.Order;
import com.google.android.gms.fitness.data.Value;

import java.util.ArrayList;

public class BaseUrlController {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "BaseUrlController";

    public BaseUrlController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateBaseUrl() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        BaseURL baseURL = new BaseURL();

        try {

               String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_CONNECTION ;

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                values.put(ValueHolder.CONNE_ID, baseURL.getCONNE_ID());
                values.put(ValueHolder.CONN_BASE_URL, baseURL.getCONN_BASE_URL());
                values.put(ValueHolder.CONN_NAME, baseURL.getCONN_NAME());
                values.put(ValueHolder.CONN_STATUS, baseURL.getCONN_STATUS());

                int cn = cursor.getCount();
                if (cn > 0) {
                  //  count = dB.update(ValueHolder.TABLE_CONNECTION,null, values);
                } else {
                    count = (int) dB.insert(ValueHolder.TABLE_ORDHED, null, values);
                }
                if(count <= 0){
                    cn = 0;
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

}
