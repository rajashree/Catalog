/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerProperty;
import com.dell.acs.persistence.repository.RetailerRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2704 $, $Date:: 2012-05-29 10:23:47#$
 */
@Repository
public class RetailerRepositoryImpl
        extends PropertiesAwareRepositoryImpl<Retailer>
        implements RetailerRepository {

    /**
     * Constructor.
     */
    public RetailerRepositoryImpl() {
        super(Retailer.class, RetailerProperty.class);
    }


    /**
     * RetailerSiteRepository Bean Injection.
     */
    @Autowired
    private RetailerSiteRepository retailerSiteRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Retailer getByName(String retailerName) {
        return (Retailer) getSession().createCriteria(Retailer.class)
                .add(Restrictions.eq("name", retailerName)).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Retailer> getRetailers() {

        Criteria retailerCriteria = getSession().createCriteria(Retailer.class);
        List<Retailer> retailers = retailerCriteria.list();


//        for (int i = 0; i < retailers.size(); i++) {
//            List<RetailerSite> retailerSite =
//                    (List<RetailerSite>) retailerSiteRepository.getRetailerSites(retailers.get(i));
//        }
        return retailers;
        //return (List<Retailer>) getSession().createCriteria(Retailer.class);

    }
    
    public List<Retailer> getSubset(Boolean active) {
        Criteria retailerCriteria = getSession().createCriteria(Retailer.class)
				.add(Restrictions.eq("active", active));
		List<Retailer> retailers = retailerCriteria.list();
		return retailers;    	
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Retailer getActive(Long id) {
        return (Retailer) getSession().createCriteria(Retailer.class)
                .add(Restrictions.eq("id", id))
                .add(Restrictions.eq("active", true))
                .uniqueResult();
    }   
}
