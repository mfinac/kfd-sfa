package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.FreeItem;

import java.util.ArrayList;

public class FreeItemController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "FreeItemController";

    public FreeItemController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateFreeItem(ArrayList<FreeItem> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_FREEITEM+ " (RefNo,ItemCode) " + " VALUES (?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (FreeItem freeItem : list) {

                stmt.bindString(1, freeItem.getFFREEITEM_REFNO());
                stmt.bindString(2, freeItem.getFFREEITEM_ITEMCODE());

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
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_FREEITEM, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_FREEITEM, null, null);
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

    public ArrayList<FreeItem> getFreeItemssByRefno(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeItem> list = new ArrayList<FreeItem>();

        String selectQuery = "select * from TblFreeItem where refno='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            FreeItem item = new FreeItem();

            item.setFFREEITEM_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
            item.setFFREEITEM_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            item.setFFREEITEM_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));

            list.add(item);

        }

        return list;
    }
    public ArrayList<String> getFreeItemsByRefno(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String> list = new ArrayList<String>();

        String selectQuery = "select * from TblFreeItem where refno='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            String item = cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE));


            list.add(item);

        }

        return list;
    }
}
