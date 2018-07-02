/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import com.bmsils.gcn.beans.Notification;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/24/12
 * Time: 12:30 PM
 * Service to handle Pushnotification related function calls
 */
public interface PushNotificationManager extends Manager{
    /**
     * send push notifications to the devices
     * @param notifications
     */
    public void pushNotifications(List<Notification> notifications);

}
