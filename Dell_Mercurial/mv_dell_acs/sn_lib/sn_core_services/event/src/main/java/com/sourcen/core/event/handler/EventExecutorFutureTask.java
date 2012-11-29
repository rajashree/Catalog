/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.handler;

import com.sourcen.core.event.Event;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class EventExecutorFutureTask extends FutureTask<Object> implements Comparable<EventExecutorFutureTask> {


    public EventExecutorFutureTask(Callable<Object> objectCallable, Integer priority) {
        super(objectCallable);
        this.priority = priority;
    }

    private Integer priority = Event.Priority.MEDIUM;

    @Override
    public int compareTo(EventExecutorFutureTask task) {
        return task.priority - this.priority;
    }

}
