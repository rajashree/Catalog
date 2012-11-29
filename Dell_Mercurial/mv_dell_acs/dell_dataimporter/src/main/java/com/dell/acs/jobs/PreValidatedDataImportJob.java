/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.jobs;

import java.util.Collection;
import java.util.Set;

import com.dell.acs.FeedUtil;
import com.dell.acs.managers.PreValidatedDataImportManager;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.jobs.AbstractJob;
import com.sourcen.core.config.ConfigurationServiceImpl;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public final class PreValidatedDataImportJob extends AbstractJob {

    private static final String STATUS_COMPLETED = "COMPLETED";
	private static final String ERROR = "ERROR";
	private static final String EXCEED_MAX_EXECUTORS = "EXCEED_MAX_EXECUTORS";
	private static volatile int currentDataImportExecutors = 0;
    private static volatile int maxExecutors = 8;

    static {
        maxExecutors = ConfigurationServiceImpl.getInstance().getIntegerProperty(PreValidatedDataImportJob.class,
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
                DataFile dataFile = null;
                try {
                	Set<String> retailerSiteNames = FeedUtil.getRetailerSiteRestriction(configurationService);
        			Collection<Long> retailerSiteIds = retailerSiteRepository.getByNameIds(retailerSiteNames);
                    boolean done = false;
                    while(!done) {
	                    dataFile = preValidatedDataImportManager.getLatestProductDataFile(retailerSiteIds);
	                    if (dataFile != null) {
	                    	//logger.info("start processing data file: " + dataFile.getFilePath());
	                    	preValidatedDataImportManager.processDataFile(dataFile);
	                    	//logger.info("end processing data file: " + dataFile.getFilePath());
	                    	context.getJobDetail().getJobDataMap().put(PreValidatedDataImportJob.STATUS_KEY, PreValidatedDataImportJob.STATUS_COMPLETED);
						} else {
							done = true;
							continue;
						}
                    }
                } catch (Exception e) {
                	if (dataFile != null) {
                		//logger.info("error processing data file: " + dataFile.getFilePath());
                	}
                    logger.warn(e.getMessage(), e);
                	context.getJobDetail().getJobDataMap().put(PreValidatedDataImportJob.STATUS_KEY, PreValidatedDataImportJob.ERROR);
                } finally {
                	currentDataImportExecutors--;
                }
            } else {
                logger.warn("reached max level of threads := " + maxExecutors + " will check on next Job execution.");
            	context.getJobDetail().getJobDataMap().put(PreValidatedDataImportJob.STATUS_KEY, PreValidatedDataImportJob.EXCEED_MAX_EXECUTORS);
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
     * propertie and corresponding setter() for value injection
     */
    @Autowired
    protected PreValidatedDataImportManager preValidatedDataImportManager;

    public void setPreValidatedDataImportManager(final PreValidatedDataImportManager pPreValidatedDataImportManager) {
        this.preValidatedDataImportManager = pPreValidatedDataImportManager;
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
