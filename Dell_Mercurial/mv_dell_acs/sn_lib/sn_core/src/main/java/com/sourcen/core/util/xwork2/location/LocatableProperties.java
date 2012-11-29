/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.xwork2.location;

import com.sourcen.core.util.xwork2.PropertiesReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * Properties implementation that remembers the location of each property. When loaded, a custom properties file parser
 * is used to remember both the line number and preceeding comments for each property entry.
 */
public class LocatableProperties extends Properties implements Locatable {

    Location location;

    Map<String, Location> propLocations;

    public LocatableProperties() {
        this(null);
    }

    public LocatableProperties(final Location loc) {
        super();
        this.location = loc;
        this.propLocations = new HashMap<String, Location>();
    }

    @Override
    public void load(final InputStream in) throws IOException {
        final Reader reader = new InputStreamReader(in);
        final PropertiesReader pr = new PropertiesReader(reader);
        while (pr.nextProperty()) {
            final String name = pr.getPropertyName();
            final String val = pr.getPropertyValue();
            final int line = pr.getLineNumber();
            final String desc = convertCommentsToString(pr.getCommentLines());

            final Location loc = new LocationImpl(desc, this.location.getURI(), line, 0);
            setProperty(name, val, loc);
        }
    }

    String convertCommentsToString(final List<String> lines) {
        final StringBuilder sb = new StringBuilder();
        if (lines != null && lines.size() > 0) {
            for (final String line : lines) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    public Object setProperty(final String key, final String value, final Object locationObj) {
        final Object obj = super.setProperty(key, value);
        if (this.location != null) {
            final Location loc = LocationUtils.getLocation(locationObj);
            this.propLocations.put(key, loc);
        }
        return obj;
    }

    public Location getPropertyLocation(final String key) {
        return this.propLocations.get(key);
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

}
