package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Brand {

    @SerializedName("brandCode")
    private String FBRAND_CODE;
    @SerializedName("brandName")
    private String FBRAND_NAME;

    public String getFBRAND_CODE() {
        return FBRAND_CODE;
    }

    public void setFBRAND_CODE(String FBRAND_CODE) {
        this.FBRAND_CODE = FBRAND_CODE;
    }

    public String getFBRAND_NAME() {
        return FBRAND_NAME;
    }

    public void setFBRAND_NAME(String FBRAND_NAME) {
        this.FBRAND_NAME = FBRAND_NAME;
    }

    public static Brand parseBrand(JSONObject instance) throws JSONException{

        if(instance != null){

            Brand brand =  new Brand();

            brand.setFBRAND_CODE(instance.getString("brandCode").trim());
            brand.setFBRAND_NAME(instance.getString("brandName").trim());

            return brand;

        }
        return null;
    }
}
