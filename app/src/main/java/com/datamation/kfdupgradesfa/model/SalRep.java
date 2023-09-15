package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/*
    create by kaveesha - 2022-11-16
 */

public class SalRep {

    @SerializedName("repCode")
    private String FREP_CODE;
    @SerializedName("repName")
    private String FREP_NAME;
    @SerializedName("costCode")
    private String FREP_COSTCODE;
    @SerializedName("dealCode")
    private String FREP_DEALCODE;
    @SerializedName("pdapassword")
    private String FREP_PASSWORD;
    @SerializedName("repEmail")
    private String FREP_EMAIL;
    @SerializedName("repIdNo")
    private String FREP_REPIDNO;
    @SerializedName("repMobil")
    private String FREP_MOB;
    @SerializedName("repPreFix")
    private String FREP_PREFIX;
    @SerializedName("repTele")
    private String FREP_TELE;
    @SerializedName("status")
    private String FREP_STATUS;
    @SerializedName("macid")
    private String FREP_MACID;

    public String getFREP_CODE() {
        return FREP_CODE;
    }

    public void setFREP_CODE(String FREP_CODE) {
        this.FREP_CODE = FREP_CODE;
    }

    public String getFREP_NAME() {
        return FREP_NAME;
    }

    public void setFREP_NAME(String FREP_NAME) {
        this.FREP_NAME = FREP_NAME;
    }

    public String getFREP_COSTCODE() {
        return FREP_COSTCODE;
    }

    public void setFREP_COSTCODE(String FREP_COSTCODE) {
        this.FREP_COSTCODE = FREP_COSTCODE;
    }

    public String getFREP_DEALCODE() {
        return FREP_DEALCODE;
    }

    public void setFREP_DEALCODE(String FREP_DEALCODE) {
        this.FREP_DEALCODE = FREP_DEALCODE;
    }

    public String getFREP_PASSWORD() {
        return FREP_PASSWORD;
    }

    public void setFREP_PASSWORD(String FREP_PASSWORD) {
        this.FREP_PASSWORD = FREP_PASSWORD;
    }

    public String getFREP_EMAIL() {
        return FREP_EMAIL;
    }

    public void setFREP_EMAIL(String FREP_EMAIL) {
        this.FREP_EMAIL = FREP_EMAIL;
    }

    public String getFREP_REPIDNO() {
        return FREP_REPIDNO;
    }

    public void setFREP_REPIDNO(String FREP_REPIDNO) {
        this.FREP_REPIDNO = FREP_REPIDNO;
    }

    public String getFREP_MOB() {
        return FREP_MOB;
    }

    public void setFREP_MOB(String FREP_MOB) {
        this.FREP_MOB = FREP_MOB;
    }

    public String getFREP_PREFIX() {
        return FREP_PREFIX;
    }

    public void setFREP_PREFIX(String FREP_PREFIX) {
        this.FREP_PREFIX = FREP_PREFIX;
    }

    public String getFREP_TELE() {
        return FREP_TELE;
    }

    public void setFREP_TELE(String FREP_TELE) {
        this.FREP_TELE = FREP_TELE;
    }

    public String getFREP_STATUS() {
        return FREP_STATUS;
    }

    public void setFREP_STATUS(String FREP_STATUS) {
        this.FREP_STATUS = FREP_STATUS;
    }

    public String getFREP_MACID() {
        return FREP_MACID;
    }

    public void setFREP_MACID(String FREP_MACID) {
        this.FREP_MACID = FREP_MACID;
    }

    public static SalRep parseSalRep(JSONObject instance) throws JSONException{

        if(instance != null)
        {
            SalRep salRep = new SalRep();

            salRep.setFREP_CODE(instance.getString("repCode").trim());
            salRep.setFREP_NAME(instance.getString("repName").trim());
            salRep.setFREP_COSTCODE(instance.getString("costCode").trim());
            salRep.setFREP_DEALCODE(instance.getString("dealCode").trim());
            salRep.setFREP_PASSWORD(instance.getString("pdapassword").trim());
            salRep.setFREP_EMAIL(instance.getString("repEmail").trim());
            salRep.setFREP_REPIDNO(instance.getString("repIdNo").trim());
            salRep.setFREP_MOB(instance.getString("repMobil").trim());
            salRep.setFREP_PREFIX(instance.getString("repPreFix").trim());
            salRep.setFREP_TELE(instance.getString("repTele").trim());
            salRep.setFREP_STATUS(instance.getString("status").trim());
            salRep.setFREP_MACID(instance.getString("macid").trim());

            return salRep;
        }
        return null;
    }
}
