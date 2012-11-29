package com.dell.dw.jobs.order;

import com.dell.dw.managers.OrderDataSchedulerManager;
import com.sourcen.core.jobs.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class OrderDataSchedulerJob extends AbstractJob {

    @Override
    protected void executeJob(JobExecutionContext context) {
        try {
            orderDataSchedulerManager.updateDataSchedulerBatches();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Autowired
    OrderDataSchedulerManager orderDataSchedulerManager;

    public OrderDataSchedulerManager getOrderDataSchedulerManager() {
        return orderDataSchedulerManager;
    }

    public void setOrderDataSchedulerManager(OrderDataSchedulerManager orderDataSchedulerManager) {
        this.orderDataSchedulerManager = orderDataSchedulerManager;
    }
}
