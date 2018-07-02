/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.beans;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/24/12
 * Time: 12:02 PM
 * Notification is a bean object being used by PushNotification Service
 */
public class Notification {
    private int messageCount;
    private int inviteCount;
    private String deviceToken;
    private String deviceId;
    private String deviceType;


    public Notification(String deviceToken, String deviceId, String deviceType, int messageCount, int inviteCount) {
        this.messageCount = messageCount;
        this.inviteCount = inviteCount;
        this.deviceToken = deviceToken;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public int getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
