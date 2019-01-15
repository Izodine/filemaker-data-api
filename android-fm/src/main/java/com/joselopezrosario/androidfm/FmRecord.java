package com.joselopezrosario.androidfm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FmRecord {
    private JSONObject record;
    private String portalName;
    private boolean isPortalRecord;

    public FmRecord(JSONObject record) {
        this.record = record;
    }

    public int getRecordId(){
        try {
            return record.getInt("recordId");
        }catch(JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    public int getModId(){
        try {
            return record.getInt("modId");
        }catch(JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    public FmRecord getPortalRecord (String portalName, int index){
        try {
            JSONObject portalData = this.record.getJSONObject("portalData");
            JSONArray portalRecords = portalData.getJSONArray(portalName);
            JSONObject record = portalRecords.getJSONObject(index);
            return new FmRecord(record).setPortalName(portalName).setIsPortalRecord();
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public int portalSize (String portalName){
        try {
            JSONArray portalData = this.record.getJSONObject("portalData").getJSONArray(portalName);
            return portalData.length();
        }catch(JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    public String getValue(String fieldName){
        if ( !this.isPortalRecord ){
            try{
                return record.getJSONObject("fieldData").getString(fieldName);
            }catch(JSONException e){
                e.printStackTrace();
            }
        } else {
            try{
                return record.getString(this.getPortalName() + "::" + fieldName);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getPortalName() {
        return portalName;
    }

    private FmRecord setPortalName(String portalName) {
        this.portalName = portalName;
        return this;
    }

    private FmRecord setIsPortalRecord() {
        isPortalRecord = true;
        return this;
    }
}
