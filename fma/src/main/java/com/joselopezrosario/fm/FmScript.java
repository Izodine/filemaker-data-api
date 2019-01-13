package com.joselopezrosario.fm;

public class FmScript {
    private String script;
    private String scriptParam;
    private String preRequest;
    private String preRequestParam;
    private String preSort;
    private String preSortParam;
    private String layoutReponse;

    public FmScript() {
    }

    /*----------------------------------------------------------------------------------------------
    Public setters
    ----------------------------------------------------------------------------------------------*/

    public FmScript setScript(String script) {
        this.script = script;
        return this;
    }

    public FmScript setScriptParam(String scriptParam) {
        this.scriptParam = scriptParam;
        return this;
    }

    public FmScript setPreRequest(String preRequest) {
        this.preRequest = preRequest;
        return this;
    }

    public FmScript setPreRequestParam(String preRequestParam) {
        this.preRequestParam = preRequestParam;
        return this;
    }

    public FmScript setPreSort(String preSort) {
        this.preSort = preSort;
        return this;
    }

    public FmScript setPreSortParam(String preSortParam) {
        this.preSortParam = preSortParam;
        return this;
    }

    public FmScript setLayoutReponse(String layoutReponse) {
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
