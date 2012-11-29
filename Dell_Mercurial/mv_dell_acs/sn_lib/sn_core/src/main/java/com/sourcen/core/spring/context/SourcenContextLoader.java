/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.spring.context;

import com.sourcen.core.App;
import com.sourcen.core.services.DefaultImplementationSingletonProvider;
import com.sourcen.core.services.Service;
import com.sourcen.core.upgrade.UpgradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2766 $, $Date:: 2012-05-30 02:19:10#$
 */
public class SourcenContextLoader extends ContextLoader {

    private static final Logger log = LoggerFactory.getLogger(SourcenContextLoader.class);
    private App.ContextConfigurer contextConfigurer;

    public SourcenContextLoader(App.ContextConfigurer contextConfigurer) {
        this.contextConfigurer = contextConfigurer;
    }

    @Override
    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc) {
        super.configureAndRefreshWebApplicationContext(wac, sc);
    }

    @Override
    protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {

        ApplicationState appState = App.getState();
        String activeProfiles = "";
        if (appState.equals(ApplicationState.SETUP)) {
            activeProfiles = "default,setup";
        } else if (appState.equals(ApplicationState.UPGRADE)) {
            activeProfiles = "default,upgrade,persistence";
        } else {
            activeProfiles = "default,full,persistence";
        }
        log.info("Setting activeProfiles :=" + activeProfiles);
        applicationContext.getEnvironment().setActiveProfiles(activeProfiles.split(","));
        contextConfigurer.setApplicationContext(applicationContext);
        super.customizeContext(servletContext, applicationContext);
    }


    @Override
    protected Class<?> determineContextClass(ServletContext servletContext) {
        Class<?> clazz = super.determineContextClass(servletContext);
        if (clazz.isAssignableFrom(SourcenContext.class)) {
            return clazz;
        } else {
            log.warn(clazz + " is not of type:=" + SourcenContext.class + ", defaulting to " + SourcenContextImpl.class);
        }
        return SourcenContextImpl.class;
    }

    public SourcenContext createApplicationContext() {
        SourcenContext context = DefaultImplementationSingletonProvider.getDefaultService(SourcenContext.class, SourcenContextImpl.class);
        context.initialize();
        if (context instanceof ConfigurableWebApplicationContext) {
            ConfigurableWebApplicationContext configurableContext = (ConfigurableWebApplicationContext) context;
            customizeContext(null, configurableContext);
            configurableContext.refresh();
        }
        return context;
    }


    public void closeApplicationContext(ServletContext sc, SourcenContext context) {
        if (sc != null) {
            closeWebApplicationContext(sc);
        } else if (context != null) {
            closeApplicationContext(context);
        }
    }

    public void closeApplicationContext(SourcenContext context) {
        try {
            context.destroy();
            if (context instanceof ConfigurableWebApplicationContext) {
                ((ConfigurableWebApplicationContext) context).close();
            }
        } catch (Throwable t) {
            log.error(t.getMessage());
            // cannot do anything much here.
        }
    }


    @Override
    public void closeWebApplicationContext(ServletContext sc) {
        super.closeWebApplicationContext(sc);

        // clean any variables.
        Enumeration attrNames = sc.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attrName = (String) attrNames.nextElement();
            if (attrName.startsWith("org.springframework.") || attrName.startsWith("com.sourcen.")) {
                Object attrValue = sc.getAttribute(attrName);
                try {
                    Service.Lifecycle.destroy(attrValue);
                    if (attrValue instanceof DisposableBean) {
                        ((DisposableBean) attrValue).destroy();
                    }
                } catch (Throwable ex) {
                    log.error("Couldn't invoke destroy method of attribute with name '" + attrName + "'", ex);
                }
            }
        }
    }

}
