/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository;

import org.springframework.dao.DuplicateKeyException;

import java.util.Collection;
import java.util.List;

/**
 * Repository Map approach. [read up on DDD]
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 533 $, $Date:: 2012-03-07 09:58:35#$
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

    void clear();
    
    void merge(R record);

}
