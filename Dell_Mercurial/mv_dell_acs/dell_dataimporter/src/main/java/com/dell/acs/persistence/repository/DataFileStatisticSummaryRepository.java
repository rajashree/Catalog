/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.DataFileStatisticSummary;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface DataFileStatisticSummaryRepository extends IdentifiableEntityRepository<Long, DataFileStatisticSummary> {
    @Transactional(propagation = Propagation.MANDATORY)
    Collection<DataFileStatisticSummary> getSummaryByRetailerSite(Session session, RetailerSite rs);
}
