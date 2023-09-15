package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.FreeMslab;

import java.util.ArrayList;

public class FreeMslabController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "FreeMslabController";

    public FreeMslabController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateFreeMslab(ArrayList<FreeMslab> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_FREEMSLAB +
                    " (RefNo," +
                    "Qtyf," +
                    "Qtyt," +
                    "Qty," +
                    "FreeItQty," +
                    "SeqNo) " + " VALUES (?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (FreeMslab freeMslab : list) {

                stmt.bindString(1, freeMslab.getFFREEMSLAB_REFNO());
                stmt.bindString(2, freeMslab.getFFREEMSLAB_QTY_F());
                stmt.bindString(3, freeMslab.getFFREEMSLAB_QTY_T());
                stmt.bindString(4, freeMslab.getFFREEMSLAB_ITEM_QTY());
                stmt.bindString(5, freeMslab.getFFREEMSLAB_FREE_IT_QTY());
                stmt.bindString(6, freeMslab.getFFREEMSLAB_SEQ_NO());

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

    public ArrayList<FreeMslab> getMixDetails(String refno, int tQty) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeMslab> list = new ArrayList<FreeMslab>();

        //String selectQuery = "select * from ffreeMslab where refno='" + refno + "' and " + tQty + " between CAST(Qtyf as double) and CAST(Qtyt as double)";

        //get range which quantity include ; ex : if we enter 150 as qty, there is a scheme mix type 1-143 : 8-1
        String selectQuery = "select * from TblFreeMslab where refno='" + refno + "' and " + tQty + " >= CAST(Qtyf as double) order by CAST(Qtyf as double) desc limit 1";


        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            FreeMslab freeMslab = new FreeMslab();

            freeMslab.setFFREEMSLAB_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
            freeMslab.setFFREEMSLAB_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            freeMslab.setFFREEMSLAB_QTY_F(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY_F)));
            freeMslab.setFFREEMSLAB_QTY_T(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY_T)));
            freeMslab.setFFREEMSLAB_ITEM_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
            freeMslab.setFFREEMSLAB_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FREE_IT_QTY)));

            freeMslab.setFFREEMSLAB_SEQ_NO(cursor.getString(cursor.getColumnIndex(ValueHolder.SEQNO)));

            list.add(freeMslab);

        }

        return list;
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
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_FREEMSLAB, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_FREEMSLAB, null, null);
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

    public String getFreeDetails(String itemcode, String DebCode) {
        String FreeDetails = "";
        String freeRefNo = "";
        boolean hasdebfree = false;
        Cursor cursor = null;
        Cursor cursordebcnt = null;
        Cursor cursordeb = null;
        Cursor cursordebfree = null;
        String Seperate = "";

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            // String selectQuery = "SELECT refno,ItemQty, FreeItQty FROM
            // ffreemslab WHERE refno IN (SELECT refno FROM ffreedet where
            // itemcode='" + itemcode
            // + "') and refno NOT IN (SELECT refno FROM ffreedeb WHERE
            // DebCode='"+DebCode+"') order by seqno";
            //

            String selectQuery = " SELECT distinct refno as RefNo FROM "+ValueHolder.TABLE_FREEMSLAB+" WHERE refno IN (SELECT refno FROM "+ValueHolder.TABLE_FREEDET+" where itemcode='" + itemcode + "')";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                // cursor.moveToFirst();
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {

                        // for (cursor.moveToFirst(); !cursor.isAfterLast();
                        // cursor.moveToNext()) {
                        // do what you need with the cursor here
                        // String freeRefNo ="";

                        freeRefNo = cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO));

                        String selectQuerydebcnt = "SELECT refno FROM " + ValueHolder.TABLE_FREEDEB + " WHERE " + ValueHolder.REFNO + "='" + freeRefNo + "'";
                        cursordebcnt = dB.rawQuery(selectQuerydebcnt, null);

                        if (cursordebcnt.getCount() > 0) {

                            String selectQueryDeb = "SELECT refno FROM " + ValueHolder.TABLE_FREEDEB + " WHERE DebCode='" + DebCode + "' AND RefNo = '" + freeRefNo + "'";
                            cursordeb = dB.rawQuery(selectQueryDeb, null);

                            if (cursordeb.getCount() > 0) {
                                hasdebfree = true;
                                String selectQueryDebfree = "SELECT refno,Qty, FreeItQty FROM "+ValueHolder.TABLE_FREEMSLAB+" WHERE refno = '" + freeRefNo + "'";
                                // freeRefNo =
                                // cursor.getString(cursor.getColumnIndex(dbHelper.FFREEDEB_REFNO));
                                cursordebfree = dB.rawQuery(selectQueryDebfree, null);

                                while (cursordebfree.moveToNext()) {

                                    if (cursordebfree.isLast()) {
                                        Seperate = "";
                                    } else {
                                        Seperate = " || ";
                                    }

                                    FreeDetails = FreeDetails + cursordebfree.getString(cursordebfree.getColumnIndex(ValueHolder.ITEMQTY)) + " x " + cursordebfree.getString(cursordebfree.getColumnIndex(ValueHolder.FREEIT_QTY)) + Seperate;

                                    Seperate = "";
                                }
                            }
                        } else {

                            String selectQueryDebfree = "SELECT refno,Qty, FreeItQty FROM "+ValueHolder.TABLE_FREEMSLAB+" WHERE refno = '" + freeRefNo + "'";

                            cursordebfree = dB.rawQuery(selectQueryDebfree, null);

                            while (cursordebfree.moveToNext()) {

                                if (cursordebfree.isLast()) {
                                    Seperate = "";
                                } else {
                                    Seperate = " || ";
                                }

                                FreeDetails = FreeDetails + cursordebfree.getString(cursordebfree.getColumnIndex(ValueHolder.ITEMQTY)) + " x " + cursordebfree.getString(cursordebfree.getColumnIndex(ValueHolder.FREEIT_QTY)) + Seperate;

                                Seperate = "";
                            }
                        }

                        cursor.moveToNext();
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return FreeDetails;
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            return FreeDetails;
        }
    }
}