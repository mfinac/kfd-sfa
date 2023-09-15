package com.datamation.kfdupgradesfa.controller;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Reason;

import java.util.ArrayList;

public class ReasonController {

	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "ReasonController";

	public ReasonController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	public void createOrUpdateReason(ArrayList<Reason> list) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {
			dB.beginTransactionNonExclusive();
			String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_REASON +
					" (ReaCode," +
					"ReaName," +
					"ReaTcode) " + " VALUES (?,?,?)";

			SQLiteStatement stmt = dB.compileStatement(sql);
			for (Reason reason : list) {

				stmt.bindString(1, reason.getFREASON_REATCODE());
				stmt.bindString(2, reason.getFREASON_NAME());
				stmt.bindString(3, reason.getFREASON_REATCODE());

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

	/*
	 * delete code
	 */
	@SuppressWarnings("static-access")
	public int deleteAll() {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		try {

			cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_REASON, null);
			count = cursor.getCount();
			if (count > 0) {
				int success = dB.delete(ValueHolder.TABLE_REASON, null, null);
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

	public ArrayList<String> getAllReasonsByType(String code) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<String> list = new ArrayList<String>();

		String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_REASON + " WHERE trim(" + ValueHolder.TYPE + ") ='" + code + "'";

		Cursor cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {

			String reason = "";

			reason =  cursor.getString(cursor.getColumnIndex(ValueHolder.REACODE))+" - "+cursor.getString(cursor.getColumnIndex(ValueHolder.REANAME));

			list.add(reason);

		}

		return list;
	}
	public ArrayList<Reason> getAllReasons() {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reason> list = new ArrayList<Reason>();

		String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_REASON;

		Cursor cursor = dB.rawQuery(selectQuery, null);
		while (cursor.moveToNext()) {

			Reason reason = new Reason();

			reason.setFREASON_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REACODE)));
			reason.setFREASON_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.REANAME)));

			list.add(reason);

		}

		return list;
	}
	public ArrayList<String> getReasonName() {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<String> FREASON = new ArrayList<String>();

		//String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FREASON + " WHERE " + dbHelper.FREASON_CODE + "='RT01' OR dbHelper.FREASON_CODE='RT02'";
		String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_REASON;

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);
		FREASON.add("Tap to select a Reason");

		while (cursor.moveToNext()) {

			FREASON.add(cursor.getString(cursor.getColumnIndex(ValueHolder.REANAME)));

		}

		return FREASON;
	}

	public String getReaCodeByName(String name) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_REASON + " WHERE " + ValueHolder.REANAME + "='" + name + "'";

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex(ValueHolder.REACODE));
		}

		return "";
	}


	public String getReasonByReaCode(String reaCode) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_REASON + " WHERE " + ValueHolder.REACODE + "='" + reaCode + "'";

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex(ValueHolder.REANAME));
		}

		return "";
	}

	public ArrayList<Reason> getAllNonPrdReasons() {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reason> list = new ArrayList<Reason>();

		String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_REASON;

		Cursor cursor = dB.rawQuery(selectQuery, null);
		while (cursor.moveToNext()) {

			Reason FREASON = new Reason();

			FREASON.setFREASON_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REACODE)));
			FREASON.setFREASON_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.REANAME)));

			list.add(FREASON);

		}

		return list;
	}

	public ArrayList<Reason> getDebDetails(String searchword) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reason> Itemname = new ArrayList<Reason>();

		String selectQuery = "select * from fReason where ReaTcode='RT02' AND ReaCode LIKE '%" + searchword + "%' OR ReaName LIKE '%" + searchword + "%' ";

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {
			Reason items = new Reason();

			items.setFREASON_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REACODE)));
			items.setFREASON_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.REANAME)));
			Itemname.add(items);
		}

		return Itemname;
	}

}
