/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.csv;

import au.com.bytecode.opencsv.CSVWriter;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.dataimport.service.DataWriter;
import com.sourcen.dataimport.service.support.BaseDataAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Adarsh
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2757 $, $Date:: 2012-05-29 20:44:49#$
 */
public class CSVDataWriter extends BaseDataAdapter implements DataWriter {

    /**
     * logger class
     */
    private static final Logger logger = LoggerFactory.getLogger(CSVDataWriter.class);

    private ConfigurationService configurationService;

    /**
     * default constructor.
     */
    public CSVDataWriter() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object[]> executeBatchUpdate(Collection<Map<String, Object>> batch, String batchId, Map<Object[],
            Map<String, Object>> recordsToInsert, Map<Object[], Map<String, Object>> recordsToUpdate) {
        final List<String[]> rowDataList = getDataList(batch);
        final String filesystem = configurationService.getProperty("filesystem.dataFiles.directory");
        final String tmpDir = filesystem + configurationService.getProperty("filesystem.dataFiles.temp");
        final CSVWriter csvWriter = getCsvWriter(tmpDir);
        boolean result = writeToFile(csvWriter, rowDataList);
        String msg = (result ? "batch is inserted in the Csv file "
                + batchId : "batch isn't inserted in the Csv file " + batchId);
        logger.info(msg);
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processRowBeforeInsertion(Map<String, Object> srcRecord, Map<String, Object> record,
                                          Integer recordIndex) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preExecuteBatchUpdate(Collection<Map<String, Object>> batch, String batchId, Map<Object[],
            Map<String, Object>> recordsToInsert, Map<Object[], Map<String, Object>> recordsToUpdate) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postExecuteBatchUpdate(Collection<Map<String, Object>> batch, String batchId, Map<Object[],
            Map<String, Object>> recordsToInsert, Map<Object[], Map<String, Object>> recordsToUpdate, List result) {
    }

    //getting the csv writer object by passing the file Name
    private CSVWriter getCsvWriter(String fileName) {
        CSVWriter csvWriter = null;
        final char delemeter = ',';
        Assert.notNull(fileName, "File Path Can't be Null or Empty ");
        fileName += tableDefinition.getDestinationTable().toString() + ".csv";
        try {
            if (fileName != null && fileName.trim().length() > 0) {
                File file = new File(fileName);
                file.setWritable(true);
                if (file == null && !file.canWrite()) {
                    throw new RuntimeException("Unable to Create file");
                }
                csvWriter = new CSVWriter(new FileWriter(file), delemeter);
                if (csvWriter == null) {
                    throw new RuntimeException("Unable to Open Csv Writer");
                }
                return csvWriter;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    //closing the csv writer stream
    private void close(CSVWriter csvWriter) {
        try {
            csvWriter.flush();
            csvWriter.close();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    //actually writing the data into the csv file
    private boolean writeToFile(CSVWriter csvWriter, List<String[]> data) {
        try {
            for (String[] rowArray : data) {
                csvWriter.writeNext(rowArray);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return false;
        }
        return true;
    }

    //converting the batch in to the List of String array
    private List<String[]> getDataList(Collection<Map<String, Object>> batch) {
        final List<String[]> dataArrayList = new ArrayList<String[]>();
        Iterator<Map<String, Object>> batchIterator = batch.iterator();
        while (batchIterator.hasNext()) {
            Map<String, Object> dataMap = (Map<String, Object>) batchIterator.next();
            dataArrayList.add(getDataArray(dataMap));
        }
        return dataArrayList;
    }

    //converting the Map object to the Sting array
    private String[] getDataArray(Map<String, Object> map) {
        final String[] data = new String[map.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue().toString().trim().length() > 0) {
                data[i++] = entry.getValue().toString();
            } else {
                data[i++] = " ";
            }
        }
        return data;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
