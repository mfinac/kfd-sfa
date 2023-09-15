package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.PayReceiptHed;

import java.util.ArrayList;

/*
   kaveesha - 22-09-2020
 */

public class PaymentHeaderController {


    Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase dB;
    private String TAG = "PaymentHeaderController";


    public PaymentHeaderController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateCusPRecHed(ArrayList<PayReceiptHed> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_CUSP_RECHED +
                    " (AddDate," +
                    "AddMach," +
                    "AddUser," +
                    "ChqDate," +
                    "ChqNo," +
                    "DebCode," +
                    "PayType," +
                    "RefNo," +
                    "RepCode," +
                    "TotalAmt," +
                    "TxnDate) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (PayReceiptHed receiptHed : list) {

                stmt.bindString(1, receiptHed.getFPRECHED_ADDDATE());
                stmt.bindString(2, receiptHed.getFPRECHED_ADDMACH());
                stmt.bindString(3, receiptHed.getFPRECHED_ADDUSER());
                stmt.bindString(4, receiptHed.getFPRECHED_CHQDATE());
                stmt.bindString(5, receiptHed.getFPRECHED_CHQNO());
                stmt.bindString(6, receiptHed.getFPRECHED_DEBCODE());
                stmt.bindString(7, receiptHed.getFPRECHED_PAYTYPE());
                stmt.bindString(8, receiptHed.getFPRECHED_REFNO());
                stmt.bindString(9, receiptHed.getFPRECHED_REPCODE());
                stmt.bindString(10, receiptHed.getFPRECHED_TOTALAMT());
                stmt.bindString(11, receiptHed.getFPRECHED_TXNDATE());

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
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_CUSP_RECHED, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_CUSP_RECHED, null, null);
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



}
