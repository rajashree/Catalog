/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import com.bmsils.gcn.notification.ForgotPwdNotification;
import com.bmsils.gcn.persistence.domain.Device;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.persistence.domain.UserProperty;
import com.bmsils.gcn.web.beans.ProfileDataBean;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 3/22/12
 * Time: 5:51 PM
 * Service to handle Users related function calls
 */
public interface UserManager extends Manager{

    /**
     * get user based on gcn
     * @param userGCN
     * @return
     */
    public User getUser(String userGCN);

    /**
     * login
     * @param gcn
     * @param password
     * @param uuid
     * @param deviceType
     * @return
     */
    public HashMap<String,Object> login(String gcn, String password, String uuid, String deviceType);

    /**
     * register gcn
     * @param gcn
     * @param password
     * @param uuid
     * @param deviceType
     * @param phoneNumber
     * @param email
     * @throws IOException
     */
    public void registerGCN(String gcn, String password, String uuid, String deviceType, Long phoneNumber, String email) throws IOException;

    /**
     * get user based on gcn and password
     * @param gcn
     * @param password
     * @return
     */
    public User getUser(String gcn, String password);
    
    
    
    public boolean checkUserFromAlias(String alias,String password);
    /**
     * get all users in the system
     * @return
     */
    public Collection<User> getAllUsers();

    /**
     * add user
     * @param user
     */
    void addUser(User user);

    /**
     * update user
     * @param user
     */
    void updateUser(User user);

    /**
     * get user settings based on gcn and device id
     * @param userGCN
     * @param uuid
     * @return
     */
    public Device getUserSettings(String userGCN, String uuid);

    /**
     * update user settings for a device
     * @param userGCN
     * @param uuid
     * @param isPrimaryDevice
     */
    public void updateUserSettings(String userGCN, String uuid, boolean isPrimaryDevice);

    /**
     * get contacts based on GCN
     * @param gcn
     * @return
     */
    Collection<User> getContacts(String gcn);

    /**
     * get contact's profile details
     * @param gcn
     * @param contactGCN
     * @return
     */
    ProfileDataBean getContactProfileDetails(String gcn, String contactGCN);

    /**
     * update profile field updates
     * @param gcn
     * @param profileFields
     */
    public void updateProfileFieldUpdates(String gcn, String profileFields);

    /**
     * get contact chat details - name and city
     * @param gcn
     * @param contactGCN
     * @return
     */
    public Map<String,String> getContactChatDetails(String gcn, String contactGCN);

    /**
     * get recent updates of the contacts
      * @param gcn
     * @return
     */
    Map<String,Object> getRecentUpdates(String gcn);

    /**
     * get all registered gcn's in the system
     * @return
     */
    List<String> getRegisteredGCNs();

    /**
     * add alias for an user
     * @param userGCN
     * @param alias
     */
    void addAlias(String userGCN, String alias);

    /**
     * remove alias of an user
     * @param userGCN
     */
    void removeAlias(String userGCN);

    /**
     * get total number of gcn based on device
     * @param uuid
     * @return
     */
    public int getTotalGCNCount(String uuid);

    /**
     * update shared profile details with a particular contact
     * @param gcn
     * @param contactGCN
     * @param visibleProfileFields
     */
    void updateSharedProfileFields(String gcn, String contactGCN, List<String> visibleProfileFields);

    /**
     * get GCN based on gcn (or) alias
     * @param gcn
     * @return
     */
    String getUserGCN(String gcn);

    /**
     * get user and aliases
     * @param start
     * @param limit
     * @return
     */
    List<User> getUserAndAliases(int start, int limit);

    /**
     * add user property
     * @param user
     * @param propName
     * @param propValue
     * @return
     */
    boolean addUserProperty(User user, String propName, String propValue);

    /**
     * update user property
     * @param userGCN
     * @param propName
     * @param propValue
     */
    void updateUserProperty(String userGCN, String propName, String propValue);

    /**
     * get user property
     * @param user
     * @param propValue
     * @return
     */
    UserProperty getUserProperty(User user, String propValue);

    /**
     * remove user property
     * @param userGCN
     * @param propName
     * @return
     */
    boolean removeUserProperty(String userGCN, String propName);

    /**
     * get user by emailId
     * @param emailid
     * @return
     */
    User getUserByEmail(String emailid);

    /**
     * mark an user as admin
     * @param user
     * @param isAdmin
     */
    void markAsAdmin(User user, boolean isAdmin);

    /**
     * get active forgot password requests
     * @return
     */
    List<ForgotPwdNotification> getEligibleForgotPasswordRequests();

    /**
     * update profile status
     * @param gcn
     * @param profileStatus
     */
    void updateProfileStatus(User gcn, String profileStatus);
}
