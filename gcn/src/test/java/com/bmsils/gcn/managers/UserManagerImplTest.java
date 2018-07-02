/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import com.bmsils.gcn.BaseTestCase;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/3/12
 * Time: 2:12 PM
 * UserManagerImplTest - contains Test Cases for UserManager methods
 */

//TODO: Test cases implementation
public class UserManagerImplTest extends BaseTestCase {
    @Test
    public void getUser() {
        UserManager userManager = applicationContext.getBean("userManagerImpl", UserManager.class);
        assertTrue(userManager.getUser("11111111") != null);
    }
}