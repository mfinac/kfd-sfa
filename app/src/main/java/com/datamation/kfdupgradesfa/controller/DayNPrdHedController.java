package com.datamation.kfdupgradesfa.controller;

import android.annotation.SuppressLint;
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
import com.datamation.kfdupgradesfa.model.DayNPrdHed;
import com.datamation.kfdupgradesfa.model.NonPrdHed;
import com.datamation.kfdupgradesfa.model.ReceiptHed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
       Modification by kaveesha - 08-12-2021
 */


public class DayNPrdHedController {
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "DayNPrdHedController";

    public DayNPrdHedController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateNonPrdHed(ArrayList<DayNPrdHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (DayNPrdHed nonhed : list) {
                ContentValues values = new ContentValues();

                values.put(ValueHolder.REFNO, nonhed.getNONPRDHED_REFNO());
                values.put(ValueHolder.TXNDATE, nonhed.getNONPRDHED_TXNDATE());
                values.put(ValueHolder.REPCODE, nonhed.getNONPRDHED_REPCODE());
                values.put(ValueHolder.NONPRDHED_REMARK, nonhed.getNONPRDHED_REMARKS());
                values.put(ValueHolder.NONPRDHED_ADDDATE, nonhed.getNONPRDHED_ADDDATE());
                values.put(ValueHolder.NONPRDHED_COSTCODE, nonhed.getNONPRDHED_COSTCODE());
                values.put(ValueHolder.NONPRDHED_ADDUSER, nonhed.getNONPRDHED_ADDUSER());
                values.put(ValueHolder.NONPRDHED_IS_SYNCED, nonhed.getNONPRDHED_IS_SYNCED());
                values.put(ValueHolder.NONPRDHED_LONGITUDE, nonhed.getNONPRDHED_LONGITUDE());
                values.put(ValueHolder.NONPRDHED_LATITUDE, nonhed.getNONPRDHED_LATITUDE());
                values.put(ValueHolder.DEBCODE, nonhed.getNONPRDHED_DEBCODE());
                values.put(ValueHolder.NONPRDHED_IS_ACTIVE, nonhed.getNONPRDHED_IS_ACTIVE());
                values.put(ValueHolder.NONPRDHED_ADDMACH, nonhed.getNONPRDHED_ADDMACH());
                values.put(ValueHolder.NONPRDHED_ADDRESS, nonhed.getNONPRDHED_ADDRESS());
                values.put(ValueHolder.START_TIME_SO, nonhed.getNONPRDHED_STARTTIME());
                values.put(ValueHolder.END_TIME_SO, nonhed.getNONPRDHED_ENDTIME());

                count = (int) dB.insert(ValueHolder.TABLE_NONPRDHED, null, values);

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

    public int updateIsSynced(String refno, String res) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();


            //if (hed.getORDER_IS_SYNCED().equals("1")) {
            values.put(ValueHolder.NONPRDHED_IS_SYNCED, res);
            count = dB.update(ValueHolder.TABLE_NONPRDHED, values, ValueHolder.REFNO + " =?", new String[]{refno});
//            }else{
//                values.put(FORDHED_IS_SYNCED, "0");
//                count = dB.update(TABLE_FORDHED, values, REFNO + " =?", new String[] { String.valueOf(hed.getORDER_REFNO()) });
//            }

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

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    //   *%*%*%*%*%*%*%%%*%*%* MMS 2022/02/15 *%*%%*%*%*%*%*%*%*//
    public ArrayList<DayNPrdHed> getNPHedsByDate(String from,String to) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<DayNPrdHed> list = new ArrayList<DayNPrdHed>();

        try {
            String selectQuery = "select DebCode, RefNo, TxnDate, Reason, ISsync from "+ ValueHolder.TABLE_NONPRDHED  + "  where txndate between '" + from + "' and " +" '" + to + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                DayNPrdHed npHed = new DayNPrdHed();
//
                npHed.setNONPRDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                npHed.setNONPRDHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                npHed.setNONPRDHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_TXNDAET)));
                npHed.setNONPRDHED_REASON(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_REASON)));
                npHed.setNONPRDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_IS_SYNCED)));
                //TODO :set  discount, free

                list.add(npHed);
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

    public ArrayList<DayNPrdHed> getTodayNPHeds() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<DayNPrdHed> list = new ArrayList<DayNPrdHed>();

        try {
            String selectQuery = "select DebCode, RefNo, TxnDate, Reason, ISsync from "+ ValueHolder.TABLE_NONPRDHED+ "  where TxnDate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                DayNPrdHed npHed = new DayNPrdHed();
//
                npHed.setNONPRDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                npHed.setNONPRDHED_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                npHed.setNONPRDHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_TXNDAET)));
                npHed.setNONPRDHED_REASON(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_REASON)));
                npHed.setNONPRDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_IS_SYNCED)));
                //TODO :set  discount, free

                list.add(npHed);
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


//    public boolean validateActiveNonPrd()
//    {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        boolean res = false;
//
//        String toDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        Cursor cursor = null;
//        try {
//            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_NONPRDHED + " WHERE " + NONPRDHED_IS_ACTIVE+ "='1'";
//            cursor = dB.rawQuery(selectQuery, null);
//
//            /*Active invoice available*/
//            if (cursor.getCount() > 0) {
//                cursor.moveToFirst();
//
//                String txndate = cursor.getString(cursor.getColumnIndex(NONPRDHED_TXNDATE));
//
//                /*txndate is equal to current date*/
//                if (txndate.equals(toDay))
//                    res = true;
//                /*if invoice is older, then reset data*/
//                else {
//                    String Refno = cursor.getString(cursor.getColumnIndex(ValueHolder.FTRANSOHED_REFNO));
//                    restData(Refno);
////                    new InvDetDS(context).restData(Refno);
////                    new OrderDiscDS(context).clearData(Refno);
////                    new OrdFreeIssueDS(context).ClearFreeIssues(Refno);
//                    UtilityContainer.ClearNonSharedPref(context);
//                }
//
//            } else
//                res = false;
//
//        } catch (Exception e) {
//            Log.v(TAG, e.toString());
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            dB.close();
//        }
//
//        return res;
//
//    }

    /*-*-*-*--*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*--*-*-*-*-*-*-*/

    /**
     * -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
     * *-*-*-*-*-*-*-*-*-*-*-*-
     */

    public boolean restData(String refno) {

        boolean Result = false;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_NONPRDHED + " WHERE " + ValueHolder.REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String status = cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_IS_SYNCED));
                /* if order already synced, can't delete */
                if (status.equals("1"))
                    Result = false;
                else {
                    int success = dB.delete(ValueHolder.TABLE_NONPRDHED, ValueHolder.REFNO + " ='" + refno + "'", null);
                    Log.v("Success", success + "");
                    Result = true;
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
        return Result;

    }
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public ArrayList<DayNPrdHed> getAllnonprdHedDetails(String newText) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayNPrdHed> list = new ArrayList<DayNPrdHed>();

        String selectQuery = "select * from " + ValueHolder.TABLE_NONPRDHED + " WHERE AddDate LIKE '%" + newText + "%'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            DayNPrdHed fnonset = new DayNPrdHed();
            fnonset.setNONPRDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            fnonset.setNONPRDHED_ADDDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_ADDDATE)));
            fnonset.setNONPRDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_IS_SYNCED)));

            list.add(fnonset);

        }

        return list;
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    @SuppressWarnings("static-access")
    public int undoOrdHedByID(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_NONPRDHED + " WHERE " + ValueHolder.REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_NONPRDHED, ValueHolder.REFNO + "='" + RefNo + "'", null);

            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return count;

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

//    public ArrayList<NonProdMapper> getUnSyncedData() {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<NonProdMapper> list = new ArrayList<NonProdMapper>();
//
//        try {
//
//            String selectQuery = "SELECT * FROM " + TABLE_NONPRDHED + " WHERE " + NONPRDHED_IS_SYNCED + "='0'";
//            Cursor cursor = dB.rawQuery(selectQuery, null);
//            localSP = context.getSharedPreferences(SETTINGS, 0);
//
//            while (cursor.moveToNext()) {
//
//                NonProdMapper mapper = new NonProdMapper();
//                mapper.setNextNumVal(new CompanyBranchDS(context).getCurrentNextNumVal(context.getResources().getString(R.string.NonProd)));
//                mapper.setConsoleDB(localSP.getString("Console_DB", "").toString());
//
//                mapper.setNONPRDHED_ADDDATE(cursor.getString(cursor.getColumnIndex(NONPRDHED_ADDDATE)));
//                mapper.setNONPRDHED_ADDMACH(cursor.getString(cursor.getColumnIndex(NONPRDHED_ADDMACH)));
//                mapper.setNONPRDHED_ADDRESS(cursor.getString(cursor.getColumnIndex(NONPRDHED_ADDRESS)));
//                mapper.setNONPRDHED_ADDUSER(cursor.getString(cursor.getColumnIndex(NONPRDHED_ADDUSER)));
//                mapper.setNONPRDHED_COSTCODE("000");
//                // mapper.setNONPRDHED_DEALCODE(cursor.getString(cursor.getColumnIndex(NONPRDHED_DEALCODE)));
//                mapper.setNONPRDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(NONPRDHED_IS_SYNCED)));
//                mapper.setREFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
//                mapper.setNONPRDHED_REMARK(cursor.getString(cursor.getColumnIndex(NONPRDHED_REMARK)));
//                mapper.setNONPRDHED_REPCODE(cursor.getString(cursor.getColumnIndex(NONPRDHED_REPCODE)));
//                mapper.setNONPRDHED_TRANSBATCH(cursor.getString(cursor.getColumnIndex(NONPRDHED_TRANSBATCH)));
//                mapper.setNONPRDHED_TXNDATE(cursor.getString(cursor.getColumnIndex(NONPRDHED_TXNDATE)));
//                mapper.setNONPRDHED_DEBCODE(cursor.getString(cursor.getColumnIndex(NONPRDHED_DEBCODE)));
//                mapper.setNONPRDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(NONPRDHED_LONGITUDE)));
//                mapper.setNONPRDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(NONPRDHED_LATITUDE)));
//                mapper.setNonPrdDet(new DayNPrdDetController(context).getAllnonprdDetails(mapper.getREFNO()));
//
//                list.add(mapper);
//            }
//
//        } catch (Exception e) {
//            Log.v(TAG + " Exception", e.toString());
//        } finally {
//            dB.close();
//        }
//
//        return list;
//
//    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

//    public int updateIsSynced(NonProdMapper mapper) {
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
//            values.put(NONPRDHED_IS_SYNCED, "1");
//            if (mapper.isSynced()) {
//                count = dB.update(TABLE_NONPRDHED, values, ValueHolder.REFNO + " =?", new String[]{String.valueOf(mapper.getREFNO())});
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

    public boolean isEntrySynced(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select ISsync from FDaynPrdHed where refno ='" + Refno + "'", null);

        while (cursor.moveToNext()) {

            String result = cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_IS_SYNCED));

            if (result.equals("1"))
                return true;

        }
        cursor.close();
        dB.close();
        return false;

    }


    public ArrayList<NonPrdHed> getUnSyncedData(String repCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<NonPrdHed> list = new ArrayList<NonPrdHed>();

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_NONPRDHED + " WHERE " + ValueHolder.NONPRDHED_IS_SYNCED + "='0' and " + ValueHolder.REPCODE + " = '" + repCode + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);
//            localSP = context.getSharedPreferences(SETTINGS, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
            localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);

            while (cursor.moveToNext()) {

                NonPrdHed mapper = new NonPrdHed();

                mapper.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_REFNO)));
                mapper.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_TXNDAET)));
                mapper.setRepCode(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                mapper.setRemarks(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_REMARK)));
                mapper.setCostCode(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_COSTCODE)));
                mapper.setAddUser(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_ADDUSER)));
                mapper.setAddDate(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_ADDDATE)));
                mapper.setDebCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                mapper.setLongitude(cursor.getDouble(cursor.getColumnIndex(ValueHolder.NONPRDHED_LONGITUDE)));
                mapper.setLatitude(cursor.getDouble(cursor.getColumnIndex(ValueHolder.NONPRDHED_LATITUDE)));
                mapper.setAddress(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_ADDRESS)));
                mapper.setAddMach(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_ADDMACH)));
                mapper.setStartTime(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
                mapper.setEndTime(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
                mapper.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.NonProd)));

                mapper.setLoginId(new SalRepController(context).getCurrentRepCode());

                mapper.setNonPrdDetList(new DayNPrdDetController(context).getAllnonprdDetails(mapper.getRefNo(), mapper.getDebCode()));

                list.add(mapper);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;

    }

    public int getAllDayBeforeUnSyncNonPrdCount(String repCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }


        @SuppressWarnings("static-access")
        //String selectQuery = "select * from " + ValueHolder.TABLE_NONPRDHED + " Where ISsync=1 and ISActive=0 and " + ValueHolder.REPCODE + " = '" + repCode + "'";
                //IsActive status not updaing or using (ISsync = '0' means not uploaded)
        String selectQuery = "select * from " + ValueHolder.TABLE_NONPRDHED + " Where ISsync=0 and " + ValueHolder.REPCODE + " = '" + repCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        Integer count = cursor.getCount();


        return count;
    }

    public int updateIsSynced(NonPrdHed hed) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();

            values.put(ValueHolder.NONPRDHED_IS_SYNCED, "1");

            if (hed.getIsSync().equals("1")) {
                count = dB.update(ValueHolder.TABLE_NONPRDHED, values, ValueHolder.REFNO + " =?", new String[]{String.valueOf(hed.getRefNo())});
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

    public int IsSyncedOrder(String repCode) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;


        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_NONPRDHED + " WHERE " + ValueHolder.NONPRDDET_IS_SYNCED + " = '0' and " + ValueHolder.REPCODE + " = '" + repCode + "' ";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            int cn = cursor.getCount();
            count = cn;

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

    @SuppressLint("Range")
    public NonPrdHed getActiveNP() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery;
        NonPrdHed nonPrdHed = new NonPrdHed();
        try {
            selectQuery = "select * from " + ValueHolder.TABLE_NONPRDHED + " Where "
                    + ValueHolder.NONPRDHED_IS_SYNCED + "='0' and " + ValueHolder.NONPRDHED_IS_ACTIVE + "='1'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                nonPrdHed.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.NONPRDHED_REFNO)));

            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return nonPrdHed;
    }

    public int deleteActiveNPHed(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_NONPRDHED + " WHERE " + ValueHolder.REFNO
                    + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(ValueHolder.TABLE_NONPRDHED, ValueHolder.REFNO + " ='" + refno + "'", null);
                Log.v("Success", count + "");
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

    @SuppressWarnings("static-access")
    public int deleteActiveNPDet(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_NONPRDDET + " WHERE " + ValueHolder.REFNO
                    + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(ValueHolder.TABLE_NONPRDDET, ValueHolder.REFNO + " ='" + refno + "'", null);
                Log.v("Success", count + "");
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
