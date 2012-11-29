package com.dell.acs.web.ws.v2;

import com.dell.acs.*;
import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.ProductReview;
import com.dell.acs.web.ws.v1.beans.WSCampaign;
import com.sourcen.core.util.beans.ServiceFilterBean;
import com.sourcen.core.web.ws.WebService;

import java.util.Collection;

/**
 * Campaign Service is the second iteration of the existing {@link com.dell.acs.web.ws.v1.CampaignService}
 * In v2, we are standardizing the REST API definition and deprecating few end points.
 *
 * @author Vivek Kondur
 * @version 1.0
 *
 */
public interface CampaignService extends WebService {

    /**
     * Returns all Active Campaigns for specified RetailerSiteName / MerchantName
     *
     * @param retailerSiteName - String - RetailerSiteName could be: Dell, DellMpp, DellBusiness, DellWorld
     * @param filterBean - ServiceFilterBean - see {@link ServiceFilterBean} for usage
     * @return Collection of {@link Campaign}. Empty if no Campaigns are found.
     * @throws CampaignForInactiveRetailerException
     *
     * Also see {@link RetailerService}
     */
    Collection<Campaign> getCampaigns(String retailerSiteName,ServiceFilterBean filterBean) throws CampaignForInactiveRetailerException;

    /**
     * Returns a Campaign for a specified campaignID
     * @param campaignID - Long a campaignID
     * @param trackerID - String a trackerID. The trackerID could be related to any pixel-tracking system.
     *                  It is an optional parameter.
     * @return Campaign {@link Campaign}
     */
    Campaign getCampaign(Long campaignID, String trackerID) throws CampaignNotFoundException, CampaignForInactiveRetailerException;

    /**
     * Returns all the available reviews for a give CampaignItem
     * @param campaignItemID - An unique ID for a {@link com.dell.acs.persistence.domain.CampaignItem}
     * @param filterBean - ServiceFilterBean - see {@link ServiceFilterBean} for usage
     * @return Collection of reviews {@link ProductReview}
     */
    Collection<ProductReview> getCampaignItemReviews(Long campaignItemID, ServiceFilterBean filterBean) throws CampaignItemNotFoundException, CampaignForInactiveRetailerException, ProductNotFoundException, ProductDisabledException, ProductReviewNotFoundException;

}
