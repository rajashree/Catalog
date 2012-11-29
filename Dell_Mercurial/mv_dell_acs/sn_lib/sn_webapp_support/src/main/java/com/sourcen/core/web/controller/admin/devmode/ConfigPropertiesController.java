/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.controller.admin.devmode;

import com.sourcen.core.config.providers.DbPropertiesProvider;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
import com.sourcen.core.util.collections.PropertiesProvider;
import com.sourcen.core.web.controller.BaseController;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3091 $, $Date:: 2012-06-11 13:19:19#$
 */
@Controller
public class ConfigPropertiesController extends BaseController {

    @RequestMapping(value = "/admin/devmode/config-properties.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView config_properties(HttpServletRequest request) {
        Assert.notNull(configurationService);
        ModelAndView mv = new ModelAndView();
        Collection<PropertiesProvider> providers = configurationService.getProviders();

        Map<Object[], Map<String, String>> configs = new LinkedHashMap<Object[], Map<String, String>>(providers.size());
        int index = -1;
        for (PropertiesProvider provider : providers) {
            // get keys
            index++;
            Object[] key = new Object[4];
            String name = provider.getClass().getSimpleName();
            if (Proxy.isProxyClass(provider.getClass())) {
                if (AopUtils.getTargetClass(provider) != null &&
                        AopUtils.getTargetClass(provider).equals(DbPropertiesProvider.class)) {
                    name = "DatabaseProperties";

                }
            } else {
                if (name.endsWith("Provider")) {
                    name = name.substring(0, name.lastIndexOf("Provider"));
                } else {
                    name = name.substring(0, 10);
                }
            }

            key[0] = StringUtils.splitCamelCase(name);
            key[1] = provider.supportsPersistence();
            key[2] = index;
            key[3] = StringUtils.getSimpleString(name);
            Map<String, String> props = new HashMap<String, String>();
            for (String propKey : provider.keySet()) {
                if (propKey.startsWith("__")) {
                    continue;
                }
                if (StringUtils.containsIgnoreCase(propKey, "password")) {
                    if (WebUtils.getBooleanParameter(request, "showPasswords")) {
                        props.put(propKey, provider.getProperty(propKey));
                    } else {
                        props.put(propKey, "*******");
                    }
                } else {
                    props.put(propKey, provider.getProperty(propKey));
                }
            }
            configs.put(key, props);
        }
        mv.addObject("configurations", configs);
        return mv;
    }

}