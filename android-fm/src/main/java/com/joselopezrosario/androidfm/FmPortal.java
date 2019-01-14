package com.joselopezrosario.androidfm;

import java.util.ArrayList;

public class FmPortal {
    private int countPortals;
    private ArrayList<Portal> portalParams;

    public FmPortal(){
        this.portalParams = new ArrayList<>();
    }

    public FmPortal set(String name){
        Portal portal = new Portal(name);
        this.portalParams.add(portal);
        this.countPortals = this.portalParams.size();
        return this;
    }

   public FmPortal setLimit(int limit){
        this.portalParams.get(this.countPortals - 1).setLimit(limit);
        return this;
    }

    public FmPortal setOffset(int offset){
        this.portalParams.get(this.countPortals - 1).setOffset(offset);
        return this;
    }

    public ArrayList<Portal> getPortalParams() {
        return portalParams;
    }
}
