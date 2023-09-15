package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Town {

    @SerializedName("townCode")
    private String FTOWN_CODE;
    @SerializedName("townName")
    private String FTOWN_NAME;
    @SerializedName("distrCode")
    private String FTOWN_DISTR_CODE;

    public String getFTOWN_CODE() {
        return FTOWN_CODE;
    }

    public void setFTOWN_CODE(String FTOWN_CODE) {
        this.FTOWN_CODE = FTOWN_CODE;
    }

    public String getFTOWN_NAME() {
        return FTOWN_NAME;
    }

    public void setFTOWN_NAME(String FTOWN_NAME) {
        this.FTOWN_NAME = FTOWN_NAME;
    }

    public String getFTOWN_DISTR_CODE() {
        return FTOWN_DISTR_CODE;
    }

    public void setFTOWN_DISTR_CODE(String FTOWN_DISTR_CODE) {
        this.FTOWN_DISTR_CODE = FTOWN_DISTR_CODE;
    }

    public static Town parseTown(JSONObject instance) throws JSONException {

        if (instance != null) {
            Town town = new Town();

            town.setFTOWN_CODE(instance.getString("townCode").trim());
            town.setFTOWN_NAME(instance.getString("townName").trim());
            town.setFTOWN_DISTR_CODE(instance.getString("distrCode").trim());
            return town;
        }
        return null;
    }
}
