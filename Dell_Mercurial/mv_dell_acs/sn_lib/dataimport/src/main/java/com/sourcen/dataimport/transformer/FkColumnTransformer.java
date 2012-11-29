/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.transformer;

import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.service.DataImportLookupService;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1741 $, $Date:: 2012-04-18 11:55:57#$
 */

/**
 {@inheritDoc}
 */
public class FkColumnTransformer extends GenericColumnTransformer {

    /**
     class fields
     */
    private Map<String, Long> tableCache = new ConcurrentHashMap<String, Long>();

    private Boolean ignoreNullForeignKeys = false;

    protected DataImportLookupService dataImportLookupService;

    /**
     FkColumnTransformer default constructor
     */
    public FkColumnTransformer() {
    }

    /**
     {@inheritDoc}
     */
    @Override
    public Object transform(Map<String, Object> record, ColumnDefinition columnDefinition, Object value) throws
            SQLException {
        if (ignoreNullForeignKeys && value == null) {
            return null;
        }

        if (value == null) {
            throw new SQLException("The value for column " + columnDefinition.getDestination() + " is null in table:="
                    + columnDefinition.getTableDefinition().getDestinationTable());
        }

        String srcFkTable = columnDefinition.getReferenceTable().getSourceTable();
        Long transformedValue = null;
        if (tableCache.isEmpty()) {
            tableCache = dataImportLookupService.getTableCache(srcFkTable);
        }
        // try-catch just in case class cast exception occurs
        transformedValue = tableCache.get(value.toString());
        if (transformedValue == null) {
            if (columnDefinition.getAllowNull()) {
                return null;
            }
            throw new ForeignKeyException("Unable to find mapping, srcFkTable:="
                    + srcFkTable + " srcId:=" + value + ", originalRecord :=" + value);
        }


        return transformedValue;
    }

    /**
     setter() and getter()
     */
    public void setDataImportLookupService(DataImportLookupService dataImportLookupService) {
        this.dataImportLookupService = dataImportLookupService;
    }
}
