/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.task;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A task utility that allows us to run Runnable implementations as a Quartz Job.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.01
 */
public final class RunnableTask extends Task {

    /**
     * an instance of the runnable object that needs to be executed.
     */
    private volatile Runnable runnable;

    /**
     * Returns the runnable of this RunnableTask object.
     *
     * @return the runnable (type Runnable) of this RunnableTask object.
     */
    public Runnable getRunnable() {
        return this.runnable;
    }

    /**
     * Sets the runnable of this RunnableTask object.
     *
     * @param runnable the runnable of this RunnableTask object.
     */
    public void setRunnable(final Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
     * causes the object's <code>run</code> method to be called in that separately executing thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        final Logger logger = LoggerFactory.getLogger(RunnableTask.class);
        logger.warn(this.runnable.toString());
        this.runnable.run();
    }
}
