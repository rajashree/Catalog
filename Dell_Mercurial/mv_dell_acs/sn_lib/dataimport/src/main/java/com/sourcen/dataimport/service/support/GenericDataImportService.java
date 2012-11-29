/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support;

import com.sourcen.dataimport.definition.Key;
import com.sourcen.dataimport.service.DataReader;
import com.sourcen.dataimport.service.DataWriter;
import com.sourcen.dataimport.service.errors.DataExceptionHandler;
import com.sourcen.dataimport.service.errors.DataWriterException;
import com.sourcen.dataimport.transformer.ForeignKeyException;
import com.sourcen.dataimport.transformer.GenericRowTransformer;
import com.sourcen.dataimport.transformer.RowTransformer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 3964 $, $Date:: 2012-07-12 14:51:53#$
 */

/**
 * {@inheritDoc}
 */
public class GenericDataImportService extends AbstractDataImportService
        implements InitializingBean {

    /**
     * class fields
     */
    private DataReader dataReader;

    private DataWriter dataWriter;

    private RowTransformer rowTransformer;

    private DataImportListener listener;

    protected DataExceptionHandler exceptionHandler;


    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        if (listener != null) {
            listener.beginRun(this);
        }
        try {
            if (tableDefinition == null) {
                tableDefinition = dataReader.getTableDefinition();
            }
            logger.info("execution initialized");
            // never call tableDefinition before initialization.
            profiler.markEvent("process "
                    + tableDefinition.getDestinationTable() + " records");

            profiler.markEvent("reading-records");
            Collection<Map<String, Object>> result = dataReader.getRows();
            profiler.markEvent("reading-records");

            // calculate the pages
            int batchCount = Double.valueOf(
                    Math.ceil(result.size() / getBatchSize())).intValue();
            Iterator<Map<String, Object>> iterator = result.iterator();
            profiler.markEvent("process "
                    + tableDefinition.getDestinationTable()
                    + " records total count:" + result.size());
            for (int i = 0; i <= batchCount && !result.isEmpty(); i++) {
                Collection<Map<String, Object>> batch = new LinkedList<Map<String, Object>>();
                for (int j = 1; j <= getBatchSize(); j++) {
                    if (iterator.hasNext()) {
                        batch.add(iterator.next());
                    }
                }

                String batchId = "batch-" + (i * getBatchSize()) + "-"
                        + ((i * getBatchSize()) + batch.size());
                String msg = "Processing table "
                        + tableDefinition.getDestinationTable() + " " + batchId;
                logger.info(msg);
                try {
                    processAsBatch(batch, batchId, i);
                } catch (Exception e) {
                    if (listener != null) {
                        listener.batchFailed(this, batchId, batchSize);
                    }
                    DataWriterException dataWriterException = new DataWriterException(
                            "BATCH_UPDATE", tableDefinition);
                    dataWriterException.set("batch", batch).set("batchId",
                            batchId);

                    dataWriter.getExceptionHandler().onDataWriterException(
                            dataWriterException);
                    logger.error("Unable to process batch for table:"
                            + tableDefinition.getDestinationTable()
                            + " batchId:" + batchId, e);
                }
            }
            profiler.endEvent("process "
                    + tableDefinition.getDestinationTable() + " records");
        } finally {
            if (listener != null) {
                listener.endRun(this);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    }

    /**
     * processAsBatch() is for processing the data in the form of the batch
     *
     * @param batch      collection object having the Map data representing the row
     *                   value
     * @param batchId    value assigned to every batch at the time of the creation
     * @param batchIndex index for every batch
     */
    public void processAsBatch(Collection<Map<String, Object>> batch,
                               String batchId, Integer batchIndex) {
        Map<Object[], Map<String, Object>> recordsToUpdate = new LinkedHashMap<Object[], Map<String, Object>>(
                batchSize);
        Map<Object[], Map<String, Object>> recordsToInsert = new LinkedHashMap<Object[], Map<String, Object>>(
                batchSize);

        int[] recordsToInsertKeysCache = new int[batch.size()];
        Object[] recordsToInsertKeyObjCache = new Object[batch.size()];

        boolean isKeysMappedInLookup = tableDefinition.getPrimaryKey() != null;
        if (isKeysMappedInLookup) {
            if (tableDefinition.getPrimaryKey() == null) {
                logger.info("Ignoring update check on table :"
                        + tableDefinition.getDestinationTable()
                        + " as it doesn't have a primary key");
                isKeysMappedInLookup = false;
            } else if (tableDefinition.getPrimaryKey().getSourceKey() == null) {
                logger.info("Ignoring update check on table :"
                        + tableDefinition.getDestinationTable()
                        + " as the primary key doesn't have a source mapping");
                isKeysMappedInLookup = false;
            }
        }

        Iterator<Map<String, Object>> iter = batch.iterator();
        int index = 0;

        while (iter.hasNext()) {
            Map<String, Object> item = iter.next();
            index++;
            int recordIndex = (batchIndex * batchSize) + index;
            if (listener != null) {
                listener.startLine(this, recordIndex);
            }

            Map<String, Object> transformedItem = null;
            try {
                transformedItem = getRowTransformer().transform(item);

                dataWriter.processRowBeforeInsertion(item, transformedItem,
                        recordIndex);
                boolean isUpdateOperation = false;
                if (isKeysMappedInLookup) {
                    // check lookupIf item exists.
                    Object srcKey = item.get(tableDefinition.getPrimaryKey()
                            .getSourceKey());
                    // the key can be null if the source is null. this can
                    // happen if we are migrating data from a
                    // table which does not really have a primary key.
                    Long destKeyId = null;
                    if (srcKey != null) {
                        try {
                            destKeyId = dataImportConfig.getDataImportLookupService().getKey(
                                    tableDefinition.getSourceTable(),
                                    String.valueOf(srcKey));
                        } catch (Exception e) {
                            //logger.error("unable to convert into integer value := "+srcKey);
                            throw new RuntimeException("unable to convert into get the destinationKey for srcKey:= "
                                    + srcKey + " and srcTableName:=" + tableDefinition.getSourceTable(), e);
                        }
                    }
                    if (destKeyId != null) {
                        transformedItem.put(tableDefinition
                                .getPrimaryKey().getDestinationKey(),
                                destKeyId);
                        logger.info("marking for update record with srcKey:="
                                + srcKey + " destKey:=" + destKeyId);
                        isUpdateOperation = true;
                    }
                }

                Object[] key = getMappingKey(item, transformedItem);
                if (isUpdateOperation) {
                    recordsToUpdate.put(key, transformedItem);
                } else {
                    int srcKeyHashCode = key[1].toString().intern().hashCode();
                    int recordBatchIndex = index - 1;
                    if (recordsToInsert.size() == 0) {
                        recordsToInsertKeysCache[recordBatchIndex] = srcKeyHashCode;
                        recordsToInsertKeyObjCache[recordBatchIndex] = key;
                        recordsToInsert.put(key, transformedItem);
                    } else {

                        int recordExists = -1;
                        for (int i = 0; i < recordBatchIndex; i++) {
                            if (recordsToInsertKeysCache[i] == srcKeyHashCode) {
                                // record exists.
                                recordExists = i;
                                break;
                            }
                        }
                        if (recordExists == -1) {
                            // record does not exist in our cache.
                            recordsToInsertKeysCache[recordBatchIndex] = srcKeyHashCode;
                            recordsToInsertKeyObjCache[recordBatchIndex] = key;
                            recordsToInsert.put(key, transformedItem);
                        } else {

                            Object[] duplicateKey = (Object[]) recordsToInsertKeyObjCache[recordExists];
                            logger.info("found a duplicate record in same batch rowId="
                                    + batchIndex
                                    + " for key :="
                                    + duplicateKey[1]);
                            recordsToInsert.put(duplicateKey, transformedItem);
                            DataWriterException dataWriterException = new DataWriterException("SINGLE_ROW", tableDefinition);
                            dataWriterException
                                    .set("e", "ERROR_WRITE := Duplicate Record Found in Side a Batch  having batch id := " + batchId)
                                    .set("row", transformedItem);
                            exceptionHandler.onDataWriterException(dataWriterException);
                        }
                        /*
                               * for (Map.Entry entry : recordsToInsert.entrySet()) {
                               * Object[] tempKey = (Object[]) entry.getKey(); if
                               * (key[1].toString().equals(tempKey[1].toString())) {
                               * recordsToInsert.remove(tempKey); break; } }
                               * recordsToInsert.put(key, transformedItem);
                               */
                    }
                }
            } catch (ForeignKeyException fkEx) {
                if (listener != null) {
                    listener.failureLine(this, recordIndex, fkEx);
                }
                logger.error("Unable to find a mapping, foreign key exception for the recordIndex : " + recordIndex);
                DataWriterException dataWriterException = new DataWriterException("SINGLE_ROW", tableDefinition);
                dataWriterException
                        .set("e", "ERROR_WRITE := Unable to find a mapping, foreign key exception for the recordIndex : " + recordIndex)
                        .set("row", item);
                exceptionHandler.onDataWriterException(dataWriterException);
            } catch (Exception e) {
                DataWriterException dataWriterException = new DataWriterException("SINGLE_ROW", tableDefinition);
                dataWriterException
                        .set("e", "ERROR_WRITE := " + e.getMessage() + " " + recordIndex)
                        .set("row", item);
                exceptionHandler.onDataWriterException(dataWriterException);
                if (listener != null) {
                    listener.failureLine(this, recordIndex, e);
                }
                logger.error(e.getMessage(), e);
            } finally {
                if (listener != null) {
                    listener.endLine(this, recordIndex);
                }
            }
        }

        profiler.markEvent("batchUpdate-" + batchId);
        List<Object[]> successfulKeys = executeBatchUpdate(batch, batchId,
                recordsToInsert, recordsToUpdate);
        profiler.endEvent("batchUpdate-" + batchId);
    }

    /**
     * getMappingKey() is for getting the related mapping keys from the lookup
     * table // 0=srcTableName, 1= srcKey, 2=destTable, 3=destKey});
     *
     * @param sourceRecord
     * @param destinationRecord
     * @return
     */
    protected Object[] getMappingKey(Map<String, Object> sourceRecord,
                                     Map<String, Object> destinationRecord) {

        Key primaryKey = tableDefinition.getPrimaryKey();
        if (primaryKey == null) {
            // just return a null array.
            return new Object[]{};
        }
        Object[] keyMapping = new Object[4];

        keyMapping[0] = tableDefinition.getSourceTable();
        keyMapping[2] = tableDefinition.getDestinationTable();

        if (Key.Type.is(primaryKey.getSourceKeySource(), Key.Type.SOURCE)) {
            keyMapping[1] = sourceRecord.get(tableDefinition.getPrimaryKey()
                    .getSourceKey());
        } else {
            keyMapping[1] = destinationRecord.get(tableDefinition
                    .getPrimaryKey().getSourceKey());
        }

        if (Key.Type.is(primaryKey.getDestinationKeySource(), Key.Type.SOURCE)) {
            keyMapping[3] = sourceRecord.get(tableDefinition.getPrimaryKey()
                    .getDestinationKey());
        } else {
            keyMapping[3] = destinationRecord.get(tableDefinition
                    .getPrimaryKey().getDestinationKey());
        }

        return keyMapping;
    }

    /**
     * executeBatchUpdate() is for executing the batch update
     *
     * @param batch           Collection object having the Map object which represent the
     *                        rows of file or table
     * @param batchId         id value which is assigned to batch at the time of batch
     *                        creation
     * @param recordsToInsert group of the records which is having new value for insertion
     * @param recordsToUpdate group of the records which is having updated value for
     *                        updating
     * @return list object having the status of the insertion and updation
     */
    public List<Object[]> executeBatchUpdate(
            Collection<Map<String, Object>> batch, String batchId,
            Map<Object[], Map<String, Object>> recordsToInsert,
            Map<Object[], Map<String, Object>> recordsToUpdate) {
        try {
            return dataWriter.executeBatchUpdate(batch, batchId,
                    recordsToInsert, recordsToUpdate);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    protected RowTransformer getRowTransformer() {
        if (rowTransformer == null) {
            rowTransformer = new GenericRowTransformer(this.tableDefinition);
        }
        return rowTransformer;
    }

    protected Integer batchSize = 100;

    protected Integer getBatchSize() {
        return batchSize;
    }

    @Autowired
    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * setter and getter
     */
    public void setDataReader(DataReader dataReader) {
        this.dataReader = dataReader;
    }

    public void setDataWriter(DataWriter dataWriter) {
        this.dataWriter = dataWriter;
    }

    public void setRowTransformer(RowTransformer rowTransformer) {
        this.rowTransformer = rowTransformer;
    }

    public void setListener(DataImportListener listener) {
        this.listener = listener;
    }

    public void setExceptionHandler(DataExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
}
