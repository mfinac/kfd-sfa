package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class RepTrgDet {

    @SerializedName("RefNo")
    private String FREPTRGDET_REFNO;
    @SerializedName("RepCode")
    private String FREPTRGDET_REPCODE;
    @SerializedName("Txndate")
    private String FREPTRGDET_TXNDATE;
    @SerializedName("Monthn")
    private String FREPTRGDET_MONTHN;
    @SerializedName("YearT")
    private String FREPTRGDET_YEART;
    @SerializedName("TrAmt")
    private String FREPTRGDET_TRAMT;


    public String getFREPTRGDET_REFNO() {
        return FREPTRGDET_REFNO;
    }

    public void setFREPTRGDET_REFNO(String FREPTRGDET_REFNO) {
        this.FREPTRGDET_REFNO = FREPTRGDET_REFNO;
    }

    public String getFREPTRGDET_REPCODE() {
        return FREPTRGDET_REPCODE;
    }

    public void setFREPTRGDET_REPCODE(String FREPTRGDET_REPCODE) {
        this.FREPTRGDET_REPCODE = FREPTRGDET_REPCODE;
    }

    public String getFREPTRGDET_TXNDATE() {
        return FREPTRGDET_TXNDATE;
    }

    public void setFREPTRGDET_TXNDATE(String FREPTRGDET_TXNDATE) {
        this.FREPTRGDET_TXNDATE = FREPTRGDET_TXNDATE;
    }

    public String getFREPTRGDET_MONTHN() {
        return FREPTRGDET_MONTHN;
    }

    public void setFREPTRGDET_MONTHN(String FREPTRGDET_MONTHN) {
        this.FREPTRGDET_MONTHN = FREPTRGDET_MONTHN;
    }

    public String getFREPTRGDET_YEART() {
        return FREPTRGDET_YEART;
    }

    public void setFREPTRGDET_YEART(String FREPTRGDET_YEART) {
        this.FREPTRGDET_YEART = FREPTRGDET_YEART;
    }

    public String getFREPTRGDET_TRAMT() {
        return FREPTRGDET_TRAMT;
    }

    public void setFREPTRGDET_TRAMT(String FREPTRGDET_TRAMT) {
        this.FREPTRGDET_TRAMT = FREPTRGDET_TRAMT;
    }

    public static RepTrgDet parseRepTrgDet(JSONObject instance) throws JSONException {

        if (instance != null) {
            RepTrgDet repTrgHed = new RepTrgDet();
            repTrgHed.setFREPTRGDET_REFNO(instance.getString("RefNo"));
            repTrgHed.setFREPTRGDET_REPCODE(instance.getString("RepCode"));
            repTrgHed.setFREPTRGDET_TXNDATE(instance.getString("Txndate"));
            repTrgHed.setFREPTRGDET_YEART(instance.getString("YearT"));
            repTrgHed.setFREPTRGDET_MONTHN(instance.getString("Monthn"));
            repTrgHed.setFREPTRGDET_TRAMT(instance.getString("TrAmt"));

            return repTrgHed;
        }

        return null;
    }
}
