/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import com.bmsils.gcn.BaseTestCase;

import com.bmsils.gcn.managers.impl.ConfigurationManagerImpl;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 3/21/12
 * Time: 4:25 PM
 * ConfigurationManagerImplTest - contains Test Cases for ConfigurationManager methods
 */
//TODO: Test cases implementation
public class ConfigurationManagerImplTest extends BaseTestCase {
    @Test
    public void getApplicationProperties() {
        ConfigurationManager configurationManager = applicationContext.getBean("configurationManager", ConfigurationManagerImpl.class);
        assertTrue(configurationManager.isDevMode());
    }
}
