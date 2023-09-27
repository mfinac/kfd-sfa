package com.datamation.kfdupgradesfa.controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.PreProduct;
import com.datamation.kfdupgradesfa.model.Product;

import java.util.ArrayList;

public class PreProductController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    public PreProductController(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public boolean tableHasRecords() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        boolean result = false;
        Cursor cursor = null;

        try {
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT_PRE, null);
            Log.d("1017 - Table has ",">>>>>>"+cursor.getCount());
            if (cursor.getCount() > 0)
                result = true;
            else
                result = false;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();

        }

        return result;

    }


    public ArrayList<PreProduct> getAllItems(String newText) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<PreProduct> list = new ArrayList<>();
        try {
             cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT_PRE + " WHERE itemcode || itemname LIKE '%" + newText + "%'  group by itemcode order by CAST(qoh AS FLOAT) desc", null);


            while (cursor.moveToNext()) {
                PreProduct product = new PreProduct();
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                product.setPREPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRICE)));
                product.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }

    public void updateProductQty(String itemCode, String qty, String type) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(ValueHolder.QTY, qty);
            dB.update(ValueHolder.TABLE_PRODUCT_PRE, values,  ValueHolder.ITEMCODE
                    + " = '" + itemCode + "'  ", null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }
    public int updateQuantities(String itemCode,String itemname, String price, String qty, String refno) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_PRODUCT_PRE + " WHERE " + ValueHolder.ITEMCODE
                    + " = '" + itemCode + "' ";

            cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();
            values.put(ValueHolder.ITEMCODE, itemCode);
            values.put(ValueHolder.ITEMNAME, itemname);
            values.put(ValueHolder.PRICE, price);
           // values.put(ValueHolder.QOH, qoh);
            values.put(ValueHolder.QTY, qty);


            int cn = cursor.getCount();
            if (cn > 0) {
                count = dB.update(ValueHolder.TABLE_PRODUCT_PRE, values, ValueHolder.ITEMCODE
                        + " = '" + itemCode + "'",
                        null);
            } else {
                count = (int) dB.insert(ValueHolder.TABLE_PRODUCT_PRE, null, values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
        return count;
    }
   @SuppressLint("Range")
   public ArrayList<PreProduct> getSelectedItems() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<PreProduct> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT_PRE + " WHERE   qty<>'0'", null);

            while (cursor.moveToNext()) {
                PreProduct product = new PreProduct();
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                product.setPREPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRICE)));
                product.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));

                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }
    public void mClearTables() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            dB.delete(ValueHolder.TABLE_PRODUCT_PRE, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }


    public void insertOrUpdatePreProducts(Product product) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_PRODUCT_PRE + " (itemcode,itemname,price,qoh,qty) VALUES (?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
                stmt.bindString(1, product.getFPRODUCT_ITEMCODE());
                stmt.bindString(2, product.getFPRODUCT_ITEMNAME());
                double price = Double.parseDouble(product.getFPRODUCT_QTY())  * Double.parseDouble(product.getFPRODUCT_PRICE());
                stmt.bindString(3, price+"");
                stmt.bindString(4, product.getFPRODUCT_QOH());
                stmt.bindString(5, product.getFPRODUCT_QTY());

                stmt.execute();
                stmt.clearBindings();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dB.setTransactionSuccessful();
            dB.endTransaction();
            dB.close();
        }


    }


    public void insertIntoProductAsBulkForPre(String LocCode)
    {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try
        {

                String insertQuery2;
                insertQuery2 = "INSERT INTO TblProductsPre (itemcode,itemname,price,qoh,qty) " +
                        "SELECT  itm.ItemCode AS ItemCode , itm.ItemName AS ItemName ,  \n" +
                        "                        IFNULL(pri.Price,0.0) AS Price , \n" +

                        "                        loc.QOH AS QOH , '0' AS Qty " +
                        " FROM TblItem itm\n" +
                        "                        INNER JOIN TblItemLoc loc ON loc.ItemCode = itm.ItemCode \n" +
                        "                        LEFT JOIN TblItemPri pri ON pri.ItemCode = itm.ItemCode  \n" +
                        "                        WHERE loc.LocCode = '"+LocCode+"' AND pri.ItemCode = itm.ItemCode \n" +
                        "                        Group by  itm.ItemCode ORDER BY CAST(QOH AS FLOAT) DESC ";

                dB.execSQL(insertQuery2);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if(dB.isOpen())
            {
                dB.close();
            }
        }
    }



    public int insertOrUpdatePreProductsNew(Product product) {
        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

                Cursor cursor = null;
                ContentValues values = new ContentValues();


                String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ORDDET + " WHERE " + ValueHolder.ITEMCODE
                        + " = '" + product.getFPRODUCT_ITEMCODE() + "' and " + ValueHolder.ITEMNAME
                        + " = '" + product.getFPRODUCT_ITEMNAME() + "' ";

                cursor = dB.rawQuery(selectQuery, null);
                values.put(ValueHolder.ITEMCODE, product.getFPRODUCT_ITEMCODE());
                values.put(ValueHolder.ITEMNAME, product.getFPRODUCT_ITEMNAME());
                values.put(ValueHolder.PRICE, product.getFPRODUCT_PRICE());
                values.put(ValueHolder.QOH, product.getFPRODUCT_QOH());
                values.put(ValueHolder.QTY, product.getFPRODUCT_QTY());


                int cn = cursor.getCount();

                if (cn > 0) {
                    count = dB.update(ValueHolder.TABLE_PRODUCT_PRE, values, ValueHolder.ITEMCODE + " = '" + product.getFPRODUCT_ITEMCODE() + "'  and " + ValueHolder.ITEMNAME + " = '" + product.getFPRODUCT_ITEMNAME() + "'", null);
                } else {
                    count = (int) dB.insert(ValueHolder.TABLE_PRODUCT_PRE, null, values);
                }

                cursor.close();

        } catch (Exception e) {

            // Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return count;


    }


}
