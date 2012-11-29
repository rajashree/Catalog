/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.servlet.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class ClasspathResourceServlet extends ResourceServlet {

    private static final Logger log = LoggerFactory.getLogger(ClasspathResourceServlet.class);

    protected String prefix;

    public ClasspathResourceServlet() {
        this("/");
    }

    public ClasspathResourceServlet(String prefix) {
        super();
        this.prefix = prefix;
    }

    @Override
    protected File getFile(HttpServletRequest request, String requestPath) {
        try {
            return new File(ClasspathResourceServlet.class.getClassLoader().getResource(prefix + requestPath).toURI());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
