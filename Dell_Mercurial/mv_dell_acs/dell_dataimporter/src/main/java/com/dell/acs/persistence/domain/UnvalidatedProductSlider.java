/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
@SuppressWarnings("serial")
//sfisk - CS-380
@Table(name = "t_unvalidated_product_slider")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UnvalidatedProductSlider extends IdentifiableEntityModel<Long> {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private RetailerSite retailerSite;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private UnvalidatedProduct sourceProduct;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private UnvalidatedProduct targetProduct;

    @Column(length = 1000, nullable = true)
    private String targetURL;

    @Column(length = 255, nullable = true)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = DataFile.class)
    private DataFile dataFile;
    
    @Column(nullable = true) // The identifier of the product slider being updated else null if insert
    private Long updateProductSliderId; 

    public UnvalidatedProductSlider() {
    }


    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    public UnvalidatedProduct getSourceProduct() {
        return sourceProduct;
    }

    public void setSourceProduct(UnvalidatedProduct sourceProduct) {
        this.sourceProduct = sourceProduct;
    }

    public UnvalidatedProduct getTargetProduct() {
        return targetProduct;
    }

    public void setTargetProduct(UnvalidatedProduct targetProduct) {
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

    public Long getUpdateProductSliderId() {
    	return this.updateProductSliderId;
    }

    public void setUpdateProductSliderId(Long pUpdateProductSliderId) {
    	this.updateProductSliderId = pUpdateProductSliderId;
    }


	public DataFile getDataFile() {
		return dataFile;
	}


	public void setDataFile(DataFile dataFile) {
		this.dataFile = dataFile;
	}
}
