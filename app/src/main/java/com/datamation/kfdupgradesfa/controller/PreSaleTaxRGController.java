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
import com.datamation.kfdupgradesfa.model.TaxDet;
import com.datamation.kfdupgradesfa.model.TaxRG;

import java.util.ArrayList;

public class PreSaleTaxRGController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "PreSaleTaxRGController ";
    public static final String PRETAXRG_RGNO = "RGNo";

    public PreSaleTaxRGController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int UpdateSalesTaxRG(ArrayList<OrderDetail> list, String debtorCode) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            Cursor cursor = null;
            for (OrderDetail ordDet : list) {


                    ArrayList<TaxDet> taxcodelist = new TaxDetController(context).getTaxInfoByComCode(ordDet.getFORDDET_TAX_COM_CODE());

                    for (TaxDet taxDet : taxcodelist) {

                        String s = "SELECT * FROM " + ValueHolder.TABLE_PRETAXRG + " WHERE " + ValueHolder.REFNO + "='" + ordDet.getFORDDET_REFNO() + "' AND " + ValueHolder.TAXCODE + "='" + taxDet.getTAXCODE() + "'";

                        cursor = dB.rawQuery(s, null);

                        ContentValues values = new ContentValues();
                        values.put(ValueHolder.REFNO, ordDet.getFORDDET_REFNO());
                        values.put(ValueHolder.TAXREGNO, new TaxController(context).getTaxRGNo(taxDet.getTAXCODE()));
                        values.put(ValueHolder.TAXCODE, taxDet.getTAXCODE());

                        if (cursor.getCount() <= 0)
                            count = (int) dB.insert(ValueHolder.TABLE_PRETAXRG, null, values);


                }
            }
            cursor.close();
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return count;

    }

    public ArrayList<TaxRG> getAllTaxRG(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<TaxRG> list = new ArrayList<TaxRG>();
        try {
            String selectQuery = "select * from " + ValueHolder.TABLE_PRETAXRG + " WHERE RefNo='" + RefNo + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                TaxRG tax = new TaxRG();

                tax.setREFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                tax.setTAXCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCODE)));
                tax.setRGNO(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXREGNO)));
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

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void ClearTable(String RefNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {

            dB.delete(ValueHolder.TABLE_PRETAXRG, ValueHolder.REFNO + "='" + RefNo + "'", null);
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

    }

}
