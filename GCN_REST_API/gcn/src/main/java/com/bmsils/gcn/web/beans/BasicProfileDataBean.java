/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.beans;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/20/12
 * Time: 2:19 PM
 * BasicProfileDataBean is a UI bean object being used to map User Entity.  It is a sub version of ProfileDataBean
 */
public class BasicProfileDataBean {
    private String userGCN;
    private String alias;
    private String firstName;
    private String lastName;
    private String userName;
    private int presence;
    private boolean blockedUser;
    private byte[] avatar;
    private String profileStatus;


    public String getUserGCN() {
        return userGCN;
    }

    public void setUserGCN(String userGCN) {
        this.userGCN = userGCN;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPresence() {
        return presence;
    }

    public void setPresence(int presence) {
        this.presence = presence;
    }

    public boolean isBlockedUser() {
        return blockedUser;
    }

    public void setBlockedUser(boolean blockedUser) {
        this.blockedUser = blockedUser;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }
}
