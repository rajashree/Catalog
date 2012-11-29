/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support;

import com.sourcen.core.util.Profiler;
import com.sourcen.dataimport.DataImportService;
import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.TableDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1730 $, $Date:: 2012-04-18 11:50:37#$
 */

/**
 {@inheritDoc}
 */
public abstract class AbstractDataImportService implements DataImportService, InitializingBean,
        ApplicationContextAware {

    /**
     logger class
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Profiler profiler = new Profiler(getClass().getCanonicalName());

    protected TableDefinition tableDefinition;

    protected DataImportConfig dataImportConfig;

    protected ApplicationContext applicationContext;

    protected String beanName;

    private Collection<String> dependencies;

    private String tableDefinitionName;

    /**
     Dependent fk values are searched

     @return Collection<String>
     */
    public Collection<String> getDependencies() {
        if (this.dependencies == null) {
            this.dependencies = new LinkedHashSet<String>();
        }
        return dependencies;
    }

    /**
     {@inheritDoc}
     */

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     {@inheritDoc}
     */

    @Override
    public void destroy() {
    }

    /**
     {@inheritDoc}
     */

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    /**
     setter and getter
     */

    public void setDataImportConfig(final DataImportConfig dataImportConfig) {
        this.dataImportConfig = dataImportConfig;
        this.tableDefinition = dataImportConfig.getTableDefinition(this.tableDefinitionName);
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setTableDefinitionName(String tableDefinitionName) {
        this.tableDefinitionName = tableDefinitionName;
    }

    public String getTableDefinitionName() {
        return tableDefinitionName;
    }

    public void setLogger(Logger dataFileLogger) {
        this.logger = dataFileLogger;
    }

    public String getSourceName() {
        return this.tableDefinition.getSourceTable();
    }
}
