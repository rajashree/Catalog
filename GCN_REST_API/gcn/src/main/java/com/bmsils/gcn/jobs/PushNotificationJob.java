/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.jobs;

import com.bmsils.gcn.beans.Notification;
import com.bmsils.gcn.managers.DeviceManager;
import com.bmsils.gcn.managers.InviteManager;
import com.bmsils.gcn.managers.MessageManager;
import com.bmsils.gcn.managers.PushNotificationManager;
import com.bmsils.gcn.persistence.domain.Device;
import com.bmsils.gcn.persistence.domain.Invite;
import com.bmsils.gcn.persistence.domain.Message;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/23/12
 * Time: 12:11 PM
 * The PushNotificationJob schedules push notifications for Android, Blackberry and IPhone devices for messages and invites being sent to the device
 */
public class PushNotificationJob extends AbstractJob{

    /**
     *  Messages, Invites which have not been received and Messages, Invites which has been sent before duration set in
     *  Application Properties are sent as Push Notifications to the Device's PushNotification Services
     * @param context
     */
    @Override
    protected void executeJob(JobExecutionContext context) {
        logger.info(" -----  PushNotificationJob -----  ");

        HashMap<String, Notification> notifications = new HashMap<String, Notification>();
        long startTime = System.currentTimeMillis();

        //All Messages which have not yet been received and the Message has been sent before "application.properties -> pushNotification.message.duration" 
        List<Message> messagesForPN = this.messageManager.getEligibleMessagesForPN();

        if (messagesForPN != null && messagesForPN.size() > 0) {   
            logger.info(" No of messages which are eligible for pushnotification - " + messagesForPN.size());
        } else {
            logger.info("No new messages to be sent as Push Notifications.");
        }

        //All Invites which have not yet been received and the Invite has been sent before "application.properties -> pushNotification.invite.duration" 
        List<Invite> invitesForPN = this.inviteManager.getEligibleInvitesForPN();

        if (invitesForPN != null && invitesForPN.size() > 0) {
            logger.info(" No of invites which are eligible for pushnotification - " + invitesForPN.size());
        } else {
            logger.info("No new invites  to be sent as Push Notifications.");
        }

        for(Message message : messagesForPN){
            Device device = deviceManager.getPrimaryDeviceDetails(message.getReceiverGCN().getUserGCN());
            String deviceToken = device.getDeviceToken();
            if(deviceToken != null){
                if(notifications.get(deviceToken) != null){
                    Notification value = (Notification)notifications.get(deviceToken);
                    value.setMessageCount(value.getMessageCount() + 1);

                }else{
                    Notification bean = new Notification(device.getDeviceToken(),device.getUuid(),device.getDeviceType(),1,0);
                    notifications.put(device.getDeviceToken(),bean);
                }
            }
            System.out.println(message.toString());
        }

        for(Invite invite: invitesForPN){
            Device device = deviceManager.getPrimaryDeviceDetails(invite.getInviteeGcn().getUserGCN());
            String deviceToken = device.getDeviceToken();
            if(deviceToken != null){
                if(notifications.get(deviceToken) != null){
                    Notification value = (Notification)notifications.get(deviceToken);
                    value.setInviteCount(value.getInviteCount() + 1);

                }else{
                    Notification bean = new Notification(device.getDeviceToken(),device.getUuid(),device.getDeviceType(),0,1);
                    notifications.put(device.getDeviceToken(),bean);
                }
            }
            System.out.println(invite.toString());
        }
        pushNotificationManager.pushNotifications(new ArrayList<Notification>(notifications.values()));

        long endTime = System.currentTimeMillis();
        computePerformance("PushNotifications For Messages -  Process", startTime, endTime);


    }

    

    protected void computePerformance(String taskName, long startTime, long endTime) {
        logger.info(" Total time for completing the - " + taskName + "  is   " + (endTime - startTime) / 1000+"seconds");
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    protected InviteManager inviteManager;

    public void setInviteManager(InviteManager inviteManager) {
        this.inviteManager = inviteManager;
    }

    @Autowired
    protected MessageManager messageManager;

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @Autowired
    protected DeviceManager deviceManager;

    public DeviceManager getDeviceManager() {
        return deviceManager;
    }

    public void setDeviceManager(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }
    
    @Autowired
    protected PushNotificationManager pushNotificationManager;

    public PushNotificationManager getPushNotificationManager() {
        return pushNotificationManager;
    }

    public void setPushNotificationManager(PushNotificationManager pushNotificationManager) {
        this.pushNotificationManager = pushNotificationManager;
    }
}
