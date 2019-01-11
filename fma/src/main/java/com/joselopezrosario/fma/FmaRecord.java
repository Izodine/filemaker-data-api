package com.joselopezrosario.fma;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FmaRecord {
    private JSONObject record;
    private String portalName;
    private boolean isPortalRecord;

    public FmaRecord(JSONObject record) {
        this.record = record;
    }

    public String getRecordId(){
        try {
            return record.getString("recordId");
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getModId(){
        try {
            return record.getString("modId");
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public FmaRecord getPortalRecord (String portalName, int index){
        try {
            JSONObject portalData = this.record.getJSONObject("portalData");
            JSONArray portalRecords = portalData.getJSONArray(portalName);
            JSONObject record = portalRecords.getJSONObject(index);
            return new FmaRecord(record).setPortalName(portalName).setIsPortalRecord(true);
        }catch(JSONException e){
            e.printStackTrace();
            return null;
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

    private FmaRecord setPortalName(String portalName) {
        this.portalName = portalName;
        return this;
    }

    private boolean isPortalRecord() {
        return isPortalRecord;
    }

    private FmaRecord setIsPortalRecord(boolean portalRecord) {
        isPortalRecord = portalRecord;
        return this;
    }
}
