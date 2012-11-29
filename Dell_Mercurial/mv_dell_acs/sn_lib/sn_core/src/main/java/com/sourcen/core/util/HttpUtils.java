/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.0
 */
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static int getIE(final HttpServletRequest request) {
        final String userAgent = request.getHeader("User-Agent").toLowerCase();
        if (userAgent.indexOf("msie") != -1 && userAgent.indexOf("windows") != -1) {
            final int indexStart = userAgent.indexOf("msie ");
            final int indexEnd = userAgent.indexOf(";", indexStart);
            try {
                String revision = userAgent.substring(indexStart + 5, indexEnd);
                if (Character.isLetter(revision.charAt(revision.length() - 1))) {
                    revision = revision.substring(0, revision.length() - 1);
                }
                return Integer.parseInt(revision);
            } catch (final Exception e) {
                log.info("Exception parsing UserAgent for IE : " + userAgent);
            }
        }
        return -1;
    }

    /**
     * Gets the uri from the request
     *
     * @param request The request
     *
     * @return The uri
     */
    public static String getUri(final HttpServletRequest request) {
        // handle http dispatcher includes.
        String uri = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (uri != null) {
            return uri;
        }

        String servletPath = request.getServletPath();

        final String requestUri = request.getRequestURI();
        // Detecting other characters that the servlet container cut off (like anything after ';')
        if (requestUri != null && servletPath != null && !requestUri.endsWith(servletPath)) {
            final int pos = requestUri.indexOf(servletPath);
            if (pos > -1) {
                servletPath = requestUri.substring(requestUri.indexOf(servletPath));
            }
        }

        if (null != servletPath && !"".equals(servletPath)) {
            return servletPath;
        }

        final int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
        int endIndex = request.getPathInfo() == null ? requestUri.length() : requestUri.lastIndexOf(request.getPathInfo());

        if (startIndex > endIndex) { // this should not happen
            endIndex = startIndex;
        }

        uri = requestUri.substring(startIndex, endIndex);

        if (uri != null && !"".equals(uri)) {
            return uri;
        }

        uri = request.getRequestURI();
        return uri.substring(request.getContextPath().length());
    }

}
