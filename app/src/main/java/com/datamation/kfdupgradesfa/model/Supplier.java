package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Supplier {

    @SerializedName("supCode")
    private String FSUP_CODE;
    @SerializedName("supName")
    private String FSUP_NAME;

    public String getFSUP_CODE() {
        return FSUP_CODE;
    }

    public void setFSUP_CODE(String FSUP_CODE) {
        this.FSUP_CODE = FSUP_CODE;
    }

    public String getFSUP_NAME() {
        return FSUP_NAME;
    }

    public void setFSUP_NAME(String FSUP_NAME) {
        this.FSUP_NAME = FSUP_NAME;
    }

    public static Supplier parseSupplier(JSONObject instance) throws JSONException
    {
        if(instance != null)
        {
            Supplier supplier = new Supplier();

            supplier.setFSUP_CODE(instance.getString("supCode").trim());
            supplier.setFSUP_NAME(instance.getString("supName").trim());


            return supplier;

        }

        return  null;
    }

}
