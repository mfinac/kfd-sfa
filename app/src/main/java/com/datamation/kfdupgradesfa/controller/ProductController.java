package com.datamation.kfdupgradesfa.controller;

import java.util.ArrayList;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class ProductController {
	 Context context;
	    private SQLiteDatabase dB;
	    private DatabaseHelper dbHelper;

	    public ProductController(Context context) {
	        this.context = context;
	        dbHelper = new DatabaseHelper(context);
	    }

	    public void open() throws SQLException {
	        dB = dbHelper.getWritableDatabase();
	    }


	    public void insertOrUpdateProducts(ArrayList<Product> list) {
	        if (dB == null) {
	            open();
	        } else if (!dB.isOpen()) {
	            open();
	        }

	        try {
	            dB.beginTransactionNonExclusive();
	            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_PRODUCT + " (itemcode,itemname,price,qoh,NOUCase,Pack,qty,fSchema,SupCode) VALUES (?,?,?,?,?,?,?,?,?)";

	            SQLiteStatement stmt = dB.compileStatement(sql);

	            for (Product items : list) {

	                stmt.bindString(1, items.getFPRODUCT_ITEMCODE());
	                stmt.bindString(2, items.getFPRODUCT_ITEMNAME());
	                stmt.bindString(3, items.getFPRODUCT_PRICE());
	                stmt.bindString(4, items.getFPRODUCT_QOH());
	                stmt.bindString(5, items.getFPRODUCT_NOUCASE());
	                stmt.bindString(6, items.getFPRODUCT_PACK());
	                stmt.bindString(7, items.getFPRODUCT_QTY());
	                stmt.bindString(8, items.getFPRODUCT_FREESCREMA());
	                stmt.bindString(9, items.getSupCode());

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


	    public boolean tableHasRecords() {

	        if (dB == null) {
	            open();
	        } else if (!dB.isOpen()) {
	            open();
	        }
	        boolean result = false;
	        Cursor cursor = null;

	        try {
	            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT, null);

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

	    public ArrayList<Product> getAllItems(String newText,String supcode,String debCode) {

	        if (dB == null) {
	            open();
	        } else if (!dB.isOpen()) {
	            open();
	        }
	        Cursor cursor = null;
	        ArrayList<Product> list = new ArrayList<Product>();
	        try {
	        	
	        	if (supcode.length()>0){
	        		String supsearchsql = "";
	        		supsearchsql = "SELECT * FROM " + ValueHolder.TABLE_PRODUCT + " WHERE itemname LIKE '" + newText + "%' AND SupCode = '" + supcode + "'"; 
	        		cursor = dB.rawQuery(supsearchsql , null);	
	        	}else{
	        		String searchsql = "";
	        		searchsql = "SELECT * FROM " + ValueHolder.TABLE_PRODUCT + " WHERE itemname LIKE '" + newText + "%'";
	        		cursor = dB.rawQuery( searchsql, null);
	        		// old 09-04-2018 cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT + " WHERE itemcode || itemname LIKE '%" + newText + "%' AND SupCode = '" + supcode + "'" , null);
	        	}
	            

	            while (cursor.moveToNext()) {
	                Product product = new Product();
	                product.setFPRODUCT_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_ID)));
	                product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_ITEMCODE)));
	                product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_ITEMNAME)));
	                product.setFPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_PRICE)));
	                product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_QOH)));
	                product.setFPRODUCT_NOUCASE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_NOUCASE)));
	                product.setFPRODUCT_PACK(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_PACK)));
	                product.setFPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_QTY)));
	                product.setFPRODUCT_FREESCREMA(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_FREESCHEMA)));
	                product.setSupCode(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_supCode)));
	                product.setFPRODUCT_DEBCODE(debCode);
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
	    
	    public ArrayList<Product> getAllItemsbySupCode(String supcode) {

	        if (dB == null) {
	            open();
	        } else if (!dB.isOpen()) {
	            open();
	        }
	        Cursor cursor = null;
	        ArrayList<Product> list = new ArrayList<Product>();
	        try {
	        	String sql="SELECT * FROM " + ValueHolder.TABLE_PRODUCT + " WHERE SupCode = '" + supcode +"'";
	        	cursor = dB.rawQuery(sql , null);

	            while (cursor.moveToNext()) {
	                Product product = new Product();
	                product.setFPRODUCT_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_ID)));
	                product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_ITEMCODE)));
	                product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_ITEMNAME)));
	                product.setFPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_PRICE)));
	                product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_QOH)));
	                product.setFPRODUCT_NOUCASE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_NOUCASE)));
	                product.setFPRODUCT_PACK(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_PACK)));
	                product.setFPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_QTY)));
	                product.setFPRODUCT_FREESCREMA(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_FREESCHEMA)));
	                product.setSupCode(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_supCode)));
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


	    public void updateProductQty(String itemCode, String qty) {

	        if (dB == null) {
	            open();
	        } else if (!dB.isOpen()) {
	            open();
	        }

	        try {

	            ContentValues values = new ContentValues();
	            values.put(ValueHolder.FPRODUCT_QTY, qty);
	            dB.update(ValueHolder.TABLE_PRODUCT, values, ValueHolder.FPRODUCT_ITEMCODE + " =?", new String[]{String.valueOf(itemCode)});

	        } catch (Exception e) {
	            Log.v(" Exception", e.toString());
	        } finally {
	            dB.close();
	        }
	    }
	//---------------------------------added by dhanushika----------------------------------------------------------------------------


	    public int updateProductQtyfor(String itemCode, String qty) {
	        int count = 0;
	        if (dB == null) {
	            open();
	        } else if (!dB.isOpen()) {
	            open();
	        }

	        try {

	            ContentValues values = new ContentValues();
	            values.put(ValueHolder.FPRODUCT_QTY, qty);
	            count=(int)dB.update(ValueHolder.TABLE_PRODUCT, values, ValueHolder.FPRODUCT_ITEMCODE + " =?", new String[]{String.valueOf(itemCode)});

	        } catch (Exception e) {
	        e.printStackTrace();
	        } finally {
	            dB.close();
	        }
	        return  count;
	    }


	    public ArrayList<Product> getSelectedItems() {

	        if (dB == null) {
	            open();
	        } else if (!dB.isOpen()) {
	            open();
	        }
	        Cursor cursor = null;
	        ArrayList<Product> list = new ArrayList<Product>();
	        try {
	            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT + " WHERE  qty<>'0'", null);

	            while (cursor.moveToNext()) {
	                Product product = new Product();
	                product.setFPRODUCT_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_ID)));
	                product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_ITEMCODE)));
	                product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_ITEMNAME)));
	                product.setFPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_PRICE)));
	                product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_QOH)));
	                product.setFPRODUCT_NOUCASE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_NOUCASE)));
	                product.setFPRODUCT_PACK(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_PACK)));
	                product.setFPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_QTY)));
	                product.setFPRODUCT_FREESCREMA(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_FREESCHEMA)));
	                product.setSupCode(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_supCode)));
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


	    /*public ArrayList<InvDet> getSelectedItemsForInvoice(String Refno) {

	        if (dB == null) {
	            open();
	        } else if (!dB.isOpen()) {
	            open();
	        }
	        Cursor cursor = null;
	        ArrayList<InvDet> list = new ArrayList<>();
	        try {
	            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_PRODUCT + " WHERE  qty<>'0'", null);

	            while (cursor.moveToNext()) {
	                InvDet invDet = new InvDet();
	                invDet.setFINVDET_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_ID)));
	                invDet.setFINVDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_ITEMCODE)));
	                invDet.setFINVDET_REFNO(Refno);
	                invDet.setFINVDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_PRICE)));
	                invDet.setFINVDET_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_QOH)));
	                invDet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FPRODUCT_QTY)));
	                list.add(invDet);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            cursor.close();
	            dB.close();
	        }

	        return list;
	    }
*/
	    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*/

	    public void mClearTables() {

	        if (dB == null) {
	            open();
	        } else if (!dB.isOpen()) {
	            open();
	        }
	        try {
	            dB.delete(ValueHolder.TABLE_PRODUCT, null, null);

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            dB.close();
	        }
	    }
}
