package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemPri {

	@SerializedName("ItemCode")
	private String FITEMPRI_ITEM_CODE;
	@SerializedName("Price")
	private String FITEMPRI_PRICE;
	@SerializedName("PrilCode")
	private String FITEMPRI_PRIL_CODE;
	@SerializedName("TxnMach")
	private String FITEMPRI_TXN_MACH;
	@SerializedName("Txnuser")
	private String FITEMPRI_TXN_USER;
	@SerializedName("CostCode")
	private String FITEMPRI_COST_CODE;
	@SerializedName("SKUCode")
	private String FITEMPRI_SKU_CODE;


	public String getFITEMPRI_ITEM_CODE() {
		return FITEMPRI_ITEM_CODE;
	}

	public void setFITEMPRI_ITEM_CODE(String FITEMPRI_ITEM_CODE) {
		this.FITEMPRI_ITEM_CODE = FITEMPRI_ITEM_CODE;
	}

	public String getFITEMPRI_PRICE() {
		return FITEMPRI_PRICE;
	}

	public void setFITEMPRI_PRICE(String FITEMPRI_PRICE) {
		this.FITEMPRI_PRICE = FITEMPRI_PRICE;
	}

	public String getFITEMPRI_PRIL_CODE() {
		return FITEMPRI_PRIL_CODE;
	}

	public void setFITEMPRI_PRIL_CODE(String FITEMPRI_PRIL_CODE) {
		this.FITEMPRI_PRIL_CODE = FITEMPRI_PRIL_CODE;
	}

	public String getFITEMPRI_TXN_MACH() {
		return FITEMPRI_TXN_MACH;
	}

	public void setFITEMPRI_TXN_MACH(String FITEMPRI_TXN_MACH) {
		this.FITEMPRI_TXN_MACH = FITEMPRI_TXN_MACH;
	}

	public String getFITEMPRI_TXN_USER() {
		return FITEMPRI_TXN_USER;
	}

	public void setFITEMPRI_TXN_USER(String FITEMPRI_TXN_USER) {
		this.FITEMPRI_TXN_USER = FITEMPRI_TXN_USER;
	}

	public String getFITEMPRI_COST_CODE() {
		return FITEMPRI_COST_CODE;
	}

	public void setFITEMPRI_COST_CODE(String FITEMPRI_COSTCODE) {
		this.FITEMPRI_COST_CODE = FITEMPRI_COSTCODE;
	}

	public String getFITEMPRI_SKU_CODE() {
		return FITEMPRI_SKU_CODE;
	}

	public void setFITEMPRI_SKU_CODE(String FITEMPRI_SKU_CODE) {
		this.FITEMPRI_SKU_CODE = FITEMPRI_SKU_CODE;
	}

	public static ItemPri parseItemPrices(JSONObject instance) throws JSONException {

		if (instance != null) {
			ItemPri pri = new ItemPri();

			pri.setFITEMPRI_COST_CODE(instance.getString("costcode").trim());
			pri.setFITEMPRI_ITEM_CODE(instance.getString("itemCode").trim());
			pri.setFITEMPRI_PRICE(instance.getString("price").trim());
			pri.setFITEMPRI_PRIL_CODE(instance.getString("prilCode").trim());
		//	pri.setFITEMPRI_SKU_CODE(instance.getString("SKUCode"));

			return pri;
		}

		return null;
	}

}
