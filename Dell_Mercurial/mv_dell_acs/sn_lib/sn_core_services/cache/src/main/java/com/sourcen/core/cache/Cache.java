/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Just a simple wrapper for the cache.
 * <p/>
 * The simple purpose of creating our own cache interface is to prevent issues while migrating from one cache
 * implementation to another. Since all the caches returned from the CacheFactory are of type
 * {@link Cache} we can change the implementation from EhCache to Tangasol or other cache providers
 * if required.
 * <p/>
 * The cache extends {@link net.sf.ehcache.Cache} which is potentially supposed to become the javax.cache.Cache and is
 * <p/>
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 * @since 1.01
 */
public interface Cache {

    public String getName();

    public void setName(String name);

    public int size();

    public boolean isEmpty();

    public boolean containsKey(Object key);

    public boolean containsValue(Object value);

    public Object put(Object key, Object value);

    public Object getValue(Object key);

    public void putAll(Map<?, ?> map);

    public void clear();

    public Set<?> keySet();

    public Collection<?> values();

    /**
     * Please reduce the useage of this method unless needed. Its very expensive to interate over the entire cache.
     *
     * @return Set<Map.Entry>
     */
    public Set<Entry<?, ?>> entrySet();

    public Map<String, Object> getCacheStatistics();
}
