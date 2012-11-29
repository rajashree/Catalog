/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository;

import com.bmsils.gcn.persistence.domain.Device;
import com.bmsils.gcn.persistence.domain.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/5/12
 * Time: 4:59 PM
 * DeviceRepository contains data access methods pertaining to Device table related functions
 */

public interface DeviceRepository extends Repository{

    public Device getDeviceDetails(String userGCN, String uuid);

    public List<Device> getDeviceDetails(String userGCN);

    public int getTotalGCNCount(String uuid);

    void updateDeviceLocation(String deviceId, String location);

    void registerDevice(String deviceId, String deviceToken);

    void unregisterDevice(String deviceId, String deviceToken);

    public Device getDeviceDetails(String userGCN, boolean primaryDevice);

    void updateDeviceBlock(String deviceId, boolean block);

    Device getDeviceDetailsById(String deviceId, String gcn);

    int getTotalDeviceCount(String gcn);

    void disassociateDeviceFromAccount(String gcn, String uuid);
}
