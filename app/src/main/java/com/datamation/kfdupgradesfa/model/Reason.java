package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Reason {

	@SerializedName("ReaCode")
	private String FREASON_CODE;
	@SerializedName("ReaName")
	private String FREASON_NAME;
	@SerializedName("ReaTcode")
	private String FREASON_REATCODE;
	@SerializedName("Type")
	private String FREASON_TYPE;


	public String getFREASON_CODE() {
		return FREASON_CODE;
	}

	public void setFREASON_CODE(String FREASON_CODE) {
		this.FREASON_CODE = FREASON_CODE;
	}

	public String getFREASON_NAME() {
		return FREASON_NAME;
	}

	public void setFREASON_NAME(String FREASON_NAME) {
		this.FREASON_NAME = FREASON_NAME;
	}

	public String getFREASON_REATCODE() {
		return FREASON_REATCODE;
	}

	public void setFREASON_REATCODE(String FREASON_REATCODE) {
		this.FREASON_REATCODE = FREASON_REATCODE;
	}

	public String getFREASON_TYPE() {
		return FREASON_TYPE;
	}

	public void setFREASON_TYPE(String FREASON_TYPE) {
		this.FREASON_TYPE = FREASON_TYPE;
	}

	public static Reason parseReason(JSONObject instance) throws JSONException {

		if (instance != null) {
			Reason reason = new Reason();

			reason.setFREASON_CODE(instance.getString("reaCode").trim());
			reason.setFREASON_NAME(instance.getString("reaName").trim());
			reason.setFREASON_REATCODE(instance.getString("reaTcode").trim());
			//reason.setFREASON_TYPE(instance.getString("Type"));
			return reason;
		}

		return null;
	}
}
