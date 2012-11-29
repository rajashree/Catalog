package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.Retailer;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/28/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RetailerRepository extends IdentifiableEntityRepository<Long, Retailer> {
    public Retailer getByRetailerId(String retailerId);
}

