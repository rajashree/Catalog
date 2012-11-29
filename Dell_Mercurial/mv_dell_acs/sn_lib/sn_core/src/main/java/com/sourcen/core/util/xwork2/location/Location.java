/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.xwork2.location;

import java.util.List;

/**
 * A location in a resource. The location is composed of the URI of the resource, and the line and column numbers within
 * that resource (when available), along with a description.
 * <p/>
 * Locations are mostly provided by {@link com.sourcen.core.util.xwork2.location.Locatable}s objects.
 */
public interface Location {

    /**
     * Constant for unknown locations.
     */
    public static final Location UNKNOWN = LocationImpl.UNKNOWN;

    /**
     * Get the description of this location
     *
     * @return the description (can be <code>null</code>)
     */
    String getDescription();

    /**
     * Get the URI of this location
     *
     * @return the URI (<code>null</code> if unknown).
     */
    String getURI();

    /**
     * Get the line number of this location
     *
     * @return the line number (<code>-1</code> if unknown)
     */
    int getLineNumber();

    /**
     * Get the column number of this location
     *
     * @return the column number (<code>-1</code> if unknown)
     */
    int getColumnNumber();

    /**
     * Gets a source code snippet with the default padding
     *
     * @param padding The amount of lines before and after the error to include
     *
     * @return A list of source lines
     */
    List<String> getSnippet(int padding);
}
