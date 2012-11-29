/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service;

import com.sourcen.dataimport.definition.TableDefinition;
import com.sourcen.dataimport.service.errors.DataExceptionHandler;
import org.slf4j.Logger;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1733 $, $Date:: 2012-04-18 11:52:42#$
 */

/**
 DataAdapter is the having all the Data Manupulation specification
 */
public interface DataAdapter {

    /**
     initilization of the Services
     */
    void initialize();

    /**
     getTableDefinition() helps in the getting the Table definition which has the structure of the file or database tale

     @return TableDefinition object
     */
    TableDefinition getTableDefinition();


    /**
     setTableDefinition() helps in the setting the table definition

     @param tableDefinition objects
     */
    void setTableDefinition(TableDefinition tableDefinition);

    /**
     setExceptionHandler() helps in the settting the Custom Excpeiton Hander for the operation for the data manupulation

     @param exceptionHandler objects
     */
    void setExceptionHandler(DataExceptionHandler exceptionHandler);

    /**
     getExceptionHandler() helps in getting the Exception Handler associated withe implementing class

     @return DataExceptionHandler objects
     */
    DataExceptionHandler getExceptionHandler();

    void setLogger(Logger logger);


}
