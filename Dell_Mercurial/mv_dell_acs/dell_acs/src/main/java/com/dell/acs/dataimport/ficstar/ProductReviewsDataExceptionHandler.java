/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.dataimport.ficstar;

import au.com.bytecode.opencsv.CSVWriter;
import com.sourcen.core.util.FileSystem;
import com.sourcen.dataimport.service.errors.DataExceptionHandlerAdapter;
import com.sourcen.dataimport.service.errors.DataWriterException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Adarsh Kumar.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 3143 $, $Date:: 2012-06-13 11:58:07#$
 */

/**
 * {@inheritDoc}
 */
public final class ProductReviewsDataExceptionHandler extends DataExceptionHandlerAdapter {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static boolean header = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataWriterException(DataWriterException e) {
        CSVWriter writer = null;
        if (e.getErrorId().equalsIgnoreCase("SINGLE_ROW")) {
            try {
                String path = ProductDataExceptionHandler.getErrorFilePath(e.getTableDefinition().getProperty("relativePath"));
                File file = FileSystem.getDefault().getFile(path, true, true);
                writer = new CSVWriter(new FileWriter(file, true));
                Map<String, Object> row = (Map<String, Object>) e.getData().get("row");
                Object[] values = row.values().toArray();
                Object[] keys = row.keySet().toArray();
                String[] stringValues = new String[values.length];
                String[] headerValue = new String[keys.length];
                for (int i = 0; i < stringValues.length; i++) {
                    stringValues[i] = (values[i] != null) ? values[i].toString() : null;
                    headerValue[i] = (keys[i] != null) ? keys[i].toString() : null;
                }
                if (!header) {
                    writer.writeNext(headerValue);
                    writer.flush();
                    header = true;
                }
                String data = stringValues[stringValues.length - 1] + " " + e.getData().get("e");
                stringValues[stringValues.length - 1] = data;
                writer.writeNext(stringValues);
                writer.flush();
                writer.close();
            } catch (Exception ee) {
                logger.error(ee.getMessage());
            }
        } else if (e.getErrorId().equalsIgnoreCase("BATCH_READ")) {
            try {
                String path = ProductDataExceptionHandler.getErrorFilePath(e.getTableDefinition().getProperty("relativePath"));
                File file = FileSystem.getDefault().getFile(path, true, true);
                writer = new CSVWriter(new FileWriter(file, true));
                String[] row = (String[]) e.getData().get("row");
                String[] headerRow = (String[]) e.getData().get("header");
                if (!header) {
                    writer.writeNext(headerRow);
                    header = true;
                }

                String data = row[row.length - 1] + e.getData().get("e");
                row[row.length - 1] = data;
                writer.writeNext(row);

                writer.flush();
                writer.close();
            } catch (Exception exceptionObject) {
                logger.error(exceptionObject.getMessage());
            }
        }else if (e.getErrorId().equalsIgnoreCase("ROW_UPDATE")) {
            String[] headerValue = null;
            try {

                String path = ProductDataExceptionHandler.getErrorFilePath(e.getTableDefinition().getProperty("relativePath"));
                File file = FileSystem.getDefault().getFile(path, true, true);
                writer = new CSVWriter(new FileWriter(file, true));
                Collection<Map<String, Object>> batch = (Collection<Map<String, Object>>) e.getData().get("batch");
                super.writerFailedCount = writerFailedCount + batch.size();
                List<String[]> rows = new ArrayList<String[]>(batch.size());
                for (Map<String, Object> row : batch) {
                    Object[] values = row.values().toArray();
                    Object[] keys = row.keySet().toArray();
                    String[] stringValues = new String[values.length];
                    headerValue = new String[keys.length];

                    for (int i = 0; i < stringValues.length; i++) {
                        boolean exceptionFlag = false;
                        stringValues[i] = (values[i] != null) ? values[i].toString() : null;
                        headerValue[i] = (keys[i] != null) ? keys[i].toString() : null;
                    }
                    rows.add(stringValues);
                }
                if (!header) {
                    writer.writeNext(headerValue);
                    writer.flush();
                    header = true;
                }


                writer.writeAll(rows);
                writer.flush();
                writer.close();
            } catch (Exception exceptionObject) {
                logger.error(exceptionObject.getMessage(), exceptionObject);
            }
        } else if (e.getErrorId().equalsIgnoreCase("BATCH_UPDATE")) {
            String[] headerValue = null;
            try {
                String path = ProductDataExceptionHandler.getErrorFilePath(e.getTableDefinition().getProperty("relativePath"));
                File file = FileSystem.getDefault().getFile(path, true, true);
                writer = new CSVWriter(new FileWriter(file, true));
                Collection<Map<String, Object>> batch = (Collection<Map<String, Object>>) e.getData().get("batch");
                super.writerFailedCount = writerFailedCount + batch.size();

                List<String[]> rows = new ArrayList<String[]>(batch.size());
                for (Map<String, Object> row : batch) {
                    Object[] values = row.values().toArray();
                    Object[] keys = row.keySet().toArray();
                    String[] stringValues = new String[values.length];
                    headerValue = new String[keys.length];

                    for (int i = 0; i < stringValues.length; i++) {
                        stringValues[i] = (values[i] != null) ? values[i].toString() : null;
                        headerValue[i] = (keys[i] != null) ? keys[i].toString() : null;
                    }
                    rows.add(stringValues);
                }
                if (!header) {
                    writer.writeNext(headerValue);
                    writer.flush();
                    header = true;
                }
                writer.writeAll(rows);
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                logger.warn("ProductDataExceptionHandler onDataWriterException() " + e.getMessage());
            }
        } else if (e.getErrorId().equalsIgnoreCase("ROW_DUPLICATE")) {
            String[] headerValue = null;
            try {
                String path = ProductDataExceptionHandler.getErrorFilePath(e.getTableDefinition().getProperty("relativePath"));
                File file = FileSystem.getDefault().getFile(path, true, true);
                writer = new CSVWriter(new FileWriter(file, true));
                Collection<Map<String, Object>> batch = (Collection<Map<String, Object>>) e.getData().get("batch");
                super.writerFailedCount = writerFailedCount + batch.size();

                List<String[]> rows = new ArrayList<String[]>(batch.size());
                for (Map<String, Object> row : batch) {
                    Object[] values = row.values().toArray();
                    Object[] keys = row.keySet().toArray();
                    String[] stringValues = new String[values.length];
                    headerValue = new String[keys.length];

                    for (int i = 0; i < stringValues.length; i++) {
                        stringValues[i] = (values[i] != null) ? values[i].toString() : null;
                        headerValue[i] = (keys[i] != null) ? keys[i].toString() : null;
                    }
                    String data = stringValues[stringValues.length - 1] + "  " + e.getData().get("e");
                    stringValues[stringValues.length - 1] = data;
                    rows.add(stringValues);
                }
                if (!header) {
                    writer.writeNext(headerValue);
                    writer.flush();
                    header = true;
                }

                writer.writeAll(rows);
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                logger.warn("ProductDataExceptionHandler onDataWriterException() " + e.getMessage());
            }
        }
        super.onDataWriterException(e, false);
    }

    static final String getErrorFilePath(String path) {
        String extension = FilenameUtils.getExtension(path);
        return path.substring(0, (path.length() - extension.length() - 1)) + "_errors." + extension;
    }

}
