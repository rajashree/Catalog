package com.dell.acs.web.controller.formbeans;

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
public class DocumentBean extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long>, FormBean {

    @NotEmpty(message = "Name field is mandatory")
    private String name;

    private String image;


    private String document;

    @NotEmpty(message = "Decription field is mandatory")
    @Length(max = 1000, message = "Description exceeds the maxlength")
    private String description;


    private Date startDate;
    private Date endDate;

    private RetailerSite retailerSite;

    private PropertiesAware properties;

    // Audit info
    private Date creationDate;

    private Date modifiedDate;

    private User createdBy;

    private User modifiedBy;

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
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

    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(final RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
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
}
