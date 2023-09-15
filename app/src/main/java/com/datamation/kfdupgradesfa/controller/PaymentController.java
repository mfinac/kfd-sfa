package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Payment;

import java.util.ArrayList;

/*
    kaveesha   10/11/2020
 */

public class PaymentController {

    Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase dB;
    private String TAG = "PaymentController";

    public PaymentController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open()throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    public void CreateOrUpdatePayments(ArrayList<Payment> list){

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            dB.beginTransactionNonExclusive();

            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_PAYMENTS +
                    " (InvoiceDate," +
                    "InvoiceNo," +
                    "PaidAmount," +
                    "PaymentType," +
                    "ReceiptDate," +
                    "ReceiptNo) " + " VALUES (?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (Payment payment : list) {

                stmt.bindString(1, payment.getFPAYMENT_INVOICE_DATE());
                stmt.bindString(2, payment.getFPAYMENT_INVOICE_NO());
                stmt.bindString(3, payment.getFPAYMENT_PAID_AMT());
                stmt.bindString(4, payment.getFPAYMENT_TYPE());
                stmt.bindString(5, payment.getFPAYMENT_RECEIPT_DATE());
                stmt.bindString(6, payment.getFPAYMENT_RECEIPT_NO());

                stmt.execute();
                stmt.clearBindings();
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }finally {
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
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PAYMENTS, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_PAYMENTS, null, null);
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

    public ArrayList<Payment> getPayments(){

        if(dB == null)
        {
            open();
        }else if(!dB.isOpen())
        {
            open();
        }

        Cursor cursor = null;
        ArrayList<Payment> list = new ArrayList<Payment>();

        try
        {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_PAYMENTS ;
            cursor = dB.rawQuery(selectQuery,null);

            while (cursor.moveToNext())
            {
                Payment payment = new Payment();

                payment.setFPAYMENT_RECEIPT_NO(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_RECEIPT_NO)));
                payment.setFPAYMENT_RECEIPT_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_RECEIPT_DATE)));
                payment.setFPAYMENT_INVOICE_NO(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_INVOICE_NO)));
                payment.setFPAYMENT_INVOICE_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_INVOICE_DATE)));
                payment.setFPAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
                payment.setFPAYMENT_PAID_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_AMT)));

                list.add(payment);

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
