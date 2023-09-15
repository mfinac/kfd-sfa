package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Tax;

import java.util.ArrayList;

public class TaxController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbeHelper;
    private String TAG = "TaxController";

    public TaxController(Context context) {
        this.context = context;
        dbeHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbeHelper.getWritableDatabase();
    }

    public int createOrUpdateTaxHed(ArrayList<Tax> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (Tax tax : list) {

                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_TAX + " WHERE " + ValueHolder.TAXCODE + " = '" + tax.getTAXCODE() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                values.put(ValueHolder.TAXCODE, tax.getTAXCODE());
                values.put(ValueHolder.TAXNAME, tax.getTAXNAME());
                values.put(ValueHolder.TAXPER, tax.getTAXPER());
                values.put(ValueHolder.TAXREGNO, tax.getTAXREGNO());

                int cn = cursor.getCount();
                if (cn > 0)
                    count = dB.update(ValueHolder.TABLE_TAX, values, ValueHolder.TAXCODE + " =?", new String[]{String.valueOf(tax.getTAXCODE())});
                else
                    count = (int) dB.insert(ValueHolder.TABLE_TAX, null, values);

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


    public String getTaxRGNo(String Taxcode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_TAX + " WHERE " + ValueHolder.TAXCODE + "='" + Taxcode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.TAXREGNO));

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            //if (cursor != null) {
            //	cursor.close();
            //}
            //dB.close();
        }
        return "";
    }


}
