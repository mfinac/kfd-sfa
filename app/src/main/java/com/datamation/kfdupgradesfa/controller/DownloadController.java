package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.model.Control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DownloadController {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "DownloadController";

    // Shared Preferences variables
    public static final String SETTINGS = "SETTINGS";


    public DownloadController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }


    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    //table
    public static final String TABLE_DOWNLOAD = "Table_Download";
    //table attribute
    public static final String DOWNLOAD_COUNT = "download_due";
    public static final String DOWNLOADED_COUNT = "download_done";
    public static final String DOWNLOAD_TITLE = "download_title";
    public static final String DOWNLOAD_ID = "download_id";

    //create String
    public static final String CREATE_DOWNLOAD_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DOWNLOAD + " ("+ DOWNLOAD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DOWNLOAD_COUNT + " TEXT, " + DOWNLOADED_COUNT + " TEXT, " + DOWNLOAD_TITLE + " TEXT); ";



    //----------------MMS -----17/02/2022-----------To get download List------------
    public ArrayList<Control> getAllDownload() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Control> list = new ArrayList<Control>();

        String selectQuery = "select * from " + TABLE_DOWNLOAD;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                Control aControl = new Control();

                aControl.setFCONTROL_DOWNLOAD_TITLE(cursor.getString(cursor.getColumnIndex(DOWNLOAD_TITLE)));
                aControl.setFCONTROL_DOWNLOADCOUNT(cursor.getString(cursor.getColumnIndex(DOWNLOAD_COUNT)));
                aControl.setFCONTROL_DOWNLOADEDCOUNT(cursor.getString(cursor.getColumnIndex(DOWNLOADED_COUNT)));

                list.add(aControl);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();

        }

        return list;
    }


    //----------------MMS -----04/08/2022-----------------------
    public ArrayList<Control> getAllDownloadByCategoryName(String category) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Control> list = new ArrayList<Control>();

        String selectQuery = "select * from " + TABLE_DOWNLOAD +" WHERE "+ DOWNLOAD_TITLE +" IN ("+category+")";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                Control aControl = new Control();

                aControl.setFCONTROL_DOWNLOAD_TITLE(cursor.getString(cursor.getColumnIndex(DOWNLOAD_TITLE)));
                aControl.setFCONTROL_DOWNLOADCOUNT(cursor.getString(cursor.getColumnIndex(DOWNLOAD_COUNT)));
                aControl.setFCONTROL_DOWNLOADEDCOUNT(cursor.getString(cursor.getColumnIndex(DOWNLOADED_COUNT)));

                list.add(aControl);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();

        }

        return list;
    }

    //----------------MMS -----17/02/2022-----------Create or Update Download----------------------
    public int createOrUpdateDownload(String downloaded, String download, String title) {

        int serverdbID = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            cursor = dB.rawQuery("SELECT * FROM " + TABLE_DOWNLOAD, null);

            ContentValues values = new ContentValues();
            values.put(DOWNLOAD_COUNT, download);
            values.put(DOWNLOADED_COUNT,downloaded);
            values.put(DOWNLOAD_TITLE, title);

            serverdbID = (int) dB.insert(TABLE_DOWNLOAD, null, values);
            Log.v(TAG, " Inserted " + serverdbID);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return serverdbID;

    }

    public void deleteAll() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        dB.delete(TABLE_DOWNLOAD, null, null);
        dB.close();
    }

}
