/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */


package com.bmsils.gcn.persistence.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 3/22/12
 * Time: 5:48 PM
 * User is a domain object for "addresses" table
 */
@Table(name = "users")
@Entity
public class User  implements Serializable {

    @Id
    @Column(unique = true, length = 7, nullable = false)
    private String userGCN;

    @Column(nullable = true, columnDefinition="TinyInt(1) default '0'")
    private boolean isAdmin;

    @Column(nullable = true)
    private String alias;

    @Column(nullable = true)
    private String userName;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long phoneNumber;

    @Column(nullable = true)
    private String profileStatus;

    @Transient
    public byte[] avatar;

    @Column(nullable = true)
    private String emailId;

    @Column(nullable = true)
    private String facebookId;

    @Column(nullable = true)
    private String twitterId;

    @ManyToOne(fetch = FetchType.LAZY,  optional = true)
    private Address residentialAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Address officeAddress;

    @Transient
    public boolean blockedUser;


    public Address getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(Address residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public Address getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(Address officeAddress) {
        this.officeAddress = officeAddress;
    }

    @Column(nullable = true)
    private String linkedinId;

    @Column(nullable = true)
    private String designation;

    @Column(nullable = true)
    private String officeName;

    @Column(nullable = true)
    private String officeEmailId;

    @Column(nullable = true)
    private Long officePhoneNumber;
    
    @Column(name="home_phone2" ,  nullable = true)
    private Long PhoneNumber2;

    
    public Long getPhoneNumber2() {
		return PhoneNumber2;
	}

	public void setPhoneNumber2(Long phoneNumber2) {
		PhoneNumber2 = phoneNumber2;
	}

	@Column(name="office_phone2" ,  nullable = true)
    private Long officePhoneNumber2;

    public Long getOfficePhoneNumber2() {
		return officePhoneNumber2;
	}

	public void setOfficePhoneNumber2(Long officePhoneNumber2) {
		this.officePhoneNumber2 = officePhoneNumber2;
	}

	@Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date creationDate;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=true, insertable = false, updatable = false, columnDefinition="Timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date lastUpdateDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lastSyncDevice", nullable = true)
    private Device lastSyncDevice;

    @Column
    private boolean passwordChangeFlag;

    @Column(nullable = true)
    private int presence;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Device getLastSyncDevice() {
        return lastSyncDevice;
    }

    public void setLastSyncDevice(Device lastSyncDevice) {
        this.lastSyncDevice = lastSyncDevice;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
        
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
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

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
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

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }



    public String getOfficeEmailId() {
        return officeEmailId;
    }

    public void setOfficeEmailId(String officeEmailId) {
        this.officeEmailId = officeEmailId;
    }

    public Long getOfficePhoneNumber() {
        return officePhoneNumber;
    }

    public void setOfficePhoneNumber(Long officePhoneNumber) {
        this.officePhoneNumber = officePhoneNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }





    public boolean isPasswordChangeFlag() {
        return passwordChangeFlag;
    }

    public void setPasswordChangeFlag(boolean passwordChangeFlag) {
        this.passwordChangeFlag = passwordChangeFlag;
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
    public String toString(){
    	return alias+" "+emailId+" "+userGCN+" "+profileStatus+" "+facebookId+" "+PhoneNumber2;
    }
}
