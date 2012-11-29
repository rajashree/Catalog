/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.controller;

import com.sourcen.core.config.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2754 $, $Date:: 2012-05-29 20:40:43#$
 */

public abstract class BaseController implements ApplicationContextAware {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected ApplicationContext applicationContext;

    @Autowired
    protected ConfigurationService configurationService;

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    protected boolean isAnonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) ) {
            return false;
        }
        return true;
    }

    protected <T> ResponseEntity<T> getJSONResult(String data) {
        return getJSONResult(data, HttpStatus.OK);
    }

    protected <T> ResponseEntity<T> getJSONResult(String data, HttpStatus status) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<T>((T) data, responseHeaders, HttpStatus.OK);
    }

}
