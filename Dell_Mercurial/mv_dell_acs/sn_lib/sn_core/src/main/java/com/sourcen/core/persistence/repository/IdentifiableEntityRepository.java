/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.repository;

import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.persistence.domain.constructs.StatusAware;

import java.io.Serializable;


/**
 * The IdentifiableRepository provides CRUD operations for tables that can be identified by a unique primary key. All
 * Repositorys' whose objects can be indentified by a unique ID should extend this Repository.
 *
 * @param <K> the unique datatype Identifier for the record.
 * @param <R> the record that this Repository will handle.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2699 $, $Date:: 2012-05-29 10:00:40#$
 * @since 1.0
 */

public interface IdentifiableEntityRepository<K extends Serializable, R extends IdentifiableEntity<K>> extends Repository<R> {

    R get(K id);

    boolean containsKey(K id);

    void remove(K entityId);

    <T extends StatusAware & IdentifiableEntity<K>> R acquireLock(final T obj, final Integer oldStatus, final Integer newStatus);

    <O> O acquireLock(final IdentifiableEntity obj, final String field, final Object oldValue, final Object newValue);

}
