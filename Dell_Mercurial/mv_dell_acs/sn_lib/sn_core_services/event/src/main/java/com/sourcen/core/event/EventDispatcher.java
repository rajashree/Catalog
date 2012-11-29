/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event;

import com.sourcen.core.event.handler.AsynchronousEventHandler;
import com.sourcen.core.event.handler.BlockingEventHandler;
import com.sourcen.core.event.handler.EventHandler;
import com.sourcen.core.event.registrar.EventRegistrarForBeans;
import com.sourcen.core.event.registrar.EventRegistrarForClasses;
import com.sourcen.core.event.registrar.EventRegistrarForObjects;
import com.sourcen.core.services.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2730 $, $Date:: 2012-05-29 12:07:23#$
 */
public final class EventDispatcher extends TaskQueue<EventHandler> implements Service {

    private static final EventDispatcher instance = new EventDispatcher();
    private static final Logger log = LoggerFactory.getLogger(EventDispatcher.class);

    public static EventDispatcher getInstance() {
        return instance;
    }

    private EventDispatcher() {
        super();
    }

    public static void dispatch(Event event) {
        dispatchWithFuture(event);
    }

    public static Collection<Future<Object>> dispatchWithFuture(Event event) {
        if (log.isDebugEnabled()) {
            log.debug("Dispatching blocking event :=" + event);
        }
        try {
            EventHandler handler = new BlockingEventHandler(event);
            // we got to run this manually as we don't execute it within a TaskQueue.
            handler.run();
            return handler.get();
        } catch (Exception e) {
            throw new EventException(e);
        }
    }


    public static void dispatchAsynchronous(Event event) {
        dispatchAsynchronousWithFuture(event);
    }

    public static EventHandler dispatchAsynchronousWithFuture(Event event) {
        return dispatchAsynchronousWithFuture(event, null);
    }


    public static EventHandler dispatchAsynchronousWithFuture(Event event, EventCallback callback) {
        if (log.isDebugEnabled()) {
            log.debug("Dispatching asynchronous callback event :=" + event + ", callback:=" + callback);
        }
        EventHandler handler = new AsynchronousEventHandler(event, callback);
        instance.queue.execute(handler);
        return handler;
    }


    //
    // register listeners.
    //
    public static void addListener(Class clazz) {
        EventRegistrarForClasses.getInstance().add(clazz);
    }

    public static void addListener(Object listener) {
        EventRegistrarForObjects.getInstance().add(listener);
    }

    public static void addListener(String beanName) {
        EventRegistrarForBeans.getInstance().add(beanName);
    }

    public static void addListenerClass(String className) {
        try {
            Class clazz = Class.forName(className);
            EventRegistrarForClasses.getInstance().add(clazz);
        } catch (ClassNotFoundException e) {
            log.error("Unable to register classListener : - " + e.getMessage());
        }
    }

    @Override
    public String getId() {
        return ServiceIdGenerator.get(getClass());
    }

    @Override
    public void initialize() {
    }

    @Override
    public void refresh() {
    }

    @Override
    public void destroy() {
        // just wait if something was dispatching events.
        while (this.queue.getActiveCount() > 0) {
            try {
                log.info("Waiting for EventDispatchers to terminate. current count = " + this.queue.getActiveCount());
                this.queue.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.warn(e.getMessage(), e);
            }
        }
    }
}
