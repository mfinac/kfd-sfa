package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


public class PayReceiptHed {

	@SerializedName("AddDate")
	private String FPRECHED_ADDDATE ;
	@SerializedName("AddMach")
	private String FPRECHED_ADDMACH ;
	@SerializedName("AddUser")
	private String FPRECHED_ADDUSER ;
	@SerializedName("ChqDate")
	private String FPRECHED_CHQDATE ;
	@SerializedName("ChqNo")
	private String FPRECHED_CHQNO ;
	@SerializedName("DebCode")
	private String FPRECHED_DEBCODE ;
	@SerializedName("PayType")
	private String FPRECHED_PAYTYPE ;
	@SerializedName("RefNo")
	private String FPRECHED_REFNO ;
	@SerializedName("RepCode")
	private String FPRECHED_REPCODE ;
	@SerializedName("TotalAmt")
	private String FPRECHED_TOTALAMT;
	@SerializedName("TxnDate")
	private String FPRECHED_TXNDATE ;

	public String getFPRECHED_ADDDATE() {
		return FPRECHED_ADDDATE;
	}

	public void setFPRECHED_ADDDATE(String FPRECHED_ADDDATE) {
		this.FPRECHED_ADDDATE = FPRECHED_ADDDATE;
	}

	public String getFPRECHED_ADDMACH() {
		return FPRECHED_ADDMACH;
	}

	public void setFPRECHED_ADDMACH(String FPRECHED_ADDMACH) {
		this.FPRECHED_ADDMACH = FPRECHED_ADDMACH;
	}

	public String getFPRECHED_ADDUSER() {
		return FPRECHED_ADDUSER;
	}

	public void setFPRECHED_ADDUSER(String FPRECHED_ADDUSER) {
		this.FPRECHED_ADDUSER = FPRECHED_ADDUSER;
	}

	public String getFPRECHED_CHQDATE() {
		return FPRECHED_CHQDATE;
	}

	public void setFPRECHED_CHQDATE(String FPRECHED_CHQDATE) {
		this.FPRECHED_CHQDATE = FPRECHED_CHQDATE;
	}

	public String getFPRECHED_CHQNO() {
		return FPRECHED_CHQNO;
	}

	public void setFPRECHED_CHQNO(String FPRECHED_CHQNO) {
		this.FPRECHED_CHQNO = FPRECHED_CHQNO;
	}

	public String getFPRECHED_DEBCODE() {
		return FPRECHED_DEBCODE;
	}

	public void setFPRECHED_DEBCODE(String FPRECHED_DEBCODE) {
		this.FPRECHED_DEBCODE = FPRECHED_DEBCODE;
	}

	public String getFPRECHED_PAYTYPE() {
		return FPRECHED_PAYTYPE;
	}

	public void setFPRECHED_PAYTYPE(String FPRECHED_PAYTYPE) {
		this.FPRECHED_PAYTYPE = FPRECHED_PAYTYPE;
	}

	public String getFPRECHED_REFNO() {
		return FPRECHED_REFNO;
	}

	public void setFPRECHED_REFNO(String FPRECHED_REFNO) {
		this.FPRECHED_REFNO = FPRECHED_REFNO;
	}

	public String getFPRECHED_REPCODE() {
		return FPRECHED_REPCODE;
	}

	public void setFPRECHED_REPCODE(String FPRECHED_REPCODE) {
		this.FPRECHED_REPCODE = FPRECHED_REPCODE;
	}

	public String getFPRECHED_TOTALAMT() {
		return FPRECHED_TOTALAMT;
	}

	public void setFPRECHED_TOTALAMT(String FPRECHED_TOTALAMT) {
		this.FPRECHED_TOTALAMT = FPRECHED_TOTALAMT;
	}

	public String getFPRECHED_TXNDATE() {
		return FPRECHED_TXNDATE;
	}

	public void setFPRECHED_TXNDATE(String FPRECHED_TXNDATE) {
		this.FPRECHED_TXNDATE = FPRECHED_TXNDATE;
	}

	public static PayReceiptHed parseRecHed(JSONObject instance) throws JSONException{

		if(instance != null){

			PayReceiptHed recHed = new PayReceiptHed();

			recHed.setFPRECHED_ADDDATE(instance.getString("AddDate"));
			recHed.setFPRECHED_ADDMACH(instance.getString("AddMach"));
			recHed.setFPRECHED_ADDUSER(instance.getString("AddUser"));
			recHed.setFPRECHED_CHQDATE(instance.getString("ChqDate"));
			recHed.setFPRECHED_CHQNO(instance.getString("ChqNo"));
			recHed.setFPRECHED_DEBCODE(instance.getString("DebCode"));
			recHed.setFPRECHED_PAYTYPE(instance.getString("PayType"));
			recHed.setFPRECHED_REFNO(instance.getString("RefNo"));
			recHed.setFPRECHED_REPCODE(instance.getString("RepCode"));
			recHed.setFPRECHED_TOTALAMT(instance.getString("TotalAmt"));
			recHed.setFPRECHED_TXNDATE(instance.getString("TxnDate"));

			return recHed;

		}
		return  null;

	}
}
