package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.OrderHed;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class OrderController {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "OrderController";

    // Shared Preferences variables
    public static final String SETTINGS = "SETTINGS";

    public static final String REFNO = "RefNo";
    public static final String DEBCODE = "DebCode";
    public static final String FORDHED_IS_SYNC = "IsSync";
    public static final String TXNDATE = "TxnDate";
    public static final String FORDHED_TOTAL_AMT = "TotalAmt";


    // rashmi - 2019-12-17 *******************************************************************************
    public OrderController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateOrdHed(ArrayList<Order> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (Order ordHed : list) {

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.REFNO
                        + " = '" + ordHed.getFORDHED_REFNO() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                values.put(ValueHolder.REFNO, ordHed.getFORDHED_REFNO());
                values.put(ValueHolder.ORDERID, ordHed.getOrderId());
                values.put(ValueHolder.ADDDATE, ordHed.getFORDHED_ADD_DATE());
                values.put(ValueHolder.ADDMACH, ordHed.getFORDHED_ADD_MACH());
                values.put(ValueHolder.ADDUSER, ordHed.getFORDHED_ADD_USER());
                values.put(ValueHolder.APP_DATE, ordHed.getFORDHED_APP_DATE());
                values.put(ValueHolder.APPSTS, ordHed.getFORDHED_APPSTS());
                values.put(ValueHolder.APP_USER, ordHed.getFORDHED_APP_USER());
                values.put(ValueHolder.BP_TOTAL_DIS, ordHed.getFORDHED_BP_TOTAL_DIS());
                values.put(ValueHolder.B_TOTAL_AMT, ordHed.getFORDHED_B_TOTAL_AMT());
                values.put(ValueHolder.B_TOTAL_DIS, ordHed.getFORDHED_B_TOTAL_DIS());
                values.put(ValueHolder.B_TOTAL_TAX, ordHed.getFORDHED_B_TOTAL_TAX());
                values.put(ValueHolder.COSTCODE, ordHed.getFORDHED_COST_CODE());
                values.put(ValueHolder.CUR_CODE, ordHed.getFORDHED_CUR_CODE());
                values.put(ValueHolder.CUR_RATE, ordHed.getFORDHED_CUR_RATE());
                values.put(ValueHolder.DEBCODE, ordHed.getFORDHED_DEB_CODE());
                values.put(ValueHolder.DISPER, ordHed.getFORDHED_DIS_PER());
                values.put(ValueHolder.START_TIME_SO, ordHed.getFORDHED_START_TIME_SO());
                values.put(ValueHolder.END_TIME_SO, ordHed.getFORDHED_END_TIME_SO());
                values.put(ValueHolder.LONGITUDE, ordHed.getFORDHED_LONGITUDE());
                values.put(ValueHolder.LATITUDE, ordHed.getFORDHED_LATITUDE());
                values.put(ValueHolder.LOCCODE, ordHed.getFORDHED_LOC_CODE());
                values.put(ValueHolder.MANU_REF, ordHed.getFORDHED_MANU_REF());
                values.put(ValueHolder.APPVERSION, ordHed.getFORDHED_RECORD_ID());
                values.put(ValueHolder.REMARKS, ordHed.getFORDHED_REMARKS());
                values.put(ValueHolder.REPCODE, ordHed.getFORDHED_REPCODE());
                values.put(ValueHolder.TAX_REG, ordHed.getFORDHED_TAX_REG());
                values.put(ValueHolder.TOTALAMT, ordHed.getFORDHED_TOTAL_AMT());
                values.put(ValueHolder.TOTALDIS, ordHed.getFORDHED_TOTALDIS());
                values.put(ValueHolder.TOTAL_TAX, ordHed.getFORDHED_TOTAL_TAX());
                values.put(ValueHolder.TXNTYPE, ordHed.getFORDHED_TXN_TYPE());
                values.put(ValueHolder.TXNDATE, ordHed.getFORDHED_TXN_DATE());
                values.put(ValueHolder.ADDRESS, ordHed.getFORDHED_ADDRESS());
                values.put(ValueHolder.TOTAL_ITM_DIS, ordHed.getFORDHED_TOTAL_ITM_DIS());
                values.put(ValueHolder.TOT_MKR_AMT, ordHed.getFORDHED_TOT_MKR_AMT());
                values.put(ValueHolder.DELV_DATE, ordHed.getFORDHED_DELV_DATE());
                values.put(ValueHolder.ROUTECODE, ordHed.getFORDHED_ROUTE_CODE());
                values.put(ValueHolder.DIS_VAL, ordHed.getFORDHED_HED_DIS_VAL());
                values.put(ValueHolder.DIS_PER_VAL, ordHed.getFORDHED_HED_DIS_PER_VAL());
                values.put(ValueHolder.IS_SYNC, ordHed.getFORDHED_IS_SYNCED());
                values.put(ValueHolder.IS_ACTIVE, ordHed.getFORDHED_IS_ACTIVE());
                values.put(ValueHolder.PAYMENT_TYPE, ordHed.getFORDHED_PAYMENT_TYPE());
                values.put(ValueHolder.STATUS, ordHed.getFORDHED_STATUS());
                values.put(ValueHolder.CUSADD1, ordHed.getFORDHED_CUSADD1());
                values.put(ValueHolder.CUSADD2, ordHed.getFORDHED_CUSADD2());
                values.put(ValueHolder.CUSADD3, ordHed.getFORDHED_CUSADD3());
                values.put(ValueHolder.CUSMOB, ordHed.getFORDHED_CONTACT());
                values.put(ValueHolder.CUSTELE, ordHed.getFORDHED_CUSTELE());

                int cn = cursor.getCount();
                if (cn > 0) {
                    count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.REFNO + " =?", new String[]{String.valueOf(ordHed.getFORDHED_REFNO())});
                } else {
                    count = (int) dB.insert(ValueHolder.TABLE_ORDHED, null, values);
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
    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int restData(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.REFNO + " = '"
                    + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(ValueHolder.TABLE_ORDHED, ValueHolder.REFNO + " ='" + refno + "'", null);
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
    public int InactiveStatusUpdate(String refno) {

        int count = 0;
        String UploadDate = "";
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        TimeZone tz = TimeZone.getTimeZone("UTC +5:30");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        UploadDate = df.format(new Date());
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.REFNO + " = '"
                    + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

           // values.put(ValueHolder.IS_ACTIVE, "1");
            values.put(ValueHolder.END_TIME_SO, UploadDate);
            values.put(ValueHolder.ADDDATE, UploadDate);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.REFNO + " =?",
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

    @SuppressWarnings("static-access")
    public int updateIsSynced(Order hed) {

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
            values.put(ValueHolder.IS_SYNC, "1");
            count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.REFNO + " =?", new String[]{String.valueOf(hed.getFORDHED_REFNO())});
//            }else{
//                values.put(FORDHED_IS_SYNCED, "0");
//                count = dB.update(ValueHolder.TABLE_ORDHED, values, REFNO + " =?", new String[] { String.valueOf(hed.getORDER_REFNO()) });
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

    public int updateIsSynced(String refno, String res, String stat, String activeStatus) {

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
            values.put(ValueHolder.IS_SYNC, res);
            values.put(ValueHolder.STATUS, stat);
            values.put(ValueHolder.IS_ACTIVE, activeStatus);
            count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.REFNO + " =?", new String[]{refno});
//            }else{
//                values.put(FORDHED_IS_SYNCED, "0");
//                count = dB.update(ValueHolder.TABLE_ORDHED, values, REFNO + " =?", new String[] { String.valueOf(hed.getORDER_REFNO()) });
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

    public int updateIsActive(String refno, String res) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();
            values.put(ValueHolder.IS_ACTIVE, res);
            count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.REFNO + " =?", new String[]{refno});
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

    public int updateIsSynced(String refno) {

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
            values.put(ValueHolder.IS_SYNC, "1");
            count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.REFNO + " =?", new String[]{refno});
//            }else{
//                values.put(FORDHED_IS_SYNCED, "0");
//                count = dB.update(ValueHolder.TABLE_ORDHED, values, REFNO + " =?", new String[] { String.valueOf(hed.getORDER_REFNO()) });
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

    public int updateFeedback(String feedback, String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.REFNO
                    + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(ValueHolder.FEEDBACK, feedback);


            int cn = cursor.getCount();
            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.REFNO + " =?",
                        new String[]{String.valueOf(refno)});
            } else {
                count = (int) dB.insert(ValueHolder.TABLE_ORDHED, null, values);
            }


        } catch (Exception e) {

            Log.v(TAG + " ExcptnInFeedbackUpdate", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }

    public int getUnsyncedOrderCount() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.IS_SYNC + "= '0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        int count = 0;

        try {
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
    public ArrayList<Order> getAllOrders() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Order> list = new ArrayList<Order>();

        @SuppressWarnings("static-access")
//        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " where "+ ValueHolder.IS_ACTIVE + " <> '1' ORDER BY Id DESC";
         String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " where " + ValueHolder.IS_ACTIVE + " = '0'    ORDER BY Id DESC";
        //  String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + "";
        Cursor cursor = dB.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {

            Order order = new Order();
            OrderDetailController detDS = new OrderDetailController(context);

            // order.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
            order.setDistDB(SharedPref.getInstance(context).getDistDB().trim());
            //order.setNextNumVal(branchDS.getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));

            order.setFORDHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
            order.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            order.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
            order.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
            order.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
            order.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            order.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            order.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            order.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
            order.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            order.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            order.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            order.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            order.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
            order.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_SYNC)));
            order.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            order.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTECODE)));
            order.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
            order.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
            order.setFORDHED_LOC_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
            order.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            order.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            order.setFORDHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
            order.setFORDHED_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));


            list.add(order);

        }

        return list;
    }


    public ArrayList<OrderHed> getAllNotIssueOrders() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderHed> list = new ArrayList<OrderHed>();


        TimeZone tz = TimeZone.getTimeZone("UTC+5:30");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " where " + ValueHolder.IS_ACTIVE + " <> '1' AND " +
                ValueHolder.STATUS + " NOT LIKE 'ISSUED' ORDER BY Id DESC";

        Cursor cursor = dB.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {

            OrderHed order = new OrderHed();
            OrderDetailController detDS = new OrderDetailController(context);
            OrdFreeIssueController freeIssDS = new OrdFreeIssueController(context);

            order.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            order.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            order.setManuRef(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            order.setCurCode(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
            order.setCostCode(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
            order.setCurRate(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE))));
            order.setDebCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            order.setRemarks(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            order.setTxnType(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
            order.setLocCode(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
            order.setRepCode(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            order.setBtotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_DIS))));
            order.setTotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALDIS))));
            order.setPtotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS))));
            order.setBptotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS))));
            order.setBtotalTax(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_TAX))));
            order.setTotalTax(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_TAX))));
            order.setBtotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_AMT))));
            order.setTotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT))));
            order.setTaxReg(cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG)));
            order.setContact(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSMOB)));
            order.setCusAdd1(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD1)));
            order.setCusAdd2(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD2)));
            order.setCusAdd3(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD3)));
            order.setCusTele(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSTELE)));
            order.setAddDate(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            order.setAddMach(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            order.setAddUser(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            order.setRouteCode(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE)));
            order.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE))));
            order.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE))));
            order.setStartTimeSo(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            order.setEndTimeSo(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            order.setAddress(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
            order.setUpdDate(df.format(new Date()));
            order.setAppVersion(cursor.getString(cursor.getColumnIndex(ValueHolder.APPVERSION)));
            order.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
            order.setAppSts(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ValueHolder.APPSTS))));
            order.setStatus(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));
            order.setRefNo1("");
            order.setQuoTerm("");
            order.setPrtCopy(0);
            order.setInvFlg(0);
            order.setDisHflg(0);
            order.setSoclsflg(0);
            order.setDispflg(1);
            order.setWebOrdflg(0);
            order.setAndFlg(0);

            order.setOrderDetDetails(detDS.getAllUnSyncNew(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));
            order.setFreeIssueDetails(freeIssDS.getAllFreeIssuesNew(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));

            list.add(order);

        }

        return list;
    }

//    public ArrayList<Order> getAllNotIssueOrders() {
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<Order> list = new ArrayList<Order>();
//
//        @SuppressWarnings("static-access")
//        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " where " + ValueHolder.IS_ACTIVE + " <> '1' AND " +
//                ValueHolder.STATUS + " NOT LIKE 'ISSUED' ORDER BY Id DESC";
//
//        Cursor cursor = dB.rawQuery(selectQuery, null);
//
//
//        while (cursor.moveToNext()) {
//
//            Order order = new Order();
//            OrderDetailController detDS = new OrderDetailController(context);
//            OrdFreeIssueController freeIssDS = new OrdFreeIssueController(context);
//
//            order.setDistDB(new SharedPref(context).getDistDB());
//            order.setConsoleDB(new SharedPref(context).getDistDB());
//            order.setFORDHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
//            order.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
//            order.setFORDHED_ORDERID(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID)));
//            order.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
//            order.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
//            order.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
//            order.setFORDHED_APP_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_DATE)));
//            order.setFORDHED_APPSTS(cursor.getString(cursor.getColumnIndex(ValueHolder.APPSTS)));
//            order.setFORDHED_APP_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_USER)));
//            order.setFORDHED_BP_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS)));
//            order.setFORDHED_B_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_AMT)));
//            order.setFORDHED_B_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_DIS)));
//            order.setFORDHED_B_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_TAX)));
//            order.setFORDHED_COST_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)).trim());
//            order.setFORDHED_CUR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
//            order.setFORDHED_CUR_RATE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE)));
//            order.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
//            order.setFORDHED_DIS_PER(cursor.getString(cursor.getColumnIndex(ValueHolder.DISPER)));
//            order.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
//            order.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
//            order.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
//            order.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
//            order.setFORDHED_LOC_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
//            order.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
//            order.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
//            order.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
//            order.setFORDHED_RECORD_ID("");
//            order.setFORDHED_TAX_REG(cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG)));
//            order.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
//            order.setFORDHED_TOTALDIS(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALDIS)));
//            order.setFORDHED_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_TAX)));
//            order.setFORDHED_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
//            order.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
//            order.setFORDHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
//            order.setFORDHED_TOTAL_ITM_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_ITM_DIS)));
//            order.setFORDHED_TOT_MKR_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_MKR_AMT)));
//            order.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_SYNC)));
//            order.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
//            order.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
//            order.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE)));
//            order.setFORDHED_HED_DIS_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
//            order.setFORDHED_HED_DIS_PER_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
//            order.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
//            order.setFORDHED_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));
//
//            order.setOrdDet(detDS.getAllUnSync(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));
//            order.setOrdFreeIss(freeIssDS.getAllFreeIssues(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
//            order.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
//
//
//            list.add(order);
//
//        }
//
//        return list;
//    }

    public int IsSavedHeader(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;


        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.REFNO + " = '" + refno + "'";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            //  values.put(DatabaseHelper.FINVHED_IS_ACTIVE, "0");

            int cn = cursor.getCount();
            count = cn;

//            if (cn > 0) {
//                count = dB.update(DatabaseHelper.TABLE_FINVHED, values, DatabaseHelper.REFNO + " =?", new String[]{String.valueOf(refno)});
//            } else {
//                count = (int) dB.insert(DatabaseHelper.TABLE_FINVHED, null, values);
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

    public int IsSyncedOrder(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;


        try {

            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.REFNO + " = '" + refno + "' and " + ValueHolder.IS_SYNC + " = '1' ";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            //  values.put(DatabaseHelper.FINVHED_IS_ACTIVE, "0");

            int cn = cursor.getCount();
            count = cn;

//            if (cn > 0) {
//                count = dB.update(DatabaseHelper.TABLE_FINVHED, values, DatabaseHelper.REFNO + " =?", new String[]{String.valueOf(refno)});
//            } else {
//                count = (int) dB.insert(DatabaseHelper.TABLE_FINVHED, null, values);
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

    public Order getAllActiveOrdHed() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
                + "='1' and " + ValueHolder.IS_SYNC + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);


        Order presale = new Order();

        while (cursor.moveToNext()) {

            OrderDetailController detDS = new OrderDetailController(context);

            ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);
            presale.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            presale.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
            presale.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            presale.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            presale.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            presale.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            presale.setFORDHED_APP_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_DATE)));
            presale.setFORDHED_APPSTS(cursor.getString(cursor.getColumnIndex(ValueHolder.APPSTS)));
            presale.setFORDHED_APP_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_USER)));
            presale.setFORDHED_CUR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
            presale.setFORDHED_CUR_RATE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE)));
            presale.setFORDHED_RECORD_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.APPVERSION)));
            // presale.setFORDHED_ADDTIME(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            presale.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
            presale.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
            presale.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            presale.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            presale.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            presale.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
            presale.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            presale.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
            presale.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTECODE)));
            presale.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
            presale.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
            presale.setFORDHED_COST_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
            presale.setFORDHED_CUSADD1(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD1)));
            presale.setFORDHED_CUSADD2(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD2)));
            presale.setFORDHED_CUSADD3(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD3)));
            presale.setFORDHED_CONTACT(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSMOB)));
            presale.setFORDHED_CUSTELE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSTELE)));
            presale.setOrdDet(detDS.getAllActives(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));

        }

        return presale;

    }

    public Order getAllActiveOrdHedByID(String RefNo) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        @SuppressWarnings("static-access")
//        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
//                + "='1' and " + ValueHolder.IS_SYNC + "='0' and "+ ValueHolder.ORDERID + "='"
//                + RefNo + "'";

                String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where "
                + ValueHolder.IS_SYNC + "='0' and " + ValueHolder.REFNO + "='"
                + RefNo + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);


        Order presale = new Order();

        while (cursor.moveToNext()) {

            OrderDetailController detDS = new OrderDetailController(context);

            ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);
            presale.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            presale.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
            presale.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            presale.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            presale.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            presale.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            // presale.setFORDHED_ADDTIME(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            presale.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
            presale.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
            presale.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            presale.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            presale.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            presale.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
            presale.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            presale.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
            presale.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTECODE)));
            presale.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
            presale.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
            presale.setFORDHED_COST_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
            presale.setOrdDet(detDS.getAllActives(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));

        }

        return presale;

    }

    public Order getAllActiveOrdHedByIDAfterSave(String RefNo) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
                + "='1' and " + ValueHolder.IS_SYNC + "='0' and " + ValueHolder.REFNO + "='"
                + RefNo + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);


        Order presale = new Order();

        while (cursor.moveToNext()) {

            OrderDetailController detDS = new OrderDetailController(context);

            ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);
            presale.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            presale.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
            presale.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            presale.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            presale.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            presale.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            // presale.setFORDHED_ADDTIME(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            presale.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            presale.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
            presale.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
            presale.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            presale.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            presale.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            presale.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
            presale.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            presale.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
            presale.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTECODE)));
            presale.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
            presale.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
            presale.setFORDHED_COST_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
            presale.setOrdDet(detDS.getAllActives(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));

        }

        return presale;

    }

//    public ArrayList<Order> getAllUnSyncOrdHed() {
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<Order> list = new ArrayList<Order>();
//
//        @SuppressWarnings("static-access")
//        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
//                + "='0' and " + ValueHolder.IS_SYNC + "='0'";
//
//        Cursor cursor = dB.rawQuery(selectQuery, null);
//
//
//        while (cursor.moveToNext()) {
////
//            Order order = new Order();
//            OrderDetailController detDS = new OrderDetailController(context);
//            OrdFreeIssueController freeIssDS = new OrdFreeIssueController(context);
//////
////            CompanyBranchDS branchDS = new CompanyBranchDS(context);
////            preSalesMapper
////                    .setNextNumVal(branchDS.getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
//
//            order.setDistDB(new SharedPref(context).getDistDB());
//            order.setConsoleDB(new SharedPref(context).getDistDB());
//            order.setFORDHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
//            order.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
//            order.setFORDHED_ORDERID(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID)));
//            order.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
//            order.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
//            order.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
//            order.setFORDHED_APP_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_DATE)));
//            order.setFORDHED_APPSTS(cursor.getString(cursor.getColumnIndex(ValueHolder.APPSTS)));
//            order.setFORDHED_APP_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_USER)));
//            order.setFORDHED_BP_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS)));
//            order.setFORDHED_B_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_AMT)));
//            order.setFORDHED_B_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_DIS)));
//            order.setFORDHED_B_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_TAX)));
//            order.setFORDHED_COST_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)).trim());
//            order.setFORDHED_CUR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
//            order.setFORDHED_CUR_RATE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE)));
//            order.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
//            order.setFORDHED_DIS_PER(cursor.getString(cursor.getColumnIndex(ValueHolder.DISPER)));
//            order.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
//            order.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
//            order.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
//            order.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
//            order.setFORDHED_LOC_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
//            order.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
//            order.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
//            order.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
//            order.setFORDHED_RECORD_ID("");
//            order.setFORDHED_TAX_REG(cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG)));
//            order.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
//            order.setFORDHED_TOTALDIS(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALDIS)));
//            order.setFORDHED_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_TAX)));
//            order.setFORDHED_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
//            order.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
//            order.setFORDHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
//            order.setFORDHED_TOTAL_ITM_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_ITM_DIS)));
//            order.setFORDHED_TOT_MKR_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_MKR_AMT)));
//            order.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_SYNC)));
//            order.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
//            order.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
//            order.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE)));
//            order.setFORDHED_HED_DIS_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
//            order.setFORDHED_HED_DIS_PER_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
//            order.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
//
//            order.setOrdDet(detDS.getAllUnSync(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));
//            order.setOrdFreeIss(freeIssDS.getAllFreeIssues(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
//            order.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
//
//            list.add(order);
//
//        }
//
//        return list;
//    }

    public ArrayList<OrderHed> getAllUnSyncOrdHedNew() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderHed> list = new ArrayList<OrderHed>();

        TimeZone tz = TimeZone.getTimeZone("UTC+5:30");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);


        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
                + "='0' and " + ValueHolder.IS_SYNC + "='1'";

        Cursor cursor = dB.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {

            OrderHed order = new OrderHed();
            OrderDetailController detDS = new OrderDetailController(context);
            OrdFreeIssueController freeIssDS = new OrdFreeIssueController(context);

            order.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            order.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            order.setManuRef(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            order.setCurCode(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
            order.setCostCode(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
          //  order.setCurRate(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE))));
            //order.setCurCode(String.valueOf(cursor.getDouble((int) Double.parseDouble(ValueHolder.CUR_RATE))));
            order.setDebCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            order.setRemarks(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            order.setTxnType(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
            order.setLocCode(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
            order.setRepCode(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            order.setBtotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_DIS))));
            order.setTotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALDIS))));
            order.setPtotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS))));
            order.setBptotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS))));
            order.setBtotalTax(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_TAX))));
            order.setTotalTax(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_TAX))));
            order.setBtotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_AMT))));
            order.setTotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT))));
            order.setTaxReg(cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG)));
            order.setContact(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSMOB)));
            order.setCusAdd1(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD1)));
            order.setCusAdd2(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD2)));
            order.setCusAdd3(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD3)));
            order.setCusTele(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSTELE)));
            order.setAddDate(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            order.setAddMach(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            order.setAddUser(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            order.setRouteCode(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE)));
            order.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE))));
            order.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE))));
            order.setStartTimeSo(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            order.setEndTimeSo(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            order.setAddress(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
            order.setUpdDate(df.format(new Date()));
            order.setAppVersion(cursor.getString(cursor.getColumnIndex(ValueHolder.APPVERSION)));
            order.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
            order.setAppSts(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ValueHolder.APPSTS))));
            order.setRefNo1("");
            order.setQuoTerm("");
            order.setPrtCopy(0);
            order.setInvFlg(0);
            order.setDisHflg(0);
            order.setSoclsflg(0);
            order.setDispflg(1);
            order.setWebOrdflg(0);
            order.setAndFlg(0);

            order.setOrderDetDetails(detDS.getAllUnSyncNew(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));
            order.setFreeIssueDetails(freeIssDS.getAllFreeIssuesNew(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));

            list.add(order);

        }

        return list;
    }


    public ArrayList<OrderHed> getAllUnSyncOrdHedByRefNo(String RefNo) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<OrderHed> list = new ArrayList<OrderHed>();

        TimeZone tz = TimeZone.getTimeZone("UTC+5:30");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
                + "='1' and " + ValueHolder.IS_SYNC + "='1' AND " + ValueHolder.REFNO + " = '" + RefNo + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {

            OrderHed order = new OrderHed();
            OrderDetailController detDS = new OrderDetailController(context);
            OrdFreeIssueController freeIssDS = new OrdFreeIssueController(context);

            order.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            order.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            order.setManuRef(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            order.setCurCode(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
            order.setCostCode(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)));
            order.setCurRate(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE))));
            order.setDebCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            order.setRemarks(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            order.setTxnType(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
            order.setLocCode(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
            order.setRepCode(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            order.setBtotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_DIS))));
            order.setTotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALDIS))));
            order.setPtotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS))));
            order.setBptotalDis(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS))));
            order.setBtotalTax(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_TAX))));
            order.setTotalTax(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_TAX))));
            order.setBtotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_AMT))));
            order.setTotalAmt(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT))));
            order.setTaxReg(cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG)));
            order.setContact(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSMOB)));
            order.setCusAdd1(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD1)));
            order.setCusAdd2(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD2)));
            order.setCusAdd3(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSADD3)));
            order.setCusTele(cursor.getString(cursor.getColumnIndex(ValueHolder.CUSTELE)));
            order.setAddDate(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            order.setAddMach(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            order.setAddUser(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            order.setRouteCode(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE)));
            order.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE))));
            order.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE))));
            order.setStartTimeSo(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            order.setEndTimeSo(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            order.setAddress(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
            order.setUpdDate(df.format(new Date()));
            order.setAppVersion(cursor.getString(cursor.getColumnIndex(ValueHolder.APPVERSION)));
            order.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
            order.setAppSts(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ValueHolder.APPSTS))));
            order.setStatus(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));
            order.setRefNo1("");
            order.setQuoTerm("");
            order.setPrtCopy(0);
            order.setInvFlg(0);
            order.setDisHflg(0);
            order.setSoclsflg(0);
            order.setDispflg(1);
            order.setWebOrdflg(0);
            order.setAndFlg(0);

            order.setOrderDetDetails(detDS.getAllUnSyncNew(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));
            order.setFreeIssueDetails(freeIssDS.getAllFreeIssuesNew(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));

            list.add(order);

        }

        return list;
    }

//    public ArrayList<Order> getAllUnSyncOrdHedByRefNo(String RefNo) {
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<Order> list = new ArrayList<Order>();

//
//        @SuppressWarnings("static-access")
//        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where " + ValueHolder.IS_ACTIVE
//                + "='0' and " + ValueHolder.IS_SYNC + "='0' AND " + ValueHolder.REFNO + " = '" + RefNo + "'";
//
//        Cursor cursor = dB.rawQuery(selectQuery, null);
//
//
//        while (cursor.moveToNext()) {
//
//            Order order = new Order();
//            OrderDetailController detDS = new OrderDetailController(context);
//            OrdFreeIssueController freeIssDS = new OrdFreeIssueController(context);
//
//            order.setDistDB(new SharedPref(context).getDistDB());
//            order.setConsoleDB(new SharedPref(context).getDistDB());
//            order.setFORDHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
//            order.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
//            order.setFORDHED_ORDERID(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID)));
//            order.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
//            order.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
//            order.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
//            order.setFORDHED_APP_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_DATE)));
//            order.setFORDHED_APPSTS(cursor.getString(cursor.getColumnIndex(ValueHolder.APPSTS)));
//            order.setFORDHED_APP_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.APP_USER)));
//            order.setFORDHED_BP_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.BP_TOTAL_DIS)));
//            order.setFORDHED_B_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_AMT)));
//            order.setFORDHED_B_TOTAL_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_DIS)));
//            order.setFORDHED_B_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(ValueHolder.B_TOTAL_TAX)));
//            order.setFORDHED_COST_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE)).trim());
//            order.setFORDHED_CUR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
//            order.setFORDHED_CUR_RATE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE)));
//            order.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
//            order.setFORDHED_DIS_PER(cursor.getString(cursor.getColumnIndex(ValueHolder.DISPER)));
//            order.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
//            order.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
//            order.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
//            order.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
//            order.setFORDHED_LOC_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
//            order.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
//            order.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
//            order.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
//            order.setFORDHED_RECORD_ID("");
//            order.setFORDHED_TAX_REG(cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG)));
//            order.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
//            order.setFORDHED_TOTALDIS(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALDIS)));
//            order.setFORDHED_TOTAL_TAX(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_TAX)));
//            order.setFORDHED_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
//            order.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
//            order.setFORDHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
//            order.setFORDHED_TOTAL_ITM_DIS(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTAL_ITM_DIS)));
//            order.setFORDHED_TOT_MKR_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_MKR_AMT)));
//            order.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_SYNC)));
//            order.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
//            order.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
//            order.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE)));
//            order.setFORDHED_HED_DIS_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
//            order.setFORDHED_HED_DIS_PER_VAL(cursor.getString(cursor.getColumnIndex(ValueHolder.DIS_VAL)));
//            order.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
//            order.setFORDHED_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));
//
//            order.setOrdDet(detDS.getAllUnSync(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO))));
//            order.setOrdFreeIss(freeIssDS.getAllFreeIssues(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
//            order.setNextNumVal(new ReferenceController(context).getCurrentNextNumVal(context.getResources().getString(R.string.NumVal)));
//
//            list.add(order);
//
//        }
//
//        return list;
//    }


    public int getAllDayBeforeUnSyncOrdHed() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }


        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " Where IsSync <> 1 and IsActive <> 1";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        Integer count = cursor.getCount();


        return count;
    }

    public String getRefnoByDebcode(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.REFNO + "='"
                + refno + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE));

        }
        return "";

    }

    public boolean isAnyConfirmOrderHed(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        boolean res = false;

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.IS_ACTIVE + "='2'" + " AND " + ValueHolder.ORDERID + " = '" + RefNo + "'";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                res = true;
            else
                res = false;

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return res;

    }

    //-------------------kaveesha --------- 22/02/2021----------------------------------------
    public boolean isAnySyncOrderHed(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        boolean res = false;

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.IS_SYNC + "='1'" + " AND " + ValueHolder.ORDERID + " = '" + RefNo + "'";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                res = true;
            else
                res = false;

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return res;

    }


    public String getActiveRefNoFromOrders() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String refNo = "";

        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.IS_ACTIVE + "='" + "1" + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    refNo = cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID));
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

        return refNo;
    }

    //-----------------------------kaveeasha ---2/10/2020-----To get cost code-------------------------------------------------------
    public String getCostCodeByLocCode(String LocCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT CostCode FROM " + ValueHolder.TABLE_LOCATIONS + " WHERE " + ValueHolder.LOCCODE + "='"
                + LocCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.COSTCODE));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }


    //-------------------kaveesha -------------------- 24/11/2021 ------------------------------------------------
    public int CreateOrUpdateOrderStatus(ArrayList<Order> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;


        Log.d("*********^^^JSON ARRAY", "doInBackground: " + list);
        try {

            for (Order order : list) {

//                String newRef = order.getFORDHED_REFNO().replace("/","");

                Log.d("*********^^ref", "CreateOrUpdateOrderStatus: " + order.getFORDHED_REFNO());

                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.REFNO
                        + " = '" + order.getFORDHED_REFNO() + "'";

                cursor = dB.rawQuery(selectQuery, null);

                ContentValues values = new ContentValues();

                values.put(ValueHolder.STATUS, order.getFORDHED_STATUS());

                count = dB.update(ValueHolder.TABLE_ORDHED, values, ValueHolder.REFNO + " =?",
                        new String[]{String.valueOf(order.getFORDHED_REFNO())});
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

    public ArrayList<Order> getAllOrdersBySearch(String key) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Order> list = new ArrayList<Order>();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + ValueHolder.TABLE_ORDHED + " where " + ValueHolder.IS_ACTIVE + " <> '1' and " + ValueHolder.TXNDATE + " LIKE '%" + key + "%' ORDER BY Id DESC";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            Order order = new Order();

            order.setDistDB(SharedPref.getInstance(context).getDistDB().trim());

            order.setFORDHED_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
            order.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            order.setOrderId(Long.parseLong(cursor.getString(cursor.getColumnIndex(ValueHolder.ORDERID))));
            order.setFORDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LONGITUDE)));
            order.setFORDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.LATITUDE)));
            order.setFORDHED_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
            order.setFORDHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            order.setFORDHED_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            order.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TOTALAMT)));
            order.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            order.setFORDHED_ADD_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDDATE)));
            order.setFORDHED_ADD_MACH(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDMACH)));
            order.setFORDHED_ADD_USER(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDUSER)));
            order.setFORDHED_IS_ACTIVE(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_ACTIVE)));
            order.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(ValueHolder.IS_SYNC)));
            order.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            order.setFORDHED_ROUTE_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTECODE)));
            order.setFORDHED_DELV_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE)));
            order.setFORDHED_PAYMENT_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.PAYMENT_TYPE)));
            order.setFORDHED_LOC_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)));
            order.setFORDHED_START_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.START_TIME_SO)));
            order.setFORDHED_END_TIME_SO(cursor.getString(cursor.getColumnIndex(ValueHolder.END_TIME_SO)));
            order.setFORDHED_ADDRESS(cursor.getString(cursor.getColumnIndex(ValueHolder.ADDRESS)));
            order.setFORDHED_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));


            list.add(order);

        }

        return list;
    }

    //-------------------------kaveesha ------ 21-03-2021 --------------------------------------------------------
    public ArrayList<String> getDeliveryDates(String debCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<String> list = new ArrayList<String>();

        String selectQuery = "SELECT DelvDate FROM " + ValueHolder.TABLE_ORDHED + " WHERE " + ValueHolder.DEBCODE + "='"
                + debCode + "'";

        cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                String date = "";
                date = cursor.getString(cursor.getColumnIndex(ValueHolder.DELV_DATE));

                list.add(date);
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


    public ArrayList<Order> getTodayOrders() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Order> list = new ArrayList<Order>();

        try {
            //String selectQuery = "select DebCode, RefNo from fordHed " +
            String selectQuery = "select DebCode, RefNo, IsSync, TxnDate, TotalAmt from TblOrder "
                    + "  where txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "' and isActive <> 1" +
                    " ORDER BY RefNo DESC";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                Order ordDet = new Order();

//
                ordDet.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(REFNO)));
                ordDet.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(DEBCODE)));
                ordDet.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(FORDHED_IS_SYNC)));
                ordDet.setFORDHED_TXN_TYPE("Order");
                ordDet.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(TXNDATE)));
                ordDet.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(FORDHED_TOTAL_AMT)));
                //TODO :set  discount, free

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

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<Order> getOrdersByDate(String from, String to) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Order> list = new ArrayList<Order>();

        try {
            //String selectQuery = "select DebCode, RefNo from fordHed " +
            String selectQuery = "SELECT DebCode, RefNo, IsSync, TxnDate, TotalAmt FROM TblOrder WHERE isActive <> 1 " +
                    "AND txndate between '" + from + "' and " + "'" + to + "' ORDER BY RefNo DESC";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                Order order = new Order();

                order.setFORDHED_REFNO(cursor.getString(cursor.getColumnIndex(REFNO)));
                order.setFORDHED_DEB_CODE(cursor.getString(cursor.getColumnIndex(DEBCODE)));
                order.setFORDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(FORDHED_IS_SYNC)));
                order.setFORDHED_TXN_DATE(cursor.getString(cursor.getColumnIndex(TXNDATE)));
                order.setFORDHED_TOTAL_AMT(cursor.getString(cursor.getColumnIndex(FORDHED_TOTAL_AMT)));
                order.setFORDHED_TXN_TYPE("Order");


                list.add(order);
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

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
}
