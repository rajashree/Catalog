package com.dell.acs.managers.model;

import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.util.collections.PropertiesProvider;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $, $Date:: 7/19/12 2:05 PM#$ */
public class CurationData extends PropertiesAwareData {

    private Long id;


    private RetailerSiteData retailerSite;


    private String name;


    private String description;


    private Date createdDate;


    private Date modifiedDate;


    private Boolean active;


    private UserData createdBy;


    private UserData modifiedBy;

   // private PropertiesAware properties;

    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public RetailerSiteData getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(final RetailerSiteData retailerSite) {
        this.retailerSite = retailerSite;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public UserData getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final UserData createdBy) {
        this.createdBy = createdBy;
    }

    public UserData getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final UserData modifiedBy) {
        this.modifiedBy = modifiedBy;
    }


/*    public PropertiesProvider getProperties() {
        if(properties==null){
               properties=new PropertiesAware();
        }
        return this.properties.getProperties();
    }

    public void setProperties(final PropertiesAware properties) {
        this.properties = properties;
    }*/

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }
}
