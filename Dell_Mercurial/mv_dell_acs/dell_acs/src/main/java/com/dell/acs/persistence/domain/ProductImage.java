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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 3794 $, $Date:: 2012-07-03 13:01:57#$
 */
//sfisk - CS-380
@Table(name = "t_product_images")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Scopes({
                @Scope(name = "minimal", fields = {"imageURL"}),
                @Scope(name = "default", fields = {"id","imageType","imageName","imageURL"})
        })
public class ProductImage extends IdentifiableEntityModel<Long> {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Product.class, optional = false)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private Product product;

    @Column(length = 24, nullable = true)
    private String imageType;

    @Column(length = 255, nullable = true)
    private String imageName;

    @Column(length = 1000, nullable = true)
    private String imageURL;

    @Column(length = 1000, nullable = false)
    private String srcImageURL;


    @Column(nullable = true)
    private Integer cached=0;

    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;


    public Integer getCached() {
        return cached;
    }

    public void setCached(Integer cached) {
        this.cached = cached;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(final Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSrcImageURL() {
        return srcImageURL;
    }

    public void setSrcImageURL(String srcImageURL) {
        this.srcImageURL = srcImageURL;
    }


}
