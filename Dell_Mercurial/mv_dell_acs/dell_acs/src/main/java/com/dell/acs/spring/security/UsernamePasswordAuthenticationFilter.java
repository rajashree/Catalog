/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.spring.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 864 $, $Date:: 2012-03-28 12:59:48#$
 */
public class UsernamePasswordAuthenticationFilter extends org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter {

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username = super.obtainUsername(request);
        if (username == null && request.getAttribute("username") != null) {
            username = request.getAttribute("username").toString();
        }
        return username;
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String password = super.obtainPassword(request);
        if (password == null && request.getAttribute("password") != null) {
            password = request.getAttribute("password").toString();
        }
        return password;
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("forceAuthentication") != null) {
            return true;
        }
        return super.requiresAuthentication(request, response);
    }
}
