/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.executor;

import com.sourcen.core.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class ObjectEventExecutor extends AbstractEventExecutor {

    private static final Logger log = LoggerFactory.getLogger(ObjectEventExecutor.class);

    private Object instance;
    private Method method;
    private Event event;


    public ObjectEventExecutor(Method method, Event event) {
        this(null, method, event);
    }

    public ObjectEventExecutor(Object instance, Method method, Event event) {
        this.instance = instance;
        this.method = method;
        this.event = event;
        this.priority = event.getPriority();
    }


    @Override
    public void run() {
        try {
            this.result = method.invoke(getObject(), event);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    protected Object getObject() {
        if (instance == null) {
            throw new IllegalArgumentException("Execution instance should never be null, Please pass this value in the constructor.");
        }
        return this.instance;
    }

    @Override
    public String toString() {
        return "ObjectEventExecutor{" +
                "instance=" + instance +
                ", method=" + method +
                ", event=" + event +
                '}';
    }
}
