package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Type {

    @SerializedName("typeCode")
    private String FTYPE_CODE;
    @SerializedName("typeName")
    private String FTYPE_NAME;

    public String getFTYPE_CODE() {
        return FTYPE_CODE;
    }

    public void setFTYPE_CODE(String FTYPE_CODE) {
        this.FTYPE_CODE = FTYPE_CODE;
    }

    public String getFTYPE_NAME() {
        return FTYPE_NAME;
    }

    public void setFTYPE_NAME(String FTYPE_NAME) {
        this.FTYPE_NAME = FTYPE_NAME;
    }

    public static Type parseType(JSONObject instance) throws JSONException{

        if(instance != null)
        {
            Type type = new Type();

            type.setFTYPE_CODE(instance.getString("typeCode").trim());
            type.setFTYPE_NAME(instance.getString("typeName").trim());

            return type;
        }
        return null;
    }
}
