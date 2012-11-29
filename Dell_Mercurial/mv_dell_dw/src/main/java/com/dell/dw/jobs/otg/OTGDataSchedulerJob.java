package com.dell.dw.jobs.otg;

import com.dell.dw.managers.OTGDataSchedulerManager;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class OTGDataSchedulerJob extends AbstractJob {

    // download the csv from File System and process it (update the DataSchedulerBatch table).
    @Override
    protected void executeJob(JobExecutionContext context) {
         try {
            if (configurationService.getBooleanProperty(OTGDataSchedulerManager.class, "downloadFeeds", true)) {
                if (!otgDataSchedulerManager.isDownloadingFiles()) {
                     otgDataSchedulerManager.downloadDataFiles();
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Autowired
    OTGDataSchedulerManager otgDataSchedulerManager;

    public OTGDataSchedulerManager getOtgDataSchedulerManager() {
        return otgDataSchedulerManager;
    }

    public void setOtgDataSchedulerManager(OTGDataSchedulerManager otgDataSchedulerManager) {
        this.otgDataSchedulerManager = otgDataSchedulerManager;
    }
}
