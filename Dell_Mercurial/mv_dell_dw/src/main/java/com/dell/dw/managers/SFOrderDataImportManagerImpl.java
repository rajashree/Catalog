package com.dell.dw.managers;

import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.dell.dw.persistence.domain.DataSource;
import com.dell.dw.persistence.repository.DataSchedulerBatchRepository;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.DateUtils;
import com.sourcen.core.util.StringUtils;
import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.Schema;
import com.sourcen.dataimport.definition.TableDefinition;
import com.sourcen.dataimport.service.DataReader;
import com.sourcen.dataimport.service.DataWriter;
import com.sourcen.dataimport.service.errors.DataExceptionHandler;
import com.sourcen.dataimport.service.support.GenericDataImportService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "sfOrderDataImportManagerImpl")
public class SFOrderDataImportManagerImpl implements SFOrderDataImportManager {
    private static final Logger logger = LoggerFactory.getLogger(SFOrderDataImportManagerImpl.class);

    @Override
    @Transactional(readOnly = false)
    public List<DataSchedulerBatch> getUnprocessedBatches() {
        List<DataSchedulerBatch> list = dataSchedulerBatchRepository.getUnprocessedBatches(DataSource.DSConstants.SF_ORDER);
        return list;
    }

    @Override
    public void importData(DataSchedulerBatch record) {
        DataSchedulerBatch lockedBatch = record;
        if(!record.getStatus().equals(DataSchedulerBatch.Status.PROCESSING)) {
            lockedBatch = dataSchedulerBatchRepository.acquireLock(lockedBatch, lockedBatch.getStatus(),
                    DataSchedulerBatch.Status.PROCESSING);
        }
        String loggerName = "com.dell.dw.dataimport."+ StringUtils
                .getSimpleString(lockedBatch.getFilePath().replace("csv", ""))+ DateUtils.TIMESTAMP_DATEFORMAT.format(lockedBatch.getStartDate()).replaceAll(":", "_") ;
        Logger dataSchedulerBatchLogger = LoggerFactory.getLogger(loggerName);

        //dataSchedulerBatchRepository.refresh(lockedBatch);
        dataSchedulerBatchLogger.info("starting to process dataFile for retailerSite :=" + lockedBatch.getReferenceId()
                + " SchedulerBatch:=" + lockedBatch);
        Integer finalStatus = DataSchedulerBatch.Status.DONE;
        try {

            File fileSchema = configurationService.getFileSystem().getFile("/config/storefront/data_import_config.xml", false, false);
            if(!fileSchema.exists() || !fileSchema.canRead()) {
                throw  new RuntimeException("Unable to access data import schema");
            }

            // Import/Read data import schema from data_import_config.xml file
            DataImportConfig dataImportConfig = applicationContext.getBean("dataImportConfig", DataImportConfig.class);
            dataImportConfig.setConfigFilePath(fileSchema.getAbsolutePath());
            dataImportConfig.afterPropertiesSet();

            Schema schema = dataImportConfig.getSchema();
            String destinationTable = null;
            if(lockedBatch.getEndpoint().equals(DataSchedulerBatch.EndPoint.STORES)){
                destinationTable = "com.dell.dw.persistence.domain.Store";
            }else if(lockedBatch.getEndpoint().equals(DataSchedulerBatch.EndPoint.SF_ORDERS)){
                destinationTable = "com.dell.dw.persistence.domain.SFOrder";
            }else if(lockedBatch.getEndpoint().equals(DataSchedulerBatch.EndPoint.SF_ORDER_ITEMS)){
                destinationTable = "com.dell.dw.persistence.domain.SFOrderItem";
            }


            TableDefinition tableDefinition = schema.getDefinitionByDestination(destinationTable);
            Assert.notNull(tableDefinition, "cannot find tableDefinition for :="
                    + tableDefinition.getDestinationTable()
                    + " in schema :="
                    + fileSchema.getAbsolutePath());



            String csvFilePath = FilenameUtils.normalize(configurationService.getFileSystem().getFileSystemAsString()
                    + "/"
                    + lockedBatch.getFilePath());
            tableDefinition.setProperty("path", csvFilePath);
            tableDefinition.setProperty("relativePath", lockedBatch.getFilePath());
            tableDefinition.setProperty("startDate",lockedBatch.getStartDate().toString());
            tableDefinition.setProperty("isPathAbsolute", "true");

            DataReader dataReader =  applicationContext.getBean("sfOrderDataReader", DataReader.class);
            DataWriter dataWriter = applicationContext.getBean("hibernateDataWriter", DataWriter.class);

            DataExceptionHandler exceptionHandler = null;
            if (applicationContext.containsBean(tableDefinition.getSourceTable() + "DataExceptionHandler")) {
                exceptionHandler = (DataExceptionHandler) applicationContext
                        .getBean(tableDefinition.getSourceTable() + "DataExceptionHandler");
                exceptionHandler.setLogger(dataSchedulerBatchLogger);
                dataReader.setExceptionHandler(exceptionHandler);
                dataWriter.setExceptionHandler(exceptionHandler);
            }


            dataReader.setTableDefinition(tableDefinition);
            dataWriter.setTableDefinition(tableDefinition);

            dataSchedulerBatchLogger.info("Importing data from the feed file " + lockedBatch.getFilePath());
            GenericDataImportService dataImportService = applicationContext.getBean("genericDataImportService",
                    GenericDataImportService.class);
            dataImportService.setLogger(dataSchedulerBatchLogger);
            dataImportService.setDataImportConfig(dataImportConfig);
            dataImportService.setDataReader(dataReader);
            dataImportService.setDataWriter(dataWriter);
            dataReader.setLogger(dataSchedulerBatchLogger);
            dataWriter.setLogger(dataSchedulerBatchLogger);
            dataReader.initialize();
            dataWriter.initialize();
            dataImportService.setExceptionHandler(exceptionHandler);
            dataImportService.run();
            if (exceptionHandler.getReaderFailedCount() > 0) {
                finalStatus = DataSchedulerBatch.Status.ERROR_READ;
            }
            if (exceptionHandler.getWriterFailedCount() > 0) {
                finalStatus = DataSchedulerBatch.Status.ERROR_WRITE;
            }

        }catch (StackOverflowError se) {
            logger.error(se.getMessage());
            finalStatus = DataSchedulerBatch.Status.ERROR_PARSING;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            finalStatus = DataSchedulerBatch.Status.ERROR_PARSING;
            if (e.getCause() != null) {
                Class errorClass = e.getCause().getClass();
                if (errorClass.equals(FileNotFoundException.class) || errorClass.equals(IOException.class)) {
                    finalStatus = DataSchedulerBatch.Status.ERROR_READ;
                    dataSchedulerBatchLogger.error(e.getMessage());
                } else {
                    dataSchedulerBatchLogger.error(e.getMessage(), e);
                }
            } else {
                dataSchedulerBatchLogger.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            finalStatus = DataSchedulerBatch.Status.ERROR_PARSING;
            dataSchedulerBatchLogger.error(e.getMessage(), e);
        } finally {
            // Update processed date
            dataSchedulerBatchRepository.updateLastProcessedDate(record.getId());
            dataSchedulerBatchRepository.acquireLock(lockedBatch, lockedBatch.getStatus(), finalStatus);
        }
    }

    @Autowired
    DataSchedulerBatchRepository dataSchedulerBatchRepository;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    ApplicationContext applicationContext;

    public DataSchedulerBatchRepository getDataSchedulerBatchRepository() {
        return dataSchedulerBatchRepository;
    }

    public void setDataSchedulerBatchRepository(DataSchedulerBatchRepository dataSchedulerBatchRepository) {
        this.dataSchedulerBatchRepository = dataSchedulerBatchRepository;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}

