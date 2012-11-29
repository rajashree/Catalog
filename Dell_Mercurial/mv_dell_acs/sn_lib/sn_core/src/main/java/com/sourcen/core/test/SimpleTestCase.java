/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.test;

import com.sourcen.core.util.Profiler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2803 $, $Date:: 2012-06-01 08:33:25#$
 */
public abstract class SimpleTestCase {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected Profiler profiler = new Profiler(getClass().getCanonicalName());

    @BeforeClass
    public static void setUp() throws Exception {

    }

    @AfterClass
    public static void destroy() throws Exception {

    }

}
