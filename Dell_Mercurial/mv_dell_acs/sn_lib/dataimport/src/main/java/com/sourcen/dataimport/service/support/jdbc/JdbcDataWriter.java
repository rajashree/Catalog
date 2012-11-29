/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.jdbc;

import com.google.common.base.Joiner;
import com.sourcen.dataimport.service.DataWriter;
import com.sourcen.dataimport.service.support.BaseDataAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: navinr $
 @version $Revision: 2651 $, $Date:: 2012-05-28 08:27:49#$ */
public class JdbcDataWriter extends BaseDataAdapter implements DataWriter {

    /**
     class fields
     */
    private SimpleJdbcTemplate destinationJdbcTemplate;

    /**
     default constructor.
     */
    public JdbcDataWriter() {
    }

    @Override
    public void initialize() {
        super.initialize();
        if (tableDefinition.getDestinationDataSource() != null) {
            destinationJdbcTemplate = JdbcUtils.getSimpleJdbcTemplate(applicationContext, tableDefinition);
        } else {
            destinationJdbcTemplate = new SimpleJdbcTemplate(destDataSource);
        }

        try {
            destDataSource.getConnection();
        } catch (SQLException e) {
            logger.warn("Unable to connect to the Destination database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Object[]> executeBatchUpdate(Collection<Map<String, Object>> batch, String batchId,
                                             Map<Object[], Map<String, Object>> recordsToInsert,
                                             Map<Object[], Map<String, Object>> recordsToUpdate) {
        List<Object[]> result = new LinkedList<Object[]>();


        try {
            preExecuteBatchUpdate(batch, batchId, recordsToInsert, recordsToUpdate);
            if (tableDefinition.getBooleanProperty("disableBatchUpdate", false)) {
                result = executeBatchUpdateAsSingleRecords(batch, batchId, recordsToInsert, recordsToUpdate);
            } else {
                List<Object[]> batchList = new LinkedList<Object[]>();
                List<Object[]> keysList = new LinkedList<Object[]>();
                for (Map.Entry<Object[], Map<String, Object>> entry : recordsToInsert.entrySet()) {
                    batchList.add(entry.getValue().values().toArray());
                    batchList.add(entry.getKey());
                }

                int[] affectedRows = destinationJdbcTemplate.batchUpdate(tableDefinition.sqlInsert, batchList);
                for (int i = 0; i < affectedRows.length; i++) {
                    if (affectedRows[i] != 0) {
                        result.add(keysList.get(i));
                    }
                }
                // TODO - handle updates.

                Boolean insertKeys = tableDefinition.getBooleanProperty("mapKeys", false);

                if (insertKeys) {
                    profiler.markEvent("keyMapping-" + batchId);
                    dataImportLookupService.putKeys(result);
                    profiler.endEvent("keyMapping-" + batchId);
                }

            }
            postExecuteBatchUpdate(batch, batchId, recordsToInsert, recordsToUpdate, result);
        } catch (Exception sqle) {
            logger.warn(sqle.getMessage(), sqle);
        }
        return result;
    }


    protected List<Object[]> executeBatchUpdateAsSingleRecords(Collection<Map<String, Object>> batch, String batchId,
                                                               Map<Object[], Map<String, Object>> recordsToInsert,
                                                               Map<Object[], Map<String, Object>> recordsToUpdate) {
        List<Object[]> result = new LinkedList<Object[]>();

        Boolean insertKeys = tableDefinition.getBooleanProperty("mapKeys", false);

        for (Map.Entry<Object[], Map<String, Object>> entry : recordsToInsert.entrySet()) {
            Object[] item = entry.getValue().values().toArray();
            try {
                destinationJdbcTemplate.update(tableDefinition.sqlInsert, item);

                if (insertKeys) {
                    try {
                        dataImportLookupService.putKey(entry.getKey());
                        result.add(entry.getKey());
                    } catch (Exception e) {
                        logger.warn("Unable to insert key for " + tableDefinition.getDestinationTable() + " :>> key=:" +
                                Joiner.on(",").useForNull("null").join(entry.getKey())
                                + " values :=" + Joiner.on(",").useForNull("null").join(item)
                                + " Error:" + e.getMessage());
                    }
                }
            } catch (Exception sqle) {
                logger.warn("Unable to insert " + tableDefinition.getDestinationTable() + " :>> " +
                        Joiner.on(",").useForNull("null").join(item)
                        + " Error:" + sqle.getMessage());
            }

            // TODO - handle updates.

        }
        return result;
    }

    @Override
    public void processRowBeforeInsertion(Map<String, Object> srcRecord, Map<String, Object> record, Integer recordIndex) {
    }

    @Override
    public void preExecuteBatchUpdate(Collection<Map<String, Object>> batch, String batchId, Map<Object[], Map<String, Object>> recordsToInsert, Map<Object[], Map<String, Object>> recordsToUpdate) {
    }

    @Override
    public void postExecuteBatchUpdate(Collection<Map<String, Object>> batch, String batchId, Map<Object[], Map<String, Object>> recordsToInsert, Map<Object[], Map<String, Object>> recordsToUpdate, List result) {
    }

    /**
     injection
     */
    @Autowired
    private DataSource destDataSource;

    public void setDestDataSource(final DataSource destDataSource) {
        this.destDataSource = destDataSource;
    }

}
