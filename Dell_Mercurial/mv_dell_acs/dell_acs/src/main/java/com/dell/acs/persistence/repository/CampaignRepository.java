/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.CampaignForInactiveRetailerException;
import com.dell.acs.CampaignItemNotFoundException;
import com.dell.acs.CampaignNotFoundException;
import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Collection;
import java.util.List;

/**
 @author Samee K.S */

public interface CampaignRepository extends IdentifiableEntityRepository<Long, Campaign> {

    /**
     Method to load the campaign using ID.

     @param campaignID - Campaign ID
     @return - Campaign object
     */
    Campaign getCampaign(Long campaignID) throws CampaignNotFoundException;

    /**
     @param campaignPropertyId
     @param name
     @param value
     @return
     @throws CampaignItemNotFoundException
     */
    Campaign updateProperty(Long campaignPropertyId, String name, String value) throws CampaignItemNotFoundException;


    /**
     Method to fetch the list of all Campaign objects.

     @return List of Campaign objects
     @see Campaign
     */
    List<Campaign> getCampaigns();

    /**
     Method to fetch a list of CampaignItems matching the keyword. If entered keyword is of numeric type then search
     criteria is built on itemID ( Product, Videos or White Paper) or else it will be for description and item title.

     @param keyword - search term ( id/description/title )
     @return List of CampaignItem objects matching the criteria
     @see CampaignItem
     */
    List<CampaignItem> searchItems(String keyword);

    /**
     To retrieve list of Campaign objects using the RetailerSite ID.

     @param retailerSiteID - Retailer site id
     @return - List of Campaign object which has matching retailerSiteID
     @see RetailerSite
     @see Campaign
     */
    List<Campaign> getCampaignsBySite(Long retailerSiteID);


    /**
     Method to fetch the Campaign object using retailerSite object.

     @param retailerSite - RetailerSite object
     @return - Campaign object with matching retailerSite
     @see RetailerSite
     */
    Campaign getCampaignBySite(RetailerSite retailerSite);

    Collection<Campaign> getCampaignsBySiteandType(Long retailerSiteID, String campaignType);

    Collection<Campaign> getActiveCampaignsBySiteName(String retailerSiteName, String campaignType)
    					 throws CampaignForInactiveRetailerException;

    /**
     Check whether campaign used the particular document or not.

     @param itemID , Store the document ID.
     @return boolean
     */
    boolean checkCampaignForDoc(Long itemID);

    List<CampaignItem> getDocumentAssociatedCampaigns(Long documentID);
}
