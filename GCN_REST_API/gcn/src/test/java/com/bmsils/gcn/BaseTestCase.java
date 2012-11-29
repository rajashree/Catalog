/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/3/12
 * Time: 12:23 PM
 * BaseTestCase to be extended by all the TestCase
 */
public class BaseTestCase {
    protected static ApplicationContext applicationContext;
    protected static Logger logger = LoggerFactory.getLogger(BaseTestCase.class);

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
    }

}
