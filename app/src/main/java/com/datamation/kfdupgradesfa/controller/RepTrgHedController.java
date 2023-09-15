package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.RepTrgHed;

import java.util.ArrayList;

public class RepTrgHedController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "RepTrgHedController";

    public static final String TABLE_FREFTRGHED = "TblRepTrgHed";
    // table attributes
    public static final String FREPTRGHED_ID = "Id";
    public static final String FREPTRGHED_YEART = "YearT";

    // create String
    public static final String CREATE_FREPTRGHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FREFTRGHED + " (" + FREPTRGHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ValueHolder.REFNO + " TEXT, "    + ValueHolder.REPCODE + " TEXT, " + ValueHolder.TXNDATE + " TEXT, "+ FREPTRGHED_YEART + " TEXT); ";


    public RepTrgHedController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public void InsertOrReplaceRepTrgHed(ArrayList<RepTrgHed> list) {
        Log.d("InsertOrReplaceDebtor", "" + list.size());
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + TABLE_FREFTRGHED + " (RefNo,RepCode,TxnDate,YearT) " + " VALUES (?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (RepTrgHed repTrgHed : list) {
                stmt.bindString(1, repTrgHed.getFREPTRGHED_REFNO());
                stmt.bindString(2, repTrgHed.getFREPTRGHED_REPCODE());
                stmt.bindString(3, repTrgHed.getFREPTRGHED_TXNDATE());
                stmt.bindString(4, repTrgHed.getFREPTRGHED_YEART());


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


    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
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

            cursor = dB.rawQuery("SELECT * FROM " + TABLE_FREFTRGHED, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(TABLE_FREFTRGHED, null, null);
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
