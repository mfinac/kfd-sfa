package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DashboardController {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "DashboardController";

    // Shared Preferences variables
    public static final String SETTINGS = "SETTINGS";


    public DashboardController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }


    //rashmi-2019-11-29
    public Date subtractDay(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    //rashmi-2019-11-29
    public String getFirstDay() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));


        String firstday = "" + curYear + "-" + String.format("%02d", curMonth) + "-" + "01";


        return firstday;


    }

    //current month target
    public Double getRepTarget() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));


        double targetsum = 0.00;
        Cursor cursor = null;

        // String selectQuery = "SELECT ifnull((sum(Rdtarget)),0)  as Target from FItenrDet where txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth)+ "-_%'";
        String selectQuery = "SELECT TrAmt from TblRepTrgDet where Monthn = '" + String.format("%02d", curMonth) + "' and YearT = '" + curYear + "'";

        cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {
                targetsum = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Target")));
                return targetsum;
            }

        } catch (Exception e) {

            Log.v(TAG + " Excep getRepTarget", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return targetsum;

    }

    //previous month target
    public Double getPMRepTarget() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        double targetsum = 2000000.00;
        try {
            String selectQuery = "SELECT TrAmt from TblRepTrgDet where Monthn = '" + String.format("%02d", curMonth - 1) + "' and YearT = '" + curYear + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                targetsum = Double.parseDouble(cursor.getString(cursor.getColumnIndex("TrAmt")));
                return targetsum;
            }

        } catch (Exception e) {

            Log.v(TAG + " Excep getPMTarget", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return targetsum;
    }

    public Double getTodayReturn() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));


        double todayreturn = 0.0;

        Cursor cursor = null;

        try {
            String selectQuery = "select ifnull((sum(a.Amt)),0)  as totAmt from TblInvRDet a where a.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" +
                    String.format("%02d", curDate) + "'";

            cursor = dB.rawQuery(selectQuery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                todayreturn = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }


            cursor.close();

        } catch (Exception e) {

            Log.v(TAG + " Excep getTReturn", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return todayreturn;

    }

    public Double getTodayCashCollection() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));


        double discount = 0.0;

        Cursor cursor = null;

        try {
            String selectQuery = "select ifnull((sum(Amt)),0)  as totAmt from Tblrecdets  det, Tblrecheds hed where det.refno = hed.refno and " +

                    "det.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "'" +
                    " and  det.dtxndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "' and hed.paytype = 'CA'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }

            cursor.close();

        } catch (Exception e) {

            Log.v(TAG + " Excep getTCashCol", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return discount;

    }

    public Double getTodayChequeCollection() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        double discount = 0.0;


        Cursor cursor = null;

        try {
            String selectQuery = "select ifnull((sum(det.amt)),0)  as totAmt from Tblrecdets  det, Tblrecheds hed where det.refno = hed.refno and " +

                    "det.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "'" +
                    " and  det.dtxndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "' and hed.paytype = 'CH'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }


        } catch (Exception e) {

            Log.v(TAG + " Excep getTCashCol", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return discount;
    }//***


    public Double getCashPreviousCollection() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        double discount = 0.0;
        Cursor cursor = null;

        try {


            String selectquery = "select ifnull((sum(det.aloamt)),0)  as totAmt from Tblrecdets  det, Tblrecheds hed where det.refno = hed.refno and " +
                    "det.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "'" +
                    " and  det.dtxndate <> '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "' and hed.paytype = 'CA'";

            cursor = dB.rawQuery(selectquery, null);
//                    "det.txndate = '2019-04-12'" +
//                    " and  det.dtxndate <> '2019-02-13'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }


        } catch (Exception e) {

            Log.v(TAG + "getTodayCashPreviCollec", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return discount;

    }


    public Double getChequePreviousCollection() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        double discount = 0.0;
        Cursor cursor = null;
        try {

            String selectquery = "select ifnull((sum(det.aloamt)),0)  as totAmt from Tblrecdets  det , Tblrecheds hed where det.refno = hed.refno and " +
                    "det.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "'" +
                    " and  det.dtxndate <> '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "' and hed.paytype = 'CH'";


            cursor = dB.rawQuery(selectquery, null);
//                    "det.txndate = '2019-04-12'" +
//                    " and  det.dtxndate <> '2019-02-13'", null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }

        } catch (Exception e) {

            Log.v(TAG + "getTodayCheqPrevsCollec", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }


        return discount;

    }

    //current month discount
    public Double getTMDiscounts() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        double discount = 0.0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();
        Cursor cursor = null;
        try {


            String selectQuery = "select ifnull((sum(a.DisAmt)),0)  as totAmt from TblOrddet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
            }

        } catch (Exception e) {

            Log.v(TAG + " Excep getTMDiscounts", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return discount;
    }

    //current month gross
    public Double getMonthAchievement() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        double monthAchieve = 0.0;
        Cursor cursor = null;
        try {

            String selectQuery = "select ifnull((sum(a.BAmt)),0)  as totAmt from TblOrddet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                monthAchieve = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }


        } catch (Exception e) {

            Log.v(TAG + " Excep getMonthAchieve", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return monthAchieve;

    }

    //current month tax
    public Double getMonthTax() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        double monthTax = 0.0;
        Cursor cursor = null;
        try {
            //BAmt = tax reverse amount
            String selectQuery = "select ifnull((sum(a.TaxAmt)),0)  as totAmt from TblOrddet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                monthTax = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }


        } catch (Exception e) {

            Log.v(TAG + " Excep getMonthmonthTax", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return monthTax;

    }

    //previous month discount
    public Double getPMDiscounts() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        double discount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "select ifnull((sum(a.DisAmt)),0)  as totAmt from TblOrddet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth - 1) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getPMDiscounts", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return discount;


    }

    public Double getPMTax() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        double tax = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "select ifnull((sum(a.TaxAmt)),0)  as totAmt from TblOrddet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth - 1) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                tax = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getPMtax", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return tax;


    }

    //previous month gross
    public Double getPMonthAchievement() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        double monthAchieve = 0.0;

        Cursor cursor = null;
        try {
//BAmt = tax reverse amount
            String selectQuery = "select ifnull((sum(a.BAmt)),0)  as totAmt from TblOrddet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth - 1) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                monthAchieve = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }


        } catch (Exception e) {

            Log.v(TAG + " Excep getPMonthAchiev", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return monthAchieve;

    }

    //current month return
    public Double getTMReturn() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        Cursor cursor = null;
        double discount = 0.0;
        try {


            String selectQuery = "select ifnull((sum(a.Amt)),0)  as totAmt from TblInvRDet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }


        } catch (Exception e) {

            Log.v(TAG + " Excep getTMDiscounts", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return discount;

    }


    //previous month return
    public Double getPMReturn() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        double discount = 0.0;
        Cursor cursor = null;
        try {

            String selectQuery = "select ifnull((sum(a.Amt)),0)  as totAmt from TblInvRDet a where a.txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth - 1) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }


        } catch (Exception e) {

            Log.v(TAG + " Excep getPMReturn", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return discount;

    }

    //current month productive
    public int getTMProductiveCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        int result = 0;
        try {
            String selectQuery = "select count(DISTINCT DebCode) from TblOrder where txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                if (cursor.getInt(0) > 0)
                    result = cursor.getInt(0);
            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getTMProductCunt", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return result;

    }


    //previous month productive
    public int getPMProductiveCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        int result = 0;

        try {
            String selectQuery = "select count(DISTINCT DebCode) from TblOrder where txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth - 1) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                if (cursor.getInt(0) > 0)
                    result = cursor.getInt(0);

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getPMProdnt", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return result;

    }


    //current month non productive
    public int getTMNonPrdCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        int result = 0;
        try {
            String selectQuery = "select count(refno) from TblDaynPrdHed where txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                if (cursor.getInt(0) > 0)
                    result = cursor.getInt(0);

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getTMNonPrdCount", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return result;


    }

    //previous month non productive
    public int getPMNonPrdCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        int result = 0;

        try {
            String selectQuery = "select count(refno) from TblDaynPrdHed where txndate LIKE '" + curYear + "-" + String.format("%02d", curMonth - 1) + "-_%'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                result = cursor.getInt(0);

                if (result > 0)
                    return result;

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getPMNCunt", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return result;
    }

    public Double getTodayDiscount() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        double discount = 0.0;

        Cursor cursor = null;

        try {
            String selectQuery = "select ifnull((sum(a.DisAmt)),0)  as totAmt from TblinvDet a where a.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "'";

            cursor = dB.rawQuery(selectQuery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
            cursor.close();

        } catch (Exception e) {

            Log.v(TAG + " Excep getTDiscount", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return discount;
    }

    public Double getDailyAchievement() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        double monthAchieve = 0.0;
        Cursor cursor = null;
        try {
//BAmt = tax reverse amount
            String selectquery = "select ifnull((sum(a.BAmt)),0)  as totAmt from TblOrddet a where a.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "'";
            cursor = dB.rawQuery(selectquery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                monthAchieve = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
            }

        } catch (Exception e) {

            Log.v(TAG + "getDailyAchievement", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return monthAchieve;
    }

    public Double getDailyTax() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<String[]> list = new ArrayList<String[]>();

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        double tax = 0.0;
        Cursor cursor = null;
        try {
//BAmt = tax reverse amount
            String selectquery = "select ifnull((sum(a.TaxAmt)),0)  as totAmt from TblinvDet a where a.txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "'";
            cursor = dB.rawQuery(selectquery, null);
            // Old 18-12-2017 cursor1 = dB.rawQuery("select ifnull((sum(a.qty)),0)  as totqty from ftransodet a, fitem b,ftransohed c where a.itemcode=b.itemcode and b.brandcode='" + arr[0] + "' and c.costcode='" + costCode + "' and c.refno=a.refno AND c.txndate LIKE '" + iYear + "-" + String.format("%02d", iMonth) + "-_%'", null);

            while (cursor.moveToNext()) {
                tax = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));
            }

        } catch (Exception e) {

            Log.v(TAG + "getDailytax", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return tax;
    }

    //get January Sale - kaveesha -2020-11-03*****************
    public Double getJanGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '01' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getJanSale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;
    }

    //get February Sale - kaveesha -2020-11-03*****************
    public Double getFebGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '02' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getFebSale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;


    }

    //get March Sale - kaveesha -2020-11-03*****************
    public Double getMarGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '03' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getMarchSale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;


    }

    //get April Sale - kaveesha -2020-11-03*****************
    public Double getAprGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '04' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getAprilSale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;


    }

    //get May Sale - kaveesha -2020-11-03*****************
    public Double getMayGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '05' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getMaySale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;


    }

    //get June Sale - kaveesha -2020-11-03*****************
    public Double getJuneGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '06' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getJuneSale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;


    }

    //get July Sale - kaveesha -2020-11-03*****************
    public Double getJulyGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '07' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getJulySale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;


    }

    //get August Sale - kaveesha -2020-11-03*****************
    public Double getAugGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '08' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getAugustSale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;


    }

    //get September Sale - kaveesha -2020-11-03*****************
    public Double getSepGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '09' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getSepSale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;


    }

    //get October Sale - kaveesha -2020-11-03*****************
    public Double getOctGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '10' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getOctSale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;


    }

    //get November Sale - kaveesha -2020-11-03*****************
    public Double getNovGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '11' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getNovSale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;


    }

    //get December Sale - kaveesha -2020-11-03*****************
    public Double getDecGrossSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double amount = 0.0;
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT sum(Amt) as totAmt , strftime('%m',TxnDate) as Month  FROM TblOrddet WHERE strftime('%m',TxnDate) = '12' GROUP by strftime('%m',TxnDate)";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                amount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("totAmt")));

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getDecSale", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return amount;


    }


    //today nonproductive
    public int getNonPrdCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        int result = 0;

        try {
            String selectQuery = "select count(refno) from TblDaynPrdHed where txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                result = cursor.getInt(0);

                if (result > 0)
                    return result;

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getNPCount", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return result;

    }

    //today productive
    public int getProductiveCount() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        int result = 0;

        try {
            String selectQuery = "select count(DISTINCT DebCode) from TblOrder where txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "'";
//            String selectQuery = "select count(DISTINCT DebCode) from FOrdHed where txndate = '" + curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate) + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                result = cursor.getInt(0);

                if (result > 0)
                    return result;

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getProdCount", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return result;
    }

    public String getRoute() {
        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        String curdate = curYear + "-" + String.format("%02d", curMonth) + "-" + String.format("%02d", curDate);

        String result = "";

        result = new FItenrDetController(context).getRouteFromItenary(curdate);

        return result;
    }


    public int getOutletCount(String route) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        int result = 0;


        try {
            String selectQuery = "select count(DISTINCT DebCode) from TblRouteDet where RouteCode = '" + route.trim() + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                result = cursor.getInt(0);

                if (result > 0)
                    return result;

            }
        } catch (Exception e) {

            Log.v(TAG + " Excep getOutCount", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return result;
    }

    public double getCurrMonthSale() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double totret = 0.00, totamt = 0.00;
        Cursor cursor = null;
        Cursor cursoret = null;
        try {
            String selectQuery = "SELECT Sum(TotalAmt) as TotalAmt FROM " + ValueHolder.TABLE_FINVHEDL3 + " WHERE RefNo1='22'";
            cursor = dB.rawQuery(selectQuery, null);

            String selectQueryRet = "SELECT Sum(TotalAmt) as TotalAmt FROM " + ValueHolder.TABLE_FINVHEDL3 + " WHERE RefNo1<>'22'";
            cursoret = dB.rawQuery(selectQueryRet, null);

            while (cursor.moveToNext()) {

                totamt = totamt+ cursor.getDouble(cursor.getColumnIndex(ValueHolder.FINVHEDL3_TOTAL_AMT));

            }

            while (cursoret.moveToNext()) {

                totret = totret+ cursoret.getDouble(cursoret.getColumnIndex(ValueHolder.FINVHEDL3_TOTAL_AMT));
            }

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursoret != null) {
                cursoret.close();
            }
            if (cursor != null) {
                cursor.close();
            }

            dB.close();
        }

        return totamt - totret;

    }

    public double getTodaySale(String Date) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double totret = 0, totamt = 0;
        Cursor cursor = null;
        Cursor cursoret = null;
        try {
            String selectQuery = "SELECT Sum(TotalAmt) as TotalAmt FROM " + ValueHolder.TABLE_FINVHEDL3 + " WHERE TxnDate ='" + Date + "' AND RefNo1='22'";
            cursor = dB.rawQuery(selectQuery, null);

            String selectQueryRet = "SELECT Sum(TotalAmt) as TotalAmt FROM " + ValueHolder.TABLE_FINVHEDL3 + " WHERE TxnDate ='" + Date + "' AND RefNo1<>'22'";
            cursoret = dB.rawQuery(selectQueryRet, null);

            while (cursor.moveToNext()) {


                totamt = totamt+ cursor.getDouble(cursor.getColumnIndex(ValueHolder.FINVHEDL3_TOTAL_AMT));
            }

            while (cursoret.moveToNext()) {

                totret = totret + cursoret.getDouble(cursoret.getColumnIndex(ValueHolder.FINVHEDL3_TOTAL_AMT));
            }

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return totamt - totret;

    }
}
