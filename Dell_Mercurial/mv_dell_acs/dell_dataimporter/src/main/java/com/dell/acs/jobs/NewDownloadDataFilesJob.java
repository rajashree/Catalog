/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.jobs;

import com.dell.acs.managers.DataFilesDownloadCache;
import com.dell.acs.managers.DataFilesDownloadManager;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;


/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public final class NewDownloadDataFilesJob extends AbstractJob {
	public static final String STAT_NAME = "DownloadDataFilesJob.Count";
	public static final String JOB_ENABLED_KEY = ".enabled";
	public static final String STATUS_COMPLETED = "COMPLETED";
	public static final String STATUS_ALREADY_DOWNLOADED = "ALREADY_DOWNLOADED";
	public static final String STATUS_DISABLED = "DISABLED";
	
    /**
     {@inheritDoc}
     */
    @Override
    protected void executeJob(final JobExecutionContext context) {
    	CountStatMutator count = null;
        try {
        	count = (CountStatMutator)StatUtil.getInstance().getStat(CountStat.class, NewDownloadDataFilesJob.STAT_NAME);
        	count.inc();
        	count.apply();
            if (configurationService
                    .getBooleanProperty(NewDownloadDataFilesJob.class, dataFilesDownloadManager.getProviderName() + NewDownloadDataFilesJob.JOB_ENABLED_KEY, true)) {
                if (!dataFilesDownloadManager.isDownloadingFiles()) {
                    dataFilesDownloadManager.downloadDataFiles();
                    context.getJobDetail().getJobDataMap().put(NewDownloadDataFilesJob.STATUS_KEY, NewDownloadDataFilesJob.STATUS_COMPLETED);
                    if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 23) {
                        DataFilesDownloadCache.clearDownloadFile();
                    }
                } else {
                    context.getJobDetail().getJobDataMap().put(NewDownloadDataFilesJob.STATUS_KEY, NewDownloadDataFilesJob.STATUS_ALREADY_DOWNLOADED);
                    logger.info("Files are already being downloaded for := " + dataFilesDownloadManager.getProviderName().toUpperCase());
                }
            } else {
                context.getJobDetail().getJobDataMap().put(NewDownloadDataFilesJob.STATUS_KEY, NewDownloadDataFilesJob.STATUS_DISABLED);
                logger.info("Download for " + dataFilesDownloadManager.getProviderName().toUpperCase() + " has been disabled");
            }

            context.setResult(true);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            context.setResult(e);
        } finally {
        	if (count != null) {
        		count.clear();
        		count.dec();
        		count.apply();
        	}
        }
    }

    /**
     DataFilesDownloadManager bean injection.
     */

    @Autowired
    protected DataFilesDownloadManager dataFilesDownloadManager;

    public void setDataFilesDownloadManager(final DataFilesDownloadManager dataFilesDownloadManager) {
        this.dataFilesDownloadManager = dataFilesDownloadManager;
    }
}
