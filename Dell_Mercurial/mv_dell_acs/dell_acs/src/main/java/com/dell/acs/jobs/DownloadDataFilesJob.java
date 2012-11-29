/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.jobs;

import com.dell.acs.managers.DataFilesDownloadCache;
import com.dell.acs.managers.DataFilesDownloadManager;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatManager;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.jobs.AbstractJob;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Calendar;


/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: shawn $
 @version $Revision: 3430 $, $Date:: 2012-06-20 14:11:55#$ */

public final class DownloadDataFilesJob extends AbstractJob {

    /**
     {@inheritDoc}
     */
    @Override
    protected void executeJob(final JobExecutionContext context) {
    	CountStatMutator count = null;
        try {
        	count = (CountStatMutator)StatUtil.getInstance().getStat(CountStat.class, "DownloadDataFilesJob.Count");
        	count.inc();
        	count.apply();
            if (configurationService.getBooleanProperty(DataFilesDownloadManager.class, "downloadFeeds", true)) {
                if (configurationService
                        .getBooleanProperty(DownloadDataFilesJob.class, providerName + ".enabled", true)) {
                    if (!dataFilesDownloadManager.isDownloadingFiles(providerName)) {
                        dataFilesDownloadManager.downloadDataFiles(providerName);
                        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 23) {
                        	DataFilesDownloadCache.clearDownloadFile();
                        }
                    } else {
                        logger.info("Files are already being downloaded for := " + providerName.toUpperCase());
                    }
                } else {
                    logger.info("Download for " + providerName.toUpperCase() + " has been disabled");
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
        	if (count != null) {
        		count.clear();
        		count.dec();
        		count.apply();
        	}
        }
    }

    private String providerName;

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(final String providerName) {
        this.providerName = providerName;
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
