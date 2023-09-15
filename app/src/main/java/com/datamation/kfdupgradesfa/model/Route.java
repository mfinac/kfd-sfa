package com.datamation.kfdupgradesfa.model;


import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Route {


    @SerializedName("AreaCode")
    private String FROUTE_AREACODE;
    @SerializedName("DealCode")
    private String FROUTE_DEALCODE;
    @SerializedName("FreqNo")
    private String FROUTE_FREQNO;
    @SerializedName("Km")
    private String FROUTE_KM;
    @SerializedName("MinProcall")
    private String FROUTE_MINPROCALL;
    @SerializedName("RDAloRate")
    private String FROUTE_RDALORATE;
    @SerializedName("Remarks")
    private String FROUTE_REMARKS;
    @SerializedName("RepCode")
    private String FROUTE_REPCODE;
    @SerializedName("RouteCode")
    private String FROUTE_ROUTECODE;
    @SerializedName("RouteName")
    private String FROUTE_ROUTE_NAME;
    @SerializedName("Status")
    private String FROUTE_STATUS;


    public String getFROUTE_AREACODE() {
        return FROUTE_AREACODE;
    }

    public void setFROUTE_AREACODE(String FROUTE_AREACODE) {
        this.FROUTE_AREACODE = FROUTE_AREACODE;
    }

    public String getFROUTE_DEALCODE() {
        return FROUTE_DEALCODE;
    }

    public void setFROUTE_DEALCODE(String FROUTE_DEALCODE) {
        this.FROUTE_DEALCODE = FROUTE_DEALCODE;
    }

    public String getFROUTE_FREQNO() {
        return FROUTE_FREQNO;
    }

    public void setFROUTE_FREQNO(String FROUTE_FREQNO) {
        this.FROUTE_FREQNO = FROUTE_FREQNO;
    }

    public String getFROUTE_KM() {
        return FROUTE_KM;
    }

    public void setFROUTE_KM(String FROUTE_KM) {
        this.FROUTE_KM = FROUTE_KM;
    }

    public String getFROUTE_MINPROCALL() {
        return FROUTE_MINPROCALL;
    }

    public void setFROUTE_MINPROCALL(String FROUTE_MINPROCALL) {
        this.FROUTE_MINPROCALL = FROUTE_MINPROCALL;
    }

    public String getFROUTE_RDALORATE() {
        return FROUTE_RDALORATE;
    }

    public void setFROUTE_RDALORATE(String FROUTE_RDALORATE) {
        this.FROUTE_RDALORATE = FROUTE_RDALORATE;
    }

    public String getFROUTE_REMARKS() {
        return FROUTE_REMARKS;
    }

    public void setFROUTE_REMARKS(String FROUTE_REMARKS) {
        this.FROUTE_REMARKS = FROUTE_REMARKS;
    }

    public String getFROUTE_REPCODE() {
        return FROUTE_REPCODE;
    }

    public void setFROUTE_REPCODE(String FROUTE_REPCODE) {
        this.FROUTE_REPCODE = FROUTE_REPCODE;
    }

    public String getFROUTE_ROUTECODE() {
        return FROUTE_ROUTECODE;
    }

    public void setFROUTE_ROUTECODE(String FROUTE_ROUTECODE) {
        this.FROUTE_ROUTECODE = FROUTE_ROUTECODE;
    }

    public String getFROUTE_ROUTE_NAME() {
        return FROUTE_ROUTE_NAME;
    }

    public void setFROUTE_ROUTE_NAME(String FROUTE_ROUTE_NAME) {
        this.FROUTE_ROUTE_NAME = FROUTE_ROUTE_NAME;
    }

    public String getFROUTE_STATUS() {
        return FROUTE_STATUS;
    }

    public void setFROUTE_STATUS(String FROUTE_STATUS) {
        this.FROUTE_STATUS = FROUTE_STATUS;
    }

    public static Route parseRoute(JSONObject instance) throws JSONException {

        if (instance != null) {
            Route route = new Route();

            route.setFROUTE_AREACODE(instance.getString("areaCode").trim());
            route.setFROUTE_DEALCODE(instance.getString("dealCode").trim());
            route.setFROUTE_FREQNO(instance.getString("freqNo").trim());
            route.setFROUTE_KM(instance.getString("km").trim());
            route.setFROUTE_MINPROCALL(instance.getString("minProcall").trim());
            route.setFROUTE_RDALORATE(instance.getString("rdaloRate").trim());
            route.setFROUTE_REMARKS(instance.getString("remarks").trim());
            route.setFROUTE_REPCODE(instance.getString("repCode").trim());
            route.setFROUTE_ROUTECODE(instance.getString("routeCode").trim());
            route.setFROUTE_ROUTE_NAME(instance.getString("routeName").trim());
            route.setFROUTE_STATUS(instance.getString("status").trim());

            return route;
        }

        return null;
    }
}
