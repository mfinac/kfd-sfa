package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.CompanyBranch;
import com.datamation.kfdupgradesfa.model.Reference;
import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.settings.ReferenceNum;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReferenceController {
	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "ReferenceController";

	public ReferenceController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	public void isNewMonth(String cCode) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {

			//String RepCode = new SalRepDS(context).getCurrentRepCode();

			Calendar c = Calendar.getInstance();
			Cursor cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_FCOMPANYBRANCH + " WHERE cSettingsCode='" + cCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'", null);

			if (cursor.getCount() == 0) {

				ContentValues values = new ContentValues();
			//	values.put(DatabaseHelper.REFERENCE_REPCODE, RepCode);
			//	values.put(DatabaseHelper.FCOMPANYBRANCH_ID, "");
				values.put(ValueHolder.CSETTINGS_CODE, cCode);
				values.put(ValueHolder.NNUM_VAL, "1");
				values.put(ValueHolder.FCOMPANYBRANCH_YEAR, String.valueOf(c.get(Calendar.YEAR)));
				values.put(ValueHolder.FCOMPANYBRANCH_MONTH, String.valueOf(c.get(Calendar.MONTH) + 1));

				dB.insert(ValueHolder.TABLE_FCOMPANYBRANCH, null, values);

			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}

	}
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public String getNextNumVal(String cSettingsCode) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String nextNumVal = "";

		Calendar c = Calendar.getInstance();

		try {
			String query = "SELECT " + ValueHolder.NNUM_VAL + " FROM " + ValueHolder.TABLE_FCOMPANYBRANCH + " WHERE " + ValueHolder.BRANCH_CODE + " in (SELECT " + ValueHolder.REPCODE + " FROM " + ValueHolder.TABLE_SALREP + ")  AND cSettingsCode='" + cSettingsCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";
			Cursor cursor = dB.rawQuery(query, null);
			int count = cursor.getCount();
			if (count > 0) {
				while (cursor.moveToNext()) {
					nextNumVal = cursor.getString(0);
				}
			} else {
				nextNumVal = "1";
			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}

		return nextNumVal;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public ArrayList<Reference> getCurrentPreFix(String cSettingsCode) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reference> list = new ArrayList<Reference>();

		try {
			String selectRep = "select c.cCharVal, s.Prefix from TblcompanySetting c, TblsalRep s where c.cSettingscode='" + cSettingsCode + "'";

			Cursor cursor = null;
			cursor = dB.rawQuery(selectRep, null);

			while (cursor.moveToNext()) {

				Reference reference = new Reference();

				reference.setCharVal(cursor.getString(cursor.getColumnIndex(ValueHolder.CHAR_VAL)));
				// reference.setDealPreFix(cursor.getString(cursor.getColumnIndex(dbHelper.FDEALER_PREFIX)));
				reference.setRepPrefix(cursor.getString(cursor.getColumnIndex(ValueHolder.FSALREP_PREFIX)));
				list.add(reference);

			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}
		return list;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public String getCurrentRefNo(String cSettingsCode) {

		String preFix = "";
		DecimalFormat dFormat = new DecimalFormat("0000");

		Calendar c = Calendar.getInstance();
		/* Check if its new month. if so update fCompanyBranch */
		//referenceDS.isNewMonth(cSettingsCode);

		String sDate = String.valueOf(c.get(Calendar.YEAR)).substring(2) + String.format("%02d", c.get(Calendar.MONTH) + 1);

		String nextNumVal = getNextNumVal(cSettingsCode);
		ArrayList<Reference> list = getCurrentPreFix(cSettingsCode);

		if (!nextNumVal.equals("")) {

			for (Reference reference : list) {
				preFix = reference.getCharVal() + reference.getRepPrefix() + sDate;
			}
			Log.v("next num val", "NEXT :" + preFix + "/" + dFormat.format(Integer.valueOf(nextNumVal)));

		} else {
			for (Reference reference : list) {
				preFix = reference.getCharVal() + reference.getRepPrefix() + sDate;
			}

			Log.v("next num val", "NEXT :" + preFix + "/" + dFormat.format(Integer.valueOf(nextNumVal)));
			Log.v("next num val", "NEXT :" + preFix);
		}



		return preFix + "/" + dFormat.format(Integer.valueOf(nextNumVal));

	}


	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


	public void findUsageOfrefNo(String newRefNo) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String lastVal = "";

		Calendar c = Calendar.getInstance();

		try {
			String query = "select * from TblOrder where RefNo = '"+newRefNo+"'";
			Cursor cursor = dB.rawQuery(query, null);
			int count = cursor.getCount();
			if (count > 0) {
				new ReferenceNum(context).NumValueUpdate(context.getResources().getString(R.string.NumVal));

			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}

	}
	public int IsExistRefNoOrder(String newRefNo) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		int count = 0;

		Calendar c = Calendar.getInstance();

		try {
			String query = "select * from TblOrder where RefNo = '"+newRefNo+"' and isActive = '0'";
			Cursor cursor = dB.rawQuery(query, null);
			 count = cursor.getCount();
//			if (count > 0) {
//				new ReferenceNum(context).NumValueUpdate(context.getResources().getString(R.string.NumVal));
//
//			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}
		return count;
	}
	public int IsExistRefNoReceipt(String newRefNo) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		int count = 0;

		Calendar c = Calendar.getInstance();

		try {
			String query = "select * from TblRecHedS where RefNo = '"+newRefNo+"' and isActive = '0'";
			Cursor cursor = dB.rawQuery(query, null);
			count = cursor.getCount();
//			if (count > 0) {
//				new ReferenceNum(context).NumValueUpdate(context.getResources().getString(R.string.NumVal));
//
//			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}
		return count;
	}
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public int IsActiveReceipt(String RefNo) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		int count = 0;

		Calendar c = Calendar.getInstance();

		try {
			String query = "select * from TblRecHedS where RefNo = '"+RefNo+"' and isActive = '1'";
			Cursor cursor = dB.rawQuery(query, null);
			count = cursor.getCount();
//			if (count > 0) {
//				new ReferenceNum(context).NumValueUpdate(context.getResources().getString(R.string.NumVal));
//
//			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}
		return count;
	}
	public String getNextNumVal(String cSettingsCode,String repcode) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String nextNumVal = "";

		Calendar c = Calendar.getInstance();

		try {
			String query = "SELECT " + ValueHolder.NNUM_VAL +" from "+ ValueHolder.TABLE_FCOMPANYBRANCH + " WHERE " + ValueHolder.BRANCH_CODE + " = '" + repcode + "'  AND cSettingsCode = '" + cSettingsCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";
			Cursor cursor = dB.rawQuery(query, null);
			int count = cursor.getCount();
			if (count > 0) {
				while (cursor.moveToNext()) {
					nextNumVal = cursor.getString(cursor.getColumnIndex(ValueHolder.NNUM_VAL));
				}
			} else {
				nextNumVal = "1";
			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}

		return nextNumVal;
	}
	public String getNextNumValAvoidDuplicateOrder(String txnDate,String cSettingsCode,String repcode) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String nextNumVal = "";
		int nextnum = 0;

		Calendar c = Calendar.getInstance();

		try {
			//String query = "SELECT " + ValueHolder.NNUM_VAL +" from "+ ValueHolder.TABLE_FCOMPANYBRANCH + " WHERE " + ValueHolder.BRANCH_CODE + " = '" + repcode + "'  AND cSettingsCode = '" + cSettingsCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";
			String query = "select  SUBSTR(Max(RefNo),9,4) as currentNum from TblOrder where TxnDate = '" + txnDate + "'";
			Cursor cursor = dB.rawQuery(query, null);
			int count = cursor.getCount();
			if (count > 0) {
				while (cursor.moveToNext()) {
					nextnum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("currentNum")));
					nextNumVal = ""+nextnum;
				}
			} else {
				String queryNext = "SELECT " + ValueHolder.NNUM_VAL +" from "+ ValueHolder.TABLE_FCOMPANYBRANCH + " WHERE " + ValueHolder.BRANCH_CODE + " = '" + repcode + "'  AND cSettingsCode = '" + cSettingsCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";
				Cursor cursorNext  = dB.rawQuery(queryNext , null);
				int countNext  = cursorNext.getCount();
				if (countNext  > 0) {
					while (cursorNext.moveToNext()) {
						nextNumVal = cursorNext.getString(cursor.getColumnIndex(ValueHolder.NUM_VAL));
					}
				}else {
					nextNumVal = "1";
				}
			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}

		return nextNumVal;
	}
	public String getNextNumValAvoidDuplicateReceipt(String txnDate,String cSettingsCode,String repcode) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String nextNumVal = "";

		Calendar c = Calendar.getInstance();

		try {
			//String query = "SELECT " + ValueHolder.NNUM_VAL +" from "+ ValueHolder.TABLE_FCOMPANYBRANCH + " WHERE " + ValueHolder.BRANCH_CODE + " = '" + repcode + "'  AND cSettingsCode = '" + cSettingsCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";
			String query = "select  SUBSTR(Max(RefNo),9,4) as currentNum from TblRecHedS where TxnDate = '" + txnDate + "'";
			Cursor cursor = dB.rawQuery(query, null);
			int count = cursor.getCount();
			if (count > 0) {
				while (cursor.moveToNext()) {
					nextNumVal = cursor.getString(cursor.getColumnIndex("currentNum"));
				}
			} else {
				String queryNext = "SELECT " + ValueHolder.NNUM_VAL +" from "+ ValueHolder.TABLE_FCOMPANYBRANCH + " WHERE " + ValueHolder.BRANCH_CODE + " = '" + repcode + "'  AND cSettingsCode = '" + cSettingsCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";
				Cursor cursorNext  = dB.rawQuery(queryNext , null);
				int countNext  = cursorNext.getCount();
				if (countNext  > 0) {
					while (cursorNext.moveToNext()) {
						nextNumVal = cursorNext.getString(cursor.getColumnIndex(ValueHolder.NUM_VAL));
					}
				}else {
					nextNumVal = "1";
				}
			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}

		return nextNumVal;
	}
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public ArrayList<Reference> getCurrentPreFix(String cSettingsCode, String repPrefix) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reference> list = new ArrayList<Reference>();

		try {
			String selectRep = "select cCharVal from TblCompanySetting where cSettingsCode ='" + cSettingsCode + "'";

			Cursor cursor = null;
			cursor = dB.rawQuery(selectRep, null);

			while (cursor.moveToNext()) {

				Reference reference = new Reference();

				reference.setCharVal(cursor.getString(cursor.getColumnIndex(ValueHolder.CHAR_VAL)));
				reference.setRepPrefix(repPrefix);
				list.add(reference);

			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}
		return list;
	}

	public String getCurrentNextNumVal(String cSettingsCode) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		Calendar c = Calendar.getInstance();

		String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FCOMPANYBRANCH + " WHERE " + ValueHolder.BRANCH_CODE + "='" + new SalRepController(context).getCurrentRepCode() + "' AND " + ValueHolder.CSETTINGS_CODE + "='" + cSettingsCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";

		Cursor cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {

			return cursor.getString(cursor.getColumnIndex(ValueHolder.NNUM_VAL));

		}

		return "0";
	}


	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public int getCount(String cSettingsCode,String repcode) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		int count = 0;

		try {
			count = 0;

			String query = "SELECT " + ValueHolder.NNUM_VAL + " FROM " + ValueHolder.TABLE_FCOMPANYBRANCH + " WHERE " + ValueHolder.BRANCH_CODE + " ='" + repcode + "' AND " + ValueHolder.CSETTINGS_CODE + "='" + cSettingsCode + "'";
			Cursor cursor = dB.rawQuery(query, null);
			count = cursor.getCount();

		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}

		return count;

	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public int InsetOrUpdate(String code, int nextNumVal) {
		int count = 0;
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {

			Calendar c = Calendar.getInstance();

		//	SalRepDS repDS = new SalRepDS(context);

			ContentValues values = new ContentValues();

			values.put(ValueHolder.NNUM_VAL, String.valueOf(nextNumVal));

			//String query = "SELECT " + dbHelper.REFERENCE_NNUM_VAL + " FROM " + dbHelper.TABLE_REFERENCE + " WHERE " + dbHelper.REFERENCE_REPCODE + "='" + repDS.getCurrentRepCode() + "' AND " + dbHelper.REFERENCE_SETTINGS_CODE + "='" + code + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";
			String query = "SELECT " + ValueHolder.NNUM_VAL + " FROM " + ValueHolder.TABLE_FCOMPANYBRANCH + " WHERE "  + ValueHolder.CSETTINGS_CODE + "='" + code + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";
			Cursor cursor = dB.rawQuery(query, null);

			if (cursor.getCount() > 0) {
				count = (int) dB.update(ValueHolder.TABLE_FCOMPANYBRANCH, values,  ValueHolder.CSETTINGS_CODE + "='" + code + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'", null);
			} else {
				values.put(ValueHolder.BRANCH_CODE, SharedPref.getInstance(context).getUserId());
				values.put(ValueHolder.CSETTINGS_CODE, code);
				values.put(ValueHolder.FCOMPANYBRANCH_YEAR, String.valueOf(c.get(Calendar.YEAR)));
				values.put(ValueHolder.FCOMPANYBRANCH_MONTH, String.valueOf(c.get(Calendar.MONTH) + 1));

				count = (int) dB.insert(ValueHolder.TABLE_FCOMPANYBRANCH, null, values);
			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}
		return count;

	}

}
