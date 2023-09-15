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
import com.datamation.kfdupgradesfa.model.ItemLoc;
import com.datamation.kfdupgradesfa.model.OrderDetail;

import java.util.ArrayList;

public class ItemLocController
{
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "ItemLocController";

    public ItemLocController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void InsertOrReplaceItemLoc(ArrayList<ItemLoc> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_ITEMLOC + " (ItemCode,LocCode,QOH) VALUES (?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (ItemLoc itemLoc : list) {

                stmt.bindString(1, itemLoc.getFITEMLOC_ITEM_CODE());
                stmt.bindString(2, itemLoc.getFITEMLOC_LOC_CODE());
                stmt.bindString(3, itemLoc.getFITEMLOC_QOH());

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
    public String getProductQOH(String code, String loccode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;

        try {
            //String selectQuery = "SELECT SUM(QOH) AS 'QOH' FROM " + dbHelper.TABLE_FITEMLOC + " WHERE " + dbHelper.FITEMLOC_ITEM_CODE + "='" + code + "' GROUP BY "+ dbHelper.FITEMLOC_ITEM_CODE;
            String selectQuery = "SELECT SUM(QOH) AS 'QOH' FROM " + ValueHolder.TABLE_ITEMLOC + " WHERE " + ValueHolder.ITEMCODE + "='" + code + "' AND "+ ValueHolder.LOCCODE + "='"+loccode+"' GROUP BY "+ ValueHolder.ITEMCODE;


            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex("QOH"));

            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return "0";

    }
    public int deleteAllItemLoc() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ITEMLOC, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_ITEMLOC, null, null);
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

//use order as sale in swadeshi..
    public void UpdateOrderQOH(String RefNo, String Task, String locCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            //ArrayList<InvDet> list = new InvDetController(context).getAllItemsforPrint(RefNo);
            ArrayList<OrderDetail> list = new OrderDetailController(context).getAllUnSync(RefNo);//2019-11-18 rashmi

            for (OrderDetail item : list) {

                double qoh = 0;

                Cursor cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ITEMLOC + " WHERE " +ValueHolder.ITEMCODE + "='" + item.getFORDDET_ITEM_CODE() + "' AND " + ValueHolder.LOCCODE + "='" + locCode + "'", null);
                double Qty = Double.parseDouble(item.getFORDDET_QTY());


                if (cursor.getCount() > 0) {

                    while (cursor.moveToNext()) {
                        qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                    }

                    ContentValues values = new ContentValues();

                    if (Task.equals("+")) {
                        values.put(ValueHolder.QOH, String.valueOf(qoh + Qty));
                    } else {
                        values.put(ValueHolder.QOH, String.valueOf(qoh - Qty));
                    }


                    dB.update(ValueHolder.TABLE_ITEMLOC, values, ValueHolder.ITEMCODE + "=? AND " + ValueHolder.LOCCODE + "=?", new String[]{item.getFORDDET_ITEM_CODE(), locCode});

                }

                cursor.close();

            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }

}
