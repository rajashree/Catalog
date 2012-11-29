/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers.impl;

import com.bmsils.gcn.managers.DeviceManager;
import com.bmsils.gcn.persistence.domain.Device;
import com.bmsils.gcn.persistence.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/23/12
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DeviceManagerImpl implements DeviceManager{
    @Override
    @Transactional
    public void registerDevice(String deviceId, String deviceToken) {
        deviceRepository.registerDevice(deviceId, deviceToken); 
    }

    @Override
    @Transactional
    public void unregisterDevice(String deviceId, String deviceToken) {
        deviceRepository.unregisterDevice(deviceId, deviceToken);
    }

    @Override
    public Device getPrimaryDeviceDetails(String userGCN) {
        return deviceRepository.getDeviceDetails(userGCN, true);
    }

    @Override
    public List<Device> getDeviceDetails(String userGCN) {
        return deviceRepository.getDeviceDetails(userGCN);
    }

    @Override
    public int getTotalGCNCount(String uuid) {
        return deviceRepository.getTotalGCNCount(uuid);
    }
    @Override
    public void updateDeviceLocation(String deviceId, String location) {
        deviceRepository.updateDeviceLocation(deviceId, location);
    }

    @Override
    public void blockDevice(String deviceId) {
        deviceRepository.updateDeviceBlock(deviceId,true);
    }

    @Override
    public void unBlockDevice(String deviceId) {
        deviceRepository.updateDeviceBlock(deviceId,false);
    }

    @Override
    public Device getDeviceDetailsById(String deviceId, String gcn) {
        return deviceRepository.getDeviceDetailsById(deviceId, gcn);
    }

    @Override
    public int getTotalDeviceCount(String gcn) {
        return deviceRepository.getTotalDeviceCount(gcn);
    }

    @Override
    public void disassociateDeviceFromAccount(String gcn, String uuid) {
        deviceRepository.disassociateDeviceFromAccount(gcn,uuid);
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    DeviceRepository deviceRepository;

    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }

    public void setDeviceRepository(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }
}
