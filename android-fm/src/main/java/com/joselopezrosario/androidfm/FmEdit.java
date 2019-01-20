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
     * Create a new FmFind object and initialize its values ArrayList
     */
    public FmEdit() {
        this.values = new ArrayList<>();
    }

    /**
     * setName
     * Create a new Value object with the field name and value and store it it in the values ArrayList
     *
     * @param fieldName  the field name
     * @param fieldValue the field value
     * @return an FmEdit object with the new edit request value
     */
    public FmEdit set(String fieldName, String fieldValue) {
        String finalFieldValue;
        if (fieldValue == null) {
            finalFieldValue = "";
        } else {
            finalFieldValue = fieldValue;
        }
        Value value = new Value(fieldName, finalFieldValue);
        this.values.add(value);
        return this;
    }

    /**
     * countValues
     * Count the Value objects in the values ArrayList and return the number
     *
     * @return the number of Value objects
     */
    public int countValues() {
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
    Value get(int index) {
        return this.values.get(index);
    }

    /**
     * getString
     * Get all the edit values in JSON string format
     *
     * @return a JSON formatted string of field name value pairs
     */
    String getString() {
        StringBuilder string = Util.valuestoString(this.values);
        return "{" + string + "}";
    }
}
