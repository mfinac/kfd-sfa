package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Order {
    private String FORDHED_ID;


    private String FORDHED_REFNO;
    private String FORDHED_ADD_DATE;
    private String FORDHED_ADD_MACH;
    private String FORDHED_ADD_USER;
    private String FORDHED_APP_DATE;
    private String FORDHED_APPSTS;
    private String FORDHED_APP_USER;
    private String FORDHED_BP_TOTAL_DIS;
    private String FORDHED_B_TOTAL_AMT;
    private String FORDHED_B_TOTAL_DIS;
    private String FORDHED_B_TOTAL_TAX;
    private String FORDHED_COST_CODE;
    private String FORDHED_CUR_CODE;
    private String FORDHED_CUR_RATE;
    private String FORDHED_DEB_CODE;
    private String FORDHED_DIS_PER;
    private String FORDHED_START_TIME_SO;
    private String FORDHED_END_TIME_SO;
    private String FORDHED_LONGITUDE;
    private String FORDHED_LATITUDE;
    private String FORDHED_LOC_CODE;
    private String FORDHED_MANU_REF;
    private String FORDHED_ORDERID;
    private String FORDHED_RECORD_ID;
    private String FORDHED_REMARKS;
    private String FORDHED_REPCODE;
    private String FORDHED_TAX_REG;
    private String FORDHED_TOTAL_AMT;
    private String FORDHED_TOTALDIS;
    private String FORDHED_TOTAL_TAX;
    private String FORDHED_TXN_TYPE;
    private String FORDHED_TXN_DATE;
    private String FORDHED_ADDRESS;
    private String FORDHED_TOTAL_ITM_DIS;
    private String FORDHED_TOT_MKR_AMT;
    private String FORDHED_IS_SYNCED;
    private String FORDHED_IS_ACTIVE;
    private String FORDHED_DELV_DATE;
    private String FORDHED_ROUTE_CODE;
    private String FORDHED_AREA_CODE;
    private String FORDHED_HED_DIS_VAL;
    private String FORDHED_HED_DIS_PER_VAL;
    private String FORDHED_PAYMENT_TYPE;
    private String FORDHED_UPLOAD_TIME;
    private String DistDB;
    private String ConsoleDB;
    private String FEEDBACK;
    private String NextNumVal;
    private String FORDHED_CONTACT;
    private String FORDHED_CUSADD1;
    private String FORDHED_CUSADD2;
    private String FORDHED_CUSADD3;
    private String FORDHED_CUSTELE;

    //    @SerializedName("OrderNo")
    private long orderId;

    @SerializedName("OrderState")
    private String FORDHED_STATUS;


    public String getFORDHED_CONTACT() {
        return FORDHED_CONTACT;
    }

    public void setFORDHED_CONTACT(String FORDHED_CONTACT) {
        this.FORDHED_CONTACT = FORDHED_CONTACT;
    }

    public String getFORDHED_CUSADD1() {
        return FORDHED_CUSADD1;
    }

    public void setFORDHED_CUSADD1(String FORDHED_CUSADD1) {
        this.FORDHED_CUSADD1 = FORDHED_CUSADD1;
    }

    public String getFORDHED_CUSADD2() {
        return FORDHED_CUSADD2;
    }

    public void setFORDHED_CUSADD2(String FORDHED_CUSADD2) {
        this.FORDHED_CUSADD2 = FORDHED_CUSADD2;
    }

    public String getFORDHED_CUSADD3() {
        return FORDHED_CUSADD3;
    }

    public void setFORDHED_CUSADD3(String FORDHED_CUSADD3) {
        this.FORDHED_CUSADD3 = FORDHED_CUSADD3;
    }

    public String getFORDHED_CUSTELE() {
        return FORDHED_CUSTELE;
    }

    public void setFORDHED_CUSTELE(String FORDHED_CUSTELE) {
        this.FORDHED_CUSTELE = FORDHED_CUSTELE;
    }

    public String getFORDHED_STATUS() {
        return FORDHED_STATUS;
    }

    public void setFORDHED_STATUS(String FORDHED_STATUS) {
        this.FORDHED_STATUS = FORDHED_STATUS;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    private ArrayList<OrderDetail> ordDet;
    private ArrayList<OrdFreeIssue> ordFreeIss;

    public ArrayList<OrderDetail> getOrdDet() {
        return ordDet;
    }

    public void setOrdDet(ArrayList<OrderDetail> ordDet) {
        this.ordDet = ordDet;
    }

    public ArrayList<OrdFreeIssue> getOrdFreeIss() {
        return ordFreeIss;
    }

    public void setOrdFreeIss(ArrayList<OrdFreeIssue> ordFreeIss) {
        this.ordFreeIss = ordFreeIss;
    }

    public String getFEEDBACK() {
        return FEEDBACK;
    }

    public void setFEEDBACK(String FEEDBACK) {
        this.FEEDBACK = FEEDBACK;
    }

    public String getFORDHED_AREA_CODE() {
        return FORDHED_AREA_CODE;
    }

    public void setFORDHED_AREA_CODE(String FORDHED_AREA_CODE) {
        this.FORDHED_AREA_CODE = FORDHED_AREA_CODE;
    }

    public String getFORDHED_ORDERID() {
        return FORDHED_ORDERID;
    }

    public void setFORDHED_ORDERID(String FORDHED_ORDERID) {
        this.FORDHED_ORDERID = FORDHED_ORDERID;
    }

    public String getDistDB() {
        return DistDB;
    }

    public void setDistDB(String distDB) {
        DistDB = distDB;
    }


    public String getFORDHED_ID() {
        return FORDHED_ID;
    }

    public void setFORDHED_ID(String FORDHED_ID) {
        this.FORDHED_ID = FORDHED_ID;
    }

    public String getFORDHED_REFNO() {
        return FORDHED_REFNO;
    }

    public void setFORDHED_REFNO(String FORDHED_REFNO) {
        this.FORDHED_REFNO = FORDHED_REFNO;
    }

    public String getFORDHED_ADD_DATE() {
        return FORDHED_ADD_DATE;
    }

    public void setFORDHED_ADD_DATE(String FORDHED_ADD_DATE) {
        this.FORDHED_ADD_DATE = FORDHED_ADD_DATE;
    }

    public String getFORDHED_ADD_MACH() {
        return FORDHED_ADD_MACH;
    }

    public void setFORDHED_ADD_MACH(String FORDHED_ADD_MACH) {
        this.FORDHED_ADD_MACH = FORDHED_ADD_MACH;
    }

    public String getFORDHED_ADD_USER() {
        return FORDHED_ADD_USER;
    }

    public void setFORDHED_ADD_USER(String FORDHED_ADD_USER) {
        this.FORDHED_ADD_USER = FORDHED_ADD_USER;
    }

    public String getFORDHED_APP_DATE() {
        return FORDHED_APP_DATE;
    }

    public void setFORDHED_APP_DATE(String FORDHED_APP_DATE) {
        this.FORDHED_APP_DATE = FORDHED_APP_DATE;
    }

    public String getFORDHED_APPSTS() {
        return FORDHED_APPSTS;
    }

    public void setFORDHED_APPSTS(String FORDHED_APPSTS) {
        this.FORDHED_APPSTS = FORDHED_APPSTS;
    }

    public String getFORDHED_APP_USER() {
        return FORDHED_APP_USER;
    }

    public void setFORDHED_APP_USER(String FORDHED_APP_USER) {
        this.FORDHED_APP_USER = FORDHED_APP_USER;
    }

    public String getFORDHED_BP_TOTAL_DIS() {
        return FORDHED_BP_TOTAL_DIS;
    }

    public void setFORDHED_BP_TOTAL_DIS(String FORDHED_BP_TOTAL_DIS) {
        this.FORDHED_BP_TOTAL_DIS = FORDHED_BP_TOTAL_DIS;
    }

    public String getFORDHED_B_TOTAL_AMT() {
        return FORDHED_B_TOTAL_AMT;
    }

    public void setFORDHED_B_TOTAL_AMT(String FORDHED_B_TOTAL_AMT) {
        this.FORDHED_B_TOTAL_AMT = FORDHED_B_TOTAL_AMT;
    }

    public String getFORDHED_B_TOTAL_DIS() {
        return FORDHED_B_TOTAL_DIS;
    }

    public void setFORDHED_B_TOTAL_DIS(String FORDHED_B_TOTAL_DIS) {
        this.FORDHED_B_TOTAL_DIS = FORDHED_B_TOTAL_DIS;
    }

    public String getFORDHED_B_TOTAL_TAX() {
        return FORDHED_B_TOTAL_TAX;
    }

    public void setFORDHED_B_TOTAL_TAX(String FORDHED_B_TOTAL_TAX) {
        this.FORDHED_B_TOTAL_TAX = FORDHED_B_TOTAL_TAX;
    }

    public String getFORDHED_COST_CODE() {
        return FORDHED_COST_CODE;
    }

    public void setFORDHED_COST_CODE(String FORDHED_COST_CODE) {
        this.FORDHED_COST_CODE = FORDHED_COST_CODE;
    }

    public String getFORDHED_CUR_CODE() {
        return FORDHED_CUR_CODE;
    }

    public void setFORDHED_CUR_CODE(String FORDHED_CUR_CODE) {
        this.FORDHED_CUR_CODE = FORDHED_CUR_CODE;
    }

    public String getFORDHED_CUR_RATE() {
        return FORDHED_CUR_RATE;
    }

    public void setFORDHED_CUR_RATE(String FORDHED_CUR_RATE) {
        this.FORDHED_CUR_RATE = FORDHED_CUR_RATE;
    }

    public String getFORDHED_DEB_CODE() {
        return FORDHED_DEB_CODE;
    }

    public void setFORDHED_DEB_CODE(String FORDHED_DEB_CODE) {
        this.FORDHED_DEB_CODE = FORDHED_DEB_CODE;
    }

    public String getFORDHED_DIS_PER() {
        return FORDHED_DIS_PER;
    }

    public void setFORDHED_DIS_PER(String FORDHED_DIS_PER) {
        this.FORDHED_DIS_PER = FORDHED_DIS_PER;
    }

    public String getFORDHED_START_TIME_SO() {
        return FORDHED_START_TIME_SO;
    }

    public void setFORDHED_START_TIME_SO(String FORDHED_START_TIME_SO) {
        this.FORDHED_START_TIME_SO = FORDHED_START_TIME_SO;
    }

    public String getFORDHED_END_TIME_SO() {
        return FORDHED_END_TIME_SO;
    }

    public void setFORDHED_END_TIME_SO(String FORDHED_END_TIME_SO) {
        this.FORDHED_END_TIME_SO = FORDHED_END_TIME_SO;
    }

    public String getFORDHED_LONGITUDE() {
        return FORDHED_LONGITUDE;
    }

    public void setFORDHED_LONGITUDE(String FORDHED_LONGITUDE) {
        this.FORDHED_LONGITUDE = FORDHED_LONGITUDE;
    }

    public String getFORDHED_LATITUDE() {
        return FORDHED_LATITUDE;
    }

    public void setFORDHED_LATITUDE(String FORDHED_LATITUDE) {
        this.FORDHED_LATITUDE = FORDHED_LATITUDE;
    }

    public String getFORDHED_LOC_CODE() {
        return FORDHED_LOC_CODE;
    }

    public void setFORDHED_LOC_CODE(String FORDHED_LOC_CODE) {
        this.FORDHED_LOC_CODE = FORDHED_LOC_CODE;
    }

    public String getFORDHED_MANU_REF() {
        return FORDHED_MANU_REF;
    }

    public void setFORDHED_MANU_REF(String FORDHED_MANU_REF) {
        this.FORDHED_MANU_REF = FORDHED_MANU_REF;
    }

    public String getFORDHED_RECORD_ID() {
        return FORDHED_RECORD_ID;
    }

    public void setFORDHED_RECORD_ID(String FORDHED_RECORD_ID) {
        this.FORDHED_RECORD_ID = FORDHED_RECORD_ID;
    }

    public String getFORDHED_REMARKS() {
        return FORDHED_REMARKS;
    }

    public void setFORDHED_REMARKS(String FORDHED_REMARKS) {
        this.FORDHED_REMARKS = FORDHED_REMARKS;
    }

    public String getFORDHED_REPCODE() {
        return FORDHED_REPCODE;
    }

    public void setFORDHED_REPCODE(String FORDHED_REPCODE) {
        this.FORDHED_REPCODE = FORDHED_REPCODE;
    }

    public String getFORDHED_TAX_REG() {
        return FORDHED_TAX_REG;
    }

    public void setFORDHED_TAX_REG(String FORDHED_TAX_REG) {
        this.FORDHED_TAX_REG = FORDHED_TAX_REG;
    }

    public String getFORDHED_TOTAL_AMT() {
        return FORDHED_TOTAL_AMT;
    }

    public void setFORDHED_TOTAL_AMT(String FORDHED_TOTAL_AMT) {
        this.FORDHED_TOTAL_AMT = FORDHED_TOTAL_AMT;
    }

    public String getFORDHED_TOTALDIS() {
        return FORDHED_TOTALDIS;
    }

    public void setFORDHED_TOTALDIS(String FORDHED_TOTALDIS) {
        this.FORDHED_TOTALDIS = FORDHED_TOTALDIS;
    }

    public String getFORDHED_TOTAL_TAX() {
        return FORDHED_TOTAL_TAX;
    }

    public void setFORDHED_TOTAL_TAX(String FORDHED_TOTAL_TAX) {
        this.FORDHED_TOTAL_TAX = FORDHED_TOTAL_TAX;
    }

    public String getFORDHED_TXN_TYPE() {
        return FORDHED_TXN_TYPE;
    }

    public void setFORDHED_TXN_TYPE(String FORDHED_TXN_TYPE) {
        this.FORDHED_TXN_TYPE = FORDHED_TXN_TYPE;
    }

    public String getFORDHED_TXN_DATE() {
        return FORDHED_TXN_DATE;
    }

    public void setFORDHED_TXN_DATE(String FORDHED_TXN_DATE) {
        this.FORDHED_TXN_DATE = FORDHED_TXN_DATE;
    }

    public String getFORDHED_ADDRESS() {
        return FORDHED_ADDRESS;
    }

    public void setFORDHED_ADDRESS(String FORDHED_ADDRESS) {
        this.FORDHED_ADDRESS = FORDHED_ADDRESS;
    }

    public String getFORDHED_TOTAL_ITM_DIS() {
        return FORDHED_TOTAL_ITM_DIS;
    }

    public void setFORDHED_TOTAL_ITM_DIS(String FORDHED_TOTAL_ITM_DIS) {
        this.FORDHED_TOTAL_ITM_DIS = FORDHED_TOTAL_ITM_DIS;
    }

    public String getFORDHED_TOT_MKR_AMT() {
        return FORDHED_TOT_MKR_AMT;
    }

    public void setFORDHED_TOT_MKR_AMT(String FORDHED_TOT_MKR_AMT) {
        this.FORDHED_TOT_MKR_AMT = FORDHED_TOT_MKR_AMT;
    }

    public String getFORDHED_IS_SYNCED() {
        return FORDHED_IS_SYNCED;
    }

    public void setFORDHED_IS_SYNCED(String FORDHED_IS_SYNCED) {
        this.FORDHED_IS_SYNCED = FORDHED_IS_SYNCED;
    }

    public String getFORDHED_IS_ACTIVE() {
        return FORDHED_IS_ACTIVE;
    }

    public void setFORDHED_IS_ACTIVE(String FORDHED_IS_ACTIVE) {
        this.FORDHED_IS_ACTIVE = FORDHED_IS_ACTIVE;
    }

    public String getFORDHED_DELV_DATE() {
        return FORDHED_DELV_DATE;
    }

    public void setFORDHED_DELV_DATE(String FORDHED_DELV_DATE) {
        this.FORDHED_DELV_DATE = FORDHED_DELV_DATE;
    }

    public String getFORDHED_ROUTE_CODE() {
        return FORDHED_ROUTE_CODE;
    }

    public void setFORDHED_ROUTE_CODE(String FORDHED_ROUTE_CODE) {
        this.FORDHED_ROUTE_CODE = FORDHED_ROUTE_CODE;
    }

    public String getFORDHED_HED_DIS_VAL() {
        return FORDHED_HED_DIS_VAL;
    }

    public void setFORDHED_HED_DIS_VAL(String FORDHED_HED_DIS_VAL) {
        this.FORDHED_HED_DIS_VAL = FORDHED_HED_DIS_VAL;
    }

    public String getFORDHED_HED_DIS_PER_VAL() {
        return FORDHED_HED_DIS_PER_VAL;
    }

    public void setFORDHED_HED_DIS_PER_VAL(String FORDHED_HED_DIS_PER_VAL) {
        this.FORDHED_HED_DIS_PER_VAL = FORDHED_HED_DIS_PER_VAL;
    }

    public String getFORDHED_PAYMENT_TYPE() {
        return FORDHED_PAYMENT_TYPE;
    }

    public void setFORDHED_PAYMENT_TYPE(String FORDHED_PAYMENT_TYPE) {
        this.FORDHED_PAYMENT_TYPE = FORDHED_PAYMENT_TYPE;
    }

    public String getFORDHED_UPLOAD_TIME() {
        return FORDHED_UPLOAD_TIME;
    }

    public void setFORDHED_UPLOAD_TIME(String FORDHED_UPLOAD_TIME) {
        this.FORDHED_UPLOAD_TIME = FORDHED_UPLOAD_TIME;
    }

    public String getNextNumVal() {
        return NextNumVal;
    }

    public void setNextNumVal(String nextNumVal) {
        NextNumVal = nextNumVal;
    }

    public static Order parseOrderStatus(JSONObject instance) throws JSONException {

        if (instance != null) {
            Order order = new Order();
            order.setFORDHED_REFNO(instance.getString("refNo").trim());
            order.setFORDHED_STATUS(instance.getString("orderStatus").trim());

            return order;
        }

        return null;
    }


    public String getConsoleDB() {
        return ConsoleDB;
    }

    public void setConsoleDB(String consoleDB) {
        ConsoleDB = consoleDB;
    }

}

