package com.joselopezrosario.androidfm;

public class Portal {
    private String name;
    private String limit;
    private String offset;

    public Portal(String name) {
        this.setName(name);
        this.setLimit(100);
        this.setOffset(1);
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

    private Portal setName(String name) {
        this.name = name;
        return this;
    }

    Portal setLimit(int limit) {
        this.limit = String.valueOf(limit);
        return this;
    }

    Portal setOffset(int offset) {
        this.offset = String.valueOf(offset);
        return this;
    }
}
