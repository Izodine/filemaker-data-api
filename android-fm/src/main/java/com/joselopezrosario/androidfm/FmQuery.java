package com.joselopezrosario.androidfm;

import java.util.ArrayList;

public class FmQuery {
    private int countFoundRequests;
    private ArrayList<FindRequest> findRequests;

    /**
     * FmQuery
     * Create a new FmQuery object and initialize the findRequests
     */
    public FmQuery() {
        this.findRequests = new ArrayList<>();
    }

    /**
     * newFindRequest
     *
     * @return returns a new find request object
     */
    public FmQuery newFindRequest() {
        FindRequest findRequest = new FindRequest();
        this.findRequests.add(findRequest);
        this.countFoundRequests = this.findRequests.size();
        return this;
    }

    /**
     * set
     *
     * @param fieldName  the field name
     * @param fieldValue the field value
     * @return an FmQuery object with the new FindRequest
     */
    public FmQuery set(String fieldName, String fieldValue) {
        FieldNameValuePair fieldNameValuePair = new FieldNameValuePair(fieldName, fieldValue);
        this.findRequests.get(this.countFoundRequests - 1).set(fieldNameValuePair);
        return this;
    }

    public void omit() {
        this.findRequests.get(this.countFoundRequests - 1).Omit();
    }

    public int countFindRequests() {
        return this.countFoundRequests;
    }

    public int countQueries() {
        if (countFoundRequests == 0) {
            return 0;
        }
        int count = 0;
        int i = 0;
        while (i < this.findRequests.size()) {
            count = count + findRequests.get(i).countQueries;
            i++;
        }
        return count;
    }


    /**
     * FindRequest
     * Inner class to handle find requests
     */
    private static class FindRequest {
        int countQueries;
        private ArrayList<FieldNameValuePair> queries;
        private boolean omit = false;

        private FindRequest() {
            if (this.queries == null) {
                this.queries = new ArrayList<>();
            }
        }

        private FindRequest set(FieldNameValuePair fieldNameValuePair) {
            this.countQueries = this.countQueries + 1;
            this.queries.add(fieldNameValuePair);
            return this;
        }

        private void Omit() {
            this.omit = true;
        }
    }

    /**
     * FieldNameValuePair
     * Inner class to handle the field name value pair objects in a find request
     */
    private class FieldNameValuePair {
        private String fieldName;
        private String fieldValue;

        private FieldNameValuePair(String fieldName, String fieldValue) {
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }

        private String getString() {
            return getFieldName() + ":" + getFieldValue();
        }

        private String getFieldName() {
            String result;
            if (this.fieldName == null) {
                result = "\"\"";
            } else {
                result = "\"" + this.fieldName + "\"";
            }
            return result;
        }

        private String getFieldValue() {
            String result;
            if (this.fieldValue == null) {
                result = "\"\"";
            } else {
                result = "\"" + this.fieldValue + "\"";
            }
            return result;
        }
    }
}
