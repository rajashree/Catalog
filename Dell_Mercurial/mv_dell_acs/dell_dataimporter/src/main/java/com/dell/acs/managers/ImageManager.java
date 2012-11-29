/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.UnvalidatedProductImage;
import com.sourcen.core.managers.Manager;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public interface ImageManager extends Manager {
	public static final String MAX_RETRY_COUNT_KEY = ".maxRetryCount";
	public static final Integer DEFAULT_MAX_RETRY_COUNT = 10;
	
	/**
	 * Gets the latest imported file from the database.
	 * 
	 * @param retailerSiteIds
	 *            the set of identifiers for the retailer site to filter.
	 * @return dataFile - which are in queue to be processed
	 */
	@Transactional
	DataFile getLatestImagesDataFile(Collection<Long> retailerSiteIds);

	/**
	 * Processes a size file and dumps the data into the database.
	 * 
	 * @param dataFile
	 *            to be processed
	 */
	@Transactional
	void processImages(DataFile dataFile);

	/**
	 * @param retailerSiteIds
	 *            the set of identifiers for the retailer site to filter.
	 * @param currentStatus 
	 * @param nextStatus 
	 * @return the un-validate product image that needs to be resolved.
	 */
	@Transactional
	UnvalidatedProductImage getLatestUnresolvedImage(
			Collection<Long> retailerSiteIds,
			ProductValidationStatus currentStatus, ProductValidationStatus nextStatus);

	/**
	 * Called to download the unresolved image.
	 * 
	 * @param productImage
	 *            the un-validated product image that needs to be resolved.
	 */
	@Transactional
	void downloadImage(UnvalidatedProductImage productImage);
}
