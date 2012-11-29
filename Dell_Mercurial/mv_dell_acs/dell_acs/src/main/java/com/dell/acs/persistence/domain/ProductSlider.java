/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
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
@Table(name = "t_product_slider")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Scopes({
        @Scope(name = "default", fields = {"id","sourceProduct","targetProduct","targetURL","title"}),
        @Scope(name = "minimal", fields = {"targetProduct"})
})
public class ProductSlider extends IdentifiableEntityModel<Long> {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private RetailerSite retailerSite;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private Product sourceProduct;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private Product targetProduct;

    @Column(length = 1000, nullable = true)
    private String targetURL;

    @Column(length = 255, nullable = true)
    private String title;

    public ProductSlider() {
    }


    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    public Product getSourceProduct() {
        return sourceProduct;
    }

    public void setSourceProduct(Product sourceProduct) {
        this.sourceProduct = sourceProduct;
    }

    public Product getTargetProduct() {
        return targetProduct;
    }

    public void setTargetProduct(Product targetProduct) {
        this.targetProduct = targetProduct;
    }

    public String getTargetURL() {
        return targetURL;
    }

    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
