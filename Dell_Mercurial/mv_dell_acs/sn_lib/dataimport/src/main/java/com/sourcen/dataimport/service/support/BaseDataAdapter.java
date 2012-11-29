/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support;

import com.sourcen.core.util.Profiler;
import com.sourcen.dataimport.definition.TableDefinition;
import com.sourcen.dataimport.service.DataAdapter;
import com.sourcen.dataimport.service.DataImportLookupService;
import com.sourcen.dataimport.service.errors.DataExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: adarsh $
 @version $Revision: 1731 $, $Date:: 2012-04-18 11:51:14#$ */
public abstract class BaseDataAdapter implements ApplicationContextAware, DataAdapter {


    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Profiler profiler = null;

    protected TableDefinition tableDefinition;

    protected DataExceptionHandler exceptionHandler;

    /**
     default constructor
     */
    public BaseDataAdapter() {
    }

    @Override
    public void setExceptionHandler(DataExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    @Override
    public void setTableDefinition(TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    @Override
    public void initialize() {
        profiler = new Profiler(this.tableDefinition.getDestinationTable());
        Assert.notNull(tableDefinition, "tableDefinition cannot be null for :=" + getClass());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Autowired
    protected DataImportLookupService dataImportLookupService;

    @Autowired
    protected ApplicationContext applicationContext;


    public void setDataImportLookupService(DataImportLookupService dataImportLookupService) {
        this.dataImportLookupService = dataImportLookupService;
    }

    public DataExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
