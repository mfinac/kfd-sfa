package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Payment implements Serializable {

    @SerializedName("InvoiceDate")
    private String FPAYMENT_INVOICE_DATE;
    @SerializedName("InvoiceNo")
    private String  FPAYMENT_INVOICE_NO;
    @SerializedName("PaidAmount")
    private String  FPAYMENT_PAID_AMT;
    @SerializedName("PaymentType")
    private String  FPAYMENT_TYPE;
    @SerializedName("ReceiptDate")
    private String  FPAYMENT_RECEIPT_DATE;
    @SerializedName("ReceiptNo")
    private String  FPAYMENT_RECEIPT_NO;

    private long orderId;
    private CashPayment cashPayment = new CashPayment();
    private Cheque cheque = new Cheque();
    private boolean cash,cheq;

    public Payment() {
    }

    public Payment(CashPayment cashPayment, long orderId) {
        this.cashPayment = cashPayment;
        this.orderId = orderId;
        cash=true;
        cheq=false;
    }

    public Payment(long orderId, Cheque cheque) {
        this.orderId = orderId;
        this.cheque = cheque;
        cheq =true;
        cash=false;
    }

    public String getFPAYMENT_INVOICE_DATE() {
        return FPAYMENT_INVOICE_DATE;
    }

    public void setFPAYMENT_INVOICE_DATE(String FPAYMENT_INVOICE_DATE) {
        this.FPAYMENT_INVOICE_DATE = FPAYMENT_INVOICE_DATE;
    }

    public String getFPAYMENT_INVOICE_NO() {
        return FPAYMENT_INVOICE_NO;
    }

    public void setFPAYMENT_INVOICE_NO(String FPAYMENT_INVOICE_NO) {
        this.FPAYMENT_INVOICE_NO = FPAYMENT_INVOICE_NO;
    }

    public String getFPAYMENT_PAID_AMT() {
        return FPAYMENT_PAID_AMT;
    }

    public void setFPAYMENT_PAID_AMT(String FPAYMENT_PAID_AMT) {
        this.FPAYMENT_PAID_AMT = FPAYMENT_PAID_AMT;
    }

    public String getFPAYMENT_TYPE() {
        return FPAYMENT_TYPE;
    }

    public void setFPAYMENT_TYPE(String FPAYMENT_TYPE) {
        this.FPAYMENT_TYPE = FPAYMENT_TYPE;
    }

    public String getFPAYMENT_RECEIPT_DATE() {
        return FPAYMENT_RECEIPT_DATE;
    }

    public void setFPAYMENT_RECEIPT_DATE(String FPAYMENT_RECEIPT_DATE) {
        this.FPAYMENT_RECEIPT_DATE = FPAYMENT_RECEIPT_DATE;
    }

    public String getFPAYMENT_RECEIPT_NO() {
        return FPAYMENT_RECEIPT_NO;
    }

    public void setFPAYMENT_RECEIPT_NO(String FPAYMENT_RECEIPT_NO) {
        this.FPAYMENT_RECEIPT_NO = FPAYMENT_RECEIPT_NO;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public CashPayment getCashPayment() {
        return cashPayment;
    }

    public void setCashPayment(CashPayment cashPayment) {
        this.cashPayment = cashPayment;
    }

    public Cheque getCheque() {
        return cheque;
    }

    public void setCheque(Cheque cheque) {
        this.cheque = cheque;
    }

    public JSONObject getPaymentAsJSON() {
        Map<String, Object> map = new HashMap<>();

        map.put("order_id", orderId);

        if(isCash()) {
            map.put("cash_amount", String.valueOf(cashPayment.getPaymentAmount()));
            map.put("cash_datetime", String.valueOf(cashPayment.getPaymentTime()));
        } else {
            map.put("check_amount", String.valueOf(cheque.getAmount()));
            map.put("check_datetime", String.valueOf(cheque.getChequeDate()));
            map.put("check_no", String.valueOf(cheque.getChequeNo()));
            map.put("check_bank_id", String.valueOf(cheque.getBankId()));
            map.put("check_branch_id", String.valueOf(cheque.getBranchid()));
        }

        return new JSONObject(map);
    }

    public boolean isCash() {
        return cash;
    }

    public void setCash(boolean cash) {
        this.cash = cash;
    }

    public boolean isCheq() {
        return cheq;
    }

    public void setCheq(boolean cheq) {
        this.cheq = cheq;
    }


    public static Payment parsePayment(JSONObject instance) throws JSONException {

        if (instance != null) {
            Payment payment = new Payment();
            payment.setFPAYMENT_INVOICE_DATE(instance.getString("InvoiceDate"));
            payment.setFPAYMENT_INVOICE_NO(instance.getString("InvoiceNo"));
            payment.setFPAYMENT_PAID_AMT(instance.getString("PaidAmount"));
            payment.setFPAYMENT_TYPE(instance.getString("PaymentType"));
            payment.setFPAYMENT_RECEIPT_DATE(instance.getString("ReceiptDate"));
            payment.setFPAYMENT_RECEIPT_NO(instance.getString("ReceiptNo"));
            return payment;
        }

        return null;
    }
}
