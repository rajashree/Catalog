/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.collections;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * An implementation that allows us to convert datatypes from one form to another within a map.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1877 $, $Date:: 2012-04-20 21:41:17#$
 */
public abstract class AbstractPropertiesProvider implements PropertiesProvider {

    /**
     * Creates a new AbstractPropertiesProvider instance.
     */
    protected AbstractPropertiesProvider() {
    }

    /**
     * save a property into the database or the file system.
     *
     * @param clazz of type Class
     * @param name  of type String
     * @param value of type Object
     */
    public void setProperty(final Class<?> clazz, final String name, final Object value) {
        setProperty(getClassKey(clazz, name), value.toString());
    }

    @Override
    public void setProperty(String key, Object value) {
        setProperty(key, (value == null) ? null : value.toString());
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the
     * key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the
     *         key
     * @throws NullPointerException if the specified key is null
     */
    protected String getPropertyValue(final String key) throws NullPointerException {
        final Object value = getObjectProperty(key);
        return (value != null) ? value.toString() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getProperties(final String key) {
        return getProperties(keySet(), key, false, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getProperties(final Class<?> clazz, final String key) {
        return getProperties(getClassKey(clazz, key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getProperties(final Class<?> clazz, final String key, final Boolean cleaned, final Boolean parseValues) {
        return getProperties(keySet(), getClassKey(clazz, key), cleaned, parseValues);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getProperties(final String name, final Boolean cleaned, final Boolean parseValues) {
        return getProperties(keySet(), name, cleaned, parseValues);
    }

    /**
     * Returns a list of properties whose keys start with the key specified. The final key is constructed by the
     * class.getCanonicalName()+ "." + key;
     *
     * @param keysArr     is the list of properties we must search in.
     * @param name        that we must match with all the keys in properties.
     * @param cleaned     if the value is true the for example, if a property's key is "com.test.myProp1" the cleaned value
     *                    in the HashMap would be "myProp1"
     * @param parseValues if the value is true all the properties that were returned for the criteria of the clazz, and
     *                    the key will be parsed into Integer,Boolean,Long or String.
     * @return A Map of type <code>Map<String,Object></code> that contains properties whose keys start with the
     *         parameter specified.
     */
    protected final Map<String, Object> getProperties(final Set<String> keysArr, final String name, final Boolean cleaned, final Boolean parseValues) {
        final Map<String, Object> map = new HashMap<String, Object>();

        // get all keys starting with the key asked for.
        final Collection<String> keys = getPropertyNames(name, keysArr, false);

        if (keys.isEmpty()) {
            return new HashMap<String, Object>(0);
        }
        for (final String key : keys) {
            Object value;
            // if parseValues is true, the parse it into Integer, Boolean,
            // Long or String whichever is appropriate.
            if (parseValues) {
                final String valueStr = getProperty(key);
                final int len = valueStr.length();
                // try to parse as int, then boolean, string
                if (len <= 10) {
                    try {
                        value = Integer.parseInt(valueStr);
                    } catch (final Exception e) {
                        if (valueStr.equalsIgnoreCase("true") || valueStr.equalsIgnoreCase("false")) {
                            value = valueStr.equalsIgnoreCase("true");
                        } else {
                            value = valueStr;
                        }
                    }
                } else if (len < 20) {
                    try {
                        value = Long.parseLong(valueStr);
                    } catch (final Exception e) {
                        value = valueStr;
                    }
                } else {
                    value = valueStr;
                }
            } else {
                value = getProperty(key);
            }
            if (cleaned) {
                final int index = key.lastIndexOf(".");
                if (index > -1) {
                    map.put(key.substring(index + 1), value);
                } else {
                    map.put(key, value);
                }
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Set<String> getPropertyNames(final String name, final boolean stripParentName) {
        return getPropertyNames(name, keySet(), stripParentName);
    }

    /**
     * return a collection of propertyNames that starts with a particular key.
     *
     * @param name            of type String
     * @param keySet          of type Collection
     * @param stripParentName of type boolean if set to true will remove the name prefix of the keys.
     * @return Collection<String>
     */
    protected final Set<String> getPropertyNames(String name, final Collection<String> keySet, final boolean stripParentName) {
        // get all keys starting with the key asked for.
        if (!name.endsWith(".")) {
            name = name + ".";
        }
        final Set<String> keys = new HashSet<String>();
        for (final String key : keySet) {
            if (key.startsWith(name)) {
                if (stripParentName) {
                    final int index = key.lastIndexOf(".");
                    if (index > -1) {
                        keys.add(key.substring(index + 1));
                    } else {
                        keys.add(key);
                    }
                } else {
                    keys.add(key);
                }
            }
        }
        if (keys.isEmpty()) {
            return Collections.emptySet();
        } else {
            return keys;
        }
    }

    //
    // STRING OPERATIONS
    //

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProperty(final String key) {
        return getPropertyValue(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProperty(final String key, final String defaultValue) {
        if (hasProperty(key)) {
            return getPropertyValue(key);
        }
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProperty(final Class<?> clazz, final String key) {
        return getProperty(getClassKey(clazz, key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProperty(final Class<?> clazz, final String key, final String defaultValue) {
        return getProperty(getClassKey(clazz, key), defaultValue);
    }

    //
    // LONG OPERATIONS
    //

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLongProperty(final String key) {
        if (hasProperty(key)) {
            final Object prop = getPropertyValue(key);
            if (prop instanceof Long) {
                return (Long) prop;
            } else if (prop instanceof String) {
                return Long.parseLong((String) prop);
            }
            return Long.parseLong(prop.toString());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLongProperty(final String key, final Long defaultValue) {
        if (hasProperty(key)) {
            return getLongProperty(key);
        }
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLongProperty(final Class<?> clazz, final String key) {
        return getLongProperty(getClassKey(clazz, key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLongProperty(final Class<?> clazz, final String key, final Long defaultValue) {
        return getLongProperty(getClassKey(clazz, key), defaultValue);
    }

    //
    // INTEGER OPERATIONS
    //

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getIntegerProperty(final String key) {
        if (hasProperty(key)) {
            return Integer.parseInt(getPropertyValue(key));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getIntegerProperty(final String key, final Integer defaultValue) {
        if (hasProperty(key)) {
            return getIntegerProperty(key);
        }
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getIntegerProperty(final Class<?> clazz, final String key) {
        return getIntegerProperty(getClassKey(clazz, key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getIntegerProperty(final Class<?> clazz, final String key, final Integer defaultValue) {
        return getIntegerProperty(getClassKey(clazz, key), defaultValue);
    }

    //
    // FLOAT OPERATIONS
    //

    /**
     * {@inheritDoc}
     */
    @Override
    public Float getFloatProperty(final String key) {
        if (hasProperty(key)) {
            return Float.parseFloat(getPropertyValue(key));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float getFloatProperty(final String key, final Float defaultValue) {
        if (hasProperty(key)) {
            return getFloatProperty(key);
        }
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float getFloatProperty(final Class<?> clazz, final String key) {
        return getFloatProperty(getClassKey(clazz, key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float getFloatProperty(final Class<?> clazz, final String key, final Float defaultValue) {
        return getFloatProperty(getClassKey(clazz, key), defaultValue);
    }

    //
    // DOUBLE OPERATIONS
    //

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getDoubleProperty(final String key) {
        if (hasProperty(key)) {
            return Double.parseDouble(getPropertyValue(key));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getDoubleProperty(final String key, final Double defaultValue) {
        if (hasProperty(key)) {
            return getDoubleProperty(key);
        }
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getDoubleProperty(final Class<?> clazz, final String key) {
        return getDoubleProperty(getClassKey(clazz, key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getDoubleProperty(final Class<?> clazz, final String key, final Double defaultValue) {
        return getDoubleProperty(getClassKey(clazz, key), defaultValue);
    }

    //
    // BOOLEAN OPERATIONS
    //

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBooleanProperty(final String key) {
        if (hasProperty(key)) {
            return Boolean.parseBoolean(getPropertyValue(key));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBooleanProperty(final String key, final Boolean defaultValue) {
        if (hasProperty(key)) {
            return getBooleanProperty(key);
        }
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBooleanProperty(final Class<?> clazz, final String key) {
        return getBooleanProperty(getClassKey(clazz, key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBooleanProperty(final Class<?> clazz, final String key, final Boolean defaultValue) {
        return getBooleanProperty(getClassKey(clazz, key), defaultValue);
    }

    //
    // Helper methods.
    //

    /**
     * A helper method to construct the final key based on the class reference and the key passed. It basically returns
     * a key of type clazz.getCanonicalName()+ "." +key;
     *
     * @param clazz The reference of the class for which we should get the mapped value from the properties map.
     * @param key   the key whose associated value is to be returned
     * @return A final String value of the combination of clazz and key.
     */
    protected String getClassKey(final Class<?> clazz, final String key) {
        return clazz.getCanonicalName().concat(".").concat(key);
    }

    /**
     * merge a java.util.Properties into this AbstractPropertiesProvider object.
     *
     * @param properties of type AbstractPropertiesProvider
     */
    protected void mergeProperties(final Properties properties) {
        if (properties == null) {
            return;
        }
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            this.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
    }

    @Override
    public Boolean supportsPersistence() {
        return false;
    }

        @Override
    public int size() {
        return keySet().size();
    }

    //
    // xml operations
    //

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadFromXML(final String path) throws IOException {
        loadFromXML(new FileInputStream(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadFromXML(final InputStream stream) throws IOException {
        final Properties prop = new Properties();
        prop.loadFromXML(stream);
        mergeProperties(prop);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToXML(final String path) throws IOException {
        saveToXML(new FileOutputStream(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToXML(final String path, final String comment) throws IOException {
        saveToXML(new FileOutputStream(path), comment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToXML(final OutputStream stream) throws IOException {
        saveToXML(stream, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToXML(final OutputStream stream, final String comment) throws IOException {
        final Properties prop = new Properties();
        for (final String key : keySet()) {
            prop.put(key, getObjectProperty(key));
        }
        prop.storeToXML(stream, comment);
    }

    /**
     * Provides an XStream Converter for all AbstractPropertiesProvider subclasses.
     */
    public static final class XStreamPropertiesConverter implements Converter {

        /**
         * {@inheritDoc"
         */
        @Override
        public void marshal(final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
            final AbstractPropertiesProvider abstractPropertiesProvider = (AbstractPropertiesProvider) source;
            for (final String key : abstractPropertiesProvider.keySet()) {
                writer.startNode("property");
                writer.addAttribute("name", key);
                writer.addAttribute("value", abstractPropertiesProvider.getPropertyValue(key));
                writer.endNode();
            }
        }

        /**
         * {@inheritDoc"
         */
        @Override
        public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext context) {
            final SimpleProperties properties = new SimpleProperties();
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                final String name = reader.getAttribute("name");
                final String value = reader.getAttribute("value");
                properties.setProperty(name, value);
                reader.moveUp();
            }
            return properties;
        }

        /**
         * {@inheritDoc"
         */
        @Override
        public boolean canConvert(final Class type) {
            return PropertiesProvider.class.isAssignableFrom(type);
        }
    }

}

