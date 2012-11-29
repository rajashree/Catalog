package com.dell.dw.managers;

import com.dell.dw.managers.dataimport.util.DateUtils;
import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.dell.dw.persistence.domain.DataSource;
import com.dell.dw.persistence.repository.DataSchedulerBatchRepository;
import com.dell.dw.persistence.repository.GAWebPropertyProfileRepository;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.Schema;
import com.sourcen.dataimport.definition.TableDefinition;
import com.sourcen.dataimport.service.DataReader;
import com.sourcen.dataimport.service.DataWriter;
import com.sourcen.dataimport.service.support.GenericDataImportService;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
@Service
public class GADataImportManagerImpl implements GADataImportManager {
    private static final Logger logger = LoggerFactory.getLogger(GADataImportManagerImpl.class);
    private FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");

    /**
     * Get Unprocessed Google Analytics batches to import data
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public List<DataSchedulerBatch> getUnprocessedGABatches() {
        List<DataSchedulerBatch> list = dataSchedulerBatchRepository.getUnprocessedBatches(DataSource.DSConstants.GA);
        return list;
    }

    /**
     *  Entry to import Google Analytics Events/Page Views for given profile
     * @param record
     */
    @Override
    public void importData(final DataSchedulerBatch record) {
        DataSchedulerBatch lockedBatch = record;
        if(!record.getStatus().equals(DataSchedulerBatch.Status.PROCESSING)) {
            lockedBatch = dataSchedulerBatchRepository.acquireLock(lockedBatch, lockedBatch.getStatus(),
                    DataSchedulerBatch.Status.PROCESSING);
            if(lockedBatch == null) {
                logger.info("Unable to lock object from IN_QUEUE TO PROCESSING  record:="
                        + record + " as it was locked by " + record.getLockedThread());
            }
        }
        if(lockedBatch != null){

            String loggerName = "com.dell.dw.dataimport./"
                    + lockedBatch.getFilePath()+"_"+ com.sourcen.core.util.DateUtils.TIMESTAMP_DATEFORMAT.format(lockedBatch.getStartDate()).replaceAll(":", "_")  ;
            Logger dataSchedulerBatchLogger = LoggerFactory.getLogger(loggerName);

            //dataSchedulerBatchRepository.refresh(lockedBatch);
            dataSchedulerBatchLogger.info("Starting to process batch for Profile:=" + lockedBatch.getReferenceId()
                    + " SchedulerBatch:=" + lockedBatch);

            Integer finalStatus = DataSchedulerBatch.Status.DONE;
            try {
                // Load data import schema to transform and insert data in to db
                File fileSchema = configurationService.getFileSystem().getFile("/config/ga/data_import_config.xml", false, false);
                if(!fileSchema.exists() || !fileSchema.canRead()) {
                    throw  new RuntimeException("Unable to access data import schema for Google Analytics "
                            + (lockedBatch.getEndpoint().equals(DataSchedulerBatch.EndPoint.GA_PAGE_VIEWS)?"Page Views":"Events"));
                }

                // Import/Read data import schema from data_import_config.xml file
                DataImportConfig dataImportConfig = applicationContext.getBean("dataImportConfig", DataImportConfig.class);
                dataImportConfig.setConfigFilePath(fileSchema.getAbsolutePath());
                dataImportConfig.afterPropertiesSet();

                Schema schema = dataImportConfig.getSchema();

                String destinationTable = lockedBatch.getEndpoint().equals(DataSchedulerBatch.EndPoint.GA_PAGE_VIEWS)
                        ?"com.dell.dw.persistence.domain.GAPageViewMetrics"
                        :"com.dell.dw.persistence.domain.GAEventMetrics";

                TableDefinition tableDefinition = schema.getDefinitionByDestination(destinationTable);
                Assert.notNull(tableDefinition, "cannot find tableDefinition for :="
                        + tableDefinition.getDestinationTable()
                        + " in schema :="
                        + fileSchema.getAbsolutePath());


                // Set required properties to pull events or page views from Google Analytics API
                tableDefinition.setProperty("endPoint", lockedBatch.getEndpoint());
                tableDefinition.setProperty("profileId", lockedBatch.getReferenceId().toString());
                tableDefinition.setProperty("startDate", DateUtils.getFormattedDate(lockedBatch.getStartDate(), "yyyy-MM-dd HH:mm:ss"));
                tableDefinition.setProperty("endDate", DateUtils.getFormattedDate(lockedBatch.getEndDate(),"yyyy-MM-dd HH:mm:ss"));
                tableDefinition.setProperty("startIndex", lockedBatch.getStartIndex().toString());
                tableDefinition.setProperty("maxResults", lockedBatch.getMaxResults().toString());

                // Initialize Google Analytics Data Reader

                DataReader dataReader = applicationContext.getBean("gaDataReader", DataReader.class);

                DataWriter dataWriter = applicationContext.getBean("hibernateDataWriter", DataWriter.class);

                dataReader.setTableDefinition(tableDefinition);
                dataWriter.setTableDefinition(tableDefinition);

                GenericDataImportService dataImportService = applicationContext.getBean("genericDataImportService",
                        GenericDataImportService.class);
                dataImportService.setLogger(dataSchedulerBatchLogger);
                dataImportService.setDataImportConfig(dataImportConfig);
                dataImportService.setDataReader(dataReader);
                dataImportService.setDataWriter(dataWriter);
                dataReader.setLogger(dataSchedulerBatchLogger);
                dataWriter.setLogger(dataSchedulerBatchLogger);
                logger.info("Initializing Google Analytics data reader....");
                dataReader.initialize();
                logger.info("Initializing Google Analytics data writer....");
                dataWriter.initialize();
                dataSchedulerBatchLogger.info("Starting data import process for Google Analytics Profile:" + lockedBatch.getReferenceId());
                dataImportService.run();

            } catch (StackOverflowError se) {
                logger.error(se.getMessage(), se);
                finalStatus = DataSchedulerBatch.Status.ERROR_READ;
            } catch (MalformedURLException me) {
                logger.error(me.getMessage(), me);
                finalStatus = DataSchedulerBatch.Status.ERROR_READ;
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                finalStatus = DataSchedulerBatch.Status.ERROR_READ;
            } catch (NullPointerException e) {
                logger.error(e.getMessage(), e);
                finalStatus = DataSchedulerBatch.Status.ERROR_READ;
            } catch (RuntimeException re) {
                logger.error(re.getMessage(), re);
                finalStatus = DataSchedulerBatch.Status.ERROR_READ;
            } finally {
                // Update processed date
                dataSchedulerBatchRepository.updateLastProcessedDate(record.getId());
                dataSchedulerBatchRepository.acquireLock(lockedBatch, lockedBatch.getStatus(), finalStatus);
            }
        }
    }



    @Autowired
    DataSchedulerBatchRepository dataSchedulerBatchRepository;

    @Autowired
    GAWebPropertyProfileRepository gaWebPropertyProfileRepository;

    @Autowired
    ConfigurationServiceImpl configurationService;

    @Autowired
    ApplicationContext applicationContext;

    public DataSchedulerBatchRepository getDataSchedulerBatchRepository() {
        return dataSchedulerBatchRepository;
    }

    public void setDataSchedulerBatchRepository(DataSchedulerBatchRepository dataSchedulerBatchRepository) {
        this.dataSchedulerBatchRepository = dataSchedulerBatchRepository;
    }

    public GAWebPropertyProfileRepository getGaWebPropertyProfileRepository() {
        return gaWebPropertyProfileRepository;
    }

    public void setGaWebPropertyProfileRepository(GAWebPropertyProfileRepository gaWebPropertyProfileRepository) {
        this.gaWebPropertyProfileRepository = gaWebPropertyProfileRepository;
    }

    public ConfigurationServiceImpl getconfigurationService() {
        return configurationService;
    }

    public void setconfigurationService(ConfigurationServiceImpl configurationService) {
        this.configurationService = configurationService;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}


