/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.collections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3032 $, $Date:: 2012-06-08 10:35:20#$
 */
public class MapBackedPropertiesProvider extends AbstractPropertiesProvider {

    // I am just creating a new map as this is usually the default behaviour.
    protected Map<Object, Object> map;
    protected boolean useAsReference = false;


    public MapBackedPropertiesProvider() {
        super();
        map = new ConcurrentHashMap(20);
    }

    public MapBackedPropertiesProvider(Map backedMap) {
        this(backedMap, false);
    }

    public MapBackedPropertiesProvider(Map backedMap, boolean useAsReference) {
        super();
        this.useAsReference = useAsReference;
        if (useAsReference) {
            map = backedMap;
        } else {
            map = new ConcurrentHashMap(20);
            map.putAll(backedMap);
        }
    }

    @Override
    public boolean hasProperty(String key) throws NullPointerException {
        return map.containsKey(key);
    }

    @Override
    public Set<String> keySet() {
        return (Set) map.keySet();
    }

    @Override
    public void setProperty(String key, String value) {
        map.put(key, value);
    }

    @Override
    public Object getObjectProperty(String key) {
        return map.get(key);
    }

    @Override
    public void refresh() {
        // dont refresh anything on the map.
    }

    // but we will allow extended classes to reload the map
    protected void refresh(Map backedMap) {
        if (useAsReference) {
            map = backedMap;
        } else {
            map.clear();
            map.putAll(backedMap);
        }
    }
}
