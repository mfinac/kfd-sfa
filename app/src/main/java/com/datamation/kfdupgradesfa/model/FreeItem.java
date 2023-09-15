package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class FreeItem {

    @SerializedName("itemcode")
    private String FFREEITEM_ITEMCODE;
    @SerializedName("refno")
    private String FFREEITEM_REFNO;

    private String FFREEITEM_ID;

    public String getFFREEITEM_ID() {
        return FFREEITEM_ID;
    }

    public void setFFREEITEM_ID(String FFREEITEM_ID) {
        this.FFREEITEM_ID = FFREEITEM_ID;
    }

    public String getFFREEITEM_REFNO() {
        return FFREEITEM_REFNO;
    }

    public void setFFREEITEM_REFNO(String fFREEITEM_REFNO) {
        FFREEITEM_REFNO = fFREEITEM_REFNO;
    }

    public String getFFREEITEM_ITEMCODE() {
        return FFREEITEM_ITEMCODE;
    }

    public void setFFREEITEM_ITEMCODE(String fFREEITEM_ITEMCODE) {
        FFREEITEM_ITEMCODE = fFREEITEM_ITEMCODE;
    }

    public static FreeItem parseFreeItem(JSONObject instance) throws JSONException {

        if (instance != null) {
            FreeItem freeItem = new FreeItem();

            freeItem.setFFREEITEM_ITEMCODE(instance.getString("itemcode").trim());
            freeItem.setFFREEITEM_REFNO(instance.getString("refno").trim());

            return freeItem;
        }

        return null;
    }

}
