package com.joselopezrosario.fma;

public class FmaParamPortal {
    private String name;
    private String limit;
    private String offset;


    public FmaParamPortal(String name, int limit, int offset) {
        this.setName(name);
        this.setLimit(limit);
        this.setOffset(offset);

    }

    /* ---------------------------------------------------------------------------------------------
    Public getters
    ----------------------------------------------------------------------------------------------*/
    public String getName() {
        return name;
    }

    public String getLimit() {
        return limit;
    }

    public String getOffset() {
        return offset;
    }

    /* ---------------------------------------------------------------------------------------------
    Private setters
    ----------------------------------------------------------------------------------------------*/

    private FmaParamPortal setName(String name) {
        this.name = name;
        return this;
    }

    private FmaParamPortal setLimit(int limit) {
        this.limit = String.valueOf(limit);
        return this;
    }

    private FmaParamPortal setOffset(int offset) {
        this.offset = String.valueOf(offset);
        return this;
    }

}
