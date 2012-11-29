/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */
@Table(name = "t_tags")
@Entity
@Scopes({
        @Scope(name = "default", fields = {"id", "name"})
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tag extends IdentifiableEntityModel<Long> {

    @Column(length = 255, nullable = false)
    private String name;

    private Integer count; // Will be useful for tag statistics

    @ManyToOne(fetch = FetchType.LAZY)
    private RetailerSite retailerSite;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    public Tag(){ }

    public Tag(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        // Inject the tag manager and calculate the tag usage count
        return count;
    }

    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
