/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.cache.ehcache;

import com.sourcen.core.cache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Statistics;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Constructs a {@link com.sourcen.core.cache.Cache} implementation based on the current cache provider.which should be {@link net.sf.ehcache.Ehcache}
 * compatible. All Caches returned by {@link EhCacheServiceImpl#getInstance()#getCache} will be of type CacheImpl.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 * @since 1.01
 */
final class EhCacheImpl extends net.sf.ehcache.Cache implements Cache {

    public EhCacheImpl(final String name, final int maxElementsInMemory, final boolean overflowToDisk, final boolean eternal, final long timeToLiveSeconds,
                       final long timeToIdleSeconds) {
        super(name, maxElementsInMemory, overflowToDisk, eternal, timeToLiveSeconds, timeToIdleSeconds);
    }

    @Override
    public int size() {
        return super.getSize();
    }

    @Override
    public boolean isEmpty() {
        return super.getSize() == 0;
    }

    @Override
    public boolean containsKey(final Object key) {
        return super.get(key) != null;
    }

    @Override
    public boolean containsValue(final Object value) {
        return super.get(value) != null;
    }

    @Override
    public Object put(final Object key, final Object value) {
        final Element element = new Element(key, value);
        super.put(element);
        return element;
    }

    @Override
    public void putAll(final Map map) {
        final Set<Map.Entry> entries = map.entrySet();
        for (final Map.Entry entry : entries) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Object getValue(final Object key) {
        Element element = super.get(key);
        if(element != null){
            return element.getValue();
        }
        return null;
    }

    @Override
    public void clear() {
        super.removeAll();
        super.clearStatistics();
    }

    @Override
    public Set<?> keySet() {
        return new HashSet(getKeysNoDuplicateCheck());
    }

    /**
     * Please reduce the usage of this method unless needed. Its very expensive to iterate over the entire cache.
     *
     * @return Set<Map.Entry>
     */
    @Override
    public Collection<?> values() {
        return super.getAllWithLoader(getKeys(), null).values();
    }

    /**
     * Please reduce the usage of this method unless needed. Its very expensive to iterate over the entire cache.
     *
     * @return Set<Map.Entry>
     */
    @Override
    public Set entrySet() {
        return super.getAllWithLoader(getKeys(), null).entrySet();
    }

    @Override
    public Map<String, Object> getCacheStatistics() {
        Statistics stats = getStatistics();

        Map<String, Object> result = new HashMap<String, Object>(25);

        result.put("size", stats.getObjectCount());
        result.put("memoryStoreSize", stats.getMemoryStoreObjectCount());
        result.put("offHeapStoreSize", stats.getOffHeapStoreObjectCount());
        result.put("diskStoreSize", stats.getDiskStoreObjectCount());

        result.put("cacheHits", stats.getCacheHits());
        result.put("cacheMisses", stats.getCacheMisses());

        result.put("onDiskHits", stats.getOnDiskHits());
        result.put("onDiskMisses", stats.getOnDiskMisses());

        result.put("offHeapHits", stats.getOffHeapHits());
        result.put("offHeapMisses", stats.getOffHeapMisses());

        result.put("inMemoryHits", stats.getInMemoryHits());
        result.put("inMemoryMisses", stats.getInMemoryMisses());

        result.put("averageGetTime", stats.getAverageGetTime());
        result.put("searchesPerSecond", stats.getSearchesPerSecond());
        result.put("averageSearchTime", stats.getAverageSearchTime());

        result.put("evictionCount", stats.getEvictionCount());
        result.put("writerQueueLength", stats.getWriterQueueSize());

        if (stats.getCacheHits() != 0) {
            float percentage = Math.round(((stats.getCacheHits() - stats.getCacheMisses() * 1.0F) / stats.getCacheHits()) * 100);
            if (percentage < 0) {
                percentage = 0; // some times misses can be more than hits when data is initially loaded.
            }
            result.put("effectivePercentage", percentage);
        } else {
            result.put("effectivePercentage", 0);
        }

        return result;
    }
}
