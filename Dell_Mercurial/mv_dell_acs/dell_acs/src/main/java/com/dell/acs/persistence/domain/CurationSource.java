/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.dell.acs.content.EntityConstants;
import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.ThreadLockAware;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;
import com.sourcen.core.util.collections.PropertiesProvider;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

/**
 @author Navin Raj Kumar G.S. */
@Table(name = "t_curation_source")
@Scopes({@Scope(name = "search", fields = {"id", "name", "sourceType"}),
        @Scope(name = "default",
                fields = {"id", "name", "value", "description", "sourceType", "rssurl", "rssname","username","listname","hashtag","keyword","channel","page","facebookName"}),
        @Scope(name = "minimal", fields = {"id", "name", "value", "sourceType"})
})
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CurationSource extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long>,
        ThreadLockAware {

    @Column(nullable = false, length = 100)
    private String name;

    @Transient
    private String username;

    @Transient
    private String rssurl;

    @Transient
    private String listname;

    @Transient
    private String hashtag;

    @Transient
    private String keyword;

    @Transient
    private String channel;


    @Transient
    private String page;

    @Transient
    private String rssname;

    @Transient
    private String facebookName;


    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 4)
    private Integer sourceType;

    @Column(nullable = false)
    private Integer limit;

    @Column(nullable = false)
    private Integer hashCode;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User modifiedBy;

    @Embedded
    private PropertiesAware properties;

    /// execution fields.

    @Column(length = 3)
    private Integer executionStatus = EntityConstants.ExecutionStatus.IN_QUEUE.getId();

    @Column(nullable = true)
    private String lockedThread;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date executionStartTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date lastUpdatedTime;

    public CurationSource() {
    }

    public CurationSource(final String name, final Integer sourceType, final User createdBy) {
        this.name = name;
        this.description = "";
        this.sourceType = sourceType;
        this.limit = 500;
        this.createdDate = new Date();
        this.modifiedDate = new Date();
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }

    public Integer getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(final Integer executionStatus) {
        this.executionStatus = executionStatus;
    }

    public String getLockedThread() {
        return lockedThread;
    }

    public void setLockedThread(final String lockedThread) {
        this.lockedThread = lockedThread;
    }

    public Date getExecutionStartTime() {
        return executionStartTime;
    }

    public void setExecutionStartTime(final Date executionStartTime) {
        this.executionStartTime = executionStartTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(final Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Integer getHashCode() {
        return hashCode;
    }

    public void setHashCode(final Integer hashCode) {
        this.hashCode = hashCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(final Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(final Integer limit) {
        this.limit = limit;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getRssurl() {
        return rssurl;
    }

    public void setRssurl(final String rssurl) {
        this.rssurl = rssurl;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(final String listname) {
        this.listname = listname;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(final String hashtag) {
        this.hashtag = hashtag;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(final String keyword) {
        this.keyword = keyword;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(final String channel) {
        this.channel = channel;
    }

    public String getPage() {
        return page;
    }

    public void setPage(final String page) {
        this.page = page;
    }

    public String getRssname() {
        return rssname;
    }

    public void setRssname(final String rssname) {
        this.rssname = rssname;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(final String facebookName) {
        this.facebookName = facebookName;
    }

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }

    public void setProperties(final PropertiesAware properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "CurationSource{" +
                "name='" + name + '\'' +
                ", sourceType=" + sourceType +
                ", properties=" + properties +
                '}';
    }


}
