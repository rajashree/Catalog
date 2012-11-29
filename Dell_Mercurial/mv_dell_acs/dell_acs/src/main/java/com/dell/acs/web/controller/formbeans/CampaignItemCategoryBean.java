/*
 * Copyright (c) SourceN Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.formbeans;

import com.dell.acs.managers.model.CampaignItemCategoryData;
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

public class CampaignItemCategoryBean implements FormBean {

    private Long id;

    private String name;

    private CampaignItemCategoryBean parent;

    // For recommendation api call we need this map
    private Map<CampaignCategory, List<Object>> data = new HashMap<CampaignCategory, List<Object>>();

    /* Audit fields */
    private Date creationDate;

    private Date modifiedDate;

    private User createdBy;

    private User modifiedBy;

    public CampaignItemCategoryBean() {
	}

    public CampaignItemCategoryBean(CampaignItemCategoryData category) {
        this.id = category.getId();
        this.name = category.getName();
        this.parent = null;
        if (category.getParent() != null) {
        	this.parent = new CampaignItemCategoryBean(category.getParent());
        }
        this.data = category.getData();
        this.creationDate = category.getCreationDate();
        this.modifiedDate = category.getModifiedDate();
        this.createdBy = category.getCreatedBy();
        this.modifiedBy = category.getModifiedBy();
	}

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

    public CampaignItemCategoryBean getParent() {
        return parent;
    }

    public void setParent(CampaignItemCategoryBean parent) {
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
