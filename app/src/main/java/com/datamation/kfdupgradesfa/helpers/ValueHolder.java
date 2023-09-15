package com.datamation.kfdupgradesfa.helpers;

import android.content.SharedPreferences;

import com.datamation.kfdupgradesfa.model.CompanyBranch;

public class ValueHolder {
    /**
     * created by rashmi 2020-08-14
     */
    //GPS values
    public static final int LOCATION_SWITCH_DELAY = 10 * 1000;

    public static final String CONNECT_TO_WIFI = "WIFI";
    public static final String NOT_CONNECT = "NOT_CONNECT";
    //common string
    public static final String ID = "Id";
    public static final String REFNO = "RefNo";
    public static final String REFNO1 = "RefNo1";
    public static final String TXNDATE = "TxnDate";
    public static final String TXNTIME = "TxnTime";
    public static final String ADDMACH = "AddMach";
    public static final String ADDUSER = "AddUser";
    public static final String ADDDATE = "AddDate";
    public static final String REPCODE = "RepCode";
    public static final String DEBCODE = "DebCode";
    public static final String ROUTECODE = "RouteCode";
    public static final String DEALCODE = "DealCode";
    public static final String PRILCODE = "PrilCode";
    public static final String ITEMCODE = "ItemCode";
    public static final String LOCCODE = "LocCode";
    public static final String COSTCODE = "CostCode";
    public static final String TAXCOMCODE = "TaxComCode";
    public static final String REACODE = "ReaCode";
    public static final String TOTALAMT = "TotalAmt";
    public static final String AMT = "Amt";
    public static final String DISAMT = "DisAmt";
    public static final String TAXAMT = "TaxAmt";
    public static final String STATUS = "Status";
    public static final String IS_SYNC = "IsSync";
    public static final String IS_ACTIVE = "isActive";
    public static final String DISPER = "DisPer";
    public static final String ITEMNAME = "ItemName";
    public static final String QOH = "QOH";
    public static final String PRICE = "Price";
    public static final String QTY = "Qty";
    public static final String TXNTYPE = "TxnType";
    public static final String TAX_REG = "TaxReg";
    public static final String AREACODE = "AreaCode";
    public static final String APPVERSION = "AppVersion";
    public static final String SEQNO = "SeqNo";
    public static final String REMARKS = "Remarks";
    public static final String TYPE = "Types";
    public static final String FREE_IT_QTY = "FreeItQty";
    public static final String MANU_REF = "ManuRef";
    public static final String CUR_CODE = "CurCode";
    public static final String CUR_RATE = "CurRate";
    public static final String BT_TAX_AMT = "BTaxAmt";
    public static final String B_AMT = "BAmt";
    public static final String TYPE_CODE = "TypeCode";
    public static final String BRAND_CODE = "BrandCode";
    public static final String GROUP_CODE = "GroupCode";
    public static final String REPNAME = "RepName";
    public static final String DEBTOR_REP = "DebtorRep";
    public static final String CUSADD1 = "CusAdd1";
    public static final String CUSADD2 = "CusAdd2";
    public static final String CUSADD3 = "CusAdd3";
    public static final String CUSMOB = "CusMob";
    public static final String CUSTELE = "CusTele";
    public static final String TXNDATE_TIME = "TxnDateTime";


    //table strings
    public static final String TABLE_DEBTOR = "TblDebtor";
    public static final String TABLE_CUSP_RECHED = "TblPaymentHeader";
    public static final String TABLE_CUSP_RECDET = "TblPaymentDetail";
    public static final String TABLE_PMSG_HEDDET = "TblPushMsg";
    public static final String TABLE_ITEM = "TblItem";
    public static final String TABLE_GROUP = "TblGroup";
    public static final String TABLE_ORDDISC = "TblOrdDisc";
    public static final String TABLE_ITEMLOC = "TblItemLoc";
    public static final String TABLE_ITEMPRI = "TblItemPri";
    public static final String TABLE_ORDFREEISS = "TblOrdFreeIss";
    public static final String TABLE_TAXDET = "TblTaxDet";
    public static final String TABLE_DEBTAX = "TblDebTax";
    public static final String TABLE_ORDDET = "TblOrddet";
    public static final String TABLE_PRODUCT_PRE = "TblProductsPre";
    public static final String TABLE_PRETAXRG = "TblPreTaxRg";
    public static final String TABLE_PRETAXDT = "TblPreTaxDT";
    public static final String TABLE_TAX = "TblTax";
    public static final String TABLE_ORDHED = "TblOrder";
    public static final String TABLE_CONTROL = "TblControl";
    public static final String TABLE_COMPANYSETTING = "TblCompanySetting";
    public static final String TABLE_ROUTEDET = "TblRouteDet";
    public static final String TABLE_FREEITEM = "TblFreeItem";
    public static final String TABLE_FREEHED = "TblFreehed";
    public static final String TABLE_FREEMSLAB = "TblFreeMslab";
    public static final String TABLE_FREEDEB = "TblFreedeb";
    public static final String TABLE_FREEDET = "TblFreedet";
    public static final String TABLE_REASON = "TblReason";
    public static final String TABLE_ROUTE = "TblRoute";
    public static final String TABLE_FDDBNOTE = "TblFDDbNote";
    public static final String TABLE_BANK = "TblBank";
    public static final String TABLE_AREA = "TblArea";
    public static final String TABLE_LOCATIONS = "TblLocations";
    public static final String TABLE_TOWN = "TblTown";
    public static final String TABLE_TYPE = "TblType";
    public static final String TABLE_BRAND = "TblBrand";
    public static final String TABLE_PAYMENTS = "TblPayments";
    public static final String TABLE_REPDEBTOR = "TblRepDebtor";
    public static final String TABLE_COST = "TblCost";
    public static final String TABLE_SUPPLIER = "TblSupplier";
    public static final String TABLE_FCOMPANYBRANCH = "TblCompanyBranch";
    public static final String TABLE_SALREP = "TblSalRep";
    public static final String TABLE_FPRECDET = "TblRecDet";
    public static final String TABLE_FPRECDETS = "TblRecDetS";
    public static final String TABLE_FPRECHEDS = "TblRecHedS";
    public static final String TABLE_NONPRDHED = "TblDaynPrdHed";
    public static final String TABLE_NONPRDDET = "TblDaynPrdDet";
    public static final String TABLE_PRODUCT = "TblProducts";
    public static final String TABLE_ATTENDANCE = "TblAttendance";
    public static final String TABLE_FINVHEDL3 = "TblinvHedL3";
    public static final String TABLE_FINVDETL3 = "TblinvDetL3";

    // Company Branch
    public static final String BRANCH_CODE = "BranchCode";
    public static final String CSETTINGS_CODE = "cSettingsCode";
    public static final String NNUM_VAL = "nNumVal";
    public static final String FCOMPANYBRANCH_YEAR = "nYear";
    public static final String FCOMPANYBRANCH_MONTH = "nMonth";

    //SalRep
    public static final String FSALREP_PASSWORD = "Password";
    public static final String FSALREP_EMAIL = "Email";
    public static final String FSALREP_MOB = "Mobile";
    public static final String FSALREP_IDNO = "RepIdNo";
    public static final String FSALREP_PREFIX = "Prefix";
    public static final String FSALREP_TELE = "Tele";
    public static final String FSALREP_MAC = "Mac";

    // debtor
    public static final String FDEBTOR_NAME = "DebName";
    public static final String FDEBTOR_ADD1 = "DebAdd1";
    public static final String FDEBTOR_ADD2 = "DebAdd2";
    public static final String FDEBTOR_ADD3 = "DebAdd3";
    public static final String FDEBTOR_TELE = "DebTele";
    public static final String FDEBTOR_MOB = "DebMob";
    public static final String FDEBTOR_EMAIL = "DebEMail";
    public static final String FDEBTOR_TOWN_CODE = "TownCode";
    public static final String FDEBTOR_DBGR_CODE = "DbGrCode";
    public static final String FDEBTOR_CRD_PERIOD = "CrdPeriod";
    public static final String FDEBTOR_CHK_CRD_PRD = "ChkCrdPrd";
    public static final String FDEBTOR_CHK_FREE = "ChkFree";
    public static final String FDEBTOR_CHK_MUST_FRE = "ChkMustFre";
    public static final String FDEBTOR_CRD_LIMIT = "CrdLimit";
    public static final String FDEBTOR_CHK_CRD_LIMIT = "ChkCrdLmt";
    public static final String FDEBTOR_RANK_CODE = "RankCode";
    public static final String FDEBTOR_ID = "CusId";
    public static final String FDEBTOR_MULTIFLG = "MultiFlg";

    //payment
    public static final String RECHED_CHQDATE = "ChqDate";
    public static final String RECHED_CHQNO = "ChqNo";
    public static final String RECHED_PAYTYPE = "PayType";
    public static final String RECDET_ALOAMT = "AloAmt";
    public static final String RECDET_INVNO = "InvNo";
    public static final String RECDET_INVTXNDATE = "InvTxnDate";

    // Push Msg
    public static final String PMSG_HEDDET_MBODY = "MsgBody";
    public static final String PMSG_HEDDET_MGRP = "MsgGrp";
    public static final String PMSG_HEDDET_MTYPE = "MsgType";
    public static final String PMSG_HEDDET_SUBJECT = "Subject";
    public static final String PMSG_HEDDET_SUPCODE = "SupCode";

    //item
    public static final String AVG_PRICE = "AvgPrice";
    public static final String ITEM_STATUS = "ItemStatus";
    public static final String MUST_SALE = "MustSale";
    public static final String NOU_CASE = "NOUCase";
    public static final String ORD_SEQ = "OrdSeq";
    public static final String RE_ORDER_LVL = "ReOrderLvl";
    public static final String RE_ORDER_QTY = "ReOrderQty";
    public static final String UNIT_CODE = "UnitCode";
    public static final String VEN_P_CODE = "VenPcode";
    public static final String CAT_CODE = "CatCode";
    public static final String PACK = "Pack";
    public static final String PACK_SIZE = "PackSize";
    public static final String SUP_CODE = "SupCode";
    public static final String MUST_FREE = "ChkMustFre";
    public static final String GENERIC_NAME = "GenericName";

    //group
    public static final String FGROUP_NAME = "GroupName";

    //order detail

    public static final String BAL_QTY = "BalQty";
    public static final String B_DIS_AMT = "BDisAmt";
    public static final String BP_DIS_AMT = "BPDisAmt";
    public static final String B_SELL_PRICE = "BSellPrice";
    public static final String BT_SELL_PRICE = "BTSellPrice";
    public static final String CASE = "Cases";
    public static final String CASE_QTY = "CaseQty";
    public static final String FREE_QTY = "freeqty";
    public static final String P_DIS_AMT = "PDisAmt";
    public static final String DIS_VAL_AMT = "DisValAmt";
    public static final String PICE_QTY = "PiceQty";
    public static final String DISCTYPE = "DiscType";
    public static final String SELL_PRICE = "SellPrice";
    public static final String T_SELL_PRICE = "TSellPrice";
    public static final String DISFLAG = "DisFlag";
    public static final String SCH_DISPER = "SchDisPer";
    public static final String PACKSIZE = "PackSize";
    public static final String COSTPRICE = "CostPrice";

    //tax
    public static final String TAXCODE = "TaxCode";
    public static final String RATE = "Rate";
    public static final String TAXSEQ = "TaxSeq";
    public static final String TAXVAL = "TaxVal";
    public static final String TAXTYPE = "TaxType";
    public static final String TAXREGNO = "taxRegNo";
    public static final String TAXNAME = "TaxName";
    public static final String TAXPER = "TaxPer";
    public static final String DETAMT = "TaxDetAmt";
    public static final String BDETAMT = "BTaxDetAmt";

    //order
    public static final String APP_DATE = "Appdate";
    public static final String APPSTS = "Appsts";
    public static final String APP_USER = "AppUser";
    public static final String BP_TOTAL_DIS = "BPTotalDis";
    public static final String B_TOTAL_AMT = "BTotalAmt";
    public static final String B_TOTAL_DIS = "BTotalDis";
    public static final String B_TOTAL_TAX = "BTotalTax";
    public static final String START_TIME_SO = "startTimeSO";
    public static final String END_TIME_SO = "endTimeSO";
    public static final String LONGITUDE = "Longitude";
    public static final String LATITUDE = "Latitude";
    public static final String TOTALDIS = "TotalDis";
    public static final String TOTAL_TAX = "TotalTax";
    public static final String ADDRESS = "gpsAddress";
    public static final String TOTAL_ITM_DIS = "TotalItemDis";
    public static final String TOT_MKR_AMT = "TotMkrAmt";
    public static final String DELV_DATE = "DelvDate";
    public static final String DIS_VAL = "HedDisVal";
    public static final String DIS_PER_VAL = "HedDisPerVal";
    public static final String PAYMENT_TYPE = "PaymentType";
    public static final String UPLOAD_TIME = "UploadTime";
    public static final String ORDERID = "OrderId";
    public static final String FEEDBACK = "feedback";

    //Compnay Details
    public static final String COM_NAME = "ComName";
    public static final String COM_ADD1 = "ComAdd1";
    public static final String COM_ADD2 = "ComAdd2";
    public static final String COM_ADD3 = "ComAdd3";
    public static final String COM_TEL1 = "comtel1";
    public static final String COM_TEL2 = "comtel2";
    public static final String COM_FAX = "comfax1";
    public static final String COM_EMAIL = "comemail";
    public static final String COM_WEB = "comweb";
    public static final String COM_REGNO = "comRegNo";
    public static final String SYSTYPE = "SysType";
    public static final String BASECUR = "basecur";
    public static final String CONAGE1 = "conage1";
    public static final String CONAGE2 = "conage2";
    public static final String CONAGE3 = "conage3";
    public static final String CONAGE4 = "conage4";
    public static final String CONAGE5 = "conage5";
    public static final String CONAGE6 = "conage6";
    public static final String CONAGE7 = "conage7";
    public static final String CONAGE8 = "conage8";
    public static final String CONAGE9 = "conage9";
    public static final String CONAGE10 = "conage10";
    public static final String CONAGE11 = "conage11";
    public static final String CONAGE12 = "conage12";
    public static final String CONAGE13 = "conage13";
    public static final String CONAGE14 = "conage14";

    //Company Setting
    public static final String SETTINGS_CODE = "cSettingsCode";
    public static final String GRP = "cSettingGrp";
    public static final String LOCATION_CHAR = "cLocationChar";
    public static final String CHAR_VAL = "cCharVal";
    public static final String NUM_VAL = "nNumVal";
    public static final String COMPANY_CODE = "cCompanyCode";

    //Route Det
    public static final String ROUTE_CODE = "RouteCode";

    //ItemPri
    public static final String SKUCODE = "SKUCode";

    //Free Hed
    public static final String DISC_DESC = "DiscDesc";
    public static final String PRIORITY = "Priority";
    public static final String VDATEF = "Vdatef";
    public static final String VDATET = "Vdatet";
    public static final String MUSTFLG = "Mustflg";
    public static final String REC_CNT = "RecCnt";
    public static final String FREE_ITEM_QTY = "RecCnt";
    public static final String FREE_ISSUE_QTY = "RecCnt";

    //Freemslab
    public static final String QTY_F = "Qtyf";
    public static final String QTY_T = "Qtyt";
    public static final String ITEMQTY = "Qty";
    public static final String FREEIT_QTY = "FreeItQty";

    //Reason
    public static final String REANAME = "ReaName";
    public static final String REATCODE = "ReaTcode";

    //Route
    public static final String ROUTE_NAME = "RouteName";
    public static final String FREQNO = "FreqNo";
    public static final String KM = "Km";
    public static final String MINPROCALL = "MinProcall";
    public static final String RDALORATE = "RDAloRate";

    //FDDbNote
    public static final String REF_INV = "RefInv";
    public static final String SALE_REF_NO = "SaleRefNo";
    public static final String TOT_BAL = "TotBal";
    public static final String TOT_BAL1 = "TotBal1";
    public static final String OV_PAY_AMT = "OvPayAmt";
    public static final String ENTER_AMT = "EnterAmt";
    public static final String ENT_REMARK = "EntRemark";
    public static final String PDAAMT = "PdaAmt";

    //Bank
    public static final String BANK_CODE = "BankCode";
    public static final String BANK_NAME = "BankName";
    public static final String BANK_ACC_NO = "BankAccno";
    public static final String BRANCH = "Branch";
    public static final String ADD1 = "BankAdd1";
    public static final String ADD2 = "BankAdd2";

    //Area
    public static final String AREA_CODE = "AreaCode";
    public static final String AREA_NAME = "AreaName";
    public static final String REG_CODE = "RegCode";

    //Locations
    public static final String LOC_NAME = "LocName";
    public static final String LOC_T_CODE = "LoctCode";

    //Town
    public static final String TOWN_CODE = "TownCode";
    public static final String TOWN_NAME = "TownName";
    public static final String DISTR_CODE = "DistrCode";

    //Type
    public static final String TYPE_NAME = "TypeName";

    //Brand
    public static final String BRAND_NAME = "BrandName";

    //Payments
    public static final String PAYMENT_INVOICE_DATE = "InvoiceDate";
    public static final String PAYMENT_INVOICE_NO = "InvoiceNo";
    public static final String PAYMENT_AMT = "PaidAmount";
    public static final String PAYMENT_RECEIPT_DATE = "ReceiptDate";
    public static final String PAYMENT_RECEIPT_NO = "ReceiptNo";

    //Cost
    public static final String COSTNAME = "CostName";

    //Supplier
    public static final String SUP_NAME = "SupName";


    //Receipt Det
    public static final String FPRECDET_ID = "Id";
    public static final String FPRECDET_REFNO1 = "RefNo1";
    public static final String FPRECDET_REFNO2 = "RefNo2";
    public static final String FPRECDET_SALEREFNO = "SaleRefNo";
    public static final String FPRECDET_MANUREF = "ManuRef";
    public static final String FPRECDET_TXNTYPE = "TxnType";

    public static final String FPRECDET_DTXNDATE = "DtxnDate";
    public static final String FPRECDET_DTXNTYPE = "DtxnType";
    public static final String FPRECDET_DCURCODE = "DCurCode";
    public static final String FPRECDET_DCURRATE = "DCurRate";
    public static final String FPRECDET_OCURRATE = "OCurRate";
    public static final String FPRECDET_AMT = "Amt";
    public static final String FPRECDET_BAMT = "BAmt";
    public static final String FPRECDET_ALOAMT = "AloAmt";
    public static final String FPRECDET_OVPAYAMT = "OvPayAmt";
    public static final String FPRECDET_OVPAYBAL = "OvPayBal";
    public static final String FPRECDET_RECORDID = "RecordId";
    public static final String FPRECDET_TIMESTAMP = "timestamp_column";
    public static final String FPRECDET_ISDELETE = "IsDelete";
    public static final String FPRECDET_REMARK = "Remark";

    //Receipt Hed
    public static final String TABLE_FPRECHED = "fpRecHed";
    public static final String FPRECHED_ID = "Id";
    public static final String FPRECHED_REFNO1 = "RefNo1";
    public static final String FPRECHED_MANUREF = "ManuRef";
    public static final String FPRECHED_SALEREFNO = "SaleRefNo";
    public static final String FPRECHED_TXNTYPE = "TxnType";
    public static final String FPRECHED_CHQNO = "Chqno";
    public static final String FPRECHED_CHQDATE = "ChqDate";

    public static final String FPRECHED_CURCODE = "CurCode";
    public static final String FPRECHED_CURRATE1 = "CurRate1";
    public static final String FPRECHED_TOTALAMT = "TotalAmt";
    public static final String FPRECHED_BTOTALAMT = "BTotalAmt";
    public static final String FPRECHED_PAYTYPE = "PayType";
    public static final String FPRECHED_PRTCOPY = "PrtCopy";
    public static final String FPRECHED_REMARKS = "Remarks";
    public static final String FPRECHED_ADDUSER = "AddUser";
    public static final String FPRECHED_ADDMACH = "AddMach";
    public static final String FPRECHED_ADDDATE = "AddDate";
    public static final String FPRECHED_RECORDID = "RecordId";
    public static final String FPRECHED_TIMESTAMP = "timestamp_column";
    public static final String FPRECHED_CURRATE = "CurRate";
    public static final String FPRECHED_CUSBANK = "CusBank";
    public static final String FPRECHED_COST_CODE = "CostCode";
    public static final String FPRECHED_LONGITUDE = "Longitude";
    public static final String FPRECHED_LATITUDE = "Latitude";
    public static final String FPRECHED_ADDRESS = "Address";
    public static final String FPRECHED_START_TIME = "StartTime";
    public static final String FPRECHED_END_TIME = "EndTime";
    public static final String FPRECHED_ISACTIVE = "IsActive";
    public static final String FPRECHED_ISSYNCED = "IsSynced";
    public static final String FPRECHED_STATUS= "Status";
    public static final String FPRECHED_ISDELETE = "IsDelete";
    public static final String FPRECHED_BANKCODE = "BankCode";
    public static final String FPRECHED_ADDUSER_NEW = "AddUserNew";
    public static final String FPRECHED_BRANCHCODE = "BranchCode";


    // table attributes - NonPrdHed
    public static final String NONPRDHED_ID = "FNonprdHed_id";
    public static final String NONPRDHED_REFNO = "RefNo";
    public static final String NONPRDHED_TXNDAET = "TxnDate";
    public static final String NONPRDHED_REMARK = "Remarks";
    public static final String NONPRDHED_COSTCODE = "CostCode";
    public static final String NONPRDHED_ADDUSER = "AddUser";
    public static final String NONPRDHED_ADDDATE = "AddDate";
    public static final String NONPRDHED_ADDMACH = "AddMach";
    public static final String NONPRDHED_TRANSBATCH = "TranBatch";
    public static final String NONPRDHED_IS_SYNCED = "ISsync";
    public static final String NONPRDHED_ADDRESS = "Address";
    public static final String NONPRDHED_REASON = "Reason";
    public static final String NONPRDHED_LONGITUDE = "Longitude";
    public static final String NONPRDHED_LATITUDE = "Latitude";
    public static final String NONPRDHED_IS_ACTIVE = "ISActive";


    // table attributes -NonPrdDet
    public static final String NONPRDDET_ID = "FNonprdDet_id";
    public static final String NONPRDDET_REFNO = "RefNo";
    public static final String NONPRDDET_TXNDATE = "TxnDate";
    public static final String NONPRDDET_REASON = "Reason";
    public static final String NONPRDDET_REASON_CODE = "ReasonCode";
    public static final String NONPRDDET_IS_SYNCED = "ISsync";
    public static final String NONPRDDET_IS_ACTIVE = "IsActive";
    public static final String NONPRDDET_REMARK = "Remark";

    // Attendance
    public static final String ATTENDANCE_DATE = "tDate";
    public static final String ATTENDANCE_S_TIME = "StartTime";
    public static final String ATTENDANCE_F_TIME = "EndTime";
    public static final String ATTENDANCE_VEHICLE = "Vehicle";
    public static final String ATTENDANCE_S_KM = "StartKm";
    public static final String ATTENDANCE_F_KM = "EndKm";
    public static final String ATTENDANCE_ROUTE = "Route";
    public static final String ATTENDANCE_DRIVER = "Driver";
    public static final String ATTENDANCE_ASSIST = "Assist";
    public static final String ATTENDANCE_DISTANCE = "Distance";
    public static final String ATTENDANCE_IS_SYNCED = "IsSynced";
    public static final String ATTENDANCE_MAC = "MacAdd";
    public static final String ATTENDANCE_STLATITUDE = "StLtitiude";
    public static final String ATTENDANCE_STLONGTITUDE = "StLongtitiude";
    public static final String ATTENDANCE_ENDLATITUDE = "EndLtitiude";
    public static final String ATTENDANCE_ENDLONGTITUDE = "EndLongtitiude";


    // table attributes -Product
    public static final String FPRODUCT_ID = "id";
    public static final String FPRODUCT_ITEMCODE = "itemcode";
    public static final String FPRODUCT_ITEMNAME = "itemname";
    public static final String FPRODUCT_PRICE = "price";
    public static final String FPRODUCT_QOH = "qoh";
    public static final String FPRODUCT_NOUCASE = "NOUCase";
    public static final String FPRODUCT_PACK = "Pack";
    public static final String FPRODUCT_QTY = "qty";
    public static final String FPRODUCT_FREESCHEMA = "fSchema";
    public static final String FPRODUCT_supCode = "SupCode";

    // table attributes -Product
    public static final String FBATTERY_ST = "baterry_lvl";

    // table attribute TblinvHedL3 table Details
    public static final String FINVHEDL3_ID = "FinvHedL3_id";
    public static final String FINVHEDL3_DEB_CODE = "DebCode";
    public static final String FINVHEDL3_REF_NO = "RefNo";
    public static final String FINVHEDL3_REF_NO1 = "RefNo1";
    public static final String FINVHEDL3_TOTAL_AMT = "TotalAmt";
    public static final String FINVHEDL3_TOTAL_TAX = "TotalTax";
    public static final String FINVHEDL3_TXN_DATE = "TxnDate";


    // table attribute TblinvHedL3 table Details
    public static final String FINVDETL3_ID = "FinvDetL3_id";
    public static final String FINVDETL3_AMT = "Amt";
    public static final String FINVDETL3_ITEM_CODE = "ItemCode";
    public static final String FINVDETL3_QTY = "Qty";
    public static final String FINVDETL3_REF_NO = "RefNo";
    public static final String FINVDETL3_SEQ_NO = "SeqNo";
    public static final String FINVDETL3_TAX_AMT = "TaxAmt";
    public static final String FINVDETL3_TAX_COM_CODE = "TaxComCode";
    public static final String FINVDETL3_TXN_DATE = "TxnDate";


}
