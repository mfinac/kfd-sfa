package com.datamation.kfdupgradesfa.model;

public class RepGpsLoc {

    public String RepCode ;
    public String Gpsdate ;
    public double Longitude ;
    public double Latitude ;
    public double Battper ;
    public int SeqNo ;
    public String IsSync ;
    public String TxnTime ;

    public String getTxnTime() {
        return TxnTime;
    }

    public void setTxnTime(String txnTime) {
        TxnTime = txnTime;
    }

    public String getRepCode() {
        return RepCode;
    }

    public void setRepCode(String repCode) {
        RepCode = repCode;
    }

    public String getGpsdate() {
        return Gpsdate;
    }

    public void setGpsdate(String gpsdate) {
        Gpsdate = gpsdate;
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

    public double getBattper() {
        return Battper;
    }

    public void setBattper(double battper) {
        Battper = battper;
    }

    public int getSeqNo() {
        return SeqNo;
    }

    public void setSeqNo(int seqNo) {
        SeqNo = seqNo;
    }

    public String getIsSync() {
        return IsSync;
    }

    public void setIsSync(String isSync) {
        IsSync = isSync;
    }
}
