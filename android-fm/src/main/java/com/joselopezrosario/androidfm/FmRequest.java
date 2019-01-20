package com.joselopezrosario.androidfm;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class FmRequest {
    private static final String EMPTY_BODY = "{}";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private final String GET = "GET";
    private final String BASIC = "Basic";
    private final String BEARER = "Bearer";
    private static final String LOGIN = "LOGIN";
    private static final String LOGINOAUTH = "LOGINOAUTH";
    private static final String LOGOUT = "LOGOUT";
    private static final String GETRECORDS = "GETRECORDS";
    private static final String GETRECORD = "GETRECORD";
    private static final String FINDRECORDS = "FINDRECORDS";
    private static final String CREATE = "CREATE";
    private static final String SESSIONS = "/sessions";
    private static final String LAYOUTS = "/layouts/";
    private static final String RECORDS = "/records";
    private static final String _FIND = "/_find";
    private boolean disableSSL;
    private String fmMethod;
    private String endpoint;
    private int recordId;
    private String httpMethod;
    private String auth;
    private String accountName;
    private String oAuthRequestId;
    private String oAuthIdentifier;
    private String password;
    private String layout;
    private String token;
    private String limit;
    private String offset;
    private FmPortal fmPortal;
    private FmSort fmSort;
    private FmScript fmScript;
    private FmFind fmFind;
    private FmEdit fmEdit;
    private String body;
    private String message;

    public FmRequest() {
        this.disableSSL(false);
    }
    // TODO: Decide if the final URL will be created here or in the build httpMethod

    /**
     * login
     *
     * @param url      the FileMaker Data API url (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param account  the FileMaker account with fmrest extended privileges
     * @param password the FileMaker account's password
     * @return an FmRequest object
     */
    public FmRequest login(@NonNull String url, @NonNull String account, @NonNull String password) {
        this.setEndpoint(url);
        this.setAccountName(account);
        this.setPassword(password);
        this.setFmMethod(LOGIN);
        this.setHttpMethod(POST);
        this.setBody(EMPTY_BODY);
        this.setAuth(BASIC);
        return this;
    }

    /**
     * loginOAuth
     *
     * @param url        the FileMaker Data API url (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param requestId  the request id header value returned from /ouath/gettoauthurl
     * @param identifier the oauth identifier
     * @return
     */
    public FmRequest loginOAuth(@NonNull String url, @NonNull String requestId, @NonNull String identifier) {
        String endpoint = url + "/" + SESSIONS;
        this.setFmMethod(LOGINOAUTH);
        this.setEndpoint(endpoint);
        this.setHttpMethod(POST);
        this.setoAuthRequestId(requestId);
        this.setoAuthIdentifier(identifier);
        this.setBody(EMPTY_BODY);
        this.setAuth(BASIC);
        return this;
    }

    /**
     * logout
     *
     * @param url   the FileMaker Data API url (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token the token returned in the response by executing the login request
     * @return an FmRequest object
     */
    public FmRequest logout(@NonNull String url, @NonNull String token) {
        String endpoint = url + "/" + SESSIONS + "/" + token;
        this.setFmMethod(LOGOUT);
        this.setEndpoint(endpoint);
        this.setHttpMethod(DELETE);
        this.setToken(token);
        this.setBody(EMPTY_BODY);
        this.setAuth(BEARER);
        return this;
    }

    /**
     * getRecords
     *
     * @param url    the FileMaker Data API url (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token  the token returned in the response by executing the login request
     * @param layout the layout from where to retrieve the records
     * @return an FmRequest object
     */
    public FmRequest getRecords(@NonNull String url, @NonNull String token, @NonNull String layout) {
        this.setEndpoint(url);
        this.setToken(token);
        this.setLayout(layout);
        this.setFmMethod(GETRECORDS);
        this.setHttpMethod(GET);
        this.setAuth(BEARER);
        return this;
    }

    /**
     * getRecords
     *
     * @param url    the FileMaker Data API url (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token  the token returned in the response by executing the login request
     * @param layout the layout from where to retrieve the records
     * @return an FmRequest object
     */
    public FmRequest getRecord(@NonNull String url, @NonNull String token, @NonNull String layout, @NonNull int recordId) {
        this.setFmMethod(GETRECORD);
        this.setRecordId(recordId);
        this.setEndpoint(url);
        this.setToken(token);
        this.setLayout(layout);
        this.setHttpMethod(GET);
        this.setAuth(BEARER);
        return this;
    }

    /**
     * FindRecords
     *
     * @param url    the FileMaker Data API url (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token  the token returned in the response by executing the login request
     * @param layout the layout from where to retrieve the records
     * @param fmFind an FmFind object containing the find request(s)
     * @return an FmRequest object
     */
    public FmRequest findRecords(@NonNull String url, @NonNull String token, @NonNull String layout, @NonNull FmFind fmFind) {
        this.setEndpoint(url);
        this.setToken(token);
        this.setLayout(layout);
        this.setFmFind(fmFind);
        this.setFmMethod(FINDRECORDS);
        this.setHttpMethod(POST);
        this.setAuth(BEARER);
        return this;
    }

    /**
     * create
     * Create a record with values
     *
     * @param url    the FileMaker Data API url (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token  the token returned in the response by executing the login request
     * @param layout the layout from where to create the record
     * @param fmEdit an FmEdit object containing Value field name value pair objects
     * @return an FmRequest object
     */
    public FmRequest create(@NonNull String url, @NonNull String token, @NonNull String layout, @NonNull FmEdit fmEdit) {
        this.setEndpoint(url);
        this.setToken(token);
        this.setLayout(layout);
        this.setEditParams(fmEdit);
        this.setFmMethod(CREATE);
        this.setHttpMethod(POST);
        this.setAuth(BEARER);
        return this;
    }

    /**
     * create
     * Create a blank record
     *
     * @param url    the FileMaker Data API url (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token  the token returned in the response by executing the login request
     * @param layout the layout from where to create the record
     * @return an FmRequest object
     */
    public FmRequest create(@NonNull String url, @NonNull String token, @NonNull String layout) {
        this.setEndpoint(url);
        this.setToken(token);
        this.setLayout(layout);
        this.setEditParams(new FmEdit());
        this.setFmMethod(CREATE);
        this.setHttpMethod(POST);
        this.setAuth(BEARER);
        return this;
    }

    /**
     * delete
     * Delete a record by its id
     * @param url    the FileMaker Data API url (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token  the token returned in the response by executing the login request
     * @param layout the layout from where to create the record
     * @param recordId the record's id
     * @return an FmRequest object
     */
    public FmRequest delete(@NonNull String url, @NonNull String token, @NonNull String layout, int recordId) {
        this.setEndpoint(url);
        this.setToken(token);
        this.setLayout(layout);
        this.setRecordId(recordId);
        this.setFmMethod(DELETE);
        this.setHttpMethod(DELETE);
        this.setAuth(BEARER);
        return this;

    }

    /**
     * build
     * Build the final end point and add any parameters if needed
     *
     * @return an FmRequest object
     */
    public FmRequest build() {
        switch (fmMethod) {
            case LOGIN:
                this.setEndpoint(this.getEndpoint() + SESSIONS);
                return this;
            case LOGINOAUTH:
                return this;
            case LOGOUT:
                return this;
            case GETRECORDS:
                this.setEndpoint(this.getEndpoint() + LAYOUTS + layout + RECORDS);
                return buildParameters(null, this.limit, this.offset, this.fmSort, this.fmPortal, this.fmScript, null);
            case GETRECORD:
                this.setEndpoint(this.getEndpoint() + LAYOUTS + layout + RECORDS + "/" + recordId);
                return this; case FINDRECORDS:
                this.setEndpoint(endpoint + LAYOUTS + layout + _FIND);
                return buildParameters(this.fmFind, this.limit, this.offset, this.fmSort, this.fmPortal, this.fmScript, null);
            case CREATE:
                this.setEndpoint(endpoint + LAYOUTS + layout + RECORDS);
                return buildParameters(this.fmFind, this.limit, this.offset, this.fmSort, this.fmPortal, this.fmScript, this.fmEdit);
            case DELETE:
                this.setEndpoint(endpoint + LAYOUTS + layout + RECORDS + "/" + this.recordId);
                return buildParameters(null, null, null, null, null, this.fmScript, null);
            default:
                return null;
        }

    }

    /**
     * buildParameters
     * A httpMethod to create the final parameters. Either in URL or JSON body format
     *
     * @param limit    the maximum number of records that should be returned
     * @param offset   the record number of the first record in the range of records
     * @param fmSort   an ArrayList of sort parameter objects
     * @param fmPortal an ArrayList of portal parameter objects
     * @return the final FmRequest object
     */
    private FmRequest buildParameters(@Nullable FmFind fmFind,
                                      @Nullable String limit,
                                      @Nullable String offset,
                                      @Nullable FmSort fmSort,
                                      @Nullable FmPortal fmPortal,
                                      @Nullable FmScript fmScript,
                                      @Nullable FmEdit fmEdit) {
        int type;
        if (this.getFmMethod().equals(GETRECORDS)) {
            type = 1;
        } else {
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
        if (limit != null) {
            String limitParams = getLimitString(limit, type);
            if (!limitParams.isEmpty()) {
                paramsArray.add(limitParams);
            }
        }
        /*------------------------------------------------------------------------------------------
        Build the offset parameter
        ------------------------------------------------------------------------------------------*/
        if (offset != null) {
            String offsetParams = getOffsetString(offset, type);
            if (!offsetParams.isEmpty()) {
                paramsArray.add(offsetParams);
            }
        }
        /*------------------------------------------------------------------------------------------
        Build the sort parameters
        ------------------------------------------------------------------------------------------*/
        if (fmSort != null) {
            String sortParams = getSortString(fmSort, type);
            if (!sortParams.isEmpty()) {
                paramsArray.add(sortParams);
            }
        }
        /*------------------------------------------------------------------------------------------
        Build the portal parameters
        ------------------------------------------------------------------------------------------*/
        if (fmPortal != null) {
            String portalParams = getPortalString(fmPortal, type);
            if (!portalParams.isEmpty()) {
                paramsArray.add(portalParams);
            }
        }
        /*------------------------------------------------------------------------------------------
        Build the script parameters
        ------------------------------------------------------------------------------------------*/
        if (fmScript != null) {
            String scriptParams = getScriptString(fmScript, type);
            if (!scriptParams.isEmpty()) {
                paramsArray.add(scriptParams);
            }
        }
        /*------------------------------------------------------------------------------------------
        Build the query parameters
        ------------------------------------------------------------------------------------------*/
        String queryParams = getQueryString(fmFind);
        if (!queryParams.isEmpty()) {
            paramsArray.add(queryParams);
        }
        /*------------------------------------------------------------------------------------------
        Build the edit parameters
        ------------------------------------------------------------------------------------------*/
        String editParams = getEditString(fmEdit);
        if (!editParams.isEmpty()) {
            paramsArray.add(editParams);
        }
        /*------------------------------------------------------------------------------------------
        Build the final parameters string and append to url or set to body accordingly
        ------------------------------------------------------------------------------------------*/
        switch (this.fmMethod) {
            case GETRECORDS:
                params = "?" + android.text.TextUtils.join("&", paramsArray);
                this.setEndpoint(this.getEndpoint() + params);
                return this;
            case FINDRECORDS:
                params = "{" + android.text.TextUtils.join(",", paramsArray) + "}";
                this.setBody(params);
                return this;
            case CREATE:
                params = "{" + android.text.TextUtils.join(",", paramsArray) + "}";
                this.setBody(params);
                return this;
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
            default:
                return "";
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
            default:
                return "";
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

    private String getQueryString(FmFind fmFind) {
        if (fmFind == null) {
            return "";
        }
        int countRequests = fmFind.countFindRequests();
        if (countRequests == 0) {
            return "";
        }
        int i = 0;
        StringBuilder string = new StringBuilder();
        while (i < countRequests) {
            FmFind.FindRequest findRequest = fmFind.get(i);
            if (i == 0) {
                string = string.append(findRequest.getString());
            } else {
                string = string.append(",").append(findRequest.getString());
            }
            i++;
        }
        return "\"query\":[" + string + "]";
    }

    private String getEditString(FmEdit fmEdit) {
        if (fmEdit == null) {
            return "";
        }
        return "\"fieldData\":" + fmEdit.getString() + "";
    }

    /* ---------------------------------------------------------------------------------------------
    Public getters
    ----------------------------------------------------------------------------------------------*/
    public String getToken() {
        return this.token;
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

    public FmRequest setEditParams(FmEdit fmEdit) {
        this.fmEdit = fmEdit;
        return this;
    }


    /* ---------------------------------------------------------------------------------------------
    Private setters
    ----------------------------------------------------------------------------------------------*/
    private void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    private void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    private void setFmFind(FmFind fmFind) {
        this.fmFind = fmFind;
    }

    private void setBody(String body) {
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

    String getHttpMethod() {
        return httpMethod;
    }

    String getFmMethod() {
        return fmMethod;
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

    String getoAuthRequestId() {
        return oAuthRequestId;
    }

    void setoAuthRequestId(String oAuthRequestId) {
        this.oAuthRequestId = oAuthRequestId;
    }

    public String getoAuthIdentifier() {
        return oAuthIdentifier;
    }

    public void setoAuthIdentifier(String oAuthIdentifier) {
        this.oAuthIdentifier = oAuthIdentifier;
    }

    boolean isSSLDisabled() {
        return disableSSL;
    }

    String getLayout() {
        return layout;
    }
}
