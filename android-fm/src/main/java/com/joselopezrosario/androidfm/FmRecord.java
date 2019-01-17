package com.joselopezrosario.androidfm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FmRecord
 * A class to facilitate parsing records from an FmData object
 */
public class FmRecord {
    private JSONObject record;
    private String portalName;
    private boolean isPortalRecord;

    /**
     * FmRecord
     * Private constructor used to create the FmRecord object from an FmData's data JSONObject
     * @param record an FmData's JSONObject
     */
    FmRecord(JSONObject record) {
        this.record = record;
    }

    /* ---------------------------------------------------------------------------------------------
    Public getters
    ----------------------------------------------------------------------------------------------*/
    /**
     * getRecordId
     * Get the record's internal id
     * @return the record's id
     */
    public int getRecordId(){
        try {
            return record.getInt("recordId");
        }catch(JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * getModId
     * Get the record's internal modification id (or version)
     * @return the record's mod id
     */
    public int getModId(){
        try {
            return record.getInt("modId");
        }catch(JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * getPortalRecord
     * @param portalName the portal's name or related table's name
     * @param index the position of the record
     * @return an FmRecord object
     */
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

    /**
     * getPortalSize
     * @param portalName
     * @return
     */
    public int portalSize (String portalName){
        try {
            JSONArray portalData = this.record.getJSONObject("portalData").getJSONArray(portalName);
            return portalData.length();
        }catch(JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * getValue
     * Get a record's value
     * @param fieldName the field name
     * @return the field value
     */
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
    /* ---------------------------------------------------------------------------------------------
     Private getters
     ----------------------------------------------------------------------------------------------*/
    private String getPortalName() {
        return portalName;
    }
    /* ---------------------------------------------------------------------------------------------
     Private setters
     ----------------------------------------------------------------------------------------------*/
    private FmRecord setPortalName(String portalName) {
        this.portalName = portalName;
        return this;
    }

    private FmRecord setIsPortalRecord() {
        isPortalRecord = true;
        return this;
    }
}
