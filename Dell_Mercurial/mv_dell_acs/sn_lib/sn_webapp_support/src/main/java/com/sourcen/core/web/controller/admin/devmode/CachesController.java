/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.controller.admin.devmode;

import com.sourcen.core.cache.CacheProvider;
import com.sourcen.core.cache.CacheService;
import com.sourcen.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2934 $, $Date:: 2012-06-06 06:24:55#$
 */
@Controller
public class CachesController extends BaseController {

    @RequestMapping(value = "/admin/devmode/caches.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView caches_list(@RequestParam(required = false) String[] selectedCaches,
                                    @RequestParam(required = false) String action) {
        Assert.notNull(cacheService);

        if (selectedCaches != null && selectedCaches.length > 0) {
            if (action != null && action.equalsIgnoreCase("clear selected caches")) {
                // clear the caches.
                for (String cacheId : selectedCaches) {
                    cacheService.getCache(cacheId).clear();
                }
            }
        }

        ModelAndView mv = new ModelAndView();

        Map<String, Map<String, Object>> cacheStats = new HashMap<String, Map<String, Object>>();

        CacheProvider ormCacheManager = cacheService.getOrmCacheManager();
        String[] ormCacheIds = ormCacheManager.getCacheIds();
        for (String cacheId : ormCacheIds) {
            Map<String, Object> stats = ormCacheManager.getCache(cacheId).getCacheStatistics();
            stats.put("cacheName", getCacheSimpleName(cacheId));
            cacheStats.put(cacheId, stats);
        }

        CacheProvider systemCacheManager = cacheService.getSystemCacheManager();
        String[] systemCacheIds = systemCacheManager.getCacheIds();
        for (String cacheId : systemCacheIds) {
            Map<String, Object> stats = systemCacheManager.getCache(cacheId).getCacheStatistics();
            stats.put("cacheName", getCacheSimpleName(cacheId));
            cacheStats.put(cacheId, stats);
        }
        mv.addObject("caches", cacheStats);
        return mv;
    }

    private Object getCacheSimpleName(final String cacheId) {
        if (cacheId.startsWith("caches.orm")) {
            int idx = cacheId.indexOf(".domain.impl.hibernate.");
            if (idx > -1) {
                idx += 23;
            } else {
                idx = cacheId.indexOf(".domain.");
                if (idx > -1) {
                    idx += 8;
                }
            }
            if (idx > -1) {
                return "orm.entities." + cacheId.substring(idx);
            } else if (cacheId.startsWith("caches.orm.org.hibernate")) {
                return cacheId.substring(15);
            }
        }
        return cacheId;
    }


    @Autowired
    private CacheService cacheService;

    public void setCacheService(final CacheService cacheService) {
        this.cacheService = cacheService;
    }
}