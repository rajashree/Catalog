/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.jobs;

import com.dell.acs.managers.DataFilesDownloadManager;
import com.dell.acs.managers.DataImportManager;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sandeep M Heggi.
 * @author $LastChangedBy: shawn $
 * @version $Revision: 3430 $, $Date:: 2012-06-20 14:11:55#$
 */
public final class ImageProcessJob extends AbstractJob {

    @Autowired
    protected DataImportManager dataImportManager;

    @Override
    protected void executeJob(final JobExecutionContext context) {
    	CountStatMutator count = null;
    	try {
        	count = (CountStatMutator)StatUtil.getInstance().getStat(CountStat.class, "ImageProcessJob.Count");
        	count.inc();
        	count.apply();
            if (!dataImportManager.isProcessingImages()) {
                DataFile dataFile = dataImportManager.getLatestImportedImageFile();
                if (dataFile != null) {
                    dataImportManager.processImages(dataFile);
                }
            } else {
                logger.info("Image process job is already in progress");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
    	} finally {
        	if (count != null) {
        		count.clear();
        		count.dec();
        		count.apply();
        	}
        }
    }

    public void setDataImportManager(final DataImportManager dataImportManager) {
        this.dataImportManager = dataImportManager;
    }
}
