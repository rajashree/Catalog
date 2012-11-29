/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs.jpa;

import com.sourcen.core.util.collections.AbstractPropertiesProvider;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The map implementation for the list of properties that a record can have.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 1292 $, $Date:: 2012-04-04 23:24:45#$
 * @since 1.01
 */
public final class RecordProperties extends AbstractPropertiesProvider implements Map<String, String>, Cloneable, Serializable {

    //
    // =================================================================================================================
    // == Class Fields ==
    // =================================================================================================================
    //

    /**
     * stores all the properties for the identifiable.
     */
    private transient volatile Map<String, String> backingMap;

    /**
     * stores the states of the properties.
     */
    private Map<String, States> propertyStates;

    /**
     * The states of a record property.
     */
    private static enum States {
        /**
         * property was added.
         */
        INSERT,

        /**
         * property was updated.
         */
        UPDATE,

        /**
         * property was deleted.
         */
        DELETE,

        /**
         * property that was not modified.
         */
        UNMODIFIED
    }

    //
    // =================================================================================================================
    // == Constructors ==
    // =================================================================================================================
    //

    /**
     * Creates a new RecordProperties instance.
     */
    RecordProperties() {
        this.backingMap = new ConcurrentHashMap<String, String>(10);
        this.propertyStates = new ConcurrentHashMap<String, States>(10);
    }

    //
    // =================================================================================================================
    // == Map<String, String> Implementation ==
    // =================================================================================================================
    //

    @Override
    public int size() {
        return this.backingMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.backingMap.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.backingMap.containsValue(value);
    }

    @Override
    public String get(final Object key) {
        return this.backingMap.get(key);
    }

    @Override
    public String put(final String key, final String value) {
        if (value == null) {
            return remove(key);
        }

        if (this.propertyStates.containsKey(key)) {
            this.propertyStates.put(key, States.UPDATE);
        } else {
            this.propertyStates.put(key, States.INSERT);
        }
        return this.backingMap.put(key, value);
    }

    @Override
    public String remove(final Object key) {
        final String stringKey = key.toString();
        if (this.propertyStates.containsKey(stringKey)) {
            this.propertyStates.put(stringKey, States.DELETE);
        }
        if (this.backingMap.containsKey(stringKey)) {
            return this.backingMap.remove(stringKey);
        }
        return null;
    }

    @Override
    public void putAll(final Map<? extends String, ? extends String> map) {
        this.backingMap.putAll(map);
        for (final String key : map.keySet()) {
            this.propertyStates.put(key, States.UPDATE);
        }
    }

    @Override
    public void clear() {
        this.backingMap.clear();
        for (final String key : this.propertyStates.keySet()) {
            if (this.propertyStates.get(key) == States.INSERT) {
                this.propertyStates.remove(key);
            } else {
                this.propertyStates.put(key, States.DELETE);
            }
        }
    }

    @Override
    public Set<String> keySet() {
        return this.backingMap.keySet();
    }

    @Override
    public Collection<String> values() {
        return this.backingMap.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return this.backingMap.entrySet();
    }

    //
    // =================================================================================================================
    // == Properties Implementation ==
    // =================================================================================================================
    //

    @Override
    public Object getObjectProperty(final String key) {
        return get(key);
    }

    @Override
    public boolean hasProperty(final String key) {
        return containsKey(key);
    }

    @Override
    public void setProperty(final String key, final String value) {
        put(key, value);
    }

    //
    // =================================================================================================================
    // == Helper Methods ==, Repository required methods.
    // =================================================================================================================
    //

    /**
     * loads the RecordProperties with the map where all propertyStates are unmodified.
     *
     * @param map of type Map<? extends String, ? extends String>
     */
    public void loadAll(final Map<? extends String, ? extends String> map) {
        this.backingMap.putAll(map);
        for (final String key : map.keySet()) {
            this.propertyStates.put(key, States.UNMODIFIED);
        }
    }

    /**
     * loads the RecordProperties with the map where all propertyStates are unmodified.
     *
     * @param key   of type String
     * @param value of type String
     */
    public void load(final String key, final String value) {
        this.backingMap.put(key, value);
        this.propertyStates.put(key, States.UNMODIFIED);
    }

    /**
     * Returns the insertedProperties of this RecordProperties object.
     *
     * @return insertedProperties of this object.
     */
    public Map<String, String> getInsertedProperties() {
        return getDbProperties(States.INSERT);
    }

    /**
     * Returns the updatedProperties of this RecordProperties object.
     *
     * @return updatedProperties of this object.
     */
    public Map<String, String> getUpdatedProperties() {
        return getDbProperties(States.UPDATE);
    }

    /**
     * Returns the removedProperties of this RecordProperties object.
     *
     * @return removedProperties of this object.
     */
    public Map<String, String> getDeletedProperties() {
        return getDbProperties(States.DELETE);
    }

    /**
     * returns a list of properties for different record state.
     *
     * @param state of type States
     *
     * @return Map<String, String>
     */
    private Map<String, String> getDbProperties(final States state) {
        final HashMap<String, String> map = new HashMap<String, String>(this.propertyStates.size() / 3);
        for (final Entry<String, States> entry : this.propertyStates.entrySet()) {
            if (entry.getValue() == state) {
                map.put(entry.getKey(), this.backingMap.get(entry.getKey()));
            }
        }
        return map;
    }

    /**
     * clear all the propertyState flags of Status.INSERT Type.
     */
    public void clearInsertedStateFlags() {
        clearStateFlag(States.INSERT);
    }

    /**
     * clear all the propertyState flags of Status.UPDATE Type.
     */
    public void clearUpdatedStateFlags() {
        clearStateFlag(States.UPDATE);
    }

    /**
     * clear all the propertyState flags of Status.DELETE Type.
     */
    public void clearDeletedStateFlags() {
        for (final Entry<String, States> entry : this.propertyStates.entrySet()) {
            if (entry.getValue() == States.DELETE) {
                this.propertyStates.remove(entry.getKey());
            }
        }
    }

    /**
     * helper method to clear the propertyStates map.
     *
     * @param state for which we need to refresh it to States.UNMODIFIED.
     */
    private void clearStateFlag(final States state) {
        for (final Entry<String, States> entry : this.propertyStates.entrySet()) {
            if (entry.getValue() == state) {
                entry.setValue(States.UNMODIFIED);
            }
        }
    }

    /**
     * returns a deepclone of the recordProperties.
     *
     * @return RecordProperties
     */
    @Override
    protected Object clone() {
        final RecordProperties rp = new RecordProperties();
        rp.backingMap = new ConcurrentHashMap<String, String>(this.backingMap.size());
        rp.backingMap.putAll(this.backingMap);

        rp.propertyStates = new ConcurrentHashMap<String, States>(this.propertyStates.size());
        rp.propertyStates.putAll(this.propertyStates);
        return rp;
    }

    @Override
    public String toString() {
        return "RecordProperties{" + this.backingMap.toString() + "} ";
    }

    @Override
    public void refresh() {

    }
}
