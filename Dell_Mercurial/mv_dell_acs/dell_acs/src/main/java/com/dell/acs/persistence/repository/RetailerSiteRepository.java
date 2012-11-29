/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Collection;
import java.util.Set;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 2975 $, $Date:: 2012-06-07 10:19:50#$
 */
public interface RetailerSiteRepository extends
		IdentifiableEntityRepository<Long, RetailerSite> {

	/**
	 * Gets the retailer site with the given retailer site name.
	 * 
	 * @param siteName
	 * 
	 * @return RetailerSite
	 */
	RetailerSite getByName(String siteName);

	/**
	 * Gets the retailer site with loaded properties with the given retailer
	 * site name.
	 * 
	 * @param siteName
	 * 
	 * @return RetailerSite
	 */
	RetailerSite getByName(String siteName, boolean loadProperties);

	/**
	 * Retrieves the existing site names from the retailerSite table.
	 * 
	 * @return Collection of available site names
	 */
	Collection<String> getSiteNames();

	/**
	 * Gives a collection of sites for a particular Retailer.
	 * 
	 * @param retailer
	 * 
	 * @return Collection of retailerSites
	 */
	Collection<RetailerSite> getRetailerSites(Retailer retailer);

	/**
	 * Gives Collection of RetailerSites.
	 * 
	 * @return Collection of retailerSites
	 */
	Collection<RetailerSite> getAllRetailerSites();

	Collection<RetailerSite> getAllActiveRetailerSites();
	
	/**
	 * Gives Collection of RetailerSites with properties loaded.
	 * 
	 * @return Collection of retailerSites
	 */
	Collection<RetailerSite> getAllRetailerSitesWithProperties();

	/**
	 * Gives a collection of active sites for a particular Retailer.
	 * 
	 * @param retailerId
	 * 
	 * @return Collection of active retailerSites
	 */
	Collection<RetailerSite> getActiveRetailerSites(Long retailerId);

	/**
	 * Returns RetailerSite for given Id if active
	 * 
	 * @param retailerSiteId
	 * 
	 * @return RetailerSite or null
	 */
	RetailerSite getActiveRetailerSite(Long retailerSiteId);

	/**
	 * 
	 * @param retailerSiteNames
	 *            the set of retailer site names.
	 * @return the set of identifiers for the retailer site names.
	 */
	Collection<Long> getByNameIds(Set<String> retailerSiteNames);
}
