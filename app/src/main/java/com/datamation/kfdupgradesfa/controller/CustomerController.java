package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Customer;
import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.model.Debtor;
import com.datamation.kfdupgradesfa.model.FddbNote;
import java.util.ArrayList;


public class CustomerController {

    private SQLiteDatabase dB;
    private DatabaseHelper DbHelper;
    Context context;
    private String TAG = "CustomerController";

    public CustomerController(Context context) {
        this.context = context;
        DbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = DbHelper.getWritableDatabase();
    }

    public void InsertOrReplaceDebtor(ArrayList<Debtor> list) {
        Log.d("InsertOrReplaceDebtor", "" + list.size());
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_DEBTOR +
                    " (AreaCode," +
                    " ChkCrdLmt," +
                    " ChkCrdPrd," +
                    " ChkFree," +
                    " ChkMustFre," +
                    " CrdLimit," +
                    " CrdPeriod," +
                    " DebTele," +
                    " DebMob," +
                    " DebEMail," +
                    " DbGrCode," +
                    " DebAdd1," +
                    " DebAdd2," +
                    " DebAdd3," +
                    " RepCode," +
                    " PrilCode," +
                    " TaxReg," +
                    " RankCode," +
                    " TownCode," +
                    " DebCode," +
                    " RouteCode," +
                    " Status," +
                    " DebName," +
                    " Latitude," +
                    " Longitude," +
                    " MultiFlg) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_FDEBTOR + " (DebCode,DebName,DebAdd1,DebAdd2,DebAdd3,DebTele,DebMob,DebEMail,TownCode,AreaCode,DbGrCode,Status,CrdPeriod,ChkCrdPrd,CrdLimit,ChkCrdLmt,RepCode,PrillCode,TaxReg,RankCode,Latitude,Longitude) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (Debtor debtor : list) {
                stmt.bindString(1, debtor.getFDEBTOR_AREA_CODE());
                stmt.bindString(2, debtor.getFDEBTOR_CHK_CRD_LIMIT());
                stmt.bindString(3, debtor.getFDEBTOR_CHK_CRD_PERIOD());
                stmt.bindString(4, debtor.getFDEBTOR_CHK_FREE());
                stmt.bindString(5, debtor.getFDEBTOR_CHK_MUST());
                stmt.bindString(6, debtor.getFDEBTOR_CRD_LIMIT());
                stmt.bindString(7, debtor.getFDEBTOR_CRD_PERIOD());
                stmt.bindString(8, debtor.getFDEBTOR_TELE());
                stmt.bindString(9, debtor.getFDEBTOR_MOB());
                stmt.bindString(10, debtor.getFDEBTOR_EMAIL());
                stmt.bindString(11, debtor.getFDEBTOR_DBGR_CODE());
                stmt.bindString(12, debtor.getFDEBTOR_ADD1());
                stmt.bindString(13, debtor.getFDEBTOR_ADD2());
                stmt.bindString(14, debtor.getFDEBTOR_ADD3());
                stmt.bindString(15, debtor.getFDEBTOR_REPCODE());
                stmt.bindString(16, debtor.getFDEBTOR_PRILLCODE());
                stmt.bindString(17, debtor.getFDEBTOR_TAX_REG());
                stmt.bindString(18, debtor.getFDEBTOR_RANK_CODE());
                stmt.bindString(19, debtor.getFDEBTOR_TOWNCODE());
                stmt.bindString(20, debtor.getFDEBTOR_CODE());
                stmt.bindString(21, debtor.getFDEBTOR_ROUTE_CODE());
                stmt.bindString(22, debtor.getFDEBTOR_STATUS());
                stmt.bindString(23, debtor.getFDEBTOR_NAME());
                stmt.bindString(24, "0.000000");
                stmt.bindString(25, "0.000000");
                stmt.bindString(26, debtor.getFDEBTOR_MULTIFLG());

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
    public String getTaxRegStatus(String code) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_DEBTOR  + " WHERE " + ValueHolder.DEBCODE + "='"
                    + code + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG));

            }
        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
                dB.close();
            }
        }
        return "";
    }
    public String getCusNameByCode(String debcode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT " + ValueHolder.FDEBTOR_NAME + " FROM " + ValueHolder.TABLE_DEBTOR + " where " + ValueHolder.DEBCODE + " = '" + debcode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_NAME));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception get Name", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }

//    public ArrayList<Debtor> uploadCustomerPassword(String debcode) {
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<Debtor> list = new ArrayList<Debtor>();
//        Cursor cursor = null;
//        try {
//            String selectQuery = "select * from " + ValueHolder.TABLE_DEBTOR + " where DebCode = '"+ debcode + "' and "+ValueHolder.IS_SYNC+ " = '2'";
//
//            cursor = dB.rawQuery(selectQuery, null);
//            while (cursor.moveToNext()) {
//
//                Debtor customer = new Debtor();
//
//                customer.setKFDDB(new SharedPref(context).getDistDB());
//                customer.setFDEBTOR_CUSID(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ID)));
//                customer.setLOGIN_DEBCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
//                customer.setLOGIN_PASSWORD(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_PASSWORD)));
//
//                list.add(customer);
//
//            }
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            dB.close();
//        }
//
//        return list;
//    }
    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_DEBTOR, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_DEBTOR, null, null);
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

    public ArrayList<FddbNote> getOutStandingList(String debCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FddbNote> list = new ArrayList<FddbNote>();
        Cursor cursor = null;
        try {
            String selectQuery = "select outs.*, deb.crdperiod from TblFDDbNote outs , TblDebtor deb where deb.debcode = outs.debcode and outs.debcode = '"+ debCode + "' ORDER BY date(Txndate) ASC";
           // String selectQuery = "select outs.*, deb.crdperiod from TblFDDbNote outs , TblDebtor deb where deb.debcode = outs.debcode and outs.debcode = '"+ debCode + "' ORDER BY date(Txndate) ASC";


            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {

                FddbNote fddbNote = new FddbNote();
//
                fddbNote.setFDDBNOTE_REPNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.REPNAME)));
                fddbNote.setRefNo(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                fddbNote.setTxnDate(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                fddbNote.setAmt(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                fddbNote.setCreditPeriod(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CRD_PERIOD)));
                fddbNote.setFDDBNOTE_TOT_BAL(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL)));

                list.add(fddbNote);

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


    public Customer getSelectedCustomerByCode(String code) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        try {
            String selectQuery = "select * from " + ValueHolder.TABLE_DEBTOR + " Where " + ValueHolder.DEBCODE + "='"
                    + code + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                Customer customer = new Customer();

                customer.setCusName(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_NAME)));
                customer.setCusCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                customer.setCusAdd1(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD1)));
                customer.setCusAdd2(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD2)));
                customer.setCusAdd3(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD3)));
                customer.setCusMob(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_MOB)));
                customer.setCusTele(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_TELE)));
                customer.setCusStatus(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));

                return customer;

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return null;

    }
    public ArrayList<Customer> getRouteCustomerList(String routecode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Customer> list = new ArrayList<Customer>();
        Cursor cursor = null;
        try {
            String selectQuery = "select * from " + ValueHolder.TABLE_DEBTOR;
//            String selectQuery = "select * from " + ValueHolder.TABLE_DEBTOR + " Where " + ValueHolder.ROUTE_CODE + "='"
//                    + routecode + "'";
            // String selectQuery = "select outs.*, deb.crdperiod from TblFDDbNote outs , TblDebtor deb where deb.debcode = outs.debcode and outs.debcode = '"+ debCode + "' ORDER BY date(Txndate) ASC";


            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {

                Customer customer = new Customer();
//
                customer.setCusName(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_NAME)));
                customer.setCusCode(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                customer.setCusAdd1(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD1)));
                customer.setCusAdd2(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD2)));
                customer.setCusMob(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_MOB)));
                customer.setCusStatus(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));

                list.add(customer);

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

    public int updateIsSynced(String debCode, String res) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            ContentValues values = new ContentValues();
            values.put(ValueHolder.IS_SYNC, res);

            if (res.equalsIgnoreCase("1")) {
                count = dB.update(ValueHolder.TABLE_DEBTOR, values, ValueHolder.DEBCODE + " =?", new String[]{String.valueOf(debCode)});
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            dB.close();
        }
        return count;
    }

    public String getCurrentLocCode() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
//comment 2021-12-02 rashmi doesn't have loccode on table
    //    String selectQuery = "SELECT " + ValueHolder.LOCCODE + " FROM " + ValueHolder.TABLE_DEBTOR;
        String selectQuery = "SELECT " + ValueHolder.ROUTE_CODE + " FROM " + ValueHolder.TABLE_DEBTOR;

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.ROUTE_CODE));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }
    public String getCurrentRepCode() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT " + ValueHolder.REPCODE + " FROM " + ValueHolder.TABLE_DEBTOR;

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }
    public ArrayList<Debtor> getRouteCustomers(String RouteCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Debtor> list = new ArrayList<Debtor>();

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ROUTEDET + " RD, " + ValueHolder.TABLE_DEBTOR
                + " D WHERE RD." + ValueHolder.DEBCODE + "=D." + ValueHolder.DEBCODE + " AND RD."
                + ValueHolder.ROUTE_CODE + "='" + RouteCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            Debtor aDebtor = new Debtor();
            aDebtor.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
            aDebtor.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_NAME)));
            aDebtor.setFDEBTOR_ADD1(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD1)));
            aDebtor.setFDEBTOR_ADD2(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD2)));
            aDebtor.setFDEBTOR_ADD3(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD3)));
            aDebtor.setFDEBTOR_TELE(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_TELE)));
            aDebtor.setFDEBTOR_MOB(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_MOB)));
            aDebtor.setFDEBTOR_EMAIL(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_EMAIL)));
            aDebtor.setFDEBTOR_AREA_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.AREACODE)));
            aDebtor.setFDEBTOR_DBGR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_DBGR_CODE)));
            aDebtor.setFDEBTOR_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));
            aDebtor.setFDEBTOR_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CRD_PERIOD)));
            aDebtor.setFDEBTOR_CHK_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CHK_CRD_PRD)));
            aDebtor.setFDEBTOR_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CRD_LIMIT)));
            aDebtor.setFDEBTOR_CHK_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CHK_CRD_LIMIT)));
            aDebtor.setFDEBTOR_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
            aDebtor.setFDEBTOR_RANK_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_RANK_CODE)));

            list.add(aDebtor);

        }

        return list;
    }
    public ArrayList<Debtor> getRouteCustomers(String RouteCode, String newText) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Debtor> list = new ArrayList<Debtor>();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ROUTEDET + " RD, " + ValueHolder.TABLE_DEBTOR
                    + " D WHERE RD." + ValueHolder.DEBCODE + "=D." + ValueHolder.DEBCODE + " AND RD."
                    + ValueHolder.ROUTE_CODE + "='" + RouteCode + "' AND D." + ValueHolder.DEBCODE + " || D."
                    + ValueHolder.FDEBTOR_NAME + " like '%" + newText + "%'";
            ;

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                Debtor aDebtor = new Debtor();

                aDebtor.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                aDebtor.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_NAME)));
                aDebtor.setFDEBTOR_ADD1(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD1)));
                aDebtor.setFDEBTOR_ADD2(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD2)));
                aDebtor.setFDEBTOR_ADD3(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD3)));
                aDebtor.setFDEBTOR_TELE(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_TELE)));
                aDebtor.setFDEBTOR_MOB(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_MOB)));
                aDebtor.setFDEBTOR_EMAIL(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_EMAIL)));
                aDebtor.setFDEBTOR_AREA_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.AREACODE)));
                aDebtor.setFDEBTOR_DBGR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_DBGR_CODE)));
                aDebtor.setFDEBTOR_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));
                aDebtor.setFDEBTOR_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CRD_PERIOD)));
                aDebtor.setFDEBTOR_CHK_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CHK_CRD_PRD)));
                aDebtor.setFDEBTOR_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CRD_LIMIT)));
                aDebtor.setFDEBTOR_CHK_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CHK_CRD_LIMIT)));
                aDebtor.setFDEBTOR_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                aDebtor.setFDEBTOR_RANK_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_RANK_CODE)));

                list.add(aDebtor);

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

    public ArrayList<Debtor> getAllCustomers(String newText) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Debtor> list = new ArrayList<Debtor>();
        Cursor cursor = null;
        try {
            String selectQuery = "select * from " + ValueHolder.TABLE_DEBTOR + " where " + ValueHolder.FDEBTOR_NAME
                    + " like '%" + newText + "%'";
            // dbHelper.TABLE_FDEBTOR + " where " + dbHelper.FDEBTOR_CODE + " ||
            // " +
            // dbHelper.FDEBTOR_NAME + " like '%" + newText + "%'";
            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {

                Debtor aDebtor = new Debtor();

                aDebtor.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
                aDebtor.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_NAME)));
                aDebtor.setFDEBTOR_ADD1(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD1)));
                aDebtor.setFDEBTOR_ADD2(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD2)));
                aDebtor.setFDEBTOR_ADD3(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_ADD3)));
                aDebtor.setFDEBTOR_TELE(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_TELE)));
                aDebtor.setFDEBTOR_MOB(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_MOB)));
                aDebtor.setFDEBTOR_EMAIL(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_EMAIL)));
                aDebtor.setFDEBTOR_AREA_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.AREACODE)));
                aDebtor.setFDEBTOR_DBGR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_DBGR_CODE)));
                aDebtor.setFDEBTOR_STATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.STATUS)));
                aDebtor.setFDEBTOR_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CRD_PERIOD)));
                aDebtor.setFDEBTOR_CHK_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CHK_CRD_PRD)));
                aDebtor.setFDEBTOR_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CRD_LIMIT)));
                aDebtor.setFDEBTOR_CHK_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_CHK_CRD_LIMIT)));
                aDebtor.setFDEBTOR_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                aDebtor.setFDEBTOR_RANK_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FDEBTOR_RANK_CODE)));
                list.add(aDebtor);

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

//$%$%$%$%$% MMS 2022/01/21 &&&$%$%$%$%//
    public String getCustomerVatStatus(String debcode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT " + ValueHolder.TAX_REG + " FROM " + ValueHolder.TABLE_DEBTOR  + " where DebCode = '" + debcode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.TAX_REG));


            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return "";
    }
}
