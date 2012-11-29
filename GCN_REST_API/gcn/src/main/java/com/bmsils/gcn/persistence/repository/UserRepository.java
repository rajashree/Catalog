/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository;

import com.bmsils.gcn.notification.ForgotPwdNotification;
import com.bmsils.gcn.persistence.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/3/12
 * Time: 12:17 PM
 * UserRepository contains data access methods pertaining to User table related functions
 */
public interface UserRepository extends Repository {
    public User get(String userGCN);
    public boolean getFromAlias(String userAlias,String password);

    public User getUserByEmail(String emailid);
    
    public User get(String username, String password);

    void addAlias(String userGCN, String alias);

    void removeAlias(String userGCN);

    int getTotalGCNCount(String email);

    String getUserGCN(String gcn);

    List<User> getUserAndAliases(int start, int limit);

    List<ForgotPwdNotification> getEligibleForgotPasswordRequests();
}
