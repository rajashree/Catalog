/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.jobs;

import com.dell.acs.managers.CurationSourceManager;
import com.dell.acs.persistence.domain.CurationSource;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: shawn $
 * @version $Revision: 3430 $, $Date:: 2012-06-20 14:11:55#$
 */
public final class CurationSourceCacheImportJob extends AbstractJob {

    private static volatile int currentDataImportExecutors = 0;

    private static volatile int maxExecutors = 8;

    static {
        maxExecutors = ConfigurationServiceImpl.getInstance()
                                               .getIntegerProperty(CurationSourceCacheImportJob.class, "maxExecutors",
                                                       8);
    }

    /** @param context  */
    @Override
    protected void executeJob(final JobExecutionContext context) {
        CountStatMutator count = null;
        try {
            count = (CountStatMutator) StatUtil.getInstance()
                                               .getStat(CountStat.class, "CurationSourceCacheImportJob.Count");
            count.inc();
            count.apply();

            if (currentDataImportExecutors < maxExecutors) {
                currentDataImportExecutors++;
                try {
                    CurationSource curationSource = curationSourceManager.getSourceToUpdateCache();
                    if (curationSource != null) {
                        curationSourceManager.updateCache(curationSource);
                    }
                } catch (Throwable e) {
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

    //
    //      IoC
    //

    /** IoC reference */
    @Autowired
    protected CurationSourceManager curationSourceManager;

    /** IoC reference */
    public void setCurationSourceManager(final CurationSourceManager curationSourceManager) {
        this.curationSourceManager = curationSourceManager;
    }
}
