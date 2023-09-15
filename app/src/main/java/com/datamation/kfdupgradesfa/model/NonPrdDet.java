package com.datamation.kfdupgradesfa.model;

public class NonPrdDet {

    private String RefNo;
    private String TxnDate;
    private String ReasonCode;
    private String Reason;
    private String DebCode;

    public String getDebCode() {
        return DebCode;
    }

    public void setDebCode(String debCode) {
        DebCode = debCode;
    }

    public String getRefNo() {
        return RefNo;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public String getTxnDate() {
        return TxnDate;
    }

    public void setTxnDate(String txnDate) {
        TxnDate = txnDate;
    }

    public String getReasonCode() {
        return ReasonCode;
    }

    public void setReasonCode(String reasonCode) {
        ReasonCode = reasonCode;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}
