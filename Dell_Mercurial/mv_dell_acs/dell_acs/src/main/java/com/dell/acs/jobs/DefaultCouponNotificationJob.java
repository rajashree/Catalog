package com.dell.acs.jobs;

import com.dell.acs.managers.FacebookWallShareManager;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.jobs.AbstractJob;
import com.sourcen.core.notification.EmailAddressNotification;
import com.sourcen.core.notification.NotificationManager;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/29/12
 * Time: 5:02 PM
 * The DefaultCoupon notification job which schedules email notifications for
 * every successful facebook wall share.
 */
public final class DefaultCouponNotificationJob extends AbstractJob {

    @Autowired
    protected FacebookWallShareManager facebookWallShareManager;

    public void setFacebookWallShareManager(FacebookWallShareManager facebookWallShareManager) {
        this.facebookWallShareManager = facebookWallShareManager;
    }

    @Autowired
    protected NotificationManager notificationManager;

    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    @Override
    protected void executeJob(JobExecutionContext context) {
    	CountStatMutator count = null;
    	try {
        	count = (CountStatMutator)StatUtil.getInstance().getStat(CountStat.class, "DefaultCouponNotificationJob.Count");
        	count.inc();
        	count.apply();
	        logger.info(" -------  DefaultCouponNotificationJob -----  ");
	        List<EmailAddressNotification> notifications = this.facebookWallShareManager.getEligibleNotifications();
	
	        if (notifications != null && notifications.size() > 0) {   //All records which have notification flag = 0/-1 are eligible for email notifications
	            logger.info(" No of shares which are eligible for coupon email notifications - " + notifications.size());
	            long startTime = System.currentTimeMillis();
	            Collection<EmailAddressNotification> responses = this.notificationManager.processMessages(notifications);
	            long endTime = System.currentTimeMillis();
	            computePerformance("Email Process", startTime, endTime);
	            handleResponseNotifications(responses);
	
	        } else {
	            logger.info("No records for email processing.");
	        }
    	} finally {
        	if (count != null) {
        		count.clear();
        		count.dec();
        		count.apply();
        	}
    	}
    }

    protected void handleResponseNotifications(Collection<EmailAddressNotification> responses) {
        logger.info(" Update the facebook wall share notification status post EmailNotification process ");
        for (EmailAddressNotification notification : responses) {
            //logger.info(" Update the following FB Share wall "+notification.toString());
            this.facebookWallShareManager.updateFacebookWallShare(notification);
        }
        logger.info(" EmailNotification processed # of records  ::: " + responses.size());
    }

    protected void computePerformance(String taskName, long startTime, long endTime) {

        logger.info(" Total time for completing the - " + taskName + "  is   " + (endTime - startTime) / 1000);
    }
}
