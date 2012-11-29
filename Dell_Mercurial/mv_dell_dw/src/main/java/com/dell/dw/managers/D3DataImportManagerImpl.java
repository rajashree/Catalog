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
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/30/12
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class D3DataImportManagerImpl implements D3DataImportManager {
    private static final Logger logger = LoggerFactory.getLogger(D3DataImportManagerImpl.class);

    @Override
    @Transactional(readOnly = false)
    public List<DataSchedulerBatch> getUnprocessedBatches() {
        List<DataSchedulerBatch> list = dataSchedulerBatchRepository.getUnprocessedBatches(DataSource.DSConstants.D3);
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

            File fileSchema = configurationService.getFileSystem().getFile("/config/d3/data_import_config.xml", false, false);
            if(!fileSchema.exists() || !fileSchema.canRead()) {
                throw  new RuntimeException("Unable to access data import schema");
            }

            // Import/Read data import schema from data_import_config.xml file
            DataImportConfig dataImportConfig = applicationContext.getBean("dataImportConfig", DataImportConfig.class);
            dataImportConfig.setConfigFilePath(fileSchema.getAbsolutePath());
            dataImportConfig.afterPropertiesSet();

            Schema schema = dataImportConfig.getSchema();

            String destinationTable = lockedBatch.getEndpoint().equals(DataSchedulerBatch.EndPoint.D3_LINKTRACKER_METRICS)
                    ?"com.dell.dw.persistence.domain.D3LinkTrackerMetrics"
                    :"com.dell.dw.persistence.domain.D3RevenueMetrics";


            TableDefinition tableDefinition = schema.getDefinitionByDestination(destinationTable);
            Assert.notNull(tableDefinition, "cannot find tableDefinition for :="
                    + tableDefinition.getDestinationTable()
                    + " in schema :="
                    + fileSchema.getAbsolutePath());



            String csvFilePath = FilenameUtils.normalize(configurationService.getFileSystem().getFileSystemAsString()
                    + "/"
                    + lockedBatch.getFilePath());
            tableDefinition.setProperty("path", csvFilePath);
            tableDefinition.setProperty("isPathAbsolute", "true");
            tableDefinition.setProperty("relativePath", lockedBatch.getFilePath());
            tableDefinition.setProperty("startDate",lockedBatch.getStartDate().toString());

            DataReader dataReader =  applicationContext.getBean("d3DataReader", DataReader.class);
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
            dataImportService.run();

        } catch (StackOverflowError se) {
            logger.warn("Unable to process batch");
            finalStatus = DataSchedulerBatch.Status.ERROR_PARSING;
        } catch (RuntimeException e) {
            logger.warn("Unable to process batch");
            finalStatus = DataSchedulerBatch.Status.ERROR_PARSING;
        } catch (Exception e) {
            logger.warn("Unable to process batch");
            finalStatus = DataSchedulerBatch.Status.ERROR_PARSING;
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

