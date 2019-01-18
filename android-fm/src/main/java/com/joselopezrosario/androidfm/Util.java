package com.joselopezrosario.androidfm;

import java.util.ArrayList;

class Util {

    static StringBuilder valuestoString(ArrayList<Value> values){
        if (values == null) {
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
        return string;
    }
}
