package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.OrdFreeIssue;
import com.datamation.kfdupgradesfa.model.OrderFreeIss;

import java.util.ArrayList;

public class OrdFreeIssueController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "OrdFreeIssueController";

    public OrdFreeIssueController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void UpdateOrderFreeIssue(OrdFreeIssue ordFreeIssue) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        int count = 0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDFREEISS + " WHERE " + ValueHolder.REFNO
                    + " = '" + ordFreeIssue.getOrdFreeIssue_RefNo() + "' and " + ValueHolder.REFNO1
                    + " = '" + ordFreeIssue.getOrdFreeIssue_RefNo1() + "' and " + ValueHolder.ITEMCODE
                    + " = '" + ordFreeIssue.getOrdFreeIssue_ItemCode() + "' ";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(ValueHolder.REFNO, ordFreeIssue.getOrdFreeIssue_RefNo());
            values.put(ValueHolder.TXNDATE, ordFreeIssue.getOrdFreeIssue_TxnDate());
            values.put(ValueHolder.REFNO1, ordFreeIssue.getOrdFreeIssue_RefNo1());
            values.put(ValueHolder.ITEMCODE, ordFreeIssue.getOrdFreeIssue_ItemCode());
            values.put(ValueHolder.QTY, ordFreeIssue.getOrdFreeIssue_Qty());

            count = cursor.getCount();

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_ORDFREEISS, values, ValueHolder.ITEMCODE + " = '" + ordFreeIssue.getOrdFreeIssue_ItemCode() + "' and " + ValueHolder.REFNO1 + " = '" + ordFreeIssue.getOrdFreeIssue_RefNo1() + "' and " + ValueHolder.REFNO + " = '" + ordFreeIssue.getOrdFreeIssue_RefNo() + "'", null);
            } else {
                dB.insert(ValueHolder.TABLE_ORDFREEISS, null, values);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void ClearFreeIssues(String RefNo) {

        int count;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDFREEISS + " WHERE " + ValueHolder.REFNO + " = '" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                dB.delete(ValueHolder.TABLE_ORDFREEISS, ValueHolder.REFNO + " = '" + RefNo + "'", null);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

    }

    public void ClearFreeIssuesForPreSale(String RefNo) {

        int count;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDFREEISS + " WHERE " + ValueHolder.REFNO1 + " = '" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                dB.delete(ValueHolder.TABLE_ORDFREEISS, ValueHolder.REFNO1 + " = '" + RefNo + "'", null);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

    }

    public void RemoveFreeIssue(String RefNo, String itemCode) {

        int count;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDFREEISS + " WHERE " + ValueHolder.REFNO + " = '" + RefNo + "' AND itemcode='" + itemCode + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                dB.delete(ValueHolder.TABLE_ORDFREEISS, ValueHolder.REFNO + " = '" + RefNo + "' AND itemcode='" + itemCode + "'", null);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

    }

    public ArrayList<OrderFreeIss> getAllFreeIssuesNew(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderFreeIss> list = new ArrayList<OrderFreeIss>();

        try {
            Cursor cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDFREEISS + " WHERE RefNo1='" + Refno + "'", null);

            while (cursor.moveToNext()) {

                OrderFreeIss freeIssue = new OrderFreeIss();

                freeIssue.setItemcode(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                freeIssue.setQty(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY))));
                freeIssue.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO1)));
                freeIssue.setRefNo1(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                freeIssue.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                list.add(freeIssue);
            }
            cursor.close();

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<OrdFreeIssue> getAllFreeIssues(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrdFreeIssue> list = new ArrayList<OrdFreeIssue>();

        try {
            Cursor cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ORDFREEISS + " WHERE RefNo1='" + Refno + "'", null);

            while (cursor.moveToNext()) {

                OrdFreeIssue freeIssue = new OrdFreeIssue();

                freeIssue.setOrdFreeIssue_ItemCode(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                freeIssue.setOrdFreeIssue_Qty(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                freeIssue.setOrdFreeIssue_RefNo1(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                freeIssue.setOrdFreeIssue_RefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO1)));
                freeIssue.setOrdFreeIssue_TxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                list.add(freeIssue);
            }
            cursor.close();

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;
    }

}
