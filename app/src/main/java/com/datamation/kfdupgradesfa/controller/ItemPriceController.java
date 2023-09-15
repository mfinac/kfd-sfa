package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.ItemPri;

import java.util.ArrayList;

public class ItemPriceController {

	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "ItemPriceController";

	public ItemPriceController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	@SuppressWarnings("static-access")
	public void InsertOrReplaceItemPri(ArrayList<ItemPri> list) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {
			dB.beginTransactionNonExclusive();
			String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_ITEMPRI + " (ItemCode,Price,PrilCode,CostCode) VALUES (?,?,?,?)";

			SQLiteStatement stmt = dB.compileStatement(sql);

			for (ItemPri itemPri : list) {


				stmt.bindString(1, itemPri.getFITEMPRI_ITEM_CODE());
				stmt.bindString(2, itemPri.getFITEMPRI_PRICE());
				stmt.bindString(3, itemPri.getFITEMPRI_PRIL_CODE());
				stmt.bindString(4, itemPri.getFITEMPRI_COST_CODE());


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
	public int deleteAllItemPri() {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		try {

			cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ITEMPRI, null);
			count = cursor.getCount();
			if (count > 0) {
				int success = dB.delete(ValueHolder.TABLE_ITEMPRI, null, null);
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

	public String getProductPriceByCode(String code) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		try {
			String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ITEMPRI + " WHERE " + ValueHolder.ITEMCODE + "='" + code + "'";


			cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				return cursor.getString(cursor.getColumnIndex(ValueHolder.PRICE));

			}
		}catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			dB.close();
		}
		return "";

	}
	public String getPrilCodeByItemCode(String code) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		try {
			String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ITEMPRI + " WHERE " + ValueHolder.ITEMCODE + "='" + code + "'";

			cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				return cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE));

			}

		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			dB.close();
		}

		return "";

	}
}
