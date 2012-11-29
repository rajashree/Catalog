package com.sourcen.meteriffic.security;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;

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

