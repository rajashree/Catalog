/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.cache;

import com.sourcen.core.cache.ehcache.EhCacheServiceImpl;
import com.sourcen.core.services.DefaultImplementation;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3030 $, $Date:: 2012-06-08 10:22:36#$
 */
@DefaultImplementation(EhCacheServiceImpl.class)
public interface CacheService extends CacheProvider {

    @SuppressWarnings("unchecked")
    <T extends Map<?, ?>> T getCacheMap(String id, Map<?, ?> defaultCache);

    <T extends Map<?, ?>> T getCacheMap(String id, boolean autoDecay);

    @SuppressWarnings("unchecked")
    <T extends Set<?>> T getCacheSet(String id, Set<?> defaultCache);

    <T extends Set<?>> T getCacheSet(final String id, boolean autoDecay);

    @SuppressWarnings("unchecked")
    <T> T getSingletonObject(String id, Object defaultObject);

    File getFileCacheDirectory() throws IOException;

    public CacheProvider getSystemCacheManager();

    public CacheProvider getOrmCacheManager();

}
