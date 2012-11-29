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
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 */
//sfisk - CS-380
@Table(name = "t_retailer_sites")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Scopes({
        @Scope(name = "default", fields = {"id", "siteName","siteURL","logoUri"}),
        @Scope(name = "minimal", fields = {"siteName"})
})
public class RetailerSite extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long> {

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @Scope(name = "id")
    private Retailer retailer;

    @Column(length = 100, nullable = false)
    private String siteName;

    @Column(length = 255, nullable = false)
    private String siteUrl;

    @Column(length = 255, nullable = true)
    private String logoUri;

    @Transient
    private EntityConstants.EnumPixelTracker enumPixelTracker;

    @Transient
    private EntityConstants.EnumDataImport enumDataImport;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Embedded
    private PropertiesAware properties;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private Boolean active;

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
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

    public EntityConstants.EnumPixelTracker getEnumPixelTracker() {
        return enumPixelTracker;
    }

    public void setEnumPixelTracker(EntityConstants.EnumPixelTracker enumPixelTracker) {
        this.enumPixelTracker = enumPixelTracker;
    }

    public EntityConstants.EnumDataImport getEnumDataImport() {
        return enumDataImport;
    }

    public void setEnumDataImport(EntityConstants.EnumDataImport enumDataImport) {
        this.enumDataImport = enumDataImport;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "RetailerSite{" + "createdDate=" + createdDate + ", logoUri='" + logoUri + '\'' + ", modifiedDate=" +
                modifiedDate + ", properties=" + properties + ", retailer=" + retailer + ", siteName='" + siteName +
                '\'' + ", siteUrl='" + siteUrl + '\'' + '}';
    }
}
