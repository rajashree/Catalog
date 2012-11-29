package com.dell.acs.curation;

import au.com.bytecode.opencsv.CSVReader;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.*;
import java.util.*;

/**
 @author Adarsh kumar
 @author $LastChangedBy: Adarsh $
 @version $Revision: 2704 $, $Date:: 2012-09-04 10:23:47#$ */

public class CurationSourceReader {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public Collection<Map<String, Object>> getRows(File csvFile) {
        Assert.notNull(csvFile, "File cannot be null.");
        try {
            // csv , txt , tab.
            fileType = FilenameUtils.getExtension(csvFile.getName());
            if (fileType == null || !(fileType.equalsIgnoreCase("txt")
                    || fileType.equalsIgnoreCase("csv") || fileType.equalsIgnoreCase("tab"))) {
                throw new RuntimeException("Unable to Read the file ");
            } else {
                /* Setting the Delimeter for file */
                if (fileType.equalsIgnoreCase("csv")) {
                    separator = "44";
                }
                if (fileType.equalsIgnoreCase("tab")) {
                    separator = "9";
                }
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        logger.info("skipping rows :=" + rowsToSkip);

        CSVReader reader = null;
        FileInputStream inputStream = null;
        Collection<Map<String, Object>> result = new ArrayList<Map<String, Object>>(0);
        try {
            String encoding = "UTF-8";
            if (!csvFile.canRead()) {
                throw new FileNotFoundException("Unable to read from file :=" + csvFile.getName());
            }

            try {
                encoding = FileUtils.getCharacterEncoding(csvFile);
            } catch (UnsupportedEncodingException e) {
                logger.error("Unable to read the encoding. using default encoding UTF-8");
                encoding = "UTF-8";
            }
            logger.info("using " + encoding + " to read file :=" + csvFile.getName());
            result = new LinkedList<Map<String, Object>>();
            char separator = (char) Integer.valueOf(this.separator).intValue();
            inputStream = new FileInputStream(csvFile);
            reader = new CSVReader(new InputStreamReader(inputStream, encoding), separator);
            String[] headers = reader.readNext();
            if (headers == null) {
                logger.warn("header row is null");
            } else {
                for (int i = 0; i < headers.length; i++) {
                    headers[i] = StringUtils.getSimpleString(headers[i].trim()).intern();
                }
            }

            String[] row;
            int i = 0;
            /*  rowsToSkip + header + 1 as in CSV files start from 1 */
            Integer rowIndex = rowsToSkip + 1 + 1;
            int headersCount = headers.length;
            while ((row = reader.readNext()) != null) {
                try {
                    Map<String, Object> record = new HashMap<String, Object>(row.length);
                    for (i = 0; i < headersCount; i++) {
                        record.put(headers[i].trim(), row[i].trim());
                    }
                    result.add(record);
                    rowIndex++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    logger.error("Unable to Read row from file :=" + rowIndex);
                } catch (Exception e) {
                    logger.error("Unable to Read row from file :=" + rowIndex);
                }
            }

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
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
        return result;
    }

    @Autowired
    private ConfigurationService configurationService;

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }


    private String fileType = "csv";

    private Integer rowsToSkip = 0;

    private Integer maxRows = 1000;

    private String separator = "44";


    public void setRowsToSkip(Integer rowsToSkip) {
        this.rowsToSkip = rowsToSkip;
    }

    public void setMaxRows(Integer maxRows) {
        this.maxRows = maxRows;
    }
}
