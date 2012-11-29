/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.cache;

import com.sourcen.core.InvalidArgumentException;
import com.sourcen.core.services.Service;

public interface CacheProvider extends Service {

    /**
     * default cache size.
     */
    public final Integer DEFAULT_CACHE_SIZE = 10000;

    /**
     * default cache timeToLive.
     */
    public final Long DEFAULT_CACHE_TIMETOLIVE = 600L;

    /**
     * default cache timeToIdle.
     */
    public final Long DEFAULT_CACHE_TIMETOIDLE = 120L;

    /**
     * Returns an {@link Cache} implementation identified by a unique id.
     * <p/>
     * All caches must be configured using the configurationService.
     * <p/>
     * A cache has properties like size, timeToLive, timeToIdle, eternal and overflowToDisk which can be customized. To
     * set a size for a specific cache we just need to set the a property as
     * <p/>
     * <pre>
     * system.caches.[cacheId].size=1000
     * </pre>
     * <p/>
     * in the database. If the property doesn't exist then we check for a global cache property
     * com.sourcen.core.cache.CacheConfigurer.size, and even if this property doesn't exist then we just set a default size
     * of 10,000. This will allow us total control over the cache management just from the database without having to
     * modify the application.
     *
     * @param id unique string that identifies this cache.
     *
     * @return Cache
     */
    public Cache getCache(final String id);

    public boolean containsCache(final String id);

    public void removeCache(final String id);

    /**
     * Returns an {@link Cache} implementation identified by a unique id, and provides options to customize the cache
     * properties.
     *
     * @param id             unique string that identifies this cache.
     * @param size           of the cache.
     * @param timeToLive     in seconds, the maximum number of seconds an object can live within the cache before it gets
     *                       removed.
     * @param timeToIdle     in seconds.
     * @param eternal        is set to true if we want to persist the cache even after system restarts.
     * @param overflowToDisk if the cache can be saved to disk if the cache is full.
     *
     * @return Cache Proxy instance of the Cache implementation wrapped by the {@link Cache}.
     * @see net.sf.ehcache.CacheManager
     */
    public Cache getCache(final String id, final int size, final long timeToLive, final long timeToIdle, final boolean eternal, final boolean overflowToDisk);

    public Status getStatus();

    String[] getCacheIds();

    public static final class Status {

        public static final Status UNINITIALISED = new Status(0, "UNINITIALISED");
        public static final Status INITIALIZED = new Status(1, "INITIALIZED");
        public static final Status DESTROYED = new Status(2, "DESTROYED");

        private final Integer id;
        private final String name;

        public Status(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static Status get(Integer id) {
            if (id.equals(0)) return UNINITIALISED;
            if (id.equals(1)) return INITIALIZED;
            if (id.equals(2)) return DESTROYED;
            throw new InvalidArgumentException("Status with id:=" + id + " not found");
        }

        public Boolean is(Status status) {
            return status.getId().equals(this.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode() * name.hashCode() * 31;
        }
    }

}
