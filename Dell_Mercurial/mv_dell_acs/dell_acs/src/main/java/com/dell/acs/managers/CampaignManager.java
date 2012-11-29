/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.*;
import com.dell.acs.managers.model.CampaignItemData;
import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.CampaignCategory;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.persistence.domain.ProductReview;
import com.sourcen.core.managers.Manager;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author Samee K.S
 */

public interface CampaignManager extends Manager {

    // Campaign related properties
    public static final String CAMPAIGN_PACKAGE_TYPE_PROPERTY = "dell.campaign.packageType";
    public static final String CAMPAIGN_THUMBNAIL_PROPERTY = "dell.campaign.thumbnail";
    public static final String CAMPAIGN_THUMBNAIL_TYPE_PROPERTY = "dell.campaign.thumbnailType";
    public static final String CAMPAIGN_DEAL_OF_DAY_PROPERTY = "dell.campaign.dealOfDay";

    // Campaign item related properties
    public static final String CAMPAIGN_ITEM_REVIEWS_PROPERTY = "dell.campaign.item.reviews";

    public static final String CAMPAIGN_LT_LINKS_DOWNLOADED_PROP = "dell.campaign.items.downloaded";
    public static final String CAMPAIGN_LT_LINKS_UPLOADED_PROP = "dell.campaign.items.uploaded";
    public static final String CAMPAIGN_ITEM_LT_PROP = "dell.linkTracker.url.";

    // Campaign item start and end time properties
    // Format : dell.campaign.<campaign_item_id>.startTime
    public static final String CAMPAIGN_ITEM_START_TIME_PROP = "dell.campaign.%s.startTime";
    public static final String CAMPAIGN_ITEM_END_TIME_PROP = "dell.campaign.%s.endTime";

    // Max depth allowed for a category
    public static final String CAMPAIGN_CATEGORY_MAX_DEPTH = "dell.campaign.category.maxDepth";

    // Cloned item properties
    public static final String CAMPAIGN_ITEM_CLONED_PROP = "dell.campaign.item.cloned";
    public static final String CLONED_PRODUCT_ID_FORMAT = "CL-%s-%s";
    public static final String CAMPAIGN_CATEGORY_TREE_JSON_DATA = "campaign.category.json";

    /* Campaign management related methods */

    /**
     * Method to persist the Campaign object
     *
     * @param campaign - Campaign object to be persisted
     * @return - Return the campaign object
     */
    Campaign saveCampaign(Campaign campaign) throws CampaignAlreadyExistsException;

    /**
     * Method to load the Campaign by ID
     *
     * @param campaignID - Campaign ID
     * @return - Campaign object with ID = campaignID
     */
    Campaign getCampaign(Long campaignID) throws CampaignNotFoundException;

    Campaign getCampaign(Long campaignID, Boolean loadReferences) throws CampaignNotFoundException;

    /**
     * Method to delete a Campaign object by ID
     *
     * @param campaignID - Campaign ID
     */
    void deleteCampaign(Long campaignID) throws CampaignNotFoundException;

    /**
     * Method to retrieve the the Campaign objects list
     *
     * @return - List of Campaign's
     */
    Collection<Campaign> getCampaigns();

    /**
     * Retrieve the list of Campaign's using retailer siteID
     *
     * @param retailerSiteID
     * @return - Return the list of Campaign's with matching siteID
     * @see com.dell.acs.persistence.domain.RetailerSite
     */
    Collection<Campaign> getCampaignByRetailerSiteID(Long retailerSiteID);

    /**
     * Retrieve the list of Campaign's which has the retailer siteID within the collection retailerSites
     *
     * @param retailerSites - Collection of retailerSiteId's
     * @return - Matching campaign's
     */
    Collection<Campaign> getCampaignsByRetailerSite(Collection<Long> retailerSites);

    /* Campaign Item related methods */

    /**
     * Method to fetch all campaign items
     *
     * @param campaignID
     * @return
     */
    Collection<CampaignItemData> getCampaignItems(Long campaignID);

    /**
     * Method to fetch campaignItem record
     *
     * @param id
     * @return
     */
    CampaignItem getCampaignItem(Long id) throws CampaignItemNotFoundException;

    CampaignItem getCampaignItem(Long id, Boolean loadReferences) throws CampaignItemNotFoundException;

    /**
     * Method to delete campaignItem records based on campaignItemID
     *
     * @param campaignItemID
     * @throws CampaignItemNotFoundException
     */
    void deleteCampaignItem(Long campaignItemID, Long campaignID) throws CampaignItemNotFoundException;

    CampaignItem saveCampaignItem(CampaignItem item);


    /* Campaign category management related methods */
    CampaignCategory saveCategory(CampaignCategory category);

    CampaignCategory getCategory(Long id) throws CampaignCategoryNotFoundException;

    Integer getMaxItemPriority(Long campaignID) throws CampaignNotFoundException;

    Integer adjustItemPriorityBasedOnPosition(Long categoryID, Long referenceItemID, String position);

    Integer getMaxCategoryPosition(Long id, Long campaignID) throws CampaignCategoryNotFoundException;

    void deleteCategory(Long id) throws CampaignCategoryNotFoundException;

    File downloadCampaignItems(Long campaignID);

    Collection<Campaign> getCampaignsBySite(Long retailerSiteID);

    Collection<Campaign> getCampaignsBySiteandType(Long retailerSiteID, String campaignType);

    void updateCampaignItemLinks(Long campaignID, MultipartFile uploadFile, String pixelTrackingType) throws CampaignItemNotFoundException,
            CampaignNotFoundException, IOException, PixelTrackingException;


    Collection<Campaign> getActiveCampaignsBySiteName(String retailerSiteName, String campaignType) throws CampaignForInactiveRetailerException;

    /* Helper methods to update the properties */

    void updateCampaign(Campaign campaign);

    void updateCampaignItem(CampaignItem campaignItem);

    void updateCampaignCategory(CampaignCategory campaignCategory);

    String getBaseCDNPathForCampaign(Campaign campaign) throws CampaignNotFoundException, IOException;

    Collection<ProductReview> getCampaignItemReviews(CampaignItem campaignItem) throws ProductReviewNotFoundException;

    /**
     * Check whether campaign used the particular document or not.
     *
     * @param itemID , Store the document ID.
     * @return boolean
     */
    boolean checkDocumentAssociatedToCampaign(Long itemID);

    List<CampaignItem> getCampaignsDetailsByDocumentId(Long docID);

    /**
     * Introduced a method which loads just the Campaign object.
     * Did not re-factor the existing {@see getCampaignItem(Long id} as it is used in V1 service
     * @param campaignID - Long CamapaignID
     * @return A {@link Campaign} object if found. Else throws an EntityNotFoundException
     * @throws EntityNotFoundException
     */
    Campaign getCampaignForService(Long campaignID) throws EntityNotFoundException;



}
