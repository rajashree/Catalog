package com.dell.acs.pixeltracker;

import com.dell.acs.managers.CampaignManager;
import com.dell.acs.web.ws.v1.beans.WSCampaign;
import com.dell.acs.web.ws.v1.beans.WSCampaignItem;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * MarketVine specific URL transformer
 * @author : Vivek Kondur
 * Date: 6/20/12
 * Time: 3:06 PM
 */
public class MarketVinePixel implements PixelTracker {

    public static final Logger logger = Logger.getLogger(MarketVinePixel.class);

    @Override
    public Map<String, String> getLinks(PixelTrackerContext context) {
        logger.debug("Get MarketVinePixel links ");
        String LTCampaignId = "";
        WSCampaign campaign = context.getCampaign();
        WSCampaignItem item = context.getItem();
        Map<String, String> links = new HashMap<String, String>();
        // check if campaign.getProperties()
        Boolean isLTEnabled = campaign.getBooleanProperty(CampaignManager.CAMPAIGN_LT_LINKS_UPLOADED_PROP);
        //Fix for the NPE - https://jira.marketvine.com/browse/CS-492
        //Earlier we were getting a "Long LTCampaignId = campaign.getLongProperty("linkTrackerCampaignID");" We made changes
        //for ticket https://jira.marketvine.com/browse/CS-481 to support alphanumeric tracking IDs, not just longs.

        if( campaign.hasProperty("linkTrackerCampaignID"))
               LTCampaignId  = campaign.getProperty("linkTrackerCampaignID");

        if (isLTEnabled != null) {

            String trackerURL = null;
            String ltUrlKey = CampaignManager.CAMPAIGN_ITEM_LT_PROP + item.getId() + "." + LTCampaignId;


            if (item.hasProperty(ltUrlKey)) {
                trackerURL = item.getProperty(ltUrlKey);
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
