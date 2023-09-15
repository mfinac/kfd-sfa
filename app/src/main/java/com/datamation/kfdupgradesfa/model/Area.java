package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Area {

    @SerializedName("areaCode")
    private String FAREA_CODE;
    @SerializedName("AreaName")
    private String FAREA_NAME;
    @SerializedName("DealCode")
    private String FAREA_DEAL_CODE;
    @SerializedName("RegCode")
    private String FAREA_REG_CODE;

    public String getFAREA_CODE() {
        return FAREA_CODE;
    }

    public void setFAREA_CODE(String FAREA_CODE) {
        this.FAREA_CODE = FAREA_CODE;
    }

    public String getFAREA_NAME() {
        return FAREA_NAME;
    }

    public void setFAREA_NAME(String FAREA_NAME) {
        this.FAREA_NAME = FAREA_NAME;
    }

    public String getFAREA_DEAL_CODE() {
        return FAREA_DEAL_CODE;
    }

    public void setFAREA_DEAL_CODE(String FAREA_DEAL_CODE) {
        this.FAREA_DEAL_CODE = FAREA_DEAL_CODE;
    }

    public String getFAREA_REG_CODE() {
        return FAREA_REG_CODE;
    }

    public void setFAREA_REG_CODE(String FAREA_REG_CODE) {
        this.FAREA_REG_CODE = FAREA_REG_CODE;
    }

    public static Area parseArea(JSONObject instance) throws JSONException
    {
        if(instance != null)
        {
            Area area = new Area();

            area.setFAREA_CODE(instance.getString("areaCode").trim());
            area.setFAREA_NAME(instance.getString("areaName").trim());
//            area.setFAREA_DEAL_CODE(instance.getString("DealCode"));
//            area.setFAREA_REG_CODE(instance.getString("RegCode"));

            return area;

        }

        return  null;
    }

}
