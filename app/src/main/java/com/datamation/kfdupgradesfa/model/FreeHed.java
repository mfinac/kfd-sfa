package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class FreeHed {

    @SerializedName("Refno")
    private String FFREEHED_REFNO;
    @SerializedName("Txndate")
    private String FFREEHED_TXNDATE;
    @SerializedName("DiscDesc")
    private String FFREEHED_DISC_DESC;
    @SerializedName("Priority")
    private String FFREEHED_PRIORITY;
    @SerializedName("Vdatef")
    private String FFREEHED_VDATEF;
    @SerializedName("Vdatet")
    private String FFREEHED_VDATET;
    @SerializedName("Remarks")
    private String FFREEHED_REMARKS;
    @SerializedName("ItemQty")
    private String ITEMQTY;
    @SerializedName("FreeItQty")
    private String FFREEHED_FREE_IT_QTY;
    @SerializedName("Ftype")
    private String FFREEHED_FTYPE;
    @SerializedName("Mustflg")
    private String FFREEHED_MUSTFLG;
    @SerializedName("RecCnt")
    private String FFREEHED_REC_CNT;

    private String FFREEHED_ID;
    private String FFREEHED_RECORD_ID;
    private String FFREEHED_ITEM_QTY;

    public String getFFREEHED_MUSTFLG() {
        return FFREEHED_MUSTFLG;
    }

    public void setFFREEHED_MUSTFLG(String FFREEHED_MUSTFLG) {
        this.FFREEHED_MUSTFLG = FFREEHED_MUSTFLG;
    }

    public String getFFREEHED_REC_CNT() {
        return FFREEHED_REC_CNT;
    }

    public void setFFREEHED_REC_CNT(String FFREEHED_REC_CNT) {
        this.FFREEHED_REC_CNT = FFREEHED_REC_CNT;
    }

    public String getITEMQTY() {
        return ITEMQTY;
    }

    public void setITEMQTY(String ITEMQTY) {
        this.ITEMQTY = ITEMQTY;
    }

    public String getFFREEHED_ID() {
        return FFREEHED_ID;
    }

    public void setFFREEHED_ID(String fFREEHED_ID) {
        FFREEHED_ID = fFREEHED_ID;
    }

    public String getFFREEHED_REFNO() {
        return FFREEHED_REFNO;
    }

    public void setFFREEHED_REFNO(String fFREEHED_REFNO) {
        FFREEHED_REFNO = fFREEHED_REFNO;
    }

    public String getFFREEHED_TXNDATE() {
        return FFREEHED_TXNDATE;
    }

    public void setFFREEHED_TXNDATE(String fFREEHED_TXNDATE) {
        FFREEHED_TXNDATE = fFREEHED_TXNDATE;
    }

    public String getFFREEHED_DISC_DESC() {
        return FFREEHED_DISC_DESC;
    }

    public void setFFREEHED_DISC_DESC(String fFREEHED_DISC_DESC) {
        FFREEHED_DISC_DESC = fFREEHED_DISC_DESC;
    }

    public String getFFREEHED_PRIORITY() {
        return FFREEHED_PRIORITY;
    }

    public void setFFREEHED_PRIORITY(String fFREEHED_PRIORITY) {
        FFREEHED_PRIORITY = fFREEHED_PRIORITY;
    }

    public String getFFREEHED_VDATEF() {
        return FFREEHED_VDATEF;
    }

    public void setFFREEHED_VDATEF(String fFREEHED_VDATEF) {
        FFREEHED_VDATEF = fFREEHED_VDATEF;
    }

    public String getFFREEHED_VDATET() {
        return FFREEHED_VDATET;
    }

    public void setFFREEHED_VDATET(String fFREEHED_VDATET) {
        FFREEHED_VDATET = fFREEHED_VDATET;
    }

    public String getFFREEHED_REMARKS() {
        return FFREEHED_REMARKS;
    }

    public void setFFREEHED_REMARKS(String fFREEHED_REMARKS) {
        FFREEHED_REMARKS = fFREEHED_REMARKS;
    }

    public String getFFREEHED_RECORD_ID() {
        return FFREEHED_RECORD_ID;
    }

    public void setFFREEHED_RECORD_ID(String fFREEHED_RECORD_ID) {
        FFREEHED_RECORD_ID = fFREEHED_RECORD_ID;
    }

    public String getFFREEHED_ITEM_QTY() {
        return FFREEHED_ITEM_QTY;
    }

    public void setFFREEHED_ITEM_QTY(String fFREEHED_ITEM_QTY) {
        FFREEHED_ITEM_QTY = fFREEHED_ITEM_QTY;
    }

    public String getFFREEHED_FREE_IT_QTY() {
        return FFREEHED_FREE_IT_QTY;
    }

    public void setFFREEHED_FREE_IT_QTY(String fFREEHED_FREE_IT_QTY) {
        FFREEHED_FREE_IT_QTY = fFREEHED_FREE_IT_QTY;
    }

    public String getFFREEHED_FTYPE() {
        return FFREEHED_FTYPE;
    }

    public void setFFREEHED_FTYPE(String fFREEHED_FTYPE) {
        FFREEHED_FTYPE = fFREEHED_FTYPE;
    }

    public static FreeHed parseFreeHed(JSONObject instance) throws JSONException {

        if (instance != null) {
            FreeHed hed = new FreeHed();

            hed.setFFREEHED_REFNO(instance.getString("refno").trim());
            hed.setFFREEHED_TXNDATE(instance.getString("txndate").trim());
            hed.setFFREEHED_DISC_DESC(instance.getString("discDesc").trim());
            hed.setFFREEHED_PRIORITY(instance.getString("priority").trim());
            hed.setFFREEHED_VDATEF(instance.getString("vdatef").trim());
            hed.setFFREEHED_VDATET(instance.getString("vdatet").trim());
            hed.setFFREEHED_REMARKS(instance.getString("remarks").trim());
            hed.setFFREEHED_ITEM_QTY(instance.getString("itemQty").trim());
            hed.setFFREEHED_FREE_IT_QTY(instance.getString("freeItQty").trim());
            hed.setFFREEHED_FTYPE(instance.getString("ftype").trim());
            hed.setFFREEHED_MUSTFLG(instance.getString("mustflg").trim());
            hed.setFFREEHED_REC_CNT(instance.getString("reccnt").trim());

            return hed;
        }

        return null;
    }

    @Override
    public String toString() {
        return "FreeHed{" +
                "FFREEHED_REFNO='" + FFREEHED_REFNO + '\'' +
                ", FFREEHED_PRIORITY='" + FFREEHED_PRIORITY + '\'' +
                ", ITEMQTY='" + ITEMQTY + '\'' +
                '}';
    }
}
