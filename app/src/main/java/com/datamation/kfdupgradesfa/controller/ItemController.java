package com.datamation.kfdupgradesfa.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.datamation.kfdupgradesfa.helpers.DatabaseHelper;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.Group;
import com.datamation.kfdupgradesfa.model.Item;
import com.datamation.kfdupgradesfa.model.ItemFreeIssue;
import com.datamation.kfdupgradesfa.model.PreProduct;
import com.datamation.kfdupgradesfa.model.Product;
import com.datamation.kfdupgradesfa.model.StockInfo;

import java.util.ArrayList;


public class ItemController {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "ItemController";
    SharedPref mSharedPref;
    public int qty;

    public static SharedPreferences localSP;

    public ItemController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        mSharedPref = new SharedPref(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void InsertOrReplaceItems(ArrayList<Item> list) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            //String sql = "INSERT OR REPLACE INTO " +  ValueHolder.TABLE_ITEM + " (AvgPrice,BrandCode,GroupCode,ItemCode,ItemName,ItemStatus,PrilCode,VenPcode,NouCase,ReOrderLvl,ReOrderQty,UnitCode,TypeCode,TaxComCode,Pack) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String sql = "INSERT OR REPLACE INTO " + ValueHolder.TABLE_ITEM + " (AddUser,AvgPrice,BrandCode,GroupCode,ItemCode,ItemName,ItemStatus,PrilCode,VenPcode,NouCase,ReOrderLvl,ReOrderQty,UnitCode,TypeCode,TaxComCode,Pack,SupCode,GenericName) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (Item items : list) {

                stmt.bindString(1, items.getFITEM_ADDUSER());
                stmt.bindString(2, items.getFITEM_AVGPRICE());
                stmt.bindString(3, items.getFITEM_BRANDCODE());
                stmt.bindString(4, items.getFITEM_GROUPCODE());
                stmt.bindString(5, items.getFITEM_ITEM_CODE());
                stmt.bindString(6, items.getFITEM_ITEM_NAME());
                stmt.bindString(7, items.getFITEM_ITEMSTATUS());
                stmt.bindString(8, items.getFITEM_PRILCODE());
                stmt.bindString(9, items.getFITEM_VENPCODE());
                stmt.bindString(10, items.getFITEM_NOUCASE());
                stmt.bindString(11, items.getFITEM_REORDER_LVL());
                stmt.bindString(12, items.getFITEM_REORDER_QTY());
                stmt.bindString(13, items.getFITEM_UNITCODE());
                stmt.bindString(14, items.getFITEM_TYPECODE());
                stmt.bindString(15, items.getFITEM_TAXCOMCODE());
                stmt.bindString(16, items.getFITEM_PACK());
                stmt.bindString(17, items.getFITEM_SUPCODE());
                stmt.bindString(18, items.getFITEM_GENERIC_NM());

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

    public ArrayList<Product> getAllItemFor(String itemName, String supCode, String LocCode, String debCode, String costcd) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Product> list = new ArrayList<Product>();
        double unitPrice;
        String freeSchema = "";
        String FreeRef = "";
       // LocCode = "NEG01";

        try {


            String selectQuery ="    SELECT i.ItemCode, i.ItemName, i.NOUCase, i.Pack, i.SupCode, l.qoh, p.price "
                                 +"   FROM Tblitem i inner JOIN TblItemLoc l on l.ItemCode=i.ItemCode "
                                 + "  left join TblItemPri p on i.ItemCode=p.ItemCode "
                                 + "  where l.LocCode='" + LocCode + "'  and p.Costcode='" + costcd + "' and i.SupCode like '%" + supCode + "%' and i.itemname like '%" + itemName + "%'"
                                 +" order by l.qoh desc";


            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Product product = new Product();

                product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                product.setFPRODUCT_NOUCASE(cursor.getString(cursor.getColumnIndex(ValueHolder.NOU_CASE)));
                product.setFPRODUCT_PACK(cursor.getString(cursor.getColumnIndex(ValueHolder.PACK)));
                product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                product.setSupCode(cursor.getString(cursor.getColumnIndex(ValueHolder.SUP_CODE)));
                product.setFPRODUCT_QTY("0");
                product.setFPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex("Price")));
                freeSchema = "";
                product.setFPRODUCT_DEBCODE(debCode);
                product.setFPRODUCT_FREESCREMA(freeSchema);
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

    public ArrayList<Product> getAllItemsBySupplier(String searchStr,String refNo, String locCode, String debCode, String costcd) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Product> list = new ArrayList<Product>();
        double unitPrice;
        String freeSchema = "";
        String FreeRef = "";

        try {


            String selectQuery = "SELECT  itm.ItemCode AS ItemCode , itm.ItemName AS ItemName ,itm.NOUCase, itm.Pack AS Pack,itm.SupCode," +
            " ifnull( (select price from TblItemPri where ItemCode = itm.ItemCode and CostCode = '" + costcd + "'),'0.00')as itpri , " +

                    "  loc.QOH AS QOH , IFNULL(orddet.qty,0.0) AS Qty " +
                    "  FROM TblItem itm " +
                    "  INNER JOIN TblItemLoc loc ON loc.ItemCode = itm.ItemCode " +
                    "  LEFT JOIN TblItemPri pri ON pri.ItemCode = itm.ItemCode  " +
                    "  LEFT JOIN TblProductsPre orddet ON orddet.ItemCode = itm.ItemCode  " +
                    "  WHERE itm.SupCode LIKE '" + searchStr + "%' AND loc.LocCode = '"+locCode+"' AND pri.ItemCode = itm.ItemCode "+
//                    " ORDER BY itm.itemname ", null);
//                    " Group by  itm.ItemCode ORDER BY itm.itemname ";
                    " Group By itm.ItemCode,itm.ItemName,itm.NOUCase,itm.PackSize order by CAST(loc.QOH AS integer) DESC";

            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Product product = new Product();

                product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                product.setFPRODUCT_NOUCASE(cursor.getString(cursor.getColumnIndex(ValueHolder.NOU_CASE)));
                product.setFPRODUCT_PACK(cursor.getString(cursor.getColumnIndex(ValueHolder.PACK)));
                product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                product.setSupCode(cursor.getString(cursor.getColumnIndex(ValueHolder.SUP_CODE)));
                product.setFPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(ValueHolder.QTY)));
                //unitPrice = Double.parseDouble(new ItemPriDS(context).getProductPriceByCode(product.getFPRODUCT_ITEMCODE(),mSharedPref.getGlobalVal("PrekeyCost"))) / Double.parseDouble(product.getFPRODUCT_NOUCASE());
                //product.setFPRODUCT_PRICE(String.format("%.2f", unitPrice));
                product.setFPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex("itpri")));
                //FreeRef = cursor.getString(cursor.getColumnIndex("freeref"));

                //freeSchema = new FreeMslabDS(context).getFreeDetailsnew(FreeRef,debCode);
                freeSchema = "";
                product.setFPRODUCT_DEBCODE(debCode);
                product.setFPRODUCT_FREESCREMA(freeSchema);
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


    public ArrayList<Item> getAllItemsBySupCode(String supCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Item> list = new ArrayList<Item>();

        try {

            String selectQuery = "SELECT * FROM TblItem WHERE SupCode LIKE '" + supCode + "%' ";


            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Item item = new Item();

                item.setFITEM_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                item.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));

                list.add(item);
            }


        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }


    public ArrayList<Product> searchAllItems(String supCode,String searchStr, String LocCode, String debCode, String costcd) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Product> list = new ArrayList<Product>();
        String freeSchema = "";
        String selectQuery = "";
        try {

            if(supCode.equals(null)){
                selectQuery = "SELECT itm.ItemCode,itm.ItemName,itm.NOUCase,itm.Pack , Sum(CAST(loc.QOH AS integer)) as QOH,itm.SupCode ,"
                        + " ifnull( (select price from TblItemPri where ItemCode = itm.ItemCode and CostCode = '" + costcd + "'),'0.00') as itpri"
                        + " FROM TblItem itm, TblItemLoc loc WHERE itm.SupCode LIKE '"+supCode+"' AND itm.ItemCode || itm.ItemName LIKE '%" + searchStr + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='"
                        + LocCode + "' " +
//             "AND itm.ItemCode not in (SELECT DISTINCT ItemCode FROM TblOrddet WHERE " + type
//                    + " And RefNo ='" + refno
//                    + "') " +
                        "Group By itm.ItemCode,itm.ItemName,itm.NOUCase,itm.PackSize order by CAST(loc.QOH AS integer) DESC";
            } else {
                selectQuery = "SELECT itm.ItemCode,itm.ItemName,itm.NOUCase,itm.Pack , Sum(CAST(loc.QOH AS integer)) as QOH,itm.SupCode ,"
                        + " ifnull( (select price from TblItemPri where ItemCode = itm.ItemCode and CostCode = '" + costcd + "'),'0.00') as itpri"
                        + " FROM TblItem itm, TblItemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + searchStr + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='"
                        + LocCode + "' " +  "Group By itm.ItemCode,itm.ItemName,itm.NOUCase,itm.PackSize order by CAST(loc.QOH AS integer) DESC";
            }



            //Log.v(TAG + " ITEMCNT ", Integer.toString(cursor.getCount()));

            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Product product = new Product();

                product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                product.setFPRODUCT_NOUCASE(cursor.getString(cursor.getColumnIndex(ValueHolder.NOU_CASE)));
                product.setFPRODUCT_PACK(cursor.getString(cursor.getColumnIndex(ValueHolder.PACK)));
                product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                product.setSupCode(cursor.getString(cursor.getColumnIndex(ValueHolder.SUP_CODE)));
                product.setFPRODUCT_QTY("0");
                //unitPrice = Double.parseDouble(new ItemPriDS(context).getProductPriceByCode(product.getFPRODUCT_ITEMCODE(),mSharedPref.getGlobalVal("PrekeyCost"))) / Double.parseDouble(product.getFPRODUCT_NOUCASE());
                //product.setFPRODUCT_PRICE(String.format("%.2f", unitPrice));
                product.setFPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex("itpri")));
                //FreeRef = cursor.getString(cursor.getColumnIndex("freeref"));

                //freeSchema = new FreeMslabDS(context).getFreeDetailsnew(FreeRef,debCode);
                freeSchema = "";
                product.setFPRODUCT_DEBCODE(debCode);
                product.setFPRODUCT_FREESCREMA(freeSchema);
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


    public ArrayList<Product> searchAllItemsWithoutSupCode(String searchStr, String LocCode, String debCode, String costcd) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Product> list = new ArrayList<Product>();
        double unitPrice;
        String freeSchema = "";
        String FreeRef = "";

        try {

            String selectQuery = "SELECT itm.ItemCode,itm.ItemName,itm.NOUCase,itm.Pack , Sum(CAST(loc.QOH AS integer)) as QOH,itm.SupCode ,"
                    + " ifnull( (select price from TblItemPri where ItemCode = itm.ItemCode and CostCode = '" + costcd + "'),'0.00') as itpri"
                    + " FROM TblItem itm, TblItemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + searchStr + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='"
                    + LocCode + "' " +
                    "Group By itm.ItemCode,itm.ItemName,itm.NOUCase,itm.PackSize order by CAST(loc.QOH AS integer) DESC";


            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Product product = new Product();

                product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                product.setFPRODUCT_NOUCASE(cursor.getString(cursor.getColumnIndex(ValueHolder.NOU_CASE)));
                product.setFPRODUCT_PACK(cursor.getString(cursor.getColumnIndex(ValueHolder.PACK)));
                product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                product.setSupCode(cursor.getString(cursor.getColumnIndex(ValueHolder.SUP_CODE)));
                product.setFPRODUCT_QTY("0");

                product.setFPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex("itpri")));

                freeSchema = "";
                product.setFPRODUCT_DEBCODE(debCode);
                product.setFPRODUCT_FREESCREMA(freeSchema);
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

    public ArrayList<Product> getAllItems(String LocCode, String prillcode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Product> list = new ArrayList<>();
        String selectQuery;
        selectQuery = "SELECT itm.* , loc.QOH FROM TblItem itm, TblItemLoc loc WHERE  loc.itemcode=itm.itemcode AND  loc.LocCode='" + LocCode + "' order by CAST(loc.QOH AS Integer) DESC";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                Product product = new Product();
                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));

                /* Get rid of 0 QOH items */
                if (qoh > 0) {
                    product.setFPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                    product.setFPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                    // product.setFPRODUCT_PRICE(new ItemPriceController(context).getProductPriceByCode(product.getFPRODUCT_ITEMCODE(), prillcode));
                    product.setFPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                    product.setFPRODUCT_QTY("0");
                    list.add(product);
                }
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

    public int deleteAll() {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + ValueHolder.TABLE_ITEM, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(ValueHolder.TABLE_ITEM, null, null);
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

    /*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    public String getTaxComCodeByItemCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT TaxComCode FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + itemCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return "";
    }

    public String getUnits(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT NOUCase FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + itemCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(ValueHolder.NOU_CASE));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return "";
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<Item> getAllItem(String newText, String type, String refno, String LocCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Item> list = new ArrayList<Item>();
        String selectQuery;
        selectQuery = "SELECT itm.*, loc.QOH FROM item itm, itemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND loc.LocCode='" + LocCode + "' AND  itm.ItemCode not in (SELECT DISTINCT ItemCode FROM FTranSODet WHERE " + type + " And RefNo ='" + refno + "') ORDER BY CAST(loc.QOH AS FLOAT) DESC";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        try {
            while (cursor.moveToNext()) {

                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                /* Get rid of 0 QOH items */
                // if (qoh > 0) {
                Item items = new Item();
                items.setFITEM_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                items.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                // items.setFITEM_NOUCASE(cursor.getString(cursor.getColumnIndex(NOUCASE)));
                items.setFITEM_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                items.setFITEM_BRANDCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.BRAND_CODE)));
                items.setFITEM_AVGPRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.AVG_PRICE)));

                list.add(items);
                //    }
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
    /*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    public ArrayList<Group> getAllItemGroups() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Group> list = new ArrayList<Group>();

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_GROUP;
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {
                Group group = new Group();
                group.setFGROUP_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.GROUP_CODE)));
                group.setFGROUP_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.FGROUP_NAME)));
                list.add(group);
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

    /*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-**-*-*-*/

    public String getGroupByCode(String groupCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT GroupName FROM " + ValueHolder.TABLE_GROUP + " WHERE GroupCode='" + groupCode + "'";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(ValueHolder.FGROUP_NAME));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return null;
    }

    /*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-**-*/
    public ArrayList<String> getFreeIssueItemDetailsByItem(ArrayList<String> codes) {


        ArrayList<String> list = new ArrayList<String>();

        for (String code : codes) {
            if (dB == null) {
                open();
            } else if (!dB.isOpen()) {
                open();
            }
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + code + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);
            try {
                while (cursor.moveToNext()) {

                    String itemDetail = cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)) + " - " + cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME));

                    list.add(itemDetail);
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

        return list;
    }

    public String getItemNameByCode(String code) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + code + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME));

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return "";
    }
    /*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-**-*-*-*/

    public ArrayList<StockInfo> getStocks(String item) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        String selectQuery = "";
        ArrayList<StockInfo> list = new ArrayList<StockInfo>();

        if(item.equals("")){
            selectQuery = "SELECT itm.* , loc.LocCode, loc.QOH FROM TblItem itm, TblItemLoc loc WHERE loc.itemcode=itm.itemcode  order by loc.QOH DESC";

        } else {
            selectQuery = "SELECT itm.* , loc.LocCode, loc.QOH FROM TblItem itm, TblItemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + item + "%' AND  loc.itemcode=itm.itemcode  order by loc.QOH DESC";

        }

        //String selectQuery = "SELECT itm.* , loc.QOH FROM fitem itm, fitemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.itemcode=itm.itemcode AND  loc.LocCode='" + LocCode + "' order by loc.QOH DESC";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {

                StockInfo items = new StockInfo();
                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                if (qoh > 0) {
                    items.setStock_Itemcode(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                    //items.setStock_Itemname(cursor.getString(cursor.getColumnIndex(FITEM_ITEM_NAME)));
                    items.setStock_Itemname(cursor.getString(cursor.getColumnIndex(ValueHolder.LOCCODE)) + " - " + cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                    items.setStock_Qoh(((int) qoh) + "");
                    list.add(items);
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }
        return list;
    }

    /*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-**-*-*-*/

    public String getTotalStockQOH(String LocCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT SUM(loc.QOH) as totqty FROM TblItem itm, TblItemLoc loc WHERE loc.itemcode=itm.itemcode AND  loc.LocCode='" + LocCode + "'";

        try {

            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex("totqty"));
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }

        return null;
    }

    public ArrayList<ItemFreeIssue> getAllFreeItemNameByRefno(String code) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<ItemFreeIssue> list = new ArrayList<ItemFreeIssue>();

        String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + " in (select itemcode from TblFreeItem where refno ='" + code + "')";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            ItemFreeIssue issue = new ItemFreeIssue();
            Item items = new Item();

            items.setFITEM_ID(cursor.getString(cursor.getColumnIndex(ValueHolder.ID)));
            items.setFITEM_AVGPRICE(cursor.getString(cursor.getColumnIndex(ValueHolder.AVG_PRICE)));
            items.setFITEM_BRANDCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.BRAND_CODE)));
            items.setFITEM_GROUPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.GROUP_CODE)));
            items.setFITEM_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
            items.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
            items.setFITEM_ITEMSTATUS(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEM_STATUS)));
            items.setFITEM_PRILCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE)));
            items.setFITEM_TAXCOMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE)));
            items.setFITEM_TYPECODE(cursor.getString(cursor.getColumnIndex(ValueHolder.TYPE_CODE)));
            items.setFITEM_UNITCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.UNIT_CODE)));
            items.setFITEM_VENPCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.VEN_P_CODE)));

            issue.setItems(items);
            issue.setAlloc("0");
            list.add(issue);

        }

        return list;
    }


    public String getBrandCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("SELECT brancode FROM TblItem WHERE itemcode='" + itemCode + "'", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("brandcode"));

        }
        cursor.close();
        dB.close();

        return "";
    }
//--------------------------------------------get Items for Must Sales----------------#dhanushika#------------------------------------

    public ArrayList<Item> getItemsForMustSales() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        String selectQuery = "select * from TblItem where itemStatus='0'";
        Cursor cu = null;
        cu = dB.rawQuery(selectQuery, null);
        ArrayList<Item> itemsArrayList = new ArrayList<Item>();
        try {
            while (cu.moveToNext()) {
                Item items = new Item();
                items.setFITEM_ITEM_CODE(cu.getString(cu.getColumnIndex(ValueHolder.ITEMCODE)));
                items.setFITEM_ITEM_NAME(cu.getString(cu.getColumnIndex(ValueHolder.ITEMNAME)));
                itemsArrayList.add(items);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cu.close();
            dB.close();
        }

        return itemsArrayList;
    }
//---------------------------------------------------get Items for PreSales indra------------------------------------------------

    public ArrayList<PreProduct> getAllItemForPreSales(String newText, String type, String refno, String prillcode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<PreProduct> list = new ArrayList<PreProduct>();
        String selectQuery;
        selectQuery = "SELECT itm.ItemName, itm.NouCase, itm.ItemCode, itm.PrilCode, itm.brandcode, itm.avgprice, loc.QOH FROM TblItem itm, TblItemLoc loc WHERE itm.ItemCode || itm.ItemName LIKE '%" + newText + "%' AND loc.ItemCode=itm.ItemCode AND  itm.ItemCode not in (SELECT DISTINCT ItemCode FROM OrderDetail WHERE type ='" + type + "' And RefNo ='" + refno + "') ORDER BY CAST(loc.QOH AS FLOAT) DESC";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                double qoh = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                /* Get rid of 0 QOH items */
                //  if (qoh > 0) {
                PreProduct preProduct = new PreProduct();
                preProduct.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                preProduct.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));
                String prillCodeFromItems = cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE));
                //rashmi - 2018-08-13 for indra.because debtor has no prillcode, it get from fitems
                if (prillcode.equals("")) prillcode = prillCodeFromItems;
                String price = new ItemPriceController(context).getProductPriceByCode(preProduct.getPREPRODUCT_ITEMCODE());
                if (price.isEmpty() || price.equalsIgnoreCase("")) {
                    preProduct.setPREPRODUCT_PRICE("0.00");
                } else {
                    preProduct.setPREPRODUCT_PRICE(price);
                }

                preProduct.setPREPRODUCT_QOH(cursor.getString(cursor.getColumnIndex(ValueHolder.QOH)));
                preProduct.setPREPRODUCT_QTY("0");
                list.add(preProduct);
                // }
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

    public String getItemTaxComCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + itemCode + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE));

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

    public String getPackSizeByCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String packsize = "";
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + itemCode + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                packsize = cursor.getString(cursor.getColumnIndex(ValueHolder.PACKSIZE));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return packsize;
    }


    public String getPackSizeByItemCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String packsize = "";
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT " + ValueHolder.PACK + " FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + itemCode + "'";

            cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                packsize = cursor.getString(cursor.getColumnIndex(ValueHolder.PACK));

            }
        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return packsize;
    }

    public String getCostPriceItemCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT AvgPrice FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + itemCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(ValueHolder.AVG_PRICE));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return "";
    }
    /*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    public String getPrillCodebyItemCode(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT PrilCode FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + itemCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(ValueHolder.PRILCODE));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return "";
    }

    public String getTaxComCodeByItemCodeBeforeDebTax(String itemCode, String debtorCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT TaxComCode FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + itemCode + "'";
        Cursor cursor = dB.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(ValueHolder.TAXCOMCODE));
            }
            cursor.close();
        } else {
            String query = "SELECT taxCode FROM " + ValueHolder.TABLE_DEBTAX + " WHERE " + ValueHolder.DEBCODE + "='" + debtorCode + "'";
            Cursor cursor1 = dB.rawQuery(query, null);

            while (cursor1.moveToNext()) {
                return cursor1.getString(cursor1.getColumnIndex(ValueHolder.TAXCODE));
            }
            cursor1.close();
        }
        dB.close();

        return "";
    }

//    public ArrayList<Item> getGenericItemName(String itemcode) {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        ArrayList<Item> list = new ArrayList<>();
//        String selectQuery;
//
//        selectQuery = "SELECT AddUser,ItemName FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + itemcode + "'";
//
//        Cursor cursor = dB.rawQuery(selectQuery, null);
//        try {
//            while (cursor.moveToNext()) {
//
//                Item item = new Item();
//
//                item.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex( ValueHolder.ITEMNAME)));
//                item.setFITEM_ADDUSER(cursor.getString(cursor.getColumnIndex( ValueHolder.ADDUSER)));
//
//                list.add(item);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            dB.close();
//        }
//        return list;
//    }

    public String getGenericItemName(String itemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT "+ ValueHolder.GENERIC_NAME +" FROM " + ValueHolder.TABLE_ITEM + " WHERE " + ValueHolder.ITEMCODE + "='" + itemCode + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {

            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(ValueHolder.GENERIC_NAME));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return "";
    }


    public ArrayList<Item> getFilterdItemsBySupCodeandItemCode(String supCode,String itemName) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Item> list = new ArrayList<Item>();


        try {

            String selectQuery = "SELECT * FROM TblItem WHERE SupCode LIKE '" + supCode + "%' AND itemname LIKE '" + itemName + "%' ";


            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Item item = new Item();

                item.setFITEM_ITEM_CODE(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMCODE)));
                item.setFITEM_ITEM_NAME(cursor.getString(cursor.getColumnIndex(ValueHolder.ITEMNAME)));

                list.add(item);
            }


        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }


}
