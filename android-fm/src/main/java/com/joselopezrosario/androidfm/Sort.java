package com.joselopezrosario.androidfm;

class Sort {
    private String fieldName;
    private String sortOrder;

    Sort(String fieldName) {
        this.setFieldName(fieldName);
        this.ascend();
    }

    /* ---------------------------------------------------------------------------------------------
    Private setters
    ----------------------------------------------------------------------------------------------*/
    Sort setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    Sort ascend() {
        this.sortOrder = "ascend";
        return this;
    }

    Sort descend() {
        this.sortOrder = "descend";
        return this;
    }
        /* ---------------------------------------------------------------------------------------------
        Public getters
        ----------------------------------------------------------------------------------------------*/

    String getFieldName() {
        return fieldName;
    }

    String getSortOrder() {
        return sortOrder;
    }
}
