package com.datamation.kfdupgradesfa.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Customer implements Serializable{

	public Customer() {

	}

	private String cusCode;
	private String cusName;
	private String cusNIC;
	private String cusAdd1;
	private String cusAdd2;
	private String cusAdd3;
	private String cusMob;
	private String cusTele;
	private String cusRoute;
	private String cusStatus;
	private String cusEmail;
	private boolean selectedOnList;
	private String creditLimit;
	private String creditStatus;
	private String creditPeriod;
	private String cusPrilCode;
	private String cusImage;
	private String taxreg;
	private String latitude;
	private String longitude;


	public String getCusTele() {
		return cusTele;
	}

	public void setCusTele(String cusTele) {
		this.cusTele = cusTele;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTaxreg() {
		return taxreg;
	}

	public void setTaxreg(String taxreg) {
		this.taxreg = taxreg;
	}

	public String getCusAdd3() {
		return cusAdd3;
	}

	public void setCusAdd3(String cusAdd3) {
		this.cusAdd3 = cusAdd3;
	}

	public String getCusPrilCode() {
		return cusPrilCode;
	}

	public void setCusPrilCode(String cusPrilCode) {
		this.cusPrilCode = cusPrilCode;
	}

	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}

	public String getCreditPeriod() {
		return creditPeriod;
	}

	public void setCreditPeriod(String creditPeriod) {
		this.creditPeriod = creditPeriod;
	}

	public String getCusCode() {
		return cusCode;
	}

	public void setCusCode(String cusCode) {
		this.cusCode = cusCode;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getCusAdd1() {
		return cusAdd1;
	}

	public void setCusAdd1(String cusAdd1) {
		this.cusAdd1 = cusAdd1;
	}

	public String getCusAdd2() {
		return cusAdd2;
	}

	public void setCusAdd2(String cusAdd2) {
		this.cusAdd2 = cusAdd2;
	}

	public String getCusMob() {
		return cusMob;
	}

	public void setCusMob(String cusMob) {
		this.cusMob = cusMob;
	}

	public String getCusRoute() {
		return cusRoute;
	}

	public void setCusRoute(String cusRoute) {
		this.cusRoute = cusRoute;
	}

	public String getCusStatus() {
		return cusStatus;
	}

	public void setCusStatus(String cusStatus) {
		this.cusStatus = cusStatus;
	}

	public String getCusEmail() {
		return cusEmail;
	}

	public void setCusEmail(String cusEmail) {
		this.cusEmail = cusEmail;
	}

	public boolean isSelectedOnList() {
		return selectedOnList;
	}
	
	public void setSelectedOnList(boolean selectedOnList) {
		this.selectedOnList = selectedOnList;
	}

	public String getCusImage() {
		return cusImage;
	}

	public void setCusImage(String cusImage) {
		this.cusImage = cusImage;
	}

	public static Customer parseOutlet(JSONObject instance) throws JSONException {

		if (instance != null) {
			Customer outlet = new Customer();
			String outletIdString;
			outlet.setCusCode(instance.getString("cuscode"));
			outlet.setCusName(instance.getString("cusname"));
			outlet.setCusRoute(instance.getString("routecode"));
			outlet.setCusAdd1(instance.getString("address"));
			outlet.setCusMob(instance.getString("mobile"));
			outlet.setCusEmail(instance.getString("email"));
			outlet.setCusStatus(instance.getString("status"));
			return outlet;
		}

		return null;
	}

		
}
