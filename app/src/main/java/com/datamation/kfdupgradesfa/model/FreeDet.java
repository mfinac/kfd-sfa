package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class FreeDet {

    @SerializedName("refno")
    private String FFREEDET_REFNO;
    @SerializedName("itemcode")
    private String FFREEDET_ITEM_CODE;

    public String getFFREEDET_REFNO() {
        return FFREEDET_REFNO;
    }

    public void setFFREEDET_REFNO(String fFREEDET_REFNO) {
        FFREEDET_REFNO = fFREEDET_REFNO;
    }

    public String getFFREEDET_ITEM_CODE() {
        return FFREEDET_ITEM_CODE;
    }

    public void setFFREEDET_ITEM_CODE(String fFREEDET_ITEM_CODE) {
        FFREEDET_ITEM_CODE = fFREEDET_ITEM_CODE;
    }

    public static FreeDet parseFreeDet(JSONObject instance) throws JSONException {

        if (instance != null) {
            FreeDet det = new FreeDet();

            det.setFFREEDET_REFNO(instance.getString("refno").trim());
            det.setFFREEDET_ITEM_CODE(instance.getString("itemcode").trim());
            return det;
        }

        return null;
    }

}
