package com.datamation.kfdupgradesfa.controller;

import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.FddbNote;

import java.util.ArrayList;

public class OutstandingController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "OutstandingController ";

    public OutstandingController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateFDDbNote(ArrayList<FddbNote> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_FDDBNOTE +
                    " (RefNo," +
                    " RepName," +
                    " Remarks," +
                    " EntRemark," +
                    " PdaAmt," +
                    " RefInv," +
                    " Amt," +
                    " SaleRefNo," +
                    " ManuRef," +
                    " TxnType," +
                    " TxnDate," +
                    " CurCode," +
                    " CurRate," +
                    " DebCode," +
                    " RepCode," +
                    " TaxComCode," +
                    " TaxAmt," +
                    " BTaxAmt," +
                    " BAmt," +
                    " TotBal," +
                    " TotBal1," +
                    " OvPayAmt," +
                    " RefNo1,EnterAmt) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (FddbNote fddbNote : list) {
                stmt.bindString(1, fddbNote.getFDDBNOTE_REFNO());
                stmt.bindString(2, fddbNote.getFDDBNOTE_REPNAME());
                stmt.bindString(3, fddbNote.getFDDBNOTE_REMARKS());
                stmt.bindString(4, fddbNote.getFDDBNOTE_ENT_REMARK());
                stmt.bindString(5, fddbNote.getFDDBNOTE_PDA_AMT());
                stmt.bindString(6, fddbNote.getFDDBNOTE_REF_INV());
                stmt.bindString(7, fddbNote.getFDDBNOTE_AMT());
                stmt.bindString(8, fddbNote.getFDDBNOTE_SALE_REF_NO());
                stmt.bindString(9, fddbNote.getFDDBNOTE_MANU_REF());
                stmt.bindString(10, fddbNote.getFDDBNOTE_TXN_TYPE());
                stmt.bindString(11, fddbNote.getFDDBNOTE_TXN_DATE());
                stmt.bindString(12, fddbNote.getFDDBNOTE_CUR_CODE());
                stmt.bindString(13, fddbNote.getFDDBNOTE_CUR_RATE());
                stmt.bindString(14, fddbNote.getFDDBNOTE_DEB_CODE());
                stmt.bindString(15, fddbNote.getFDDBNOTE_REP_CODE());
                stmt.bindString(16, fddbNote.getFDDBNOTE_TAX_COM_CODE());
                stmt.bindString(17, fddbNote.getFDDBNOTE_TAX_AMT());
                stmt.bindString(18, fddbNote.getFDDBNOTE_B_TAX_AMT());
                stmt.bindString(19, fddbNote.getFDDBNOTE_B_AMT());
                stmt.bindString(20, fddbNote.getFDDBNOTE_TOT_BAL());
                stmt.bindString(21, fddbNote.getFDDBNOTE_TOT_BAL1());
                stmt.bindString(22, fddbNote.getFDDBNOTE_OV_PAY_AMT());
                stmt.bindString(23, fddbNote.getFDDBNOTE_REFNO1());
                stmt.bindString(24, "0.00");

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

    public int updateFDDbNote(ArrayList<FddbNote> list) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            for (FddbNote fdDbNote : list) {

                cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_FDDBNOTE + " WHERE " + ValueHolder.REFNO + "='" + fdDbNote.getFDDBNOTE_REFNO() + "'", null);

                ContentValues values = new ContentValues();

                values.put(ValueHolder.REFNO, fdDbNote.getFDDBNOTE_REFNO());
                values.put(ValueHolder.REPNAME, fdDbNote.getFDDBNOTE_REPNAME());
                values.put(ValueHolder.REMARKS, fdDbNote.getFDDBNOTE_REMARKS());
                values.put(ValueHolder.ENT_REMARK, fdDbNote.getFDDBNOTE_ENT_REMARK());
                values.put(ValueHolder.PDAAMT, fdDbNote.getFDDBNOTE_PDA_AMT());
                values.put(ValueHolder.REF_INV, fdDbNote.getFDDBNOTE_REF_INV());
                values.put(ValueHolder.AMT, fdDbNote.getFDDBNOTE_AMT());
                values.put(ValueHolder.SALE_REF_NO, fdDbNote.getFDDBNOTE_SALE_REF_NO());
                values.put(ValueHolder.MANU_REF, fdDbNote.getFDDBNOTE_MANU_REF());
                values.put(ValueHolder.TXNTYPE, fdDbNote.getFDDBNOTE_TXN_TYPE());
                values.put(ValueHolder.TXNDATE, fdDbNote.getFDDBNOTE_TXN_DATE());
                values.put(ValueHolder.CUR_CODE, fdDbNote.getFDDBNOTE_CUR_CODE());
                values.put(ValueHolder.CUR_RATE, fdDbNote.getFDDBNOTE_CUR_RATE());
                values.put(ValueHolder.DEBCODE, fdDbNote.getFDDBNOTE_DEB_CODE());
                values.put(ValueHolder.REPCODE, fdDbNote.getFDDBNOTE_REP_CODE());
                values.put(ValueHolder.TAXCOMCODE, fdDbNote.getFDDBNOTE_TAX_COM_CODE());
                values.put(ValueHolder.TAXAMT, fdDbNote.getFDDBNOTE_TAX_AMT());
                values.put(ValueHolder.BT_TAX_AMT, fdDbNote.getFDDBNOTE_B_TAX_AMT());
                values.put(ValueHolder.B_AMT, fdDbNote.getFDDBNOTE_B_AMT());
                values.put(ValueHolder.TOT_BAL, fdDbNote.getFDDBNOTE_TOT_BAL());
                values.put(ValueHolder.TOT_BAL1, fdDbNote.getFDDBNOTE_TOT_BAL1());
                values.put(ValueHolder.OV_PAY_AMT, fdDbNote.getFDDBNOTE_OV_PAY_AMT());
                values.put(ValueHolder.REFNO1, fdDbNote.getFDDBNOTE_REFNO1());
                values.put(ValueHolder.ENTER_AMT, fdDbNote.getFDDBNOTE_ENTER_AMT());


                if (cursor.getCount() > 0) {
                    dB.update(ValueHolder.TABLE_FDDBNOTE, values, ValueHolder.REFNO + "=?", new String[]{fdDbNote.getFDDBNOTE_REFNO().toString()});
                } else {
                    count = (int) dB.insert(ValueHolder.TABLE_FDDBNOTE, null, values);
                }

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

    /*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int deleteAll() {
        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_FDDBNOTE, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_FDDBNOTE, null, null);
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


    public ArrayList<FddbNote> getAllRecords(String debcode, boolean isSummery) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<FddbNote> list = new ArrayList<FddbNote>();
        try {

            String selectQuery;

            if (isSummery) {
                selectQuery = "select * from " + ValueHolder.TABLE_FDDBNOTE + " WHERE " + " DebCode = '" + debcode + "' AND EnterAmt<>'' AND TotBal > 0.0 Order By " + ValueHolder.TXNDATE;
            } else {
                selectQuery = "select * from " + ValueHolder.TABLE_FDDBNOTE + " WHERE DebCode = '" + debcode + "' AND TotBal > 0.0 Order By TxnDate ";
                //   selectQuery = "select * from " + ValueHolder.TABLE_FDDBNOTE + " WHERE " + " DebCode = '" + debcode + "' AND TotBal > 0.0 Order By " + ValueHolder.TXNDATE;
            }
            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                FddbNote fdDbNote = new FddbNote();

                fdDbNote.setFDDBNOTE_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.AMT)));
                fdDbNote.setFDDBNOTE_B_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.B_AMT)));
                fdDbNote.setFDDBNOTE_B_TAX_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.BT_TAX_AMT)));
                fdDbNote.setFDDBNOTE_CUR_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_CODE)));
                fdDbNote.setFDDBNOTE_CUR_RATE(cursor.getString(cursor.getColumnIndex(ValueHolder.CUR_RATE)));
                fdDbNote.setFDDBNOTE_DEB_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEBCODE)));
//                String entAmt = (cursor.getString(cursor.getColumnIndex(ValueHolder.ENTER_AMT)) == "") ? "0" : cursor.getString(cursor.getColumnIndex(ValueHolder.ENTER_AMT));
                fdDbNote.setFDDBNOTE_ENTER_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.ENTER_AMT)));
                fdDbNote.setFDDBNOTE_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                fdDbNote.setFDDBNOTE_MANU_REF(cursor.getString(cursor.getColumnIndex(ValueHolder.MANU_REF)));
                fdDbNote.setFDDBNOTE_OV_PAY_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.OV_PAY_AMT)));
                fdDbNote.setFDDBNOTE_REF_INV(cursor.getString(cursor.getColumnIndex(ValueHolder.REF_INV)));
                fdDbNote.setFDDBNOTE_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                fdDbNote.setFDDBNOTE_REFNO1(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO1)));
                fdDbNote.setFDDBNOTE_REP_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                fdDbNote.setFDDBNOTE_SALE_REF_NO(cursor.getString(cursor.getColumnIndex(ValueHolder.SALE_REF_NO)));
                fdDbNote.setFDDBNOTE_TAX_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXAMT)));
                fdDbNote.setFDDBNOTE_TAX_COM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE)));
                fdDbNote.setFDDBNOTE_TOT_BAL(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL)));
                fdDbNote.setFDDBNOTE_TOT_BAL1(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL1)));
                fdDbNote.setFDDBNOTE_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                fdDbNote.setFDDBNOTE_TXN_TYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNTYPE)));
                fdDbNote.setFDDBNOTE_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
                fdDbNote.setFDDBNOTE_REPNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.REPNAME)));
                fdDbNote.setFDDBNOTE_PDA_AMT(cursor.getString(cursor.getColumnIndex(ValueHolder.PDAAMT)));
                fdDbNote.setFDDBNOTE_ENT_REMARK(cursor.getString(cursor.getColumnIndex(ValueHolder.ENT_REMARK)));

                list.add(fdDbNote);

            }
        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return list;
    }


    public ArrayList<FddbNote> getAllAmountAllocatedRecordsByDebCode(String debcode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<FddbNote> list = new ArrayList<FddbNote>();
        try {

            String selectQuery;


            selectQuery = "select * from " + ValueHolder.TABLE_FDDBNOTE + " WHERE " + " DebCode = '" + debcode + "' AND EnterAmt<>''  ";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                FddbNote fdDbNote = new FddbNote();

                fdDbNote.setFDDBNOTE_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));

                list.add(fdDbNote);

            }
        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return list;
    }

    public double getDebtorBalance(String DebCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        double totbal = 0, totbal1 = 0;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT TotBal,TotBal1 FROM " + ValueHolder.TABLE_FDDBNOTE + " WHERE DebCode ='" + DebCode + "'";
            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                totbal = totbal + Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL)));
                totbal1 = totbal1 + Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL1)));
            }

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return totbal - totbal1;

    }

    public ArrayList<FddbNote> getDebtInfo(String DebCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FddbNote> list = new ArrayList<FddbNote>();
        try {
            String selectQuery;

            if (DebCode.equals(""))
                selectQuery = "SELECT refno,totbal,totbal1,txndate FROM " + ValueHolder.TABLE_FDDBNOTE;
            else
                selectQuery = "SELECT refno,totbal,totbal1,txndate FROM " + ValueHolder.TABLE_FDDBNOTE + " WHERE DebCode ='" + DebCode + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                FddbNote dbNote = new FddbNote();
                dbNote.setFDDBNOTE_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                dbNote.setFDDBNOTE_TXN_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                dbNote.setFDDBNOTE_TOT_BAL(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL)));
                dbNote.setFDDBNOTE_TOT_BAL1(cursor.getString(cursor.getColumnIndex(ValueHolder.TOT_BAL1)));
                list.add(dbNote);
            }

            cursor.close();

        } catch (Exception e) {
            Log.v(TAG, e.toString());

        } finally {
            dB.close();
        }

        return list;

    }

    public int ClearFddbNoteData() {

        int result = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            ContentValues values = new ContentValues();
            values.put(ValueHolder.ENTER_AMT, "");
            values.put(ValueHolder.ENT_REMARK, "");
            result = dB.update(ValueHolder.TABLE_FDDBNOTE, values, null, null);
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }
        return result;
    }

    public void UpdateFddbNoteBalance(ArrayList<FddbNote> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            for (FddbNote fddb : list) {
                ContentValues values = new ContentValues();
                values.put(ValueHolder.TOT_BAL, Double.parseDouble(fddb.getFDDBNOTE_TOT_BAL()) - Double.parseDouble(fddb.getFDDBNOTE_ENTER_AMT()));
                values.put(ValueHolder.ENTER_AMT, "");
                values.put(ValueHolder.REMARKS, "");
                dB.update(ValueHolder.TABLE_FDDBNOTE, values, ValueHolder.REFNO + "=?", new String[]{fddb.getFDDBNOTE_REFNO().toString()});
            }
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

    }

    public int ClearFddbNoteBalance(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Integer resCount = 0;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FDDBNOTE + " WHERE RefNo ='" + RefNo + "' AND EnterAmt <> 0";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put(ValueHolder.ENTER_AMT, 0);
                values.put(ValueHolder.REMARKS, "");
                resCount = dB.update(ValueHolder.TABLE_FDDBNOTE, values, ValueHolder.REFNO + "=?", new String[]{RefNo});

            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }
        return resCount;

    }

    public boolean IsAmountEmpty(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        boolean response = false;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FDDBNOTE + " WHERE RefNo ='" + RefNo + "' AND (EnterAmt <> null OR  EnterAmt > 0) ";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                response = true;
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }
        return response;

    }


}

