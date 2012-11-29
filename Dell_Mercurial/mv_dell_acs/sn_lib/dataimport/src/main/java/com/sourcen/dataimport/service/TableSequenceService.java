/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service;

import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.TableDefinition;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1737 $, $Date:: 2012-04-18 11:54:11#$
 */

/**
 TableSequenceService help in generating the sequence for the unique identification of the reacored
 */
public interface TableSequenceService {

    /**
     setDataImportConfig() setting the reference of the DataImportConfig

     @param dataImportConfig accept the DataImportConfig reference object
     */
    void setDataImportConfig(DataImportConfig dataImportConfig);

    /**
     getSequenceId() is used for generating the sequence values

     @param tableName  accept the table name for which the sequence is to be generated
     @param definition accept the column definition reference object
     @return newly generated sequence number
     */
    Long getSequenceId(String tableName, ColumnDefinition definition);

    /**
     getSequenceId() is used for generating the sequence id on the based of the Tabledefinition object and
     coloumnDefinition object

     @param tableDefinition accept the reference object
     @param definition      accept the reference object
     @return newly generated sequence number
     */
    Long getSequenceId(TableDefinition tableDefinition, ColumnDefinition definition);
}
