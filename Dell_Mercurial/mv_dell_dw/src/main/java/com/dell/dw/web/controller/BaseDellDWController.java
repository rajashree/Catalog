package com.dell.dw.web.controller;

import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 6/13/12
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseDellDWController extends BaseController {
    @Autowired
    protected ConfigurationService configurationService;

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
