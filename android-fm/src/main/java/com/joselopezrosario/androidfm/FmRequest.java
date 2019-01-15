package com.joselopezrosario.androidfm;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class FmRequest {
    public static final String EMPTY_BODY = "{}";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String GET = "GET";
    public static final String BASIC = "Basic";
    public static final String BEARER = "Bearer";
    private static final String LOGIN = "LOGIN";
    private static final String LOGOUT = "LOGOUT";
    private static final String GETRECORDS = "GETRECORDS";
    private static final String GETRECORD = "GETRECORD";
    private static final String FINDRECORDS = "FINDRECORDS";
    private boolean disableSSL;
    private String fmMethod;
    private String endpoint;
    private int recordId;
    private String method;
    private String auth;
    private String accountName;
    private String password;
    private String layout;
    private String token;
    private String limit;
    private String offset;
    private FmPortal fmPortal;
    private FmSort fmSort;
    private FmScript fmScript;
    private FmFind fmFind;
    private String body;
    private String message;
    private Boolean success;

    public FmRequest() {
        this.setLayout(null);
        this.setOffset(1);
        this.setLimit(100);
        this.setSortParams(null);
        this.setPortalParams(null);
        this.setOk(false);
        this.setAccountName(null);
        this.setPassword(null);
        this.disableSSL(false);
    }

    /**
     * login
     *
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param account  the FileMaker account with fmrest extended privileges
     * @param password the FileMaker account's password
     * @return an FmRequest object
     */
    public FmRequest login(@NonNull String endpoint,
                           @NonNull String account,
                           @NonNull String password) {
        String url = endpoint + "/sessions";
        this.setFmMethod(LOGIN);
        this.setEndpoint(url);
        this.setMethod(POST);
        this.setAccountName(account);
        this.setPassword(password);
        this.setBody(EMPTY_BODY);
        this.setAuth(BASIC);
        return this;
    }

    /**
     * logout
     *
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token    the token returned in the response by executing the login request
     * @return an FmRequest object
     */
    public FmRequest logout(@NonNull String endpoint,
                            @NonNull String token) {
        String url = endpoint + "/sessions/" + token;
        this.setFmMethod(LOGOUT);
        this.setEndpoint(url);
        this.setMethod(DELETE);
        this.setToken(token);
        this.setBody(EMPTY_BODY);
        this.setAuth(BEARER);
        return this;
    }

    /**
     * getRecords
     *
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token    the token returned in the response by executing the login request
     * @param layout   the layout from where to retrieve the records
     * @return an FmRequest object
     */
    public FmRequest getRecords(@NonNull String endpoint,
                                @NonNull String token,
                                @NonNull String layout) {
        this.setFmMethod(GETRECORDS);
        this.setEndpoint(endpoint);
        this.setToken(token);
        this.setLayout(layout);
        this.setMethod(GET);
        this.setBody(EMPTY_BODY);
        this.setAuth(BEARER);
        return this;
    }

    /**
     * getRecords
     *
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token    the token returned in the response by executing the login request
     * @param layout   the layout from where to retrieve the records
     * @return an FmRequest object
     */
    public FmRequest getRecord(@NonNull String endpoint,
                               @NonNull String token,
                               @NonNull String layout,
                               @NonNull int recordId) {
        this.setFmMethod(GETRECORD);
        this.setRecordId(recordId);
        this.setEndpoint(endpoint);
        this.setToken(token);
        this.setLayout(layout);
        this.setMethod(GET);
        this.setBody(EMPTY_BODY);
        this.setAuth(BEARER);
        return this;
    }


    /**
     * FindRecords
     *
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token    the token returned in the response by executing the login request
     * @param layout   the layout from where to retrieve the records
     * @param fmFind     an FmFind object containing the find request(s)
     * @return an FmRequest object
     */
    public FmRequest findRecords(@NonNull String endpoint,
                                 @NonNull String token,
                                 @NonNull String layout,
                                 @NonNull FmFind fmFind) {
        this.setFmMethod(FINDRECORDS);
        this.setEndpoint(endpoint);
        this.setToken(token);
        this.setLayout(layout);
        this.setMethod(POST);
        this.setFmFind(fmFind);
        this.setAuth(BEARER);
        return this;
    }


    /**
     * build
     * Make sure that all required parameters are valid before returning the final request object.
     * If there's an error, the request success field (accessed through isOk()) will return false.
     * Additional informational about the error will be set in the message field (accessed through getMessage()).
     *
     * @return an FmRequest object
     */
    public FmRequest build() {
        if (this.getEndpoint() == null || this.getEndpoint().isEmpty()) {
            this.setOk(false);
            this.setMessage("Invalid endpoint: " + this.getEndpoint());
            return this;
        }
        switch (fmMethod) {
            case LOGIN:
                if (this.getAccountName() == null || this.getAccountName().isEmpty()) {
                    this.setOk(false);
                    this.setMessage("Invalid account name: " + this.getAccountName());
                    return this;
                } else if (this.getPassword() == null || this.getPassword().isEmpty()) {
                    this.setOk(false);
                    this.setMessage("Invalid password: " + this.getPassword());
                    return this;
                } else {
                    this.setOk(true);
                    return this;
                }
            case LOGOUT:
                if (this.getToken() == null || this.getToken().isEmpty()) {
                    this.setOk(false);
                    this.setMessage("Invalid token: " + this.getToken());
                    return this;
                } else {
                    this.setOk(true);
                    return this;
                }
            case GETRECORD:
                if (this.getToken() == null || this.getToken().isEmpty()) {
                    this.setOk(false);
                    this.setMessage("Invalid token: " + this.getToken());
                    return this;
                } else if (this.getLayout() == null || this.getLayout().isEmpty()) {
                    this.setOk(false);
                    this.setMessage("Invalid layout: " + this.getLayout());
                    return this;
                } else if (this.getRecordId() < 1) {
                    this.setMessage("Invalid record id: " + this.getRecordId());
                    return this;
                } else {
                    this.setEndpoint(endpoint + "/layouts/" + layout + "/records/" + recordId);
                    this.setOk(true);
                    return this;
                }
            case GETRECORDS:
                if (this.getToken() == null || this.getToken().isEmpty()) {
                    this.setOk(false);
                    this.setMessage("Invalid token: " + this.getToken());
                    return this;
                } else if (this.getLayout() == null || this.getLayout().isEmpty()) {
                    this.setOk(false);
                    this.setMessage("Invalid layout: " + this.getLayout());
                    return this;
                } else {
                    this.setOk(true);
                    return buildParameters(this.endpoint, this.layout, null,
                            this.limit, this.offset, this.fmSort, this.fmPortal, this.fmScript);
                }
            case FINDRECORDS:
                this.setOk(true);
                this.setEndpoint(endpoint + "/layouts/" + layout + "/_find");
                return buildParameters(this.endpoint, this.layout, this.fmFind,
                        this.limit, this.offset, this.fmSort, this.fmPortal, this.fmScript);

            default:
                return null;
        }
    }

    /**
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param layout   the layout from where to retrieve the records
     * @param limit    the maximum number of records that should be returned
     * @param offset   the record number of the first record in the range of records
     * @param fmSort   an ArrayList of sort parameter objects
     * @param fmPortal an ArrayList of portal parameter objects
     * @return the final FmRequest object
     */
    private FmRequest buildParameters(String endpoint,
                                      String layout,
                                      @Nullable FmFind fmFind,
                                      @Nullable String limit,
                                      @Nullable String offset,
                                      @Nullable FmSort fmSort,
                                      @Nullable FmPortal fmPortal,
                                      @Nullable FmScript fmScript) {
        int type;
        if ( this.getMethod().equals(GET)){
            type = 1;
        }else{
            type = 2;
        }
        /*------------------------------------------------------------------------------------------
        Build the URL parameters (limit/offset, sort, portal, and script params).
        ------------------------------------------------------------------------------------------*/
        String params;
        ArrayList<String> paramsArray = new ArrayList<>();
        /*------------------------------------------------------------------------------------------
        Build the limit parameter
        ------------------------------------------------------------------------------------------*/
        if ( limit != null) {
            String limitParams = getLimitString(limit, type);
            if (!limitParams.equals("")) {
                paramsArray.add(limitParams);
            }
        }
        /*------------------------------------------------------------------------------------------
        Build the offset parameter
        ------------------------------------------------------------------------------------------*/
        if ( offset != null) {
            String offsetParams = getOffsetString(offset, type);
            if (!offsetParams.equals("")) {
                paramsArray.add(offsetParams);
            }
        }
        /*------------------------------------------------------------------------------------------
        Build the sort parameters
        ------------------------------------------------------------------------------------------*/
        if ( fmSort != null) {
            String sortParams = getSortString(fmSort, type);
            if (!sortParams.equals("")) {
                paramsArray.add(sortParams);
            }
        }
        /*------------------------------------------------------------------------------------------
        Build the portal parameters
        ------------------------------------------------------------------------------------------*/
        if ( fmPortal != null) {
            String portalParams = getPortalString(fmPortal, type);
            if (!portalParams.equals("")) {
                paramsArray.add(portalParams);
            }
        }
        /*------------------------------------------------------------------------------------------
        Build the script parameters
        ------------------------------------------------------------------------------------------*/
        if ( fmScript != null) {
            String scriptParams = getScriptString(fmScript, type);
            if (!scriptParams.equals("")) {
                paramsArray.add(scriptParams);
            }
        }
        /*------------------------------------------------------------------------------------------
        Build the query parameters
        ------------------------------------------------------------------------------------------*/
        String queryParams = getQueryString(fmFind);
        if ( !queryParams.equals("")){
            paramsArray.add(queryParams);
        }
        /*------------------------------------------------------------------------------------------
        Build the final parameters string
        ------------------------------------------------------------------------------------------*/
        if ( this.fmMethod.equals(GETRECORDS) ){
            params = "?" + android.text.TextUtils.join("&", paramsArray);
            this.setEndpoint(endpoint + "/layouts/" + layout + "/records" + params);
        } else{
            params = "{" + android.text.TextUtils.join(",", paramsArray) + "}";
            this.setBody(params);
        }
        return this;
    }

    private String getLimitString(String limit, int type) {
        if (limit == null) {
            return "";
        }
        switch (type) {
            case 1:
                return ("_limit=" + limit);
            case 2:
                return ("\"limit\":\"" + limit + "\"");
            default: return "";
        }
    }

    private String getOffsetString(String offset, int type) {
        if (offset == null) {
            return "";
        }
        switch (type) {
            case 1:
                return ("_offset=" + offset);
            case 2:
                return ("\"offset\":\"" + offset + "\"");
            default: return "";
        }
    }

    /**
     * getSortString
     *
     * @param fmSort an FmSort object containing Sort objects
     * @param type   1 for url and 2 for body
     * @return a string with the portal parameters for type 1 (Url) or type 2 (body)
     */
    private String getSortString(FmSort fmSort, int type) {
        if (fmSort == null) {
            return "";
        }
        ArrayList<Sort> sortParams = fmSort.getSortParams();
        if (sortParams == null) {
            return "";
        }
        int countSortParams = sortParams.size();
        String[] sortParamsArray = new String[countSortParams];
        int i = 0;
        while (countSortParams > i) {
            Sort sort = sortParams.get(i);
            String fieldName = sort.getFieldName();
            String sortOrder = sort.getSortOrder();
            sortParamsArray[i] = "{\"fieldName\":" + "\"" + fieldName + "\",\"sortOrder\":" + "\"" + sortOrder + "\"}";
            i++;
        }
        if (type == 1) {
            return "_sort=[" + android.text.TextUtils.join(",", sortParamsArray) + "]";
        } else {
            return "\"sort\":[" + android.text.TextUtils.join(",", sortParamsArray) + "]";
        }
    }

    private String getPortalString(FmPortal fmPortal, int type) {
        if (fmPortal == null) {
            return "";
        }
        ArrayList<Portal> portalParams = fmPortal.getPortalParams();
        if (portalParams == null) {
            return "";
        }
        StringBuilder portalOptionalParams = new StringBuilder();
        int countPortalParams = portalParams.size();
        String[] portalNames = new String[countPortalParams];
        int i = 0;
        while (countPortalParams > i) {
            Portal portal = portalParams.get(i);
            String portalName = portal.getName();
            String portalLimit = portal.getLimit();
            String portalOffset = portal.getOffset();
            portalNames[i] = "\"" + portalName + "\"";
            // TODO: This is type 1 concatenation, need to develop type 2
            if (portalLimit != null) {
                portalOptionalParams = portalOptionalParams.append("&_limit.").append(portalName).append("=").append(portalLimit);
            }
            if (portalOffset != null) {
                portalOptionalParams = portalOptionalParams.append("&_offset.").append(portalName).append("=").append(portalOffset);
            }
            i++;
        }
        if (type == 1) {
            return "portal=[" + android.text.TextUtils.join(",", portalNames) + "]" + portalOptionalParams;
        } else {
            return "\"portal\":[" + android.text.TextUtils.join(",", portalNames) + "]";
        }
    }

    private String getScriptString(FmScript fmScript, int type) {
        if (fmScript == null) {
            return "";
        }
        StringBuilder scriptParams = new StringBuilder();
        String script = fmScript.getScript();
        String scriptParam = fmScript.getScriptParam();
        String preRequest = fmScript.getPreRequest();
        String preRequestParam = fmScript.getPreRequestParam();
        String preSort = fmScript.getPreSort();
        String preSortParam = fmScript.getPreSortParam();
        String layoutResponse = fmScript.getLayoutReponse();
        if (script != null) {
            scriptParams = scriptParams.append("&script").append("=").append(Uri.encode(script));
        }
        if (scriptParam != null) {
            scriptParams = scriptParams.append("&script.param").append("=").append(Uri.encode(scriptParam));
        }
        if (preRequest != null) {
            scriptParams = scriptParams.append("&script.prerequest").append("=").append(Uri.encode(preRequest));
        }
        if (preRequestParam != null) {
            scriptParams = scriptParams.append("&script.prerequest.param").append("=").append(Uri.encode(preRequestParam));
        }
        if (preSort != null) {
            scriptParams = scriptParams.append("&script.presort").append("=").append(Uri.encode(preSort));
        }
        if (preSortParam != null) {
            scriptParams = scriptParams.append("&script.presort.param").append("=").append(Uri.encode(preSortParam));
        }
        if (layoutResponse != null) {
            scriptParams = scriptParams.append("&layout.response").append("=").append(Uri.encode(layoutResponse));
        }
        return scriptParams.toString();
    }

    private String getQueryString(FmFind fmFind){
        if ( fmFind == null ){
            return "";
        }
        int countRequests = fmFind.countFindRequests();
        if ( countRequests == 0 ){
            return "";
        }
        int i = 0;
        StringBuilder string = new StringBuilder();
        while ( i < countRequests){
            FmFind.FindRequest findRequest = fmFind.get(i);
            if ( i == 0){
                string = string.append(findRequest.getString());
            }else{
                string = string.append(",").append(findRequest.getString());
            }
            i++;
        }
        return "\"query\":[" + string + "]";
    }

    /* ---------------------------------------------------------------------------------------------
    Public getters
    ----------------------------------------------------------------------------------------------*/
    public String getToken() {
        return this.token;
    }

    public Boolean isOk() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    /* ---------------------------------------------------------------------------------------------
    Public setters
    ----------------------------------------------------------------------------------------------*/
    public FmRequest setLimit(int limit) {
        this.limit = String.valueOf(limit);
        return this;
    }

    public FmRequest setOffset(int offset) {
        this.offset = String.valueOf(offset);
        return this;
    }

    public FmRequest disableSSL(boolean disableSSL) {
        this.disableSSL = disableSSL;
        return this;
    }

    public FmRequest setPortalParams(FmPortal fmPortal) {
        this.fmPortal = fmPortal;
        return this;
    }

    public FmRequest setSortParams(FmSort fmSort) {
        this.fmSort = fmSort;
        return this;
    }

    public FmRequest setScriptPrams(FmScript fmScript) {
        this.fmScript = fmScript;
        return this;
    }


    /* ---------------------------------------------------------------------------------------------
    Private setters
    ----------------------------------------------------------------------------------------------*/
    private void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    private void setMethod(String method) {
        this.method = method;
    }

    private void setFmFind(FmFind fmFind) {
        this.fmFind = fmFind;
    }

    private void setBody(String body) {
        // TODO: Convert to JSON string
        this.body = body;
    }

    private void setAuth(String auth) {
        this.auth = auth;
    }

    private void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    private void setToken(String token) {
        this.token = token;
    }

    private void setOk(Boolean success) {
        this.success = success;
    }

    private void setFmMethod(String fmMethod) {
        this.fmMethod = fmMethod;
    }

    private void setLayout(String layout) {
        this.layout = layout;
    }

    private void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    /* ---------------------------------------------------------------------------------------------
        Package private getters
        ----------------------------------------------------------------------------------------------*/
    String getEndpoint() {
        return endpoint;
    }

    String getMethod() {
        return method;
    }

    int getRecordId() {
        return recordId;
    }

    String getBody() {
        return body;
    }

    String getAuth() {
        return auth;
    }

    String getAccountName() {
        return accountName;
    }

    String getPassword() {
        return password;
    }

    boolean isSSLDisabled() {
        return disableSSL;
    }

    String getLayout() {
        return layout;
    }
}
