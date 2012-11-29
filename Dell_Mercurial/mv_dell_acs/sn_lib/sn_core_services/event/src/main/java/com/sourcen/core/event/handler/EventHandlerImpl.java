/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.handler;

import com.sourcen.core.event.Event;
import com.sourcen.core.event.TaskQueue;
import com.sourcen.core.event.executor.EventExecutor;
import com.sourcen.core.event.registrar.EventRegistrarForBeans;
import com.sourcen.core.event.registrar.EventRegistrarForClasses;
import com.sourcen.core.event.registrar.EventRegistrarForObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
abstract class EventHandlerImpl extends FutureTask<Collection<Future<Object>>> implements EventHandler {

    protected static final EventExecutorTaskQueue eventExecutorTaskQueue = new EventExecutorTaskQueue();

    protected EventHandlerImpl(Callable<Collection<Future<Object>>> eventHandlerCallable) {
        super(eventHandlerCallable);
    }

    protected static Collection<EventExecutor> getAllExecutors(Event event) {
        Collection<EventExecutor> executors = new CopyOnWriteArrayList<EventExecutor>();

        executors.addAll(EventRegistrarForObjects.getInstance().getExecutors(event));
        executors.addAll(EventRegistrarForClasses.getInstance().getExecutors(event));
        executors.addAll(EventRegistrarForBeans.getInstance().getExecutors(event));

        return executors;
    }

    protected static Collection<RunnableFuture<Object>> getAllFutureExecutors(Event event) {
        Collection<RunnableFuture<Object>> futureTasks = new ArrayList<RunnableFuture<Object>>();
        Collection<EventExecutor> executors = EventHandlerImpl.getAllExecutors(event);
        for (EventExecutor executor : executors) {
            EventExecutorFutureTask futureExecutor = new EventExecutorFutureTask(executor, event.getPriority());
            futureTasks.add(futureExecutor);
        }
        return futureTasks;
    }

    protected static abstract class EventHandlerCallable implements Callable<Collection<Future<Object>>> {

        private static final Logger log = LoggerFactory.getLogger(EventHandlerCallable.class);

        protected Event event;

        public EventHandlerCallable(Event event) {
            this.event = event;
        }

        @Override
        public Collection<Future<Object>> call() throws Exception {
            Collection<RunnableFuture<Object>> futureTasks = EventHandlerImpl.getAllFutureExecutors(event);
            Collection<Future<Object>> result = new ArrayList<Future<Object>>(futureTasks.size());
            if (log.isDebugEnabled()) {
                log.debug(event.getType() + " has " + futureTasks.size() + " listeners.");
            }
            for (RunnableFuture<Object> task : futureTasks) {
                if (log.isDebugEnabled()) {
                    log.debug("addinToQueue :" + task);
                }
                eventExecutorTaskQueue.execute(task);
                result.add(task);
            }
            return result;
        }
    }

    protected static final class EventExecutorTaskQueue extends TaskQueue<RunnableFuture<Object>> {
        public EventExecutorTaskQueue() {
            super(new PriorityBlockingQueue<Runnable>(1000));
        }
    }

}
