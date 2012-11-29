/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.executor;

import com.sourcen.core.event.Event;
import com.sourcen.core.event.EventException;
import com.sourcen.core.event.registrar.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class BeanEventExecutor extends ObjectEventExecutor {

    private static final Logger log = LoggerFactory.getLogger(BeanEventExecutor.class);

    private String beanName;
    private static BeanFactory beanFactory;

    public BeanEventExecutor(String beanName, Method method, Event event) {
        super(method, event);
        this.beanName = beanName;
    }

    @Override
    protected Object getObject() {
        if (beanFactory == null) throw new EventException("BeanFactory has not yet been initialized");
        try {
            return beanFactory.getBean(beanName);
        } catch (Exception e) {
            throw new EventException(e);
        }
    }

    public static void setBeanFactory(BeanFactory factory) {
        BeanEventExecutor.beanFactory = factory;
    }
}
