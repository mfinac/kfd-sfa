package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.PushMsgHedDet;

import java.util.ArrayList;

public class PushMsgHedDetController {

    /*
    kaveesha - 16-09-2020
     */

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "PushMsgHedDet";


    public PushMsgHedDetController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdatePMsgHedDet(ArrayList<PushMsgHedDet> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_PMSG_HEDDET +
                    " (MsgBody," +
                    "MsgGrp," +
                    "MsgType," +
                    "RefNo," +
                    "Subject," +
                    "SupCode) " + " VALUES (?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (PushMsgHedDet pushMsgHedDet : list) {

                stmt.bindString(1, pushMsgHedDet.getFPUSHMSG_MSG_BODY());
                stmt.bindString(2, pushMsgHedDet.getFPUSHMSG_MSG_GRP());
                stmt.bindString(3, pushMsgHedDet.getFPUSHMSG_MSG_TYPE());
                stmt.bindString(4, pushMsgHedDet.getFPUSHMSG_REPNO());
                stmt.bindString(5, pushMsgHedDet.getFPUSHMSG_SUBJECT());
                stmt.bindString(6, pushMsgHedDet.getFPUSHMSG_SUPCODE());

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
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PMSG_HEDDET, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_PMSG_HEDDET, null, null);
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
