package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemLoc
{
    @SerializedName("ItemCode")
    private String FITEMLOC_ITEM_CODE;
    @SerializedName("LocCode")
    private String FITEMLOC_LOC_CODE;
    @SerializedName("QOH")
    private String FITEMLOC_QOH;


    public String getFITEMLOC_ITEM_CODE() {
        return FITEMLOC_ITEM_CODE;
    }

    public void setFITEMLOC_ITEM_CODE(String fITEMLOC_ITEM_CODE) {
        FITEMLOC_ITEM_CODE = fITEMLOC_ITEM_CODE;
    }

    public String getFITEMLOC_LOC_CODE() {
        return FITEMLOC_LOC_CODE;
    }

    public void setFITEMLOC_LOC_CODE(String fITEMLOC_LOC_CODE) {
        FITEMLOC_LOC_CODE = fITEMLOC_LOC_CODE;
    }

    public String getFITEMLOC_QOH() {
        return FITEMLOC_QOH;
    }

    public void setFITEMLOC_QOH(String fITEMLOC_QOH) {
        FITEMLOC_QOH = fITEMLOC_QOH;
    }


    public static ItemLoc parseItemLocs(JSONObject instance) throws JSONException {

        if (instance != null) {
            ItemLoc loc = new ItemLoc();

            loc.setFITEMLOC_ITEM_CODE(instance.getString("itemCode").trim());
            loc.setFITEMLOC_LOC_CODE(instance.getString("locCode").trim());
            loc.setFITEMLOC_QOH(instance.getString("qoh").trim());

            return loc;
        }

        return null;
    }
}
