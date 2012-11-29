package com.dell.dw.managers.dataimport.service;

import au.com.bytecode.opencsv.CSVReader;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.StringUtils;
import com.sourcen.dataimport.service.DataReader;
import com.sourcen.dataimport.service.errors.DataReaderException;
import com.sourcen.dataimport.service.errors.DataWriterException;
import com.sourcen.dataimport.service.support.BaseDataAdapter;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class OTGDataReader extends BaseDataAdapter implements DataReader {

    /**
     * default constructor.
     */
    public OTGDataReader() {
    }

    /**
     * getRows() helps in fetching the csv file data
     *
     * @return Collection<Map<String, Object>> having the data of the csv each row in the csv file is representing by
     *         java.util.Map<String,Object> in key value pair and store in the Collection object
     */
    @Override
    public Collection<Map<String, Object>> getRows() {
        String tableType = tableDefinition.getProperty("tableType");
        String filePath = tableDefinition.getProperty("path");
        Integer rowsToSkip = tableDefinition.getIntegerProperty("rowsToSkip", 0);
        Integer maxRows = tableDefinition.getIntegerProperty("maxRows", 10000);
        try {

            if (tableType == null || !(tableType.equalsIgnoreCase("txt")
                    || tableType.equalsIgnoreCase("csv") || tableType.equalsIgnoreCase("tab"))) {
                throw new RuntimeException("Unable to migrate table:=" + tableDefinition.getDestinationTable()
                        + " as tableType is invalid");
            }
            Assert.notNull(filePath, "path cannot be null.");

        } catch (RuntimeException e) {
            exceptionHandler.onTableDefinitionException(e);
        }
        logger.info("skipping rows :=" + rowsToSkip);
        logger.info("will process maximum of rows :=" + maxRows);


        CSVReader reader;
        FileInputStream inputStream = null;
        File csvFile = null;
        Collection<Map<String, Object>> result = new ArrayList<Map<String, Object>>(0);
        try {
            String encoding = "UTF-8";
            if (tableDefinition.getBooleanProperty("isPathAbsolute", false)
                    || tableDefinition.getBooleanProperty("isAbsolutePath", false)) {
                csvFile = new File(filePath);
            } else {
                csvFile = new File(getClass().getResource(filePath).toURI());
            }
            if (!csvFile.canRead()) {
                exceptionHandler.onTableDefinitionException(
                        new FileNotFoundException("Unable to read from file :=" + filePath));
            }

            if(tableDefinition.getProperty("encoding") == null){
                try {
                    encoding = FileUtils.getCharacterEncoding(csvFile);
                } catch (UnsupportedEncodingException e) {
                    logger.error("Unable to read the encoding. using default encoding UTF-8");
                    encoding = "UTF-8";
                }
            }  else{
                encoding = tableDefinition.getProperty("encoding");
            }
            logger.info("using "+encoding +" to read file :=" + filePath);



            // calculate an appx size of the rows, assuming that each row is appx 2kb of data.
            // using linked list to minimize the use of System.arraycopy(); when we need to scale the number of records.
            //int appxSize = Double.valueOf(csvFile.length() / 1536).intValue();
            result = new LinkedList<Map<String, Object>>();

            //char separator = (char) Integer.valueOf(tableDefinition.getProperty("separator", "9")).intValue();
            //char quotechar = (char) Integer.valueOf(tableDefinition.getProperty("quotechar", "34")).intValue();
            inputStream = new FileInputStream(csvFile);
            //reader = new CSVReader(new InputStreamReader(inputStream, encoding), separator, quotechar);
            reader = new CSVReader(new InputStreamReader(inputStream, encoding), '|');

             /* String[] dates = reader.readNext();
            Set<String> dateSet = new HashSet<String>();
            for (int i = 0; i < dates.length; i++) {
                if(dates[i].trim().length() > 0)
                    dateSet.add(dates[i].trim());
            }*/
            String[] headers = reader.readNext();
            for (int i = 0; i < headers.length; i++) {
                headers[i] = StringUtils.getSimpleString(headers[i].trim()).intern();
            }
            if (headers == null) {
                logger.warn("header row is null");
            } else {
                Collection<String> sourceColumns = tableDefinition.getSourceColumnNames(true);
                boolean headerFound = false;
                for (String headerName : headers) {
                    for (String sourceColumn : sourceColumns) {
                        if (headerName.equalsIgnoreCase(sourceColumn)) {
                            headerFound = true;
                            break;
                        }
                    }
                }
                if (!headerFound) {
                    throw new RuntimeException("Unable to find headers in CSV");
                }
            }


            String[] row;
            int i = 0;

             Integer rowIndex = rowsToSkip + 1 + 1; // rowsToSkip + header + 1 as in CSV files start from 1
            int headersCount = headers.length;
            while ((row = reader.readNext()) != null) {
                try {
                    Map<String, Object> record = new HashMap<String, Object>(row.length);
                    for (i = 0; i < headersCount; i++) {
                        record.put(headers[i], row[i]);
                    }
                    result.add(record);

                    rowIndex++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    DataWriterException dataWriterException = new DataWriterException("BATCH_READ", tableDefinition);
                    dataWriterException
                            .set("e", e.getMessage())
                            .set("row", row)
                            .set("header", headers);
                    exceptionHandler.onDataWriterException(dataWriterException);

                    DataReaderException dataReaderException = new DataReaderException("Unable to process row:="
                            + rowIndex + " due to error reading column :=" + headers[i] + " columIndex:=" + i)
                            .set("columnIndex", i).set("row", row);
                    exceptionHandler.onDataReaderException(dataReaderException, false);
                    logger.error("Unable to process row :=" + rowIndex, e);
                } catch (Exception e) {
                    DataWriterException dataWriterException = new DataWriterException("BATCH_READ", tableDefinition);
                    dataWriterException
                            .set("e", e.getMessage())
                            .set("row", row)
                            .set("header", headers);
                    exceptionHandler.onDataWriterException(dataWriterException);
                    DataReaderException dataReaderException = new DataReaderException("Unable to process row:="
                            + rowIndex + " due to error reading column :=" + headers[i] + " columIndex:=" + i)
                            .set("columnIndex", i).set("row", row);
                    exceptionHandler.onDataReaderException(dataReaderException, false);
                    logger.error("Unable to process row :=" + rowIndex, e);
                }
            }

        } catch (FileNotFoundException e) {
            exceptionHandler.onDataReaderException(e, false);
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            exceptionHandler.onDataReaderException(e, false);
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.warn("unable to close CsvFile handle :=" + csvFile);
                logger.error(e.getMessage(), e);
            }
        }
        logger.info("Number of rows to be processed:= " + result.size());
        this.resultSize = result.size();
        return result;
    }

    protected int resultSize = 0;

    @Override
    public void processRowAfterExtraction(Map<String, Object> record, Integer recordIndex) {
    }
}
