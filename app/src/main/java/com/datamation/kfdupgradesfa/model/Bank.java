package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Bank {

    @SerializedName("bankcode")
    private String FBANK_BANK_CODE;
    @SerializedName("bankname")
    private String FBANK_BANK_NAME;
    @SerializedName("bankaccno")
    private String FBANK_BANK_ACC_NO;
    @SerializedName("branch")
    private String FBANK_BRANCH;
    @SerializedName("bankadd1")
    private String FBANK_ADD1;
    @SerializedName("bankadd2")
    private String FBANK_ADD2;

    public String getFBANK_BANK_CODE() {
        return FBANK_BANK_CODE;
    }

    public void setFBANK_BANK_CODE(String fBANK_BANK_CODE) {
        FBANK_BANK_CODE = fBANK_BANK_CODE;
    }

    public String getFBANK_BANK_NAME() {
        return FBANK_BANK_NAME;
    }

    public void setFBANK_BANK_NAME(String fBANK_BANK_NAME) {
        FBANK_BANK_NAME = fBANK_BANK_NAME;
    }

    public String getFBANK_BANK_ACC_NO() {
        return FBANK_BANK_ACC_NO;
    }

    public void setFBANK_BANK_ACC_NO(String fBANK_BANK_ACC_NO) {
        FBANK_BANK_ACC_NO = fBANK_BANK_ACC_NO;
    }

    public String getFBANK_BRANCH() {
        return FBANK_BRANCH;
    }

    public void setFBANK_BRANCH(String fBANK_BRANCH) {
        FBANK_BRANCH = fBANK_BRANCH;
    }

    public String getFBANK_ADD1() {
        return FBANK_ADD1;
    }

    public void setFBANK_ADD1(String fBANK_ADD1) {
        FBANK_ADD1 = fBANK_ADD1;
    }

    public String getFBANK_ADD2() {
        return FBANK_ADD2;
    }

    public void setFBANK_ADD2(String fBANK_ADD2) {
        FBANK_ADD2 = fBANK_ADD2;
    }

    @Override
    public String toString() {
        return FBANK_BANK_NAME;
    }

    public static Bank parseBank(JSONObject instance) throws JSONException {

        if (instance != null) {
            Bank bank = new Bank();

            bank.setFBANK_BANK_CODE(instance.getString("bankcode").trim());
            bank.setFBANK_BANK_NAME(instance.getString("bankname").trim());
            bank.setFBANK_BANK_ACC_NO(instance.getString("bankaccno").trim());
            bank.setFBANK_BRANCH(instance.getString("branch").trim());
            bank.setFBANK_ADD1(instance.getString("bankadd1").trim());
            bank.setFBANK_ADD2(instance.getString("bankadd2").trim());

            return bank;
        }

        return null;
    }
}
