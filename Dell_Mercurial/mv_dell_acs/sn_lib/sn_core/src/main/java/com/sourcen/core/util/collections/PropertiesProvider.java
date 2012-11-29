/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.collections;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

public interface PropertiesProvider {

    /**
     * Returns true if the specified object is a key in the properties Map.
     *
     * @param key a key whose existance should be checked.
     * @return true if and only if the specified object is a key in this table, as determined by the equals method;
     *         false otherwise.
     * @throws NullPointerException if the specified key is null
     */
    public abstract boolean hasProperty(String key) throws NullPointerException;

    /**
     * Returns the keySet of the AbstractPropertiesProvider object.
     *
     * @return Set<String>
     */
    abstract Set<String> keySet();

    /**
     * add / update a property.
     *
     * @param key   of type String
     * @param value of type Object
     */
    void setProperty(final String key, final String value);

    void setProperty(String key, Object value);

    void setProperty(Class<?> className, String key, Object value);

    /**
     * Returns a list of properties whose keys start with the key specified.
     *
     * @param key that we must match with all the keys in properties
     * @return A Map of type <code>Map<String,Object></code> that contains properties whose keys start with the
     *         parameter specified.
     */
    public abstract Map<String, Object> getProperties(final String key);

    /**
     * Returns a list of properties whose keys start with the key specified. The final key is constructed by the
     * class.getCanonicalName()+ "." + key;
     *
     * @param clazz The reference of the class for which we should get the mapped value from the properties.
     * @param key   that we must match with all the keys in properties
     * @return A Map of type <code>Map<String,Object></code> that contains properties whose keys start with the
     *         parameter specified.
     */
    public abstract Map<String, Object> getProperties(final Class<?> clazz, final String key);

    /**
     * Returns a list of properties whose keys start with the key specified. The final key is constructed by the
     * class.getCanonicalName()+ "." + key;
     *
     * @param clazz       The reference of the class for which we should get the mapped value from the properties.
     * @param key         that we must match with all the keys in properties
     * @param cleaned     if the value is true the for example, if a property's key is "com.sourcen.test.myProp1" the cleaned
     *                    value in the HashMap would be "myProp1"
     * @param parseValues if the value is true all the properties that were returned for the criteria of the clazz, and
     *                    the key will be parsed into Integer,Boolean,Long or String.
     * @return A Map of type <code>Map<String,Object></code> that contains properties whose keys start with the
     *         parameter specified.
     */
    public abstract Map<String, Object> getProperties(final Class<?> clazz, final String key, final Boolean cleaned, final Boolean parseValues);

    /**
     * Returns a list of properties whose keys start with the key specified. The final key is constructed by the
     * class.getCanonicalName()+ "." + key;
     *
     * @param name        that we must match with all the keys in properties.
     * @param cleaned     if the value is true the for example, if a property's key is "com.test.myProp1" the cleaned value
     *                    in the HashMap would be "myProp1"
     * @param parseValues if the value is true all the properties that were returned for the criteria of the clazz, and
     *                    the key will be parsed into Integer,Boolean,Long or String.
     * @return A Map of type <code>Map<String,Object></code> that contains properties whose keys start with the
     *         parameter specified.
     */
    public abstract Map<String, Object> getProperties(final String name, final Boolean cleaned, final Boolean parseValues);

    /**
     * return a collection of propertyNames that starts with a particular key.
     *
     * @param name of type String
     * @return Collection<String>
     */
    public abstract Set<String> getPropertyNames(final String name, final boolean stripParentName);

    /**
     * Returns the String value to which the specified key is mapped, or {@code null} if this map contains no mapping
     * for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the String value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract String getProperty(final String key);

    /**
     * Returns the String value to which the specified key is mapped, or the defaultValue if this properties map
     * contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the String value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract String getProperty(final String key, final String defaultValue);

    /**
     * Returns the String value to which the specified key is mapped, or {@code null} if this map contains no mapping
     * for the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz The reference of the class for which we should get the mapped value from the properties map.
     * @param key   the key whose associated value is to be returned
     * @return the String value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract String getProperty(final Class<?> clazz, final String key);

    /**
     * Returns the String value to which the specified key is mapped, or the defaultValue if this properties map
     * contains no mapping for the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz        The reference of the class for which we should get the mapped value from the properties map.
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the String value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract String getProperty(final Class<?> clazz, final String key, final String defaultValue);

    /**
     * Returns the Long value to which the specified key is mapped, or {@code null} if this map contains no mapping for
     * the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the Long value to which the specified key is mapped, or {@code null} if there is not property for the key
     */
    public abstract Long getLongProperty(final String key);

    /**
     * Returns the Long value to which the specified key is mapped, or the defaultValue if this properties map contains
     * no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the Long value to which the specified key is mapped, or {@code null} if there is not property for the key
     */
    public abstract Long getLongProperty(final String key, final Long defaultValue);

    /**
     * Returns the Long value to which the specified key is mapped, or {@code null} if this map contains no mapping for
     * the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz The reference of the class for which we should get the mapped value from the properties map.
     * @param key   the key whose associated value is to be returned
     * @return the Long value to which the specified key is mapped, or {@code null} if there is not property for the key
     */
    public abstract Long getLongProperty(final Class<?> clazz, final String key);

    /**
     * Returns the Long value to which the specified key is mapped, or the defaultValue if this properties map contains
     * no mapping for the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz        The reference of the class for which we should get the mapped value from the properties map.
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the Long value to which the specified key is mapped, or {@code null} if there is not property for the key
     */
    public abstract Long getLongProperty(final Class<?> clazz, final String key, final Long defaultValue);

    /**
     * Returns the Integer value to which the specified key is mapped, or {@code null} if this map contains no mapping
     * for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the Integer value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Integer getIntegerProperty(final String key);

    /**
     * Returns the Integer value to which the specified key is mapped, or the defaultValue if this properties map
     * contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the Integer value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Integer getIntegerProperty(final String key, final Integer defaultValue);

    /**
     * Returns the Integer value to which the specified key is mapped, or {@code null} if this map contains no mapping
     * for the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz The reference of the class for which we should get the mapped value from the properties map.
     * @param key   the key whose associated value is to be returned
     * @return the Integer value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Integer getIntegerProperty(final Class<?> clazz, final String key);

    /**
     * Returns the Integer value to which the specified key is mapped, or the defaultValue if this properties map
     * contains no mapping for the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz        The reference of the class for which we should get the mapped value from the properties map.
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the Integer value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Integer getIntegerProperty(final Class<?> clazz, final String key, final Integer defaultValue);

    /**
     * Returns the Float value to which the specified key is mapped, or {@code null} if this map contains no mapping for
     * the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the Float value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Float getFloatProperty(final String key);

    /**
     * Returns the Float value to which the specified key is mapped, or the defaultValue if this properties map contains
     * no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the Float value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Float getFloatProperty(final String key, final Float defaultValue);

    /**
     * Returns the Float value to which the specified key is mapped, or {@code null} if this map contains no mapping for
     * the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz The reference of the class for which we should get the mapped value from the properties map.
     * @param key   the key whose associated value is to be returned
     * @return the Float value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Float getFloatProperty(final Class<?> clazz, final String key);

    /**
     * Returns the Float value to which the specified key is mapped, or the defaultValue if this properties map contains
     * no mapping for the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz        The reference of the class for which we should get the mapped value from the properties map.
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the Float value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Float getFloatProperty(final Class<?> clazz, final String key, final Float defaultValue);

    /**
     * Returns the Double value to which the specified key is mapped, or {@code null} if this map contains no mapping
     * for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the Double value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Double getDoubleProperty(final String key);

    /**
     * Returns the Double value to which the specified key is mapped, or the defaultValue if this properties map
     * contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the Double value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Double getDoubleProperty(final String key, final Double defaultValue);

    /**
     * Returns the Double value to which the specified key is mapped, or {@code null} if this map contains no mapping
     * for the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz The reference of the class for which we should get the mapped value from the properties map.
     * @param key   the key whose associated value is to be returned
     * @return the Double value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Double getDoubleProperty(final Class<?> clazz, final String key);

    /**
     * Returns the Double value to which the specified key is mapped, or the defaultValue if this properties map
     * contains no mapping for the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz        The reference of the class for which we should get the mapped value from the properties map.
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the Double value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Double getDoubleProperty(final Class<?> clazz, final String key, final Double defaultValue);

    /**
     * Returns the Boolean value to which the specified key is mapped, or {@code null} if this map contains no mapping
     * for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the Boolean value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Boolean getBooleanProperty(final String key);

    /**
     * Returns the Boolean value to which the specified key is mapped, or the defaultValue if this properties map
     * contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the Boolean value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Boolean getBooleanProperty(final String key, final Boolean defaultValue);

    /**
     * Returns the Boolean value to which the specified key is mapped, or {@code null} if this map contains no mapping
     * for the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz The reference of the class for which we should get the mapped value from the properties map.
     * @param key   the key whose associated value is to be returned
     * @return the Boolean value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Boolean getBooleanProperty(final Class<?> clazz, final String key);

    /**
     * Returns the Boolean value to which the specified key is mapped, or the defaultValue if this properties map
     * contains no mapping for the key.
     * <p/>
     * The final key is constructed by the class.getCanonicalName()+ "." + key;
     *
     * @param clazz        The reference of the class for which we should get the mapped value from the properties map.
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to be returned if there is no mapping for the key in properties map
     * @return the Boolean value to which the specified key is mapped, or {@code null} if there is not property for the
     *         key
     */
    public abstract Boolean getBooleanProperty(final Class<?> clazz, final String key, final Boolean defaultValue);

    /**
     * Returns the raw object value.
     *
     * @param key the key whose associated value is to be returned
     * @return the raw object value to which the specified key is mapped. this is based on the constructor's Map
     *         parameter passed.
     */
    public abstract Object getObjectProperty(final String key);


    //
    // xml based operations
    //

    public abstract void loadFromXML(final String path) throws IOException;

    public abstract void loadFromXML(final InputStream stream) throws IOException;

    public abstract void saveToXML(final String path) throws IOException;

    public abstract void saveToXML(final String path, final String comment) throws IOException;

    public abstract void saveToXML(final OutputStream stream) throws IOException;

    public abstract void saveToXML(final OutputStream stream, final String comment) throws IOException;

    public Boolean supportsPersistence();

    public void refresh();

    public int size();

}
