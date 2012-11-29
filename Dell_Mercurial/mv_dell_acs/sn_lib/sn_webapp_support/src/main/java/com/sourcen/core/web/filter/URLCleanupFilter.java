/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.web.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * reference from http://ravichintakunta.blogspot.com/2009/04/how-to-remove-jsessionid-appended-to.html
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class URLCleanupFilter extends AbstractFilter {

    private static final Pattern jsessionIdMatch = Pattern.compile(";?jsessionid=[0-9a-z.]+;?", Pattern.CASE_INSENSITIVE);

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, new UrlCleanUpResponseWrapper(response));
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {

        final HttpSession session = request.getSession(false);
        if (!this.isEnabled || session == null || session.isNew()) {
            return false;
        }
        return super.shouldNotFilter(request);
    }

    private final class UrlCleanUpResponseWrapper extends HttpServletResponseWrapper {

        private UrlCleanUpResponseWrapper(final HttpServletResponse response) {
            super(response);
        }

        @Override
        public String encodeURL(final String url) {
            return cleanJsessionId(super.encodeURL(url));
        }

        @Override
        public String encodeRedirectURL(final String url) {
            return cleanJsessionId(super.encodeRedirectURL(url));
        }

        @Override
        public String encodeUrl(final String url) {
            return cleanJsessionId(super.encodeUrl(url));
        }

        @Override
        public String encodeRedirectUrl(final String url) {
            return cleanJsessionId(super.encodeRedirectUrl(url));
        }

        private String cleanJsessionId(final String s) {
            return URLCleanupFilter.jsessionIdMatch.matcher(s).replaceAll("");
        }
    }

}
