/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.rest;

import com.dell.acs.web.ws.v1.ConfigurationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2753 $, $Date:: 2012-05-29 20:40:24#$
 */
public class ConfigurationServiceImpl extends WebServiceImpl implements ConfigurationService {

    @Override
    @RequestMapping("getConfiguration")
    public Object getConfiguration(@RequestParam(required = true) final String key) {
        return configurationService.getProperty("rest.configurations." + key);
    }

    @Override
    @RequestMapping("getConfigurations")
    public Map<String, Object> getConfigurations(@RequestParam(required = true) String keys) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        keys = keys.trim();
        if (!keys.isEmpty()) {
            String[] allKeys = keys.split(",");
            for (String key : allKeys) {
                key = key.trim();
                result.put(key, getConfiguration(key));
            }
        }
        return result;
    }

}
