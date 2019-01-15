package com.joselopezrosario.androidfm;

import java.util.ArrayList;

/**
 * FmPortal
 * A class to handle creating the portal parameters of an FmRequest
 */
public class FmPortal {
    private int countPortals;
    private ArrayList<Portal> portalParams;

    /**
     * FmPortal
     * Create a new FmPortal object and initialize the portalParams ArrayList
     */
    public FmPortal(){
        this.portalParams = new ArrayList<>();
    }

    /**
     * set
     * Pass the portal's name or related table's name to create a new Portal object and add it
     * to the portalParams ArrayList
     * @param name the portal's name or related table's name
     * @return the FmPortal object with the new Portal object
     */
    public FmPortal set(String name){
        Portal portal = new Portal(name);
        this.portalParams.add(portal);
        this.countPortals = this.portalParams.size();
        return this;
    }

    /**
     * setLimit
     * Set the number of records to return - the default is 100
     * @param limit the number of records to return
     * @return the FmPortal object with the new limit
     */
   public FmPortal setLimit(int limit){
        this.portalParams.get(this.countPortals - 1).setLimit(limit);
        return this;
    }

    /**
     * setOffset
     * Set the starting record
     * @param offset the starting record - the default is 1
     * @return the FmPortal object with the new offset
     */
    public FmPortal setOffset(int offset){
        this.portalParams.get(this.countPortals - 1).setOffset(offset);
        return this;
    }

    /**
     * getPortalParams
     * @return the portalParams ArrayList
     */
    public ArrayList<Portal> getPortalParams() {
        return portalParams;
    }
}
