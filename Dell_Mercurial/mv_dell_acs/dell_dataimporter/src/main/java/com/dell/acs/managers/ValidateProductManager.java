/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import java.util.Collection;

import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.sourcen.core.managers.Manager;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public interface ValidateProductManager extends Manager {
	/**
	 * Gets the latest unvalidated product that needs to be validated from the
	 * database.
	 * 
	 * @param retailerSiteIds
	 *            the set of ids for the retailer site to filter.
	 * @param currentStatus
	 *            the current status.
	 * @param nextStatus
	 *            the next status.
	 * @return product which are specified status to be validated
	 */
	UnvalidatedProduct getLatestUnvalidatedProduct(
			Collection<Long> retailerSiteIds,
			ProductValidationStatus currentStatus,
			ProductValidationStatus nextStatus);

	/**
	 * Validate the unvalidated product.
	 * 
	 * @param product
	 *            to be processed
	 */
	void validateProduct(UnvalidatedProduct product);

	/**
	 * Validate the images of the unvalidated product.
	 * 
	 * @param product
	 *            to be processed
	 */
	void validateProductImages(UnvalidatedProduct product);
}
