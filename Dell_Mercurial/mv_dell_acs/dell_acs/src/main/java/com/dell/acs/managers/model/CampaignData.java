/*
 * Copyright (c) SourceN Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers.model;

import com.dell.acs.persistence.domain.CampaignCategory;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 @author Samee K.S
 @author $: sameeks $
 @version : 0 $,  :: 2012-03-07 09:56:40#$ */

public class CampaignData implements FormData {

    private String name;

    private String type;

    private Date startDate;

    private Date endDate;

    private Boolean enabled;

    private Boolean packageType;

    private RetailerSite retailerSite;

    private Set<CampaignItemData> items;

    private String thumbnail;

    private String thumbnailType;

    private String campaignItems;

    private String itemReviews;

    private Collection<Product> products = new ArrayList<Product>();

    private PropertiesAware properties;

    // For recommendation api call we need this map
    private Map<CampaignCategory, List<Object>> data = new HashMap<CampaignCategory, List<Object>>();


    /* Audit fields */
    private Date creationDate;

    private Date modifiedDate;

    private User createdBy;

    private User modifiedBy;


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Boolean getPackageType() {
        return packageType;
    }

    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public Set<CampaignItemData> getItems() {
        return items;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getThumbnailType() {
        return thumbnailType;
    }

    public String getCampaignItems() {
        return campaignItems;
    }

    public String getItemReviews() {
        return itemReviews;
    }

    public Collection<Product> getProducts() {
        return products;
    }

    public PropertiesAware getProperties() {
        return properties;
    }

    public Map<CampaignCategory, List<Object>> getData() {
        return data;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setPackageType(Boolean packageType) {
        this.packageType = packageType;
    }

    public void setRetailerSite(RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    public void setItems(Set<CampaignItemData> items) {
        this.items = items;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setThumbnailType(String thumbnailType) {
        this.thumbnailType = thumbnailType;
    }

    public void setCampaignItems(String campaignItems) {
        this.campaignItems = campaignItems;
    }

    public void setItemReviews(String itemReviews) {
        this.itemReviews = itemReviews;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

    public void setProperties(PropertiesAware properties) {
        this.properties = properties;
    }

    public void setData(Map<CampaignCategory, List<Object>> data) {
        this.data = data;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
