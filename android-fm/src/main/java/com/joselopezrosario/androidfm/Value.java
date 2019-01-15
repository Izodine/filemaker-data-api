package com.joselopezrosario.androidfm;

/**
 * Value
 * A class to manage field name value pair objects for FmFind and FmEdit objects
 */
class Value {
    private String fieldName;
    private String fieldValue;

    Value(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * getFieldName
     * @return a JSON compatible field name string
     */
    String getFieldName() {
        String result;
        if (this.fieldName == null) {
            result = "\"\"";
        } else {
            result = "\"" + this.fieldName + "\"";
        }
        return result;
    }

    /**
     * getFieldValue
     * @return a JSON compatible field value string
     */
    String getFieldValue() {
        String result;
        if (this.fieldValue == null) {
            result = "\"\"";
        } else {
            result = "\"" + this.fieldValue + "\"";
        }
        return result;
    }
}