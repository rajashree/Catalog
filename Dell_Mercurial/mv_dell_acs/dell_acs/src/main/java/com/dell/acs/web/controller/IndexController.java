/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Navin Raj
 * @version 1/19/12 4:30 PM
 */
@Controller
public class IndexController extends BaseDellController {

    /*Logger Class*/
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);


    @RequestMapping(value = "/index.do", method = RequestMethod.GET)
    public void index() {
        logger.info("current user is :=" + getUser().getUsername());
    }

}

