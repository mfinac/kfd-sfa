package com.datamation.kfdupgradesfa.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.DayExpDetController;
import com.datamation.kfdupgradesfa.controller.DownloadController;
import com.datamation.kfdupgradesfa.controller.ExpenseController;
import com.datamation.kfdupgradesfa.controller.FItenrDetController;
import com.datamation.kfdupgradesfa.controller.InvDetController;
import com.datamation.kfdupgradesfa.controller.InvHedController;
import com.datamation.kfdupgradesfa.controller.ReceiptDetController;
import com.datamation.kfdupgradesfa.controller.RepGPSLocationController;
import com.datamation.kfdupgradesfa.controller.RepTrgDetController;
import com.datamation.kfdupgradesfa.controller.RepTrgHedController;
import com.datamation.kfdupgradesfa.controller.SalesReturnDetController;
import com.datamation.kfdupgradesfa.model.CompanyBranch;


public class DatabaseHelper extends SQLiteOpenHelper {
    // database information
    public static final String DATABASE_NAME = "kfdsfa_database.db";

    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String CREATE_FDEBTOR_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_DEBTOR +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.DEBCODE + " TEXT, " +
            ValueHolder.FDEBTOR_NAME + " TEXT, " + ValueHolder.FDEBTOR_ADD1 + " TEXT, " + ValueHolder.FDEBTOR_ADD2 + " TEXT, " +
            ValueHolder.FDEBTOR_ADD3 + " TEXT, " + ValueHolder.FDEBTOR_TELE + " TEXT, " + ValueHolder.FDEBTOR_MOB + " TEXT, " +
            ValueHolder.FDEBTOR_EMAIL + " TEXT, " + ValueHolder.FDEBTOR_TOWN_CODE + " TEXT, " + ValueHolder.AREACODE + " TEXT, " +
            ValueHolder.FDEBTOR_DBGR_CODE + " TEXT, " + ValueHolder.STATUS + " TEXT, " + ValueHolder.FDEBTOR_ID + " TEXT, " +
            ValueHolder.FDEBTOR_CHK_CRD_LIMIT + " TEXT, " + ValueHolder.FDEBTOR_CRD_PERIOD + " TEXT, " + ValueHolder.FDEBTOR_CHK_CRD_PRD + " TEXT, " +
            ValueHolder.FDEBTOR_CRD_LIMIT + " TEXT, " + ValueHolder.FDEBTOR_CHK_FREE + " TEXT, " + ValueHolder.FDEBTOR_CHK_MUST_FRE + " TEXT, " +
            ValueHolder.REPCODE + " TEXT, " + ValueHolder.FDEBTOR_RANK_CODE + " TEXT, " + ValueHolder.TAX_REG + " TEXT, " +
            ValueHolder.LATITUDE + " TEXT, " + ValueHolder.LONGITUDE + " TEXT, " +
            ValueHolder.ROUTECODE + " TEXT," + ValueHolder.PRILCODE + " TEXT," + ValueHolder.FDEBTOR_MULTIFLG + " TEXT," + ValueHolder.IS_SYNC + " TEXT ); ";

    public static final String CREATE_FSALREP_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_SALREP +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REPCODE + " TEXT, " +
            ValueHolder.REPNAME + " TEXT, " + ValueHolder.COSTCODE + " TEXT, " + ValueHolder.DEALCODE + " TEXT, " +
            ValueHolder.FSALREP_PASSWORD + " TEXT, " + ValueHolder.FSALREP_EMAIL + " TEXT, " + ValueHolder.FSALREP_MOB + " TEXT, " +
            ValueHolder.FSALREP_IDNO + " TEXT, " + ValueHolder.FSALREP_TELE + " TEXT, " + ValueHolder.FSALREP_PREFIX + " TEXT, " +
            ValueHolder.STATUS + " TEXT," + ValueHolder.FSALREP_MAC + " TEXT); ";

    public static final String IDXFSALREP = "CREATE UNIQUE INDEX IF NOT EXISTS idxsalrep_something ON " + ValueHolder.TABLE_SALREP + " (" + ValueHolder.REPCODE + ")";

    public static final String CREATE_CUSP_RECHED_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_CUSP_RECHED +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.ADDDATE + " TEXT, " +
            ValueHolder.ADDMACH + " TEXT, " + ValueHolder.ADDUSER + " TEXT, " +
            ValueHolder.RECHED_CHQDATE + " TEXT, " + ValueHolder.RECHED_CHQNO + " TEXT, " + ValueHolder.DEBCODE + " TEXT, " +
            ValueHolder.RECHED_PAYTYPE + " TEXT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.REPCODE + " TEXT, " +
            ValueHolder.TOTALAMT + " TEXT," + ValueHolder.TXNDATE + " TEXT); ";

    public static final String CREATE_CUSP_RECDET_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_CUSP_RECDET +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.RECDET_ALOAMT + " TEXT, " +
            ValueHolder.AMT + " TEXT, " + ValueHolder.RECDET_INVNO + " TEXT, " + ValueHolder.RECDET_INVTXNDATE + " TEXT, " +
            ValueHolder.REFNO + " TEXT, " + ValueHolder.REPCODE + " TEXT, " + ValueHolder.TXNDATE + " TEXT); ";


    public static final String CREATE_PMSG_HEDDET_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_PMSG_HEDDET +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.PMSG_HEDDET_MBODY + " TEXT, " +
            ValueHolder.PMSG_HEDDET_MGRP + " TEXT, " + ValueHolder.PMSG_HEDDET_MTYPE + " TEXT, " +
            ValueHolder.REFNO + " TEXT, " + ValueHolder.PMSG_HEDDET_SUBJECT + " TEXT, " + ValueHolder.PMSG_HEDDET_SUPCODE + " TEXT); ";

    public static final String CREATE_FITEM_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_ITEM + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.ADDMACH + " TEXT, " + ValueHolder.ADDUSER + " TEXT, " +
            ValueHolder.AVG_PRICE + " TEXT, " + ValueHolder.BRAND_CODE + " TEXT, " + ValueHolder.GROUP_CODE + " TEXT, " +
            ValueHolder.ITEMCODE + " TEXT, " + ValueHolder.ITEMNAME + " TEXT," + ValueHolder.ITEM_STATUS + " TEXT,  " + ValueHolder.GENERIC_NAME + " TEXT, " +
            ValueHolder.MUST_SALE + " TEXT, " + ValueHolder.NOU_CASE + " TEXT, " + ValueHolder.ORD_SEQ + " TEXT, " +
            ValueHolder.PRILCODE + " TEXT, " + ValueHolder.RE_ORDER_LVL + " TEXT, " + ValueHolder.RE_ORDER_QTY + " TEXT, " +
            ValueHolder.TAXCOMCODE + " TEXT, " + ValueHolder.TYPE_CODE + " TEXT, " + ValueHolder.UNIT_CODE + " TEXT, " +
            ValueHolder.CAT_CODE + " TEXT, " + ValueHolder.PACK + " TEXT, " + ValueHolder.PACK_SIZE + " TEXT, " +
            ValueHolder.SUP_CODE + " TEXT, " + ValueHolder.VEN_P_CODE + " TEXT, " + ValueHolder.MUST_FREE + " TEXT); ";

    public static final String CREATE_FGROUP_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_GROUP + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.GROUP_CODE + " TEXT, " + ValueHolder.FGROUP_NAME + " TEXT); ";

    public static final String CREATE_FORDDISC_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_ORDDISC +
            " (" + ValueHolder.REFNO + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " + ValueHolder.REFNO1 + " TEXT, " +
            ValueHolder.ITEMCODE + " TEXT, " + ValueHolder.DISAMT + " TEXT, " + ValueHolder.DISPER + " TEXT ); ";

    public static final String CREATE_FITEMLOC_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_ITEMLOC +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.ITEMCODE + " TEXT, " +
            ValueHolder.LOCCODE + " TEXT, " + ValueHolder.QOH + " TEXT); ";

    public static final String TESTITEMLOC = "CREATE UNIQUE INDEX IF NOT EXISTS idxitemloc_something ON " + ValueHolder.TABLE_ITEMLOC +
            " (" + ValueHolder.ITEMCODE + "," + ValueHolder.LOCCODE + ")";

    public static final String CREATE_FITEMPRI_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_ITEMPRI +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.ITEMCODE + " TEXT, " +
            ValueHolder.PRICE + " TEXT, " + ValueHolder.PRILCODE + " TEXT, " + ValueHolder.COSTCODE + " TEXT, " +
            ValueHolder.SKUCODE + " TEXT); ";

    public static final String CREATE_FORDFREEISS_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_ORDFREEISS +
            " (" + ValueHolder.REFNO + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " + ValueHolder.REFNO1 + " TEXT, " +
            ValueHolder.ITEMCODE + " TEXT, " + ValueHolder.QTY + " TEXT ); ";

    public static final String CREATE_FTAXDET_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_TAXDET +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.TAXCOMCODE + " TEXT, " +
            ValueHolder.TAXCODE + " TEXT, " + ValueHolder.RATE + " TEXT, " + ValueHolder.TAXVAL + " TEXT, " +
            ValueHolder.TAXTYPE + " TEXT, " + ValueHolder.TAXSEQ + " TEXT ); ";

    public static final String CREATE_FDEBTAX_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_DEBTAX +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.DEBCODE + " TEXT, " +
            ValueHolder.TAXCODE + " TEXT, " + ValueHolder.TAXREGNO + " TEXT); ";

    public static final String CREATE_FORDDET_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_ORDDET +
            " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.AMT + " TEXT, " +
            ValueHolder.BAL_QTY + " TEXT, " + ValueHolder.B_AMT + " TEXT, " + ValueHolder.B_DIS_AMT + " TEXT, " +
            ValueHolder.BP_DIS_AMT + " TEXT, " + ValueHolder.B_SELL_PRICE + " TEXT, " + ValueHolder.BT_TAX_AMT + " TEXT, " +
            ValueHolder.BT_SELL_PRICE + " TEXT, " + ValueHolder.CASE + " TEXT, " + ValueHolder.CASE_QTY + " TEXT, " +
            ValueHolder.DISAMT + " TEXT, " + ValueHolder.DISPER + " TEXT, " + ValueHolder.FREE_QTY + " TEXT, " +
            ValueHolder.ITEMCODE + " TEXT, " + ValueHolder.P_DIS_AMT + " TEXT, " + ValueHolder.PRILCODE + " TEXT, " +
            ValueHolder.QTY + " TEXT, " + ValueHolder.DIS_VAL_AMT + " TEXT, " + ValueHolder.PICE_QTY + " TEXT, " +
            ValueHolder.REACODE + " TEXT, " + ValueHolder.TYPE + " TEXT, " + ValueHolder.REFNO + " TEXT, " +
            ValueHolder.SELL_PRICE + " TEXT, " + ValueHolder.SEQNO + " TEXT, " + ValueHolder.TAXAMT + " TEXT, " +
            ValueHolder.TAXCOMCODE + " TEXT, " + ValueHolder.T_SELL_PRICE + " TEXT, " + ValueHolder.ITEMNAME + " TEXT, " +
            ValueHolder.PACKSIZE + " TEXT, " + ValueHolder.COSTPRICE + " TEXT, " + ValueHolder.DISCTYPE + " TEXT, " +
            ValueHolder.SCH_DISPER + " TEXT, " + ValueHolder.DISFLAG + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " +
            ValueHolder.IS_ACTIVE + " TEXT, " + ValueHolder.ORDERID + " TEXT," + ValueHolder.TXNTYPE + " TEXT); ";

    public static final String CREATE_FPRODUCT_PRE_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_PRODUCT_PRE + " ("
            + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.ITEMCODE + " TEXT, " + ValueHolder.ITEMNAME + " TEXT, "
            + ValueHolder.PRICE + " TEXT, " + ValueHolder.QOH + " TEXT, " + ValueHolder.QTY + " TEXT); ";

    public static final String INDEX_PRODUCTS = "CREATE UNIQUE INDEX IF NOT EXISTS ui_fProducts_pre ON fProducts_pre (itemcode,itemname);";

    public static final String CREATE_FPRETAXRG_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_PRETAXRG + " ("
            + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.TAXCODE + " TEXT, " +
            ValueHolder.TAXREGNO + " TEXT ); ";

    public static final String CREATE_FTAX_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_TAX + " (" +
            ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.TAXCODE + " TEXT, " + ValueHolder.TAXNAME + " TEXT, " +
            ValueHolder.TAXPER + " TEXT, " + ValueHolder.TAXREGNO + " TEXT ); ";

    public static final String CREATE_FPRETAXDT_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_PRETAXDT + " (" +
            ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.ITEMCODE + " TEXT, " +
            ValueHolder.TAXCOMCODE + " TEXT, " + ValueHolder.TAXCODE + " TEXT, " + ValueHolder.TAXPER + " TEXT, " +
            ValueHolder.RATE + " TEXT, " + ValueHolder.TAXSEQ + " TEXT, " + ValueHolder.DETAMT + " TEXT, " +
            ValueHolder.TAXTYPE + " TEXT, " + ValueHolder.BDETAMT + " TEXT ); ";

    private static final String CREATE_FORDHED_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_ORDHED + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.ADDMACH + " TEXT, " + ValueHolder.ADDDATE + " TEXT," +//1,2
            ValueHolder.ADDUSER + " TEXT, " + ValueHolder.APP_DATE + " TEXT, " + ValueHolder.ADDRESS + " TEXT, " + //3,4,5
            ValueHolder.APPSTS + " TEXT, " + ValueHolder.APP_USER + " TEXT, " + ValueHolder.BP_TOTAL_DIS + " TEXT, " +//6,7,8
            ValueHolder.B_TOTAL_AMT + " TEXT, " + ValueHolder.B_TOTAL_DIS + " TEXT, " + ValueHolder.B_TOTAL_TAX + " TEXT, " + //9,10,11
            ValueHolder.COSTCODE + " TEXT, " + ValueHolder.CUR_CODE + " TEXT, " + ValueHolder.CUR_RATE + " TEXT, " + //12,13,14
            ValueHolder.DEBCODE + " TEXT, " + ValueHolder.LOCCODE + " TEXT, " + ValueHolder.MANU_REF + " TEXT, " +//15,16,17
            ValueHolder.DISPER + " TEXT, " + ValueHolder.APPVERSION + " TEXT, " + ValueHolder.REFNO + " TEXT, " + //18,19,20
            ValueHolder.REMARKS + " TEXT, " + ValueHolder.REPCODE + " TEXT, " +//21,22
            ValueHolder.TAX_REG + " TEXT, " + ValueHolder.TOTAL_TAX + " TEXT, " + ValueHolder.TOTALAMT + " TEXT, " +//23,24
            ValueHolder.TOTALDIS + " TEXT, " + ValueHolder.TOTAL_ITM_DIS + " TEXT, " + ValueHolder.TOT_MKR_AMT + " TEXT, " +//25,26,27
            ValueHolder.TXNTYPE + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " + ValueHolder.LONGITUDE + " TEXT, " +//28,29,30
            ValueHolder.LATITUDE + " TEXT, " + ValueHolder.START_TIME_SO + " TEXT, " + ValueHolder.IS_SYNC + " TEXT, " +//31,32,33
            ValueHolder.IS_ACTIVE + " TEXT, " + ValueHolder.DELV_DATE + " TEXT, " + ValueHolder.ROUTECODE + " TEXT, " +//34,35,36
            ValueHolder.DIS_VAL + " TEXT, " + ValueHolder.DIS_PER_VAL + " TEXT," + ValueHolder.PAYMENT_TYPE + " TEXT," +//37,38,39
            ValueHolder.END_TIME_SO + " TEXT," + ValueHolder.ORDERID + " TEXT," + ValueHolder.STATUS + " TEXT," + //40,41,42
            ValueHolder.CUSADD1 + " TEXT," + ValueHolder.CUSADD2 + " TEXT," + ValueHolder.CUSADD3 + " TEXT," + //43,44,45
            ValueHolder.CUSMOB + " TEXT," + ValueHolder.CUSTELE + " TEXT," + //46,47
            ValueHolder.UPLOAD_TIME + " TEXT); ";//48


    public static final String CREATE_FCONTROL_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_CONTROL + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.COM_NAME + " TEXT, " + ValueHolder.COM_ADD1 + " TEXT, " +
            ValueHolder.COM_ADD2 + " TEXT, " + ValueHolder.COM_ADD3 + " TEXT, " + ValueHolder.COM_TEL1 + " TEXT, " +
            ValueHolder.COM_TEL2 + " TEXT, " + ValueHolder.COM_FAX + " TEXT, " + ValueHolder.COM_EMAIL + " TEXT, " +
            ValueHolder.COM_WEB + " TEXT, " + ValueHolder.COM_REGNO + " TEXT, " + ValueHolder.SYSTYPE + " TEXT, " +
            ValueHolder.DEALCODE + " TEXT, " + ValueHolder.BASECUR + " TEXT, " + ValueHolder.CONAGE1 + " TEXT, " +
            ValueHolder.CONAGE2 + " TEXT, " + ValueHolder.CONAGE3 + " TEXT, " + ValueHolder.CONAGE4 + " TEXT, " +
            ValueHolder.CONAGE5 + " TEXT, " + ValueHolder.CONAGE6 + " TEXT, " + ValueHolder.CONAGE7 + " TEXT, " +
            ValueHolder.CONAGE8 + " TEXT, " + ValueHolder.CONAGE9 + " TEXT, " + ValueHolder.CONAGE10 + " TEXT, " +
            ValueHolder.CONAGE11 + " TEXT, " + ValueHolder.CONAGE12 + " TEXT, " + ValueHolder.CONAGE13 + " TEXT, " +
            ValueHolder.CONAGE14 + " TEXT, " + ValueHolder.APPVERSION + " TEXT, " + ValueHolder.SEQNO + " TEXT); ";

    public static final String CREATE_FCOMPANYSETTING_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_COMPANYSETTING + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.SETTINGS_CODE + " TEXT, " + ValueHolder.GRP + " TEXT, " +
            ValueHolder.LOCATION_CHAR + " TEXT, " + ValueHolder.CHAR_VAL + " TEXT, " + ValueHolder.NUM_VAL + " TEXT, " +
            ValueHolder.REMARKS + " TEXT, " + ValueHolder.TYPE + " TEXT, " + ValueHolder.COMPANY_CODE + " TEXT); ";

    public static final String CREATE_FROUTEDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_ROUTEDET + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.DEBCODE + " TEXT, " + ValueHolder.ROUTE_CODE + " TEXT); ";

    public static final String CREATE_FFREEITEM_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FREEITEM + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.ITEMCODE + " TEXT); ";

    public static final String CREATE_FFREEHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FREEHED + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " +
            ValueHolder.DISC_DESC + " TEXT, " + ValueHolder.PRIORITY + " TEXT, " + ValueHolder.VDATEF + " TEXT, " +
            ValueHolder.VDATET + " TEXT, " + ValueHolder.REMARKS + " TEXT, " + ValueHolder.QTY + " TEXT, " + ValueHolder.FREE_IT_QTY + " TEXT, " +
            ValueHolder.TYPE + " TEXT, " + ValueHolder.MUSTFLG + " TEXT, " + ValueHolder.REC_CNT + " TEXT); ";


    public static final String CREATE_FFREEMSLAB_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FREEMSLAB + " (" + ValueHolder.ID + " " +
            "INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.QTY_F + " TEXT, " +
            ValueHolder.QTY_T + " TEXT, " + ValueHolder.QTY + " TEXT, " + ValueHolder.FREE_IT_QTY + " TEXT, " +
            ValueHolder.SEQNO + " TEXT); ";

    public static final String CREATE_FFREEDEB_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FREEDEB + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.DEBCODE + " TEXT); ";

    public static final String CREATE_FFREEDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FREEDET + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.ITEMCODE + " TEXT); ";


    public static final String CREATE_FREASON_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_REASON + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REACODE + " TEXT, " + ValueHolder.REANAME + " TEXT, " +
            ValueHolder.REATCODE + " TEXT, " + ValueHolder.TYPE + " TEXT); ";

    public static final String CREATE_FROUTE_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_ROUTE + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REPCODE + " TEXT, " + ValueHolder.ROUTECODE + " TEXT, " +
            ValueHolder.ROUTE_NAME + " TEXT, " + ValueHolder.AREACODE + " TEXT, " + ValueHolder.DEALCODE + " TEXT, " +
            ValueHolder.FREQNO + " TEXT, " + ValueHolder.KM + " TEXT, " + ValueHolder.MINPROCALL + " TEXT, " + ValueHolder.REMARKS + " TEXT, " +
            ValueHolder.STATUS + " TEXT, " + ValueHolder.RDALORATE + " TEXT); ";

    public static final String CREATE_FDDBNOTE_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_FDDBNOTE + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.REPNAME + " TEXT, " + //1,2
            ValueHolder.REMARKS + " TEXT, " + ValueHolder.ENT_REMARK + " TEXT, " + ValueHolder.PDAAMT + " TEXT, " +//3,4,5
            ValueHolder.REF_INV + " TEXT, " + ValueHolder.AMT + " TEXT, " + ValueHolder.SALE_REF_NO + " TEXT, " +//6,7,8
            ValueHolder.MANU_REF + " TEXT, " + ValueHolder.TXNTYPE + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " +//9,10,11
            ValueHolder.CUR_CODE + " TEXT, " + ValueHolder.CUR_RATE + " TEXT, " + ValueHolder.DEBCODE + " TEXT, " +//12,13,14
            ValueHolder.REPCODE + " TEXT, " + ValueHolder.TAXCOMCODE + " TEXT, " + ValueHolder.TAXAMT + " TEXT, " +//15,16,17
            ValueHolder.BT_TAX_AMT + " TEXT, " + ValueHolder.B_AMT + " TEXT, " + ValueHolder.TOT_BAL + " TEXT, " +//18,19,20
            ValueHolder.ENTER_AMT + " TEXT, " +
            ValueHolder.TOT_BAL1 + " TEXT, " + ValueHolder.OV_PAY_AMT + " TEXT, " + ValueHolder.REFNO1 + " TEXT); ";//21,22,23

    public static final String CREATE_FBANK_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_BANK + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.BANK_CODE + " TEXT, " + ValueHolder.BANK_NAME + " TEXT, " +
            ValueHolder.BANK_ACC_NO + " TEXT, " + ValueHolder.BRANCH + " TEXT, " + ValueHolder.ADD1 + " TEXT, " +
            ValueHolder.ADD2 + " TEXT); ";

    public static final String CREATE_FAREA_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_AREA + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.AREA_CODE + " TEXT, " + ValueHolder.AREA_NAME + " TEXT, " +
            ValueHolder.DEALCODE + " TEXT, " + ValueHolder.REG_CODE + " TEXT); ";

    public static final String CREATE_FLOCATIONS_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_LOCATIONS + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.LOCCODE + " TEXT, " + ValueHolder.LOC_NAME + " TEXT, " +
            ValueHolder.LOC_T_CODE + " TEXT, " + ValueHolder.COSTCODE + " TEXT); ";

    public static final String CREATE_FTOWN_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_TOWN + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.TOWN_CODE + " TEXT, " + ValueHolder.TOWN_NAME + " TEXT, " +
            ValueHolder.DISTR_CODE + " TEXT); ";

    public static final String CREATE_FTYPE_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_TYPE + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.TYPE_CODE + " TEXT, " + ValueHolder.TYPE_NAME + " TEXT); ";

    public static final String CREATE_FBRAND_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_BRAND + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.BRAND_CODE + " TEXT, " + ValueHolder.BRAND_NAME + " TEXT); ";

    public static final String CREATE_FPAYMENT_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_PAYMENTS + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.PAYMENT_INVOICE_DATE + " TEXT, " + ValueHolder.PAYMENT_INVOICE_NO + " TEXT, " +
            ValueHolder.PAYMENT_AMT + " TEXT, " + ValueHolder.PAYMENT_TYPE + " TEXT, " + ValueHolder.PAYMENT_RECEIPT_DATE + " TEXT, " +
            ValueHolder.PAYMENT_RECEIPT_NO + " TEXT); ";

    public static final String IDXFREEHED = "CREATE UNIQUE INDEX IF NOT EXISTS idxfdebtor_something ON " + ValueHolder.TABLE_DEBTOR + " (" + ValueHolder.DEBCODE + ")";

    public static final String CREATE_FCOST_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_COST + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.COSTCODE + " TEXT, " + ValueHolder.COSTNAME + " TEXT); ";

    public static final String CREATE_FSUPPLIER_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_SUPPLIER + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.SUP_CODE + " TEXT, " + ValueHolder.SUP_NAME + " TEXT); ";

    public static final String CREATE_FCOMPANYBRANCH_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FCOMPANYBRANCH + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.BRANCH_CODE + " TEXT, " + ValueHolder.CSETTINGS_CODE + " TEXT, " +
            ValueHolder.NNUM_VAL + " TEXT," + ValueHolder.FCOMPANYBRANCH_YEAR + " TEXT," + ValueHolder.FCOMPANYBRANCH_MONTH + " TEXT);";

    public static final String CREATE_FREPDEBTOR_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_REPDEBTOR + " (" + ValueHolder.ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REPCODE + " TEXT, " + ValueHolder.DEBCODE + " TEXT); ";

    public static final String CREATE_FPRECDETS_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FPRECDETS + " (" + ValueHolder.FPRECDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.REFNO + " TEXT, " + ValueHolder.FPRECDET_REFNO1 + " TEXT, " + ValueHolder.FPRECDET_REFNO2 + " TEXT, " + ValueHolder.DEBCODE + " TEXT, " + ValueHolder.FPRECDET_SALEREFNO + " TEXT, "

            + ValueHolder.FPRECDET_MANUREF + " TEXT, " + ValueHolder.FPRECDET_TXNTYPE + " TEXT, " + ValueHolder.TXNDATE + " TEXT, "
            + ValueHolder.FPRECDET_DTXNDATE + " TEXT, " + ValueHolder.FPRECDET_DTXNTYPE + " TEXT, " + ValueHolder.FPRECDET_DCURCODE + " TEXT, " + ValueHolder.FPRECDET_DCURRATE + " TEXT, "
            + ValueHolder.FPRECDET_OCURRATE + " TEXT, " + ValueHolder.REPCODE + " TEXT, " + ValueHolder.FPRECDET_AMT + " TEXT, " + ValueHolder.FPRECDET_BAMT + " TEXT, " + ValueHolder.FPRECDET_ISDELETE + " TEXT, "
            + ValueHolder.FPRECDET_REMARK + " TEXT, " + ValueHolder.FPRECDET_ALOAMT + " TEXT, " + ValueHolder.FPRECDET_OVPAYAMT + " TEXT, " + ValueHolder.FPRECDET_OVPAYBAL + " TEXT, " + ValueHolder.FPRECDET_RECORDID + " TEXT, " + ValueHolder.FPRECDET_TIMESTAMP + " TEXT );";

    public static final String CREATE_FPRECHEDS_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FPRECHEDS + " (" + ValueHolder.FPRECHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ValueHolder.REFNO + " TEXT, " + ValueHolder.FPRECHED_REFNO1 + " TEXT, " + ValueHolder.FPRECHED_MANUREF + " TEXT, " + ValueHolder.FPRECHED_SALEREFNO + " TEXT, " +
            ValueHolder.REPCODE + " TEXT, " + ValueHolder.FPRECHED_TXNTYPE + " TEXT, " + ValueHolder.FPRECHED_CHQNO + " TEXT, " + ValueHolder.FPRECHED_CHQDATE + " TEXT, " + ValueHolder.TXNDATE + " TEXT, " + ValueHolder.FPRECHED_CURCODE + " TEXT, " +
            ValueHolder.FPRECHED_CURRATE1 + " TEXT, " + ValueHolder.DEBCODE + " TEXT, " + ValueHolder.FPRECHED_TOTALAMT + " TEXT, " + ValueHolder.FPRECHED_BANKCODE + " TEXT, " + ValueHolder.FPRECHED_BRANCHCODE + " TEXT, " +
            ValueHolder.FPRECHED_BTOTALAMT + " TEXT, " + ValueHolder.FPRECHED_PAYTYPE + " TEXT, " + ValueHolder.FPRECHED_PRTCOPY + " TEXT, " + ValueHolder.FPRECHED_REMARKS + " TEXT, " + ValueHolder.FPRECHED_ADDUSER + " TEXT, " + ValueHolder.FPRECHED_ADDMACH + " TEXT, " + ValueHolder.FPRECHED_ADDDATE + " TEXT, " +
            ValueHolder.FPRECHED_RECORDID + " TEXT, " + ValueHolder.FPRECHED_TIMESTAMP + " TEXT, " + ValueHolder.FPRECHED_ISDELETE + " TEXT, " + ValueHolder.FPRECHED_COST_CODE + " TEXT, " + ValueHolder.FPRECHED_ADDUSER_NEW + " TEXT, "
            + ValueHolder.FPRECHED_LONGITUDE + " TEXT, " + ValueHolder.FPRECHED_LATITUDE + " TEXT, " + ValueHolder.FPRECHED_ADDRESS + " TEXT, " + ValueHolder.FPRECHED_START_TIME + " TEXT, " + ValueHolder.FPRECHED_END_TIME + " TEXT, " + ValueHolder.FPRECHED_ISACTIVE + " TEXT, " + ValueHolder.FPRECHED_ISSYNCED + " TEXT,"
            + ValueHolder.FPRECHED_STATUS + " TEXT, " + ValueHolder.FPRECHED_CURRATE + " TEXT, " + ValueHolder.FPRECHED_CUSBANK + " TEXT);";

    public static final String CREATE_TABLE_NONPRDHED = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_NONPRDHED + " (" + ValueHolder.NONPRDHED_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.NONPRDHED_REFNO +
            " TEXT, " + ValueHolder.NONPRDHED_TXNDAET + " TEXT, " +
            ValueHolder.REPCODE + " TEXT, " +
            ValueHolder.NONPRDHED_REMARK + " TEXT, " +
            ValueHolder.NONPRDHED_COSTCODE + " TEXT, " +
            ValueHolder.NONPRDHED_LONGITUDE + " TEXT, " +
            ValueHolder.NONPRDHED_LATITUDE + " TEXT, " +
            ValueHolder.NONPRDHED_IS_ACTIVE + " TEXT, " +
            ValueHolder.NONPRDHED_REASON + " TEXT, " +
            ValueHolder.DEBCODE + " TEXT, " +
            ValueHolder.NONPRDHED_ADDUSER + " TEXT, " + ValueHolder.NONPRDHED_ADDDATE + " TEXT," +
            ValueHolder.NONPRDHED_ADDMACH + " TEXT," + ValueHolder.NONPRDHED_TRANSBATCH + " TEXT, " +
            ValueHolder.START_TIME_SO + " TEXT," + ValueHolder.END_TIME_SO + " TEXT, " +
            ValueHolder.NONPRDHED_IS_SYNCED + " TEXT," + ValueHolder.NONPRDHED_ADDRESS + " TEXT); ";

    public static final String CREATE_TABLE_NONPRDDET = "CREATE  TABLE IF NOT EXISTS "
            + ValueHolder.TABLE_NONPRDDET + " (" + ValueHolder.NONPRDDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ValueHolder.NONPRDDET_REFNO + " TEXT, "
            + ValueHolder.NONPRDDET_TXNDATE + " TEXT, "
            + ValueHolder.REPCODE + " TEXT, "
            + ValueHolder.NONPRDDET_REASON_CODE + " TEXT, "
            + ValueHolder.NONPRDDET_REASON + " TEXT, "
            + ValueHolder.NONPRDDET_REMARK + " TEXT, "
            + ValueHolder.NONPRDDET_IS_ACTIVE + " TEXT, "
            + ValueHolder.NONPRDDET_IS_SYNCED + " TEXT); ";
    private static final String CREATE_FPRODUCT_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_PRODUCT + " ("
            + ValueHolder.FPRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ValueHolder.FPRODUCT_ITEMCODE + " TEXT, "
            + ValueHolder.FPRODUCT_ITEMNAME + " TEXT, "
            + ValueHolder.FPRODUCT_PRICE + " TEXT, "
            + ValueHolder.FPRODUCT_NOUCASE + " TEXT, "
            + ValueHolder.FPRODUCT_PACK + " TEXT, "
            + ValueHolder.FPRODUCT_QOH + " TEXT, "
            + ValueHolder.FPRODUCT_QTY + " TEXT, "
            + ValueHolder.FPRODUCT_FREESCHEMA + " TEXT, "
            + ValueHolder.FPRODUCT_supCode + " TEXT); ";

    public static final String CREATE_ATTENDANCE_TABLE = "CREATE TABLE IF NOT EXISTS " + ValueHolder.TABLE_ATTENDANCE + " (" + ValueHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ValueHolder.ATTENDANCE_DATE + " TEXT, " + ValueHolder.ATTENDANCE_S_TIME + " TEXT, " + ValueHolder.ATTENDANCE_F_TIME + " TEXT, " +
            ValueHolder.ATTENDANCE_VEHICLE + " TEXT, " + ValueHolder.ATTENDANCE_S_KM + " TEXT, " + ValueHolder.ATTENDANCE_F_KM + " TEXT, " +
            ValueHolder.ATTENDANCE_DISTANCE + " TEXT, " + ValueHolder.ATTENDANCE_IS_SYNCED + " TEXT, " + ValueHolder.REPCODE + " TEXT, " +
            ValueHolder.ATTENDANCE_DRIVER + " TEXT, " + ValueHolder.ATTENDANCE_ASSIST + " TEXT, " + ValueHolder.ATTENDANCE_MAC + " TEXT, " +
            ValueHolder.ATTENDANCE_ROUTE + " TEXT," + ValueHolder.ATTENDANCE_STLATITUDE + " TEXT, " + ValueHolder.ATTENDANCE_STLONGTITUDE + " TEXT," +
            ValueHolder.ATTENDANCE_ENDLATITUDE + " TEXT," + ValueHolder.ATTENDANCE_ENDLONGTITUDE + " TEXT ); ";


    private static final String CREATE_TBLINVHEDL3_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FINVHEDL3 +
            " (" + ValueHolder.FINVHEDL3_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.FINVHEDL3_DEB_CODE + " TEXT, " +
            ValueHolder.FINVHEDL3_REF_NO + " TEXT, " + ValueHolder.FINVHEDL3_REF_NO1 + " TEXT, " + ValueHolder.FINVHEDL3_TOTAL_AMT + " TEXT, " +
            ValueHolder.FINVHEDL3_TOTAL_TAX + " TEXT, " +  ValueHolder.FINVHEDL3_TXN_DATE + " TEXT, " +ValueHolder.TXNDATE_TIME + " TEXT); ";


    private static final String CREATE_TBLINVDETL3_TABLE = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_FINVDETL3 +
            " (" + ValueHolder.FINVDETL3_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.FINVDETL3_AMT + " TEXT, " +
            ValueHolder.FINVDETL3_ITEM_CODE + " TEXT, " + ValueHolder.FINVDETL3_QTY + " TEXT, " + ValueHolder.FINVDETL3_REF_NO + " TEXT, " +
            ValueHolder.FINVDETL3_SEQ_NO + " TEXT, " + ValueHolder.FINVDETL3_TAX_AMT + " TEXT, " + ValueHolder.FINVDETL3_TAX_COM_CODE + " TEXT, " +
            ValueHolder.FINVDETL3_TXN_DATE + " TEXT); ";

    private static final String CREATE_TBLCONNECTION = "CREATE  TABLE IF NOT EXISTS " + ValueHolder.TABLE_CONNECTION +
            " (" + ValueHolder.CONNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ValueHolder.CONN_BASE_URL + " TEXT, " +
            ValueHolder.CONN_NAME + " TEXT, "  + ValueHolder.CONN_STATUS + " TEXT); ";



    @Override
    public void onCreate(SQLiteDatabase arg0) {
        arg0.execSQL(CREATE_FDEBTOR_TABLE);
        arg0.execSQL(CREATE_CUSP_RECHED_TABLE);
        arg0.execSQL(CREATE_CUSP_RECDET_TABLE);
        arg0.execSQL(CREATE_PMSG_HEDDET_TABLE);
        arg0.execSQL(CREATE_FITEM_TABLE);
        arg0.execSQL(CREATE_FGROUP_TABLE);
        arg0.execSQL(CREATE_FORDDISC_TABLE);
        arg0.execSQL(CREATE_FITEMLOC_TABLE);
        arg0.execSQL(CREATE_FITEMPRI_TABLE);
        arg0.execSQL(CREATE_FORDFREEISS_TABLE);
        arg0.execSQL(CREATE_FTAXDET_TABLE);
        arg0.execSQL(CREATE_FDEBTAX_TABLE);
        arg0.execSQL(CREATE_FORDDET_TABLE);
        arg0.execSQL(CREATE_FPRODUCT_PRE_TABLE);
        arg0.execSQL(CREATE_FPRETAXRG_TABLE);
        arg0.execSQL(CREATE_FTAX_TABLE);
        arg0.execSQL(CREATE_FPRETAXDT_TABLE);
        arg0.execSQL(CREATE_FORDHED_TABLE);
        arg0.execSQL(CREATE_FCONTROL_TABLE);
        arg0.execSQL(CREATE_FCOMPANYSETTING_TABLE);
        arg0.execSQL(CREATE_FROUTEDET_TABLE);
        arg0.execSQL(CREATE_FFREEITEM_TABLE);
        arg0.execSQL(CREATE_FFREEHED_TABLE);
        arg0.execSQL(CREATE_FFREEMSLAB_TABLE);
        arg0.execSQL(CREATE_FREASON_TABLE);
        arg0.execSQL(CREATE_FROUTE_TABLE);
        arg0.execSQL(CREATE_FDDBNOTE_TABLE);
        arg0.execSQL(CREATE_FBANK_TABLE);
        arg0.execSQL(CREATE_FAREA_TABLE);
        arg0.execSQL(CREATE_FLOCATIONS_TABLE);
        arg0.execSQL(CREATE_FTOWN_TABLE);
        arg0.execSQL(CREATE_FFREEDEB_TABLE);
        arg0.execSQL(CREATE_FFREEDET_TABLE);
        arg0.execSQL(CREATE_FTYPE_TABLE);
        arg0.execSQL(CREATE_FBRAND_TABLE);
        arg0.execSQL(CREATE_FPAYMENT_TABLE);
        arg0.execSQL(IDXFREEHED);
        arg0.execSQL(CREATE_FREPDEBTOR_TABLE);
        arg0.execSQL(CREATE_FCOST_TABLE);
        arg0.execSQL(CREATE_FSUPPLIER_TABLE);
        arg0.execSQL(CREATE_FCOMPANYBRANCH_TABLE);
        arg0.execSQL(CREATE_FSALREP_TABLE);
        arg0.execSQL(CREATE_FPRECDETS_TABLE);
        arg0.execSQL(CREATE_FPRECHEDS_TABLE);
        arg0.execSQL(CREATE_TABLE_NONPRDHED);
        arg0.execSQL(CREATE_TABLE_NONPRDDET);
        arg0.execSQL(CREATE_FPRODUCT_TABLE);
        arg0.execSQL(CREATE_ATTENDANCE_TABLE);
        arg0.execSQL(RepTrgHedController.CREATE_FREPTRGHED_TABLE);
        arg0.execSQL(RepTrgDetController.CREATE_FREPTRGDET_TABLE);
        arg0.execSQL(FItenrDetController.CREATE_FITENRDET_TABLE);
        arg0.execSQL(InvHedController.CREATE_FINVHED_TABLE);
        arg0.execSQL(InvHedController.CREATE_FINVHED_TABLE_LOG);
        arg0.execSQL(InvDetController.CREATE_FINVDET_TABLE);
        arg0.execSQL(InvDetController.CREATE_FINVDET_TABLE_LOG);
        arg0.execSQL(InvHedController.CREATE_FINVHED_TABLE);
        arg0.execSQL(InvDetController.CREATE_FINVDET_TABLE);
        arg0.execSQL(ExpenseController.CREATE_FEXPENSE_TABLE);
        arg0.execSQL(SalesReturnDetController.CREATE_FINVRDET_TABLE);
        arg0.execSQL(ReceiptDetController.CREATE_FPRECDETS_TABLE);
        arg0.execSQL(DayExpDetController.CREATE_FDAYEXPDET_TABLE);
        arg0.execSQL(RepGPSLocationController.CREATE_FDAILY_REPGPS_TABLE);
        arg0.execSQL(DownloadController.CREATE_DOWNLOAD_TABLE);
        arg0.execSQL(CREATE_TBLINVHEDL3_TABLE);
        arg0.execSQL(CREATE_TBLINVDETL3_TABLE);
        arg0.execSQL(IDXFSALREP);
        arg0.execSQL(CREATE_TBLCONNECTION);

    }

    // --------------------------------------------------------------------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        this.onCreate(arg0);

        try {

            arg0.execSQL(CREATE_FDEBTOR_TABLE);
            arg0.execSQL(CREATE_CUSP_RECHED_TABLE);
            arg0.execSQL(CREATE_CUSP_RECDET_TABLE);
            arg0.execSQL(CREATE_PMSG_HEDDET_TABLE);
            arg0.execSQL(CREATE_FITEM_TABLE);
            arg0.execSQL(CREATE_FGROUP_TABLE);
            arg0.execSQL(CREATE_FORDDISC_TABLE);
            arg0.execSQL(CREATE_FITEMLOC_TABLE);
            arg0.execSQL(CREATE_FITEMPRI_TABLE);
            arg0.execSQL(CREATE_FORDFREEISS_TABLE);
            arg0.execSQL(CREATE_FTAXDET_TABLE);
            arg0.execSQL(CREATE_FDEBTAX_TABLE);
            arg0.execSQL(CREATE_FORDDET_TABLE);
            arg0.execSQL(CREATE_FPRODUCT_PRE_TABLE);
            arg0.execSQL(CREATE_FPRETAXRG_TABLE);
            arg0.execSQL(CREATE_FTAX_TABLE);
            arg0.execSQL(CREATE_FPRETAXDT_TABLE);
            arg0.execSQL(CREATE_FORDHED_TABLE);
            arg0.execSQL(CREATE_FCONTROL_TABLE);
            arg0.execSQL(CREATE_FCOMPANYSETTING_TABLE);
            arg0.execSQL(CREATE_FROUTEDET_TABLE);
            arg0.execSQL(CREATE_FFREEITEM_TABLE);
            arg0.execSQL(CREATE_FFREEHED_TABLE);
            arg0.execSQL(CREATE_FFREEMSLAB_TABLE);
            arg0.execSQL(CREATE_FREASON_TABLE);
            arg0.execSQL(CREATE_FROUTE_TABLE);
            arg0.execSQL(CREATE_FDDBNOTE_TABLE);
            arg0.execSQL(CREATE_FBANK_TABLE);
            arg0.execSQL(CREATE_FAREA_TABLE);
            arg0.execSQL(CREATE_FLOCATIONS_TABLE);
            arg0.execSQL(CREATE_FTOWN_TABLE);
            arg0.execSQL(CREATE_FFREEDEB_TABLE);
            arg0.execSQL(CREATE_FFREEDET_TABLE);
            arg0.execSQL(CREATE_FTYPE_TABLE);
            arg0.execSQL(CREATE_FBRAND_TABLE);
            arg0.execSQL(CREATE_FPAYMENT_TABLE);
            arg0.execSQL(IDXFREEHED);
            arg0.execSQL(CREATE_FREPDEBTOR_TABLE);
            arg0.execSQL(CREATE_FCOST_TABLE);
            arg0.execSQL(CREATE_FSUPPLIER_TABLE);
            arg0.execSQL(CREATE_FCOMPANYBRANCH_TABLE);
            arg0.execSQL(CREATE_FSALREP_TABLE);
            arg0.execSQL(CREATE_FPRECDETS_TABLE);
            arg0.execSQL(CREATE_FPRECHEDS_TABLE);
            arg0.execSQL(CREATE_TABLE_NONPRDHED);
            arg0.execSQL(CREATE_TABLE_NONPRDDET);
            arg0.execSQL(CREATE_FPRODUCT_TABLE);
            arg0.execSQL(CREATE_ATTENDANCE_TABLE);
            arg0.execSQL(RepTrgHedController.CREATE_FREPTRGHED_TABLE);
            arg0.execSQL(RepTrgDetController.CREATE_FREPTRGDET_TABLE);
            arg0.execSQL(FItenrDetController.CREATE_FITENRDET_TABLE);
            arg0.execSQL(InvHedController.CREATE_FINVHED_TABLE);
            arg0.execSQL(InvHedController.CREATE_FINVHED_TABLE_LOG);
            arg0.execSQL(InvDetController.CREATE_FINVDET_TABLE);
            arg0.execSQL(InvDetController.CREATE_FINVDET_TABLE_LOG);
            arg0.execSQL(InvHedController.CREATE_FINVHED_TABLE);
            arg0.execSQL(InvDetController.CREATE_FINVDET_TABLE);
            arg0.execSQL(ExpenseController.CREATE_FEXPENSE_TABLE);
            arg0.execSQL(SalesReturnDetController.CREATE_FINVRDET_TABLE);
            arg0.execSQL(ReceiptDetController.CREATE_FPRECDETS_TABLE);
            arg0.execSQL(DayExpDetController.CREATE_FDAYEXPDET_TABLE);
            arg0.execSQL(RepGPSLocationController.CREATE_FDAILY_REPGPS_TABLE);
            arg0.execSQL(DownloadController.CREATE_DOWNLOAD_TABLE);
            arg0.execSQL(CREATE_TBLINVHEDL3_TABLE);
            arg0.execSQL(CREATE_TBLINVDETL3_TABLE);
            arg0.execSQL(IDXFSALREP);
            arg0.execSQL(CREATE_TBLCONNECTION);

            try {
                arg0.execSQL("ALTER TABLE TblRecHedS ADD COLUMN Status TEXT DEFAULT ''");

            } catch (SQLiteException e) {
                Log.v("SQLiteException", e.toString());
            }

            try {
                arg0.execSQL("ALTER TABLE TblinvHedL3 ADD COLUMN TxnDateTime TEXT DEFAULT ''");

            } catch (SQLiteException e) {
                Log.v("SQLiteException", e.toString());
            }

            try {
                arg0.execSQL("ALTER TABLE TblOrder ADD COLUMN CusAdd1 TEXT DEFAULT ''");

            } catch (SQLiteException e) {
                Log.v("SQLiteException", e.toString());
            }

            try {
                arg0.execSQL("ALTER TABLE TblOrder ADD COLUMN CusAdd2 TEXT DEFAULT ''");

            } catch (SQLiteException e) {
                Log.v("SQLiteException", e.toString());
            }

            try {
                arg0.execSQL("ALTER TABLE TblOrder ADD COLUMN CusAdd3 TEXT DEFAULT ''");

            } catch (SQLiteException e) {
                Log.v("SQLiteException", e.toString());
            }

            try {
                arg0.execSQL("ALTER TABLE TblOrder ADD COLUMN CusMob TEXT DEFAULT ''");

            } catch (SQLiteException e) {
                Log.v("SQLiteException", e.toString());
            }

            try {
                arg0.execSQL("ALTER TABLE TblOrder ADD COLUMN CusTele TEXT DEFAULT ''");

            } catch (SQLiteException e) {
                Log.v("SQLiteException", e.toString());
            }


        } catch (SQLiteException e) {
        }

    }
}