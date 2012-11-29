package com.dell.dw.jobs.d3;

import com.dell.dw.managers.D3DataImportManager;
import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: rajashreem $
 * @version $Revision: 2808 $, $Date:: 2012-06-01 14:31:00#$
 */
public class D3DataImportJob extends AbstractJob {

    private static volatile int currentDataImportExecutors = 0;
    private static volatile int maxExecutors = 2;


    static {
        maxExecutors = ConfigurationServiceImpl.getInstance().getIntegerProperty(D3DataImportJob.class,
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
                        d3DataImportManager.getUnprocessedBatches();
                if(dataSchedulerBatches != null) {
                    for(DataSchedulerBatch dataSchedulerBatch: dataSchedulerBatches) {
                        if(!dataSchedulerBatch.getStatus().equals(DataSchedulerBatch.Status.PROCESSING)) {
                            d3DataImportManager.importData(dataSchedulerBatch);
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
    D3DataImportManager d3DataImportManager;

    public D3DataImportManager getD3DataImportManager() {
        return d3DataImportManager;
    }

    public void setD3DataImportManager(D3DataImportManager d3DataImportManager) {
        this.d3DataImportManager = d3DataImportManager;
    }
}
