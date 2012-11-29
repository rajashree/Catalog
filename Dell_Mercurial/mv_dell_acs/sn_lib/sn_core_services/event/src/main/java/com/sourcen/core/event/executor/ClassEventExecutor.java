/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.executor;

import com.sourcen.core.event.Event;
import com.sourcen.core.event.EventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class ClassEventExecutor extends ObjectEventExecutor {

    private static final Logger log = LoggerFactory.getLogger(ClassEventExecutor.class);

    private Class clazz;

    public ClassEventExecutor(Class clazz, Method method, Event event) {
        super(method, event);
        this.clazz = clazz;
    }

    @Override
    protected Object getObject() {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new EventException(e);
        }
    }
}
