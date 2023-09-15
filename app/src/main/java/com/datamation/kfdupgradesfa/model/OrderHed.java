package com.datamation.kfdupgradesfa.model;

import java.util.ArrayList;

public class OrderHed {

    public String RefNo ;
    public String RefNo1 ;
    public String TxnDate ;
    public String ManuRef ;
    public String CurCode ;
    public String CostCode ;
    public double CurRate ;
    public String DebCode ;
    public String Remarks ;
    public String TxnType ;
    public String QuoTerm ;
    public String LocCode ;
    public String RepCode ;
    public double BtotalDis ;
    public double TotalDis ;
    public double PtotalDis ;
    public double BptotalDis ;
    public double BtotalTax ;
    public double TotalTax;
    public double BtotalAmt ;
    public double TotalAmt ;
    public String TaxReg ;
    public String Contact ;
    public String CusAdd1 ;
    public String CusAdd2 ;
    public String CusAdd3 ;
    public String CusTele ;
    public double PrtCopy ;
    public String AddUser ;
    public String AddDate ;
    public String AddMach;
    public String RouteCode ;
    public double InvFlg ;
    public double Longitude ;
    public double Latitude ;
    public String StartTimeSo ;
    public String EndTimeSo ;
    public String Address ;
    public double DisHflg ;
    public String UpdDate ;
    public String AppVersion ;
    public String NextNumVal ;
    public double Soclsflg ;
    public double Dispflg ;
    public boolean AppSts ;
    public double WebOrdflg;
    public double AndFlg ;
    public String IsSync ;
    public String IsActive ;
    public String Status ;



    private ArrayList<OrderDet> OrderDetDetails;
    private ArrayList<OrderFreeIss> FreeIssueDetails;


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getIsSync() {
        return IsSync;
    }

    public void setIsSync(String isSync) {
        IsSync = isSync;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getRefNo() {
        return RefNo;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public String getRefNo1() {
        return RefNo1;
    }

    public void setRefNo1(String refNo1) {
        RefNo1 = refNo1;
    }

    public String getTxnDate() {
        return TxnDate;
    }

    public void setTxnDate(String txnDate) {
        TxnDate = txnDate;
    }

    public String getManuRef() {
        return ManuRef;
    }

    public void setManuRef(String manuRef) {
        ManuRef = manuRef;
    }

    public String getCurCode() {
        return CurCode;
    }

    public void setCurCode(String curCode) {
        CurCode = curCode;
    }

    public String getCostCode() {
        return CostCode;
    }

    public void setCostCode(String costCode) {
        CostCode = costCode;
    }

    public double getCurRate() {
        return CurRate;
    }

    public void setCurRate(double curRate) {
        CurRate = curRate;
    }

    public String getDebCode() {
        return DebCode;
    }

    public void setDebCode(String debCode) {
        DebCode = debCode;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getTxnType() {
        return TxnType;
    }

    public void setTxnType(String txnType) {
        TxnType = txnType;
    }

    public String getQuoTerm() {
        return QuoTerm;
    }

    public void setQuoTerm(String quoTerm) {
        QuoTerm = quoTerm;
    }

    public String getLocCode() {
        return LocCode;
    }

    public void setLocCode(String locCode) {
        LocCode = locCode;
    }

    public String getRepCode() {
        return RepCode;
    }

    public void setRepCode(String repCode) {
        RepCode = repCode;
    }

    public double getBtotalDis() {
        return BtotalDis;
    }

    public void setBtotalDis(double btotalDis) {
        BtotalDis = btotalDis;
    }

    public double getTotalDis() {
        return TotalDis;
    }

    public void setTotalDis(double totalDis) {
        TotalDis = totalDis;
    }

    public double getPtotalDis() {
        return PtotalDis;
    }

    public void setPtotalDis(double ptotalDis) {
        PtotalDis = ptotalDis;
    }

    public double getBptotalDis() {
        return BptotalDis;
    }

    public void setBptotalDis(double bptotalDis) {
        BptotalDis = bptotalDis;
    }

    public double getBtotalTax() {
        return BtotalTax;
    }

    public void setBtotalTax(double btotalTax) {
        BtotalTax = btotalTax;
    }

    public double getTotalTax() {
        return TotalTax;
    }

    public void setTotalTax(double totalTax) {
        TotalTax = totalTax;
    }

    public double getBtotalAmt() {
        return BtotalAmt;
    }

    public void setBtotalAmt(double btotalAmt) {
        BtotalAmt = btotalAmt;
    }

    public double getTotalAmt() {
        return TotalAmt;
    }

    public void setTotalAmt(double totalAmt) {
        TotalAmt = totalAmt;
    }

    public String getTaxReg() {
        return TaxReg;
    }

    public void setTaxReg(String taxReg) {
        TaxReg = taxReg;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getCusAdd1() {
        return CusAdd1;
    }

    public void setCusAdd1(String cusAdd1) {
        CusAdd1 = cusAdd1;
    }

    public String getCusAdd2() {
        return CusAdd2;
    }

    public void setCusAdd2(String cusAdd2) {
        CusAdd2 = cusAdd2;
    }

    public String getCusAdd3() {
        return CusAdd3;
    }

    public void setCusAdd3(String cusAdd3) {
        CusAdd3 = cusAdd3;
    }

    public String getCusTele() {
        return CusTele;
    }

    public void setCusTele(String cusTele) {
        CusTele = cusTele;
    }

    public double getPrtCopy() {
        return PrtCopy;
    }

    public void setPrtCopy(double prtCopy) {
        PrtCopy = prtCopy;
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

    public String getRouteCode() {
        return RouteCode;
    }

    public void setRouteCode(String routeCode) {
        RouteCode = routeCode;
    }

    public double getInvFlg() {
        return InvFlg;
    }

    public void setInvFlg(double invFlg) {
        InvFlg = invFlg;
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

    public String getStartTimeSo() {
        return StartTimeSo;
    }

    public void setStartTimeSo(String startTimeSo) {
        StartTimeSo = startTimeSo;
    }

    public String getEndTimeSo() {
        return EndTimeSo;
    }

    public void setEndTimeSo(String endTimeSo) {
        EndTimeSo = endTimeSo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getDisHflg() {
        return DisHflg;
    }

    public void setDisHflg(double disHflg) {
        DisHflg = disHflg;
    }

    public String getUpdDate() {
        return UpdDate;
    }

    public void setUpdDate(String updDate) {
        UpdDate = updDate;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public String getNextNumVal() {
        return NextNumVal;
    }

    public void setNextNumVal(String nextNumVal) {
        NextNumVal = nextNumVal;
    }

    public double getSoclsflg() {
        return Soclsflg;
    }

    public void setSoclsflg(double soclsflg) {
        Soclsflg = soclsflg;
    }

    public double getDispflg() {
        return Dispflg;
    }

    public void setDispflg(double dispflg) {
        Dispflg = dispflg;
    }

    public boolean isAppSts() {
        return AppSts;
    }

    public void setAppSts(boolean appSts) {
        AppSts = appSts;
    }

    public double getWebOrdflg() {
        return WebOrdflg;
    }

    public void setWebOrdflg(double webOrdflg) {
        WebOrdflg = webOrdflg;
    }

    public double getAndFlg() {
        return AndFlg;
    }

    public void setAndFlg(double andFlg) {
        AndFlg = andFlg;
    }

    public ArrayList<OrderDet> getOrderDetDetails() {
        return OrderDetDetails;
    }

    public void setOrderDetDetails(ArrayList<OrderDet> orderDetDetails) {
        OrderDetDetails = orderDetDetails;
    }

    public ArrayList<OrderFreeIss> getFreeIssueDetails() {
        return FreeIssueDetails;
    }

    public void setFreeIssueDetails(ArrayList<OrderFreeIss> freeIssueDetails) {
        FreeIssueDetails = freeIssueDetails;
    }
}
