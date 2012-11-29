package com.dell.dw.jobs.ga;

import com.dell.dw.managers.GADataImportManager;
import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public class GADataImportJob extends AbstractJob {

    private static volatile int currentDataImportExecutors = 0;
    private static volatile int maxExecutors = 1;


    static {
        maxExecutors = ConfigurationServiceImpl.getInstance().getIntegerProperty(GADataImportJob.class,
                "maxExecutors", 2);
    }

    /**
     * Executes Google Analytics Data import job
     * @param context
     */
    @Override
    protected void executeJob(final JobExecutionContext context) {
        if (currentDataImportExecutors < maxExecutors) {
            currentDataImportExecutors++;
            try {
                // Get unprocessed batches to import google analtics
                List<DataSchedulerBatch> dataSchedulerBatches =
                        gaDataImportManager.getUnprocessedGABatches();
                if(dataSchedulerBatches != null) {
                    for(DataSchedulerBatch dataSchedulerBatch: dataSchedulerBatches) {
                        if(!dataSchedulerBatch.getStatus().equals(DataSchedulerBatch.Status.PROCESSING)) {
                            logger.info("Initializing data import process for Profile:"+ dataSchedulerBatch.getReferenceId());
                            gaDataImportManager.importData(dataSchedulerBatch);
                        } else {
                            logger.info("Profile:" + dataSchedulerBatch.getReferenceId() + "is already processing......");
                        }
                    }
                } else {
                    logger.info("Google Analytics: No batches are found to process data import");
                }
            } catch (RuntimeException e) {
                logger.error(e.getMessage(), e);
            }
            currentDataImportExecutors--;
        } else {
            logger.warn("reached max level of threads := " + maxExecutors + " will check on next Job execution.");
    }
    }


    @Autowired
    GADataImportManager gaDataImportManager;


    public GADataImportManager getGaDataImportManager() {
        return gaDataImportManager;
    }

    public void setGaDataImportManager(GADataImportManager gaDataImportManager) {
        this.gaDataImportManager = gaDataImportManager;
    }
}
