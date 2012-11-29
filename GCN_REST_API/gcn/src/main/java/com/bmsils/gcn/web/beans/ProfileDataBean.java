/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.beans;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/10/12
 * Time: 6:44 PM
 * ProfileDataBean is a UI bean object being used to map User Entity
 */
public class ProfileDataBean {
    private String userGCN;
    private String alias;
    private boolean isAdmin;
    private String firstName;
    private String lastName;
    private String userName;
    private String emailId;
    private String facebookId;
    private String twitterId;
    private String linkedinId;
    private String designation;
    private String officeEmailId;
    private String officeName;
    private Long officePhoneNumber;
    private Long phoneNumber;
    private String residentialAddAddressLine1;
    private String residentialAddAddressLine2;
    private String residentialAddCity;
    private String residentialAddState;
    private String residentialAddCountry;
    private String residentialAddPostalCode;
    private String officeAddAddressLine1;
    private String officeAddAddressLine2;
    private String officeAddCity;
    private String officeAddState;
    private String officeAddCountry;
    private String officeAddPostalCode;
    private Date lastConnectedTime;
    private int presence;
    private byte[] avatar;
    private String profileStatus;
    private HashMap<String,String> properties;

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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getLinkedinId() {
        return linkedinId;
    }

    public void setLinkedinId(String linkedinId) {
        this.linkedinId = linkedinId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOfficeEmailId() {
        return officeEmailId;
    }

    public void setOfficeEmailId(String officeEmailId) {
        this.officeEmailId = officeEmailId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Long getOfficePhoneNumber() {
        return officePhoneNumber;
    }

    public void setOfficePhoneNumber(Long officePhoneNumber) {
        this.officePhoneNumber = officePhoneNumber;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getResidentialAddAddressLine1() {
        return residentialAddAddressLine1;
    }

    public void setResidentialAddAddressLine1(String residentialAddAddressLine1) {
        this.residentialAddAddressLine1 = residentialAddAddressLine1;
    }

    public String getResidentialAddAddressLine2() {
        return residentialAddAddressLine2;
    }

    public void setResidentialAddAddressLine2(String residentialAddAddressLine2) {
        this.residentialAddAddressLine2 = residentialAddAddressLine2;
    }

    public String getResidentialAddCity() {
        return residentialAddCity;
    }

    public void setResidentialAddCity(String residentialAddCity) {
        this.residentialAddCity = residentialAddCity;
    }

    public String getResidentialAddState() {
        return residentialAddState;
    }

    public void setResidentialAddState(String residentialAddState) {
        this.residentialAddState = residentialAddState;
    }

    public String getResidentialAddCountry() {
        return residentialAddCountry;
    }

    public void setResidentialAddCountry(String residentialAddCountry) {
        this.residentialAddCountry = residentialAddCountry;
    }

    public String getResidentialAddPostalCode() {
        return residentialAddPostalCode;
    }

    public void setResidentialAddPostalCode(String residentialAddPostalCode) {
        this.residentialAddPostalCode = residentialAddPostalCode;
    }

    public String getOfficeAddAddressLine1() {
        return officeAddAddressLine1;
    }

    public void setOfficeAddAddressLine1(String officeAddAddressLine1) {
        this.officeAddAddressLine1 = officeAddAddressLine1;
    }

    public String getOfficeAddAddressLine2() {
        return officeAddAddressLine2;
    }

    public void setOfficeAddAddressLine2(String officeAddAddressLine2) {
        this.officeAddAddressLine2 = officeAddAddressLine2;
    }

    public String getOfficeAddCity() {
        return officeAddCity;
    }

    public void setOfficeAddCity(String officeAddCity) {
        this.officeAddCity = officeAddCity;
    }

    public String getOfficeAddState() {
        return officeAddState;
    }

    public void setOfficeAddState(String officeAddState) {
        this.officeAddState = officeAddState;
    }

    public String getOfficeAddCountry() {
        return officeAddCountry;
    }

    public void setOfficeAddCountry(String officeAddCountry) {
        this.officeAddCountry = officeAddCountry;
    }

    public String getOfficeAddPostalCode() {
        return officeAddPostalCode;
    }

    public void setOfficeAddPostalCode(String officeAddPostalCode) {
        this.officeAddPostalCode = officeAddPostalCode;
    }

    public Date getLastConnectedTime() {
        return lastConnectedTime;
    }

    public void setLastConnectedTime(Date lastConnectedTime) {
        this.lastConnectedTime = lastConnectedTime;
    }

    public int getPresence() {
        return presence;
    }

    public void setPresence(int presence) {
        this.presence = presence;
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

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }
}
