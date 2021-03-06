package com.joselopezrosario.androidfm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FmResponse {
    private static final String SCRIPT_ERROR = "scriptError";
    private static final String SCRIPT_RESULT = "scriptResult";
    private static final String SCRIPT_ERROR_PREREQUEST = "scriptError.prerequest";
    private static final String SCRIPT_RESULT_PREREQUEST = "scriptResult.prerequest";
    private static final String SCRIPT_ERROR_PRESORT = "scriptError.presort";
    private static final String SCRIPT_RESULT_PRESORT = "scriptResult.presort";
    private static final String RECORD_ID = "recordId";
    private static final String MOD_ID = "modId";
    private int httpCode;
    private String httpMessage;
    private JSONObject fmResponse;
    private JSONArray fmMessageArray;
    private int fmMessageCode;
    private String fmMessage;
    private int ScriptError;
    private String ScriptResult;
    private int ScriptErrorPreRequest;
    private String ScriptResultPreRequest;
    private int ScriptErrorPreSort;
    private String ScriptResultPreSort;
    private int recordId;
    private int modId;


    /**
     * FmResponse
     * A class to handle the response from the FileMaker Server Data API.
     */
    public FmResponse() {
        // Set the int values to -1 to prevent false positives
        this.setHttpCode(-1);
        this.setFmMessageCode(-1);
        this.setScriptError(-1);
        this.setScriptErrorPreRequest(-1);
        this.setScriptErrorPreSort(-1);
        this.setRecordId(-1);
        this.setModId(-1);
    }

    /*------------------------------------------------------------------------------------------
    Public getters
    ------------------------------------------------------------------------------------------*/

    /**
     * getHttpCode
     *
     * @return the server's http response code (for example, 200 for success)
     */
    public int getHttpCode() {
        return httpCode;
    }

    /**
     * isOk
     *
     * @return true if the FileMaker response message code = 0
     */
    public Boolean isOk() {
        return getFmMessageCode() == 0;
    }

    /**
     * getToken
     *
     * @return get the token from the response (applies to login and OAuthLogin).
     */
    public String getToken() {
        try {
            return this.getFmResponse().getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getFmMessageCode
     *
     * @return the FileMaker response message code (for example, 0 for success)
     */
    public int getFmMessageCode() {
        return fmMessageCode;
    }

    /**
     * getFmMessage
     *
     * @return the FileMaker response message (for example, Ok for success)
     */
    public String getFmMessage() {
        return fmMessage;
    }

    public int getScriptError() {
        return this.ScriptError;
    }

    public String getScriptResult() {
        return ScriptResult;
    }

    public int getPreRequestScriptError() {
        return ScriptErrorPreRequest;
    }

    public String getPreRequestScriptResult() {
        return ScriptResultPreRequest;
    }

    public int getPreSortScriptError() {
        return ScriptErrorPreSort;
    }

    public String getPreSortScriptResult() {
        return ScriptResultPreSort;
    }

    public int getRecordId() {
        return recordId;
    }

    public int getModId() {
        return modId;
    }

    /*------------------------------------------------------------------------------------------
    Package private setters
    ------------------------------------------------------------------------------------------*/
    void setHttpCode(int code) {
        this.httpCode = code;
    }

    void setHttpMessage(String message) {
        this.httpMessage = message;
    }

    /**
     * setFmMessageArray
     *
     * @param fmMessageArray the messages element from the FileMaker Data API response
     */
    void setFmMessageArray(JSONArray fmMessageArray) {
        this.fmMessageArray = fmMessageArray;
        try {
            JSONObject messageData = this.fmMessageArray.getJSONObject(0);
            if (messageData.has("message")) {
                this.setFmMessage(messageData.getString("message"));
            }
            if (messageData.has("code")) {
                this.setFmMessageCode(messageData.getInt("code"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * setFmResponse
     * Set the response, and parse out and setName the message and script responses into their own fields
     *
     * @param fmResponse an FmResponse object
     */
    void setFmResponse(JSONObject fmResponse) {
        this.fmResponse = fmResponse;
        if (this.fmResponse.has(SCRIPT_ERROR)) {
            try {
                this.setScriptError(Integer.valueOf(this.getFmResponse().getString(SCRIPT_ERROR)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (this.fmResponse.has(SCRIPT_RESULT)) {
            try {
                this.setScriptResult(this.getFmResponse().getString(SCRIPT_RESULT));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (this.fmResponse.has(SCRIPT_ERROR_PREREQUEST)) {
            try {
                this.setScriptErrorPreRequest(Integer.valueOf(this.getFmResponse().getString(SCRIPT_ERROR_PREREQUEST)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (this.fmResponse.has(SCRIPT_RESULT_PREREQUEST)) {
            try {
                this.setScriptResultPreRequest(this.getFmResponse().getString(SCRIPT_RESULT_PREREQUEST));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (this.fmResponse.has(SCRIPT_ERROR_PRESORT)) {
            try {
                this.setScriptErrorPreSort(Integer.valueOf(this.getFmResponse().getString(SCRIPT_ERROR_PRESORT)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (this.fmResponse.has(SCRIPT_RESULT_PRESORT)) {
            try {
                this.setScriptResultPreSort(this.getFmResponse().getString(SCRIPT_RESULT_PRESORT));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (this.fmResponse.has(RECORD_ID)) {
            try {
                this.setRecordId(this.getFmResponse().getInt(RECORD_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (this.fmResponse.has(MOD_ID)) {
            try {
                this.setModId(this.getFmResponse().getInt(MOD_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*------------------------------------------------------------------------------------------
    Package private getters
    ------------------------------------------------------------------------------------------*/
    String getHttpMessage() {
        return this.httpMessage;
    }

    JSONObject getFmResponse() {
        return this.fmResponse;
    }

    /*------------------------------------------------------------------------------------------
    Private setters
    ------------------------------------------------------------------------------------------*/

    private void setFmMessageCode(int fmMessageCode) {
        this.fmMessageCode = fmMessageCode;
    }

    private void setFmMessage(String fmMessage) {
        this.fmMessage = fmMessage;
    }

    private void setScriptError(int scriptError) {
        this.ScriptError = scriptError;
    }

    private void setScriptResult(String scriptResult) {
        this.ScriptResult = scriptResult;
    }

    private void setScriptErrorPreRequest(int scriptErrorPreRequest) {
        this.ScriptErrorPreRequest = scriptErrorPreRequest;
    }

    private void setScriptResultPreRequest(String scriptResultPreRequest) {
        this.ScriptResultPreRequest = scriptResultPreRequest;
    }

    private void setScriptErrorPreSort(int scriptErrorPreSort) {
        this.ScriptErrorPreSort = scriptErrorPreSort;
    }

    private void setScriptResultPreSort(String scriptResultPreSort) {
        this.ScriptResultPreSort = scriptResultPreSort;
    }

    private void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    private void setModId(int modId) {
        this.modId = modId;
    }

    /*----------------------------------------------------------------------------------------------
        Private getters
    ----------------------------------------------------------------------------------------------*/
    private JSONArray getFmMessageArray() {
        return this.fmMessageArray;
    }
}
