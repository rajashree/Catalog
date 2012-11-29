/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers.model;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

@Deprecated
public class TagData implements FormData {

    private Long id;

    private String name;

    private Integer count;

    private Long retailerSiteID;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getRetailerSiteID() {
        return retailerSiteID;
    }

    public void setRetailerSiteID(Long retailerSiteID) {
        this.retailerSiteID = retailerSiteID;
    }

    @Override
    public String toString() {
        return "TagBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", retailerSiteID=" + retailerSiteID +
                '}';
    }
}
