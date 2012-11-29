/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.definition;

import com.sourcen.dataimport.service.DataImportLookupService;
import com.sourcen.dataimport.service.TableSequenceService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: adarsh $
 @version $Revision: 1697 $, $Date:: 2012-04-18 11:26:35#$ */
public final class DataImportConfig implements InitializingBean, ApplicationContextAware {

    /**
     logger class
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DataImportConfig.class);

    /**
     class fields
     */
    private Schema schema;

    private String configFilePath;

    private ApplicationContext applicationContext;

    private TableSequenceService tableSequenceService;

    private DataImportLookupService dataImportLookupService;

    /**
     default constructor.
     */
    public DataImportConfig() {
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        if (configFilePath != null) {
            schema = SchemaProvider.getInstance().getSchema(configFilePath);
            for (TableDefinition tableDefinition : schema.getTables()) {
                tableDefinition.setDataImportConfig(this);
                if (logger.isDebugEnabled()) {
                    logger.debug("parsed " + tableDefinition.getDestinationTable());
                }
            }
            // initialize the tables.
            for (TableDefinition tableDefinition : schema.getTables()) {
                tableDefinition.afterPropertiesSet();
            }
        } else {
        //    logger.info("configFilePath not initialized yet.");
        }
    }

    /**
     {@inheritDoc}

     @throws BeansException
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     getters and setters
     */

    public Schema getSchema() {
        return schema;
    }

    public TableDefinition getTableDefinition(final String tableDefinitionName) {
        return schema.getDefinitionByDestination(tableDefinitionName);
    }

    public void setConfigFilePath(final String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public TableSequenceService getTableSequenceService() {
        return tableSequenceService;
    }

    public void setTableSequenceService(final TableSequenceService tableSequenceService) {
        tableSequenceService.setDataImportConfig(this);
        this.tableSequenceService = tableSequenceService;
    }

    public DataImportLookupService getDataImportLookupService() {
        return dataImportLookupService;
    }

    public void setDataImportLookupService(final DataImportLookupService dataImportLookupService) {
        this.dataImportLookupService = dataImportLookupService;
    }


}

