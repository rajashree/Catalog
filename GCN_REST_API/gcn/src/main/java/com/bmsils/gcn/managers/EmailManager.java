/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import com.bmsils.gcn.notification.ForgotPwdNotification;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/30/12
 * Time: 6:49 PM
 * Service to process email sending requests
 */
public interface EmailManager extends Manager{

    /**
     * Send out Forgot password notifications as emails
     * @param notifications
     * @return
     */
    public Collection<ForgotPwdNotification> processMessages(Collection<ForgotPwdNotification> notifications);


}
