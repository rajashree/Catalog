/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/29/12
 * Time: 9:02 PM
 * <p/>
 * Implementation of the Notification processor
 */
@Service
public class NotificationManagerImpl implements NotificationManager {
    private static final Logger log = LoggerFactory.getLogger(NotificationManagerImpl.class);


    private Collection<EmailMessage> messages;
    private Collection<EmailAddressNotification> notifications;


    @Override
    public Collection<EmailAddressNotification> processMessages(Collection<EmailAddressNotification> notifications) {
        GenericSMTPProxy genericSMTPProxy = GenericSMTPProxy.getInstance();
        genericSMTPProxy.initConfiguration();
        //Currently loading the Generic SMTP properties which are configured in the profile:
        //Development or UAT or Production
        //TODO: If we need to leverage Retailer specific Email Server, then we can over-ride
        //not by System Property but either a retrieving from  or another Retailer specific Configuration file
        return genericSMTPProxy.send(notifications);
    }


}
