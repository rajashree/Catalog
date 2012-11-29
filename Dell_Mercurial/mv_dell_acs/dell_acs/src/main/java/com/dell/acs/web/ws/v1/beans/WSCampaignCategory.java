/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;


import com.sourcen.core.util.beans.FieldMapping;
import com.sourcen.core.web.ws.beans.base.WSPropertiesAwareBeanModel;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Chethan
 * Date: 3/28/12
 * Time: 8:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class WSCampaignCategory extends WSPropertiesAwareBeanModel {

    Long id;
    @FieldMapping("parent.id")
    Long parent;
    String name;
    String thumbnail;
    Collection<WSCampaignCategory> children;
    Collection<WSCampaignItem> items;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(final Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(final String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Collection<WSCampaignCategory> getChildren() {
        return children;
    }

    public void setChildren(final Collection<WSCampaignCategory> children) {
        this.children = children;
    }

    public Collection<WSCampaignItem> getItems() {
        return items;
    }

    public void setItems(final Collection<WSCampaignItem> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        return "WSCampaignCategory{"
                + "children=" + children
                + ", items=" + items
                + ", name='" + name + '\''
                + ", thumbnail='" + thumbnail + '\''
                + '}';
    }
}
