/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import com.bmsils.gcn.persistence.domain.Device;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/23/12
 * Time: 6:49 PM
 * Service to handle Device related function calls
 */
public interface DeviceManager  extends Manager{
    /**
     * register the device - used by push notifications
     * @param deviceId
     * @param deviceToken
     */
    void registerDevice(String deviceId, String deviceToken);

    /**
     * unregiter the device - used by push notifications
     * @param deviceId
     * @param deviceToken
     */
    void unregisterDevice(String deviceId, String deviceToken);

    /**
     * get primary device details of an user
     * @param userGCN
     * @return Primary Device object
     */
    public Device getPrimaryDeviceDetails(String userGCN);

    /**
     * get details of all the devices associated with the GCN
     * @param userGCN
     * @return list of Devices objects
     */
    public List<Device> getDeviceDetails(String userGCN);

    /**
     * get the total GCN Count associated with a device
     * @param uuid
     * @return
     */
    public int getTotalGCNCount(String uuid);

    /**
     * update the device location
     * @param deviceId
     * @param location
     */
    void updateDeviceLocation(String deviceId, String location);

    /**
     * block the device
     * @param deviceId
     */
    void blockDevice(String deviceId);

    /**
     * unblock the device
     * @param deviceId
     */
    void unBlockDevice(String deviceId);

    /**
     * get the device details by deviceId and gcn
     * @param deviceId
     * @param gcn
     * @return Device object
     */
    public Device getDeviceDetailsById(String deviceId,String gcn);

    /**
     * get the total device count associated with a GCN
     * @param gcn
     * @return
     */
    int getTotalDeviceCount(String gcn);

    /**
     * dis associate a device from an account
     * @param gcn
     * @param uuid
     */
    public void disassociateDeviceFromAccount(String gcn, String uuid);
}
