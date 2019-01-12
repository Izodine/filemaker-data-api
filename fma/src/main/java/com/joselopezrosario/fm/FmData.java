package com.joselopezrosario.fm;

import org.json.JSONArray;
import org.json.JSONException;

public class FmData {
    private JSONArray data;

    public FmData() {
    }

    /**
     * create
     * Creates a JSONObject from the data element of the FmResponse
     *
     * @param response the FmResponse object
     * @return an FMData object
     */
    public FmData create(FmResponse response) {
        try {
            this.setData(response.getFmResponse().getJSONArray("data"));
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
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
        if (index > this.size()) {
            return null;
        } else {
            try {
                return new FmRecord(this.getData().getJSONObject(index));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /* ---------------------------------------------------------------------------------------------
    Public getters
    ----------------------------------------------------------------------------------------------*/
    public int size() {
        return this.getData().length();
    }

    /* ---------------------------------------------------------------------------------------------
    Private setters
    ----------------------------------------------------------------------------------------------*/
    private void setData(JSONArray data) {
        this.data = data;
    }

    /* ---------------------------------------------------------------------------------------------
    Private getters
    ----------------------------------------------------------------------------------------------*/
    private JSONArray getData() {
        return data;
    }


}

