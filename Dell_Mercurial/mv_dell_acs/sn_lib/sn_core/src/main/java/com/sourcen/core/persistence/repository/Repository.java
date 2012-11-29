/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.repository;


import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.Criteria;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collection;
import java.util.List;

/**
 * Repository Map approach. [read up on DDD]
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2699 $, $Date:: 2012-05-29 10:00:40#$
 * @since 1.0
 */

@org.springframework.stereotype.Repository
public interface Repository<R> {

    List<R> getAll();

    List<R> getAll(Integer firstResult, Integer maxResults);

    List<R> getByExample(R example);

    R getUniqueByExample(R example);

    Long size();

    void insert(R record);

    void put(R record) throws DuplicateKeyException;

    void put(Collection<R> records);

    void insertAll(Collection<R> items);

    void update(R record);

    void updateAll(Collection<R> items);

    void remove(R entity);

    void refresh(R entity);

    void clear();

    void updateNow(R entity);

    /**
     * Generic Criteria builder for v2 REST endpoints
     * @param criteria - Criteria object
     * @param filter - Various Filters that need to be applied.
     */
    void applyGenericCriteria(Criteria criteria, ServiceFilterBean filter);

}
