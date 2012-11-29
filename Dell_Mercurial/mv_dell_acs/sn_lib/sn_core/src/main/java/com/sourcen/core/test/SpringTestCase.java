/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2803 $, $Date:: 2012-06-01 08:33:25#$
 */
public abstract class SpringTestCase extends SimpleTestCase {

    protected static ClassPathXmlApplicationContext applicationContext;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(
                "/spring/applicationContext.xml",
                "/spring/applicationContext-test.xml"
        );
    }

    @AfterClass
    public static void destroy() throws Exception {
        if (applicationContext != null) {
            if (applicationContext.isActive()) {
                applicationContext.destroy();
            }
        }
    }

}
