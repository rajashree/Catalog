/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/5/12
 * Time: 12:23 PM
 * Device is a domain object for "addresses" table
 */
@Table(name = "devices")
@Entity
public class Device implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    public Long id;

    @Column(nullable = false)
    private String uuid;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "userGCN", nullable = false)
    private User userGCN;

    @Column(nullable = false)
    private String deviceType;

    @Column(nullable = true)
    private String lastFoundLocation;

    @Column(nullable = true)
    private boolean isPrimaryDevice;
    
    @Column(nullable = true)
    private String deviceToken;

    @Column(nullable = true)
    private boolean blockGcn;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserGCN() {
        return userGCN;
    }

    public void setUserGCN(User userGCN) {
        this.userGCN = userGCN;
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

    public boolean isBlockGcn() {
        return blockGcn;
    }

    public void setBlockGcn(boolean blockGcn) {
        this.blockGcn = blockGcn;
    }
}




