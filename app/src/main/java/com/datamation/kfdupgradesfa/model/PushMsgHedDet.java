package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class PushMsgHedDet {

    @SerializedName("MsgBody")
    private String FPUSHMSG_MSG_BODY;
    @SerializedName("MsgGrp")
    private String FPUSHMSG_MSG_GRP;
    @SerializedName("MsgType")
    private String FPUSHMSG_MSG_TYPE;
    @SerializedName("RefNo")
    private String FPUSHMSG_REPNO;
    @SerializedName("Subject")
    private String FPUSHMSG_SUBJECT;
    @SerializedName("SupCode")
    private String FPUSHMSG_SUPCODE;


    public String getFPUSHMSG_MSG_BODY() {
        return FPUSHMSG_MSG_BODY;
    }

    public void setFPUSHMSG_MSG_BODY(String FPUSHMSG_MSG_BODY) {
        this.FPUSHMSG_MSG_BODY = FPUSHMSG_MSG_BODY;
    }

    public String getFPUSHMSG_MSG_GRP() {
        return FPUSHMSG_MSG_GRP;
    }

    public void setFPUSHMSG_MSG_GRP(String FPUSHMSG_MSG_GRP) {
        this.FPUSHMSG_MSG_GRP = FPUSHMSG_MSG_GRP;
    }

    public String getFPUSHMSG_MSG_TYPE() {
        return FPUSHMSG_MSG_TYPE;
    }

    public void setFPUSHMSG_MSG_TYPE(String FPUSHMSG_MSG_TYPE) {
        this.FPUSHMSG_MSG_TYPE = FPUSHMSG_MSG_TYPE;
    }

    public String getFPUSHMSG_REPNO() {
        return FPUSHMSG_REPNO;
    }

    public void setFPUSHMSG_REPNO(String FPUSHMSG_REPNO) {
        this.FPUSHMSG_REPNO = FPUSHMSG_REPNO;
    }

    public String getFPUSHMSG_SUBJECT() {
        return FPUSHMSG_SUBJECT;
    }

    public void setFPUSHMSG_SUBJECT(String FPUSHMSG_SUBJECT) {
        this.FPUSHMSG_SUBJECT = FPUSHMSG_SUBJECT;
    }

    public String getFPUSHMSG_SUPCODE() {
        return FPUSHMSG_SUPCODE;
    }

    public void setFPUSHMSG_SUPCODE(String FPUSHMSG_SUPCODE) {
        this.FPUSHMSG_SUPCODE = FPUSHMSG_SUPCODE;
    }

    public static PushMsgHedDet parsePMsg(JSONObject instance) throws JSONException {

        if (instance != null) {
            PushMsgHedDet pMsg = new PushMsgHedDet();
            pMsg.setFPUSHMSG_MSG_BODY(instance.getString("MsgBody"));
            pMsg.setFPUSHMSG_MSG_GRP(instance.getString("MsgGrp"));
            pMsg.setFPUSHMSG_MSG_TYPE(instance.getString("MsgType"));
            pMsg.setFPUSHMSG_REPNO(instance.getString("RefNo"));
            pMsg.setFPUSHMSG_SUBJECT(instance.getString("Subject"));
            pMsg.setFPUSHMSG_SUPCODE(instance.getString("SupCode"));
            return pMsg;
        }

        return null;
    }
}
