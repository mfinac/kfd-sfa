package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class RouteDet {

    @SerializedName("debCode")
    private String FROUTEDET_DEB_CODE;
    @SerializedName("routeCode")
    private String FROUTEDET_ROUTE_CODE;

    public String getFROUTEDET_DEB_CODE() {
        return FROUTEDET_DEB_CODE;
    }

    public void setFROUTEDET_DEB_CODE(String FROUTEDET_DEB_CODE) {
        this.FROUTEDET_DEB_CODE = FROUTEDET_DEB_CODE;
    }

    public String getFROUTEDET_ROUTE_CODE() {
        return FROUTEDET_ROUTE_CODE;
    }

    public void setFROUTEDET_ROUTE_CODE(String FROUTEDET_ROUTE_CODE) {
        this.FROUTEDET_ROUTE_CODE = FROUTEDET_ROUTE_CODE;
    }

    public static RouteDet parseRoute(JSONObject instance) throws JSONException {

        if (instance != null) {
            RouteDet routeDet = new RouteDet();
            routeDet.setFROUTEDET_DEB_CODE(instance.getString("debCode").trim());
            routeDet.setFROUTEDET_ROUTE_CODE(instance.getString("routeCode").trim());

            return routeDet;
        }

        return null;
    }
}
