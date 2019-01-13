package com.joselopezrosario.fm;

import java.util.ArrayList;

public class FmSort {
    public static final String ASCEND = "ascend";
    public static final String DESCEN = "descend";
    private ArrayList<Sort> sortParams;

    public FmSort() {
        this.sortParams = new ArrayList<>();
    }

    /**
     * sortAsc
     * @param fieldName the field name to sort by
     * @return an FmSort object with the sort object in the sortParams ArrayList
     */
    public FmSort sortAsc(String fieldName) {
        Sort sort = new Sort(fieldName).ascend();
        this.sortParams.add(sort);
        return this;
    }

    /**
     * sortDesc
     * @param fieldName the field name to sort by
     * @return an FmSort object with the sort object in the sortParams ArrayList
     */
    public FmSort sortDesc(String fieldName) {
        Sort sort = new Sort(fieldName).descend();
        this.sortParams.add(sort);
        return this;
    }

    public ArrayList<Sort> getSortParams() {
        return sortParams;
    }


}

