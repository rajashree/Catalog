package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.collections.PropertiesProvider;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 * @version $Revision: 3707 $, $Date:: 2012-07-13 2:41 PM#$
 */
@Deprecated
@Table(name = "t_articles")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Article extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long> {

    @Column(nullable = false)
    private String title;

    @Column(length = 255)
    private String description;

    @Column(length = 8000)
    private String body;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User modifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private RetailerSite retailerSite;

    @Embedded
    private PropertiesAware properties;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }
}
