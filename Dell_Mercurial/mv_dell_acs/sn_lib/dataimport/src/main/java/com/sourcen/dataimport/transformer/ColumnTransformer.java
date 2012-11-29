/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.transformer;

import com.sourcen.dataimport.definition.ColumnDefinition;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3092 $, $Date:: 2012-06-11 14:02:29#$
 */

/**
 * ColumnTransformer having the abstract method for Transformation of the the column values
 */
public interface ColumnTransformer {

    /**
     * transform() helps in transformation of the column values
     *
     * @param record           accept the java.util.Map object having row data in the key value paris
     * @param columnDefinition accept the ColumnDefinition objects which is having the definition for the column
     * @param value            of the column in the form of object
     *
     * @return the transformed object value
     * @throws SQLException is throws by this method
     */
    public Object transform(Map<String, Object> record, ColumnDefinition columnDefinition, Object value) throws
            SQLException;

    /**
     * setColumnDefinition() is uesed for setting the columnDefinition reference
     *
     * @param columnDefinition accept reference of specified type
     */
    void setColumnDefinition(ColumnDefinition columnDefinition);

    /**
     * initialize() is the initialization method for the ColumnTransformer Service
     */
    void initialize();

}
