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
import com.sourcen.core.util.DateUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 @author Adarsh Kumar.
 @author $LastChangedBy: adarsh $
 @version $Revision: 3849 $, $Date:: 2012-07-05 09:54:36#$ */

public final class DataImportCleanUpJob extends AbstractJob {

    private static final Logger logger = Logger.getLogger(DataImportCleanUpJob.class);

    @Override
    protected void executeJob(final JobExecutionContext context) {
    	CountStatMutator count = null;
    	try {
        	count = (CountStatMutator)StatUtil.getInstance().getStat(CountStat.class, "DataImportCleanUpJob.Count");
        	count.inc();
        	count.apply();
	        final String jvmId = DateUtils.JVM_START_TIME_UTC.toString();
	        final Collection<DataFile> dataFileList = dataImportManager.getAllDataFilesInProcessingStatus();
	        if (dataFileList.size() > 0) {
	            for (DataFile dataFile : dataFileList) {
	                String jvmStartTime = dataFile.getLockedThread();
	                if (jvmStartTime != null) {
	                    jvmStartTime = jvmStartTime.substring(0, jvmStartTime.indexOf("-"));
	                    if (!jvmId.equalsIgnoreCase(jvmStartTime)) {
	                        //update the datafile status
	                        if (dataFile.getImportType().equals("images")) {
	                            dataFile.setStatus(DataImportManager.FileStatus.IMAGES_IMPORTED);
	                            logger.warn("Setting the data file - " + dataFile.getFilePath() + " status to 6(IN_QUEUE)");
	                            dataImportManager.update(dataFile);
	                        } else {
	                            dataFile.setStatus(DataImportManager.FileStatus.DONE);
	                            logger.warn("Setting the data file - " + dataFile.getFilePath() + " status to 0(IN_QUEUE)");
	                            dataImportManager.update(dataFile);
	                        }
	                    }
	                }
	            }
	        }
    	} finally {
        	if (count != null) {
        		count.clear();
        		count.dec();
        		count.apply();
        	}
    	}
    }

    @Autowired
    private DataImportManager dataImportManager;

    public void setDataImportManager(final DataImportManager dataImportManager) {
        this.dataImportManager = dataImportManager;
    }
}
