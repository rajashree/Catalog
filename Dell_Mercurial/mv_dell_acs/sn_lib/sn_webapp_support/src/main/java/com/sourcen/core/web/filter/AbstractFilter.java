/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.web.filter;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public abstract class AbstractFilter extends OncePerRequestFilter {

    protected Boolean isEnabled = true;

    @Autowired
    private ConfigurationService configurationService;


    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        Boolean enabled = getConfigurationService().getBooleanProperty(getClass(), "isEnabled", isEnabled);
        return !enabled;
    }

    protected ConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = App.getService(ConfigurationService.class);
        }
        return configurationService;
    }

    @Autowired
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
