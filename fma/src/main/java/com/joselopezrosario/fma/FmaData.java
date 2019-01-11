package com.joselopezrosario.fma;

import org.json.JSONArray;
import org.json.JSONException;

public class FmaData {
    private JSONArray data;

    public FmaData() {
    }

    /**
     * create
     * Creates a JSONObject from the data element of the FmaResponse
     *
     * @param response the FmaResponse object
     * @return an FMData object
     */
    public FmaData create(FmaResponse response) {
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
    public FmaRecord getRecord(int index) {
        if (index > this.size()) {
            return null;
        } else {
            try {
                return new FmaRecord(this.getData().getJSONObject(index));
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

