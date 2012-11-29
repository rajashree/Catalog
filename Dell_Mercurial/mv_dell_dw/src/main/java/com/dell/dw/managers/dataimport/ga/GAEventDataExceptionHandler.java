/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.managers.dataimport.ga;

import au.com.bytecode.opencsv.CSVWriter;
import com.sourcen.core.util.DateUtils;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.StringUtils;
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
 * @author $LastChangedBy: rajashreem $
 * @version $Revision: 3882 $, $Date:: 2012-07-09 12:09:42#$
 */

/**
 * {@inheritDoc}
 */
public class GAEventDataExceptionHandler extends DataExceptionHandlerAdapter {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static boolean header = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDataWriterException(final DataWriterException e) {
        CSVWriter writer = null;
        if (e.getErrorId().equalsIgnoreCase("SINGLE_ROW")) {
            try {
                String path = GAPageViewDataExceptionHandler.getErrorFilePath(e.getTableDefinition().getProperty("relativePath"), e.getTableDefinition().getProperty("startDate"));
                File file = FileSystem.getDefault().getFile("logs/"+path, true, true);
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
                String path = GAPageViewDataExceptionHandler.getErrorFilePath(e.getTableDefinition().getProperty("relativePath"), e.getTableDefinition().getProperty("startDate"));
                File file = FileSystem.getDefault().getFile("logs/"+path, true, true);
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

                String path = GAPageViewDataExceptionHandler.getErrorFilePath(e.getTableDefinition().getProperty("relativePath"), e.getTableDefinition().getProperty("startDate"));
                File file = FileSystem.getDefault().getFile("logs/"+path, true, true);
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
                String path = GAPageViewDataExceptionHandler.getErrorFilePath(e.getTableDefinition().getProperty("relativePath"), e.getTableDefinition().getProperty("startDate"));
                File file = FileSystem.getDefault().getFile("logs/"+path, true, true);
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
                logger.warn("GAEventDataExceptionHandler onDataWriterException() " + e.getMessage());
            }
        } else if (e.getErrorId().equalsIgnoreCase("ROW_DUPLICATE")) {
            String[] headerValue = null;
            try {
                String path = GAPageViewDataExceptionHandler.getErrorFilePath(e.getTableDefinition().getProperty("relativePath"), e.getTableDefinition().getProperty("startDate"));
                File file = FileSystem.getDefault().getFile("logs/"+path, true, true);
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
                logger.warn("GAEventDataExceptionHandler onDataWriterException() " + e.getMessage());
            }
        }
        super.onDataWriterException(e, false);
    }

    static final String getErrorFilePath(String path, String startDate) {
        String extension = FilenameUtils.getExtension(path);
        String loggerName = "error_feeds"+ StringUtils.getSimpleString(path.substring(0, (path.length() - extension.length() - 1)))+"_"+ DateUtils.TIMESTAMP_DATEFORMAT.format(DateUtils.getDate(startDate)).replaceAll(":", "_");
        return loggerName+"_errors." + extension;
    }
}
