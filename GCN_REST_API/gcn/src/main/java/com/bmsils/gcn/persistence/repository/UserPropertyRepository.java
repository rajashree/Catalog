/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository;

import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.persistence.domain.UserProperty;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/3/12
 * Time: 12:17 PM
 * UserPropertyRepository contains data access methods pertaining to UserProperty table related functions
 */
public interface UserPropertyRepository extends Repository {
    boolean addUserProperty(User user,String propName, String propValue);
    
    void updateUserProperty(String userGCN,String propName, String propValue);

    boolean removeUserProperty(User user,String propName);

    UserProperty getUserProperty(User user,String propValue);

    public List<UserProperty> getResetPwdRecords();
}
