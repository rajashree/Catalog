/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.xwork2.location;

import com.sourcen.core.util.xwork2.ClassLoaderUtil;
import org.w3c.dom.Element;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Location-related utility methods.
 */
public class LocationUtils {

    /**
     * The string representation of an unknown location: "<code>[unknown location]</code>".
     */
    public static final String UNKNOWN_STRING = "[unknown location]";

    private static List<WeakReference<LocationFinder>> finders = new ArrayList<WeakReference<LocationFinder>>();

    /**
     * An finder or object locations
     */
    public interface LocationFinder {

        /**
         * Get the location of an object
         *
         * @param obj         the object for which to find a location
         * @param description and optional description to be added to the object's location
         *
         * @return the object's location or <code>null</code> if object's class isn't handled by this finder.
         */
        Location getLocation(Object obj, String description);
    }

    private LocationUtils() {
        // Forbid instanciation
    }

    /**
     * Builds a string representation of a location, in the "
     * <code><em>descripton</em> - <em>uri</em>:<em>line</em>:<em>column</em></code>" format (e.g. "
     * <code>foo - file://path/to/file.xml:3:40</code>"). For {@link com.sourcen.core.util.xwork2.location.Location#UNKNOWN an unknown location}, returns
     * {@link #UNKNOWN_STRING}.
     *
     * @return the string representation
     */
    public static String toString(final Location location) {
        final StringBuilder result = new StringBuilder();

        final String description = location.getDescription();
        if (description != null) {
            result.append(description).append(" - ");
        }

        final String uri = location.getURI();
        if (uri != null) {
            result.append(uri).append(':').append(location.getLineNumber()).append(':').append(location.getColumnNumber());
        } else {
            result.append(LocationUtils.UNKNOWN_STRING);
        }

        return result.toString();
    }

    /**
     * Parse a location string of the form "<code><em>uri</em>:<em>line</em>:<em>column</em></code>" (e.g. "
     * <code>path/to/file.xml:3:40</code>") to a Location object. Additionally, a description may also optionally be
     * present, separated with an hyphen (e.g. "<code>foo - path/to/file.xml:3.40</code>").
     *
     * @param text the text to parse
     *
     * @return the location (possibly <code>null</code> if text was null or in an incorrect format)
     */
    public static LocationImpl parse(final String text) throws IllegalArgumentException {
        if (text == null || text.length() == 0) {
            return null;
        }

        // Do we have a description?
        String description;
        int uriStart = text.lastIndexOf(" - "); // lastIndexOf to allow the separator to be in the description
        if (uriStart > -1) {
            description = text.substring(0, uriStart);
            uriStart += 3; // strip " - "
        } else {
            description = null;
            uriStart = 0;
        }

        try {
            final int colSep = text.lastIndexOf(':');
            if (colSep > -1) {
                final int column = Integer.parseInt(text.substring(colSep + 1));

                final int lineSep = text.lastIndexOf(':', colSep - 1);
                if (lineSep > -1) {
                    final int line = Integer.parseInt(text.substring(lineSep + 1, colSep));
                    return new LocationImpl(description, text.substring(uriStart, lineSep), line, column);
                }
            } else {
                // unkonwn?
                if (text.endsWith(LocationUtils.UNKNOWN_STRING)) {
                    return LocationImpl.UNKNOWN;
                }
            }
        } catch (final Exception e) {
            // Ignore: handled below
        }

        return LocationImpl.UNKNOWN;
    }

    /**
     * Checks if a location is known, i.e. it is not null nor equal to {@link com.sourcen.core.util.xwork2.location.Location#UNKNOWN}.
     *
     * @param location the location to check
     *
     * @return <code>true</code> if the location is known
     */
    public static boolean isKnown(final Location location) {
        return location != null && !Location.UNKNOWN.equals(location);
    }

    /**
     * Checks if a location is unknown, i.e. it is either null or equal to {@link com.sourcen.core.util.xwork2.location.Location#UNKNOWN}.
     *
     * @param location the location to check
     *
     * @return <code>true</code> if the location is unknown
     */
    public static boolean isUnknown(final Location location) {
        return location == null || Location.UNKNOWN.equals(location);
    }

    /**
     * Add a {@link com.sourcen.core.util.xwork2.location.LocationUtils.LocationFinder} to the list of finders that will be queried for an object's location by
     * {@link #getLocation(Object, String)}.
     * <p/>
     * <b>Important:</b> LocationUtils internally stores a weak reference to the finder. This avoids creating strong
     * links between the classloader holding this class and the finder's classloader, which can cause some weird memory
     * leaks if the finder's classloader is to be reloaded. Therefore, you <em>have</em> to keep a strong reference to
     * the finder in the calling code, e.g.:
     * <p/>
     * <pre>
     *   private static LocationUtils.LocationFinder myFinder =
     *       new LocationUtils.LocationFinder() {
     *           public Location getLocation(Object obj, String desc) {
     *               ...
     *           }
     *       };
     *
     *   static {
     *       LocationUtils.addFinder(myFinder);
     *   }
     * </pre>
     *
     * @param finder the location finder to add
     */
    public static void addFinder(final LocationFinder finder) {
        if (finder == null) {
            return;
        }

        synchronized (LocationFinder.class) {
            // Update a clone of the current finder list to avoid breaking
            // any iteration occuring in another thread.
            final List<WeakReference<LocationFinder>> newFinders = new ArrayList<WeakReference<LocationFinder>>(LocationUtils.finders);
            newFinders.add(new WeakReference<LocationFinder>(finder));
            LocationUtils.finders = newFinders;
        }
    }

    /**
     * Get the location of an object. Some well-known located classes built in the JDK are handled by this method.
     * Handling of other located classes can be handled by adding new location finders.
     *
     * @param obj the object of which to get the location
     *
     * @return the object's location, or {@link com.sourcen.core.util.xwork2.location.Location#UNKNOWN} if no location could be found
     */
    public static Location getLocation(final Object obj) {
        return LocationUtils.getLocation(obj, null);
    }

    /**
     * Get the location of an object. Some well-known located classes built in the JDK are handled by this method.
     * Handling of other located classes can be handled by adding new location finders.
     *
     * @param obj         the object of which to get the location
     * @param description an optional description of the object's location, used if a Location object has to be created.
     *
     * @return the object's location, or {@link com.sourcen.core.util.xwork2.location.Location#UNKNOWN} if no location could be found
     */
    public static Location getLocation(final Object obj, String description) {
        if (obj instanceof Location) {
            return (Location) obj;
        }

        if (obj instanceof Locatable) {
            return ((Locatable) obj).getLocation();
        }

        // Check some well-known locatable exceptions
        if (obj instanceof SAXParseException) {
            final SAXParseException spe = (SAXParseException) obj;
            if (spe.getSystemId() != null) {
                return new LocationImpl(description, spe.getSystemId(), spe.getLineNumber(), spe.getColumnNumber());
            } else {
                return Location.UNKNOWN;
            }
        }

        if (obj instanceof TransformerException) {
            final TransformerException ex = (TransformerException) obj;
            final SourceLocator locator = ex.getLocator();
            if (locator != null && locator.getSystemId() != null) {
                return new LocationImpl(description, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
            } else {
                return Location.UNKNOWN;
            }
        }

        if (obj instanceof Locator) {
            final Locator locator = (Locator) obj;
            if (locator.getSystemId() != null) {
                return new LocationImpl(description, locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
            } else {
                return Location.UNKNOWN;
            }
        }

        if (obj instanceof Element) {
            return LocationAttributes.getLocation((Element) obj);
        }

        final List<WeakReference<LocationFinder>> currentFinders = LocationUtils.finders; // Keep the current list
        final int size = currentFinders.size();
        for (int i = 0; i < size; i++) {
            final WeakReference<LocationFinder> ref = currentFinders.get(i);
            final LocationFinder finder = ref.get();
            if (finder == null) {
                // This finder was garbage collected: update finders
                synchronized (LocationFinder.class) {
                    // Update a clone of the current list to avoid breaking current iterations
                    final List<WeakReference<LocationFinder>> newFinders = new ArrayList<WeakReference<LocationFinder>>(LocationUtils.finders);
                    newFinders.remove(ref);
                    LocationUtils.finders = newFinders;
                }
            }

            final Location result = finder.getLocation(obj, description);
            if (result != null) {
                return result;
            }
        }

        if (obj instanceof Throwable) {
            final Throwable t = (Throwable) obj;
            final StackTraceElement[] stack = t.getStackTrace();
            if (stack != null && stack.length > 0) {
                final StackTraceElement trace = stack[0];
                if (trace.getLineNumber() >= 0) {
                    String uri = trace.getClassName();
                    if (trace.getFileName() != null) {
                        uri = uri.replace('.', '/');
                        uri = uri.substring(0, uri.lastIndexOf('/') + 1);
                        uri = uri + trace.getFileName();
                        final URL url = ClassLoaderUtil.getResource(uri, LocationUtils.class);
                        if (url != null) {
                            uri = url.toString();
                        }
                    }
                    if (description == null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Class: ").append(trace.getClassName()).append("\n");
                        sb.append("File: ").append(trace.getFileName()).append("\n");
                        sb.append("Method: ").append(trace.getMethodName()).append("\n");
                        sb.append("Line: ").append(trace.getLineNumber());
                        description = sb.toString();
                    }
                    return new LocationImpl(description, uri, trace.getLineNumber(), -1);
                }
            }
        }

        return Location.UNKNOWN;
    }
}
