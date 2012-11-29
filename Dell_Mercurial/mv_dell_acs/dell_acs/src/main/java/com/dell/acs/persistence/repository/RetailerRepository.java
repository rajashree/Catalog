/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.Retailer;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2704 $, $Date:: 2012-05-29 10:23:47#$
 */
public interface RetailerRepository extends IdentifiableEntityRepository<Long, Retailer> {

    /**
     * Gets the retailer with the given retailer name.
     *
     * @param retailerName
     *
     * @return Retailer
     */
    Retailer getByName(String retailerName);

    /**
     * Gets all the retailers.
     *
     * @return list of all retailers
     */
    List<Retailer> getRetailers();
    
    /**
     * Gets subset of retailers.
     *
     * @return list of retailers
     */
    List<Retailer> getSubset(Boolean active);

    /**
     * Gets retailer if it's active
     *
     * @return retailer
     */    
    Retailer getActive(Long id);
}
