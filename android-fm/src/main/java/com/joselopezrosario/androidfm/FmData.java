package com.joselopezrosario.androidfm;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
/**
 * FmData
 * A class to create data objects from a response
 */
public class FmData {
    private JSONArray data;
    private String error;

    /**
     * FmData
     * Create a new FmData object by passing in an FmResponse object
     * @param response the FmResponse object
     */
    public FmData(@NonNull FmResponse response) {
       this.create(response);
    }

    /* ---------------------------------------------------------------------------------------------
    Public getters
    ----------------------------------------------------------------------------------------------*/
    /**
     * size
     * To get the number of records in the data JSONArray
     * @return the number of records
     */
    public int size() {
        if ( this.getData() == null){
            this.setError("The data object is null");
            return 0;
        }else {
            return this.getData().length();
        }
    }

    /**
     * getRecord
     * Get the record specified by its index position
     *
     * @param index the record's position
     * @return a record in JSONObject format
     */
    public FmRecord getRecord(int index) {
            try {
                return new FmRecord(this.getData().getJSONObject(index));
            } catch (JSONException e) {
                this.setError(e.toString());
                return null;
            }
    }

    public String getError() {
        return error;
    }

    /* ---------------------------------------------------------------------------------------------
    Private setters
    ----------------------------------------------------------------------------------------------*/
    /**
     * setData
     * Used to initialize the FmData object
     * @param data a JSONArray object created from the response's data element
     */
    private void setData(JSONArray data) {
        this.data = data;
    }

    /**
     * create
     * Creates a JSONObject from the data element of the FmResponse
     *
     * @param response the FmResponse object
     * @return an FMData object
     */
    private FmData create(FmResponse response) {
        if ( response == null){
            this.setError("The response object is null");
            return null;
        }
        try {
            this.setData(response.getFmResponse().getJSONArray("data"));
            return this;
        } catch (JSONException e) {
            this.setError(e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private void setError(String error){
        this.error = error;
    }
    /* ---------------------------------------------------------------------------------------------
    Private getters
    ----------------------------------------------------------------------------------------------*/
    /**
     * getData
     * @return the JSONArray data object
     */
    private JSONArray getData() {
        return data;
    }


}

