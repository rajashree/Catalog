/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/** @author Navin Raj Kumar G.S. */
@Table(name = "t_curation")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Scopes({
        @Scope(name = "default", fields = {"id", "name", "description"}),
        @Scope(name = "minimal", fields = {"id", "name"})
})
public class Curation extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long> {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @Scope(name = "id")
    private RetailerSite retailerSite;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @Fetch(FetchMode.SELECT)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @Fetch(FetchMode.SELECT)
    private User modifiedBy;

    @Embedded
    private PropertiesAware properties;

    public Curation(){

    }

    public Curation(String name, String description, RetailerSite retailerSite, Boolean active){
        this.name = name;
        this.description = description;
        this.retailerSite = retailerSite;
        this.active = active;
    }

    @Override
    public PropertiesProvider getProperties() {
        if(properties==null){
               properties=new PropertiesAware();
        }
        return this.properties.getProperties();
    }
    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(final RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
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

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
}
