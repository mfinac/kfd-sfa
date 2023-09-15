package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.RepTrgDet;

import java.util.ArrayList;

public class RepTrgDetController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "RepTrgDetController";

    public static final String TABLE_FREFTRGDET = "TblRepTrgDet";
    // table attributes
    public static final String FREPTRGDET_ID = "Id";
    public static final String FREPTRGDET_YEART = "YearT";
    public static final String FREPTRGDET_MONTHN = "Monthn";
    public static final String FREPTRGDET_TRAMT= "TrAmt";


    // create String
    public static final String CREATE_FREPTRGDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_FREFTRGDET + " (" + FREPTRGDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ValueHolder.REFNO + " TEXT, "    + ValueHolder.REPCODE + " TEXT, " + ValueHolder.TXNDATE + " TEXT, "
            + FREPTRGDET_TRAMT + " TEXT, "    + FREPTRGDET_MONTHN + " TEXT, " + FREPTRGDET_YEART + " TEXT); ";


    public RepTrgDetController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public void InsertOrReplaceRepTrgDet(ArrayList<RepTrgDet> list) {
        Log.d("InsertOrReplaceDebtor", "" + list.size());
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + TABLE_FREFTRGDET + " (RefNo,RepCode,TxnDate,TrAmt,Monthn,YearT) " + " VALUES (?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (RepTrgDet repTrgDet : list) {
                stmt.bindString(1, repTrgDet.getFREPTRGDET_REFNO());
                stmt.bindString(2, repTrgDet.getFREPTRGDET_REPCODE());
                stmt.bindString(3, repTrgDet.getFREPTRGDET_TXNDATE());
                stmt.bindString(4, repTrgDet.getFREPTRGDET_TRAMT());
                stmt.bindString(5, repTrgDet.getFREPTRGDET_MONTHN());
                stmt.bindString(6, repTrgDet.getFREPTRGDET_YEART());


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

            cursor = dB.rawQuery("SELECT * FROM " + TABLE_FREFTRGDET, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(TABLE_FREFTRGDET, null, null);
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
