/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.sourcen.core.managers.Manager;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public interface TransferProductManager extends Manager {
	/**
	 * Gets the latest un-validated product that needs to be validated from the
	 * database.
	 * 
	 * @param retailerSiteIds
	 *            the set of identifiers for the retailer site to filter.
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
	 * Called to move the product from unvalidated stage to production table.
	 * 
	 * @param product
	 *            the root of the object to move to production.
	 * @return
	 */
	UnvalidatedProduct moveToProduction(UnvalidatedProduct product);

	/**
	 * Called to move the product from unvalidated product slider stage to
	 * production table.
	 * 
	 * @param product
	 *            the root of the object to move to production.
	 * @return
	 */
	UnvalidatedProduct moveSliderToProduction(UnvalidatedProduct product);

	/**
	 * Gets the latest un-validated product with slider that needs to be
	 * validated from the database but only if all products have move
	 * transferred or in the ETL_SLIDER_ENQUE status.
	 * 
	 * @param retailerSiteIds
	 *            the set of identifiers for the retailer site to filter.
	 * @param currentStatus
	 *            the current status.
	 * @param nextStatus
	 *            the next status.
	 * @return product which are specified status to be validated
	 */
	UnvalidatedProduct getLatestUnvalidatedProductSlider(
			Collection<Long> retailerSiteIds,
			ProductValidationStatus currentStatus,
			ProductValidationStatus nextStatus);

	/**
	 * Called to clean up the unvalidate information for the transfer product.
	 * 
	 * @param product
	 *            which product to clean up.
	 */
	void cleanup(UnvalidatedProduct product);

	/**
	 * Called to see if the product are ready to processing. All the unvalidated
	 * products must be in the specified status set to be ready to process the
	 * next next.
	 * 
	 * @param retailerSiteIds
	 *            the set of identifiers for the retailer site to filter.
	 * @param invalidStatuses
	 *            the not product for the retailer site can be in the set of
	 *            status.
	 */
	boolean isReadyForProcessing(Collection<Long> retailerSiteIds,
			ProductValidationStatus[] productValidationStatus);

	void acquireLock(UnvalidatedProduct product, Integer currentStatus, Integer nextStatus);

	@Transactional
	void rollbackStatus(UnvalidatedProduct product, Integer dbValue,
			Integer dbValue2);

	@Transactional
	void setAllTransferDone(Collection<Long> retailerSiteIds);

    UnvalidatedProduct getLatestUnvalidatedProductWithProperties(Collection<Long> retailerSiteIds, ProductValidationStatus done, ProductValidationStatus etlProcessing);
}
