/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2764 $, $Date:: 2012-05-29 23:24:47#$
 */
public abstract class ServiceImpl implements Service {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected String serviceId = ServiceIdGenerator.get(getClass());

    // Service ===================================================================================================


    public ServiceImpl() {
    }

    protected ServiceImpl(String serviceId) {
        this.serviceId = serviceId;
    }


    public String getId() {
        return serviceId;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void refresh() {

    }

    @Override
    public void destroy() {
    }

}
