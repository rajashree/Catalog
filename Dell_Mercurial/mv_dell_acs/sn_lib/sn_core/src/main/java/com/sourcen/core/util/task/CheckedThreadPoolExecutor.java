/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.task;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.01
 */

public abstract class CheckedThreadPoolExecutor {

    /**
     * Returns the Logger for this class.
     *
     * @return Logger instance.
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * reference to the actual ThreadPoolExecutor.
     */
    protected final ThreadPoolExecutor executor;

    /**
     * Creates a new <tt>ThreadPoolExecutor</tt> with the given initial parameters and default thread factory and
     * rejected execution handler. It may be more convenient to use one of the {@link java.util.concurrent.Executors}
     * factory methods instead of this general purpose constructor.
     */
    protected CheckedThreadPoolExecutor() {
        this(5, 10, 60, TimeUnit.SECONDS);
    }

    /**
     * Creates a new <tt>ThreadPoolExecutor</tt> with the given initial parameters and default thread factory and
     * rejected execution handler. It may be more convenient to use one of the {@link java.util.concurrent.Executors}
     * factory methods instead of this general purpose constructor.
     *
     * @param corePoolSize  the number of threads to keep in the pool, even if they are idle.
     * @param maxPoolSize   the maximum number of threads to allow in the pool.
     * @param keepAliveTime when the number of threads is greater than the core, this is the maximum time that excess
     *                      idle threads will wait for new tasks before terminating.
     * @param unit          the time unit for the keepAliveTime argument.
     *
     * @throws IllegalArgumentException if corePoolSize or keepAliveTime less than zero, or if maximumPoolSize less than
     *                                  or equal to zero, or if corePoolSize greater than maxPoolSize.
     */
    protected CheckedThreadPoolExecutor(final int corePoolSize, final int maxPoolSize, final long keepAliveTime, final TimeUnit unit) throws IllegalArgumentException {
        this.executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
    }

    /**
     * This method verifies the number of unprocessed actions, and then based on the number of actions a processor can
     * handle, creates 'n' number of processors, and submits them for execution.
     */
    public void verifyAndSubmit() {
        final long count = getUnprocessedActionCount();
        if (count == 0) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("No processors to start.");
            }
            return;
        }

        final int requiredThreadCount = ((Double) Math.ceil((double) count / this.actionsPerProcessor)).intValue();

        final int activeThreadCount = this.executor.getActiveCount();
        int maxAvailableThreadCount = this.executor.getMaximumPoolSize() - activeThreadCount;

        if (requiredThreadCount > maxAvailableThreadCount) {
            if (maxAvailableThreadCount > 0) {
                if (this.logger.isInfoEnabled()) {
                    this.logger.info("Currently there are " + activeThreadCount + " number of Processors active, and " + "the maxPoolSize:=" + this.executor.getMaximumPoolSize()
                            + ".Hence we are scheduing only " + maxAvailableThreadCount + " processors but required " + requiredThreadCount);
                }
            } else {
                this.logger.warn("We have exceeded the maximum schedulable " + "count of processors, please increase " + "the threshold of the Executor");
                return;
            }
        } else {
            maxAvailableThreadCount = requiredThreadCount;
        }
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Starting " + maxAvailableThreadCount + " processors.");
        }
        for (int i = maxAvailableThreadCount; i > 0; i--) {
            try {
                this.executor.execute(getRunnable());
            } catch (final Exception e) {
                this.logger.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * Returns the unprocessedActionCount of this CheckedThreadPoolExecutor object.
     *
     * @return the unprocessedActionCount (type long) of this CheckedThreadPoolExecutor object.
     */
    protected abstract long getUnprocessedActionCount();

    /**
     * Returns the runnable of this CheckedThreadPoolExecutor object.
     *
     * @return the runnable (type Runnable) of this CheckedThreadPoolExecutor object.
     */
    protected abstract Runnable getRunnable();

    /**
     * Returns the runnable of this CheckedThreadPoolExecutor object, but provides an optional parameter of 'index'
     * which is the index of current task that was created to be submitted to the thread pool.
     *
     * @param index of type int
     *
     * @return Runnable
     */
    protected Runnable getRunnable(final int index) {
        return getRunnable();

    }

    /**
     * The number of actions a processor can handle.
     */
    private int actionsPerProcessor = 10;

    /**
     * Returns the actionsPerProcessor of this CheckedThreadPoolExecutor object.
     *
     * @return actionsPerProcessor of this CheckedThreadPoolExecutor object.
     */
    public int getActionsPerProcessor() {
        return this.actionsPerProcessor;
    }

    /**
     * Sets the actionsPerProcessor of this CheckedThreadPoolExecutor object.
     *
     * @param actionsPerProcessor to be set for this CheckedThreadPoolExecutor object.
     */
    public void setActionsPerProcessor(final int actionsPerProcessor) {
        this.actionsPerProcessor = actionsPerProcessor;
    }
}
