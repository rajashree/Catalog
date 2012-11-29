package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.collections.PropertiesProvider;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/27/12
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
//sfisk - CS-380
@Table(name = "t_facebook_wall_share")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FacebookWallShare extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long> {


    @Column(nullable = false)
    private String fbUID;

    @Column(nullable = false)
    private String emailID;

    /**
     * Naming it as contentID. Today this is productID. Later we could use anything in this place.
     */
    @Column(nullable = false)
    private String contentID;

    @Column
    private Integer notification;

    @Column(nullable = false)
    private String couponCode;

    @Column(nullable = false)
    private String fbAuthStatus;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Column
    private User createdBy;

    @Column
    private User modifiedBy;


    public FacebookWallShare() {

    }

    @Embedded
    private PropertiesAware properties;

    /**
     * @return a {@link com.sourcen.core.util.collections.PropertiesProvider} implementation that stores entity specific properties.
     */
    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }


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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
