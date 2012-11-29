package com.dell.acs.jobs;

import com.dell.acs.managers.DataExportManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: shawn $
 * @version $Revision: 3430 $, $Date:: 2012-06-20 14:11:55#$
 */
public class DataExportJob extends AbstractJob {

    private static final AtomicBoolean executing = new AtomicBoolean(false);


    @Override
    protected void executeJob(final JobExecutionContext context) {
    	CountStatMutator count = null;
    	try {
        	count = (CountStatMutator)StatUtil.getInstance().getStat(CountStat.class, "DataExportJob.Count");
        	count.inc();
        	count.apply();
            // if (executing.compareAndSet(false, true)) {
            Collection<Retailer> retailers = retailerManager.getRetailers();
            logger.info("Total No of retailers found :::  " + retailers.size());
            for (Retailer retailer : retailers) {
                for (RetailerSite retailerSite : retailerManager.getRetailerSites(retailer)) {
                    dataExportManager.executeJobForRetailerSite(retailerSite);
                }
            }

            /*  } else {
                logger.info(" \n\n DataExportJob is currently executing.");
            }*/
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // executing.set(false);
    	} finally {
        	if (count != null) {
        		count.clear();
        		count.dec();
        		count.apply();
        	}
        }
    }

    /**
     * DataImportManager bean injection.
     */
    @Autowired
    protected RetailerManager retailerManager;

    @Autowired
    protected DataExportManager dataExportManager;

    public void setDataExportManager(final DataExportManager dataExportManager) {
        this.dataExportManager = dataExportManager;
    }

    public void setRetailerManager(final RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }

}
