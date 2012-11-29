package com.dell.dw.jobs.d3;


import com.dell.dw.managers.D3DataSchedulerManager;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: rajashreem $
 * @version $Revision: 2808 $, $Date:: 2012-06-01 14:31:00#$
 */
public class D3DataSchedulerJob extends AbstractJob {

    // download the csv from SFTP and process it (update the DataSchedulerBatch table).
    @Override
    protected void executeJob(JobExecutionContext context) {
         try {
            if (configurationService.getBooleanProperty(D3DataSchedulerManager.class, "downloadFeeds", true)) {
                if (!d3DataSchedulerManager.isDownloadingFiles()) {
                     d3DataSchedulerManager.downloadDataFiles();
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Autowired
    D3DataSchedulerManager d3DataSchedulerManager;

    public D3DataSchedulerManager getD3DataSchedulerManager() {
        return d3DataSchedulerManager;
    }

    public void setD3DataSchedulerManager(D3DataSchedulerManager d3DataSchedulerManager) {
        this.d3DataSchedulerManager = d3DataSchedulerManager;
    }
}
