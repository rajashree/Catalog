/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers.impl;

import com.bmsils.gcn.managers.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 3/21/12
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ConfigurationManagerImpl extends PropertyPlaceholderConfigurer implements ConfigurationManager {

    private static final ConfigurationManagerImpl instance = new ConfigurationManagerImpl();    
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManagerImpl.class);

    private Properties __mergedProperties = null;
    private static final Object lock = new Object();


    public static ConfigurationManagerImpl getInstance() {
        return instance;
    }
    
    @Override
    public Long getLongProperty(String name) {
        return getLongProperty(name,0L);
    }

    @Override
    public Long getLongProperty(String name, Long defaultValue) {
        try{
            if(hasProperty(name)){
                return Long.parseLong(getProperty(name));
            }
        }catch(Exception e){
            logger.info(e.getMessage(),e);
        }                                                
        return defaultValue;
    }

    @Override
    public Boolean getBooleanProperty(String name) {
        return getBooleanProperty(name,false);
    }

    @Override
    public Boolean getBooleanProperty(final Class clazz, final String name, final Boolean defaultValue) {
        return getBooleanProperty(clazz.getCanonicalName() + "." + name, defaultValue);
    }

    @Override
    public Boolean getBooleanProperty(String name, Boolean defaultValue) {
        try{
            if(hasProperty(name)){
                return getProperty(name).equalsIgnoreCase("true");
            }
        }catch(Exception e){
            logger.info(e.getMessage(),e);            
        }
        return defaultValue;
    }

    @Override
    public Integer getIntegerProperty(String name) {
        return getIntegerProperty(name,0);
    }

    @Override
    public Integer getIntegerProperty(String name, Integer defaultValue) {
        try {
            if (hasProperty(name)) {
                return Integer.parseInt(getProperty(name));
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        }
        return defaultValue;
    }

    @Override
    public Float getFloatProperty(String name) {
        return getFloatProperty(name,0F);
    }

    @Override
    public Float getFloatProperty(String name, Float defaultValue) {
        try{
            if(hasProperty(name)){
                return Float.parseFloat(name);
            }
        }catch(Exception e){
            logger.info(e.getMessage(),e);
        }
        return defaultValue;
    }

    @Override
    public String getProperty(String name) {
        return getProperty(name,"");
    }

    @Override
    public String getProperty(String name, String defaultValue) {
        try{
            if(hasProperty(name)){
               return getRawProperty(name).toString();
            }
        }catch(Exception e){
            logger.info(e.getMessage(),e);
        }
        return defaultValue;
    }

    @Override
    public boolean hasProperty(String name) {
        return getProperties().containsKey(name);
    }

    @Override
    public void refresh() {
        __mergedProperties = null;
        getProperties();
    }

    @Override
    public boolean isDevMode() {
        return getBooleanProperty("app.devMode", false);
    }


    private Object getRawProperty(String name) {
        return getProperties().get(name);
    }
    private Properties getProperties() {
        synchronized (lock) {
            if (__mergedProperties == null) {
                try {
                    __mergedProperties = instance.mergeProperties();

                    // just a fail-over if we didn't use spring.
                    if (__mergedProperties.isEmpty()) {
                        __mergedProperties.load(ConfigurationManagerImpl.class.getResourceAsStream("/application.properties"));
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    __mergedProperties = new Properties();
                }
            }
        }
        return __mergedProperties;
    }

}
