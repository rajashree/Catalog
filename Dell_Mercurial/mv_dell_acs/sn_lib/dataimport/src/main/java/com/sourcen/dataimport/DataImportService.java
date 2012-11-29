/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NamedBean;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 572 $, $Date:: 2012-03-08 10:26:09#$
 */

/**
 * DataImportService is having abstract set of functionality
 * for Data import form different sources
 */

public interface DataImportService extends Runnable, InitializingBean,
        DisposableBean, NamedBean, BeanNameAware {

    /**
     * return the dependent services for this service,
     * usually based on the table configuration.
     *
     * @return Collection of the Service Names
     */
    public Collection<String> getDependencies();

    /**
     * getTableDefinitionName() return the table Definition name
     *
     * @return Table Name
     */
    public String getTableDefinitionName();

    /**
     * destroy() is the life cycle and callback method
     * for the Service
     */
    public void destroy();

}
