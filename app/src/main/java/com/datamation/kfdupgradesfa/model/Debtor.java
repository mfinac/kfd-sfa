package com.datamation.kfdupgradesfa.model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Debtor implements Serializable {

    @SerializedName("AreaCode")
    private String FDEBTOR_AREA_CODE;//1
    @SerializedName("ChkCrdLmt")
    private String FDEBTOR_CHK_CRD_LIMIT;//2
    @SerializedName("ChkCrdPrd")
    private String FDEBTOR_CHK_CRD_PERIOD;//
    @SerializedName("ChkFree")
    private String FDEBTOR_CHK_FREE;
    @SerializedName("ChkMustFre")
    private String FDEBTOR_CHK_MUST;
    @SerializedName("CrdLimit")
    private String FDEBTOR_CRD_LIMIT;//4
    @SerializedName("CrdPeriod")
    private String FDEBTOR_CRD_PERIOD;//5
    @SerializedName("DbGrCode")
    private String FDEBTOR_DBGR_CODE;//8
    @SerializedName("DebAdd1")
    private String FDEBTOR_ADD1;//9
    @SerializedName("DebAdd2")
    private String FDEBTOR_ADD2;//10
    @SerializedName("DebAdd3")
    private String FDEBTOR_ADD3;//11
    @SerializedName("DebCode")
    private String FDEBTOR_CODE;//12
    @SerializedName("DebEMail")
    private String FDEBTOR_EMAIL;//13
    @SerializedName("DebMob")
    private String FDEBTOR_MOB;//14
    @SerializedName("DebName")
    private String FDEBTOR_NAME;//15
    @SerializedName("DebTele")
    private String FDEBTOR_TELE;//16
    @SerializedName("PrilCode")
    private String FDEBTOR_PRILLCODE;//17
    @SerializedName("RankCode")
    private String FDEBTOR_RANK_CODE;//18
    @SerializedName("RepCode")
    private String FDEBTOR_REPCODE;//19
    @SerializedName("RouteCode")
    private String FDEBTOR_ROUTE_CODE;//20
    @SerializedName("Status")
    private String FDEBTOR_STATUS;//21
    @SerializedName("TaxReg")
    private String FDEBTOR_TAX_REG;//22
    @SerializedName("TownCode")
    private String FDEBTOR_TOWNCODE;//23
    @SerializedName("MultiRflg")
    private String FDEBTOR_MULTIFLG;
    @SerializedName("Latitude")
    private String FDEBTOR_LATITUDE;
    @SerializedName("Longitude")
    private String FDEBTOR_LONGITUDE;

    private String FDEBTOR_IS_SYNC;
    private String FDEBTOR_OTP;

    //for upload
    private String KFDDB;
    private String LOGIN_DEBCODE;
    private String LOGIN_PASSWORD;

    public String getFDEBTOR_LATITUDE() {
        return FDEBTOR_LATITUDE;
    }

    public void setFDEBTOR_LATITUDE(String FDEBTOR_LATITUDE) {
        this.FDEBTOR_LATITUDE = FDEBTOR_LATITUDE;
    }

    public String getFDEBTOR_LONGITUDE() {
        return FDEBTOR_LONGITUDE;
    }

    public void setFDEBTOR_LONGITUDE(String FDEBTOR_LONGITUDE) {
        this.FDEBTOR_LONGITUDE = FDEBTOR_LONGITUDE;
    }

    public String getFDEBTOR_IS_SYNC() {
        return FDEBTOR_IS_SYNC;
    }

    public void setFDEBTOR_IS_SYNC(String FDEBTOR_IS_SYNC) {
        this.FDEBTOR_IS_SYNC = FDEBTOR_IS_SYNC;
    }

    public String getFDEBTOR_OTP() {
        return FDEBTOR_OTP;
    }

    public void setFDEBTOR_OTP(String FDEBTOR_OTP) {
        this.FDEBTOR_OTP = FDEBTOR_OTP;
    }

    public String getKFDDB() {
        return KFDDB;
    }

    public void setKFDDB(String KFDDB) {
        this.KFDDB = KFDDB;
    }

    public String getLOGIN_DEBCODE() {
        return LOGIN_DEBCODE;
    }

    public void setLOGIN_DEBCODE(String LOGIN_DEBCODE) {
        this.LOGIN_DEBCODE = LOGIN_DEBCODE;
    }

    public String getLOGIN_PASSWORD() {
        return LOGIN_PASSWORD;
    }

    public void setLOGIN_PASSWORD(String LOGIN_PASSWORD) {
        this.LOGIN_PASSWORD = LOGIN_PASSWORD;
    }

    public String getFDEBTOR_AREA_CODE() {
        return FDEBTOR_AREA_CODE;
    }

    public void setFDEBTOR_AREA_CODE(String FDEBTOR_AREA_CODE) {
        this.FDEBTOR_AREA_CODE = FDEBTOR_AREA_CODE;
    }

    public String getFDEBTOR_CHK_CRD_LIMIT() {
        return FDEBTOR_CHK_CRD_LIMIT;
    }

    public void setFDEBTOR_CHK_CRD_LIMIT(String FDEBTOR_CHK_CRD_LIMIT) {
        this.FDEBTOR_CHK_CRD_LIMIT = FDEBTOR_CHK_CRD_LIMIT;
    }

    public String getFDEBTOR_CHK_CRD_PERIOD() {
        return FDEBTOR_CHK_CRD_PERIOD;
    }

    public void setFDEBTOR_CHK_CRD_PERIOD(String FDEBTOR_CHK_CRD_PERIOD) {
        this.FDEBTOR_CHK_CRD_PERIOD = FDEBTOR_CHK_CRD_PERIOD;
    }

    public String getFDEBTOR_CHK_FREE() {
        return FDEBTOR_CHK_FREE;
    }

    public void setFDEBTOR_CHK_FREE(String FDEBTOR_CHK_FREE) {
        this.FDEBTOR_CHK_FREE = FDEBTOR_CHK_FREE;
    }

    public String getFDEBTOR_CHK_MUST() {
        return FDEBTOR_CHK_MUST;
    }

    public void setFDEBTOR_CHK_MUST(String FDEBTOR_CHK_MUST) {
        this.FDEBTOR_CHK_MUST = FDEBTOR_CHK_MUST;
    }

    public String getFDEBTOR_CRD_LIMIT() {
        return FDEBTOR_CRD_LIMIT;
    }

    public void setFDEBTOR_CRD_LIMIT(String FDEBTOR_CRD_LIMIT) {
        this.FDEBTOR_CRD_LIMIT = FDEBTOR_CRD_LIMIT;
    }

    public String getFDEBTOR_CRD_PERIOD() {
        return FDEBTOR_CRD_PERIOD;
    }

    public void setFDEBTOR_CRD_PERIOD(String FDEBTOR_CRD_PERIOD) {
        this.FDEBTOR_CRD_PERIOD = FDEBTOR_CRD_PERIOD;
    }

    public String getFDEBTOR_DBGR_CODE() {
        return FDEBTOR_DBGR_CODE;
    }

    public void setFDEBTOR_DBGR_CODE(String FDEBTOR_DBGR_CODE) {
        this.FDEBTOR_DBGR_CODE = FDEBTOR_DBGR_CODE;
    }

    public String getFDEBTOR_ADD1() {
        return FDEBTOR_ADD1;
    }

    public void setFDEBTOR_ADD1(String FDEBTOR_ADD1) {
        this.FDEBTOR_ADD1 = FDEBTOR_ADD1;
    }

    public String getFDEBTOR_ADD2() {
        return FDEBTOR_ADD2;
    }

    public void setFDEBTOR_ADD2(String FDEBTOR_ADD2) {
        this.FDEBTOR_ADD2 = FDEBTOR_ADD2;
    }

    public String getFDEBTOR_ADD3() {
        return FDEBTOR_ADD3;
    }

    public void setFDEBTOR_ADD3(String FDEBTOR_ADD3) {
        this.FDEBTOR_ADD3 = FDEBTOR_ADD3;
    }

    public String getFDEBTOR_CODE() {
        return FDEBTOR_CODE;
    }

    public void setFDEBTOR_CODE(String FDEBTOR_CODE) {
        this.FDEBTOR_CODE = FDEBTOR_CODE;
    }

    public String getFDEBTOR_EMAIL() {
        return FDEBTOR_EMAIL;
    }

    public void setFDEBTOR_EMAIL(String FDEBTOR_EMAIL) {
        this.FDEBTOR_EMAIL = FDEBTOR_EMAIL;
    }

    public String getFDEBTOR_MOB() {
        return FDEBTOR_MOB;
    }

    public void setFDEBTOR_MOB(String FDEBTOR_MOB) {
        this.FDEBTOR_MOB = FDEBTOR_MOB;
    }

    public String getFDEBTOR_NAME() {
        return FDEBTOR_NAME;
    }

    public void setFDEBTOR_NAME(String FDEBTOR_NAME) {
        this.FDEBTOR_NAME = FDEBTOR_NAME;
    }

    public String getFDEBTOR_TELE() {
        return FDEBTOR_TELE;
    }

    public void setFDEBTOR_TELE(String FDEBTOR_TELE) {
        this.FDEBTOR_TELE = FDEBTOR_TELE;
    }

    public String getFDEBTOR_PRILLCODE() {
        return FDEBTOR_PRILLCODE;
    }

    public void setFDEBTOR_PRILLCODE(String FDEBTOR_PRILLCODE) {
        this.FDEBTOR_PRILLCODE = FDEBTOR_PRILLCODE;
    }

    public String getFDEBTOR_RANK_CODE() {
        return FDEBTOR_RANK_CODE;
    }

    public void setFDEBTOR_RANK_CODE(String FDEBTOR_RANK_CODE) {
        this.FDEBTOR_RANK_CODE = FDEBTOR_RANK_CODE;
    }

    public String getFDEBTOR_REPCODE() {
        return FDEBTOR_REPCODE;
    }

    public void setFDEBTOR_REPCODE(String FDEBTOR_REPCODE) {
        this.FDEBTOR_REPCODE = FDEBTOR_REPCODE;
    }

    public String getFDEBTOR_ROUTE_CODE() {
        return FDEBTOR_ROUTE_CODE;
    }

    public void setFDEBTOR_ROUTE_CODE(String FDEBTOR_ROUTE_CODE) {
        this.FDEBTOR_ROUTE_CODE = FDEBTOR_ROUTE_CODE;
    }

    public String getFDEBTOR_STATUS() {
        return FDEBTOR_STATUS;
    }

    public void setFDEBTOR_STATUS(String FDEBTOR_STATUS) {
        this.FDEBTOR_STATUS = FDEBTOR_STATUS;
    }

    public String getFDEBTOR_TAX_REG() {
        return FDEBTOR_TAX_REG;
    }

    public void setFDEBTOR_TAX_REG(String FDEBTOR_TAX_REG) {
        this.FDEBTOR_TAX_REG = FDEBTOR_TAX_REG;
    }

    public String getFDEBTOR_TOWNCODE() {
        return FDEBTOR_TOWNCODE;
    }

    public void setFDEBTOR_TOWNCODE(String FDEBTOR_TOWNCODE) {
        this.FDEBTOR_TOWNCODE = FDEBTOR_TOWNCODE;
    }

    public String getFDEBTOR_MULTIFLG() {
        return FDEBTOR_MULTIFLG;
    }

    public void setFDEBTOR_MULTIFLG(String FDEBTOR_MULTIFLG) {
        this.FDEBTOR_MULTIFLG = FDEBTOR_MULTIFLG;
    }

    public static Debtor parseOutlet(JSONObject instance) throws JSONException {

        if (instance != null) {
            Debtor aDebtor = new Debtor();

            aDebtor.setFDEBTOR_ADD1(instance.getString("debAdd1").trim());
            aDebtor.setFDEBTOR_ADD2(instance.getString("debAdd2").trim());
            aDebtor.setFDEBTOR_ADD3(instance.getString("debAdd3").trim());
            aDebtor.setFDEBTOR_AREA_CODE(instance.getString("areaCode").trim());
            aDebtor.setFDEBTOR_CODE(instance.getString("debCode").trim());
            aDebtor.setFDEBTOR_CRD_LIMIT(instance.getString("crdLimit").trim());
            aDebtor.setFDEBTOR_CRD_PERIOD(instance.getString("crdPeriod").trim());
            aDebtor.setFDEBTOR_CHK_FREE(instance.getString("chkFree").trim());
            aDebtor.setFDEBTOR_CHK_MUST(instance.getString("chkMustFre").trim());
            aDebtor.setFDEBTOR_CHK_CRD_LIMIT(instance.getString("chkCrdLmt").trim());
            aDebtor.setFDEBTOR_CHK_CRD_PERIOD(instance.getString("chkCrdPrd").trim());
            aDebtor.setFDEBTOR_DBGR_CODE(instance.getString("dbGrCode").trim());
            aDebtor.setFDEBTOR_EMAIL(instance.getString("debEmail").trim());
            aDebtor.setFDEBTOR_MOB(instance.getString("debMob").trim());
            aDebtor.setFDEBTOR_NAME(instance.getString("debName").trim());
            aDebtor.setFDEBTOR_PRILLCODE(instance.getString("prilCode").trim());
            aDebtor.setFDEBTOR_RANK_CODE(instance.getString("rankCode").trim());
            aDebtor.setFDEBTOR_STATUS(instance.getString("status").trim());
            aDebtor.setFDEBTOR_TAX_REG(instance.getString("taxReg").trim());
            aDebtor.setFDEBTOR_TELE(instance.getString("debTele").trim());
            aDebtor.setFDEBTOR_REPCODE(instance.getString("repCode").trim());
            aDebtor.setFDEBTOR_MULTIFLG(instance.getString("multiRflg").trim());
            aDebtor.setFDEBTOR_TOWNCODE(instance.getString("townCode").trim());
            aDebtor.setFDEBTOR_ROUTE_CODE(instance.getString("routeCode").trim());


            return aDebtor;
        }

        return null;
    }

}
