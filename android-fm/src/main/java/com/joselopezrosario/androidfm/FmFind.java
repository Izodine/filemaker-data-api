package com.joselopezrosario.androidfm;

import java.util.ArrayList;

/**
 * FmFind
 * A class to handle creating the find parameters of an FmRequest (findRecords)
 */
public class FmFind {
    private int countFoundRequests;
    private ArrayList<FindRequest> findRequests;

    /**
     * FmFind
     * Create a new FmFind object and initialize its findRequests ArrayList
     */
    public FmFind() {
        this.findRequests = new ArrayList<>();
    }

    /**
     * newRequest
     * Create a new FindRequest object
     * @return returns a new find request object
     */
    public FmFind newRequest() {
        FindRequest findRequest = new FindRequest();
        this.findRequests.add(findRequest);
        this.countFoundRequests = this.findRequests.size();
        return this;
    }
    /*----------------------------------------------------------------------------------------------
    Public setters
    ----------------------------------------------------------------------------------------------*/
    /**
     * setName
     * Set the find request field values
     * @param fieldName  the field name
     * @param fieldValue the field value
     * @return an FmFind object with the new FindRequest
     */
    public FmFind set(String fieldName, String fieldValue) {
        Value value = new Value(fieldName, fieldValue);
        this.findRequests.get(this.countFoundRequests - 1).set(value);
        return this;
    }

    /**
     * omit
     * Optional method to sets the omit value to true
     */
    public FmFind omit() {
        this.findRequests.get(this.countFoundRequests - 1).omit();
        return this;
    }

    /*----------------------------------------------------------------------------------------------
    Package private getters
    ----------------------------------------------------------------------------------------------*/

    /**
     * countValues
     * Count the FindRequest objects in the ArrayList and return the number
     * @return the number of FindRequest objects
     */
    int countFindRequests() {
        return this.countFoundRequests;
    }

    /**
     * get
     * Get a FindRequest from the ArrayList by specifying its index
     * @param index the find request's index in the ArrayList
     * @return a FindRequest object
     */
    FindRequest get(int index) {
        return this.findRequests.get(index);
    }

    /**
     * countQueries
     * Count the number of Value objects across all find requests in the ArrayList
     * @return the number of Value objects across all FindRequests
     */
    int countQueries() {
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
     * ---------------------------------------------------------------------------------------------
     * FindRequest
     * An inner class to handle find requests
     ---------------------------------------------------------------------------------------------*/
    static class FindRequest {
        int countQueries;
        private ArrayList<Value> values;
        private boolean omit = false;

        /**
         * Create a FindRequest object and initialize the values.
         * The values ArrayList holds Value objects (or field name value pairs)
         */
        private FindRequest() {
            if (this.values == null) {
                this.values = new ArrayList<>();
            }
        }

        /**
         * setName
         * Set a new value object
         * @param values a Value object
         */
        private void set(Value values) {
            this.countQueries = this.countQueries + 1;
            this.values.add(values);
        }

        /**
         * omit
         * Set the FindRequest's omit field to true
         */
        private void omit() {
            this.omit = true;
        }

        /**
         * getString
         * Get all the find request's values in JSON string format
         * @return a JSON formatted string of field name value pairs
         */
        String getString() {
           StringBuilder string = Util.valuestoString(this.values);
            if (this.omit) {
                string.append(",\"omit\":\"true\"");
            }
            return "{" + string + "}";
        }
    }
}
