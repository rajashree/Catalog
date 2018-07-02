/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.beans;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/26/12
 * Time: 6:08 PM
 * DeviceBean is a UI bean object being used to map Device Entity
 */
public class DeviceBean {
    private String uuid;

    private String gcn;

    private String deviceType;

    private String lastFoundLocation;

    private boolean isPrimaryDevice;

    private String deviceToken;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGcn() {
        return gcn;
    }

    public void setGcn(String gcn) {
        this.gcn = gcn;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLastFoundLocation() {
        return lastFoundLocation;
    }

    public void setLastFoundLocation(String lastFoundLocation) {
        this.lastFoundLocation = lastFoundLocation;
    }

    public boolean isPrimaryDevice() {
        return isPrimaryDevice;
    }

    public void setPrimaryDevice(boolean primaryDevice) {
        isPrimaryDevice = primaryDevice;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
