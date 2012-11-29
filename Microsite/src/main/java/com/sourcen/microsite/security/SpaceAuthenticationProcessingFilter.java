package com.sourcen.microsite.security;

import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class SpaceAuthenticationProcessingFilter extends AuthenticationProcessingFilter {

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        	return request.getParameter("password");
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }

    
}

