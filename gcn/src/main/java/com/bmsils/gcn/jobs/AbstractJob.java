/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.jobs;

import com.bmsils.gcn.managers.ConfigurationManager;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/23/12
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractJob extends QuartzJobBean implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Executes the Job when the application property(application.properties) is set to "true"
     */
    protected final void executeInternal(final JobExecutionContext context) {
        if (configurationManager.getBooleanProperty(getClass(), "enabled", true)) {
            afterPropertiesSet();
            try {
                executeJob(context);
            } catch (Exception e) {

                logger.error(e.getMessage(), e);
            }
        }
    }

    
    public void afterPropertiesSet() {
    }

    protected abstract void executeJob(final JobExecutionContext context);


    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    protected ConfigurationManager configurationManager;

    public final void setConfigurationManager(final ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

}
