/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2730 $, $Date:: 2012-05-29 12:07:23#$
 */
public abstract class TaskQueue<T extends Runnable> {

    protected ThreadPoolExecutor queue;
    private final ThreadFactory threadFactory;
    private final BlockingQueue<Runnable> taskQueue;

    public TaskQueue() {
        this(new ArrayBlockingQueue<Runnable>(100));
    }

    public TaskQueue(BlockingQueue<Runnable> taskQueue) {
        this.taskQueue = taskQueue;
        threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable target) {
                final Thread thread = new Thread(target);
                thread.setDaemon(true);
                return thread;
            }
        };

        queue = new ThreadPoolExecutor(5, 20, 60L, TimeUnit.SECONDS, this.taskQueue, threadFactory);
    }

    public void execute(T object) {
        queue.execute(object);
    }

}
