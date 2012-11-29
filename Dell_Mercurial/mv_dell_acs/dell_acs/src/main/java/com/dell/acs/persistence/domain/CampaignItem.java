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

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;


/**
 * @author Samee K.S
 */
//sfisk - CS-380
@Table(name = "t_campaign_items")
@Entity
@Scopes({
        @Scope( name="default", fields = {"id","itemType","product","event","document"}),
        @Scope( name="minimal", fields = {"id","itemType","product","event","document"})
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CampaignItem extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long> {

    public CampaignItem() {
    }

    public CampaignItem(Long itemID, Type itemType) {
        this.itemID = itemID;
        this.itemType = itemType;
    }

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Campaign.class, optional = true)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private Campaign campaign;

    /**
     * itemID can be any of the Products, Videos and White paper id's
     */
    @Column
    private Long itemID;

    /**
     * Type can be Products, Videos and White paper
     *
     * @see CampaignItem.Type
     */
    @Column
    private Type itemType;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, optional = true)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, optional = true)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private User modifiedBy;

    @Column
    private Integer priority;

    @Embedded
    private PropertiesAware properties;

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }

    @Transient
    private Product product;

    @Transient
    private Event event;

    @Transient
    private Document document;

    @Transient
    private WhitePaper whitePaper;

    public static enum Type {
        // Please do not change the order. Campaign uses the ordinal value for persisting in DB
        PRODUCT("product", 6000), EVENT("event", 3000), DOCUMENT("document", 2000), IMAGE("image", 2001),
        VIDEO("video", 2003), LINK("link", 2004);

        private int entityID;

        private String value;

        private Type(String v, int entityID) {
            this.value = v;
            this.entityID = entityID;
        }

        public String getValue() {
            return value;
        }

        public int getEntityID() {
            return entityID;
        }

        public static int getEntityID(String value) {
            for (Type type : values()) {
                if (value.equalsIgnoreCase(type.name())) {
                    return type.getEntityID();
                }
            }
            throw new IllegalArgumentException("Specified name does not relate to a valid CampaignItem Type");
        }

        public static Type getByValue(String value) {
            for (Type type : values()) {
                if (value.equalsIgnoreCase(type.name())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Specified name does not relate to a valid CampaignItem Type");
        }
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Long getItemID() {
        return itemID;
    }

    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }

    public Type getItemType() {
        return itemType;
    }

    public void setItemType(Type itemType) {
        this.itemType = itemType;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }


    public Product getProduct() {
        /**
         *CS-397:All APIs should not show up disabled products
         */
        //Checking if product is null and is not disabled

        if (product!=null && product.getEnabled() != null && product.getEnabled() == false) {
            return null;
        } else {
            return product;
        }
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public WhitePaper getWhitePaper() {
        return whitePaper;
    }

    public void setWhitePaper(WhitePaper whitePaper) {
        this.whitePaper = whitePaper;
    }

}
