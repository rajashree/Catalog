/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.spring.security;

import org.springframework.security.core.Authentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
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
}
