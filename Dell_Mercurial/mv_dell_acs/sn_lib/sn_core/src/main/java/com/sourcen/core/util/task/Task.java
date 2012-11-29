/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * An abstract Task that can be used for all our scheduling and concurrent execution of Tasks. This is a simple class
 * that implements Runnable, and also supports QuartzJob scheduling.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.01
 */
public abstract class Task implements Runnable, BeanFactoryAware {

    /**
     * isPropertiesSet is true, once the properties for this Task has been set.
     */
    private boolean isPropertiesSet = false;

    /**
     * The beanfactory that the task was created within.
     */
    protected BeanFactory beanFactory;

    /**
     * Creates a new Task instance.
     */
    protected Task() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * {@inheritDoc}
     */
    protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
        this.isPropertiesSet = true;
        run();
    }

    /**
     * Checks if the properties for this Task has been set or not. If properties has not yet been set, then we call
     * setJobContextProperties.
     */
    public void verifyPropertySet() {
        if (!this.isPropertiesSet) {
            setJobContextProperties();
            this.isPropertiesSet = true;
        }
    }

    /**
     * this method is implemented by the Task which allows us to set any properties for this bean before we execute the
     * task.
     */
    public void setJobContextProperties() {
        // this method is overridden if required for setting
        // additional bean properties.
    }
}
