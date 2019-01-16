package com.joselopezrosario.androidfm;

/**
 * FmScript
 * A class to handle creating script parameters
 */
public class FmScript {
    private String script;
    private String scriptParam;
    private String preRequest;
    private String preRequestParam;
    private String preSort;
    private String preSortParam;
    private String layoutReponse;

    /**
     * FmScript
     * Create a new FmScript parameter object
     */
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
    Package private setters
    ----------------------------------------------------------------------------------------------*/

    String getScript() {
        return script;
    }

    String getScriptParam() {
        return scriptParam;
    }

    String getPreRequest() {
        return preRequest;
    }

    String getPreRequestParam() {
        return preRequestParam;
    }

    String getPreSort() {
        return preSort;
    }

    String getPreSortParam() {
        return preSortParam;
    }

    String getLayoutReponse() {
        return layoutReponse;
    }
}
