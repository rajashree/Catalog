/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.filter;

import com.sourcen.core.App;
import com.sourcen.core.spring.context.ApplicationState;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2766 $, $Date:: 2012-05-30 02:19:10#$
 */
public class ApplicationStateFilter extends AbstractFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ApplicationState state = App.getState();

        String uri = request.getRequestURI();

        // check for public extensions. (jpg, css, js etc...)
        if (StringUtils.endsWith(uri, WebUtils.getPublicFileExtensions()) ||
                // if it starts with /error, ignore this filter
                uri.startsWith("/errors/")
                ) {
            filterChain.doFilter(request, response);
            return;
        }


        // if SETUP
        if (state == ApplicationState.SETUP) {
            if (uri.startsWith("/state/setup")) {
                filterChain.doFilter(request, response);
            } else {
                response.sendRedirect("/state/setup/index." + WebUtils.defaultActionExtension());
            }
        } else if (state == ApplicationState.UPGRADE) {
            if (uri.startsWith("/state/upgrade")) {
                filterChain.doFilter(request, response);
            } else {
                response.sendRedirect("/state/upgrade/index." + WebUtils.defaultActionExtension());
            }
        } else if (state == ApplicationState.COMPLETE) {
            // only when we are complete loading the application, continue the chain, else just redirect to the
            // corresponding applicationState.
            filterChain.doFilter(request, response);
        } else {
            // just redirect to the state page.
            response.sendRedirect("/state/" + state.toString().toLowerCase() + "." + WebUtils.defaultActionExtension());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // ignore the enabled state, and just check for applicationState.
        return false;
    }

}
