/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.errors;

import com.sourcen.dataimport.definition.TableDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1711 $, $Date:: 2012-04-18 11:37:39#$
 */

/**
 DataWriterException class help in the Specifying the Data Writing Exception
 */
public class DataWriterException extends RuntimeException {

    /**
     class fields
     */
    private String errorId;

    private TableDefinition tableDefinition;

    protected Map<String, Object> data;

    /**
     ' DataWriterException constructor

     @param e accept the Exception object
     */
    public DataWriterException(Exception e) {
        super(e.getMessage());
    }

    /**
     DataWriterException constructor

     @param errorId         accept the errorId which is assign to every task
     @param tableDefinition have complete structure of the table or file which is going to write
     */
    public DataWriterException(String errorId, TableDefinition tableDefinition) {
        this.errorId = errorId;
        this.tableDefinition = tableDefinition;
    }

    /**
     DataWriterException constructor

     @param message         accept the String msg for the Exception
     @param errorId         accept the errorId which is assign to every task
     @param tableDefinition have complete structure of the table or file which is going to write
     */
    public DataWriterException(String message, String errorId, TableDefinition tableDefinition) {
        super(message);
        this.errorId = errorId;
        this.tableDefinition = tableDefinition;
    }

    /**
     DataWriterException constructor

     @param message         accept the String msg for the Exception
     @param cause           accept the Throwable and its implementing class objects
     @param errorId         accept the errorId which is assign to every task
     @param tableDefinition have complete structure of the table or file which is going to write
     */
    public DataWriterException(String message, Throwable cause, String errorId, TableDefinition tableDefinition) {
        super(message, cause);
        this.errorId = errorId;
        this.tableDefinition = tableDefinition;
    }

    /**
     \ DataWriterException constructor

     @param cause           accept the Throwable and its implementing class objects
     @param errorId         accept the errorId which is assign to every task
     @param tableDefinition have complete structure of the table or file which is going to write
     */
    public DataWriterException(Throwable cause, String errorId, TableDefinition tableDefinition) {
        super(cause);
        this.errorId = errorId;
        this.tableDefinition = tableDefinition;
    }

    /**
     setter() for the DataWriterException class properties its help in setting multiple properties for the
     DataWriterException class object

     @param key   for the properties
     @param value for the properties
     @return DataWriterException object
     */
    public DataWriterException set(String key, Object value) {
        getData().put(key, value);
        return this;
    }

    /**
     setter() and getter()
     */

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    public Map<String, Object> getData() {
        if (data == null) {
            data = new HashMap<String, Object>();
        }
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
