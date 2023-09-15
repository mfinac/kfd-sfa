package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.DayNPrdDet;
import com.datamation.kfdupgradesfa.model.NonPrdDet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DayNPrdDetController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "Swadeshi";


    public DayNPrdDetController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateNonPrdDet(ArrayList<DayNPrdDet> list) {

        int serverdbID = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (DayNPrdDet nondet : list) {
                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_NONPRDDET + " WHERE " + ValueHolder.REFNO + " = '" + nondet.getNONPRDDET_REFNO() + "'"
                        //;
                        + " AND " + ValueHolder.NONPRDDET_REASON_CODE + " = '" + nondet.getNONPRDDET_REASON_CODE() + "'";
                cursor = dB.rawQuery(selectQuery, null);

                values.put(ValueHolder.REFNO, nondet.getNONPRDDET_REFNO());
                values.put(ValueHolder.NONPRDDET_REASON, nondet.getNONPRDDET_REASON());
                values.put(ValueHolder.NONPRDDET_REASON_CODE, nondet.getNONPRDDET_REASON_CODE());
                values.put(ValueHolder.NONPRDDET_TXNDATE, nondet.getNONPRDDET_TXNDATE());
                values.put(ValueHolder.REPCODE, nondet.getNONPRDDET_REPCODE());
                values.put(ValueHolder.NONPRDDET_REMARK, nondet.getNONPRDDET_REMARK());

                int count = cursor.getCount();
                if (count > 0) {
                    serverdbID = (int) dB.update(ValueHolder.TABLE_NONPRDDET, values, ValueHolder.NONPRDDET_REASON_CODE + " =?", new String[]{String.valueOf(nondet.getNONPRDDET_REFNO())});

                } else {
                    serverdbID = (int) dB.insert(ValueHolder.TABLE_NONPRDDET, null, values);
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
        return serverdbID;

    }

    public ArrayList<DayNPrdDet> getTodayNPDets(String refno) {

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayNPrdDet> list = new ArrayList<DayNPrdDet>();
        String selectQuery = "select * from "+ValueHolder.TABLE_NONPRDDET+" WHERE " + ValueHolder.REFNO + "='" + refno + "' and  TxnDate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) +"'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                DayNPrdDet npDet = new DayNPrdDet();

                npDet.setNONPRDDET_REASON(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON)));
                npDet.setNONPRDDET_REASON_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON_CODE)));
                npDet.setNONPRDDET_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                npDet.setNONPRDDET_REMARK(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REMARK)));

                list.add(npDet);

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

    //   *%*%*%*%*%*%*%%%*%*%* MMS 2022/02/15 *%*%%*%*%*%*%*%*%*//
    public ArrayList<DayNPrdDet> getTodayNPDets(String refno,String from, String to) {


        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "";

        ArrayList<DayNPrdDet> list = new ArrayList<DayNPrdDet>();

        if (from.equals("") && to.equals("")){
            selectQuery = "select * from "+ValueHolder.TABLE_NONPRDDET+" WHERE " + ValueHolder.REFNO + "='" + refno + "'";

        }else {
            selectQuery = "select * from "+ValueHolder.TABLE_NONPRDDET+" WHERE " + ValueHolder.REFNO + "='" + refno + "' and  TxnDate between '" + from + "' and " +" '" + to + "'";

        }

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                DayNPrdDet npDet = new DayNPrdDet();

                npDet.setNONPRDDET_REASON(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON)));
                npDet.setNONPRDDET_REASON_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON_CODE)));
                npDet.setNONPRDDET_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                npDet.setNONPRDDET_REMARK(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REMARK)));

                list.add(npDet);

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
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public ArrayList<NonPrdDet> getAllnonprdDetails(String refno, String debCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<NonPrdDet> list = new ArrayList<NonPrdDet>();

        String selectQuery = "select * from " + ValueHolder.TABLE_NONPRDDET + " WHERE " + ValueHolder.REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            NonPrdDet fnonset = new NonPrdDet();

            fnonset.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_TXNDATE)));
            fnonset.setReason(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON)));
            fnonset.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            fnonset.setReasonCode(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON_CODE)));
            fnonset.setDebCode(debCode);

            list.add(fnonset);
        }

        return list;
    }

    public ArrayList<DayNPrdDet> getAllnonprdDetails(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayNPrdDet> list = new ArrayList<DayNPrdDet>();

        String selectQuery = "select * from " + ValueHolder.TABLE_NONPRDDET + " WHERE " + ValueHolder.REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            DayNPrdDet fnonset = new DayNPrdDet();

            fnonset.setNONPRDDET_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_TXNDATE)));
            fnonset.setNONPRDDET_REASON(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON)));
            fnonset.setNONPRDDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            fnonset.setNONPRDDET_REASON_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON_CODE)));

            list.add(fnonset);
        }

        return list;
    }
	
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

//    public String getDuplicate(String code, String RefNo) {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        String selectQuery = "SELECT * FROM " + TABLE_NONPRDDET + " WHERE " + NONPRDDET_DEBCODE + "='" + code + "' AND " + ValueHolder.REFNO + "='" + RefNo + "'";
//
//        Cursor cursor = dB.rawQuery(selectQuery, null);
//
//        while (cursor.moveToNext()) {
//            return cursor.getString(cursor.getColumnIndex(NONPRDDET_DEBCODE));
//        }
//
//        return "";
//    }
	
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public int deleteOrdDetByID(String refNo, String ReasonCode) {

        int count = 0;
        int success = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_NONPRDDET + " WHERE " + ValueHolder.REFNO + "='" + refNo + "'" + " AND " + ValueHolder.NONPRDDET_REASON_CODE + " ='" + ReasonCode + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                success = dB.delete(ValueHolder.TABLE_NONPRDDET, ValueHolder.REFNO + "='" + refNo + "'" + " AND " + ValueHolder.NONPRDDET_REASON_CODE + " ='" + ReasonCode + "'", null);
                Log.v("FNONPRO_DET Deleted ", success + "");
            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return success;

    }

    public int deleteOrdDetByRefNo(String id) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_NONPRDDET + " WHERE " + ValueHolder.REFNO + "='" + id + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_NONPRDDET, ValueHolder.REFNO + "='" + id + "'", null);
                Log.v("FtranDet Deleted ", success + "");
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
	
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public int OrdDetByRefno(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_NONPRDDET + " WHERE " + ValueHolder.REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_NONPRDDET, ValueHolder.REFNO + "='" + RefNo + "'", null);
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
    public int getNonProdCount(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            String selectQuery = "SELECT count(RefNo) as RefNo FROM " + ValueHolder.TABLE_NONPRDDET +  " WHERE  " + ValueHolder.REFNO + "='" + refNo + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                return Integer.parseInt(cursor.getString(cursor.getColumnIndex("RefNo")));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }
        return 0;

    }

    public ArrayList<DayNPrdDet> getAllActiveNPs() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayNPrdDet> list = new ArrayList<DayNPrdDet>();

        String selectQuery = "select * from " + ValueHolder.TABLE_NONPRDDET + " WHERE " + ValueHolder.NONPRDDET_IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                DayNPrdDet ordDet = new DayNPrdDet();

                ordDet.setNONPRDDET_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_ID)));
                ordDet.setNONPRDDET_REASON(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON)));
                ordDet.setNONPRDDET_REASON_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON_CODE)));
                ordDet.setNONPRDDET_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                ordDet.setNONPRDDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REFNO)));

                list.add(ordDet);

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

    public DayNPrdDet getActiveNPRefNo()
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        DayNPrdDet ordDet = new DayNPrdDet();

        String selectQuery = "select * from " + ValueHolder.TABLE_NONPRDDET + " WHERE " + ValueHolder.NONPRDDET_IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                ordDet.setNONPRDDET_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_ID)));
                ordDet.setNONPRDDET_REASON(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON)));
                ordDet.setNONPRDDET_REASON_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REASON_CODE)));
                ordDet.setNONPRDDET_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                ordDet.setNONPRDDET_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDDET_REFNO)));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return ordDet;
    }

    public boolean isAnyActiveNPs()
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "select * from " + ValueHolder.TABLE_NONPRDHED + " WHERE " + ValueHolder.NONPRDHED_IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            if (cursor.getCount()>0)
            {
                return true;
            }
            else
            {
                return false;
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return false;
    }
}
