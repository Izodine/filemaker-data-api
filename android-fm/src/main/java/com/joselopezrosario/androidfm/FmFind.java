package com.joselopezrosario.androidfm;

import java.util.ArrayList;

public class FmFind {
    private int countFoundRequests;
    private ArrayList<FindRequest> findRequests;

    /**
     * FmFind
     * Create a new FmFind object and initialize the findRequests
     */
    public FmFind() {
        this.findRequests = new ArrayList<>();
    }

    /**
     * newRequest
     *
     * @return returns a new find request object
     */
    public FmFind newRequest() {
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
     * @return an FmFind object with the new FindRequest
     */
    public FmFind set(String fieldName, String fieldValue) {
        Value value = new Value(fieldName, fieldValue);
        this.findRequests.get(this.countFoundRequests - 1).set(value);
        return this;
    }

    public FindRequest get(int index) {
        return this.findRequests.get(index);
    }


    /**
     * omit
     * Sets the omit value to true
     */
    public void omit() {
        this.findRequests.get(this.countFoundRequests - 1).omit();
    }

    /**
     * countFindRequests
     *
     * @return the number of FindRequest objects
     */
    public int countFindRequests() {
        return this.countFoundRequests;
    }

    /**
     * countQueries
     *
     * @return the number of Value query objects across all FindRequests
     */
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
    static class FindRequest {
        int countQueries;
        private ArrayList<Value> values;
        private boolean omit = false;

        private FindRequest() {
            if (this.values == null) {
                this.values = new ArrayList<>();
            }
        }

        private FindRequest set(Value values) {
            this.countQueries = this.countQueries + 1;
            this.values.add(values);
            return this;
        }

        private void omit() {
            this.omit = true;
        }


        String getString() {
            if (this.values == null) {
                return null;
            }
            int size = values.size();
            StringBuilder string = new StringBuilder();
            int i = 0;
            while (i < size) {
                Value value = values.get(i);
                if ( i == 0){
                    string = string.append(value.getFieldName()).append(":").append(value.getFieldValue());
                } else{
                    string = string.append(",").append(value.getFieldName()).append(":").append(value.getFieldValue());
                }

                i++;
            }
            if ( this.omit){
                string.append(",\"omit\":\"true\"");
            }
            return "{" + string +"}";
        }
    }
}
