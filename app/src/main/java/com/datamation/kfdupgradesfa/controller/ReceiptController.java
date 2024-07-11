package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.CompanyBranch;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.RecHed;
import com.datamation.kfdupgradesfa.model.ReceiptHed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
    Modify by kaveesha - 01/12/2021
 */

public class ReceiptController {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "KfdUpgrade";
    SharedPref sharedPref;


    public static final String CREATE_FPRECHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FPRECHED + " (" + ValueHolder.FPRECHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

            ValueHolder.REFNO + " TEXT, " + ValueHolder.FPRECHED_REFNO1 + " TEXT, " + ValueHolder.FPRECHED_MANUREF + " TEXT, " + ValueHolder.FPRECHED_SALEREFNO + " TEXT, " +
            ValueHolder.REPCODE + " TEXT, " + ValueHolder.FPRECHED_TXNTYPE + " TEXT, " + ValueHolder.FPRECHED_CHQNO + " TEXT, " + ValueHolder.FPRECHED_CHQDATE + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " + ValueHolder.FPRECHED_CURCODE + " TEXT, " +
            ValueHolder.FPRECHED_CURRATE1 + " TEXT, " + ValueHolder.DEBCODE + " TEXT, " + ValueHolder.FPRECHED_TOTALAMT + " TEXT, " + ValueHolder.FPRECHED_BANKCODE + " TEXT, " + ValueHolder.FPRECHED_BRANCHCODE + " TEXT, " +
            ValueHolder.FPRECHED_BTOTALAMT + " TEXT, " + ValueHolder.FPRECHED_PAYTYPE + " TEXT, " + ValueHolder.FPRECHED_PRTCOPY + " TEXT, " + ValueHolder.FPRECHED_REMARKS + " TEXT, " + ValueHolder.FPRECHED_ADDUSER + " TEXT, " + ValueHolder.FPRECHED_ADDMACH + " TEXT, " + ValueHolder.FPRECHED_ADDDATE + " TEXT, " +
            ValueHolder.FPRECHED_RECORDID + " TEXT, " + ValueHolder.FPRECHED_TIMESTAMP + " TEXT, " + ValueHolder.FPRECHED_ISDELETE + " TEXT, " + ValueHolder.FPRECHED_COST_CODE + " TEXT, " +
            ValueHolder.FPRECHED_LONGITUDE + " TEXT, " + ValueHolder.FPRECHED_LATITUDE + " TEXT, " + ValueHolder.FPRECHED_ADDRESS + " TEXT, " + ValueHolder.FPRECHED_START_TIME + " TEXT, " + ValueHolder.FPRECHED_END_TIME + " TEXT, " + ValueHolder.FPRECHED_ISACTIVE + " TEXT, " + ValueHolder.FPRECHED_ISSYNCED + " TEXT, " + ValueHolder.FPRECHED_CURRATE + " TEXT, " + ValueHolder.FPRECHED_CUSBANK + " TEXT);";

    /*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


    public ReceiptController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        sharedPref = SharedPref.getInstance(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateRecHed(ArrayList<ReceiptHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (ReceiptHed recHed : list) {

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHED + " WHERE "
                        + ValueHolder.REFNO + " = '" + recHed.getFPRECHED_REFNO() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                values.put(ValueHolder.FPRECHED_ID, recHed.getFPRECHED_ID());
                values.put(ValueHolder.REFNO, recHed.getFPRECHED_REFNO());
                values.put(ValueHolder.FPRECHED_REFNO1, recHed.getFPRECHED_REFNO1());
                //values.put(DatabaseHelper.REFNO2, recHed.getFPRECHED_REFNO2());
                values.put(ValueHolder.FPRECHED_MANUREF, recHed.getFPRECHED_MANUREF());
                values.put(ValueHolder.FPRECHED_SALEREFNO, recHed.getFPRECHED_SALEREFNO());
                values.put(ValueHolder.REPCODE, recHed.getFPRECHED_REPCODE());
                values.put(ValueHolder.FPRECHED_TXNTYPE, recHed.getFPRECHED_TXNTYPE());
                values.put(ValueHolder.FPRECHED_CHQNO, recHed.getFPRECHED_CHQNO());
                values.put(ValueHolder.FPRECHED_CHQDATE, recHed.getFPRECHED_CHQDATE());
                values.put(ValueHolder.TXNDATE, recHed.getFPRECHED_TXNDATE());
                values.put(ValueHolder.FPRECHED_CURCODE, recHed.getFPRECHED_CURCODE());
                values.put(ValueHolder.FPRECHED_CURRATE1, "");

                values.put(ValueHolder.DEBCODE, recHed.getFPRECHED_DEBCODE());
                values.put(ValueHolder.FPRECHED_TOTALAMT, recHed.getFPRECHED_TOTALAMT());
                values.put(ValueHolder.FPRECHED_BTOTALAMT, recHed.getFPRECHED_BTOTALAMT());
                values.put(ValueHolder.FPRECHED_PAYTYPE, recHed.getFPRECHED_PAYTYPE());
                values.put(ValueHolder.FPRECHED_PRTCOPY, "");
                values.put(ValueHolder.FPRECHED_REMARKS, recHed.getFPRECHED_REMARKS());
                values.put(ValueHolder.FPRECHED_ADDUSER, recHed.getFPRECHED_ADDUSER());
                values.put(ValueHolder.FPRECHED_ADDMACH, recHed.getFPRECHED_ADDMACH());
                values.put(ValueHolder.FPRECHED_ADDDATE, recHed.getFPRECHED_ADDDATE());
                values.put(ValueHolder.FPRECHED_RECORDID, "");
                values.put(ValueHolder.FPRECHED_TIMESTAMP, "");
                values.put(ValueHolder.FPRECHED_CURRATE, recHed.getFPRECHED_CURRATE());
                values.put(ValueHolder.FPRECHED_CUSBANK, recHed.getFPRECHED_CUSBANK());
                values.put(ValueHolder.FPRECHED_LONGITUDE, recHed.getFPRECHED_LONGITUDE());
                values.put(ValueHolder.FPRECHED_LATITUDE, recHed.getFPRECHED_LATITUDE());
                values.put(ValueHolder.FPRECHED_ADDRESS, recHed.getFPRECHED_ADDRESS());
                values.put(ValueHolder.FPRECHED_START_TIME, recHed.getFPRECHED_START_TIME());
                values.put(ValueHolder.FPRECHED_END_TIME, recHed.getFPRECHED_END_TIME());
                values.put(ValueHolder.FPRECHED_ISSYNCED, recHed.getFPRECHED_ISSYNCED());
                values.put(ValueHolder.FPRECHED_ISACTIVE, recHed.getFPRECHED_ISACTIVE());
                values.put(ValueHolder.FPRECHED_ISDELETE, recHed.getFPRECHED_ISDELETE());

                int cn = cursor.getCount();
                if (cn > 0) {
                    count = dB.update(ValueHolder.TABLE_FPRECHED, values, ValueHolder.REFNO + " =?",
                            new String[]{String.valueOf(recHed.getFPRECHED_REFNO())});
                } else {
                    count = (int) dB.insert(ValueHolder.TABLE_FPRECHED, null, values);
                }

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

    /*
     * create for store singlr receipt
     */
    public int createOrUpdateRecHedS(ArrayList<ReceiptHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (ReceiptHed recHed : list) {

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHEDS + " WHERE "
                        + ValueHolder.REFNO + " = '" + recHed.getFPRECHED_REFNO() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                //values.put(FPRECHED_ID, recHed.getFPRECHED_ID());
                values.put(ValueHolder.REFNO, recHed.getFPRECHED_REFNO());
                values.put(ValueHolder.FPRECDET_REFNO1, recHed.getFPRECHED_REFNO1());
                values.put(ValueHolder.FPRECHED_MANUREF, recHed.getFPRECHED_MANUREF());
                values.put(ValueHolder.FPRECHED_SALEREFNO, recHed.getFPRECHED_SALEREFNO());
                values.put(ValueHolder.REPCODE, recHed.getFPRECHED_REPCODE());
                values.put(ValueHolder.FPRECHED_TXNTYPE, recHed.getFPRECHED_TXNTYPE());
                values.put(ValueHolder.FPRECHED_CHQNO, recHed.getFPRECHED_CHQNO());
                values.put(ValueHolder.FPRECHED_CHQDATE, recHed.getFPRECHED_CHQDATE());
                values.put(ValueHolder.TXNDATE, recHed.getFPRECHED_TXNDATE());
                values.put(ValueHolder.FPRECHED_CURCODE, recHed.getFPRECHED_CURCODE());
                values.put(ValueHolder.FPRECHED_CURRATE1, "");
                values.put(ValueHolder.DEBCODE, recHed.getFPRECHED_DEBCODE());
                values.put(ValueHolder.FPRECHED_TOTALAMT, recHed.getFPRECHED_TOTALAMT());
                values.put(ValueHolder.FPRECHED_BTOTALAMT, recHed.getFPRECHED_BTOTALAMT());
                values.put(ValueHolder.FPRECHED_PAYTYPE, recHed.getFPRECHED_PAYTYPE());
                values.put(ValueHolder.FPRECHED_PRTCOPY, "");
                values.put(ValueHolder.FPRECHED_REMARKS, recHed.getFPRECHED_REMARKS());
                values.put(ValueHolder.FPRECHED_ADDUSER, recHed.getFPRECHED_ADDUSER());
                values.put(ValueHolder.FPRECHED_ADDMACH, recHed.getFPRECHED_ADDMACH());
                values.put(ValueHolder.FPRECHED_ADDDATE, recHed.getFPRECHED_ADDDATE());
                values.put(ValueHolder.FPRECHED_RECORDID, "");
                values.put(ValueHolder.FPRECHED_TIMESTAMP, "");
                values.put(ValueHolder.FPRECHED_CURRATE, recHed.getFPRECHED_CURRATE());
                values.put(ValueHolder.FPRECHED_CUSBANK, recHed.getFPRECHED_CUSBANK());
                values.put(ValueHolder.FPRECHED_LONGITUDE, recHed.getFPRECHED_LONGITUDE());
                values.put(ValueHolder.FPRECHED_LATITUDE, recHed.getFPRECHED_LATITUDE());
                values.put(ValueHolder.FPRECHED_ADDRESS, recHed.getFPRECHED_ADDRESS());
                values.put(ValueHolder.FPRECHED_START_TIME, recHed.getFPRECHED_START_TIME());
                values.put(ValueHolder.FPRECHED_END_TIME, recHed.getFPRECHED_END_TIME());
                values.put(ValueHolder.FPRECHED_ISSYNCED, recHed.getFPRECHED_ISSYNCED());
                values.put(ValueHolder.FPRECHED_ISACTIVE, recHed.getFPRECHED_ISACTIVE());
                values.put(ValueHolder.FPRECHED_ISDELETE, recHed.getFPRECHED_ISDELETE());
                values.put(ValueHolder.FPRECHED_BANKCODE, recHed.getFPRECHED_BANKCODE());
                values.put(ValueHolder.FPRECHED_BRANCHCODE, recHed.getFPRECHED_BRANCHCODE());
                values.put(ValueHolder.FPRECHED_ADDUSER_NEW, recHed.getFPRECHED_ADDUSER_NEW());
                values.put(ValueHolder.FPRECHED_COST_CODE, recHed.getFPRECHED_COSTCODE());

                int cn = cursor.getCount();
                if (cn > 0) {
                    count = dB.update(ValueHolder.TABLE_FPRECHEDS, values, ValueHolder.REFNO + " =?",
                            new String[]{String.valueOf(recHed.getFPRECHED_REFNO())});
                } else {
                    count = (int) dB.insert(ValueHolder.TABLE_FPRECHEDS, null, values);
                }

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


    //-------------------MMS -------------------- 17/05/2022 ------------------------------------------------
    public int CreateOrUpdateReceiptStatus(ArrayList<ReceiptHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;


        Log.d("*********^^^JSON ARRAY", "doInBackground: " + list);
        try {

            for (ReceiptHed rec : list) {

//                String newRef = order.getFORDHED_REFNO().replace("/","");

                Log.d("*********^^ref", "CreateOrUpdateReceiptStatus: " + rec.getFPRECHED_REFNO());

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHEDS + " WHERE " + ValueHolder.REFNO
                        + " = '" + rec.getFPRECHED_REFNO() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                values.put(ValueHolder.FPRECHED_STATUS, rec.getFPRECHED_STATUS());

                count = dB.update(ValueHolder.TABLE_FPRECHEDS, values, ValueHolder.REFNO + " =?",
                        new String[]{String.valueOf(rec.getFPRECHED_REFNO())});
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

    /*
     * create for single receipt
     */
    public ArrayList<ReceiptHed> getAllCompletedRecHedS(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();
        String selectQuery;


        try {
            if (refno.equals("")) {

                selectQuery = "select * from " + ValueHolder.TABLE_FPRECHEDS + " Where "
                        + ValueHolder.FPRECHED_ISDELETE + "='0' and " + ValueHolder.FPRECHED_ISACTIVE
                        + "='0' Order by " + ValueHolder.FPRECHED_ISSYNCED + "," + ValueHolder.REFNO + " DESC";

            } else {

                selectQuery = "select * from " + ValueHolder.TABLE_FPRECHEDS + " Where "
                        + ValueHolder.FPRECHED_ISDELETE + "='0' and " + ValueHolder.FPRECHED_ISACTIVE + "='0' and " + ValueHolder.REFNO + "='" + refno
                        + "' Order by " + ValueHolder.FPRECHED_ISSYNCED + "," + ValueHolder.REFNO + " DESC";
            }

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                ReceiptHed recHed = new ReceiptHed();

                recHed.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDDATE)));
                recHed.setFPRECHED_ADDMACH(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDMACH)));
                recHed.setFPRECHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDRESS)));
                recHed.setFPRECHED_ADDUSER(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER)));
                recHed.setFPRECHED_BANKCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BANKCODE)));
                recHed.setFPRECHED_BRANCHCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BRANCHCODE)));
                recHed.setFPRECHED_BTOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BTOTALAMT)));
                recHed.setFPRECHED_CHQDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE)));
                recHed.setFPRECHED_CHQNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQNO)));
                recHed.setFPRECHED_COSTCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_COST_CODE)));
                recHed.setFPRECHED_CURCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURCODE)));
                recHed.setFPRECHED_CURRATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE)));
                recHed.setFPRECHED_CURRATE1(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE1)));
                recHed.setFPRECHED_CUSBANK(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CUSBANK)));
                recHed.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                recHed.setFPRECHED_END_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_END_TIME)));
                recHed.setFPRECHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ID)));
                recHed.setFPRECHED_ISACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISACTIVE)));
                recHed.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISSYNCED)));
                recHed.setFPRECHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LATITUDE)));
                recHed.setFPRECHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LONGITUDE)));
                recHed.setFPRECHED_MANUREF(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_MANUREF)));
                recHed.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PAYTYPE)));
                recHed.setFPRECHED_PRTCOPY(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PRTCOPY)));
                recHed.setFPRECHED_RECORDID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_RECORDID)));
                recHed.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                recHed.setFPRECHED_REFNO1(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECDET_REFNO1)));
                recHed.setFPRECHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_REMARKS)));
                recHed.setFPRECHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                recHed.setFPRECHED_SALEREFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_SALEREFNO)));
                recHed.setFPRECHED_START_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_START_TIME)));
                recHed.setFPRECHED_TIMESTAMP(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TIMESTAMP)));
                recHed.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT)));
                recHed.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                recHed.setFPRECHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TXNTYPE)));
                //	recHed.setFPRECHED_ADDUSER_NEW(cursor.getString(cursor.getColumnIndex(FPRECHED_ADDUSER_NEW)));


                list.add(recHed);

            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return list;
    }

    /*
     * create for all receipt MMS ***2021/02
     */
    public ArrayList<ReceiptHed> getAllCompletedRecHedS() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();
        String selectQuery;


        try {

            selectQuery = "select * from " + ValueHolder.TABLE_FPRECHEDS;


            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                ReceiptHed recHed = new ReceiptHed();

                recHed.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDDATE)));
                recHed.setFPRECHED_ADDMACH(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDMACH)));
                recHed.setFPRECHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDRESS)));
                recHed.setFPRECHED_ADDUSER(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER)));
                recHed.setFPRECHED_BANKCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BANKCODE)));
                recHed.setFPRECHED_BRANCHCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BRANCHCODE)));
                recHed.setFPRECHED_BTOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BTOTALAMT)));
                recHed.setFPRECHED_CHQDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE)));
                recHed.setFPRECHED_CHQNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQNO)));
                recHed.setFPRECHED_COSTCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_COST_CODE)));
                recHed.setFPRECHED_CURCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURCODE)));
                recHed.setFPRECHED_CURRATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE)));
                recHed.setFPRECHED_CURRATE1(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE1)));
                recHed.setFPRECHED_CUSBANK(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CUSBANK)));
                recHed.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                recHed.setFPRECHED_END_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_END_TIME)));
                recHed.setFPRECHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ID)));
                recHed.setFPRECHED_ISACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISACTIVE)));
                recHed.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISSYNCED)));
                recHed.setFPRECHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LATITUDE)));
                recHed.setFPRECHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LONGITUDE)));
                recHed.setFPRECHED_MANUREF(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_MANUREF)));
                recHed.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PAYTYPE)));
                recHed.setFPRECHED_PRTCOPY(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PRTCOPY)));
                recHed.setFPRECHED_RECORDID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_RECORDID)));
                recHed.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                recHed.setFPRECHED_REFNO1(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECDET_REFNO1)));
                recHed.setFPRECHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_REMARKS)));
                recHed.setFPRECHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                recHed.setFPRECHED_SALEREFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_SALEREFNO)));
                recHed.setFPRECHED_START_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_START_TIME)));
                recHed.setFPRECHED_TIMESTAMP(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TIMESTAMP)));
                recHed.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT)));
                recHed.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                recHed.setFPRECHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TXNTYPE)));
                //	recHed.setFPRECHED_ADDUSER_NEW(cursor.getString(cursor.getColumnIndex(FPRECHED_ADDUSER_NEW)));
                recHed.setRecDetList(new ReceiptDetController(context).GetReceiptByRefno(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));

                list.add(recHed);

            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return list;
    }
    /*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<ReceiptHed> getAllCompletedRecHed(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();
        String selectQuery;

        try {
            if (refno.equals("")) {

                selectQuery = "select * from " + ValueHolder.TABLE_FPRECHED + " Where "
                        + ValueHolder.FPRECHED_ISDELETE + "='0' and " + ValueHolder.FPRECHED_TOTALAMT
                        + ">'0'";

            } else {

                selectQuery = "select * from " + ValueHolder.TABLE_FPRECHED + " Where "
                        + ValueHolder.FPRECHED_ISDELETE + "='0' and " + ValueHolder.FPRECHED_ISACTIVE
                        + "='0' and " + ValueHolder.FPRECHED_TOTALAMT
                        + ">'0' and " + ValueHolder.REFNO + "='" + refno + "'";
            }

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                ReceiptHed recHed = new ReceiptHed();

                recHed.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDDATE)));
                recHed.setFPRECHED_ADDMACH(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDMACH)));
                recHed.setFPRECHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDRESS)));
                recHed.setFPRECHED_ADDUSER(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER)));
                recHed.setFPRECHED_BTOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BTOTALAMT)));
                recHed.setFPRECHED_CHQDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE)));
                recHed.setFPRECHED_CHQNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQNO)));
                recHed.setFPRECHED_CURCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURCODE)));
                recHed.setFPRECHED_CURRATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE)));
                recHed.setFPRECHED_CURRATE1(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE1)));
                recHed.setFPRECHED_CUSBANK(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CUSBANK)));
                recHed.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                recHed.setFPRECHED_END_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_END_TIME)));
                recHed.setFPRECHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ID)));
                recHed.setFPRECHED_ISACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISACTIVE)));
                recHed.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISSYNCED)));
                recHed.setFPRECHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LATITUDE)));
                recHed.setFPRECHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LONGITUDE)));
                recHed.setFPRECHED_MANUREF(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_MANUREF)));
                recHed.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PAYTYPE)));
                recHed.setFPRECHED_PRTCOPY(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PRTCOPY)));
                recHed.setFPRECHED_RECORDID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_RECORDID)));
                recHed.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                recHed.setFPRECHED_REFNO1(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_REFNO1)));
                recHed.setFPRECHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_REMARKS)));
                recHed.setFPRECHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                recHed.setFPRECHED_SALEREFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_SALEREFNO)));
                recHed.setFPRECHED_START_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_START_TIME)));
                recHed.setFPRECHED_TIMESTAMP(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TIMESTAMP)));
                recHed.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT)));
                recHed.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                recHed.setFPRECHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TXNTYPE)));
                recHed.setFPRECHED_ISDELETE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISDELETE)));

                list.add(recHed);

            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return list;
    }

    public ReceiptHed getReceiptByRefno(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery;

        ReceiptHed recHed = new ReceiptHed();
        try {

            selectQuery = "select * from " + ValueHolder.TABLE_FPRECHEDS + " Where " + ValueHolder.REFNO
                    + "='" + refno + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                recHed.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDDATE)));
                recHed.setFPRECHED_ADDMACH(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDMACH)));
                recHed.setFPRECHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDRESS)));
                recHed.setFPRECHED_ADDUSER(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER)));
                recHed.setFPRECHED_BANKCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BANKCODE)));
                recHed.setFPRECHED_BRANCHCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BRANCHCODE)));
                recHed.setFPRECHED_BTOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BTOTALAMT)));
                recHed.setFPRECHED_CHQDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE)));
                recHed.setFPRECHED_CHQNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQNO)));
                recHed.setFPRECHED_CURCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURCODE)));
                recHed.setFPRECHED_CURRATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE)));
                recHed.setFPRECHED_CURRATE1(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE1)));
                recHed.setFPRECHED_CUSBANK(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CUSBANK)));
                recHed.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                recHed.setFPRECHED_END_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_END_TIME)));
                recHed.setFPRECHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ID)));
                recHed.setFPRECHED_ISACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISACTIVE)));
                recHed.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISSYNCED)));
                recHed.setFPRECHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LATITUDE)));
                recHed.setFPRECHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LONGITUDE)));
                recHed.setFPRECHED_MANUREF(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_MANUREF)));
                recHed.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PAYTYPE)));
                recHed.setFPRECHED_PRTCOPY(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PRTCOPY)));
                recHed.setFPRECHED_RECORDID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_RECORDID)));
                recHed.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                recHed.setFPRECHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_REMARKS)));
                recHed.setFPRECHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                recHed.setFPRECHED_SALEREFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_SALEREFNO)));
                recHed.setFPRECHED_START_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_START_TIME)));
                recHed.setFPRECHED_TIMESTAMP(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TIMESTAMP)));
                recHed.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT)));
                recHed.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                recHed.setFPRECHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TXNTYPE)));
                //	recHed.setFPRECHED_ADDUSER_NEW(cursor.getString(cursor.getColumnIndex(FPRECHED_ADDUSER_NEW)));

            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return recHed;
    }

    /*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


    public int CancelReceiptS(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        int result = 0;
        try {
            result = dB.delete(ValueHolder.TABLE_FPRECHEDS, ValueHolder.REFNO + "=?",
                    new String[]{Refno});

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return result;
    }

    /*-*-*-*-*-*-**-*-*--*-*-*-*-*-*-*--**-*-*-*-*-*---*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int InactiveStatusUpdate(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHEDS + " WHERE "
                    + ValueHolder.REFNO + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(ValueHolder.FPRECHED_ISACTIVE, "0");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_FPRECHEDS, values, ValueHolder.REFNO + " =?",
                        new String[]{String.valueOf(refno)});
            } else {
                count = (int) dB.insert(ValueHolder.TABLE_FPRECHEDS, null, values);
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

    public int InactiveStatusUpdateForMultiREceipt(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHED + " WHERE "
                    + ValueHolder.FPRECHED_REFNO1 + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(ValueHolder.FPRECHED_ISACTIVE, "0");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_FPRECHED, values, ValueHolder.FPRECHED_REFNO1 + " =?",
                        new String[]{String.valueOf(refno)});
            } else {
                count = (int) dB.insert(ValueHolder.TABLE_FPRECHED, null, values);
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

    /***********************************************************************/
    public int DeleteStatusUpdateForMultiREceipt(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHED + " WHERE "
                    + ValueHolder.FPRECHED_REFNO1 + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(ValueHolder.FPRECHED_ISDELETE, "1");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_FPRECHED, values, ValueHolder.FPRECHED_REFNO1 + " =?",
                        new String[]{String.valueOf(refno)});
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
    /*-*-*-*-*-*-**-*-*--*-*-*-*-*-*-*--**-*-*-*-*-*---*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int DeleteStatusUpdateForEceipt(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHEDS + " WHERE "
                    + ValueHolder.REFNO + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(ValueHolder.FPRECHED_ISDELETE, "1");

            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_FPRECHEDS, values, ValueHolder.REFNO + " =?",
                        new String[]{String.valueOf(refno)});
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

    /********************************************************************************/
    public void UpdateRecHedForMultiReceipt(ReceiptHed recHed, String Refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ContentValues values = new ContentValues();

        try {
            values.put(ValueHolder.FPRECHED_LATITUDE, recHed.getFPRECHED_LATITUDE());
            values.put(ValueHolder.FPRECHED_LONGITUDE, recHed.getFPRECHED_LONGITUDE());
            values.put(ValueHolder.FPRECHED_START_TIME, recHed.getFPRECHED_START_TIME());
            values.put(ValueHolder.FPRECHED_END_TIME, recHed.getFPRECHED_END_TIME());
            values.put(ValueHolder.FPRECHED_ADDRESS, recHed.getFPRECHED_ADDRESS());

            dB.update(ValueHolder.TABLE_FPRECHED, values, ValueHolder.FPRECHED_REFNO1 + " =?",
                    new String[]{String.valueOf(Refno)});

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
    }

    public void UpdateRecHed(ReceiptHed recHed, String Refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ContentValues values = new ContentValues();

        try {
            values.put(ValueHolder.FPRECHED_LATITUDE, recHed.getFPRECHED_LATITUDE());
            values.put(ValueHolder.FPRECHED_LONGITUDE, recHed.getFPRECHED_LONGITUDE());
            values.put(ValueHolder.FPRECHED_START_TIME, recHed.getFPRECHED_START_TIME());
            values.put(ValueHolder.FPRECHED_END_TIME, recHed.getFPRECHED_END_TIME());
            values.put(ValueHolder.FPRECHED_ADDRESS, recHed.getFPRECHED_ADDRESS());
            values.put(ValueHolder.FPRECHED_COST_CODE, recHed.getFPRECHED_COSTCODE());
            values.put(ValueHolder.FPRECHED_STATUS, recHed.getFPRECHED_STATUS());

            dB.update(ValueHolder.TABLE_FPRECHEDS, values, ValueHolder.REFNO + " =?",
                    new String[]{String.valueOf(Refno)});

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
    }


    /*-*-*-*-*-*-**-*-*--*-*-*-*-*-*-*--**-*-*-*-*-*---*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


    /**
     * @return RefNo2ForRecDet
     */

    @SuppressWarnings("static-access")
    public String getFPRECHED_REFNO2ForRecDet(String code, String status) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHED + " WHERE " + ValueHolder.DEBCODE
                + " = '" + code + "' AND " + ValueHolder.FPRECHED_ISACTIVE + " = '" + status + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO));

        }

        return "";
    }

    @SuppressWarnings("static-access")
    public String getChequeDate(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHEDS + " WHERE " + ValueHolder.REFNO
                + " = '" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE));

        }

        return "";
    }

    @SuppressWarnings("static-access")
    public String getChequeNo(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHEDS + " WHERE " + ValueHolder.REFNO
                + " = '" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQNO));

        }

        return "";
    }

    public int getHeaderCountForNnumVal(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHED + " WHERE "
                    + ValueHolder.FPRECDET_REFNO1 + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            count = cursor.getCount();

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

    @SuppressWarnings("static-access")
    public int deleteData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHED + " WHERE " + ValueHolder.FPRECDET_REFNO1
                    + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                int success = dB.delete(ValueHolder.TABLE_FPRECHED, ValueHolder.FPRECDET_REFNO1 + " ='" + refno + "'", null);
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

    public void UpdateRecHeadTotalAmount(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHED + " WHERE "
                    + ValueHolder.FPRECHED_REFNO1 + " = '" + refNo + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                String refno = cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO));

                String query = "Update " + ValueHolder.TABLE_FPRECHED
                        + " set TotalAmt = (select sum(AloAmt) Aloamt from fprecdet where refno='" + refno
                        + "' ) where refno='" + refno + "'";
                dB.execSQL(query);
            }


        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }

    public int DeleteHedUnnessary() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        int result = 0;
        int result2 = 0;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FPRECHED + " WHERE totalamt is null ";


            cursor = dB.rawQuery(selectQuery, null);

            cursor.moveToFirst();
            //int raws = cursor.getCount();

            //cursor.

            while (!cursor.isAfterLast()) {

                String refno = cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO));
                result = dB.delete(ValueHolder.TABLE_FPRECHED, ValueHolder.REFNO + "=?",
                        new String[]{refno});

                result2 = result2 + 1;

                cursor.moveToNext();
            }

//			while (cursor.moveToNext()) {
//				String refno = cursor.getString(cursor.getColumnIndex(dbHelper.REFNO));
//				
//				result = dB.delete(DatabaseHelper.TABLE_FPRECHED, DatabaseHelper.REFNO + "=?",
//						new String[] { refno });
//				result2=result2+1;
//				//String query = "Delete FROM " + DatabaseHelper.TABLE_FPRECHED + " where refno='" + refno + "'";
//				//dB.execSQL(query);
//			}


        }
//		try {
//
//				String query = "Delete from " + DatabaseHelper.TABLE_FPRECHED+ " where totalamt is null ";
//				dB.execSQL(query);						
//
//		}
        catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }
        return result2;
    }

    public int updateIsSyncedReceipt(String refno, String active, String sync,String stat) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();

            values.put(ValueHolder.FPRECHED_ISSYNCED, sync);
            values.put(ValueHolder.FPRECHED_ISACTIVE, active);
            values.put(ValueHolder.FPRECHED_STATUS, stat);

            count = dB.update(ValueHolder.TABLE_FPRECHEDS, values, ValueHolder.REFNO + " =?", new String[]{refno});

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

//    public int updateIsSyncedReceipt(ReceiptHed mapper) {
//
//        int count = 0;
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//        Cursor cursor = null;
//
//        try {
//            ContentValues values = new ContentValues();
//
//            values.put(FPRECHED_ISSYNCED, "1");
//
//            if (mapper.getFPRECHED_ISSYNCED().equals("0")) {
//                count = dB.update(TABLE_FPRECHED, values, dbHelper.REFNO + " =?",
//                        new String[]{String.valueOf(mapper.getFPRECHED_REFNO())});
//            }
//
//        } catch (Exception e) {
//
//            Log.v(TAG + " Exception", e.toString());
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            dB.close();
//        }
//        return count;
//
//    }

    public ArrayList<ReceiptHed> getAllUnsyncedRecHed() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();
        String selectQuery;

        try {
            selectQuery = "select * from " + ValueHolder.TABLE_FPRECHEDS + " Where "
                    + ValueHolder.FPRECHED_ISACTIVE + "='0' and " + ValueHolder.FPRECHED_ISSYNCED + "='0' and "
                    + ValueHolder.FPRECHED_ISDELETE + "='0'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                ReceiptHed mapper = new ReceiptHed();

                //     mapper.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.ReceiptNumVal)));

                mapper.setDistDB(SharedPref.getInstance(context).getDistDB().trim());
                mapper.setConsoleDB(SharedPref.getInstance(context).getDistDB().trim());

                mapper.setSALEREP_DEALCODE(new SalRepController(context).getDealCode());
                //    mapper.setSALEREP_AREACODE(new SalRepController(context).getAreaCode());

                mapper.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDDATE)));
                mapper.setFPRECHED_ADDMACH(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDMACH)));
                mapper.setFPRECHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDRESS)));
                mapper.setFPRECHED_ADDUSER(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER)));
                mapper.setFPRECHED_BTOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BTOTALAMT)));
                mapper.setFPRECHED_CHQDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE)));
                mapper.setFPRECHED_CHQNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQNO)));
                mapper.setFPRECHED_CURCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURCODE)));
                mapper.setFPRECHED_CURRATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE)));
                mapper.setFPRECHED_CURRATE1(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE1)));
                mapper.setFPRECHED_CUSBANK(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CUSBANK)));
                mapper.setFPRECHED_COSTCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_COST_CODE)));
                mapper.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                mapper.setFPRECHED_END_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_END_TIME)));
                mapper.setFPRECHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ID)));
                mapper.setFPRECHED_ISACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISACTIVE)));
                mapper.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISSYNCED)));
                mapper.setFPRECHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LATITUDE)));
                mapper.setFPRECHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LONGITUDE)));
                mapper.setFPRECHED_MANUREF(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_MANUREF)));
                mapper.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PAYTYPE)));
                mapper.setFPRECHED_PRTCOPY(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PRTCOPY)));
                mapper.setFPRECHED_RECORDID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_RECORDID)));
                mapper.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                mapper.setFPRECHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_REMARKS)));
                mapper.setFPRECHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                mapper.setFPRECHED_SALEREFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_SALEREFNO)));
                mapper.setFPRECHED_START_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_START_TIME)));
                mapper.setFPRECHED_TIMESTAMP(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TIMESTAMP)));
                mapper.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT)));
                mapper.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                mapper.setFPRECHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TXNTYPE)));
                mapper.setFPRECHED_BANKCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BANKCODE)));
                mapper.setFPRECHED_BRANCHCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BRANCHCODE)));
                mapper.setFPRECHED_ADDUSER_NEW(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER_NEW)));
                mapper.setFPRECHED_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_STATUS)));

                mapper.setRecDetList(new ReceiptDetController(context)
                        .GetReceiptByRefno(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));
                mapper.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.RecNumVal)));


                list.add(mapper);

            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return list;
    }

    public ArrayList<RecHed> getAllUnsyncedReceiptHed(String repCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<com.datamation.kfdupgradesfa.model.RecHed> list = new ArrayList<com.datamation.kfdupgradesfa.model.RecHed>();
        String selectQuery;

        try {
            selectQuery = "select * from " + ValueHolder.TABLE_FPRECHEDS + " Where "
                    + ValueHolder.FPRECHED_ISACTIVE + "='0' and " + ValueHolder.FPRECHED_ISSYNCED + "='0' and "
                    + ValueHolder.FPRECHED_ISDELETE + "='0' and " + ValueHolder.REPCODE + " = '" + repCode + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                RecHed mapper = new RecHed();

                mapper.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                mapper.setManuRef(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_MANUREF)));
                mapper.setSaleRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_SALEREFNO)));
                mapper.setRepCode(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                mapper.setTxnType(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TXNTYPE)));
                mapper.setChqno(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQNO)));
                if(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE)).equals("")){
                    mapper.setChqDate("1990-01-31T00:00Z");
                }else{
                    mapper.setChqDate(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE)));
                }
                mapper.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                mapper.setCurCode(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURCODE)));
                mapper.setCurRate(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE))));
                mapper.setDebCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                mapper.setTotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT))));
                mapper.setBtotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BTOTALAMT))));
                mapper.setPayType(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PAYTYPE)));
                mapper.setRemarks(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_REMARKS)));
                mapper.setAddDate(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDDATE)));
                mapper.setAddMach(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDMACH)));
                mapper.setAddress(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDRESS)));
                mapper.setAddUser(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER)));
                mapper.setCostCode(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_COST_CODE)));
                mapper.setRefNo2(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                mapper.setTxnCdate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                mapper.setBankCode(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BANKCODE)));
                mapper.setBranchCode(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BRANCHCODE)));
                mapper.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LATITUDE))));
                mapper.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LONGITUDE))));
                mapper.setStartTime(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_START_TIME)));
                mapper.setEndTime(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_END_TIME)));
                mapper.setEntryuser(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER_NEW)));
                mapper.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.RecNumVal)));
                mapper.setLoginId(new SalRepController(context).getCurrentRepCode());
                mapper.setPrtCopy(cursor.getDouble(cursor.getColumnIndex(ValueHolder.FPRECHED_PRTCOPY)));


                mapper.setReceiptDetList(new ReceiptDetController(context).GetReceiptDetByRefno(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)),cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE))));
                list.add(mapper);

            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return list;
    }

    public int getAllDayBeforeUnSyncRecHedCount(String repCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }


        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_FPRECHEDS + " Where IsSynced = 0 and " + ValueHolder.REPCODE + " = '" + repCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        Integer count = cursor.getCount();


        return count;
    }


    public ReceiptHed getActiveRecHed() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();
        String selectQuery;
        ReceiptHed mapper = new ReceiptHed();
        try {
            selectQuery = "select * from " + ValueHolder.TABLE_FPRECHEDS + " Where "
                    + ValueHolder.FPRECHED_ISACTIVE + "='1' and " + ValueHolder.FPRECHED_ISSYNCED + "='0'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

//                mapper.setNextNumVal(new ReferenceController(context)
//                        .getCurrentNextNumVal(context.getResources().getString(R.string.ReceiptNumVal)));

                mapper.setDistDB(SharedPref.getInstance(context).getDistDB().trim());
                mapper.setConsoleDB(SharedPref.getInstance(context).getConsoleDB().trim());

                mapper.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDDATE)));
                mapper.setFPRECHED_ADDMACH(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDMACH)));
                mapper.setFPRECHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDRESS)));
                mapper.setFPRECHED_ADDUSER(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER)));
                mapper.setFPRECHED_BTOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BTOTALAMT)));
                mapper.setFPRECHED_CHQDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE)));
                mapper.setFPRECHED_END_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_END_TIME)));
                mapper.setFPRECHED_START_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_START_TIME)));


                mapper.setRecDetList(new ReceiptDetController(context).GetReceiptByRefno(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));

            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return mapper;
    }

    public ArrayList<ReceiptHed> getAllNonActiveRecHeds(String fromDt, String toDt) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();
        String selectQuery;
        ReceiptHed mapper = new ReceiptHed();
        try {
            selectQuery = "select * from " + ValueHolder.TABLE_FPRECHEDS + " Where " + ValueHolder.FPRECHED_ISACTIVE + "='0' and (" + ValueHolder.FPRECHED_ADDDATE + " >= '" + fromDt + "' and " + ValueHolder.FPRECHED_ADDDATE + " <= '" + toDt + "')";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                //     mapper.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.ReceiptNumVal)));

                mapper.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                mapper.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDDATE)));
                mapper.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                mapper.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT)));

                mapper.setRecDetList(new ReceiptDetController(context).GetReceiptByRefno(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));
                list.add(mapper);
            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return list;
    }

    //   *%*%*%*%*%*%*%%%*%*%* MMS 2022/02/11 *%*%%*%*%*%*%*%*%*//
    public ArrayList<ReceiptHed> getTodayReceipts(String repCode) {

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();

        try {
            String selectQuery = "select DebCode, RefNo, isSynced, TxnDate, TotalAmt,PayType,Status from TblRecHedS "
                    + "  where  isActive = '" + "0" + "' and " + ValueHolder.REPCODE + " = '" + repCode + "'" +
//                    + "  where txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "' and isActive = '" + "0" + "'" +
                    "ORDER BY Id DESC";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                ReceiptHed recHed = new ReceiptHed();

                recHed.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                recHed.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                recHed.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISSYNCED)));
                recHed.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PAYTYPE)));
                recHed.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                recHed.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT)));
                recHed.setFPRECHED_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));

                list.add(recHed);
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;

    }

    //   *%*%*%*%*%*%*%%%*%*%* MMS 2022/05/19 *%*%%*%*%*%*%*%*%*//
    public ArrayList<ReceiptHed> getTodayReceiptsBySearch(String key, String repCode) {

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();

        try {
            String selectQuery = "select DebCode, RefNo, isSynced, TxnDate, TotalAmt,PayType from TblRecHedS "
                    + "  where txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "' and isActive = '" + "0" + "' AND " + ValueHolder.REPCODE + " = '" + repCode + "' AND RefNo LIKE '%" + key + "%' ORDER BY Id DESC";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                ReceiptHed recHed = new ReceiptHed();

                recHed.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                recHed.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                recHed.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISSYNCED)));
                recHed.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PAYTYPE)));
                recHed.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                recHed.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT)));

                list.add(recHed);
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;

    }


    //   *%*%*%*%*%*%*%%%*%*%* MMS 2022/05/17 *%*%%*%*%*%*%*%*%*//
    public ArrayList<RecHed> getAllNotIssuedReceipts() {

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<RecHed> list = new ArrayList<RecHed>();

        try {
            String selectQuery = "select * from TblRecHedS "
                    + "  where Status NOT LIKE 'PAID' ORDER BY Id DESC";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                RecHed mapper = new RecHed();

                mapper.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                mapper.setManuRef(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_MANUREF)));
                mapper.setSaleRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_SALEREFNO)));
                mapper.setRepCode(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                mapper.setTxnType(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TXNTYPE)));
                mapper.setChqno(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQNO)));
                if(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE)).equals("")){
                    mapper.setChqDate("1990-01-31T00:00Z");
                }else{
                    mapper.setChqDate(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE)));
                }
                mapper.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                mapper.setCurCode(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURCODE)));
                mapper.setCurRate(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE))));
                mapper.setDebCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                mapper.setTotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT))));
                mapper.setBtotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BTOTALAMT))));
                mapper.setPayType(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PAYTYPE)));
                mapper.setRemarks(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_REMARKS)));
                mapper.setAddDate(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDDATE)));
                mapper.setAddMach(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDMACH)));
                mapper.setAddress(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDRESS)));
                mapper.setAddUser(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER)));
                mapper.setCostCode(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_COST_CODE)));
                mapper.setRefNo2(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                mapper.setTxnCdate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                mapper.setBankCode(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BANKCODE)));
                mapper.setBranchCode(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BRANCHCODE)));
                mapper.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LATITUDE))));
                mapper.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LONGITUDE))));
                mapper.setStartTime(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_START_TIME)));
                mapper.setEndTime(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_END_TIME)));
                mapper.setEntryuser(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER_NEW)));
                mapper.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.RecNumVal)));
                mapper.setLoginId(new SalRepController(context).getCurrentRepCode());
                mapper.setPrtCopy(cursor.getDouble(cursor.getColumnIndex(ValueHolder.FPRECHED_PRTCOPY)));
                mapper.setStatus(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_STATUS)));


                mapper.setReceiptDetList(new ReceiptDetController(context).GetReceiptDetByRefno(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)),cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE))));

                list.add(mapper);
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;

    }

//    public ArrayList<ReceiptHed> getAllNotIssuedReceipts() {
//
//        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
//        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
//        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//        Cursor cursor = null;
//        ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();
//
//        try {
//            String selectQuery = "select * from TblRecHedS "
//                    + "  where Status NOT LIKE 'PAID' ORDER BY Id DESC";
//
//            cursor = dB.rawQuery(selectQuery, null);
//
//            while (cursor.moveToNext()) {
//
//                ReceiptHed mapper = new ReceiptHed();
//
//                mapper.setDistDB(SharedPref.getInstance(context).getDistDB().trim());
//                mapper.setConsoleDB(SharedPref.getInstance(context).getDistDB().trim());
//
//                mapper.setSALEREP_DEALCODE(new SalRepController(context).getDealCode());
//
//                mapper.setFPRECHED_ADDDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDDATE)));
//                mapper.setFPRECHED_ADDMACH(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDMACH)));
//                mapper.setFPRECHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDRESS)));
//                mapper.setFPRECHED_ADDUSER(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER)));
//                mapper.setFPRECHED_BTOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BTOTALAMT)));
//                mapper.setFPRECHED_CHQDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQDATE)));
//                mapper.setFPRECHED_CHQNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CHQNO)));
//                mapper.setFPRECHED_CURCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURCODE)));
//                mapper.setFPRECHED_CURRATE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE)));
//                mapper.setFPRECHED_CURRATE1(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CURRATE1)));
//                mapper.setFPRECHED_CUSBANK(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_CUSBANK)));
//                mapper.setFPRECHED_COSTCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_COST_CODE)));
//                mapper.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
//                mapper.setFPRECHED_END_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_END_TIME)));
//                mapper.setFPRECHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ID)));
//                mapper.setFPRECHED_ISACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISACTIVE)));
//                mapper.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISSYNCED)));
//                mapper.setFPRECHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LATITUDE)));
//                mapper.setFPRECHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_LONGITUDE)));
//                mapper.setFPRECHED_MANUREF(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_MANUREF)));
//                mapper.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PAYTYPE)));
//                mapper.setFPRECHED_PRTCOPY(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PRTCOPY)));
//                mapper.setFPRECHED_RECORDID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_RECORDID)));
//                mapper.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
//                mapper.setFPRECHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_REMARKS)));
//                mapper.setFPRECHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
//                mapper.setFPRECHED_SALEREFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_SALEREFNO)));
//                mapper.setFPRECHED_START_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_START_TIME)));
//                mapper.setFPRECHED_TIMESTAMP(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TIMESTAMP)));
//                mapper.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT)));
//                mapper.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
//                mapper.setFPRECHED_TXNTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TXNTYPE)));
//                mapper.setFPRECHED_BANKCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BANKCODE)));
//                mapper.setFPRECHED_BRANCHCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_BRANCHCODE)));
//                mapper.setFPRECHED_ADDUSER_NEW(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ADDUSER_NEW)));
//                mapper.setFPRECHED_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_STATUS)));
//
//                mapper.setRecDetList(new ReceiptDetController(context)
//                        .GetReceiptByRefno(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));
//                mapper.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.RecNumVal)));
//                mapper.setFPRECHED_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_STATUS)));
//
//
//                mapper.setRecDetList(new ReceiptDetController(context).GetReceiptByRefno(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));
//
//                list.add(mapper);
//            }
//
//        } catch (Exception e) {
//
//            Log.v(TAG + " Exception", e.toString());
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            dB.close();
//        }
//
//        return list;
//
//    }

    //   *%*%*%*%*%*%*%%%*%*%* MMS 2022/02/11 *%*%%*%*%*%*%*%*%*//
    public ArrayList<ReceiptHed> getReceiptsByDate(String from, String to) {


        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<ReceiptHed> list = new ArrayList<ReceiptHed>();

        try {
            String selectQuery = "select DebCode, RefNo, isSynced, TxnDate, TotalAmt,PayType,Status from TblRecHedS "
                    + "  where txndate between '" + from + "' and " + " '" + to + "' ORDER BY Id DESC";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                ReceiptHed recHed = new ReceiptHed();

                recHed.setFPRECHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                recHed.setFPRECHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                recHed.setFPRECHED_ISSYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_ISSYNCED)));
                recHed.setFPRECHED_PAYTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_PAYTYPE)));
                recHed.setFPRECHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                recHed.setFPRECHED_TOTALAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_TOTALAMT)));
                recHed.setFPRECHED_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRECHED_STATUS)));

                list.add(recHed);
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;

    }

}
