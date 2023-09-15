package com.datamation.kfdupgradesfa.model;

import java.util.ArrayList;

public class NonPrdHed {


    private String NextNumVal;
    private String LoginId;
    private String RefNo;
    private String TxnDate;
    private String RepCode;
    private String Remarks;
    private String CostCode;
    private String AddUser;
    private String AddDate;
    private String AddMach;
    private String Address;
    private String DebCode;
    private double Longitude;
    private double Latitude;
    private String StartTime;
    private String EndTime;
    private String IsSync;
    private ArrayList<NonPrdDet> NonPrdDetList;

    public ArrayList<NonPrdDet> getNonPrdDetList() {
        return NonPrdDetList;
    }

    public void setNonPrdDetList(ArrayList<NonPrdDet> nonPrdDetList) {
        NonPrdDetList = nonPrdDetList;
    }

    public String getIsSync() {
        return IsSync;
    }

    public void setIsSync(String isSync) {
        IsSync = isSync;
    }

    public String getNextNumVal() {
        return NextNumVal;
    }

    public void setNextNumVal(String nextNumVal) {
        NextNumVal = nextNumVal;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
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

    public String getRepCode() {
        return RepCode;
    }

    public void setRepCode(String repCode) {
        RepCode = repCode;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getCostCode() {
        return CostCode;
    }

    public void setCostCode(String costCode) {
        CostCode = costCode;
    }

    public String getAddUser() {
        return AddUser;
    }

    public void setAddUser(String addUser) {
        AddUser = addUser;
    }

    public String getAddDate() {
        return AddDate;
    }

    public void setAddDate(String addDate) {
        AddDate = addDate;
    }

    public String getAddMach() {
        return AddMach;
    }

    public void setAddMach(String addMach) {
        AddMach = addMach;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDebCode() {
        return DebCode;
    }

    public void setDebCode(String debCode) {
        DebCode = debCode;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
