package com.dell.acs.jobs;

import com.dell.acs.managers.ProductManager;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * written in reference to feature https://jira.marketvine.com/browse/CS-248
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$
 */
public class ProductWeightComputationJob extends AbstractJob {

    private static final AtomicBoolean isExecuting = new AtomicBoolean(false);

    @Override
    protected void executeJob(final JobExecutionContext context) {
        if (isExecuting.compareAndSet(false, true)) {
            productManager.computeProductWeights();
            isExecuting.set(false);// after execution.
        } else {
            logger.warn("Another instance is currently in execution.");
        }
    }

    @Autowired
    private ProductManager productManager;

    public void setProductManager(final ProductManager productManager) {
        this.productManager = productManager;
    }
}
