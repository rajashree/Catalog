/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.cache.ehcache;

import com.sourcen.core.cache.CacheException;
import net.sf.ehcache.Ehcache;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.hibernate.cache.ehcache.internal.regions.EhcacheCollectionRegion;
import org.hibernate.cache.ehcache.internal.regions.EhcacheQueryResultsRegion;
import org.hibernate.cache.ehcache.internal.regions.EhcacheTimestampsRegion;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.hibernate.cache.spi.TimestampsRegion;
import org.hibernate.cfg.Settings;
import org.springframework.util.Assert;

import java.util.Properties;

/**
 * had to override the build Region methods so that we can use our CacheFactory.getCache method which will then use the
 * configurationService for the properties.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public final class HibernateEhCacheRegionFactory extends EhCacheRegionFactory {

    public HibernateEhCacheRegionFactory() {
        super();
    }

    public HibernateEhCacheRegionFactory(final Properties prop) {
        super(prop);
    }


    @Override
    public void start(final Settings settings, final Properties properties) throws CacheException {
        super.start(settings, properties);
        // register this as our ORMCacheManager
        EhCacheServiceImpl.getInstance().setOrmCacheManager(new EhCacheProviderImpl("orm", this.manager));
    }


    @Override
    public EntityRegion buildEntityRegion(final String regionName, final Properties properties, final CacheDataDescription metadata) throws CacheException {
        return new org.hibernate.cache.ehcache.internal.regions.EhcacheEntityRegion(accessStrategyFactory, getCache(regionName), settings, metadata, properties);
    }

    @Override
    public CollectionRegion buildCollectionRegion(final String regionName, final Properties properties, final CacheDataDescription metadata) throws org.hibernate.cache.CacheException {
        return new EhcacheCollectionRegion(accessStrategyFactory, getCache(regionName), settings, metadata, properties);
    }

    @Override
    public QueryResultsRegion buildQueryResultsRegion(final String regionName, final Properties properties) throws org.hibernate.cache.CacheException {
        return new EhcacheQueryResultsRegion(accessStrategyFactory, getCache(regionName), properties);
    }

    @Override
    public TimestampsRegion buildTimestampsRegion(final String regionName, final Properties properties) throws org.hibernate.cache.CacheException {
        return new EhcacheTimestampsRegion(accessStrategyFactory, getCache(regionName), properties);
    }

    protected Ehcache getCache(final String name) throws CacheException {
        try {
            final Ehcache cache = (Ehcache) EhCacheServiceImpl.getInstance().getOrmCacheManager().getCache("caches.orm." + name);
            Assert.notNull(cache);
            return cache;
        } catch (final net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }
}
