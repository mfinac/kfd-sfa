package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.CompanyBranch;
import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kaveesha - 23/11/2021
 */

public class ReferenceDetailDownloader {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG="ReferenceDetailDownloader";


    public ReferenceDetailDownloader(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateFCompanyBranch(ArrayList<CompanyBranch> list) {

        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            for (CompanyBranch branch : list) {

                Cursor cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_FCOMPANYBRANCH + " WHERE " + ValueHolder.CSETTINGS_CODE + "='" + branch.getFCOMPANYBRANCH_CSETTINGS_CODE() + "' AND " + ValueHolder.BRANCH_CODE + "='" + branch.getFCOMPANYBRANCH_BRANCH_CODE() + "' AND nYear='" + branch.getNYEAR() + "' AND nMonth='" + branch.getNMONTH() + "'", null);

                ContentValues values = new ContentValues();

                values.put(ValueHolder.BRANCH_CODE, branch.getFCOMPANYBRANCH_BRANCH_CODE());
                values.put(ValueHolder.CSETTINGS_CODE, branch.getFCOMPANYBRANCH_CSETTINGS_CODE());
                values.put(ValueHolder.NNUM_VAL, branch.getFCOMPANYBRANCH_NNUM_VAL());
                values.put(ValueHolder.FCOMPANYBRANCH_YEAR, branch.getNYEAR());
                values.put(ValueHolder.FCOMPANYBRANCH_MONTH, branch.getNMONTH());

                if (cursor.getCount() > 0) {
                    dB.update(ValueHolder.TABLE_FCOMPANYBRANCH, values, ValueHolder.CSETTINGS_CODE + "=? AND " + ValueHolder.BRANCH_CODE + "=? AND " + ValueHolder.FCOMPANYBRANCH_YEAR + "=? AND " + ValueHolder.FCOMPANYBRANCH_MONTH + "=?", new String[]{branch.getFCOMPANYBRANCH_CSETTINGS_CODE().toString(), branch.getFCOMPANYBRANCH_BRANCH_CODE().toString(), branch.getNYEAR().toString(), branch.getNMONTH().toString()});
                    Log.v(TAG, "Updated");
                } else {
                    count = (int) dB.insert(ValueHolder.TABLE_FCOMPANYBRANCH, null, values);
                    Log.v(TAG, "Inserted" + count);
                }
                cursor.close();
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }
        return count;

    }

    @SuppressWarnings("static-access")
    public String getCurrentNextNumVal(String cSettingsCode ){

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Calendar c = Calendar.getInstance();

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FCOMPANYBRANCH +" WHERE "+ValueHolder.CSETTINGS_CODE +" ='"+cSettingsCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while(cursor.moveToNext()){

            return cursor.getString(cursor.getColumnIndex(ValueHolder.NNUM_VAL));

        }

        return "0";
    }

    @SuppressWarnings("static-access")
    public int deleteAll(){

        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }
        Cursor cursor = null;
        try{

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_FCOMPANYBRANCH, null);
            count =cursor.getCount();
            if(count>0){
                int success = dB.delete(ValueHolder.TABLE_FCOMPANYBRANCH, null, null);
                Log.v("Success", success+"");
            }
        }catch (Exception e){

            Log.v(TAG+" Exception", e.toString());

        }finally{
            if (cursor !=null) {
                cursor.close();
            }
            dB.close();
        }

        return count;

    }




}
