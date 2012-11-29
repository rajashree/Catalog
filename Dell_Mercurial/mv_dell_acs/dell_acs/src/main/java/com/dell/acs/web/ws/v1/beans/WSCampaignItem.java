/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import com.sourcen.core.util.beans.FieldMapping;
import com.sourcen.core.web.ws.beans.base.WSPropertiesAwareBeanModel;

import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class WSCampaignItem extends WSPropertiesAwareBeanModel {

    Long id;
    @FieldMapping("campaign.id")
    Long campaignId;
    Date creationDate;
    Date modifiedDate;
    @FieldMapping("priority")
    Integer order;
    @FieldMapping("product")
    WSProduct product;
    WSEvent event;
    WSDocument document;


    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(final Long campaignId) {
        this.campaignId = campaignId;
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(final Integer order) {
        this.order = order;
    }

    public WSProduct getProduct() {
        return product;
    }

    public void setProduct(final WSProduct product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WSEvent getEvent() {
        return event;
    }

    public void setEvent(WSEvent event) {
        this.event = event;
    }

    public WSDocument getDocument() {
        return document;
    }

    public void setDocument(WSDocument document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "WSCampaignItem{" +
                "id=" + id +
                ", campaignId=" + campaignId +
                ", creationDate=" + creationDate +
                ", modifiedDate=" + modifiedDate +
                ", order=" + order +
                ", product=" + product +
                '}';
    }
}
