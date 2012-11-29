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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Samee K.S
 * @author sameeks: svnName $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

//sfisk - CS-380
@Table(name = "t_documents")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Scopes({

        @Scope(name = "default", fields = {"id", "name", "contentType", "image", "description", "document", "startDate",
                "endDate", "body", "url", "modifiedBy", "modifiedDate","publishDate","author","source","abstractText","tags"}),

        @Scope(name = "minimal", fields = {"id", "name", "image", "contentType" , "document" , "url","tags"})
})
public class Document extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long> {

    @Column(nullable = false)
    private String name;

    // CDN path for file
    @Column
    private String image;

    @Column
    private String document;

    @Column(nullable = true)
    private String description;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endDate;

    // Audit info
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, optional = true)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, optional = true)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private User modifiedBy;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = RetailerSite.class, optional = true)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private RetailerSite retailerSite;

    @Column(nullable = false, length = 4, columnDefinition = "int default 2000")
    private Integer type;

    @Column(length = 8000)
    private String body;

    @Column
    private String url;

    @Column(columnDefinition = "int default 1")
    private Integer status;

    /*Newly Added column according sprint 6 requirement*/

    @Column(length = Integer.MAX_VALUE)
    private String author;

    @Column(length = Integer.MAX_VALUE)
    private String source;

    @Column(length = Integer.MAX_VALUE)
    private String abstractText;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date publishDate;


    @Embedded
    private PropertiesAware properties;

    @Transient
    private String tags;

    @Transient
    private String contentType;

    public String getContentType() {
        return EntityConstants.Entities.getById(this.type).name();
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(final String tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(final String document) {
        this.document = document;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }

    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    public Integer getType() {
        return type;
    }

    public void setType(final Integer type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(final String abstractText) {
        this.abstractText = abstractText;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(final Date publishDate) {
        this.publishDate = publishDate;
    }
}
