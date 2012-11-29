package com.dell.acs.pixeltracker;

import com.dell.acs.managers.CampaignManager;
import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.web.ws.v1.beans.WSCampaign;
import com.dell.acs.web.ws.v1.beans.WSCampaignItem;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Version 2 implementation of MarketVinePixel. The implementation does not have any reference to
 * WS* Beans for the URL composition. Instead the Entity objects are referenced.
 *
 * This is an effort from Sprint 4 onwards to deprecate the usage of WSBeans across various
 * modules.
 *
 * @author Vivek Kondur
 * @version 1.0
 */
public class MarketVinePixelV2 implements PixelTracker {

    private static final Logger logger = Logger.getLogger(MarketVinePixelV2.class);

    @Override
    public Map<String, String> getLinks(PixelTrackerContext context) {
        logger.debug(" Get Marketvine links for v2 endpoint ");
        Map<String, String> links = new HashMap<String, String>();
        String LTCampaignId = "";

        Campaign campaign = context.getCampaignEntity();
        CampaignItem item = context.getCampaignItem();
        Product product = item.getProduct();

        PropertiesProvider campProps = campaign.getProperties();
        PropertiesProvider itemProps = item.getProperties();

        // check if campaign.getProperties()
        Boolean isLTEnabled = campProps.getBooleanProperty(CampaignManager.CAMPAIGN_LT_LINKS_UPLOADED_PROP);
        //Fix for the NPE - https://jira.marketvine.com/browse/CS-492
        //Earlier we were getting a "Long LTCampaignId = campaign.getLongProperty("linkTrackerCampaignID");" We made changes
        //for ticket https://jira.marketvine.com/browse/CS-481 to support alphanumeric tracking IDs, not just longs.

        if( campProps.hasProperty("linkTrackerCampaignID"))
            LTCampaignId  = campProps.getProperty("linkTrackerCampaignID");

        if (isLTEnabled != null) {

            String trackerURL = null;
            String ltUrlKey = CampaignManager.CAMPAIGN_ITEM_LT_PROP + item.getId() + "." + LTCampaignId;


            if (itemProps.hasProperty(ltUrlKey)) {
                trackerURL = itemProps.getProperty(ltUrlKey);
                //TODO: Once we have MarketVine Pixel returning different pixel for - url, buyURL, InfoURL, reviewsURL - for each product
                //TODO: We will need to update the following section as well as the CSV and the Extended Property code.
                logger.info("  We have an active MarketVine pixel  " + ltUrlKey + trackerURL);
                links.put("prodURL", trackerURL);
                links.put("infoURL", trackerURL);
                links.put("buyURL", trackerURL);
                links.put("reviewsURL", trackerURL);
            }


        }
        return links;

    }
}
