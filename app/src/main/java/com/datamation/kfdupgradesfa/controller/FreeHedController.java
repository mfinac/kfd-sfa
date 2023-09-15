package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.FreeHed;
import com.datamation.kfdupgradesfa.model.OrderDetail;
import com.datamation.kfdupgradesfa.model.free;

import java.util.ArrayList;

public class FreeHedController {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "FreeHedController";


    public FreeHedController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void createOrUpdateFreeHed(ArrayList<FreeHed> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_FREEHED +
                    " (RefNo," +
                    "TxnDate," +
                    "DiscDesc," +
                    "Priority," +
                    "Vdatef," +
                    "Vdatet," +
                    "Remarks," +
                    "Qty," +
                    "FreeItQty," +
                    "Types," +
                    "Mustflg," +
                    "RecCnt) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);
            for (FreeHed freeHed : list) {

                stmt.bindString(1, freeHed.getFFREEHED_REFNO());
                stmt.bindString(2, freeHed.getFFREEHED_TXNDATE());
                stmt.bindString(3, freeHed.getFFREEHED_DISC_DESC());
                stmt.bindString(4, freeHed.getFFREEHED_PRIORITY());
                stmt.bindString(5, freeHed.getFFREEHED_VDATEF());
                stmt.bindString(6, freeHed.getFFREEHED_VDATET());
                stmt.bindString(7, freeHed.getFFREEHED_REMARKS());
                stmt.bindString(8, freeHed.getFFREEHED_ITEM_QTY());
                stmt.bindString(9, freeHed.getFFREEHED_FREE_IT_QTY());
                stmt.bindString(10, freeHed.getFFREEHED_FTYPE());
                stmt.bindString(11, freeHed.getFFREEHED_MUSTFLG());
                stmt.bindString(12, freeHed.getFFREEHED_REC_CNT());

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

//    @SuppressWarnings("static-access")
//    public int createOrUpdateFreeHed(ArrayList<FreeHed> list) {
//
//        int returnID = 0;
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//        Cursor cursor = null;
//        Cursor cursor_ini = null;
//
//        try {
//            cursor_ini = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_FREEHED, null);
//
//            for (FreeHed freehed : list) {
//
//                ContentValues values = new ContentValues();
//
//                values.put(ValueHolder.REFNO, freehed.getFFREEHED_REFNO());
//                values.put(ValueHolder.TXNDATE, freehed.getFFREEHED_TXNDATE());
//                values.put(ValueHolder.DISC_DESC, freehed.getFFREEHED_DISC_DESC());
//                values.put(ValueHolder.PRIORITY, freehed.getFFREEHED_PRIORITY());
//                values.put(ValueHolder.VDATEF, freehed.getFFREEHED_VDATEF());
//                values.put(ValueHolder.VDATET, freehed.getFFREEHED_VDATET());
//                values.put(ValueHolder.REMARKS, freehed.getFFREEHED_REMARKS());
//                values.put(ValueHolder.QTY, freehed.getFFREEHED_ITEM_QTY());
//                values.put(ValueHolder.FREE_IT_QTY, freehed.getFFREEHED_FREE_IT_QTY());
//                values.put(ValueHolder.TYPE, freehed.getFFREEHED_FTYPE());
//                values.put(ValueHolder.MUSTFLG, freehed.getFFREEHED_MUSTFLG());
//                values.put(ValueHolder.REC_CNT, freehed.getFFREEHED_FTYPE());
//
//
//                if (cursor_ini.getCount() > 0) {
//                    String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_FREEHED + " WHERE " + ValueHolder.REFNO + "='" + freehed.getFFREEHED_REFNO() + "'";
//                    cursor = dB.rawQuery(selectQuery, null);
//
//                    if (cursor.getCount() > 0) {
//                        returnID = (int) dB.update(ValueHolder.TABLE_FREEHED, values, ValueHolder.REFNO + "='" + freehed.getFFREEHED_REFNO() + "'", null);
//                    } else {
//                        returnID = (int) dB.insert(ValueHolder.TABLE_FREEHED, null, values);
//                    }
//
//                } else {
//                    returnID = (int) dB.insert(ValueHolder.TABLE_FREEHED, null, values);
//                }
//
//            }
//        } catch (Exception e) {
//
//            Log.v("Exception", e.toString());
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (cursor_ini != null) {
//                cursor_ini.close();
//            }
//            dB.close();
//        }
//        return returnID;
//
//    }
    public ArrayList<FreeHed> getFreeIssueItemDetailByRefnoPre(String itemCode,String debCodeStr) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeHed> list = new ArrayList<FreeHed>();

        String selectQuery = "select * from TblFreehed where refno in (select refno from TblFreedet where itemcode='" + itemCode + "') and date('now') between vdatef and vdatet  and refno in (select refno from TblFreedeb where DebCode='" + debCodeStr + "')";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                FreeHed freeHed = new FreeHed();

                freeHed.setFFREEHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                freeHed.setFFREEHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                freeHed.setFFREEHED_DISC_DESC(cursor.getString(cursor.getColumnIndex(ValueHolder.DISC_DESC)));
                freeHed.setFFREEHED_PRIORITY(cursor.getString(cursor.getColumnIndex(ValueHolder.PRIORITY)));
                freeHed.setFFREEHED_VDATEF(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATEF)));
                freeHed.setFFREEHED_VDATET(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATET)));
                freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
                freeHed.setFFREEHED_ITEM_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FREE_ITEM_QTY)));
                freeHed.setFFREEHED_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FREE_ISSUE_QTY)));
                freeHed.setFFREEHED_FTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));

                list.add(freeHed);

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return list;
    }

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_FREEHED, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_FREEHED, null, null);
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
//get fredetails by orderlist

    //filter free items from order details
    public ArrayList<FreeHed>  filterFreeSchemesFromOrder(ArrayList<OrderDetail> OrderList) {
        //rashmi for new swadeshi sfa - 2019-09-09
        ArrayList<FreeHed> list = new ArrayList<FreeHed>();
        for (OrderDetail det: OrderList) {
            list = getFreeIssueItemDetailByItem(det.getFORDDET_ITEM_CODE());
           // list = getFreeIssueItemDetailWithQty(det.getFORDERDET_ITEMCODE(),det.getFORDERDET_QTY());//rashmi 2019-09-17
        }

        for (FreeHed order : list) {

            Log.d("Rashmi-filterschemes", order.getFFREEHED_REFNO()+"");

        }
        return list;

    }

    public static <T> ArrayList<T> getSchemeListWithQtySum(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }
//rashmi-2019-09-12
    public FreeHed getFreeIssueItemDetailByItemCode(String itemCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        // String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') AND costcode='" + costCode + "' AND date('now') between vdatef and vdatet";
        // inoshi--Mine**CostCode change//
        FreeHed freeHed = new FreeHed();
        String selectQuery = "select * from TblFreehed where refno in (select refno from TblFreedet where itemcode='" + itemCode + "')  AND date('now') between vdatef and vdatet order by Priority asc";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {


                freeHed.setFFREEHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                freeHed.setFFREEHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                freeHed.setFFREEHED_DISC_DESC(cursor.getString(cursor.getColumnIndex(ValueHolder.DISC_DESC)));
                freeHed.setFFREEHED_PRIORITY(cursor.getString(cursor.getColumnIndex(ValueHolder.PRIORITY)));
                freeHed.setFFREEHED_VDATEF(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATEF)));
                freeHed.setFFREEHED_VDATET(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATET)));
                freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
                freeHed.setFFREEHED_ITEM_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                freeHed.setFFREEHED_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FREE_IT_QTY)));
                freeHed.setFFREEHED_FTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return freeHed;
    }
    public ArrayList<FreeHed> getFreeIssueItemDetailByItem(String itemCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeHed> list = new ArrayList<FreeHed>();

        // String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') AND costcode='" + costCode + "' AND date('now') between vdatef and vdatet";
        // inoshi--Mine**CostCode change//
        String selectQuery = "select * from TblFreehed where refno in (select refno from TblFreedet where itemcode='" + itemCode + "')  AND date('now') between vdatef and vdatet order by Priority asc";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                FreeHed freeHed = new FreeHed();

                freeHed.setFFREEHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                freeHed.setFFREEHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                freeHed.setFFREEHED_DISC_DESC(cursor.getString(cursor.getColumnIndex(ValueHolder.DISC_DESC)));
                freeHed.setFFREEHED_PRIORITY(cursor.getString(cursor.getColumnIndex(ValueHolder.PRIORITY)));
                freeHed.setFFREEHED_VDATEF(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATEF)));
                freeHed.setFFREEHED_VDATET(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATET)));
                freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
                freeHed.setFFREEHED_ITEM_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                freeHed.setFFREEHED_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FREE_IT_QTY)));
                freeHed.setFFREEHED_FTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));

                list.add(freeHed);
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
    //2019-09-17-rashmi
    public ArrayList<FreeHed> getFreeIssueItemDetailByItem(ArrayList<OrderDetail> dets) {


        ArrayList<FreeHed> list = new ArrayList<FreeHed>();

        for(OrderDetail det : dets){
            if (dB == null) {
                open();
            } else if (!dB.isOpen()) {
                open();
            }
            String selectQuery = "select * from TblFreehed where refno in (select refno from TblFreedet where itemcode='" + det.getFORDDET_ITEM_CODE()+ "')  AND date('now') between vdatef and vdatet order by Priority asc";

            Cursor cursor = dB.rawQuery(selectQuery, null);
            try {
                while (cursor.moveToNext()) {

                    FreeHed freeHed = new FreeHed();

                    freeHed.setFFREEHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                    freeHed.setFFREEHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                    freeHed.setFFREEHED_DISC_DESC(cursor.getString(cursor.getColumnIndex(ValueHolder.DISC_DESC)));
                    freeHed.setFFREEHED_PRIORITY(cursor.getString(cursor.getColumnIndex(ValueHolder.PRIORITY)));
                    freeHed.setFFREEHED_VDATEF(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATEF)));
                    freeHed.setFFREEHED_VDATET(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATET)));
                    freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
                    freeHed.setFFREEHED_ITEM_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                    freeHed.setFFREEHED_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FREE_IT_QTY)));
                    freeHed.setFFREEHED_FTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
                    list.add(freeHed);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                dB.close();
            }
        }


        // String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') AND costcode='" + costCode + "' AND date('now') between vdatef and vdatet";
        // inoshi--Mine**CostCode change//

        return list;
    }
    public ArrayList<FreeHed> getFreeIssueItemDetailWithQty(String itemCode, String qty) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeHed> list = new ArrayList<FreeHed>();

        // String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') AND costcode='" + costCode + "' AND date('now') between vdatef and vdatet";
        // inoshi--Mine**CostCode change//
        String selectQuery = "select * from TblFreehed where refno in (select refno from TblFreedet where itemcode='" + itemCode + "')  AND date('now') between vdatef and vdatet order by Priority asc";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                FreeHed freeHed = new FreeHed();

                freeHed.setFFREEHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                freeHed.setFFREEHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                freeHed.setFFREEHED_DISC_DESC(cursor.getString(cursor.getColumnIndex(ValueHolder.DISC_DESC)));
                freeHed.setFFREEHED_PRIORITY(cursor.getString(cursor.getColumnIndex(ValueHolder.PRIORITY)));
                freeHed.setFFREEHED_VDATEF(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATEF)));
                freeHed.setFFREEHED_VDATET(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATET)));
                freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
                freeHed.setFFREEHED_ITEM_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                freeHed.setFFREEHED_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FREE_IT_QTY)));
                freeHed.setFFREEHED_FTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
                freeHed.setITEMQTY(qty);


                list.add(freeHed);
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
    public ArrayList<FreeHed> getFreeIssueItemDetailByRefno(String itemCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeHed> list = new ArrayList<FreeHed>();

       // String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') AND costcode='" + costCode + "' AND date('now') between vdatef and vdatet";
    // inoshi--Mine**CostCode change//
        String selectQuery = "select * from TblFreehed where refno in (select refno from TblFreedet where itemcode='" + itemCode + "')  AND date('now') between vdatef and vdatet order by Priority asc";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
        while (cursor.moveToNext()) {

            FreeHed freeHed = new FreeHed();

            freeHed.setFFREEHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            freeHed.setFFREEHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            freeHed.setFFREEHED_DISC_DESC(cursor.getString(cursor.getColumnIndex(ValueHolder.DISC_DESC)));
            freeHed.setFFREEHED_PRIORITY(cursor.getString(cursor.getColumnIndex(ValueHolder.PRIORITY)));
            freeHed.setFFREEHED_VDATEF(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATEF)));
            freeHed.setFFREEHED_VDATET(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATET)));
            freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            freeHed.setFFREEHED_ITEM_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
            freeHed.setFFREEHED_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FREE_IT_QTY)));
            freeHed.setFFREEHED_FTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));

            list.add(freeHed);
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

    public ArrayList<free> getFreeIssueDetails(String refno, String type) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<free> list = new ArrayList<free>();
        String selectQuery = "";
        Cursor cursor = null;

        // String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') AND costcode='" + costCode + "' AND date('now') between vdatef and vdatet";
        // inoshi--Mine**CostCode change//
        if(type.trim().equals("Flat")) {
            selectQuery = "  select  hed.refno as Scheme , hed.ItemQty as ItemQty , hed.FreeItQty as FreeQty , 'click to see sale items' as SaleItem " +
                    "  , '0' as QtyFrom , '0' as QtyTo, 'click to see free items' as FreeItem " +
                    "  from TblFreehed hed  where " +
                    " hed.refno = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
        }else{
            selectQuery = "select mslab.refno as Scheme , mslab.ItemQty as ItemQty , mslab.FreeItQty as FreeQty ,  mslab.Qtyf as QtyFrom , mslab.Qtyt as QtyTo, 'click to see sale items' as SaleItem" +
                    " , 'click to see free items' as FreeItem from TblFreemslab mslab  where" +
                    "  mslab.refno in (select refno from Ffreehed where refno = '" + refno + "')";
            cursor = dB.rawQuery(selectQuery, null);
        }
        try {
            while (cursor.moveToNext()) {

                free freeHed = new free();

                freeHed.setSaleItem(cursor.getString(cursor.getColumnIndex("SaleItem")));
                freeHed.setIssueItem(cursor.getString(cursor.getColumnIndex("FreeItem")));
                freeHed.setItemQty(cursor.getString(cursor.getColumnIndex("ItemQty")));
                freeHed.setFreeQty(cursor.getString(cursor.getColumnIndex("FreeQty")));
                freeHed.setFromQty(cursor.getString(cursor.getColumnIndex("QtyFrom")));
                freeHed.setToQty(cursor.getString(cursor.getColumnIndex("QtyTo")));

                list.add(freeHed);
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
    public int getRefnoByDebCount(String refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT count(*) FROM " + ValueHolder.TABLE_FREEDEB + " WHERE " + ValueHolder.REFNO + "='" + refno + "'";

        Cursor cursor = null;
        cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {

            return cursor.getInt(0);

        }
        return 0;

    }
    public ArrayList<FreeHed> getFreeIssueHeaders() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeHed> list = new ArrayList<FreeHed>();

        // String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') AND costcode='" + costCode + "' AND date('now') between vdatef and vdatet";
        // inoshi--Mine**CostCode change//
        String selectQuery = "select * from TblFreehed where date('now') between vdatef and vdatet";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                FreeHed freeHed = new FreeHed();

                freeHed.setFFREEHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
                freeHed.setFFREEHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
                freeHed.setFFREEHED_DISC_DESC(cursor.getString(cursor.getColumnIndex(ValueHolder.DISC_DESC)));
                freeHed.setFFREEHED_PRIORITY(cursor.getString(cursor.getColumnIndex(ValueHolder.PRIORITY)));
                freeHed.setFFREEHED_VDATEF(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATEF)));
                freeHed.setFFREEHED_VDATET(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATET)));
                freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
                freeHed.setFFREEHED_ITEM_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                freeHed.setFFREEHED_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FREE_IT_QTY)));
                freeHed.setFFREEHED_FTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));

                list.add(freeHed);
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

    public ArrayList<FreeHed> getSaleItems(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeHed> list = new ArrayList<FreeHed>();

        // String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') AND costcode='" + costCode + "' AND date('now') between vdatef and vdatet";
        // inoshi--Mine**CostCode change//
        String selectQuery = "select  item.itemcode as code ,item.itemname as SaleItem " +
                "                      from fItem item, TblFreedet free  where item.itemcode = free.itemcode " +
                "                    and free.refno "+ " = '" + refno + "'";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                FreeHed freeHed = new FreeHed();
                freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex("code"))+" - "+cursor.getString(cursor.getColumnIndex("SaleItem")));

                list.add(freeHed);
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
    public ArrayList<FreeHed> getFreeItems(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeHed> list = new ArrayList<FreeHed>();

        // String selectQuery = "select * from ffreehed where refno in (select refno from ffreedet where itemcode='" + itemCode + "') AND costcode='" + costCode + "' AND date('now') between vdatef and vdatet";
        // inoshi--Mine**CostCode change//
        String selectQuery = "select  item.itemcode as code ,item.itemname as SaleItem " +
                "                      from fItem item, TblFreeItem fritem  where item.itemcode = fritem.itemcode " +
                "                    and fritem.refno "+ " = '" + refno + "'";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                FreeHed freeHed = new FreeHed();
                freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex("code"))+" - "+cursor.getString(cursor.getColumnIndex("SaleItem")));

                list.add(freeHed);
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
    public FreeHed getFreeIssueItemSchema(String itemCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        FreeHed freeHed = new FreeHed();
        String selectQuery = "select * from TblFreehed where refno in (select refno from TblFreedet where itemcode='" + itemCode + "')  AND date('now') between vdatef and vdatet";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
        while (cursor.moveToNext()) {



            freeHed.setFFREEHED_REFNO(cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO)));
            freeHed.setFFREEHED_TXNDATE(cursor.getString(cursor.getColumnIndex(ValueHolder.TXNDATE)));
            freeHed.setFFREEHED_DISC_DESC(cursor.getString(cursor.getColumnIndex(ValueHolder.DISC_DESC)));
            freeHed.setFFREEHED_PRIORITY(cursor.getString(cursor.getColumnIndex(ValueHolder.PRIORITY)));
            freeHed.setFFREEHED_VDATEF(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATEF)));
            freeHed.setFFREEHED_VDATET(cursor.getString(cursor.getColumnIndex(ValueHolder.VDATET)));
            freeHed.setFFREEHED_REMARKS(cursor.getString(cursor.getColumnIndex(ValueHolder.REMARKS)));
            freeHed.setFFREEHED_ITEM_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
            freeHed.setFFREEHED_FREE_IT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.FREE_IT_QTY)));
            freeHed.setFFREEHED_FTYPE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE)));
        }
        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return freeHed;
    }

    public String getRefoByItemCode(String itemCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<FreeHed> list = new ArrayList<FreeHed>();

        String selectQuery = "select * from TblFreehed where refno in (select refno from TblFreedet where itemcode='" + itemCode + "') and date('now') between vdatef and vdatet";

        String s = null;
        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                FreeHed freeHed = new FreeHed();

                s = cursor.getString(cursor.getColumnIndex(ValueHolder.REFNO));
                list.add(freeHed);

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return s;
    }

}
