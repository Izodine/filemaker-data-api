package com.joselopezrosario.fm.options;

public class FmSort {
    private static String ASC = "ascend";
    private static String DESC = "descend";
    private String fieldName;
    private String sortOrder;

    public FmSort(String fieldName) {
        this.setFieldName(fieldName);
        this.ascend();
    }

    /* ---------------------------------------------------------------------------------------------
    Public getters
    ----------------------------------------------------------------------------------------------*/
    public String getFieldName() {
        return this.fieldName;
    }

    public String getSortOrder() {
        return this.sortOrder;
    }

    /* ---------------------------------------------------------------------------------------------
    Public setters
    ----------------------------------------------------------------------------------------------*/
    public FmSort ascend() {
        this.sortOrder = ASC;
        return this;
    }

    public FmSort descend() {
        this.sortOrder = DESC;
        return this;
    }
    /* ---------------------------------------------------------------------------------------------
    Private setters
    ----------------------------------------------------------------------------------------------*/
    private FmSort setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }
}
