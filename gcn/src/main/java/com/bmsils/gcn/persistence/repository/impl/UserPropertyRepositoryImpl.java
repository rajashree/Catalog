/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository.impl;

import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.persistence.domain.UserProperty;
import com.bmsils.gcn.persistence.repository.AddressRepository;
import com.bmsils.gcn.persistence.repository.ImageRepository;
import com.bmsils.gcn.persistence.repository.UserPropertyRepository;
import com.bmsils.gcn.persistence.repository.UserRepository;
import com.bmsils.gcn.utils.GCNUtils;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/3/12
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("userPropertyRepository")
@Transactional
public class UserPropertyRepositoryImpl extends RepositoryImpl implements UserPropertyRepository {

    public UserPropertyRepositoryImpl() {
        super(UserProperty.class);
    }


    
    public boolean addUserProperty(User user, String propName, String propValue) {
        try{
            UserProperty obj = new UserProperty();
            obj.setUserGCN(user);
            obj.setName(propName);
            obj.setValue(propValue);
            put(obj);
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    
    public void updateUserProperty(String userGCN, String propName, String propValue) {
        try{
            Object o = getSession().createCriteria(UserProperty.class)
                    .add(Restrictions.eq("userGCN.userGCN", userGCN))
                    .add(Restrictions.eq("name",propName))
                    .uniqueResult();
            if (o != null) {
                ((UserProperty)o).setValue(propValue);
                merge(o);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }


    
    public boolean removeUserProperty(User user, String propName) {
        try{
            Object o = getSession().createCriteria(UserProperty.class)
                    .add(Restrictions.eq("userGCN", user))
                    .add(Restrictions.eq("name",propName))
                    .uniqueResult();
            if (o != null)
                remove((UserProperty)o);
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    
    public UserProperty getUserProperty(User user, String propValue) {
        try{
            Object o = getSession().createCriteria(UserProperty.class)
                    .add(Restrictions.eq("userGCN.userGCN", user.getUserGCN()))
                    .add(Restrictions.eq("value",propValue))
                    .uniqueResult();
            if (o != null)
                return ((UserProperty)o);
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    
    public List<UserProperty> getResetPwdRecords() {
        LinkedList<UserProperty> props = new LinkedList<UserProperty>();
        try {
            Criteria criteria = getSession().createCriteria(UserProperty.class);
            List<Object> oList = criteria
                    .add(Restrictions.eq("name", "resetPwdToken"))
                    .list();
            if (oList != null) {
                for (Object cObj : oList) {
                    UserProperty c = (UserProperty) cObj;
                    if(isResetPwdNotificationToBeSent(c)){
                        props.add(c);
                    }
                }
                return props;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    private boolean isResetPwdNotificationToBeSent(UserProperty userProperty){
        try {
            Criteria criteria = getSession().createCriteria(UserProperty.class);
            Object o = criteria
                    .add(Restrictions.and(Restrictions.eq("name", "resetPwdEmailSent"),Restrictions.eq("value","0")))
                    .add(Restrictions.eq("userGCN.userGCN",userProperty.getUserGCN().getUserGCN()))
                    .uniqueResult();
            if (o != null) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}