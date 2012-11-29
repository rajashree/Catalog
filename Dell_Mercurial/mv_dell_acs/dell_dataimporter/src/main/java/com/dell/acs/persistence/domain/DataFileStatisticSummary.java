/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
@SuppressWarnings("serial")
@Table(name = "t_data_file_stat_sums")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataFileStatisticSummary extends IdentifiableEntityModel<Long> {
	@Column(nullable = false)
	private long dataFile_id;

	@Column(nullable = false)
	private long retailerSite_id;

	@Column(nullable = false, length=1000)
	private String srcFile;

	/**
	 * Stores the total number of imports
	 */
	@Column(nullable = false)
	private int numImports;

	/**
	 * Stores the total number of import errors
	 */
	@Column(nullable = false)
	private int numImportErrors;

	/**
	 * Stores the total number entities updated in the import. 
	 */
	@Column(nullable = false)
	private int numImportUpdates;

	/**
	 * Stores the total number of transfer errors.
	 */
	@Column(nullable = false)
	private int numTransferErrors;

	/**
	 * Stores the total number of validation errors.
	 */
	@Column(nullable = false)
	private int numValidationErrors;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
    private transient long imagesEndTimeCalc = Long.MIN_VALUE;
	private Date imagesEndTime;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
    private transient long imagesStartTimeCalc = Long.MAX_VALUE;
    private Date imagesStartTime;

	/**
	 * Stores the total number of image errors for the row.
	 */
	@Column(nullable = false)
	private int numImageErrors;

	/**
	 * Stores the total number of images need for the row.
	 */
	@Column(nullable = false)
	private int numImages;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
    private transient long importEndTimeCalc = Long.MIN_VALUE;
    private Date importEndTime;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
    private transient long importStartTimeCalc = Long.MAX_VALUE;
    private Date importStartTime;

	/**
	 * Stores the total number of transferred products.
	 */
	@Column(nullable = false)
	private int numTransferProducts;

	/**
	 * Stores the total number of transferred product images.
	 */
	@Column(nullable = false)
	private int numTransferProductImages;

	/**
	 * Stores the total number of transferred product reviews.
	 */
	@Column(nullable = false)
	private int numTransferProductReviews;

	/**
	 * Stores the total number of transferred product sliders.
	 */
	@Column(nullable = false)
	private int numTransferProductSliders;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
    private transient long transferEndTimeCalc = Long.MIN_VALUE;
    private Date transferEndTime;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
    private transient long transferStartTimeCalc = Long.MAX_VALUE;
    private Date transferStartTime;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
    private transient long validationEndTimeCalc = Long.MIN_VALUE;
    private Date validationEndTime;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
    private transient long validationStartTimeCalc = Long.MAX_VALUE;
    private Date validationStartTime;

	public DataFileStatisticSummary() {
	}

	public DataFileStatisticSummary(DataFile pDataFile, String srcFile) {
		this.setDataFile_id(pDataFile != null ? pDataFile.getId() : null);
		this.setSrcFile(srcFile);
		this.setRetailerSite_id(pDataFile.getRetailerSite().getId());
	}

	public long getDataFile_id() {
		return dataFile_id;
	}

	public void setDataFile_id(long dataFile_id) {
		this.dataFile_id = dataFile_id;
	}

	public long getRetailerSite_id() {
		return retailerSite_id;
	}

	public void setRetailerSite_id(long retailerSite_id) {
		this.retailerSite_id = retailerSite_id;
	}

	public String getSrcFile() {
		return srcFile;
	}

	public void setSrcFile(String srcFile) {
		this.srcFile = srcFile;
	}

	public int getNumImports() {
		return numImports;
	}

	public void setNumImports(int numImports) {
		this.numImports = numImports;
	}

	public int getNumImportErrors() {
		return numImportErrors;
	}

	public void setNumImportErrors(int numImportErrors) {
		this.numImportErrors = numImportErrors;
	}

	public int getNumImportUpdates() {
		return numImportUpdates;
	}

	public void setNumImportUpdates(int numImportUpdates) {
		this.numImportUpdates = numImportUpdates;
	}

	public int getNumTransferErrors() {
		return numTransferErrors;
	}

	public void setNumTransferErrors(int numTransferErrors) {
		this.numTransferErrors = numTransferErrors;
	}

	public int getNumValidationErrors() {
		return numValidationErrors;
	}

	public void setNumValidationErrors(int numValidationErrors) {
		this.numValidationErrors = numValidationErrors;
	}

	public Date getImagesEndTime() {
		return imagesEndTime;
	}

	public void setImagesEndTime(Date imagesEndTime) {
		this.imagesEndTime = imagesEndTime;
        this.imagesEndTimeCalc = this.imagesEndTime != null ? this.imagesEndTime.getTime() : Long.MAX_VALUE;
	}

	public Date getImagesStartTime() {
		return imagesStartTime;
	}

	public void setImagesStartTime(Date imagesStartTime) {
		this.imagesStartTime = imagesStartTime;
        this.imagesStartTimeCalc = this.imagesStartTime != null ? this.imagesStartTime.getTime() : Long.MIN_VALUE;
    }

	public int getNumImageErrors() {
		return numImageErrors;
	}

	public void setNumImageErrors(int numImageErrors) {
		this.numImageErrors = numImageErrors;
	}

	public int getNumImages() {
		return numImages;
	}

	public void setNumImages(int numImages) {
		this.numImages = numImages;
	}

	public Date getImportEndTime() {
		return importEndTime;
	}

	public void setImportEndTime(Date importEndTime) {
		this.importEndTime = importEndTime;
        this.importEndTimeCalc = this.importEndTime != null ? this.importEndTime.getTime() : Long.MIN_VALUE;
    }

	public Date getImportStartTime() {
		return importStartTime;
	}

	public void setImportStartTime(Date importStartTime) {
		this.importStartTime = importStartTime;
        this.importStartTimeCalc = this.importStartTime != null ? this.importStartTime.getTime() : Long.MIN_VALUE;
    }

	public int getNumTransferProducts() {
		return numTransferProducts;
	}

	public void setNumTransferProducts(int numTransferProducts) {
		this.numTransferProducts = numTransferProducts;
	}

	public int getNumTransferProductImages() {
		return numTransferProductImages;
	}

	public void setNumTransferProductImages(int numTransferProductImages) {
		this.numTransferProductImages = numTransferProductImages;
	}

	public int getNumTransferProductReviews() {
		return numTransferProductReviews;
	}

	public void setNumTransferProductReviews(int numTransferProductReviews) {
		this.numTransferProductReviews = numTransferProductReviews;
	}

	public int getNumTransferProductSliders() {
		return numTransferProductSliders;
	}

	public void setNumTransferProductSliders(int numTransferProductSliders) {
		this.numTransferProductSliders = numTransferProductSliders;
	}

	public Date getTransferEndTime() {
		return transferEndTime;
	}

	public void setTransferEndTime(Date transferEndTime) {
		this.transferEndTime = transferEndTime;
        this.transferEndTimeCalc = this.transferEndTime != null ? this.transferEndTime.getTime() : Long.MIN_VALUE;
    }

	public Date getTransferStartTime() {
		return transferStartTime;
	}

	public void setTransferStartTime(Date transferStartTime) {
		this.transferStartTime = transferStartTime;
        this.transferStartTimeCalc = this.transferStartTime != null ? this.transferStartTime.getTime() : Long.MIN_VALUE;
    }

    public Date getValidationEndTime() {
        return validationEndTime;
    }

    public void setValidationEndTime(Date validationEndTime) {
        this.validationEndTime = validationEndTime;
    }

    public Date getValidationStartTime() {
		return validationStartTime;
	}

	public void setValidationStartTime(Date validationStartTime) {
		this.validationStartTime = validationStartTime;
        this.validationStartTimeCalc = this.validationStartTime != null ? this.validationStartTime.getTime() : Long.MIN_VALUE;
    }

    public void add(DataFileStatistic stat, long errorCount) {
        if (stat.isHasImportErrors()) {
            this.numImportErrors++;
        }
        if (stat.isHasTransferErrors()) {
            this.numTransferErrors++;
        }
        if (stat.getImagesEndTime() != null) {
            this.imagesEndTimeCalc = Math.max(imagesEndTimeCalc, stat.getImagesEndTime().getTime());
            this.imagesEndTime = new Date(this.imagesEndTimeCalc);
        }
        if (stat.getImagesStartTime() != null) {
            this.imagesStartTimeCalc = Math.min(imagesStartTimeCalc, stat.getImagesStartTime().getTime());
            this.imagesStartTime = new Date(this.imagesStartTimeCalc);
        }
        if (stat.getImportEndTime() != null) {
            this.importEndTimeCalc = Math.max(importEndTimeCalc, stat.getImportEndTime().getTime());
            this.importEndTime = new Date(this.importEndTimeCalc);
        }
        if (stat.getImportStartTime() != null) {
            this.importStartTimeCalc = Math.min(importStartTimeCalc, stat.getImportStartTime().getTime());
            this.importStartTime = new Date(this.importStartTimeCalc);
        }
        this.numImageErrors+= stat.getNumImageErrors();
        this.numImages += stat.getNumImages();
        this.numTransferProductImages += stat.getNumTransferProductImages();
        this.numTransferProductReviews += stat.getNumTransferProductReviews();
        this.numTransferProductSliders += stat.getNumTransferProductSliders();
        if (stat.getTransferEndTime() != null) {
            this.transferEndTimeCalc = Math.max(this.transferEndTimeCalc, stat.getTransferEndTime().getTime());
            this.transferEndTime = new Date(this.transferEndTimeCalc);
        }
        if (stat.isTransferProductDone()) {
            this.numTransferProducts++;
        }
        if (stat.isTransferProductImagesDone()) {
            this.numTransferProductImages++;
        }
        if (stat.isTransferProductReviewsDone()) {
            this.numTransferProductReviews++;
        }
        if (stat.isTransferProductSlidersDone()) {
            this.numTransferProductSliders++;
        }
        if (stat.getTransferStartTime() != null) {
            this.transferStartTimeCalc = Math.min(this.transferStartTimeCalc, stat.getTransferStartTime().getTime());
            this.transferStartTime = new Date(this.transferStartTimeCalc);
        }
        this.numValidationErrors += errorCount;
        if (stat.getValidationEndTime() != null) {
            this.validationEndTimeCalc = Math.max(this.validationEndTimeCalc, stat.getValidationEndTime().getTime());
            this.validationEndTime = new Date(this.validationEndTimeCalc);
        }
        if (stat.getValidationStartTime() != null) {
            this.validationStartTimeCalc = Math.min(this.validationStartTimeCalc, stat.getValidationStartTime().getTime());
            this.validationStartTime = new Date(this.validationStartTimeCalc);
        }
    }
}
