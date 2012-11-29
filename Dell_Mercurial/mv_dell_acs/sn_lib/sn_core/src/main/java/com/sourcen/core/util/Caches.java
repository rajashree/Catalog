/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util;

import com.google.common.collect.MapMaker;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * one spot to create and manage all system wide caching. This helps us to clear all caches and manage them
 * effectively.
 * NOTE :: THIS IS NOT A FULL SYSTEM CACHE REPLACEMENT.
 * Use the only for STATIC FINAL VARIABLES THAT YOU WANT TO CLEAR LATER and handle easily.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: vivekk $
 * @version $Revision: 3050 $, $Date:: 2012-06-08 13:42:27#$
 */
public class Caches {

    public static final ObjectCacheContainer OBJECTS = new ObjectCacheContainer();
    public static final DecayAwareMapCacheContainer MAPS = new DecayAwareMapCacheContainer();
    public static final DecayAwareSetCacheContainer SETS = new DecayAwareSetCacheContainer();

    private Caches() {

    }

    public static void clearCaches() {
        synchronized (MAPS) {
            synchronized (MAPS.cacheContainer) {
                MAPS.cacheContainer.clear();
            }
        }

        synchronized (SETS) {
            synchronized (SETS.cacheContainer) {
                SETS.cacheContainer.clear();
            }
        }
    }

    //
    // Helper classes.
    //

    public static final class ObjectCacheContainer extends CacheContainer<Object> {
        @Override
        protected Object newCacheInstance(final boolean autoDecay) {
            throw new UnsupportedOperationException("This method is not implemented for Object Caches.");
        }
    }


    public static final class DecayAwareMapCacheContainer extends CacheContainer<Map> {
        @Override
        protected Map newCacheInstance(boolean autoDecay) {
            if (autoDecay) {
                return new MapMaker().concurrencyLevel(16).initialCapacity(1000).softValues().makeMap();
            } else {
                return new MapMaker().concurrencyLevel(16).initialCapacity(1000).makeMap();
            }
        }
    }


    public static final class DecayAwareSetCacheContainer extends CacheContainer<Set> {
        @Override
        protected Set newCacheInstance(boolean autoDecay) {
            if (autoDecay) {
                return new MapBackedSet(new MapMaker().concurrencyLevel(16).initialCapacity(1000).softValues().makeMap());
            } else {
                return new MapBackedSet(new MapMaker().concurrencyLevel(16).initialCapacity(1000).makeMap());
            }
        }
    }

    //
    // abstract classes
    //

    private static abstract class CacheContainer<T> {

        protected final Map<String, T> cacheContainer = new ConcurrentHashMap<String, T>(1000);

        public CacheContainer() {
        }

        public <R extends T> R get(Class clazz, String id) {
            id = clazz.getCanonicalName() + "." + id;
            if (cacheContainer.containsKey(id)) {
                return (R) cacheContainer.get(id);
            }
            // no id
            T newInstance = newCacheInstance(true);
            if (newInstance == null) {
                return null;
            }
            cacheContainer.put(id, newInstance);
            return (R) newInstance;
        }

        public <R extends T> R get(String id) {
            if (cacheContainer.containsKey(id)) {
                return (R) cacheContainer.get(id);
            }
            // no id
            T newInstance = newCacheInstance(true);
            if (newInstance == null) {
                return null;
            }
            cacheContainer.put(id, newInstance);
            return (R) newInstance;
        }

        public <R extends T> R get(String id, boolean autoDecay) {
            if (cacheContainer.containsKey(id)) {
                return (R) cacheContainer.get(id);
            }
            // no id
            T newInstance = newCacheInstance(autoDecay);
            if (newInstance == null) {
                return null;
            }
            cacheContainer.put(id, newInstance);
            return (R) newInstance;
        }

        public <R> R get(String id, final T defaultCache) {
            final T cache = cacheContainer.get(id);
            if (cache == null) {
                cacheContainer.put(id, defaultCache);
                return (R) defaultCache;
            }
            return (R) cache;
        }

        public Collection<String> getKeys() {
            return cacheContainer.keySet();
        }

        protected abstract T newCacheInstance(boolean autoDecay);
    }


    private static final class MapBackedSet<E> extends AbstractSet<E> {

        private static final Object EMPTY_VALUE = new Object();
        private Map<E, Object> backingMap;

        public MapBackedSet(Map backingMap) {
            this.backingMap = backingMap;
        }

        @Override
        public Iterator<E> iterator() {
            return backingMap.keySet().iterator();
        }

        @Override
        public int size() {
            return backingMap.keySet().size();
        }

        @Override
        public boolean add(E e) {
            return backingMap.put(e, EMPTY_VALUE) != null;
        }
    }

}
