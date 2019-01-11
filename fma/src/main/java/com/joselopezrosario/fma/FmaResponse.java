package com.joselopezrosario.fma;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FmaResponse {
    private int httpCode;
    private String httpMessage;
    private JSONObject fmResponse;
    private JSONArray fmMessages;
    private Boolean success;

    public FmaResponse() {
        this.success = false;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int code) {
        this.httpCode = code;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(String message) {
        this.httpMessage = message;
    }

    public JSONObject getFmResponse() {
        return fmResponse;
    }

    public void setFmResponse(JSONObject fmResponse) {
        this.fmResponse = fmResponse;
    }

    public JSONArray getFmMessages() {
        return fmMessages;
    }

    public void setFmMessages(JSONArray fmMessages) {
        this.fmMessages = fmMessages;
    }

    public String getToken(){
        try{
            return this.getFmResponse().getString("token");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public Boolean isOk() {
        return success;
    }

    public void setOk(Boolean success) {
        this.success = success;
    }
}
