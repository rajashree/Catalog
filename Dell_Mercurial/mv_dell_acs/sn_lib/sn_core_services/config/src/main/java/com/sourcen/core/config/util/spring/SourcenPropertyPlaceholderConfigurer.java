/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.config.util.spring;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.config.ConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * This class uses the configurationService to override any values that Spring requires. any placeholders ${abc.test}
 * will be first queried in the configurationService, and then if not found will resolve to super. Hence this will give
 * us a chance to change any properties at runtime using the configurationService.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3582 $, $Date:: 2012-06-25 08:55:10#$
 * @since 1.0
 */
public class SourcenPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    //
    // =================================================================================================================
    // == Class Fields ==
    // =================================================================================================================
    //

    private static final Logger log = LoggerFactory.getLogger(SourcenPropertyPlaceholderConfigurer.class);

    private ConfigurationService _configurationService;

    public ConfigurationService getConfigurationService() {
        if (_configurationService == null) {
            if (App.isInitialized()) {
                _configurationService = App.getService(ConfigurationService.class);
            } else {
                return ConfigurationServiceImpl.getInstance();
            }
        }
        return _configurationService;
    }

    public void setConfigurationService(ConfigurationService _configurationService) {
        this._configurationService = _configurationService;
    }

    //
    // =================================================================================================================
    // == Constructors ==
    // =================================================================================================================
    //

    /**
     * creates a new SourcenPropertyPlaceholderConfigurer instance.
     */
    public SourcenPropertyPlaceholderConfigurer() {
        super();
    }

    //
    // =================================================================================================================
    // == Class Methods ==
    // =================================================================================================================
    //

    @Override
    protected final String resolvePlaceholder(String placeholder, final Properties props,
                                              final int systemPropertiesMode) {
        String value = null;
        int pipeIndex = placeholder.indexOf("|");
        String defaultValue = null;
        // abc| 4 3
        if (pipeIndex > -1) {
            // we have defaultValue in it.
            if (pipeIndex + 1 == placeholder.length()) {
                // means the default value is empty {abc|}
                defaultValue = "";
            } else {
                defaultValue = placeholder.substring(pipeIndex + 1);
            }
            if (defaultValue.equals("null")) {
                defaultValue = null;
            }
            placeholder = placeholder.substring(0, pipeIndex);
        }
        final String configValue = getConfigurationService().getProperty(placeholder);
        if (configValue != null) {
            value = configValue;
        } else {
            value = super.resolvePlaceholder(placeholder, props, systemPropertiesMode);
        }
        log.info("Resolved placeholder='{}' to '{}' and defaultValue='{}'", new Object[]{placeholder, value,
                defaultValue});
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @Override
    protected String resolvePlaceholder(final String placeholder, final Properties props) {
        return super.resolvePlaceholder(placeholder, props);
    }

}
