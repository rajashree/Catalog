/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import sun.management.FileSystem;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 3/21/12
 * Time: 3:37 PM
 * Configuration Service exposes methods to access application properties
 */
public interface ConfigurationManager extends Manager{//
    //
    // Long
    //
    Long getLongProperty(String name);

    Long getLongProperty(String name, Long defaultValue);


    //
    // Boolean
    //

    Boolean getBooleanProperty(String name);

    Boolean getBooleanProperty(Class clazz, String name, Boolean defaultValue);

    Boolean getBooleanProperty(String name, Boolean defaultValue);


    //
    // Integer
    //

    Integer getIntegerProperty(String name);

    Integer getIntegerProperty(String name, Integer defaultValue);

    //
    //Float
    //

    Float getFloatProperty(String name);

    Float getFloatProperty(String name, Float defaultValue);

    //
    // String
    //

    String getProperty(String name);
    String getProperty(String name, String defaultValue);


    // etc
    boolean hasProperty(String name);

    void refresh();

    boolean isDevMode();

}
