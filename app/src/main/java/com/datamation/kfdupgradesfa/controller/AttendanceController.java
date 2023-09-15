package com.datamation.kfdupgradesfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Attendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * create by kaveesha - 10-12-2021
 */

public class AttendanceController {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "AttendanceController";

    public AttendanceController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public long InsertUpdateTour(Attendance tour, int val) {

        long result = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ATTENDANCE + " WHERE " + ValueHolder.ATTENDANCE_DATE + " = '" + tour.getFTOUR_DATE() + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(ValueHolder.ATTENDANCE_DATE, tour.getFTOUR_DATE());
            values.put(ValueHolder.ATTENDANCE_F_KM, tour.getFTOUR_F_KM());
            values.put(ValueHolder.ATTENDANCE_F_TIME, tour.getFTOUR_F_TIME());
            values.put(ValueHolder.ATTENDANCE_ROUTE, tour.getFTOUR_ROUTE());
            values.put(ValueHolder.ATTENDANCE_S_KM, tour.getFTOUR_S_KM());
            values.put(ValueHolder.ATTENDANCE_S_TIME, tour.getFTOUR_S_TIME());
            values.put(ValueHolder.ATTENDANCE_VEHICLE, tour.getFTOUR_VEHICLE());
            values.put(ValueHolder.ATTENDANCE_DISTANCE, tour.getFTOUR_DISTANCE());
            values.put(ValueHolder.ATTENDANCE_IS_SYNCED, tour.getFTOUR_IS_SYNCED());
            values.put(ValueHolder.REPCODE, tour.getFTOUR_REPCODE());
            values.put(ValueHolder.ATTENDANCE_MAC, tour.getFTOUR_MAC());
            values.put(ValueHolder.ATTENDANCE_DRIVER, tour.getFTOUR_DRIVER());
            values.put(ValueHolder.ATTENDANCE_ASSIST, tour.getFTOUR_ASSIST());
            if (val == 0) {
                values.put(ValueHolder.ATTENDANCE_STLATITUDE, tour.getFTOUR_STLATITIUDE());
                values.put(ValueHolder.ATTENDANCE_STLONGTITUDE, tour.getFTOUR_STLONGTITIUDE());
            } else if (val == 1) {
                values.put(ValueHolder.ATTENDANCE_ENDLATITUDE, tour.getFTOUR_ENDLATITIUDE());
                values.put(ValueHolder.ATTENDANCE_ENDLONGTITUDE, tour.getFTOUR_ENDLONGTITIUDE());
            }

            if (cursor.getCount() > 0) {
                result = dB.update(ValueHolder.TABLE_ATTENDANCE, values, ValueHolder.ATTENDANCE_DATE + " =?", new String[]{String.valueOf(tour.getFTOUR_DATE())});
            } else {
                result = dB.insert(ValueHolder.TABLE_ATTENDANCE, null, values);
            }

            cursor.close();

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return result;
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<Attendance> getIncompleteRecord() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ATTENDANCE + " WHERE " + ValueHolder.ATTENDANCE_F_TIME + " IS NULL AND " + ValueHolder.ATTENDANCE_F_KM + " IS NULL AND " + ValueHolder.ATTENDANCE_DATE + " IS NOT NULL";


        Cursor cursor = dB.rawQuery(selectQuery, null);

        ArrayList<Attendance> tours = new ArrayList<>();
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Attendance tour = new Attendance();
                    tour.setFTOUR_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_DATE)));
                    tour.setFTOUR_F_KM(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_F_KM)));
                    tour.setFTOUR_F_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_F_TIME)));
                    tour.setFTOUR_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                    tour.setFTOUR_ROUTE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_ROUTE)));
                    tour.setFTOUR_S_KM(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_S_KM)));
                    tour.setFTOUR_S_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_S_TIME)));
                    tour.setFTOUR_VEHICLE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_VEHICLE)));
                    tour.setFTOUR_DISTANCE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_DISTANCE)));
                    tour.setFTOUR_DRIVER(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_DRIVER)));
                    tour.setFTOUR_ASSIST(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_ASSIST)));

                    tours.add(tour);
                }

                return tours;
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return tours;
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int hasTodayRecord() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ATTENDANCE + " WHERE " + ValueHolder.ATTENDANCE_F_TIME + " IS NOT NULL AND " + ValueHolder.ATTENDANCE_F_KM + " IS NOT NULL AND " + ValueHolder.ATTENDANCE_DATE + "='" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' AND " + ValueHolder.ATTENDANCE_S_KM + " IS NOT NULL AND " + ValueHolder.ATTENDANCE_S_TIME + " IS NOT NULL";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                return cursor.getCount();

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return 0;

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int updateIsSynced() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();

            values.put(ValueHolder.ATTENDANCE_IS_SYNCED, "1");

            count = dB.update(ValueHolder.TABLE_ATTENDANCE, values, ValueHolder.ATTENDANCE_IS_SYNCED + " =?", new String[]{String.valueOf(0)});

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

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<Attendance> getUnsyncedTourData() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Attendance> list = new ArrayList<Attendance>();
        try {
            String s = "SELECT * FROM " + ValueHolder.TABLE_ATTENDANCE + " WHERE " + ValueHolder.ATTENDANCE_IS_SYNCED + "='0' AND EndTime IS NOT NULL AND EndKm IS NOT NULL";
            Cursor cursor = dB.rawQuery(s, null);
            while (cursor.moveToNext()) {

                Attendance tour = new Attendance();

                tour.setDISTRIBUTE_DB(SharedPref.getInstance(context).getDistDB().trim());
                tour.setFTOUR_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_DATE)));
                tour.setFTOUR_F_KM(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_F_KM)));
                tour.setFTOUR_F_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_F_TIME)));
                tour.setFTOUR_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                tour.setFTOUR_ROUTE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_ROUTE)));
                tour.setFTOUR_S_KM(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_S_KM)));
                tour.setFTOUR_S_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_S_TIME)));
                tour.setFTOUR_VEHICLE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_VEHICLE)));
                tour.setFTOUR_DISTANCE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_DISTANCE)));
                tour.setFTOUR_DRIVER(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_DRIVER)));
                tour.setFTOUR_ASSIST(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_ASSIST)));
                tour.setFTOUR_REPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.REPCODE)));
                tour.setFTOUR_MAC(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_MAC)));
                tour.setFTOUR_STLATITIUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_STLATITUDE)));
                tour.setFTOUR_STLONGTITIUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_STLONGTITUDE)));
                tour.setFTOUR_ENDLATITIUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_ENDLATITUDE)));
                tour.setFTOUR_ENDLONGTITIUDE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_ENDLONGTITUDE)));

                list.add(tour);
            }
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }
        return list;
    }

    public boolean isDayEnd(String Ydate) {
        String[] dates = Ydate.split("-");
        int day = Integer.parseInt(dates[2].toString());
        day = day - 1;
        String oldDate = dates[0] + "-" + dates[1] + "-" + day;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ATTENDANCE + " WHERE EndTime IS NOT NULL AND EndKm IS NOT NULL AND tDate=" + oldDate;
            Cursor cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                return true;

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return false;
    }
    public String yesterdayMeterReading(String Ydate) {
        String[] dates = Ydate.split("-");
        int day = Integer.parseInt(dates[2].toString());
        day = day - 1;
        String oldDate = dates[0] + "-" + dates[1] + "-" + day;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        String selectQuery = "SELECT EndKm FROM TblAttendance LIMIT 1";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_F_KM));


            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return "0";

    }

    public ArrayList<Attendance> getCompleteRecordToCancel(String date) {

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        String Date = curYear + "-" + curMonth + "-" + curDate;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM  TblAttendance WHERE  EndTime IS NOT NULL AND  ENDKm IS NOT NULL AND tDate ='" + date + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        ArrayList<Attendance> tours = new ArrayList<>();
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    Attendance tour = new Attendance();
                    tour.setFTOUR_DATE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_DATE)));
                    tour.setFTOUR_F_KM(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_F_KM)));
                    tour.setFTOUR_F_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_F_TIME)));
                    tour.setFTOUR_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
                    tour.setFTOUR_ROUTE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_ROUTE)));
                    tour.setFTOUR_S_KM(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_S_KM)));
                    tour.setFTOUR_S_TIME(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_S_TIME)));
                    tour.setFTOUR_VEHICLE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_VEHICLE)));
                    tour.setFTOUR_DISTANCE(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_DISTANCE)));
                    tour.setFTOUR_DRIVER(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_DRIVER)));
                    tour.setFTOUR_ASSIST(cursor.getString(cursor.getColumnIndex(ValueHolder.ATTENDANCE_ASSIST)));

                    tours.add(tour);
                }

                return tours;
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return tours;
    }
}
