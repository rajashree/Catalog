/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository.impl;

import com.bmsils.gcn.persistence.domain.ProfileVisibility;
import com.bmsils.gcn.persistence.repository.ProfileVisibilityRepository;
import com.bmsils.gcn.persistence.repository.UserRepository;
import com.bmsils.gcn.utils.GCNUtils;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/11/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("profileVisibilityRepository")
@Transactional
public class ProfileVisibilityRepositoryImpl extends RepositoryImpl implements ProfileVisibilityRepository  {
    public ProfileVisibilityRepositoryImpl() {
        super(ProfileVisibility.class);
    }


    @Override
    public ProfileVisibility getContactProfileDetailsVisiblity(String gcn, String contactGCN) {
        try {
            Query query = getSession().getNamedQuery("getContactProfileDetailsVisiblity");
            query.setParameter("gcn", gcn);
            query.setParameter("contactGCN",contactGCN);
            Object o =query.uniqueResult();
            if (o != null) {
                return (ProfileVisibility) o;
            }


        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateProfileFieldUpdates(String gcn, String profileFieldsUpdated) {
    	
        try {
            Query query = getSession().getNamedQuery("updateProfileFieldUpdates");
           
            query.setParameter("gcn", gcn);
           
            List<Object> oList = query.list();
           
            if (oList != null) {
                for(Object o : oList){
                	
                    ((ProfileVisibility)o).setProfileFieldsUpdated(profileFieldsUpdated);
                   
                    merge(o);
                    
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public void updateProfileFieldUpdates(String gcn, String contactGCN, String profileFieldsUpdated) {
        try {
            Query query = getSession().getNamedQuery("getContactProfileDetailsVisiblity");
            query.setParameter("gcn", gcn);
            query.setParameter("contactGCN",contactGCN);
            Object o =query.uniqueResult();
           
            ((ProfileVisibility)o).setProfileFieldsUpdated(profileFieldsUpdated);
            merge(o);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Map<String,Object> getRecentUpdates(String gcn) {
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            Query query = getSession().getNamedQuery("getRecentUpdates");
            query.setParameter("gcn", gcn);
            List<Object> oList = query.list();
            if (oList != null) {
                for(Object o : oList){
                    ProfileVisibility profileVisibility = (ProfileVisibility)o;
                    if(!GCNUtils.isNullOrEmpty(profileVisibility.getProfileFieldsUpdated())){
                        List list = new ArrayList();
                        list.add(0,profileVisibility.getProfileFieldsUpdated());
                        list.add(1,userRepository.get(profileVisibility.getUserGcn().getUserGCN()).getAvatar());
                        result.put(profileVisibility.getUserGcn().getUserGCN(),list);
                    }
                }
                return result;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
