/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.xwork2.location;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A class to handle location information stored in attributes. These attributes are typically setup using
 * {@link com.opensymphony.xwork2.util.location.LocationAttributes.Pipe} which augments the SAX stream with additional
 * attributes, e.g.:
 * <p/>
 * <pre>
 * &lt;root xmlns:loc="http://opensymphony.com/xwork/location"
 *       loc:src="file://path/to/file.xml"
 *       loc:line="1" loc:column="1"&gt;
 *   &lt;foo loc:src="file://path/to/file.xml" loc:line="2" loc:column="3"/&gt;
 * &lt;/root&gt;
 * </pre>
 *
 * @version $Id: LocationAttributes.java 2705 2012-05-29 08:26:39Z navinr $
 * @see com.opensymphony.xwork2.util.location.LocationAttributes.Pipe
 * @since 2.1.8
 */
public class LocationAttributes {

    /**
     * Prefix for the location namespace
     */
    public static final String PREFIX = "loc";

    /**
     * Namespace URI for location attributes
     */
    public static final String URI = "http://opensymphony.com/xwork/location";

    /**
     * Attribute name for the location URI
     */
    public static final String SRC_ATTR = "src";

    /**
     * Attribute name for the line number
     */
    public static final String LINE_ATTR = "line";

    /**
     * Attribute name for the column number
     */
    public static final String COL_ATTR = "column";

    /**
     * Attribute qualified name for the location URI
     */
    public static final String Q_SRC_ATTR = "loc:src";

    /**
     * Attribute qualified name for the line number
     */
    public static final String Q_LINE_ATTR = "loc:line";

    /**
     * Attribute qualified name for the column number
     */
    public static final String Q_COL_ATTR = "loc:column";

    // Private constructor, we only have static methods
    private LocationAttributes() {
        // Nothing
    }

    /**
     * Add location attributes to a set of SAX attributes.
     *
     * @param locator the <code>Locator</code> (can be null)
     * @param attrs   the <code>Attributes</code> where locator information should be added
     *
     * @return Location enabled Attributes.
     */
    public static Attributes addLocationAttributes(final Locator locator, final Attributes attrs) {
        if (locator == null || attrs.getIndex(LocationAttributes.URI, LocationAttributes.SRC_ATTR) != -1) {
            // No location information known, or already has it
            return attrs;
        }

        // Get an AttributeImpl so that we can add new attributes.
        final AttributesImpl newAttrs = attrs instanceof AttributesImpl ? (AttributesImpl) attrs : new AttributesImpl(attrs);

        newAttrs.addAttribute(LocationAttributes.URI, LocationAttributes.SRC_ATTR, LocationAttributes.Q_SRC_ATTR, "CDATA", locator.getSystemId());
        newAttrs.addAttribute(LocationAttributes.URI, LocationAttributes.LINE_ATTR, LocationAttributes.Q_LINE_ATTR, "CDATA", Integer.toString(locator.getLineNumber()));
        newAttrs.addAttribute(LocationAttributes.URI, LocationAttributes.COL_ATTR, LocationAttributes.Q_COL_ATTR, "CDATA", Integer.toString(locator.getColumnNumber()));

        return newAttrs;
    }

    /**
     * Returns the {@link Location} of an element (SAX flavor).
     *
     * @param attrs       the element's attributes that hold the location information
     * @param description a description for the location (can be null)
     *
     * @return a {@link Location} object
     */
    public static Location getLocation(final Attributes attrs, final String description) {
        final String src = attrs.getValue(LocationAttributes.URI, LocationAttributes.SRC_ATTR);
        if (src == null) {
            return Location.UNKNOWN;
        }

        return new LocationImpl(description, src, LocationAttributes.getLine(attrs), LocationAttributes.getColumn(attrs));
    }

    /**
     * Returns the location of an element (SAX flavor). If the location is to be kept into an object built from this
     * element, consider using {@link #getLocation(org.xml.sax.Attributes, String)} and the {@link com.sourcen.core.util.xwork2.location.Locatable} interface.
     *
     * @param attrs the element's attributes that hold the location information
     *
     * @return a location string as defined by {@link Location}.
     */
    public static String getLocationString(final Attributes attrs) {
        final String src = attrs.getValue(LocationAttributes.URI, LocationAttributes.SRC_ATTR);
        if (src == null) {
            return LocationUtils.UNKNOWN_STRING;
        }

        return src + ":" + attrs.getValue(LocationAttributes.URI, LocationAttributes.LINE_ATTR) + ":" + attrs.getValue(LocationAttributes.URI, LocationAttributes.COL_ATTR);
    }

    /**
     * Returns the URI of an element (SAX flavor)
     *
     * @param attrs the element's attributes that hold the location information
     *
     * @return the element's URI or "<code>[unknown location]</code>" if <code>attrs</code> has no location information.
     */
    public static String getURI(final Attributes attrs) {
        final String src = attrs.getValue(LocationAttributes.URI, LocationAttributes.SRC_ATTR);
        return src != null ? src : LocationUtils.UNKNOWN_STRING;
    }

    /**
     * Returns the line number of an element (SAX flavor)
     *
     * @param attrs the element's attributes that hold the location information
     *
     * @return the element's line number or <code>-1</code> if <code>attrs</code> has no location information.
     */
    public static int getLine(final Attributes attrs) {
        final String line = attrs.getValue(LocationAttributes.URI, LocationAttributes.LINE_ATTR);
        return line != null ? Integer.parseInt(line) : -1;
    }

    /**
     * Returns the column number of an element (SAX flavor)
     *
     * @param attrs the element's attributes that hold the location information
     *
     * @return the element's column number or <code>-1</code> if <code>attrs</code> has no location information.
     */
    public static int getColumn(final Attributes attrs) {
        final String col = attrs.getValue(LocationAttributes.URI, LocationAttributes.COL_ATTR);
        return col != null ? Integer.parseInt(col) : -1;
    }

    /**
     * Returns the {@link Location} of an element (DOM flavor).
     *
     * @param elem        the element that holds the location information
     * @param description a description for the location (if <code>null</code>, the element's name is used)
     *
     * @return a {@link Location} object
     */
    public static Location getLocation(final Element elem, final String description) {
        final Attr srcAttr = elem.getAttributeNodeNS(LocationAttributes.URI, LocationAttributes.SRC_ATTR);
        if (srcAttr == null) {
            return Location.UNKNOWN;
        }

        return new LocationImpl(description == null ? elem.getNodeName() : description, srcAttr.getValue(), LocationAttributes.getLine(elem), LocationAttributes.getColumn(elem));
    }

    /**
     * Same as <code>getLocation(elem, null)</code>.
     */
    public static Location getLocation(final Element elem) {
        return LocationAttributes.getLocation(elem, null);
    }

    /**
     * Returns the location of an element that has been processed by this pipe (DOM flavor). If the location is to be
     * kept into an object built from this element, consider using {@link #getLocation(org.w3c.dom.Element)} and the
     * {@link com.sourcen.core.util.xwork2.location.Locatable} interface.
     *
     * @param elem the element that holds the location information
     *
     * @return a location string as defined by {@link Location}.
     */
    public static String getLocationString(final Element elem) {
        final Attr srcAttr = elem.getAttributeNodeNS(LocationAttributes.URI, LocationAttributes.SRC_ATTR);
        if (srcAttr == null) {
            return LocationUtils.UNKNOWN_STRING;
        }

        return srcAttr.getValue() + ":" + elem.getAttributeNS(LocationAttributes.URI, LocationAttributes.LINE_ATTR) + ":"
                + elem.getAttributeNS(LocationAttributes.URI, LocationAttributes.COL_ATTR);
    }

    /**
     * Returns the URI of an element (DOM flavor)
     *
     * @param elem the element that holds the location information
     *
     * @return the element's URI or "<code>[unknown location]</code>" if <code>elem</code> has no location information.
     */
    public static String getURI(final Element elem) {
        final Attr attr = elem.getAttributeNodeNS(LocationAttributes.URI, LocationAttributes.SRC_ATTR);
        return attr != null ? attr.getValue() : LocationUtils.UNKNOWN_STRING;
    }

    /**
     * Returns the line number of an element (DOM flavor)
     *
     * @param elem the element that holds the location information
     *
     * @return the element's line number or <code>-1</code> if <code>elem</code> has no location information.
     */
    public static int getLine(final Element elem) {
        final Attr attr = elem.getAttributeNodeNS(LocationAttributes.URI, LocationAttributes.LINE_ATTR);
        return attr != null ? Integer.parseInt(attr.getValue()) : -1;
    }

    /**
     * Returns the column number of an element (DOM flavor)
     *
     * @param elem the element that holds the location information
     *
     * @return the element's column number or <code>-1</code> if <code>elem</code> has no location information.
     */
    public static int getColumn(final Element elem) {
        final Attr attr = elem.getAttributeNodeNS(LocationAttributes.URI, LocationAttributes.COL_ATTR);
        return attr != null ? Integer.parseInt(attr.getValue()) : -1;
    }

    /**
     * Remove the location attributes from a DOM element.
     *
     * @param elem    the element to remove the location attributes from.
     * @param recurse if <code>true</code>, also remove location attributes on descendant elements.
     */
    public static void remove(final Element elem, final boolean recurse) {
        elem.removeAttributeNS(LocationAttributes.URI, LocationAttributes.SRC_ATTR);
        elem.removeAttributeNS(LocationAttributes.URI, LocationAttributes.LINE_ATTR);
        elem.removeAttributeNS(LocationAttributes.URI, LocationAttributes.COL_ATTR);
        if (recurse) {
            final NodeList children = elem.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                final Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    LocationAttributes.remove((Element) child, recurse);
                }
            }
        }
    }

    /**
     * A SAX filter that adds the information available from the <code>Locator</code> as attributes. The purpose of
     * having location as attributes is to allow this information to survive transformations of the document (an XSL
     * could copy these attributes over) or conversion of SAX events to a DOM.
     * <p/>
     * The location is added as 3 attributes in a specific namespace to each element.
     * <p/>
     * <pre>
     * &lt;root xmlns:loc="http://opensymphony.com/xwork/location"
     *       loc:src="file://path/to/file.xml"
     *       loc:line="1" loc:column="1"&gt;
     *   &lt;foo loc:src="file://path/to/file.xml" loc:line="2" loc:column="3"/&gt;
     * &lt;/root&gt;
     * </pre>
     * <p/>
     * <strong>Note:</strong> Although this adds a lot of information to the serialized form of the document, the
     * overhead in SAX events is not that big, as attribute names are interned, and all <code>src</code> attributes
     * point to the same string.
     *
     * @see com.opensymphony.xwork2.util.location.LocationAttributes
     */
    public static class Pipe implements ContentHandler {

        private Locator locator;

        private ContentHandler nextHandler;

        /**
         * Create a filter. It has to be chained to another handler to be really useful.
         */
        public Pipe() {
        }

        /**
         * Create a filter that is chained to another handler.
         *
         * @param next the next handler in the chain.
         */
        public Pipe(final ContentHandler next) {
            this.nextHandler = next;
        }

        @Override
        public void setDocumentLocator(final Locator locator) {
            this.locator = locator;
            this.nextHandler.setDocumentLocator(locator);
        }

        @Override
        public void startDocument() throws SAXException {
            this.nextHandler.startDocument();
            this.nextHandler.startPrefixMapping(LocationAttributes.PREFIX, LocationAttributes.URI);
        }

        @Override
        public void endDocument() throws SAXException {
            endPrefixMapping(LocationAttributes.PREFIX);
            this.nextHandler.endDocument();
        }

        @Override
        public void startElement(final String uri, final String loc, final String raw, final Attributes attrs) throws SAXException {
            // Add location attributes to the element
            this.nextHandler.startElement(uri, loc, raw, LocationAttributes.addLocationAttributes(this.locator, attrs));
        }

        @Override
        public void endElement(final String arg0, final String arg1, final String arg2) throws SAXException {
            this.nextHandler.endElement(arg0, arg1, arg2);
        }

        @Override
        public void startPrefixMapping(final String arg0, final String arg1) throws SAXException {
            this.nextHandler.startPrefixMapping(arg0, arg1);
        }

        @Override
        public void endPrefixMapping(final String arg0) throws SAXException {
            this.nextHandler.endPrefixMapping(arg0);
        }

        @Override
        public void characters(final char[] arg0, final int arg1, final int arg2) throws SAXException {
            this.nextHandler.characters(arg0, arg1, arg2);
        }

        @Override
        public void ignorableWhitespace(final char[] arg0, final int arg1, final int arg2) throws SAXException {
            this.nextHandler.ignorableWhitespace(arg0, arg1, arg2);
        }

        @Override
        public void processingInstruction(final String arg0, final String arg1) throws SAXException {
            this.nextHandler.processingInstruction(arg0, arg1);
        }

        @Override
        public void skippedEntity(final String arg0) throws SAXException {
            this.nextHandler.skippedEntity(arg0);
        }
    }
}
