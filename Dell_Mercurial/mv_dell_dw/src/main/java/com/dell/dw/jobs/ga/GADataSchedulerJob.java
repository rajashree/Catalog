package com.dell.dw.jobs.ga;


import com.dell.dw.managers.GADataSchedulerManager;
import com.dell.dw.persistence.domain.GAWebPropertyProfile;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public class GADataSchedulerJob extends AbstractJob {

    @Override
    protected void executeJob(final JobExecutionContext context) {
        try {
            //Obtain the GAWebPropertyProfiles that need to be processed and inserted as batches
            Collection<GAWebPropertyProfile> list = gaDataSchedulerManager.getUnprocessedGAProfiles();
            if(list != null) {
                for(GAWebPropertyProfile profile: list) {
                    if (!profile.getStatus().equals(GAWebPropertyProfile.Status.PROCESSING)) {
                        gaDataSchedulerManager.updateSchedulerBatches(profile);
                    } else {
                        logger.info("GA Profile:" + profile.getId() + " already processing....");
                    }
                }
            } else {
                logger.warn("No unprocessed GA profiles found to update SchedulerBatches");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Autowired
    private GADataSchedulerManager gaDataSchedulerManager;

    public GADataSchedulerManager getGaDataSchedulerManager() {
        return gaDataSchedulerManager;
    }

    public void setGaDataSchedulerManager(GADataSchedulerManager gaDataSchedulerManager) {
        this.gaDataSchedulerManager = gaDataSchedulerManager;
    }
}
