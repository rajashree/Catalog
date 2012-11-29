/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.RetailerSiteProperty;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Set;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 3467 $, $Date:: 2012-06-21 09:22:19#$
 */
@Repository
public class RetailerSiteRepositoryImpl extends
		PropertiesAwareRepositoryImpl<RetailerSite> implements
		RetailerSiteRepository {

	/**
	 * Constructor.
	 */
	public RetailerSiteRepositoryImpl() {
		super(RetailerSite.class, RetailerSiteProperty.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RetailerSite getByName(final String siteName)
			throws EntityNotFoundException {
		Criteria criteria = getSession().createCriteria(RetailerSite.class)
				.add(Restrictions.eq("siteName", siteName));
		Object o = criteria.uniqueResult();
		if (o == null) {
			criteria = getSession().createCriteria(RetailerSite.class).add(
					Restrictions.eq("siteName", siteName.toLowerCase()));
			o = criteria.uniqueResult();
			if (o == null) {
				throw new EntityNotFoundException(
						siteName.concat(" - retailer site not found"));
			}
		}
		return (RetailerSite) o;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public RetailerSite getByName(final String siteName, boolean loadProperties) {
		if (!loadProperties) {
			return this.getByName(siteName);
		} else {
			Criteria criteria = getSession().createCriteria(RetailerSite.class);
			criteria.add(Restrictions.eq("siteName", siteName));
			return onFindForObject((RetailerSite) criteria.uniqueResult());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<RetailerSite> getRetailerSites(Retailer retailer) {
		return getSession().createCriteria(RetailerSite.class)
				.add(Restrictions.eq("retailer.id", retailer.getId())).list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<RetailerSite> getAllRetailerSites() {
		return getSession().createCriteria(RetailerSite.class).list();
	}
	
	@Override
	public Collection<RetailerSite> getAllActiveRetailerSites() {
		return getSession().createCriteria(RetailerSite.class)
				.add(Restrictions.eq("active", true))
				.list();
	}	

	/**
	 * {@inheritDoc}
	 */
	public Collection<RetailerSite> getActiveRetailerSites(Long retailerId) {
		Criteria criteria = getSession().createCriteria(RetailerSite.class)
				.add(Restrictions.eq("active", true));

		// filter by active retailerId
		criteria.add(Restrictions.eq("retailer.id", retailerId))
				.add(Restrictions.eq("retailer.active", true));

		return criteria.list();
	}

	/**
	 * {@inheritDoc}
	 */
	public RetailerSite getActiveRetailerSite(Long retailerSiteId) {
        RetailerSite retailerSite = get(retailerSiteId);
        if(retailerSite != null){
            if(retailerSite.getActive() && retailerSite.getRetailer().getActive()){
                return retailerSite;
            }   else{
                logger.info("retailerSite with ID:={} is not active", retailerSiteId);
            }
        }
        return null;
	}

	@Override
	public Collection<RetailerSite> getAllRetailerSitesWithProperties() {
		return onFindForList(getSession().createCriteria(RetailerSite.class)
				.list());
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	@Override
	public Collection<String> getSiteNames() {
		Criteria criteria = getSession().createCriteria(RetailerSite.class);
		Projection siteNames = Projections.property("siteName").as("siteName");
		return criteria.setProjection(
				Projections.projectionList().add(siteNames)).list();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Collection<Long> getByNameIds(Set<String> retailerSiteNames) {
		Criteria criteria = getSession().createCriteria(RetailerSite.class);
		
		if (retailerSiteNames != null) {
			criteria = criteria.add(Restrictions.in("siteName", retailerSiteNames));
		}
		
		Projection siteIds = Projections.property("id");
		return (Collection<Long>)criteria
				.setProjection(Projections.projectionList().add(siteIds))
				.list();
	}
}
