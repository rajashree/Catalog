/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
@SuppressWarnings("serial")
//sfisk - CS-380
@Table(name = "t_unvalidated_product_images")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UnvalidatedProductImage extends IdentifiableEntityModel<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UnvalidatedProduct product;

    @Column(length = 24, nullable = true)
    private String imageType;

    @Column(length = 255, nullable = true)
    private String imageName;

    @Column(length = 1000, nullable = true)
    private String imageURL;

    private boolean imageURLExists;

    @Column(length = 1000, nullable = false)
    private String srcImageURL;


    @Column(nullable = true)
    private Integer cached=0;

    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int retryCount; 

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = DataFile.class)
    private DataFile dataFile;
    
    @Column(nullable = true) // The identifier of the product image being updated else null if insert
    private Long updateProductImageId; 

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

    public UnvalidatedProduct getProduct() {
        return product;
    }

    public void setProduct(UnvalidatedProduct product) {
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

    public Long getUpdateProductImageId() {
    	return this.updateProductImageId;
    }

    public void setUpdateProductImageId(Long pUpdateProductImageId) {
    	this.updateProductImageId = pUpdateProductImageId;
    }
    
    public boolean getImageURLExists() {
    	return this.imageURLExists;
    }

    public void setImageURLExists(boolean pImageURLExists) {
    	this.imageURLExists = pImageURLExists;
    }

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public DataFile getDataFile() {
		return dataFile;
	}

	public void setDataFile(DataFile dataFile) {
		this.dataFile = dataFile;
	}
}
