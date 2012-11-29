/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.definition;

import com.sourcen.dataimport.service.DataImportLookupService;
import com.sourcen.dataimport.service.TableSequenceService;

import java.util.LinkedHashSet;

/**
 Schema provides the container for holding the database and its consist of multiple table definitions.

 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: adarsh $
 @version $Revision: 1700 $, $Date:: 2012-04-18 11:29:08#$ */
public final class Schema {

    /**
     class fields
     */
    private LinkedHashSet<TableDefinition> tables = new LinkedHashSet<TableDefinition>();

    private DataImportConfig dataImportConfig;

    private TableSequenceService tableSequenceService;

    private DataImportLookupService dataImportLookupService;

    /**
     default constructor.
     */
    public Schema() {
    }

    public TableDefinition getDefinitionByDestination(final String tableName) {
        for (TableDefinition tableDefinition : tables) {
            if (tableDefinition.getDestinationTable().equalsIgnoreCase(tableName)) {
                return tableDefinition;
            }
        }
        return null;
    }

    public TableDefinition getDefinitionBySource(final String tableName) {
        for (TableDefinition tableDefinition : tables) {
            if (tableDefinition.getSourceTable().equalsIgnoreCase(tableName)) {
                return tableDefinition;
            }
        }
        return null;
    }

    /**
     getters and setters
     */
    public LinkedHashSet<TableDefinition> getTables() {
        return tables;
    }

    public void setTables(final LinkedHashSet<TableDefinition> tables) {
        this.tables = tables;
    }

    public DataImportConfig getDataImportConfig() {
        return dataImportConfig;
    }

    public void setDataImportConfig(final DataImportConfig dataImportConfig) {
        this.dataImportConfig = dataImportConfig;
    }

    public TableSequenceService getTableSequenceService() {
        return tableSequenceService;
    }

    public void setTableSequenceService(final TableSequenceService tableSequenceService) {
        this.tableSequenceService = tableSequenceService;
    }

    public DataImportLookupService getDataImportLookupService() {
        return dataImportLookupService;
    }

    public void setDataImportLookupService(final DataImportLookupService dataImportLookupService) {
        this.dataImportLookupService = dataImportLookupService;
    }
}
