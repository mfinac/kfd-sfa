package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class RepTrgHed {

    @SerializedName("RefNo")
    private String FREPTRGHED_REFNO;
    @SerializedName("RepCode")
    private String FREPTRGHED_REPCODE;
    @SerializedName("Txndate")
    private String FREPTRGHED_TXNDATE;
    @SerializedName("YearT")
    private String FREPTRGHED_YEART;


    public String getFREPTRGHED_REFNO() {
        return FREPTRGHED_REFNO;
    }

    public void setFREPTRGHED_REFNO(String FREPTRGHED_REFNO) {
        this.FREPTRGHED_REFNO = FREPTRGHED_REFNO;
    }

    public String getFREPTRGHED_REPCODE() {
        return FREPTRGHED_REPCODE;
    }

    public void setFREPTRGHED_REPCODE(String FREPTRGHED_REPCODE) {
        this.FREPTRGHED_REPCODE = FREPTRGHED_REPCODE;
    }

    public String getFREPTRGHED_TXNDATE() {
        return FREPTRGHED_TXNDATE;
    }

    public void setFREPTRGHED_TXNDATE(String FREPTRGHED_TXNDATE) {
        this.FREPTRGHED_TXNDATE = FREPTRGHED_TXNDATE;
    }

    public String getFREPTRGHED_YEART() {
        return FREPTRGHED_YEART;
    }

    public void setFREPTRGHED_YEART(String FREPTRGHED_YEART) {
        this.FREPTRGHED_YEART = FREPTRGHED_YEART;
    }


    public static RepTrgHed parseRepTrgHed(JSONObject instance) throws JSONException {

        if (instance != null) {
            RepTrgHed repTrgHed = new RepTrgHed();
            repTrgHed.setFREPTRGHED_REFNO(instance.getString("RefNo"));
            repTrgHed.setFREPTRGHED_REPCODE(instance.getString("RepCode"));
            repTrgHed.setFREPTRGHED_TXNDATE(instance.getString("Txndate"));
            repTrgHed.setFREPTRGHED_YEART(instance.getString("YearT"));

            return repTrgHed;
        }

        return null;
    }
}
