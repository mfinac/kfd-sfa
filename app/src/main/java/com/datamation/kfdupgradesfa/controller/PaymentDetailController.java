package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.PayReceiptDet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
   kaveesha - 22-09-2020
 */

public class PaymentDetailController {


    Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase dB;
    private String TAG = "PaymentDetailController";

    public PaymentDetailController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateCusPRecDet(ArrayList<PayReceiptDet> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_CUSP_RECDET +
                    " (AloAmt," +
                    "Amt," +
                    "InvNo," +
                    "InvTxnDate," +
                    "RefNo," +
                    "RepCode," +
                    "TxnDate) " + " VALUES (?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (PayReceiptDet receiptDet : list) {

                stmt.bindString(1, receiptDet.getFPRECDET_ALOAMT());
                stmt.bindString(2, receiptDet.getFPRECDET_INVNO());
                stmt.bindString(4, receiptDet.getFPRECDET_INVTXNDATE());
                stmt.bindString(5, receiptDet.getFPRECDET_REFNO());
                stmt.bindString(6, receiptDet.getFPRECDET_REPCODE());
                stmt.bindString(7, receiptDet.getFPRECDET_TXNDATE());

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
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_CUSP_RECDET, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_CUSP_RECDET, null, null);
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

    public ArrayList<PayReceiptDet> getTodayPayments (){

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        if(dB == null)
        {
            open();
        }else if(!dB.isOpen())
        {
            open();
        }

        Cursor cursor = null;
        ArrayList<PayReceiptDet> list = new ArrayList<PayReceiptDet>();

        try
        {
            String selectQuery = "SELECT RefNo, PayType, TotalAmt,TxnDate FROM " + ValueHolder.TABLE_CUSP_RECDET ;
            cursor = dB.rawQuery(selectQuery,null);

            while (cursor.moveToNext())
            {
                PayReceiptDet recDet = new PayReceiptDet();

                recDet.setFPRECDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                recDet.setFPRECDET_PAYTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.RECHED_PAYTYPE)));
                recDet.setFPRECDET_ALOAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.RECDET_ALOAMT)));
                recDet.setFPRECDET_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                recDet.setFPRECDET_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));

                list.add(recDet);

            }
        }
        catch (Exception e)
        {
            Log.v(TAG + " Exception", e.toString());
        }
        finally {
            if(cursor != null)
            {
                cursor.close();
            }
            dB.close();
        }
    return list;
    }







}
