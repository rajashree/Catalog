package com.dell.acs.managers.model;

import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $, $Date:: 6/13/12 4:26 PM#$ */
public class DocumentData implements FormData{

    private Long id;

    @NotEmpty(message = "Name field is mandatory")
    private String name;

    private String document;

    @NotEmpty(message = "Description field is mandatory")
    @Length(max = 1000, message = "Description exceeds the max-length")
    private String description;

    private Date startDate;
    private Date endDate;

    private Date modifiedDate;

    private User createdBy;

    private User modifiedBy;

    private String image;

    private RetailerSiteData retailerSite;

    private PropertiesAware properties;

    private Long version;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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

    public void setDescription(final String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

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

    public PropertiesProvider getProperties() {
        if(properties==null){
               properties=new PropertiesAware();
        }
        return this.properties.getProperties();
    }

    public void setProperties(final PropertiesAware properties) {
        this.properties = properties;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(final Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
