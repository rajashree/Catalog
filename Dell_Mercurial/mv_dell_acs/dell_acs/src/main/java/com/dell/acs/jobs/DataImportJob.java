/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.jobs;

import com.dell.acs.managers.DataImportManager;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.jobs.AbstractJob;
import com.sourcen.core.config.ConfigurationServiceImpl;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: shawn $
 * @version $Revision: 3430 $, $Date:: 2012-06-20 14:11:55#$
 */
public final class DataImportJob extends AbstractJob {

    private static volatile int currentDataImportExecutors = 0;
    private static volatile int maxExecutors = 8;

    static {
        maxExecutors = ConfigurationServiceImpl.getInstance().getIntegerProperty(DataImportJob.class,
                "maxExecutors", 8);
    }

    /**
     * @param context
     */
    @Override
    protected void executeJob(final JobExecutionContext context) {
    	CountStatMutator count = null;
    	try {
        	count = (CountStatMutator)StatUtil.getInstance().getStat(CountStat.class, "DataImportJob.Count");
        	count.inc();
        	count.apply();
            if (currentDataImportExecutors < maxExecutors) {
                currentDataImportExecutors++;
                try {
                    DataFile dataFile = dataImportManager.getLatestImportedFile();
                    if (dataFile != null) {
                        dataImportManager.processImportedFile(dataFile);
                    }
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
                currentDataImportExecutors--;
            } else {
                logger.warn("reached max level of threads := " + maxExecutors + " will check on next Job execution.");
            }
    	} finally {
        	if (count != null) {
        		count.clear();
        		count.dec();
        		count.apply();
        	}
    	}
    }

    /**
     * properteis and corresponding setter() for value injection
     */
    @Autowired
    protected DataImportManager dataImportManager;

    public void setDataImportManager(final DataImportManager dataImportManager) {
        this.dataImportManager = dataImportManager;
    }
}
