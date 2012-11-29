package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.Store;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StoreRepository extends  IdentifiableEntityRepository<Long, Store> {

    Store getByStoreId(String storeId);
}
