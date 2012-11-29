/*
 * Copyright (c) SourceN Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers.model;

import com.dell.acs.persistence.domain.CampaignCategory;
import com.dell.acs.persistence.domain.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 @author Samee K.S
 @author $: sameeks $
 @version : 0 $,  :: 2012-03-07 09:56:40#$ */

public class CampaignItemCategoryData implements FormData {

    private Long id;

    private String name;

    private CampaignItemCategoryData parent;

    // For recommendation api call we need this map
    private Map<CampaignCategory, List<Object>> data = new HashMap<CampaignCategory, List<Object>>();

    /* Audit fields */
    private Date creationDate;

    private Date modifiedDate;

    private User createdBy;

    private User modifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CampaignItemCategoryData getParent() {
        return parent;
    }

    public void setParent(CampaignItemCategoryData parent) {
        this.parent = parent;
    }

    public Map<CampaignCategory, List<Object>> getData() {
        return data;
    }

    public void setData(Map<CampaignCategory, List<Object>> data) {
        this.data = data;
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
}
