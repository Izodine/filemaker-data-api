package com.joselopezrosario.fma;

public class FmaParamSort {
    private static String ASC = "ascend";
    private static String DESC = "descend";
    private String fieldName;
    private String sortOrder;

    public FmaParamSort(String fieldName) {
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
    public FmaParamSort ascend() {
        this.sortOrder = ASC;
        return this;
    }

    public FmaParamSort descend() {
        this.sortOrder = DESC;
        return this;
    }
    /* ---------------------------------------------------------------------------------------------
    Private setters
    ----------------------------------------------------------------------------------------------*/
    private FmaParamSort setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }
}
