package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Town;

import java.util.ArrayList;

public class TownController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "TownController";

    public TownController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateFTown(ArrayList<Town> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_TOWN +
                    " (TownCode," +
                    " TownName," +
                    "DistrCode) " + " VALUES (?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (Town town : list) {

                stmt.bindString(1, town.getFTOWN_CODE());
                stmt.bindString(2, town.getFTOWN_NAME());
                stmt.bindString(3, town.getFTOWN_DISTR_CODE());

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

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_TOWN, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_TOWN, null, null);
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
    public ArrayList<Town> getAllTowns() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        ArrayList<Town> list = new ArrayList<Town>();

        String selectQuery = "select * from fTown";
        Cursor cursor = null;
        try {
            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Town town = new Town();

                town.setFTOWN_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TOWN_CODE)));
                town.setFTOWN_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.TOWN_NAME)));
                town.setFTOWN_DISTR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DISTR_CODE)));

                list.add(town);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
