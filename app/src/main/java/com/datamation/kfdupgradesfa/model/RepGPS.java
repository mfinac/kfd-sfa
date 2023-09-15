package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class RepGPS {

    private String FREP_CODE;
    private String FTXN_DATE;
    private String FTXN_TIME;
    private String FLONGITUDE;
    private String FLATITUDE;
    private String FIS_SYNC;
    private String ConsoleDB;
    private String DistDB;
    private Integer BATTERY_LVL;

    public String getFREP_CODE() {
        return FREP_CODE;
    }

    public void setFREP_CODE(String FREP_CODE) {
        this.FREP_CODE = FREP_CODE;
    }

    public String getFTXN_DATE() {
        return FTXN_DATE;
    }

    public void setFTXN_DATE(String FTXN_DATE) {
        this.FTXN_DATE = FTXN_DATE;
    }

    public String getFTXN_TIME() {
        return FTXN_TIME;
    }

    public void setFTXN_TIME(String FTXN_TIME) {
        this.FTXN_TIME = FTXN_TIME;
    }

    public String getFLONGITUDE() {
        return FLONGITUDE;
    }

    public void setFLONGITUDE(String FLONGITUDE) {
        this.FLONGITUDE = FLONGITUDE;
    }

    public String getFLATITUDE() {
        return FLATITUDE;
    }

    public void setFLATITUDE(String FLATITUDE) {
        this.FLATITUDE = FLATITUDE;
    }

    public String getFIS_SYNC() {
        return FIS_SYNC;
    }

    public void setFIS_SYNC(String FIS_SYNC) {
        this.FIS_SYNC = FIS_SYNC;
    }

    public String getConsoleDB() {
        return ConsoleDB;
    }

    public void setConsoleDB(String consoleDB) {
        ConsoleDB = consoleDB;
    }

    public String getDistDB() {
        return DistDB;
    }

    public void setDistDB(String distDB) {
        DistDB = distDB;
    }

    public Integer getBATTERY_LVL() {
        return BATTERY_LVL;
    }

    public void setBATTERY_LVL(Integer BATTERY_LVL) {
        this.BATTERY_LVL = BATTERY_LVL;
    }
}
