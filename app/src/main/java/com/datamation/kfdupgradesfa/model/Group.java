package com.datamation.kfdupgradesfa.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Group {

    @SerializedName("groupCode")
    private String FGROUP_CODE;
    @SerializedName("groupName")
    private String FGROUP_NAME;

    public String getFGROUP_CODE() {
        return FGROUP_CODE;
    }

    public void setFGROUP_CODE(String FGROUP_CODE) {
        this.FGROUP_CODE = FGROUP_CODE;
    }

    public String getFGROUP_NAME() {
        return FGROUP_NAME;
    }

    public void setFGROUP_NAME(String FGROUP_NAME) {
        this.FGROUP_NAME = FGROUP_NAME;
    }

    public static Group parseGroup(JSONObject instance) throws JSONException{

        if(instance != null)
        {
            Group group = new Group();

            group.setFGROUP_CODE(instance.getString("groupCode").trim());
            group.setFGROUP_NAME(instance.getString("groupName").trim());

            return group;
        }

        return null;
    }

}
