package com.dell.acs.web.ws.v2;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;

import java.util.Collection;
import java.util.Map;

/**
 * Retailer Service is the second version of the existing {@link com.dell.acs.web.ws.v1.RetailerService}
 * In version 2, we are adding lot more service endpoints.
 *
 * @author Vivek Kondur
 * @version 1.0
 */
public interface RetailerService {

    /**
     * Create a Retailer
     *
     * @param name        String - Name of the Retailer
     * @param description String - Description for the Retailer
     * @return Retailer
     */
    Retailer createRetailer(String name, String description);


    /**
     * Create a RetailerSite
     *
     * @param retailer Object - Retailer
     * @param name     String - Name of the RetailerSite
     * @param url      String - URL typically a website
     * @param logoURL  String - Image URL
     * @param pixelTracker String - Type of PixelTracking the RetailerSite - LinkTracker, MarketVine, etc
     * @param dataImport String - Type of Feed Import for various content - Ficstar, Google, Merchant, CJ, etc
     * @return RetailerSite
     * <p> Note: PixelTracker & DataImport Type will have default values - LinkTracker & Ficstar -
     * if the End User doesn't send those parameters</p>
     */
    RetailerSite createRetailerSite(Object retailer, String name, String url, String logoURL, String pixelTracker, String dataImport);

    /**
     * Updates a Retailer for Name or Description
     *
     * @param retailerId  Object - Can be either a (Long) RetailerID or (String) RetailerName
     * @param name        String - Name of the Retailer to be updated
     * @param description String - Description for the Retailer to be updated
     * @return Retailer
     */
    Retailer updateRetailer(Object retailerId, String name, String description);

    /**
     * Update a RetailerSite for Name or URL or LogoURL
     *
     * @param retailerSite Object - Can either be a (Long) RetailerSiteID or (String) RetailerSiteName
     * @param name         String - Name of the RetailerSite to be updated
     * @param url          String - URL to be updated
     * @param logoURL      string - logoURL to be updated
     * @return RetailerSite
     */
    RetailerSite updateRetailerSite(Object retailerSite, String name, String url, String logoURL, String pixelTracker, String dataImport);

    /**
     * De-activate an RetailerSite
     *
     * @param retailerSite Object - Can either be a (Long) RetailerSiteID or (String) RetailerSiteName
     * @return RetailerSite
     */
    RetailerSite deActivateRetailerSite(Object retailerSite);


    /**
     * Returns all Retailers which are defined
     * @return Collection of {@link Retailer}
     */
    Collection<Retailer> getRetailers();

    /**
     * Returns all the RetailerSites which have been created for a specific Retailer.
     * @param retailer Object - Can be either a (Long) RetailerID or (String) RetailerName
     * @return Collection of {@link RetailerSite}
     */
    Collection<RetailerSite> getRetailerSites(Object retailer);

    /**
     * Return a Retailer
     * @param retailer Object - Can be either a (Long) RetailerID or (String) RetailerName
     * @return Retailer
     */
    Retailer getRetailer(Object retailer);


    /**
     * Return a RetailerSite
     * @param site Object - Can be either a (Long) RetailerSiteID or (String) RetailerSiteName
     * @return RetailerSiteName
     */
    RetailerSite getRetailerSite(Object site);

    /**
     * Returns all the PixelTrackers that are defined in the system
     * @return All the definitions of Enum {@link EntityConstants.EnumPixelTracker}
     */
    EntityConstants.EnumPixelTracker[] getPixelTrackers();

    /**
     * Returns all the DataImport types that are defined in the system.
     * @return  All the definitions of Enum {@link EntityConstants.EnumDataImport}
     */
    EntityConstants.EnumDataImport[] getDataImportTypes();


}
