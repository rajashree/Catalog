/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;


import com.sourcen.core.web.ws.beans.base.WSPropertiesAwareBeanModel;

/**
 * Created with IntelliJ IDEA.
 * User: Chethan
 * Date: 4/24/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class WSTaxonomyCategory extends WSPropertiesAwareBeanModel {

    Long id;
    String name;
    private WSTaxonomyCategory parent;

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

    public WSTaxonomyCategory getParent() {
        return parent;
    }

    public void setParent(WSTaxonomyCategory parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "WSTaxonomyCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                '}';
    }
}
