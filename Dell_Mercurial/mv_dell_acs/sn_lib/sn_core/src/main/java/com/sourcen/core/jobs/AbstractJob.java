/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.jobs;

import com.sourcen.core.config.ConfigurationService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3520 $, $Date:: 2012-06-22 10:55:07#$
 */
public abstract class AbstractJob extends QuartzJobBean implements InitializingBean, Runnable {
	public static final String JOB_CLASS_ENABLED_KEY = "enabled";
	public static final String STATUS_KEY = "status";
	public static final String STATUS_CLASS_DISABLED = "CLASS_DISABLED";

    /**
     * logger
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * ConfigurationService bean injection.
     */
    @Autowired
    protected ConfigurationService configurationService;

    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    private JobExecutionContext jobContext;

    // SFISK - need it public for testing.
    public final void executeInternal(final JobExecutionContext context) {
        if (configurationService.getBooleanProperty(getClass(), JOB_CLASS_ENABLED_KEY, true)) {
            afterPropertiesSet();
            try {
                this.jobContext = context;
                run();
                context.setResult(true);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                context.setResult(e);
            }
        } else {
        	context.getJobDetail().getJobDataMap().put(AbstractJob.STATUS_KEY, AbstractJob.STATUS_CLASS_DISABLED);
        }
    }

    @Override
    public void afterPropertiesSet() {
    }

    @Override
    public void run() {
        this.executeJob(jobContext);
    }

    protected abstract void executeJob(final JobExecutionContext context);

}
