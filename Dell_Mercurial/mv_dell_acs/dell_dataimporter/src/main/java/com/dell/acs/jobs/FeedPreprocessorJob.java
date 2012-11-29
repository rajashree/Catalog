/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.jobs;

import java.util.Collection;
import java.util.Set;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.dell.acs.FeedUtil;
import com.dell.acs.managers.FeedPreprocessorManager;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.jobs.AbstractJob;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public final class FeedPreprocessorJob extends AbstractJob {

    public static final String STATUS_COMPLETED = "STATUS_COMPLETED";
	private static final String NULL_DATAFILE = "NULL_DATAFILE";
	private static final String ERROR = "ERROR";
	private static final String EXCEED_MAX_EXECUTORS = "EXCEED_MAX_EXECUTORS";
	
	private static volatile int currentDataImportExecutors = 0;
    private static volatile int maxExecutors = 8;

    static {
        maxExecutors = ConfigurationServiceImpl.getInstance().getIntegerProperty(FeedPreprocessorJob.class,
                "maxExecutors", Runtime.getRuntime().availableProcessors());
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
                	Set<String> retailerSiteNames = FeedUtil.getRetailerSiteRestriction(configurationService);
        			Collection<Long> retailerSiteIds = retailerSiteRepository.getByNameIds(retailerSiteNames);
                    DataFile dataFile = feedPreprocessorManager.getLatestPreprocessFile(retailerSiteIds);
                    if (dataFile != null) {
                    	feedPreprocessorManager.preprocessDataFile(dataFile);
                    	context.getJobDetail().getJobDataMap().put(FeedPreprocessorJob.STATUS_KEY, FeedPreprocessorJob.STATUS_COMPLETED);
                    } else {
                    	context.getJobDetail().getJobDataMap().put(FeedPreprocessorJob.STATUS_KEY, FeedPreprocessorJob.NULL_DATAFILE);
                    }
                } catch (Exception e) {
                    logger.warn(e.getMessage() != null ? e.getMessage() : "Null pointer exception", e);
                	context.getJobDetail().getJobDataMap().put(FeedPreprocessorJob.STATUS_KEY, FeedPreprocessorJob.ERROR);
				} finally {
					currentDataImportExecutors--;
				}
            } else {
                logger.warn("reached max level of threads := " + maxExecutors + " will check on next Job execution.");
            	context.getJobDetail().getJobDataMap().put(FeedPreprocessorJob.STATUS_KEY, FeedPreprocessorJob.EXCEED_MAX_EXECUTORS);
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
     * properties and corresponding setter() for value injection
     */
    @Autowired
    protected FeedPreprocessorManager feedPreprocessorManager;

    public void setFeedPreprocessorManager(final FeedPreprocessorManager feedPreprocessorManager) {
        this.feedPreprocessorManager = feedPreprocessorManager;
    }
    
    /**
     * RetailerSiteRepository bean injection.
     */
    @Autowired
    private RetailerSiteRepository retailerSiteRepository;

    /**
     * Setter for retailerSiteRepository
     */
    public void setRetailerSiteRepository(final RetailerSiteRepository retailerSiteRepository) {
        this.retailerSiteRepository = retailerSiteRepository;
    }
}
