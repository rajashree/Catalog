/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.DataFileStatistic;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

public interface DataFileStatisticRepository extends IdentifiableEntityRepository<Long, DataFileStatistic> {

	@Transactional(propagation = Propagation.MANDATORY)
	DataFileStatistic create(Session session, DataFile dataFile, int row);

	@Transactional(propagation = Propagation.MANDATORY)
	Collection<DataFileStatistic> getByDataFile(Session session, DataFile dataFile);

	@Transactional(propagation = Propagation.MANDATORY)
	DataFileStatistic getByDataFileAndRow(Session session, DataFile dataFile,
			int row);

	@Transactional(propagation = Propagation.MANDATORY)
	Collection<DataFileStatistic> getByRetailerSite(Session session,
                                                    RetailerSite rs, List<Long> skipStatsDataFileIds);

	@Transactional(propagation = Propagation.MANDATORY)
	Collection<DataFileStatistic> getByRetailerSiteAndHost(Session session,
                                                           RetailerSite rs, String host, List<Long> skipStatsDataFileIds);
}
