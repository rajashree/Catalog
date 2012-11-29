/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.beans;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/30/12
 * Time: 11:17 AM
 * ProfileVisibilityBean is a UI bean object being used to map ProfileVisibility Entity
 */
public class ProfileVisibilityBean {
    String gcn;
    String profileFields;
    byte[] avatar;

    public ProfileVisibilityBean(String gcn, String profileFields, byte[] avatar) {
        this.gcn = gcn;
        this.profileFields = profileFields;
        this.avatar = avatar;
    }

    public String getGcn() {
        return gcn;
    }

    public void setGcn(String gcn) {
        this.gcn = gcn;
    }

    public String getProfileFields() {
        return profileFields;
    }

    public void setProfileFields(String profileFields) {
        this.profileFields = profileFields;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
