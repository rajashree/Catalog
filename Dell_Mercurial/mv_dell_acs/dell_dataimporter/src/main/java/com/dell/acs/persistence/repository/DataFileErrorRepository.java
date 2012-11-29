/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.dataimport.model.DataImportError;
import com.dell.acs.dataimport.model.ValidatorError;
import com.dell.acs.persistence.domain.DataFileError;
import com.dell.acs.persistence.domain.DataFileStatistic;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

public interface DataFileErrorRepository extends IdentifiableEntityRepository<Long, DataFileError> {
	@Transactional(propagation = Propagation.MANDATORY)
	DataFileError create(Session session, DataFileStatistic stat, int column,
			Throwable t);

	@Transactional(propagation = Propagation.MANDATORY)
	DataFileError create(Session session, DataFileStatistic stat,
			DataImportError diError);

	@Transactional(propagation = Propagation.MANDATORY)
	DataFileError create(Session session, DataFileStatistic stat,
			ValidatorError vError);

	@Transactional(propagation = Propagation.MANDATORY)
	DataFileError create(Session session, DataFileStatistic stat, int column,
			String msg, Object... args);

    @Transactional(propagation = Propagation.MANDATORY)
    Long getErrorCountForStat(Session session, DataFileStatistic dataFileStat);
}
