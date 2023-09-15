package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Area;

import java.util.ArrayList;

/*
    create by kaveesha 05-06-2020
 */

public class AreaController {

    Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase dB;
    private String TAG = "AreaController";

    public AreaController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void CreateOrUpdateArea(ArrayList<Area> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_AREA +
                    " (AreaCode," +
                    " AreaName) " + " VALUES (?,?)";
//                    " DealCode," +
                  //  " RegCode) " + " VALUES (?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (Area area : list) {
                stmt.bindString(1, area.getFAREA_CODE());
                stmt.bindString(2, area.getFAREA_NAME());
//                stmt.bindString(3, area.getFAREA_DEAL_CODE());
//                stmt.bindString(4, area.getFAREA_REG_CODE());

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

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_AREA, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_AREA, null, null);
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
