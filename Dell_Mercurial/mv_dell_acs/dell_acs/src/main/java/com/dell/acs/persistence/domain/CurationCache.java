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
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

/** @author Navin Raj Kumar G.S. */
@javax.persistence.Table(name = "t_curation_cache",
        uniqueConstraints = {@UniqueConstraint(name = "curation_key",
                columnNames = {"curationSource_id", "guid"})})
@Table(appliesTo = "t_curation_cache",
        indexes = {@Index(name = "idx_curationSource", columnNames = {"curationSource_id"}),
                @Index(name = "idx_guid", columnNames = {"guid"})})
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Scopes({
        @Scope(name = "minimal", fields = {"id", "title", "publishedDate", "updatedDate", "sourceType"}),
        @Scope(name = "default", fields = {"id", "title", "description", "body", "guid", "source", "status", "publishedDate", "updatedDate", "sourceType" })
})
public class CurationCache extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long> {


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private CurationSource curationSource;

    // twitter is an ID, RSS the guid is a lengthy URL string.
    @Column(unique = true, nullable = false, length = 300)
    private String guid;

    @Column(nullable = false, length = 4000)
    private String title;

    // a link to either the source or an attachment like video or image.
    @Column(nullable = true, length = 2000)
    private String link;

    @Column(nullable = true, length = Integer.MAX_VALUE)
    private String description;

    // should be a longtext or varchar
    // can be nullable if the body has not be generated yet.
    // in RSS feeds, this would be the <content> tag.
    @Column(nullable = true, length = Integer.MAX_VALUE)
    private String body;

    /*
     status column is for the status of
      curation Cache l
    * */
    @Column(nullable = false)
    private Integer status = EntityConstants.Status.PUBLISHED.getId();

    // this will contain the actual source of that we can use later.
    // json or RSS entry, this can be used in the future to
    // construct/re-construct the body in any form if needed.
    // should be a longtext or varchar
    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String source;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date publishedDate;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date importedDate;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Embedded
    private PropertiesAware properties;


    @Transient
    private String sourceType;

    //
    //                             End fields.
    //

    public CurationSource getCurationSource() {
        return curationSource;
    }

    public void setCurationSource(final CurationSource curationSource) {
        this.curationSource = curationSource;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(final String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(final Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Date getImportedDate() {
        return importedDate;
    }

    public void setImportedDate(final Date importedDate) {
        this.importedDate = importedDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(final Date updatedDate) {
        this.updatedDate = updatedDate;
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

    public String getSourceType() {
        String type = EntityConstants.CurationSourceType.getById(curationSource.getSourceType()).name();
        return type;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
