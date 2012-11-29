/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public abstract class BeanProcessorAdapter implements BeanProcessor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supportsBean(final Class clazz) {
        return false;
    }

    @Override
    public Map<String, Object> preProcessBeanValues(final Map<String, Object> row) {
        return row;
    }

    @Override
    public Object preProcessBeforePersist(final Object bean, final Map<String, Object> row) {
        return bean;
    }

    @Override
    public Object postProcessAfterPersist(final Object bean, final Map<String, Object> row) {
        return bean;
    }
}
