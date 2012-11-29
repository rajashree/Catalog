/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.cache.ehcache;

import com.sourcen.core.App;
import com.sourcen.core.cache.CacheProvider;
import com.sourcen.core.cache.CacheServiceImpl;
import com.sourcen.core.config.ConfigurationService;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 */
public class EhCacheServiceImpl extends CacheServiceImpl {

    /**
     * singleton instance place holder.
     */
    private static EhCacheServiceImpl instance;
    private ConfigurationService configurationService;

    /**
     * Returns an instance of the CacheFactory if it already exists, or creates a new instance an returns it.
     *
     * @return CacheFactory instance
     */
    public static EhCacheServiceImpl getInstance() {
        if (instance == null) {
            instance = new EhCacheServiceImpl();
            instance.configurationService = App.getService(ConfigurationService.class);
        }
        return instance;
    }

    private EhCacheServiceImpl() {
        super();
    }

    @Override
    protected CacheProvider createSystemCacheManager() {
        return new EhCacheProviderImpl("system", "/config/cache/ehcache-system.xml");
    }

    @Override
    protected CacheProvider createOrmCacheManager() {
        return new EhCacheProviderImpl("orm", "/config/cache/ehcache-orm.xml");
    }

}
