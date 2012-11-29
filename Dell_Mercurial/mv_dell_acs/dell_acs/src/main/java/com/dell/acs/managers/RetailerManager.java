/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.managers.model.RetailerSiteData;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.managers.Manager;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3522 $, $Date:: 2012-06-22 10:56:06#$
 */
public interface RetailerManager extends Manager {

    String RETAILER_SITE_PIXEL_TRACKER_ID_PROPERTY_KEY = "retailerSite.pixelTracker.id";

    String RETAILER_SITE_PIXEL_TRACKER_NAME_PROPERTY_KEY = "retailerSite.pixelTracker.name";

    String RETAILER_SITE_DATA_IMPORT_TYPE_ID_PROPERTY_KEY = "retailerSite.dataImportType.id";

    String RETAILER_SITE_DATA_IMPORT_TYPE_NAME_PROPERTY_KEY = "retailerSite.dataImportType.name";

    /**
     * Get All Retailer Collection.
     *
     * @return Collection of Retailer
     */
    Collection<Retailer> getAllRetailers();

    /**
     * Get All Active Retailer Collection.
     *
     * @return Collection of Active Retailer
     */
    List<Retailer> getActiveRetailers();

    /**
     * Get Retailer Collection.
     *
     * @return Collection of Retailer
     */
    List<Retailer> getRetailerSubset(Boolean active);

    /**
     * Get Retailer by Id.
     *
     * @param id
     * @return Retailer
     */
    Retailer getRetailer(Long id);

    Retailer getActiveRetailer(Long id);

    /**
     * Create a Retailer.
     *
     * @param retailer
     * @return Retailer
     */
    Retailer createRetailer(Retailer retailer);

    /**
     * Update Retailer.
     *
     * @param retailer
     * @return Retailer
     */
    Retailer updateRetailer(Retailer retailer);

    /**
     * Get Retailer Site by ID or NAME.
     *
     * @param site - Long OR String -  siteID or siteName (Ex : 1 OR dell)
     * @return Object of {@link RetailerSite}
     */
    RetailerSite getRetailerSite(Object site);

    /**
     * Get the retailer site by ID
     *
     * @param id
     * @return Object of {@link RetailerSite}
     */
    RetailerSite getRetailerSite(Long id);

    /**
     * return the retailerSite pojo class
     *
     * @param id
     * @return
     */
    RetailerSiteData getRetailerSiteData(Long id);

    RetailerSite getActiveRetailerSite(Long id);

    /**
     * Create a RetailerSite.
     *
     * @param retailerSite
     * @return RetailerSite
     */
    RetailerSite createRetailerSite(RetailerSite retailerSite);

    /**
     * Update RetailerSite.
     *
     * @param retailerSite
     * @return RetailerSite
     */
    RetailerSite updateRetailerSite(RetailerSite retailerSite);

    /**
     * Returns Collection of RetailerSite by RetailerID.
     *
     * @param retailerId
     * @return Collection of RetailerSites
     */
    Collection<RetailerSite> getRetailerSites(Long retailerId);

    Collection<RetailerSite> getActiveRetailerSites(Long retailerId);

    Collection<RetailerSite> getRetailerSites(Retailer retailer);

    /**
     * Get All RetailerSites.
     *
     * @return Collection of RetailerSites
     */
    Collection<RetailerSite> getAllRetailerSites();

	Collection<RetailerSite> getAllActiveRetailerSites();
    
    /**
     * Get All WSRetailer(For WebService).
     *
     * @return List of WSRetailer
     */
    List<Retailer> getRetailers();

    /**
     * Delete Retailer Site.
     *
     * @param retailerSite
     */
    void deleteRetailerSite(RetailerSite retailerSite);

    /**
     * Get retailer site names
     *
     * @return collection of retailer site names
     */
    Collection<String> getSiteNames();

    /**
     * Get RetailerSite by Name
     *
     * @param retailerSiteName
     * @return retailerSite
     */

    RetailerSite getRetailerSitebyName(String retailerSiteName) throws EntityNotFoundException;

    //v2 Implementations - vivek
    Retailer getActiveRetailer(Object retailer) throws EntityNotFoundException;

    RetailerSite getRetailerSite(String retailerSiteName, boolean loadProps);

}