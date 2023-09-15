package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class RepCredentials {

    @SerializedName("repCode")
    private String FREF_REPCODE;
    @SerializedName("password")
    private String FREF_PASSWORD;

    public String getFREF_REPCODE() {
        return FREF_REPCODE;
    }

    public void setFREF_REPCODE(String FREF_REPCODE) {
        this.FREF_REPCODE = FREF_REPCODE;
    }

    public String getFREF_PASSWORD() {
        return FREF_PASSWORD;
    }

    public void setFREF_PASSWORD(String FREF_PASSWORD) {
        this.FREF_PASSWORD = FREF_PASSWORD;
    }

    public static RepCredentials parseRefCredentials(JSONObject instance) throws JSONException
    {
        if(instance != null)
        {
            RepCredentials credentials = new RepCredentials();

            credentials.setFREF_REPCODE(instance.getString("repCode"));
            credentials.setFREF_PASSWORD(instance.getString("password"));

            return credentials;

        }

        return  null;
    }

}
