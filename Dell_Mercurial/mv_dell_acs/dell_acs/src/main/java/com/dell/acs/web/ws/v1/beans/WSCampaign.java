/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.ScopeAware;
import com.sourcen.core.util.beans.Scopes;
import com.sourcen.core.web.ws.beans.base.WSPropertiesAwareBeanModel;

import java.util.Collection;
import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: vivekk $
 * @version $Revision: 3145 $, $Date:: 2012-06-13 12:08:38#$
 */
@Scopes({
        @Scope(name = "minimal", fields = {"id", "name", "enabled", "thumbnail", "startDate", "endDate"})
})
public class WSCampaign extends WSPropertiesAwareBeanModel implements ScopeAware {

    Long id;
    String name;
    //Type type;
    Date startDate;
    Date endDate;
    Boolean enabled;
    Boolean packageType;
    Date creationDate;
    Date modifiedDate;
    WSRetailerSite retailerSite;
    //Collection<WSCampaignItem> items;
    Collection<WSCampaignCategory> categories;
    //User createdBy;
    //User modifiedBy;
    String thumbnail;
    String thumbnailType;
    //Collection<WSProduct> products = new ArrayList<WSProduct>();
    //Map<WSCampaignCategory, List<Object>> data = new HashMap<WSCampaignCategory, List<Object>>();


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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getPackageType() {
        return packageType;
    }

    public void setPackageType(Boolean packageType) {
        this.packageType = packageType;
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

    public WSRetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(WSRetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    /* public Collection<WSCampaignItem> getItems() {
        return items;
    }

    public void setItems(Collection<WSCampaignItem> items) {
        this.items = items;
    }*/

    public Collection<WSCampaignCategory> getCategories() {
        return categories;
    }

    public void setCategories(Collection<WSCampaignCategory> categories) {
        this.categories = categories;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailType() {
        return thumbnailType;
    }

    public void setThumbnailType(String thumbnailType) {
        this.thumbnailType = thumbnailType;
    }

    @Override
    public String toString() {
        return "WSCampaign{" +
                " id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", enabled=" + enabled +
                ", packageType=" + packageType +
                ", creationDate=" + creationDate +
                ", modifiedDate=" + modifiedDate +
                ", retailerSite=" + retailerSite +
                /*", items=" + items +*/
                ", categories=" + categories +
                ", thumbnail='" + thumbnail + '\'' +
                ", thumbnailType='" + thumbnailType + '\'' +
                '}';
    }
}
