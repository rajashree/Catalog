/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/5/12
 * Time: 1:01 PM
 * ProfileVisibility is a domain object for "addresses" table
 */

@NamedQueries(
        {
                /*@NamedQuery(name="getRecentUpdates",
                        query="SELECT pv from ProfileVisibility pv " +
                                "where (contactGcn.userGCN = :gcn OR contactGcn.alias = :gcn) "),*/

                /*@NamedQuery(name="getRecentUpdates",
                        query="SELECT pv from ProfileVisibility pv, Invite pv_inv " +
                                "WHERE ((pv.userGcn.userGCN = pv_inv.inviteeGcn.userGCN AND pv.contactGcn.userGCN = pv_inv.inviterGcn.userGCN) OR (pv.userGcn.userGCN = pv_inv.inviterGcn.userGCN AND pv.contactGcn.userGCN = pv_inv.inviteeGcn.userGCN)) " +
                                "AND (pv.contactGcn.userGCN = :gcn) AND (pv_inv.subscriptionStatus=3)"),


*/
                @NamedQuery(name="getRecentUpdates",
                        query="SELECT pv from ProfileVisibility pv, Invite pv_inv " +
                                "WHERE ((pv.userGcn.userGCN = pv_inv.inviteeGcn.userGCN AND pv.contactGcn.userGCN = pv_inv.inviterGcn.userGCN) " +
                                "OR (pv.userGcn.userGCN = pv_inv.inviterGcn.userGCN AND pv.contactGcn.userGCN = pv_inv.inviteeGcn.userGCN)) " +
                                "AND (pv.contactGcn.userGCN = :gcn) AND (pv_inv.subscriptionStatus=3)"),



                /*@NamedQuery(name="updateProfileFieldUpdates",
                        query="SELECT pv from ProfileVisibility pv " +
                                "where (userGcn.userGCN = :gcn OR userGcn.alias = :gcn) " +
                                "and profileFieldsUpdated Is Not Null"),*/
                @NamedQuery(name="updateProfileFieldUpdates",
                        query="SELECT pv from ProfileVisibility pv " +
                                "where (userGcn.userGCN = :gcn OR userGcn.alias = :gcn)"),
                @NamedQuery(name="getContactProfileDetailsVisiblity",
                        query="SELECT pv from ProfileVisibility pv " +
                                "where (contactGcn.userGCN = :contactGCN OR contactGcn.alias = :contactGCN) " +
                                "and (userGcn.userGCN = :gcn OR userGcn.alias = :gcn) ")
        }
)

@Table(name="profile_visibility")
@Entity
public class ProfileVisibility implements Serializable {
    @Id
    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "userGcn", nullable = false)
    public User userGcn ;

    @Id
    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "contactGcn", nullable = false)
    public User contactGcn;

    @Column(nullable = true)
    private String profileFieldsUpdated;

    @Column(nullable = true)
    private boolean phoneNumber;

    @Column(nullable = true)
    private boolean emailId;

    @Column(nullable = true)
    private boolean facebookId;

    @Column(nullable = true)
    private boolean twitterId;

    @Column(nullable = true)
    private boolean residentialAddress;

    @Column(nullable = true)
    private boolean linkedinId;

    @Column(nullable = true)
    private boolean designation;

    @Column(nullable = true)
    private boolean officeName;

    @Column(nullable = true)
    private boolean officeAddress;

    @Column(nullable = true)
    private boolean officePhoneNumber;
    @Column(nullable = true)
    private boolean officePhoneNumber2;
   

	@Column(nullable = true)
    private boolean phoneNumber2;

    @Column(nullable = true)
    private boolean officeEmailId;
    



    public ProfileVisibility() {
    }

    public ProfileVisibility(User userGcn, User contactGcn) {
        this.userGcn = userGcn;
        this.contactGcn = contactGcn;
    }

    public User getUserGcn() {
        return userGcn;
    }
    public boolean isOfficePhoneNumber2() {
		return officePhoneNumber2;
	}

	public void setOfficePhoneNumber2(boolean officePhoneNumber2) {
		this.officePhoneNumber2 = officePhoneNumber2;
	}

	public boolean isPhoneNumber2() {
		return phoneNumber2;
	}

	public void setPhoneNumber2(boolean phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}
    public void setUserGcn(User userGcn) {
        this.userGcn = userGcn;
    }

    public User getContactGcn() {
        return contactGcn;
    }

    public void setContactGcn(User contactGcn) {
        this.contactGcn = contactGcn;
    }


    public String getProfileFieldsUpdated() {
        return profileFieldsUpdated;
    }

    public void setProfileFieldsUpdated(String profileFieldsUpdated) {
        this.profileFieldsUpdated = profileFieldsUpdated;
    }

    public boolean isPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(boolean phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isEmailId() {
        return emailId;
    }

    public void setEmailId(boolean emailId) {
        this.emailId = emailId;
    }

    public boolean isFacebookId() {
        return facebookId;
    }

    public void setFacebookId(boolean facebookId) {
        this.facebookId = facebookId;
    }

    public boolean isTwitterId() {
        return twitterId;
    }

    public void setTwitterId(boolean twitterId) {
        this.twitterId = twitterId;
    }

    public boolean isResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(boolean residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public boolean isLinkedinId() {
        return linkedinId;
    }

    public void setLinkedinId(boolean linkedinId) {
        this.linkedinId = linkedinId;
    }

    public boolean isDesignation() {
        return designation;
    }

    public void setDesignation(boolean designation) {
        this.designation = designation;
    }

    public boolean isOfficeName() {
        return officeName;
    }

    public void setOfficeName(boolean officeName) {
        this.officeName = officeName;
    }

    public boolean isOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(boolean officeAddress) {
        this.officeAddress = officeAddress;
    }

    public boolean isOfficePhoneNumber() {
        return officePhoneNumber;
    }

    public void setOfficePhoneNumber(boolean officePhoneNumber) {
        this.officePhoneNumber = officePhoneNumber;
    }

    public boolean isOfficeEmailId() {
        return officeEmailId;
    }

    public void setOfficeEmailId(boolean officeEmailId) {
        this.officeEmailId = officeEmailId;
    }
}
