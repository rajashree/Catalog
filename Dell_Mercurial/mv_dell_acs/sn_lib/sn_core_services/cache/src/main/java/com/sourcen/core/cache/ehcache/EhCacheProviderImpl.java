package com.sourcen.core.cache.ehcache;

import com.sourcen.core.App;
import com.sourcen.core.cache.Cache;
import com.sourcen.core.cache.CacheException;
import com.sourcen.core.cache.CacheProvider;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.services.ServiceImpl;
import com.sourcen.core.util.FileUtils;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.TerracottaConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class EhCacheProviderImpl extends ServiceImpl implements CacheProvider {

    private static final Logger log = LoggerFactory.getLogger(EhCacheProviderImpl.class);

    /**
     * default cache size.
     */
    private static final int DEFAULT_CACHE_SIZE = 10000;

    /**
     * default cache timeToLive.
     */
    private static final long DEFAULT_CACHE_TIMETOLIVE = 600L;

    /**
     * default cache timeToIdle.
     */
    private static final long DEFAULT_CACHE_TIMETOIDLE = 120;


    private CacheManager cacheManager;
    private Configuration configuration;
    private String cacheManagerName;
    private ConfigurationService configurationService = App.getService(ConfigurationService.class);

    public EhCacheProviderImpl(String cacheManagerName, String configUrl) {
        super("SourcenCacheManager." + cacheManagerName);
        this.cacheManagerName = cacheManagerName;
        final URL url = FileUtils.loadResource(configUrl);
        configuration = ConfigurationFactory.parseConfiguration(url);
        initialize();
    }

    public EhCacheProviderImpl(String cacheManagerName, CacheManager cacheManager) {
        super("SourcenCacheManager." + cacheManagerName);
        this.cacheManagerName = cacheManagerName;
        this.cacheManager = cacheManager;
        this.cacheManager.setName(cacheManagerName);
        initialize();

    }


    @Override
    public Cache getCache(final String id) {

        // if the configurationService was set, then just get the properties from the
        // configurationService, or else use the default values.

        final Ehcache cache = getCacheManager().getEhcache(id);

        if (cache != null) {
            return (Cache) cache;
        } else if (configurationService != null) {
            final int size = configurationService.getIntegerProperty(getId("size"),
                    configurationService.getIntegerProperty("caches.defaultCache.size", DEFAULT_CACHE_SIZE));

            final long timeToLive = configurationService.getLongProperty(getId("timeToLive"),
                    configurationService.getLongProperty("caches.defaultCache.timeToLive", DEFAULT_CACHE_TIMETOLIVE));

            final long timeToIdle = configurationService.getLongProperty(getId("timeToIdle"),
                    configurationService.getLongProperty("caches.defaultCache.timeToIdle", DEFAULT_CACHE_TIMETOIDLE));

            final boolean eternal = configurationService.getBooleanProperty(getId("eternal"),
                    configurationService.getBooleanProperty("caches.defaultCache.eternal", false));

            final boolean overflowToDisk = configurationService.getBooleanProperty(getId("overflowToDisk"),
                    configurationService.getBooleanProperty("caches.defaultCache.overflowToDisk", false));

            return getCache(id, size, timeToLive, timeToIdle, eternal, overflowToDisk);

        } else {
            return getCache(id, DEFAULT_CACHE_SIZE, DEFAULT_CACHE_TIMETOLIVE, DEFAULT_CACHE_TIMETOLIVE, false, false);
        }
    }

    @Override
    public void removeCache(final String id) {
        getCacheManager().removeCache(id);
    }

    @Override
    public boolean containsCache(final String id) {
        return getCacheManager().cacheExists(id);
    }

    private String getId(String id) {
        return "caches." + cacheManagerName + "." + id + ".";
    }

    @Override
    public Cache getCache(final String id, final int size, final long timeToLive, final long timeToIdle, final boolean eternal, final boolean overflowToDisk) {
        if (!getCacheManager().cacheExists(id)) {

            // we forgot to create a cache by the id, so we should just create
            // a new one by that id.
            if (log.isDebugEnabled()) {
                log.debug("Cache '" + id + "' doesn't exist, creating cache with properties === " + "id:= " + id + ", size:= " + size + ", eternal:= "
                        + eternal + ", overflowToDisk:= " + overflowToDisk + ", timeToLive:= " + timeToLive + ", timeToIdle:= " + timeToIdle);
            }
            // add the cache
            final EhCacheImpl cache = new EhCacheImpl(id, size, eternal, overflowToDisk, timeToLive, timeToIdle);
            if(getCacheManager().cacheExists(id)){
                getCacheManager().removeCache(id);
            }
            getCacheManager().addCache((net.sf.ehcache.Cache) cache);
            cache.setStatisticsEnabled(true);
            verifyCache(cache);
            return cache;
        }
        return (Cache) getCacheManager().getCache(id);
    }

    @Override
    public String[] getCacheIds() {
        return getCacheManager().getCacheNames();
    }

    protected void verifyCache(final Cache cache) {
        final CacheConfiguration cacheConfig = ((Ehcache) cache).getCacheConfiguration();
        if (cacheConfig.isTerracottaClustered()) {
            final TerracottaConfiguration tcConfig = cacheConfig.getTerracottaConfiguration();
            switch (tcConfig.getValueMode()) {
                case IDENTITY:
                    throw new CacheException("The clustered cache " + cache.getName() + " is using IDENTITY value mode.\n"
                            + "Identity value mode cannot be used with cache regions.");
                case SERIALIZATION:
                default:
                    // this is the recommended valueMode
                    break;
            }
        }
        //   ((Ehcache) cache).setStatisticsEnabled(configurationService.getBooleanProperty("caches.statistics.enabled", true));
    }


    protected CacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    public Status getStatus() {
        if (cacheManager == null) {
            return Status.UNINITIALISED;
        }

        return Status.get(cacheManager.getStatus().intValue());
    }


    @Override
    public void initialize() {
        if (this.cacheManager == null && configuration != null) {
            this.cacheManager = CacheManager.create(configuration);
            this.cacheManager.setName(cacheManagerName);
        }
        super.initialize();
    }

    @Override
    public void refresh() {
        destroy();
        initialize();
        super.refresh();
    }

    @Override
    public void destroy() {
        if (configuration != null) {
            this.cacheManager.shutdown();
        }
        this.cacheManager = null;
    }

}
