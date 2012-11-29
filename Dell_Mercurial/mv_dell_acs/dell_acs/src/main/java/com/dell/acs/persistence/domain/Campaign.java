/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.*;

/**
 * @author Samee K.S
 * @author $LastChangedBy: sameeks $
 * @version $Revision: 2989 $, $Date:: 2012-06-07 12:34:33#$
 */

//sfisk - CS-380
@Table(name = "t_campaigns")
@Entity
@Scopes({
        @Scope( name="default", fields = {"name","thumbnail","enabled","startDate","endDate","categories","properties"}),
        @Scope( name="minimal", fields = {"name","thumbnail","enabled","startDate","endDate","categories"}),
        @Scope( name="basic", fields = {"name","thumbnail","enabled","startDate","endDate"})
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Campaign extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long> {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 255, nullable = true)
    private Type type;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startDate;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endDate;

    @Column
    private Boolean enabled;

    @Transient
    private Boolean packageType;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private RetailerSite retailerSite;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "campaign")
    private Set<CampaignItem> items;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "campaign")
    @OrderBy(value = "parent, position")
    private Set<CampaignCategory> categories;


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, optional = true)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, optional = true)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private User modifiedBy;

    @Transient
    private String thumbnail;

    @Transient
    private String thumbnailType;

    @Transient
    private Collection<Product> products = new ArrayList<Product>();

    @Transient
    private Map<CampaignCategory, List<Object>> data = new HashMap<CampaignCategory, List<Object>>();

    @Embedded
    private PropertiesAware properties;

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }


    public static enum Type {
        HOTDEALS("hotdeals"), RECOMMENDS("recommends");

        private String value;

        private Type(String v) {
            this.value = v;
        }

        public String getValue() {
            return value;
        }

        public static Type getByValue(String value) {
            for (Type type : values()) {
                if (value.equalsIgnoreCase(type.name())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Specified name does not relate to a valid Campaign Type");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
        return enabled != null ? enabled : false;
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

    public Set<CampaignItem> getItems() {
        return items;
    }

    public void setItems(Set<CampaignItem> items) {
        this.items = items;
    }

    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    public Collection<Product> getProducts() {
        return products;
    }

    public Map<CampaignCategory, List<Object>> getData() {
        return data;
    }

    public void setData(Map<CampaignCategory, List<Object>> data) {
        this.data = data;
    }

    public Set<CampaignCategory> getCategories() {
        return getCategories(false);
    }

    public Set<CampaignCategory> getCategories(Boolean getAll) {
        if(categories == null){
            categories = new HashSet<CampaignCategory>(1);
        }
        if (getAll) {
            return categories;
        }
        Set topLevelCategories = new HashSet();
        for (CampaignCategory category : categories) {
            if (category.getParent() == null) {
                topLevelCategories.add(category);
            }
        }
        return topLevelCategories;
    }

    public void setCategories(Set<CampaignCategory> categories) {
        this.categories = categories;
    }
}
