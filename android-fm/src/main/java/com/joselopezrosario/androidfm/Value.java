package com.joselopezrosario.androidfm;

/**
 * FieldNameValuePair
 * Inner class to handle the field name value pair objects in a find request
 */
class Value {
    private String fieldName;
    private String fieldValue;

    Value(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    private String getString() {
        return getFieldName() + ":" + getFieldValue();
    }

    String getFieldName() {
        String result;
        if (this.fieldName == null) {
            result = "\"\"";
        } else {
            result = "\"" + this.fieldName + "\"";
        }
        return result;
    }

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