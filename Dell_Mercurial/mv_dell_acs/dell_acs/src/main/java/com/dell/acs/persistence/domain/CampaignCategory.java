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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Collection;

/**
 * @author Samee K.S
 * @author : skammar $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */
//sfisk - CS-380
@Table(name = "t_campaign_category")
@Entity
@Scopes({
        @Scope( name = "default", fields = {"id","name","parent","position","children","items"}),
        @Scope( name = "minimal", fields = {"id", "name","children","items"})
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CampaignCategory extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Campaign.class)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private Campaign campaign;

    /**
     * Default is a root category so we set default value as '0'.
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent", columnDefinition = "bigint default null")
    @Scope(name = "id")
    @Fetch(FetchMode.JOIN)
    private CampaignCategory parent;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parent")
    @Fetch(FetchMode.SELECT)
    Collection<CampaignCategory> children;

    @Column
    private String name;

    @Column(columnDefinition = "bigint default 0")
    private Integer position;

    // This detail will be stored in category properties table
    @Transient
    private String thumbnail;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @OrderBy("priority DESC")
    // sfisk - CS-380
    @JoinTable(name = "t_campaign_category_items",
		joinColumns = {
  	      @JoinColumn(name="campaign_category_id", unique = false)           
  	    }
    )
    Collection<CampaignItem> items;


    @Embedded
    private PropertiesAware properties;

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }


    public CampaignCategory getParent() {
        return parent;
    }

    public void setParent(CampaignCategory parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Collection<CampaignCategory> getChildren() {
        return children;
    }

    public void setChildren(Collection<CampaignCategory> children) {
        this.children = children;
    }

    public Collection<CampaignItem> getItems() {
        return items;
    }

    public void setItems(Collection<CampaignItem> items) {
        this.items = items;
    }
}
