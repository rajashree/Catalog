/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.csv;

import com.google.common.base.Joiner;
import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.service.DataReader;
import com.sourcen.dataimport.service.support.BaseDataAdapter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


public class ExcelDataReader extends BaseDataAdapter implements DataReader {

    /**
     default Constructor
     */
    public ExcelDataReader() {
    }


    public Collection<Map<String, Object>> getRows() {

        String tableType = tableDefinition.getProperty("tableType");
        if (tableType == null || (!tableType.equalsIgnoreCase("spreadsheet")
                && tableType.equalsIgnoreCase("excel"))) {
            throw new RuntimeException("Unable to migrate table:="
                    + tableDefinition.getDestinationTable() + " as tableType is invalid");
        }
        String excelPath = tableDefinition.getProperty("path");
        Assert.notNull(excelPath, "path cannot be null.");

        Integer rowsToSkip = tableDefinition.getIntegerProperty("rowsToSkip", 0);
        logger.info("skipping rows :=" + rowsToSkip);

        Integer maxRows = tableDefinition.getIntegerProperty("maxRows", 10000);
        logger.info("will process maximum of rows :=" + maxRows);


        String[] sheetIndexObj = tableDefinition.getProperty("sheetIndex", "0").split(",");
        logger.info("Using sheetIndex as:=" + Joiner.on(",").join(sheetIndexObj));
        Integer[] sheetIndexes = new Integer[sheetIndexObj.length];
        for (int i = 0; i < sheetIndexes.length; i++) {
            sheetIndexes[i] = Integer.parseInt(sheetIndexObj[i].trim());
        }

        Collection<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
        try {
            POIFSFileSystem fsFileSystem;
            if (tableDefinition.getBooleanProperty("isPathAbsolute", false)) {
                File excelFile = new File(excelPath);
                if (!excelFile.canRead()) {
                    throw new FileNotFoundException("Unable to read from file :=" + excelPath);
                }
                fsFileSystem = new POIFSFileSystem(new FileInputStream(excelFile));
            } else {
                fsFileSystem = new POIFSFileSystem(getClass().getResourceAsStream(excelPath));
            }
            HSSFWorkbook workbook = new HSSFWorkbook(fsFileSystem);
            for (int i = 0; i < sheetIndexes.length && i < workbook.getNumberOfSheets(); i++) {
                processSheet(workbook, sheetIndexes[i], rowsToSkip, maxRows, result);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    protected void processSheet(HSSFWorkbook workbook, Integer sheetIndex, Integer rowsToSkip, Integer maxRows,
                                Collection<Map<String, Object>> result) {
        try {
            HSSFSheet sheetObject = workbook.getSheetAt(sheetIndex);
            Iterator<HSSFRow> rowIterator = sheetObject.rowIterator();
            for (int i = 0; i < rowsToSkip; i++) {
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }
            }

            // check the columns indexes once.
            for (ColumnDefinition columnDefinition : tableDefinition.getColumns()) {
                if (columnDefinition.getIndex() == null
                        && tableDefinition.getPrimaryKey() != null
                        && tableDefinition.getPrimaryKey().getDestinationColumn()
                        != columnDefinition) {
                    logger.info(columnDefinition.getTableDefinition().getDestinationTable() + "."
                            + columnDefinition.getDestination() + " has a null index and will be skipped");
                }
            }

            for (int i = 0; i < maxRows; i++) {
                if (rowIterator.hasNext()) {
                    try {
                        Map<String, Object> record = new HashMap<String, Object>();
                        HSSFRow row = rowIterator.next();

                        // process each cell in the current row.
                        for (ColumnDefinition columnDefinition : tableDefinition.getColumns()) {
                            if (columnDefinition.getIndex() == null) {
                                continue;
                            }


                            HSSFCell cell = row.getCell(columnDefinition.getIndex());
                            Object attributeValue = null;
                            if (cell != null) {
                                int cellType = cell.getCellType();

                                if (cellType == HSSFCell.CELL_TYPE_BOOLEAN) {
                                    attributeValue = "" + cell.getBooleanCellValue();
                                } else if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        attributeValue = new java.sql.Date(cell.getDateCellValue().getTime());
                                    } else {
                                        Double doubleValue = cell.getNumericCellValue();
                                        if ((doubleValue % 1) == 0) {
                                            // whole number.
                                            attributeValue = String.valueOf(doubleValue.longValue());
                                        } else {
                                            // fraction.
                                            attributeValue = String.valueOf(doubleValue);
                                        }
                                    }
                                } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
                                    String strVal = cell.getStringCellValue();
                                    if (strVal != null) {
                                        strVal = strVal.trim();
                                        if (strVal.isEmpty()) {
                                            strVal = null;
                                        }
                                    }
                                    attributeValue = strVal;
                                } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                                    attributeValue = null;
                                } else if (cellType == HSSFCell.CELL_TYPE_FORMULA) {
                                    try {
                                        attributeValue = cell.getStringCellValue();
                                    } catch (Exception e) {
                                        attributeValue = cell.getNumericCellValue() + "";
                                    }
                                }
                            }
                            // after processing each cell push it into the record.
                            record.put(columnDefinition.getDestination(), attributeValue);
                        }
                        // after processing each row, push it into the result.
                        // process Row if you want to modify it before addition to result.
                        // if record is empty skip it
                        Boolean containsNullValue = true;
                        for (Object val : record.values()) {
                            if (val != null) {
                                containsNullValue = false;
                                break; // must break, since atleast 1 cell has value, plz dont remove this break. added by Navin.
                            } else {
                                containsNullValue = true;
                            }
                        }

                        if (!containsNullValue) {
                            try {
                                processRowAfterExtraction(record, (rowsToSkip + i));
                            } catch (Exception e) {
                                logger.warn("Exception thrown when processRowAfterExtraction row :="
                                        + (rowsToSkip + i));
                                throw e;
                            }

                            result.add(record);
                        } else {
                            logger.info((rowsToSkip + i + 1)
                                    + " is considered as an empty row as all values are null.");
                        }


                    } catch (NullPointerException npe) {
                        logger.warn("Unable to migrate row due to null value :=> "
                                + (rowsToSkip + i) + " message:=" + npe.getMessage());
                    } catch (Exception e) {
                        if (e.getCause() != null && e.getCause() instanceof NullPointerException) {
                            logger.warn("Unable to migrate row due to null value :=> "
                                    + (rowsToSkip + i) + " message:=" + e.getMessage());
                        }
                        logger.warn("Unable to migrate row :=> " + (rowsToSkip + i) + " message:=" + e.getMessage());

                    }
                }
            }

        } catch (Exception e) {
            logger.error("Unable to read the spreadsheet :=" + tableDefinition.getSourceTable(), e);
        }
    }

    @Override
    public void processRowAfterExtraction(Map<String, Object> record, Integer recordIndex) {
    }
}
