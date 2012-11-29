/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.registrar;

import com.sourcen.core.event.SimpleEvent;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class BeanFactoryInitializedEvent extends SimpleEvent<BeanFactory> {

    public static final String EVENT_ID = "BEAN_FACTORY_INITIALIZED";

    public BeanFactoryInitializedEvent() {
        super(EVENT_ID);
    }

    private BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    public BeanFactory getBeanFactory() {
        return this.beanFactory;
    }
}
