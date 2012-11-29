/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.dell.acs.content.EntityConstants;
import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sandeep
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3769 $, $Date:: 2012-07-02 15:44:51#$
 */

@javax.persistence.Table(name = "t_curation_content")
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Scopes({
        @Scope(name = "default", fields = {"id","contentType","library","feed","categoryID","favorite","sticky","position"}),
        @Scope(name = "minimal", fields = {"id", "library","feed","favorite","sticky","position"})
})
public class CurationContent extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long> {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Curation curation;

    @Column(name = "cacheContent_id", nullable = false)
    private long cacheContent;

    @Column(nullable = false)
    private Boolean sticky;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private Boolean favorite;

    @Column(nullable = false)
    private Integer type;

    @Column(nullable = false)
    private Long categoryID;

    @Column(nullable = true)
    private Boolean edited;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, optional = true)
    @Fetch(FetchMode.SELECT)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, optional = true)
    @Fetch(FetchMode.SELECT)
    private User modifiedBy;

    @Column(nullable = false)
    private Integer position;

    @Transient
    private String contentType;

    @Transient
    private Document library;

    @Transient
    private CurationCache feed;



    public String getContentType() {
        return EntityConstants.CurationSourceType.getById(this.type).name();
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Embedded
    private PropertiesAware properties;

    public CurationContent(){

    }

    public CurationContent(Curation curation, long cacheContent, User user){
        this.curation = curation;
        this.cacheContent = cacheContent;
    }

    public Curation getCuration() {
        return curation;
    }

    public void setCuration(final Curation curation) {
        this.curation = curation;
    }

    public long getCacheContent() {
        return cacheContent;
    }

    public void setCacheContent(long cacheContent) {
        this.cacheContent = cacheContent;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(final Date modifiedDate) {
        this.modifiedDate = modifiedDate;
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

    public Document getLibrary() {
        return library;
    }

    public void setLibrary(Document library) {
        this.library = library;
    }

    public CurationCache getFeed() {
        return feed;
    }

    public void setFeed(CurationCache feed) {
        this.feed = feed;
    }

    public Boolean getSticky() {
        return sticky;
    }

    public void setSticky(Boolean sticky) {
        this.sticky = sticky;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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
}
