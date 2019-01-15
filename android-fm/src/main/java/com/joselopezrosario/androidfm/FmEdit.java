package com.joselopezrosario.androidfm;

import java.util.ArrayList;

/**
 * FmEdit
 * A class to handle creating the edit parameters of an FmRequest (create and edit)
 */
public class FmEdit {
    private ArrayList<Value> values;

    /**
     * FmEdit
     * Create a new FmFind object and initialize the values ArrayList
     */
    public FmEdit() {
        this.values = new ArrayList<>();
    }

    /**
     * set
     * Create a new Value object with the field name and value and store it it in the values ArrayList
     *
     * @param fieldName  the field name
     * @param fieldValue the field value
     * @return an FmEdit object with the new edit request value
     */
    public FmEdit set(String fieldName, String fieldValue) {
        Value value = new Value(fieldName, fieldValue);
        this.values.add(value);
        return this;
    }

    /**
     * countEditValues
     * Count the Value objects in the values ArrayList and return the number
     *
     * @return the number of Value objects
     */
    public int countEditValues() {
        if (values != null) {
            return this.values.size();
        } else {
            return 0;
        }
    }

    /**
     * get
     * Get a Value object from the ArrayList by specifying its index
     *
     * @param index the value's index in the ArrayList
     * @return a Value object
     */
    public Value get(int index) {
        return this.values.get(index);
    }

    // TODO: Convert this to a Utility? It's the same code under FmFind
    /**
     * getString
     * Get all the edit values in JSON string format
     *
     * @return a JSON formatted string of field name value pairs
     */
    String getString() {
        if (this.values == null) {
            return null;
        }
        int size = values.size();
        StringBuilder string = new StringBuilder();
        int i = 0;
        while (i < size) {
            Value value = values.get(i);
            if (i == 0) {
                string = string.append(value.getFieldName()).append(":").append(value.getFieldValue());
            } else {
                string = string.append(",").append(value.getFieldName()).append(":").append(value.getFieldValue());
            }
            i++;
        }
        return "{" + string + "}";
    }
}
