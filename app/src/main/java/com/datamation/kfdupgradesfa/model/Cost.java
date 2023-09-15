package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Cost {

    @SerializedName("costCode")
    private String FCOST_CODE;
    @SerializedName("costName")
    private String FCOST_NAME;

    public String getFCOST_CODE() {
        return FCOST_CODE;
    }

    public void setFCOST_CODE(String FCOST_CODE) {
        this.FCOST_CODE = FCOST_CODE;
    }

    public String getFCOST_NAME() {
        return FCOST_NAME;
    }

    public void setFCOST_NAME(String FCOST_NAME) {
        this.FCOST_NAME = FCOST_NAME;
    }

    public static Cost parseCost(JSONObject instance) throws JSONException
    {
        if(instance != null)
        {
            Cost cost = new Cost();

            cost.setFCOST_CODE(instance.getString("costCode").trim());
            cost.setFCOST_NAME(instance.getString("costName").trim());


            return cost;

        }

        return  null;
    }

}
