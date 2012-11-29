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
 * @version $Revision: 3964 $, $Date:: 2012-07-12 14:51:53#$
 */

/**
 * DataReaderException class help in the Specifying the Data Reading Exception
 */
public class DataReaderException extends RuntimeException {

    private String errorId;
    private TableDefinition tableDefinition;

    /**
     * DataReaderException default constructor
     */
    public DataReaderException() {
    }

    /**
     * DataReaderException constructor
     *
     * @param message for the Exception
     */
    public DataReaderException(String message) {
        super(message);
    }

    public DataReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataReaderException(String errorId, TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
        this.errorId = errorId;
    }

    /**
     * DataReaderException constructor
     *
     * @param cause accept the Throwable object
     */
    public DataReaderException(Throwable cause) {
        super(cause);
    }


    /**
     * setter() for the DataReaderException object reference its help in setting multiple properties for the
     * DataReaderException and those properties can be used in the customactons
     *
     * @param key   for the properties
     * @param value of the properties
     * @return DataReaderException object
     */
    public DataReaderException set(String key, Object value) {
        getData().put(key, value);
        return this;
    }

    /**
     * Properties and setter and getter() for value injection on properties
     */
    protected Map<String, Object> data;

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getData() {
        if (data == null) {
            data = new HashMap<String, Object>();
        }
        return data;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public TableDefinition getTableDefinition() {
        return  tableDefinition;
    }

    public void setTableDefinition(TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }
}
