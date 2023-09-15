package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Bank;

import java.util.ArrayList;

public class BankController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "BankController ";

    public BankController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateBank(ArrayList<Bank> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_BANK +
                    " (BankCode," +
                    " BankName," +
                    " BankAccno," +
                    " Branch," +
                    " BankAdd1," +
                    " BankAdd2) " + " VALUES (?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (Bank bank : list) {
                stmt.bindString(1, bank.getFBANK_BANK_CODE());
                stmt.bindString(2, bank.getFBANK_BANK_NAME());
                stmt.bindString(3, bank.getFBANK_BANK_ACC_NO());
                stmt.bindString(4, bank.getFBANK_BRANCH());
                stmt.bindString(5, bank.getFBANK_ADD1());
                stmt.bindString(6, bank.getFBANK_ADD2());

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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_BANK, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_BANK, null, null);
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
    /*-----------------------------------------------------------------------*/
    public ArrayList<Bank> getBanks() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Bank> list = new ArrayList<Bank>();

        String selectQuery = "select * from " + ValueHolder.TABLE_BANK;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                Bank bank = new Bank();

                bank.setFBANK_BANK_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.BANK_CODE)));
                bank.setFBANK_BANK_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.BANK_CODE))+" - "+cursor.getString(cursor.getColumnIndex(ValueHolder.BANK_NAME))+" - "+cursor.getString(cursor.getColumnIndex(ValueHolder.BRANCH)));


                list.add(bank);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();

        }

        return list;
    }

    public Bank getBankByName(String  name) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "select * from " + ValueHolder.TABLE_BANK +" where (select bankname || ' - ' || Branch) like '" + name + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        Bank bank = new Bank();
        try {
            while (cursor.moveToNext()) {

                bank.setFBANK_BANK_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.BANK_CODE)));
                bank.setFBANK_BANK_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.BANK_NAME))+" - "+cursor.getString(cursor.getColumnIndex(ValueHolder.BRANCH)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();

        }

        return bank;
    }


}
