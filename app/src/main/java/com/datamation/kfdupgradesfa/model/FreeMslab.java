package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class FreeMslab {

    @SerializedName("refno")
    private String FFREEMSLAB_REFNO;
    @SerializedName("qtyf")
    private String FFREEMSLAB_QTY_F;
    @SerializedName("qtyt")
    private String FFREEMSLAB_QTY_T;
    @SerializedName("itemQty")
    private String FFREEMSLAB_ITEM_QTY;
    @SerializedName("freeItQty")
    private String FFREEMSLAB_FREE_IT_QTY;
    @SerializedName("addUser")
    private String FFREEMSLAB_ADD_USER;
    @SerializedName("addDate")
    private String FFREEMSLAB_ADD_DATE;
    @SerializedName("addMach")
    private String FFREEMSLAB_ADD_MACH;
    @SerializedName("seqno")
    private String FFREEMSLAB_SEQ_NO;

    private String FFREEMSLAB_ID;
    private String FFREEMSLAB_RECORD_ID;


    public String getFFREEMSLAB_ID() {
        return FFREEMSLAB_ID;
    }

    public void setFFREEMSLAB_ID(String fFREEMSLAB_ID) {
        FFREEMSLAB_ID = fFREEMSLAB_ID;
    }

    public String getFFREEMSLAB_REFNO() {
        return FFREEMSLAB_REFNO;
    }

    public void setFFREEMSLAB_REFNO(String fFREEMSLAB_REFNO) {
        FFREEMSLAB_REFNO = fFREEMSLAB_REFNO;
    }

    public String getFFREEMSLAB_QTY_F() {
        return FFREEMSLAB_QTY_F;
    }

    public void setFFREEMSLAB_QTY_F(String fFREEMSLAB_QTY_F) {
        FFREEMSLAB_QTY_F = fFREEMSLAB_QTY_F;
    }

    public String getFFREEMSLAB_QTY_T() {
        return FFREEMSLAB_QTY_T;
    }

    public void setFFREEMSLAB_QTY_T(String fFREEMSLAB_QTY_T) {
        FFREEMSLAB_QTY_T = fFREEMSLAB_QTY_T;
    }

    public String getFFREEMSLAB_ITEM_QTY() {
        return FFREEMSLAB_ITEM_QTY;
    }

    public void setFFREEMSLAB_ITEM_QTY(String fFREEMSLAB_ITEM_QTY) {
        FFREEMSLAB_ITEM_QTY = fFREEMSLAB_ITEM_QTY;
    }

    public String getFFREEMSLAB_FREE_IT_QTY() {
        return FFREEMSLAB_FREE_IT_QTY;
    }

    public void setFFREEMSLAB_FREE_IT_QTY(String fFREEMSLAB_FREE_IT_QTY) {
        FFREEMSLAB_FREE_IT_QTY = fFREEMSLAB_FREE_IT_QTY;
    }

    public String getFFREEMSLAB_ADD_USER() {
        return FFREEMSLAB_ADD_USER;
    }

    public void setFFREEMSLAB_ADD_USER(String fFREEMSLAB_ADD_USER) {
        FFREEMSLAB_ADD_USER = fFREEMSLAB_ADD_USER;
    }

    public String getFFREEMSLAB_ADD_DATE() {
        return FFREEMSLAB_ADD_DATE;
    }

    public void setFFREEMSLAB_ADD_DATE(String fFREEMSLAB_ADD_DATE) {
        FFREEMSLAB_ADD_DATE = fFREEMSLAB_ADD_DATE;
    }

    public String getFFREEMSLAB_ADD_MACH() {
        return FFREEMSLAB_ADD_MACH;
    }

    public void setFFREEMSLAB_ADD_MACH(String fFREEMSLAB_ADD_MACH) {
        FFREEMSLAB_ADD_MACH = fFREEMSLAB_ADD_MACH;
    }

    public String getFFREEMSLAB_RECORD_ID() {
        return FFREEMSLAB_RECORD_ID;
    }

    public void setFFREEMSLAB_RECORD_ID(String fFREEMSLAB_RECORD_ID) {
        FFREEMSLAB_RECORD_ID = fFREEMSLAB_RECORD_ID;
    }

    public String getFFREEMSLAB_SEQ_NO() {
        return FFREEMSLAB_SEQ_NO;
    }

    public void setFFREEMSLAB_SEQ_NO(String fFREEMSLAB_SEQ_NO) {
        FFREEMSLAB_SEQ_NO = fFREEMSLAB_SEQ_NO;
    }
    public static FreeMslab parseFreeMslab(JSONObject instance) throws JSONException {

        if (instance != null) {
            FreeMslab freeMslab = new FreeMslab();

                        freeMslab.setFFREEMSLAB_REFNO(instance.getString("refno").trim());
                        freeMslab.setFFREEMSLAB_QTY_F(instance.getString("qtyf").trim());
                        freeMslab.setFFREEMSLAB_QTY_T(instance.getString("qtyt").trim());
                        freeMslab.setFFREEMSLAB_ITEM_QTY(instance.getString("itemQty").trim());
                        freeMslab.setFFREEMSLAB_FREE_IT_QTY(instance.getString("freeItQty").trim());
                        freeMslab.setFFREEMSLAB_ADD_USER(instance.getString("addUser").trim());
                        freeMslab.setFFREEMSLAB_ADD_DATE(instance.getString("addDate").trim());
                        freeMslab.setFFREEMSLAB_ADD_MACH(instance.getString("addMach").trim());
                        freeMslab.setFFREEMSLAB_SEQ_NO(instance.getString("seqno").trim());

            return freeMslab;
        }

        return null;
    }
}
