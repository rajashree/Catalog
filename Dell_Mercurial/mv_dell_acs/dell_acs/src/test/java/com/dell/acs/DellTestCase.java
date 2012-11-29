/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs;

import com.dell.acs.managers.CurationCacheManager;
import com.dell.acs.managers.CurationManager;
import com.dell.acs.managers.CurationSourceManager;
import com.dell.acs.managers.DocumentManager;
import com.dell.acs.managers.EventManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.managers.UserManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * updated the class to use test-contexts.
 *
 * @author Navin Raj Kumar G.S.
 */
public class DellTestCase {

    protected static ClassPathXmlApplicationContext applicationContext;

    protected Logger logger = LoggerFactory.getLogger(DellTestCase.class);

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext();
        applicationContext.setConfigLocations(new String[]{"/spring/test/applicationContext-test.xml"});
        applicationContext.getEnvironment().setActiveProfiles("default,persistence,test");
        applicationContext.refresh();
        setUpManagers();

    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (applicationContext != null) {
            applicationContext.destroy();
        }
    }

    // setup some managers for reference use later.
    protected static UserManager userManager;

    protected static CurationManager curationManager;

    protected static CurationSourceManager curationSourceManager;

    protected static CurationCacheManager curationCacheManager;

    protected static RetailerManager retailerManager;

    protected static EventManager eventManager;

    protected static DocumentManager documentManager;

    public static void setUpManagers() throws Exception {

        try {
            userManager = applicationContext.getBean(UserManager.class);
            curationManager = applicationContext.getBean(CurationManager.class);
            curationSourceManager = applicationContext.getBean(CurationSourceManager.class);
            curationCacheManager = applicationContext.getBean(CurationCacheManager.class);
            retailerManager=applicationContext.getBean("retailerManagerImpl",RetailerManager.class);
            eventManager=applicationContext.getBean("eventManagerImpl",EventManager.class);
            documentManager=applicationContext.getBean("documentManagerImpl",DocumentManager.class);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new Error(e.getMessage(), e);
        }
    }

}
