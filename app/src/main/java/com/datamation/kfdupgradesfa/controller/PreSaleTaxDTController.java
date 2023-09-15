package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.OrderDetail;
import com.datamation.kfdupgradesfa.model.TaxDT;
import com.datamation.kfdupgradesfa.model.TaxDet;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PreSaleTaxDTController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "PreSaleTaxDTController";

    public PreSaleTaxDTController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int UpdateSalesTaxDT(ArrayList<OrderDetail> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (OrderDetail invDet : list) {


                    ArrayList<TaxDet> taxcodelist = new TaxDetController(context).getTaxInfoByComCode(invDet.getFORDDET_TAX_COM_CODE());
                    BigDecimal amt = new BigDecimal(invDet.getFORDDET_AMT());

                    if (taxcodelist.size() > 0) {

                        for (int i = taxcodelist.size() - 1; i > -1; i--) {

                            BigDecimal tax = new BigDecimal("0");
                            ContentValues values = new ContentValues();

                            values.put(ValueHolder.ITEMCODE, invDet.getFORDDET_ITEM_CODE());
                            values.put(ValueHolder.RATE, taxcodelist.get(i).getRATE());
                            values.put(ValueHolder.REFNO, invDet.getFORDDET_REFNO());
                            values.put(ValueHolder.TAXSEQ, taxcodelist.get(i).getSEQ());
                            values.put(ValueHolder.TAXCODE, taxcodelist.get(i).getTAXCODE());
                            values.put(ValueHolder.TAXCOMCODE, taxcodelist.get(i).getTAXCOMCODE());
                            values.put(ValueHolder.TAXPER, taxcodelist.get(i).getTAXVAL());
                            values.put(ValueHolder.TAXTYPE, taxcodelist.get(i).getTAXTYPE());

                            tax = new BigDecimal(taxcodelist.get(i).getTAXVAL()).multiply(amt.divide(new BigDecimal("100"), 3, BigDecimal.ROUND_HALF_EVEN));
                            amt = new BigDecimal(taxcodelist.get(i).getTAXVAL()).add(new BigDecimal("100")).multiply((amt.divide(new BigDecimal("100"), 3, BigDecimal.ROUND_HALF_EVEN)));

                            values.put(ValueHolder.BDETAMT, String.format("%.2f", tax));
                            values.put(ValueHolder.DETAMT, String.format("%.2f", tax));

                            count = (int) dB.insert(ValueHolder.TABLE_PRETAXDT, null, values);

                        }
                    }

            }
        } catch (

                Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return count;

    }

    public ArrayList<TaxDT> getAllTaxDT(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<TaxDT> list = new ArrayList<TaxDT>();
        try {
            String selectQuery = "select * from " + ValueHolder.TABLE_PRETAXDT + " WHERE RefNo='" + RefNo + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                TaxDT tax = new TaxDT();

                tax.setREFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                tax.setTAXCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCODE)));
                tax.setBTAXDETAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.BDETAMT)));
                tax.setTAXCOMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE)));
                tax.setTAXDETAMT(cursor.getString(cursor.getColumnIndex(ValueHolder.DETAMT)));
                tax.setTAXPER(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXPER)));
                tax.setTAXRATE(cursor.getString(cursor.getColumnIndex(ValueHolder.RATE)));
                tax.setTAXSEQ(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXSEQ)));
                tax.setITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));

                list.add(tax);
            }
            cursor.close();

        } catch (Exception e) {
            Log.v("Erorr ", e.toString());

        } finally {
            dB.close();
        }

        return list;

    }
//
//    public ArrayList<TaxDT> getTaxDTSummery(String RefNo) {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<TaxDT> list = new ArrayList<TaxDT>();
//        try {
//            String selectQuery = "select TaxType,TaxPer,TaxSeq,SUM(TaxDetAmt) as TotTax FROM " + TABLE_PRETAXDT + " WHERE RefNo='" + RefNo + "' GROUP BY TaxType ORDER BY TaxSeq ASC";
//
//            Cursor cursor = dB.rawQuery(selectQuery, null);
//
//            while (cursor.moveToNext()) {
//                TaxDT tax = new TaxDT();
//
//                tax.setTAXDETAMT(cursor.getString(cursor.getColumnIndex("TotTax")));
//                tax.setTAXPER(cursor.getString(cursor.getColumnIndex(PRETAXDT_TAXPER)));
//                tax.setTAXSEQ(cursor.getString(cursor.getColumnIndex(PRETAXDT_SEQ)));
//                tax.setTAXTYPE(cursor.getString(cursor.getColumnIndex(PRETAXDT_TAXTYPE)));
//
//                list.add(tax);
//            }
//            cursor.close();
//
//        } catch (Exception e) {
//            Log.v("Erorr ", e.toString());
//
//        } finally {
//            dB.close();
//        }
//
//        return list;
//
//    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void ClearTable(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {

            dB.delete(ValueHolder.TABLE_PRETAXDT, ValueHolder.REFNO + "='" + RefNo + "'", null);
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

    }

}
