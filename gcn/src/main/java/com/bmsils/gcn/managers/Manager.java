/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/3/12
 * Time: 2:09 PM
 * Base Intergace for all Service class
 */
public interface Manager {
    static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
}
