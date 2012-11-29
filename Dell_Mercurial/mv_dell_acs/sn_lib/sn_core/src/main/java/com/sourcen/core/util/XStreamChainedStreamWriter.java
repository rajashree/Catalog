/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.springframework.util.Assert;

/**
 * Not used ATM.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2699 $, $Date:: 2012-05-29 10:00:40#$
 */
public class XStreamChainedStreamWriter implements HierarchicalStreamWriter {

    private HierarchicalStreamWriter writer;
    public static final int NONE = 0x0;
    public static final int BOOLEAN_AS_BIT = 0x1;
    private int mode;
    public boolean isBooleanAsBit = false;


    public XStreamChainedStreamWriter(HierarchicalStreamWriter writer) {
        this(writer, NONE);
    }

    public XStreamChainedStreamWriter(HierarchicalStreamWriter writer, int mode) {
        this.writer = writer;
        this.mode = mode;
        isBooleanAsBit = (mode & BOOLEAN_AS_BIT) == 1;
    }

    public XStreamChainedStreamWriter writeNode(String name, Object text) {
        return writeNode(name, text, null);
    }

    public XStreamChainedStreamWriter writeNode(String name, Object text, String defaultValue) {
        writer.startNode(name);
        if (text == null) {
            if (defaultValue != null) {
                writer.setValue(defaultValue);
            }
        } else if (isBooleanAsBit && text.getClass().equals(Boolean.class)) {
            writer.setValue(((Boolean) text) ? "1" : "0");
        } else {
            writer.setValue(text.toString());
        }
        writer.endNode();
        return this;
    }

    public XStreamChainedStreamWriter startNode(String name, String text) {
        startNode(name);
        if (text != null) {
            this.setValue(text);
        }
        return this;
    }

    public XStreamChainedStreamWriter startNode(String name, String text, String... attributes) {
        startNode(name);
        if (attributes != null) {
            if (attributes.length % 2 != 0) {
                throw new IllegalArgumentException("attributes parameter is a key-value pair, we found an odd length of attributes :="
                        + attributes.length + " but expected " + (attributes.length + 1));

            }
            for (int i = 0; i < attributes.length; i += 2) {
                this.addAttribute(attributes[i], attributes[i + 1]);
            }
        }
        if (text != null) {
            this.setValue(text);
        }

        return this;
    }

    public XStreamChainedStreamWriter startNode(String name, String text, Object... attributesWithDefaults) {
        startNode(name);
        if (attributesWithDefaults != null) {
            if (attributesWithDefaults.length % 3 != 0) {
                throw new IllegalArgumentException("attributes parameter is a key-value pair, we found an length % 3 length of attributes, ie, attributeName, AttributeValue, attributeDefault");
            }
            for (int i = 0; i < attributesWithDefaults.length; i += 3) {

                this.addAttribute(attributesWithDefaults[i], attributesWithDefaults[i + 1], attributesWithDefaults[i + 2]);
            }
        }
        if (text != null) {
            this.setValue(text);
        }

        return this;
    }

    public XStreamChainedStreamWriter writeNode(String name, String text, Object... attributesWithDefaults) {
        startNode(name, text, attributesWithDefaults).endNode();
        return this;
    }

    public XStreamChainedStreamWriter writeNode(String name, String text, String... attributes) {
        startNode(name, text, attributes).endNode();
        return this;
    }

    public XStreamChainedStreamWriter addAttribute(String name, String value, String defaultValue) {
        if (value == null) {
            writer.addAttribute(name, defaultValue);
        } else {
            writer.addAttribute(name, value);
        }
        return this;
    }

    private XStreamChainedStreamWriter addAttribute(Object name, Object value, Object defaultValue) {
        Assert.notNull(name, "attributeName cannot be null");
        if (value == null) {
            if (defaultValue != null) {
                writer.addAttribute(name.toString(), defaultValue.toString());
            }
        } else if (isBooleanAsBit && value.getClass().equals(Boolean.class)) {
            writer.addAttribute(name.toString(), ((Boolean) value) ? "1" : "0");
        } else {
            writer.addAttribute(name.toString(), value.toString());
        }
        return this;
    }


    public XStreamChainedStreamWriter addAttribute(String name, Object value, String defaultValue) {
        if (value == null) {
            writer.addAttribute(name, defaultValue);
        } else {
            writer.addAttribute(name, value.toString());
        }
        return this;
    }

    public XStreamChainedStreamWriter addAttribute(String name, Object value) {
        if (value != null) {
            writer.addAttribute(name, value.toString());
        }
        return this;
    }

    //
    // delegate methods
    //

    @Override
    public void startNode(String name) {
        writer.startNode(name);
    }

    @Override
    public void addAttribute(String name, String value) {
        writer.addAttribute(name, value);
    }

    @Override
    public void setValue(String text) {
        writer.setValue(text);
    }

    @Override
    public void endNode() {
        writer.endNode();
    }

    @Override
    public void flush() {
        writer.flush();
    }

    @Override
    public void close() {
        writer.close();
    }

    @Override
    public HierarchicalStreamWriter underlyingWriter() {
        return writer.underlyingWriter();
    }
}
