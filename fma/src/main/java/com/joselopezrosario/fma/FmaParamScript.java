package com.joselopezrosario.fma;

public class FmaParamScript {
    private String script;
    private String scriptParam;
    private String preRequest;
    private String preRequestParam;
    private String preSort;
    private String preSortParam;
    private String layoutReponse;

    public FmaParamScript() {
    }

    /*----------------------------------------------------------------------------------------------
    Public setters
    ----------------------------------------------------------------------------------------------*/

    public FmaParamScript setScript(String script) {
        this.script = script;
        return this;
    }

    public FmaParamScript setScriptParam(String scriptParam) {
        this.scriptParam = scriptParam;
        return this;
    }

    public FmaParamScript setPreRequest(String preRequest) {
        this.preRequest = preRequest;
        return this;
    }

    public FmaParamScript setPreRequestParam(String preRequestParam) {
        this.preRequestParam = preRequestParam;
        return this;
    }

    public FmaParamScript setPreSort(String preSort) {
        this.preSort = preSort;
        return this;
    }

    public FmaParamScript setPreSortParam(String preSortParam) {
        this.preSortParam = preSortParam;
        return this;
    }

    public FmaParamScript setLayoutReponse(String layoutReponse) {
        this.layoutReponse = layoutReponse;
        return this;
    }
    /*----------------------------------------------------------------------------------------------
    Public setters
    ----------------------------------------------------------------------------------------------*/

    public String getScript() {
        return script;
    }

    public String getScriptParam() {
        return scriptParam;
    }

    public String getPreRequest() {
        return preRequest;
    }

    public String getPreRequestParam() {
        return preRequestParam;
    }

    public String getPreSort() {
        return preSort;
    }

    public String getPreSortParam() {
        return preSortParam;
    }

    public String getLayoutReponse() {
        return layoutReponse;
    }
}
