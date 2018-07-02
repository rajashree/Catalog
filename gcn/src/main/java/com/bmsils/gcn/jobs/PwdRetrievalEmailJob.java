/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.jobs;

import com.bmsils.gcn.managers.EmailManager;
import com.bmsils.gcn.managers.UserManager;
import com.bmsils.gcn.notification.ForgotPwdNotification;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 5/8/12
 * Time: 2:12 PM
 * The PwdRetrievalEmailJob schedules email notifications sent to the users who have requested for Forgot Password requests
 */
public class PwdRetrievalEmailJob  extends AbstractJob{

    /**
     * Email Notifications are sent to the Users who have requested for password retrieval
     * @param context
     */
    @Override
    protected void executeJob(JobExecutionContext context) {
        logger.info(" -----  PwdRetrievalEmailJob -----  ");
        long startTime = System.currentTimeMillis();
        List<ForgotPwdNotification> notifications = this.userManager.getEligibleForgotPasswordRequests();

        if (notifications != null && notifications.size() > 0) {   //All records which have notification flag = 0/-1 are eligible for email notifications
            logger.info(" No of password retrieval emails - " + notifications.size());
            Collection<ForgotPwdNotification> responses = this.emailManager.processMessages(notifications);
            handleResponseNotifications(responses);
        } else {
            logger.info("No records for email processing.");
        }

        long endTime = System.currentTimeMillis();
        computePerformance("PwdRetrievalEmailJob  -  Process", startTime, endTime);

    }

    /**
     * Once the Emails are being dispatched, set the resetPwdEmailSent flag to 1.
     * This will prevent the scheduler to pick the request the second time
     * @param responses
     */
    protected void handleResponseNotifications(Collection<ForgotPwdNotification> responses) {
        logger.info("Update the resetPwdEmailSent Flag");
        for (ForgotPwdNotification notification : responses) {
            this.userManager.updateUserProperty(notification.getGcn(),"resetPwdEmailSent","1");
        }
        logger.info(" EmailNotification processed # of records  ::: " + responses.size());
    }

    protected void computePerformance(String taskName, long startTime, long endTime) {
        logger.info(" Total time for completing the - " + taskName + "  is   " + (endTime - startTime) / 1000);
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    protected EmailManager emailManager;

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    @Autowired
    protected UserManager userManager;

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
