/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.spring.context;

import com.sourcen.core.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * We have configured SourcenContextLoaderListener in the web.xml as a web listener, hence when the context is loaded,
 * this will provide us access to the SpringContext that gets initialized after the web context was initialized.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.01
 */
public class SourcenContextLoaderListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(SourcenContextLoaderListener.class);

    public void contextInitialized(ServletContextEvent event) {
        log.info("ServletContext loaded, initializing App.");
        App.initialize(event.getServletContext());
    }


    public void contextDestroyed(ServletContextEvent event) {
        log.info("ServletContext destroyed, destroying App.");
        App.destroy(event.getServletContext());
    }
}
