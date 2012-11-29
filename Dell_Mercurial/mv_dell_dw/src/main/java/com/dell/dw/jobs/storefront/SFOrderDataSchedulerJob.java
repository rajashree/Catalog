package com.dell.dw.jobs.storefront;

import com.dell.dw.managers.SFOrderDataSchedulerManager;
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
public class SFOrderDataSchedulerJob extends AbstractJob {

    // download the csv from File System and process it (update the DataSchedulerBatch table).
    @Override
    protected void executeJob(JobExecutionContext context) {
         try {
            if (configurationService.getBooleanProperty(SFOrderDataSchedulerManager.class, "downloadFeeds", true)) {
                if (!sfOrderDataSchedulerManager.isDownloadingFiles()) {
                     sfOrderDataSchedulerManager.downloadDataFiles();
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Autowired
    SFOrderDataSchedulerManager sfOrderDataSchedulerManager;

    public SFOrderDataSchedulerManager getSfOrderDataSchedulerManager() {
        return sfOrderDataSchedulerManager;
    }

    public void setSfOrderDataSchedulerManager(SFOrderDataSchedulerManager sfOrderDataSchedulerManager) {
        this.sfOrderDataSchedulerManager = sfOrderDataSchedulerManager;
    }
}
