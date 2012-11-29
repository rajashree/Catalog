/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.Taxonomy;
import com.dell.acs.persistence.repository.TaxonomyRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 Created by IntelliJ IDEA. User: Adarsh Date: 3/27/12 Time: 1:31 PM To change this template use File | Settings | File
 Templates.
 */
@Repository
public class TaxonomyRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, Taxonomy>
        implements TaxonomyRepository {

    private static final Logger LOG = LoggerFactory.getLogger(TaxonomyRepositoryImpl.class);

    public TaxonomyRepositoryImpl() {
        super(Taxonomy.class);
    }

    @Override
    public void saveOrUpdateTaxomony(Taxonomy taxonomy) {
        super.getSession().saveOrUpdate(taxonomy);
    }

    @Override
    @Transactional(readOnly = true)
    public Taxonomy getTaxonomy(Long retailerSiteID, String name) {
        try {
            Criteria criteria = getSession().createCriteria(Taxonomy.class);
            criteria.add(Restrictions.eq("retailerSite.id", retailerSiteID));
            criteria.add(Restrictions.eq("name", name));
            Taxonomy o = (Taxonomy) criteria.uniqueResult();
            if (o != null) {
                return (Taxonomy) o;
            }
        } catch (Exception e) {
        	e.printStackTrace();
            logger.warn("TaxonomyRepositoryImpl " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Taxonomy getTaxonomy(RetailerSite retailerSite, String name) {
        return getTaxonomy(retailerSite.getId(), name);
    }
}
