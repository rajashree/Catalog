/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.xwork2.location;

/**
 * Base class for location aware objects
 */
public abstract class Located implements Locatable {

    protected Location location;

    /**
     * Get the location of this object
     *
     * @return the location
     */
    @Override
    public Location getLocation() {
        return this.location;
    }

    /**
     * Set the location of this object
     *
     * @param loc the location
     */
    public void setLocation(final Location loc) {
        this.location = loc;
    }
}
