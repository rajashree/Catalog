/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 */
//sfisk - CS-380
@Table(name = "t_taxonomy")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Scopes({
        @Scope(name = "minimal", fields = {"id", "name"})
})
public class Taxonomy extends IdentifiableEntityModel<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @Scope(name = "id")
    private RetailerSite retailerSite;

    @Column(length = 100)
    private String name;

    // default taxonomy type is for product entity
    @Column(columnDefinition = "int default 6000")
    private Integer type;

    public Taxonomy() {
    }

    public Taxonomy(RetailerSite retailerSite, String name, Integer type) {
        this.retailerSite = retailerSite;
        this.name = name;
        this.type = type;
    }

    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
