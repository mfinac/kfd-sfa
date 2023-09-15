package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Control;

import java.util.ArrayList;

public class CompanyDetailsController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "CompanyDetailsController";


    public CompanyDetailsController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateFControl(ArrayList<Control> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_CONTROL +
                    " (ComName," +
                    " ComAdd1," +
                    " ComAdd2," +
                    " ComAdd3," +
                    " comtel1," +
                    " comtel2," +
                    " comfax1," +
                    " comemail," +
                    " comweb," +
                  //  " comRegNo," +
                    " SysType," +
                    " basecur," +
                    " conage1," +
                    " conage2," +
                    " conage3," +
                    " conage4," +
                    " conage5," +
                    " conage6," +
                    " conage7," +
                    " conage8," +
                    " conage9," +
                    " conage10," +
                    " conage11," +
                    " conage12," +
                    " conage13," +
                    " conage14," +
                    " AppVersion," +
                    " SeqNo) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (Control control : list) {
                stmt.bindString(1, control.getFCONTROL_COM_NAME());
                stmt.bindString(2, control.getFCONTROL_COM_ADD1());
                stmt.bindString(3, control.getFCONTROL_COM_ADD2());
                stmt.bindString(4, control.getFCONTROL_COM_ADD3());
                stmt.bindString(5, control.getFCONTROL_COM_TEL1());
                stmt.bindString(6, control.getFCONTROL_COM_TEL2());
                stmt.bindString(7, control.getFCONTROL_COM_FAX());
                stmt.bindString(8, control.getFCONTROL_COM_EMAIL());
                stmt.bindString(9, control.getFCONTROL_COM_WEB());
//                stmt.bindString(10,control.getFCONTROL_COM_REGNO());
                stmt.bindString(10,control.getFCONTROL_SYSTYPE());
                stmt.bindString(11,control.getFCONTROL_BASECUR());
                stmt.bindString(12,control.getFCONTROL_CONAGE1());
                stmt.bindString(13,control.getFCONTROL_CONAGE2());
                stmt.bindString(14,control.getFCONTROL_CONAGE3());
                stmt.bindString(15,control.getFCONTROL_CONAGE4());
                stmt.bindString(16,control.getFCONTROL_CONAGE5());
                stmt.bindString(17,control.getFCONTROL_CONAGE6());
                stmt.bindString(18,control.getFCONTROL_CONAGE7());
                stmt.bindString(19,control.getFCONTROL_CONAGE8());
                stmt.bindString(20,control.getFCONTROL_CONAGE9());
                stmt.bindString(21,control.getFCONTROL_CONAGE10());
                stmt.bindString(22,control.getFCONTROL_CONAGE11());
                stmt.bindString(23,control.getFCONTROL_CONAGE12());
                stmt.bindString(24,control.getFCONTROL_CONAGE13());
                stmt.bindString(25,control.getFCONTROL_CONAGE14());
                stmt.bindString(26,control.getFCONTROL_APPVERSION());
                stmt.bindString(27,control.getFCONTROL_INTEGSEQNO());

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

	/*-*-*-*-*-**-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<Control> getAllControl() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Control> list = new ArrayList<Control>();

        String selectQuery = "select * from " + ValueHolder.TABLE_CONTROL;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                Control aControl = new Control();

                aControl.setFCONTROL_COM_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.COM_NAME)));
                aControl.setFCONTROL_COM_ADD1(cursor.getString(cursor.getColumnIndex(ValueHolder.COM_ADD1)));
                aControl.setFCONTROL_COM_ADD2(cursor.getString(cursor.getColumnIndex(ValueHolder.COM_ADD2)));
                aControl.setFCONTROL_COM_ADD3(cursor.getString(cursor.getColumnIndex(ValueHolder.COM_ADD3)));
                aControl.setFCONTROL_COM_TEL1(cursor.getString(cursor.getColumnIndex(ValueHolder.COM_TEL1)));
                aControl.setFCONTROL_COM_TEL2(cursor.getString(cursor.getColumnIndex(ValueHolder.COM_TEL2)));
                aControl.setFCONTROL_COM_FAX(cursor.getString(cursor.getColumnIndex(ValueHolder.COM_FAX)));
                aControl.setFCONTROL_COM_EMAIL(cursor.getString(cursor.getColumnIndex(ValueHolder.COM_EMAIL)));
                aControl.setFCONTROL_COM_WEB(cursor.getString(cursor.getColumnIndex(ValueHolder.COM_WEB)));
                aControl.setFCONTROL_DEALCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.DEALCODE)));


                list.add(aControl);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();

        }

        return list;
    }


    public void deleteAll() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        dB.delete(ValueHolder.TABLE_CONTROL, null, null);
        dB.close();
    }
}
