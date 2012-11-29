/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers.impl;

import com.bmsils.gcn.managers.EmailManager;
import com.bmsils.gcn.notification.EmailMessage;
import com.bmsils.gcn.notification.ForgotPwdNotification;
import com.bmsils.gcn.notification.SMTPProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/30/12
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EmailManagerImpl implements EmailManager {
    private static final Logger log = LoggerFactory.getLogger(EmailManagerImpl.class);


    private Collection<EmailMessage> messages;
    private Collection<ForgotPwdNotification> notifications;


    @Override
    public Collection<ForgotPwdNotification> processMessages(Collection<ForgotPwdNotification> notifications) {
        log.info("Process Password Retrieval Email Notifications ");
        SMTPProxy smtpProxy = SMTPProxy.getInstance();
        smtpProxy.initConfiguration();
        return smtpProxy.send(notifications);
    }
}
