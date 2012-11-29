package com.dell.dw.jobs.otg;

import com.dell.dw.managers.OTGDataImportManager;
import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class OTGDataImportJob extends AbstractJob {

    private static volatile int currentDataImportExecutors = 0;
    private static volatile int maxExecutors = 2;


    static {
        maxExecutors = ConfigurationServiceImpl.getInstance().getIntegerProperty(OTGDataImportJob.class,
                "maxExecutors", 2);
    }

    /**
     * @param context
     */
    @Override
    protected void executeJob(final JobExecutionContext context) {
       if (currentDataImportExecutors < maxExecutors) {
            currentDataImportExecutors++;
            try {
                List<DataSchedulerBatch> dataSchedulerBatches =
                        otgDataImportManager.getUnprocessedBatches();
                if(dataSchedulerBatches != null) {
                    for(DataSchedulerBatch dataSchedulerBatch: dataSchedulerBatches) {
                        if(!dataSchedulerBatch.getStatus().equals(DataSchedulerBatch.Status.PROCESSING)) {
                            otgDataImportManager.importData(dataSchedulerBatch);
                        }
                    }
                } else {
                    logger.warn("No data found to import data");
                }
            } catch (RuntimeException e) {
                logger.warn(e.getMessage(), e);
            }
            currentDataImportExecutors--;
        } else {
            logger.warn("reached max level of threads := " + maxExecutors + " will check on next Job execution.");
        }
    }

    @Autowired
    OTGDataImportManager otgDataImportManager;

    public OTGDataImportManager getOtgDataImportManager() {
        return otgDataImportManager;
    }

    public void setOtgDataImportManager(OTGDataImportManager otgDataImportManager) {
        this.otgDataImportManager = otgDataImportManager;
    }
}
