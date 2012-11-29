/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository.impl;

import com.bmsils.gcn.persistence.domain.Device;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.persistence.repository.DeviceRepository;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/5/12
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("deviceRepository")
@Transactional
public class DeviceRepositoryImpl extends RepositoryImpl implements DeviceRepository{

    public DeviceRepositoryImpl() {
        super(Device.class);
    }

    @Override
    public List<Device> getDeviceDetails(String userGCN) {
        LinkedList<Device> devices = new LinkedList<Device>();
        try {
            Criteria criteria = getSession().createCriteria(Device.class);
            //List<Object> oList = criteria.add(Restrictions.eq("userGCN.userGCN", userGCN))
            List<Object> oList = criteria
                    .createAlias("userGCN", "u")
                    .add(Restrictions.or(Restrictions.eq("userGCN.userGCN", userGCN),Restrictions.eq("u.alias", userGCN)))
                    .list();
            if (oList != null) {
                for (Object cObj : oList) {
                    Device c = (Device) cObj;
                    devices.add(c);
                }
                return devices;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getTotalGCNCount(String uuid) {
        try {
            Criteria criteria = getSession().createCriteria(Device.class);
            List<Object> oList = criteria
                    .add(Restrictions.eq("uuid", uuid))
                    .list();
            if (oList != null) {
                return oList.size();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public void updateDeviceLocation(String deviceId, String location) {
        List<Object> oList = getSession().createCriteria(Device.class)
                .add(Restrictions.eq("uuid", deviceId))
                .list();
        if (oList != null) {
            for (Object dObj : oList) {
                Device device = (Device) dObj;
                device.setLastFoundLocation(location);
                merge(device);
            }
        }

    }

    @Override
    public Device getDeviceDetails(String userGCN, String uuid) {
        LinkedList<Device> devices = new LinkedList<Device>();
        try {
            Criteria criteria = getSession().createCriteria(Device.class);
           // Object o = criteria.add(Restrictions.eq("userGCN.userGCN",userGCN))

            Object o = criteria.createAlias("userGCN", "u")
                               .add(Restrictions.or(Restrictions.eq("userGCN.userGCN", userGCN),Restrictions.eq("u.alias", userGCN)))
                               .add(Restrictions.eq("uuid", uuid))
                               .uniqueResult();
            if (o != null) {
                return (Device)o;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    


    @Override
    public void registerDevice(String deviceId, String deviceToken) {
        Object o = getSession().createCriteria(Device.class)
                .add(Restrictions.eq("uuid", deviceId))
                .uniqueResult();
        if (o != null) {
            ((Device)o).setDeviceToken(deviceToken);
            merge(o);
        }
    }

    @Override
    public void unregisterDevice(String deviceId, String deviceToken) {
        Object o = getSession().createCriteria(Device.class)
                .add(Restrictions.eq("uuid", deviceId))
                .add(Restrictions.eq("deviceToken", deviceToken))
                .uniqueResult();
        if (o != null) {
            ((Device)o).setDeviceToken(null);
            merge(o);
        }
    }

    @Override
    public Device getDeviceDetails(String userGCN, boolean primaryDevice) {
        try {
            Criteria criteria = getSession().createCriteria(Device.class);
            //Object o = criteria.add(Restrictions.eq("userGCN.userGCN",userGCN))
            Object o = criteria
                    .createAlias("userGCN", "u")
                    .add(Restrictions.or(Restrictions.eq("userGCN.userGCN", userGCN),Restrictions.eq("u.alias", userGCN)))
                    .add(Restrictions.eq("isPrimaryDevice", true))
                    .uniqueResult();
            if (o != null) {
                return (Device)o;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateDeviceBlock(String deviceId, boolean block) {
        Object o = getSession().createCriteria(Device.class)
                .add(Restrictions.eq("uuid", deviceId))
                .uniqueResult();
        if (o != null) {
            ((Device)o).setBlockGcn(block);
            merge(o);
        }
    }

    @Override
    public Device getDeviceDetailsById(String deviceId, String gcn) {
        Object o = getSession().createCriteria(Device.class)
                .createAlias("userGCN", "u")
                .add(Restrictions.eq("uuid", deviceId))
                .add(Restrictions.or(Restrictions.eq("userGCN.userGCN", gcn),Restrictions.eq("u.alias", gcn)))
                .uniqueResult();
        return (Device)o;
    }

    @Override
    public int getTotalDeviceCount(String gcn) {
        List<Object> o = getSession().createCriteria(Device.class)
                .createAlias("userGCN", "u")
                .add(Restrictions.or(Restrictions.eq("userGCN.userGCN", gcn),Restrictions.eq("u.alias", gcn)))
                .list();
        return o.size();
    }

    @Override
    public void disassociateDeviceFromAccount(String gcn, String uuid) {
        Device device = getDeviceDetailsById(uuid,gcn);
        remove(device);
    }
}
