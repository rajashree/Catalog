/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1;

import com.dell.acs.CampaignForInactiveRetailerException;
import com.dell.acs.CampaignItemNotFoundException;
import com.dell.acs.CampaignNotFoundException;
import com.dell.acs.ProductReviewNotFoundException;
import com.dell.acs.web.ws.v1.beans.WSCampaign;
import com.dell.acs.web.ws.v1.beans.WSProductReview;
import com.sourcen.core.web.ws.WebService;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: chethanj $
 * @version $Revision: 2763 $, $Date:: 2012-05-29 21:45:15#$
 */
public interface CampaignService extends WebService {

    WSCampaign getCampaign(Long campaignId, String trackerID, String trackerId, String outputFormat, String linkTrackerCampaignID) throws CampaignNotFoundException, CampaignForInactiveRetailerException, CampaignItemNotFoundException, ProductReviewNotFoundException;

    Collection<WSCampaign> getCampaigns(String retailerSiteName, String campaignType) throws CampaignForInactiveRetailerException, Exception;

    //Collection<WSCampaign> getCampaigns(Long retailerSiteId, String campaignType);

    Collection<WSCampaign> getCampaigns(Long retailerSiteId) throws Exception;

    //TODO:remove it
    //String getCampaignbyId(Long campaignId);

    Collection<WSProductReview> getCampaignItemReviews(Long campaignItemID) throws CampaignForInactiveRetailerException, Exception;
}
