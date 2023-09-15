package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Locations {

    @SerializedName("locCode")
    private String FLOCATIONS_LOC_CODE;
    @SerializedName("locName")
    private String FLOCATIONS_LOC_NAME;
    @SerializedName("loctCode")
    private String FLOCATIONS_LOC_T_CODE;
    @SerializedName("RepCode")
    private String FLOCATIONS_REP_CODE;
    @SerializedName("costCode")
    private String FLOCATIONS_COST_CODE;

    public String getFLOCATIONS_COST_CODE() {
        return FLOCATIONS_COST_CODE;
    }

    public void setFLOCATIONS_COST_CODE(String FLOCATION_COST_CODE) {
        this.FLOCATIONS_COST_CODE = FLOCATION_COST_CODE;
    }

    public String getFLOCATIONS_LOC_CODE() {
        return FLOCATIONS_LOC_CODE;
    }

    public void setFLOCATIONS_LOC_CODE(String FLOCATIONS_LOC_CODE) {
        this.FLOCATIONS_LOC_CODE = FLOCATIONS_LOC_CODE;
    }

    public String getFLOCATIONS_LOC_NAME() {
        return FLOCATIONS_LOC_NAME;
    }

    public void setFLOCATIONS_LOC_NAME(String FLOCATIONS_LOC_NAME) {
        this.FLOCATIONS_LOC_NAME = FLOCATIONS_LOC_NAME;
    }

    public String getFLOCATIONS_LOC_T_CODE() {
        return FLOCATIONS_LOC_T_CODE;
    }

    public void setFLOCATIONS_LOC_T_CODE(String FLOCATIONS_LOC_T_CODE) {
        this.FLOCATIONS_LOC_T_CODE = FLOCATIONS_LOC_T_CODE;
    }

    public String getFLOCATIONS_REP_CODE() {
        return FLOCATIONS_REP_CODE;
    }

    public void setFLOCATIONS_REP_CODE(String FLOCATIONS_REP_CODE) {
        this.FLOCATIONS_REP_CODE = FLOCATIONS_REP_CODE;
    }

    public static Locations parseLocs(JSONObject instance) throws JSONException {

        if (instance != null) {
            Locations locations = new Locations();

            locations.setFLOCATIONS_LOC_CODE(instance.getString("locCode").trim());
            locations.setFLOCATIONS_LOC_NAME(instance.getString("locName").trim());
            locations.setFLOCATIONS_LOC_T_CODE(instance.getString("loctCode").trim());
           // locations.setFLOCATIONS_REP_CODE(instance.getString("RepCode"));
            locations.setFLOCATIONS_COST_CODE(instance.getString("costCode").trim());

            return locations;
        }

        return null;
    }
}
