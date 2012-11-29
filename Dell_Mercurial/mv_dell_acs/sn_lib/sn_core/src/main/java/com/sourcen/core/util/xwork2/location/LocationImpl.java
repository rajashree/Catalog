/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.xwork2.location;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple immutable and serializable implementation of {@link Location}.
 */
public class LocationImpl implements Location, Serializable {

    private final String uri;

    private final int line;

    private final int column;

    private final String description;

    // Package private: outside this package, use Location.UNKNOWN.
    static final LocationImpl UNKNOWN = new LocationImpl(null, null, -1, -1);

    /**
     * Build a location for a given URI, with unknown line and column numbers.
     *
     * @param uri the resource URI
     */
    public LocationImpl(final String description, final String uri) {
        this(description, uri, -1, -1);
    }

    /**
     * Build a location for a given URI and line and column numbers.
     *
     * @param uri    the resource URI
     * @param line   the line number (starts at 1)
     * @param column the column number (starts at 1)
     */
    public LocationImpl(String description, final String uri, final int line, final int column) {
        if (uri == null || uri.length() == 0) {
            this.uri = null;
            this.line = -1;
            this.column = -1;
        } else {
            this.uri = uri;
            this.line = line;
            this.column = column;
        }

        if (description != null && description.length() == 0) {
            description = null;
        }
        this.description = description;
    }

    /**
     * Copy constructor.
     *
     * @param location the location to be copied
     */
    public LocationImpl(final Location location) {
        this(location.getDescription(), location.getURI(), location.getLineNumber(), location.getColumnNumber());
    }

    /**
     * Create a location from an existing one, but with a different description
     */
    public LocationImpl(final String description, final Location location) {
        this(description, location.getURI(), location.getLineNumber(), location.getColumnNumber());
    }

    /**
     * Obtain a <code>LocationImpl</code> from a {@link Location}. If <code>location</code> is already a
     * <code>LocationImpl</code>, it is returned, otherwise it is copied.
     * <p/>
     * This method is useful when an immutable and serializable location is needed, such as in locatable exceptions.
     *
     * @param location the location
     *
     * @return an immutable and serializable version of <code>location</code>
     */
    public static LocationImpl get(final Location location) {
        if (location instanceof LocationImpl) {
            return (LocationImpl) location;
        } else if (location == null) {
            return LocationImpl.UNKNOWN;
        } else {
            return new LocationImpl(location);
        }
    }

    /**
     * Get the description of this location
     *
     * @return the description (can be <code>null</code>)
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the URI of this location
     *
     * @return the URI (<code>null</code> if unknown).
     */
    @Override
    public String getURI() {
        return this.uri;
    }

    /**
     * Get the line number of this location
     *
     * @return the line number (<code>-1</code> if unknown)
     */
    @Override
    public int getLineNumber() {
        return this.line;
    }

    /**
     * Get the column number of this location
     *
     * @return the column number (<code>-1</code> if unknown)
     */
    @Override
    public int getColumnNumber() {
        return this.column;
    }

    /**
     * Gets a source code snippet with the default padding
     *
     * @param padding The amount of lines before and after the error to include
     */
    @Override
    public List<String> getSnippet(final int padding) {
        final List<String> snippet = new ArrayList<String>();
        if (getLineNumber() > 0) {
            try {
                final InputStream in = new URL(getURI()).openStream();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                int lineno = 0;
                final int errno = getLineNumber();
                String line;
                while ((line = reader.readLine()) != null) {
                    lineno++;
                    if (lineno >= errno - padding && lineno <= errno + padding) {
                        snippet.add(line);
                    }
                }
            } catch (final Exception ex) {
                // ignoring as snippet not available isn't a big deal
            }
        }
        return snippet;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Location) {
            final Location other = (Location) obj;
            return this.line == other.getLineNumber() && this.column == other.getColumnNumber() && testEquals(this.uri, other.getURI())
                    && testEquals(this.description, other.getDescription());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = this.line ^ this.column;
        if (this.uri != null) {
            hash ^= this.uri.hashCode();
        }
        if (this.description != null) {
            hash ^= this.description.hashCode();
        }

        return hash;
    }

    @Override
    public String toString() {
        return LocationUtils.toString(this);
    }

    /**
     * Ensure serialized unknown location resolve to {@link Location#UNKNOWN}.
     */
    private Object readResolve() {
        return this.equals(Location.UNKNOWN) ? Location.UNKNOWN : this;
    }

    private boolean testEquals(final Object object1, final Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }
}
