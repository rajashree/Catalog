/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.config.util;


import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.config.ConfigurationServiceEvent;
import com.sourcen.core.event.spring.EventListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Inject this into the framework/EventListener if you want to configure Log4J at runtime.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.0
 */
@EventListener
public class Log4JPropertyListener implements
        ConfigurationServiceEvent.PropertiesLoaded,
        ConfigurationServiceEvent.PropertyUpdated {

    private static final Log4JPropertyListener instance = new Log4JPropertyListener();

    public static Log4JPropertyListener getInstance() {
        return instance;
    }

    private Log4JPropertyListener() {
    }

    private static final Logger log = Logger.getLogger(Log4JPropertyListener.class);

    public void onPropertiesLoaded(final ConfigurationServiceEvent evt) {
        reloadLog4jProperties();
    }

    public void onPropertyUpdated(final ConfigurationServiceEvent evt) {
        if (evt.getPropertyName().startsWith("log4j")) {
            reloadLog4jProperties();
        }
    }

    private void reloadLog4jProperties() {
        // if log4j property was changed.
        final String log4jLevel = getConfigurationService().getProperty("log4j.rootLogger.level");
        if (log4jLevel != null) {
            Log4JPropertyListener.log.info(Logger.getRootLogger().getEffectiveLevel().toString() + " to " + log4jLevel);
            Logger.getRootLogger().setLevel(Level.toLevel(log4jLevel, Level.WARN));
        }

        final Map<String, Object> props = getConfigurationService().getProperties("log4j.logger", false, false);
        for (final Map.Entry<String, Object> prop : props.entrySet()) {
            try {
                final String key = prop.getKey().substring(13);
                Log4JPropertyListener.log.info(prop.getKey() + " changed to " + prop.getValue().toString());
                Logger.getLogger(key).setLevel(Level.toLevel(prop.getValue().toString(), Level.WARN));
            } catch (final Exception e) {
                Log4JPropertyListener.log.fatal("An unknown exception occured while trying to set the logging properties :=" + prop, e);
            }
        }
    }

    private ConfigurationService configurationService;

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }
}
