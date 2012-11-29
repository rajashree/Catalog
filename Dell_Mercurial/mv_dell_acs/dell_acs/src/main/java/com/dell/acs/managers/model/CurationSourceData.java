package com.dell.acs.managers.model;

import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.util.collections.PropertiesProvider;

import java.util.Date;

/**
 Created by IntelliJ IDEA. User: Mahalakshmi Date: 7/19/12 Time: 11:59 AM To change this template use File | Settings |
 File Templates.
 */
public class CurationSourceData extends PropertiesAwareData{

    private Long id;

    private CurationData curation;

    private String name;

    private String description;

    private Integer sourceType;

    private Integer limit;

    private Date modifiedDate;

    private Boolean active;

    private UserData createdBy;

    private UserData modifiedBy;

    private Long version;

    private Date createdDate;

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

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public CurationData getCurationData() {
        return curation;
    }

    public void setCurationData(final CurationData curationData) {
        this.curation = curationData;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }


     public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(final Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public CurationData getCuration() {
        return curation;
    }
}
