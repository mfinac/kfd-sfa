package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

public class IsConfirm {

    @SerializedName("ConfirmSatus")
    private String isConfirm;

    public String getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(String isConfirm) {
        this.isConfirm = isConfirm;
    }
}
