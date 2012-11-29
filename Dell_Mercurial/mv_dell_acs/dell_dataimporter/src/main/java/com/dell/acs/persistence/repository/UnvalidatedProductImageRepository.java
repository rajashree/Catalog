package com.dell.acs.persistence.repository;

import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.persistence.domain.UnvalidatedProductImage;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

public interface UnvalidatedProductImageRepository extends
		IdentifiableEntityRepository<Long, UnvalidatedProductImage> {
	@Transactional(propagation = Propagation.MANDATORY)
	UnvalidatedProductImage getLatestUnresolvedImage(
			Collection<Long> retailerSiteIds);
}
