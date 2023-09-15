package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class FreeDeb {

    @SerializedName("refno")
    private String FFREEDEB_REFNO;
    @SerializedName("debcode")
    private String FFREEDEB_DEB_CODE;

    public String getFFREEDEB_REFNO() {
        return FFREEDEB_REFNO;
    }

    public void setFFREEDEB_REFNO(String FFREEDEB_REFNO) {
        this.FFREEDEB_REFNO = FFREEDEB_REFNO;
    }

    public String getFFREEDEB_DEB_CODE() {
        return FFREEDEB_DEB_CODE;
    }

    public void setFFREEDEB_DEB_CODE(String FFREEDEB_DEB_CODE) {
        this.FFREEDEB_DEB_CODE = FFREEDEB_DEB_CODE;
    }

    public static FreeDeb parseFreeDeb(JSONObject instance) throws JSONException {

        if (instance != null) {
            FreeDeb deb = new FreeDeb();

            deb.setFFREEDEB_REFNO(instance.getString("refno").trim());
            deb.setFFREEDEB_DEB_CODE(instance.getString("debcode").trim());

            return deb;
        }

        return null;
    }

}
