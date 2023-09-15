package com.datamation.kfdupgradesfa.model;

public class OrderFreeIss {

    private String RefNo;
    private String TxnDate;
    private String RefNo1;
    private String Itemcode;
    private double Qty;

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

    public String getRefNo1() {
        return RefNo1;
    }

    public void setRefNo1(String refNo1) {
        RefNo1 = refNo1;
    }

    public String getItemcode() {
        return Itemcode;
    }

    public void setItemcode(String itemcode) {
        Itemcode = itemcode;
    }

    public double getQty() {
        return Qty;
    }

    public void setQty(double qty) {
        Qty = qty;
    }
}
