package com.joselopezrosario.fma;


import android.support.annotation.Nullable;

import java.util.ArrayList;

public class FmaRequest {
    public static final String EMPTY_BODY = "{}";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String GET = "GET";
    public static final String BASIC = "Basic";
    public static final String BEARER = "Bearer";
    private static final String LOGIN = "LOGIN";
    private static final String LOGOUT = "LOGOUT";
    private static final String GETRECORDS = "GETRECORDS";
    private boolean disableSSL;
    private String fmMethod;
    private String endpoint;
    private String method;
    private String body;
    private String auth;
    private String accountName;
    private String password;
    private String layout;
    private String token;
    private String limit;
    private String offset;
    private ArrayList<FmaParamPortal> fmaParamPortalParams;
    private ArrayList<FmaParamSort> fmaParamSortParams;
    private String message;
    private Boolean success;

    public FmaRequest() {
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
     * @return an FmaRequest object
     */
    public FmaRequest login(String endpoint, String account, String password) {
        if (endpoint == null || account == null || password == null) {
            this.setOk(false);
            return this;
        }
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
     * @return an FmaRequest object
     */
    public FmaRequest logout(String endpoint, String token) {
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
     * @return an FmaRequest object
     */
    public FmaRequest getRecords(String endpoint, String token, String layout) {
        this.setFmMethod(GETRECORDS);
        this.setEndpoint(endpoint);
        this.setToken(token);
        this.setLayout(layout);
        this.setToken(token);
        this.setMethod(GET);
        this.setBody(EMPTY_BODY);
        this.setAuth(BEARER);
        return this;
    }


    public FmaRequest build() {
        // TODO: Final validation
        this.setOk(true);
        if (this.fmMethod.equals(LOGIN) || this.fmMethod.equals(LOGOUT)) {
            return this;
        }
        if (this.method.equals(GET)) {
            return buildGet(this.endpoint, this.layout, this.limit, this.offset, this.fmaParamSortParams, this.fmaParamPortalParams);
        } else {
            // TODO: Handle POST, PATCH, DELETE
            return null;
        }
    }

    /**
     * @param endpoint                 the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param layout                   the layout from where to retrieve the records
     * @param limit                    the maximum number of records that should be returned
     * @param offset                   the record number of the first record in the range of records
     * @param fmaParamSortArrayList   an ArrayList of sort parameter objects
     * @param fmaParamPortalArrayList an ArrayList of portal parameter objects
     * @return
     */
    private FmaRequest buildGet(String endpoint, String layout,
                                @Nullable String limit,
                                @Nullable String offset,
                                @Nullable ArrayList<FmaParamSort> fmaParamSortArrayList,
                                @Nullable ArrayList<FmaParamPortal> fmaParamPortalArrayList) {
        /*------------------------------------------------------------------------------------------
        Build the URL parameters (limit/offset, sort, portal, and script params).
        ------------------------------------------------------------------------------------------*/
        String params = "";
        /*------------------------------------------------------------------------------------------
        Handle limit and offset
        ------------------------------------------------------------------------------------------*/
        ArrayList<String> paramsArray = new ArrayList<>();
        if (limit != null) {
            paramsArray.add("_limit=" + limit);
        }
        if (offset != null) {
            paramsArray.add("_offset=" + offset);
        }
        /*------------------------------------------------------------------------------------------
        Build the sort parameters
        ------------------------------------------------------------------------------------------*/
        String sortParams = "";
        if (fmaParamSortArrayList != null) {
            int countSortParams = fmaParamSortArrayList.size();
            String[] sortParamsArray = new String[countSortParams];
            int i = 0;
            while (countSortParams > i) {
                FmaParamSort fmaSortParam = fmaParamSortArrayList.get(i);
                String fieldName = fmaSortParam.getFieldName();
                String sortOrder = fmaSortParam.getSortOrder();
                sortParamsArray[i] = "{\"fieldName\":" + "\"" + fieldName + "\",\"sortOrder\":" + "\"" + sortOrder + "\"}";
                i++;
            }
            sortParams = "_sort=[" + android.text.TextUtils.join(",", sortParamsArray) + "]";
            paramsArray.add(sortParams);
        }
        // &_sort=[{ "fieldName": "field-name", "sortOrder": "sort-order" }, { ... }]
        /*------------------------------------------------------------------------------------------
        Build the portal parameters
        ------------------------------------------------------------------------------------------*/
        String portalParams = "";
        if (fmaParamPortalArrayList != null) {
            StringBuilder portalOptionalParams = new StringBuilder();
            int countPortalParams = fmaParamPortalArrayList.size();
            String[] portalNames = new String[countPortalParams];
            int i = 0;
            while (countPortalParams > i) {
                FmaParamPortal fmaParamPortal = fmaParamPortalArrayList.get(i);
                String portalName = fmaParamPortal.getName();
                String portalLimit = fmaParamPortal.getLimit();
                String portalOffset = fmaParamPortal.getOffset();
                portalNames[i] = "\"" + portalName + "\"";
                if (portalLimit != null) {
                    portalOptionalParams = portalOptionalParams.append("&_limit.").append(portalName).append("=").append(portalLimit);
                }
                if (portalOffset != null) {
                    portalOptionalParams = portalOptionalParams.append("&_offset.").append(portalName).append("=").append(portalOffset);
                }
                i++;
            }
            portalParams = "portal=[" + android.text.TextUtils.join(",", portalNames) + "]" + portalOptionalParams;
            paramsArray.add(portalParams);
        }
        if (paramsArray.size() > 0) {
            params = "?" + android.text.TextUtils.join("&", paramsArray);
        }
        /*------------------------------------------------------------------------------------------
        Finalize the endpoint url
        ------------------------------------------------------------------------------------------*/
        if (this.fmMethod.equals(GETRECORDS)) {
            this.setEndpoint(endpoint + "/layouts/" + layout + "/records" + params);
        }
        return this;
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

    /* ---------------------------------------------------------------------------------------------
    Public setters
    ----------------------------------------------------------------------------------------------*/
    public FmaRequest setLimit(int limit) {
        this.limit = String.valueOf(limit);
        return this;
    }

    public FmaRequest setOffset(int offset) {
        this.offset = String.valueOf(offset);
        return this;
    }

    public FmaRequest disableSSL(boolean disableSSL) {
        this.disableSSL = disableSSL;
        return this;
    }

    public FmaRequest setPortalParams(ArrayList<FmaParamPortal> fmaParamPortalArrayList) {
        this.fmaParamPortalParams = fmaParamPortalArrayList;
        return this;
    }

    public FmaRequest setSortParams(ArrayList<FmaParamSort> fmaParamSortArrayList) {
        this.fmaParamSortParams = fmaParamSortArrayList;
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

    private void setOk(Boolean success) {
        this.success = success;
    }

    private void setFmMethod(String fmMethod) {
        this.fmMethod = fmMethod;
    }

    private void setLayout(String layout) {
        this.layout = layout;
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
}
