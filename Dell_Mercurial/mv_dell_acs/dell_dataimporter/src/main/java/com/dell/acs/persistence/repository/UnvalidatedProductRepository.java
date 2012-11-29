package com.dell.acs.persistence.repository;

import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface UnvalidatedProductRepository extends IdentifiableEntityRepository<Long, UnvalidatedProduct> {

	@Transactional(propagation = Propagation.MANDATORY)
	UnvalidatedProduct getLastestUnvalidatedProduct(
			Collection<Long> retailerSiteIds, ProductValidationStatus currentStatus);

    @Transactional(propagation = Propagation.MANDATORY)
    UnvalidatedProduct getLastestUnvalidatedProductWithProperties(Collection<Long> retailerSiteIds, ProductValidationStatus currentStatus);

    @Transactional(propagation = Propagation.MANDATORY)
	Collection<UnvalidatedProduct> getUnvalidateProductByDataFile(
			DataFile dataFile);

	@Transactional(propagation = Propagation.MANDATORY)
	boolean isReadyForProcessing(Collection<Long> retailerSiteIds,
			ProductValidationStatus[] productValidationStatus);

	@Transactional(propagation = Propagation.MANDATORY)
	Collection<UnvalidatedProduct> getRecoverProducts(String applicationUrl);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	UnvalidatedProduct acquireLock(UnvalidatedProduct product, Integer currentStatus,
			Integer nextStatus, String host);

	@Transactional(propagation = Propagation.MANDATORY)
	UnvalidatedProduct getLastestUnvalidatedProductWithImages(
			Collection<Long> retailerSiteIds,
			ProductValidationStatus currentStatus);

	@Transactional(propagation = Propagation.MANDATORY)
	Collection<UnvalidatedProduct> getUnvalidateProductByDataFiles(
			List<DataFile> productDataFiles);

	@Transactional(propagation = Propagation.MANDATORY)
	void setAllTransferDone(Collection<Long> retailerSiteIds);
}
