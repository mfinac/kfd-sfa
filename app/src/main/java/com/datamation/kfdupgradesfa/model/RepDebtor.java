package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/*
    create by kaveesha - 24/11/2021
 */

public class RepDebtor {

    @SerializedName("repcode")
    private String FREPCODE;
    @SerializedName("debcode")
    private String FDEBCODE;

    public String getFREPCODE() {
        return FREPCODE;
    }

    public void setFREPCODE(String FREPCODE) {
        this.FREPCODE = FREPCODE;
    }

    public String getFDEBCODE() {
        return FDEBCODE;
    }

    public void setFDEBCODE(String FDEBCODE) {
        this.FDEBCODE = FDEBCODE;
    }

    public static RepDebtor parseRepDebtor(JSONObject instance) throws JSONException{

        if(instance != null)
        {
            RepDebtor repDebtor = new RepDebtor();

            repDebtor.setFREPCODE(instance.getString("repcode").trim());
            repDebtor.setFDEBCODE(instance.getString("debcode").trim());

            return repDebtor;
        }
        return null;
    }
}
