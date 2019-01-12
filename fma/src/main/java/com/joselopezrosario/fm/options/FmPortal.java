package com.joselopezrosario.fm.options;

public class FmPortal {
    private String name;
    private String limit;
    private String offset;


    public FmPortal(String name, int limit, int offset) {
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

    private FmPortal setName(String name) {
        this.name = name;
        return this;
    }

    private FmPortal setLimit(int limit) {
        this.limit = String.valueOf(limit);
        return this;
    }

    private FmPortal setOffset(int offset) {
        this.offset = String.valueOf(offset);
        return this;
    }

}
