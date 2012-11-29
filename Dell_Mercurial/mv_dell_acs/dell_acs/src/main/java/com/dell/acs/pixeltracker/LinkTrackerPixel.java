package com.dell.acs.pixeltracker;

import com.dell.acs.managers.CampaignManager;
import com.dell.acs.web.ws.v1.beans.WSCampaign;
import com.dell.acs.web.ws.v1.beans.WSCampaignItem;
import com.dell.acs.web.ws.v1.beans.WSProduct;
import com.sourcen.core.util.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * LinkTracker specific URL transformer
 * @author : Vivek Kondur
 * Date: 6/20/12
 * Time: 3:04 PM
 */
public class LinkTrackerPixel implements PixelTracker {

    public static final Logger logger = Logger.getLogger(LinkTrackerPixel.class);


    @Override
    public Map<String, String> getLinks(PixelTrackerContext context) {
        logger.debug("Get LinkTrackerPixel links ");
        String LTCampaignId = null;
        WSCampaign campaign = context.getCampaign();
        WSCampaignItem item = context.getItem();
        WSProduct product = item.getProduct();
        Map<String, String> links = new HashMap<String, String>();
        // check if campaign.getProperties()
        Boolean isLTEnabled = campaign.getBooleanProperty(CampaignManager.CAMPAIGN_LT_LINKS_UPLOADED_PROP);

        //Fix for the NPE - https://jira.marketvine.com/browse/CS-492
        //Earlier we were getting a "Long LTCampaignId = campaign.getLongProperty("linkTrackerCampaignID");" We made changes
        //for ticket https://jira.marketvine.com/browse/CS-481 to support alphanumeric tracking IDs, not just longs.
        if( campaign.hasProperty("linkTrackerCampaignID"))
            LTCampaignId  = campaign.getProperty("linkTrackerCampaignID");

        if (isLTEnabled != null && LTCampaignId != null) {

            String ltUrlValue = null;
            String ltUrlKey = CampaignManager.CAMPAIGN_ITEM_LT_PROP + item.getId() + "." + LTCampaignId;


            if (item.hasProperty(ltUrlKey)) {
                logger.info("   We have an active LT Tracker ID  match " + ltUrlKey);
                ltUrlValue = item.getProperty(ltUrlKey);

                //Fetching the only the linktracker url to append it to all the others
                int index = ltUrlValue.lastIndexOf("DURL=");
                String trackerURL = ltUrlValue.substring(0, index + 5);

                logger.info(" " + trackerURL);
                try {
                    if (StringUtils.isNotEmpty(product.getUrl())) {
                        links.put("prodURL", trackerURL + URLEncoder.encode(product.getUrl(), "UTF-8"));
                    } else {
                        links.put("prodURL", "");
                    }
                    if (StringUtils.isNotEmpty(product.getInfoLink())) {
                        links.put("infoURL", trackerURL + URLEncoder.encode(product.getInfoLink(), "UTF-8"));
                    } else {
                        links.put("infoURL", "");
                    }
                    if (StringUtils.isNotEmpty(product.getReviewsLink())) {
                        links.put("reviewsURL", trackerURL + URLEncoder.encode(product.getReviewsLink(), "UTF-8"));
                    } else {
                        links.put("reviewsURL", "");
                    }
                    if (StringUtils.isNotEmpty(product.getBuyLink())) {
                        links.put("buyURL", trackerURL + URLEncoder.encode(product.getBuyLink(), "UTF-8"));
                    } else {
                        links.put("buyURL", "");
                    }
                } catch (UnsupportedEncodingException unEx) {
                    logger.warn("Failed while encoding URLs for LinkTracker Pixel ", unEx);
                }


            }


        }
        return links;
    }
}
