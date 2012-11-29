/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core;

import com.sourcen.core.services.DefaultImplementationSingletonProvider;
import com.sourcen.core.services.Service;
import com.sourcen.core.spring.context.ApplicationState;
import com.sourcen.core.spring.context.SourcenContext;
import com.sourcen.core.spring.context.SourcenContextLoader;
import com.sourcen.core.upgrade.UpgradeService;
import com.sourcen.core.util.collections.FileBackedPropertiesProvider;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: vivekk $
 * @version $Revision: 3420 $, $Date:: 2012-06-20 13:10:07#$
 */
public class App {

    private static SourcenContext context;
    private static final ContextConfigurer contextConfigurer = new ContextConfigurer();
    private static final SourcenContextLoader contextLoader = new SourcenContextLoader(contextConfigurer);
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static PropertiesProvider appProperties = null;
    public static final FileBackedPropertiesProvider buildVersionProperties = FileBackedPropertiesProvider.getProvider("/version.properties");


    public static synchronized void initialize() {
        if (getState().getId() < ApplicationState.INITIALIZING.getId()) {
            initialize(null);
        }
    }

    public static synchronized void initialize(ServletContext servletContext) {
        if (context == null) {
            ApplicationState appState = ApplicationState.INITIALIZING;

            setState(appState);

            try {
                appProperties = DefaultImplementationSingletonProvider.getDefaultService(PropertiesProvider.class, "com.sourcen.core.config.providers.FileSystemStartupPropertiesProvider");
            } catch (Exception e) {
                log.error("unable to load com.sourcen.core.config.providers.FileSystemStartupPropertiesProvider, defaulting to /startup.properties");
                appProperties = FileBackedPropertiesProvider.getProvider("/startup.properties");
            }
            try {
                String state = appProperties.getProperty(ApplicationState.APPLICATION_STATE_KEY);
                if (state == null) {
                    setState(ApplicationState.SETUP);
                } else {
                    appState = ApplicationState.get(appProperties.getProperty(ApplicationState.APPLICATION_STATE_KEY));
                }

                if (appState.isErrorState()) {
                    Integer stateId = appState.getId() - ApplicationState.ERROR_INDEX;
                    log.error("ApplicationState is set to errorstate:=" + appState);
                    appState = ApplicationState.get(stateId);
                }
                setState(appState);

                // now determine the state of the application.
                UpgradeService upgradeService = DefaultImplementationSingletonProvider.getDefaultService(UpgradeService.class);
                upgradeService.setContextConfigurer(contextConfigurer);
                log.info("upgrade is required. = " + upgradeService.isUpgradeRequired());
                if (upgradeService.isUpgradeRequired()) {
                    appState = ApplicationState.UPGRADE;
                } else {
                    appState = ApplicationState.COMPLETE;
                }
                setState(appState);

                if (servletContext == null) {
                    context = contextLoader.createApplicationContext();
                } else {
                    context = (SourcenContext) contextLoader.initWebApplicationContext(servletContext);
                }
            } catch (Exception e) {
                Integer errorStateId = appState.getId() + ApplicationState.ERROR_INDEX;
                log.info("ErrorStateID:=" + errorStateId);
                setState(ApplicationState.get(errorStateId));
                log.error(e.getMessage(), e);
                App.destroy();
            }
        }
    }

    public static SourcenContext getContext() {
        if (context == null) {
            throw new SourcenRuntimeException("SourcenContext has not yet been initialized");
        }
        return context;
    }

    public static void destroy() {
        destroy(null);
    }

    public static void destroy(ServletContext sc) {
        log.info("destroying App");
        if (!getState().equals(ApplicationState.TERMINATING)) {
            setState(ApplicationState.TERMINATING);
            contextLoader.closeApplicationContext(sc, context);
            setState(ApplicationState.TERMINATED);
        }
        log.info("App terminated.");
    }


    private static ApplicationState state = ApplicationState.TERMINATED;

    protected static void setState(ApplicationState state) {
        if (!App.state.isErrorState()) {
            log.info("ApplicationState set to :" + state.getName());
            App.state = state;
        } else {
            log.info("ApplicationState is in error mode  state:=" + state.getName());
        }
        if (state.canSaveState()) {
            if (appProperties != null) {
                appProperties.setProperty(ApplicationState.APPLICATION_STATE_KEY, state.getName());
            }
        }
    }

    public static ApplicationState getState() {
        return App.state;
    }

    public static Boolean isInitialized() {
        return state.equals(ApplicationState.INITIALIZED);
    }


    public static class ContextConfigurer {
        private ContextConfigurer() {
        }

        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            App.context = (SourcenContext) applicationContext;
        }

        public void setApplicationState(final ApplicationState appState) {
            setState(appState);
        }
    }


    // pre-initializing beans
    public static <T extends Service> T getService(Class<? extends T> iface) {
        if (context != null) {
            return (T) context.getBean(iface);
        } else {
            return (T) DefaultImplementationSingletonProvider.getDefaultService(iface);
        }
    }

    public static <T> T getBean(Class<?> iface) {
        if (context != null) {
            return (T) context.getBean(iface);
        } else {
            return (T) DefaultImplementationSingletonProvider.getDefaultService(iface);
        }
    }

}
