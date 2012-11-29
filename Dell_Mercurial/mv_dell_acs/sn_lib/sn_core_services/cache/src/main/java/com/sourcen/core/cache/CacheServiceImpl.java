/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.cache;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.services.ServiceImpl;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.Caches;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class CacheServiceImpl extends ServiceImpl implements CacheService {

    private CacheProvider systemCacheManager = null;

    private CacheProvider ormCacheManager = null;

    protected CacheServiceImpl() {
    }

    private Status status = Status.UNINITIALISED;

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public synchronized void initialize() {

        if (this.systemCacheManager == null || !this.systemCacheManager.getStatus().is(Status.INITIALIZED)) {
            logger.info("systemCacheManager is not alive, creating new one");
            this.systemCacheManager = createSystemCacheManager();
        }

        if (this.ormCacheManager != null && !this.ormCacheManager.getStatus().is(Status.INITIALIZED)) {
            logger.warn("ormCacheManager is not alive, creating new one");
            this.ormCacheManager = createOrmCacheManager();
        }
        super.initialize();
        status = Status.INITIALIZED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void destroy() {
        Caches.clearCaches();
        if (this.systemCacheManager != null) {
            this.systemCacheManager.destroy();
        }
        if (this.ormCacheManager != null) {
            this.ormCacheManager.destroy();
        }
        super.destroy();
        status = Status.DESTROYED;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Map<?, ?>> R getCacheMap(final String id, final Map<?, ?> defaultCache) {
        return (R) Caches.MAPS.get(id, defaultCache);
    }

    public <R extends Map<?, ?>> R getCacheMap(final String id, final boolean autoDecay) {
        return (R) Caches.MAPS.get(id, autoDecay);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Set<?>> T getCacheSet(final String id, final Set<?> defaultCache) {
        return (T) getCacheSet(id, defaultCache);
    }

    @SuppressWarnings("unchecked")
    public <T extends Set<?>> T getCacheSet(final String id, boolean autoDecay) {
        return (T) Caches.SETS.get(id, autoDecay);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSingletonObject(final String id, final Object defaultObject) {
        return (T) Caches.OBJECTS.get(id, defaultObject);
    }

    @Override
    public Cache getCache(final String name) {
        return getSystemCacheManager().getCache(name);
    }

    @Override
    public Cache getCache(final String id, final int size, final long timeToLive, final long timeToIdle, final boolean eternal, final boolean overflowToDisk) {
        return getSystemCacheManager().getCache(id, size, timeToLive, timeToIdle, eternal, overflowToDisk);
    }

    //
    // getters / setters
    //

    /**
     * @return the systemCacheManager
     */
    public CacheProvider getSystemCacheManager() {
        Assert.notNull(this.systemCacheManager, "The systemCacheManager has not yet been initialized.");
        return this.systemCacheManager;
    }

    /**
     * @return the ormCacheManager
     */
    public CacheProvider getOrmCacheManager() {
        Assert.notNull(this.ormCacheManager, "The ormCacheManager has not yet been initialized.");
        return this.ormCacheManager;
    }

    public void setOrmCacheManager(final CacheProvider ormCacheManager) {
        this.ormCacheManager = ormCacheManager;
    }

    public void setSystemCacheManager(CacheProvider systemCacheManager) {
        this.systemCacheManager = systemCacheManager;
    }

    @Override
    public File getFileCacheDirectory() throws IOException {
        return App.getService(ConfigurationService.class).getFileSystem().getDirectory("caches");
    }


    @Override
    public String[] getCacheIds() {
        Collection<String> ids = new ArrayList<String>(100);
        ids.addAll(Arrays.asList(getOrmCacheManager().getCacheIds()));
        ids.addAll(Arrays.asList(getSystemCacheManager().getCacheIds()));
        return ids.toArray(new String[ids.size()]);
    }

    @Override
    public void removeCache(final String id) {
        if (getSystemCacheManager().containsCache(id)) {
            getSystemCacheManager().removeCache(id);
        }
        if (getOrmCacheManager().containsCache(id)) {
            getOrmCacheManager().removeCache(id);
        }
    }

    @Override
    public boolean containsCache(final String id) {
        return getSystemCacheManager().containsCache(id) || getOrmCacheManager().containsCache(id);
    }

    protected abstract CacheProvider createSystemCacheManager();

    protected abstract CacheProvider createOrmCacheManager();


}
