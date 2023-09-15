package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.FreeDeb;

import java.util.ArrayList;

public class FreeDebController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "FreeDebController";

    public FreeDebController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateFreeDeb(ArrayList<FreeDeb> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_FREEDEB + " (RefNo,DebCode) " + " VALUES (?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (FreeDeb freeDeb : list) {

                stmt.bindString(1, freeDeb.getFFREEDEB_REFNO());
                stmt.bindString(2, freeDeb.getFFREEDEB_DEB_CODE());

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
    public ArrayList<FreeDeb> getFreeIssueDebtors(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeDeb> list = new ArrayList<FreeDeb>();

        // String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') AND costcode='" + costCode + "' AND date('now') between vdatef and vdatet";
        // inoshi--Mine**CostCode change//
        String selectQuery = "select deb.debcode , deb.debname from TblDebtor deb, TblFreedeb fdeb where fdeb.debcode = deb.debcode and fdeb.refno ='" + refno + "'";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                FreeDeb freeHed = new FreeDeb();

                freeHed.setFFREEDEB_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE))+" - "+cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_NAME)));

                list.add(freeHed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return list;
    }
    public int getRefnoByDebCount(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT count(*) FROM " + ValueHolder.TABLE_FREEDEB + " WHERE " + ValueHolder.REFNO + "='" + refno + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getInt(0);

        }
        return 0;

    }

    public int isValidDebForFreeIssue(String refno, String currDeb) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT count(*) FROM " + ValueHolder.TABLE_FREEDEB + " WHERE " + ValueHolder.REFNO + "='" + refno + "' AND " + ValueHolder.DEBCODE + "='" + currDeb + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getInt(0);

        }
        return 0;

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

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_FREEDEB, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_FREEDEB, null, null);
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
