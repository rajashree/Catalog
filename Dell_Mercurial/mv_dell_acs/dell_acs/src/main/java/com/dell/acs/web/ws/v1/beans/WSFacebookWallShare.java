package com.dell.acs.web.ws.v1.beans;

import com.sourcen.core.web.ws.beans.base.WSBeanModel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/27/12
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class WSFacebookWallShare extends WSBeanModel {

    private String fbUID;


    private String emailID;

    /**
     * Naming it as contentID. Today this is productID. Later we could use anything in this place.
     */

    private String contentID;


    private Integer notification;


    private String couponCode;


    private String fbAuthStatus;

    private Date createdDate;


    private Date modifiedDate;

    public String getFbUID() {
        return fbUID;
    }

    public void setFbUID(String fbUID) {
        this.fbUID = fbUID;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public Integer getNotification() {
        return notification;
    }

    public void setNotification(Integer notification) {
        this.notification = notification;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getFbAuthStatus() {
        return fbAuthStatus;
    }

    public void setFbAuthStatus(String fbAuthStatus) {
        this.fbAuthStatus = fbAuthStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
