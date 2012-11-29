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
@Table(name = "t_data_file_stats")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataFileStatistic extends IdentifiableEntityModel<Long> {
	@Column(nullable = false)
	private long dataFile_id;

	@Column(nullable = false)
	private long retailerSite_id;

	/**
	 * Stores the true if there are import errors.
	 */
	@Column(nullable = false)
	private boolean hasImportErrors;

	/**
	 * Stores the true if there are transfer errors.
	 */
	@Column(nullable = false)
	private boolean hasTransferErrors;

	/**
	 * Stores the true if there are validation errors.
	 */
	@Column(nullable = false)
	private boolean hasValidationErrors;

	/**
	 * Stores the host that did the image.
	 */
	@Column(nullable = true)
	private String imageHost;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date imagesEndTime;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date imagesStartTime;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date importEndTime;

	/**
	 * Stores the host that did the import.
	 */
	@Column(nullable = true)
	private String importHost;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date importStartTime;

	/**
	 * Stores the number of image errors for the row.
	 */
	@Column(nullable = false)
	private int numImageErrors;

	/**
	 * Stores the number of images need for the row.
	 */
	@Column(nullable = false)
	private int numImages;

	/**
	 * Stores the number of transferred product images.
	 */
	@Column(nullable = false)
	private int numTransferProductImages;

	/**
	 * Stores the number of transferred product reviews.
	 */
	@Column(nullable = false)
	private int numTransferProductReviews;

	/**
	 * Stores the number of transferred product sliders.
	 */
	@Column(nullable = false)
	private int numTransferProductSliders;

	@Column(nullable = false)
	private int row;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date transferEndTime;

	/**
	 * Stores the host that did the transfer.
	 */
	@Column(nullable = true)
	private String transferHost;

	/**
	 * Stores the true the transfer of product is complete.
	 */
	@Column(nullable = false)
	private boolean transferProductDone;

	/**
	 * Stores the true the transfer of product images is complete.
	 */
	@Column(nullable = false)
	private boolean transferProductImagesDone;

	/**
	 * Stores the true the transfer of product reviews is complete.
	 */
	@Column(nullable = false)
	private boolean transferProductReviewsDone;

	/**
	 * Stores the true the transfer of product sliders is complete.
	 */
	@Column(nullable = false)
	private boolean transferProductSlidersDone;

	/**
	 * Stores the host that did the transfer.
	 */
	@Column(nullable = true)
	private String transferSliderHost;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date transferStartTime;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date validationEndTime;

	/**
	 * Stores the host that did the import.
	 */
	@Column(nullable = true)
	private String validationHost;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date validationStartTime;

	public DataFileStatistic() {
	}

	/**
	 * Stores true if the entity is being updated else false.
	 */
	@Column(nullable = true)
	private Boolean updating;

	public DataFileStatistic(DataFile pDataFile, int row) {
		this.setDataFile_id(pDataFile.getId());
		this.setRetailerSite_id(pDataFile.getRetailerSite().getId());
		this.setRow(row);
	}

	public Long getDataFile_id() {
		return dataFile_id;
	}

	public String getImageHost() {
		return imageHost;
	}

	public Date getImagesEndTime() {
		return imagesEndTime;
	}

	public Date getImagesStartTime() {
		return imagesStartTime;
	}

	public Date getImportEndTime() {
		return importEndTime;
	}

	public String getImportHost() {
		return importHost;
	}

	public Date getImportStartTime() {
		return importStartTime;
	}

	public int getNumImageErrors() {
		return numImageErrors;
	}

	public int getNumImages() {
		return numImages;
	}

	public int getNumTransferProductImages() {
		return numTransferProductImages;
	}

	public int getNumTransferProductReviews() {
		return numTransferProductReviews;
	}

	public int getNumTransferProductSliders() {
		return numTransferProductSliders;
	}

	public int getRow() {
		return row;
	}

	public Date getTransferEndTime() {
		return transferEndTime;
	}

	public String getTransferHost() {
		return transferHost;
	}

	public String getTransferSliderHost() {
		return transferSliderHost;
	}

	public Date getTransferStartTime() {
		return transferStartTime;
	}

	public Date getValidationEndTime() {
		return validationEndTime;
	}

	public String getValidationHost() {
		return validationHost;
	}

	public Date getValidationStartTime() {
		return validationStartTime;
	}

	public boolean isHasImportErrors() {
		return hasImportErrors;
	}

	public boolean isHasTransferErrors() {
		return hasTransferErrors;
	}

	public boolean isHasValidationErrors() {
		return hasValidationErrors;
	}

	public boolean isTransferProductDone() {
		return transferProductDone;
	}

	public boolean isTransferProductImagesDone() {
		return transferProductImagesDone;
	}

	public boolean isTransferProductReviewsDone() {
		return transferProductReviewsDone;
	}

	public boolean isTransferProductSlidersDone() {
		return transferProductSlidersDone;
	}

	public void setDataFile_id(Long dataFile_id) {
		this.dataFile_id = dataFile_id;
	}

	public void setHasImportErrors(boolean hasImportErrors) {
		this.hasImportErrors = hasImportErrors;
	}

	public void setHasTransferErrors(boolean hasTransferErrors) {
		this.hasTransferErrors = hasTransferErrors;
	}

	public void setHasValidationErrors(boolean hasValidationErrors) {
		this.hasValidationErrors = hasValidationErrors;
	}

	public void setImageHost(String imageHost) {
		this.imageHost = imageHost;
	}

	public void setImagesEndTime(Date imagesEndTime) {
		this.imagesEndTime = imagesEndTime;
	}

	public void setImagesStartTime(Date imagesStartTime) {
		this.imagesStartTime = imagesStartTime;
	}

	public void setImportEndTime(Date importEndTime) {
		this.importEndTime = importEndTime;
	}

	public void setImportHost(String importHost) {
		this.importHost = importHost;
	}

	public void setImportStartTime(Date importStartTime) {
		this.importStartTime = importStartTime;
	}

	public void setNumImageErrors(int numImageErrors) {
		this.numImageErrors = numImageErrors;
	}

	public void setNumImages(int numImages) {
		this.numImages = numImages;
	}

	public void setNumTransferProductImages(int numTransferProductImages) {
		this.numTransferProductImages = numTransferProductImages;
	}

	public void setNumTransferProductReviews(int numTransferProductReviews) {
		this.numTransferProductReviews = numTransferProductReviews;
	}

	public void setNumTransferProductSliders(int numTransferProductSliders) {
		this.numTransferProductSliders = numTransferProductSliders;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setTransferEndTime(Date transferEndTime) {
		this.transferEndTime = transferEndTime;
	}

	public void setTransferHost(String transferHost) {
		this.transferHost = transferHost;
	}

	public void setTransferProductDone(boolean transferProductDone) {
		this.transferProductDone = transferProductDone;
	}

	public void setTransferProductImagesDone(boolean transferProductImagesDone) {
		this.transferProductImagesDone = transferProductImagesDone;
	}

	public void setTransferProductReviewsDone(boolean transferProductReviewsDone) {
		this.transferProductReviewsDone = transferProductReviewsDone;
	}

	public void setTransferProductSlidersDone(boolean transferProductSlidersDone) {
		this.transferProductSlidersDone = transferProductSlidersDone;
	}

	public void setTransferSliderHost(String transferSliderHost) {
		this.transferSliderHost = transferSliderHost;
	}

	public void setTransferStartTime(Date transferStartTime) {
		this.transferStartTime = transferStartTime;
	}

	public void setValidationEndTime(Date validationEndTime) {
		this.validationEndTime = validationEndTime;
	}

	public void setValidationHost(String validationHost) {
		this.validationHost = validationHost;
	}

	public void setValidationStartTime(Date validationStartTime) {
		this.validationStartTime = validationStartTime;
	}

	public long getRetailerSite_id() {
		return retailerSite_id;
	}

	public void setRetailerSite_id(long retailerSite_id) {
		this.retailerSite_id = retailerSite_id;
	}

	public Boolean isUpdating() {
		return (updating != null ? updating.booleanValue() : false);
	}

	public void setUpdating(Boolean updating) {
		this.updating = updating;
	}
}
