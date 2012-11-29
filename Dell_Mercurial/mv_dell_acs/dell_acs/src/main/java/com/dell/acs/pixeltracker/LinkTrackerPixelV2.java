package com.dell.acs.pixeltracker;

import com.dell.acs.CampaignNotFoundException;
import com.dell.acs.managers.CampaignManager;

import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.CampaignItem;

import com.dell.acs.persistence.domain.Product;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Version 2 implementation of LinkTracker. The implementation does not have any reference to
 * WS* Beans for the URL composition. Instead the Entity objects are referenced.
 *
 * This is an effort from Sprint 4 onwards to deprecate the usage of WSBeans across various
 * modules.
 *
 * @author Vivek Kondur
 * @version 1.0
 *
 */
public class LinkTrackerPixelV2 implements PixelTracker {

    private static final Logger logger = Logger.getLogger(LinkTrackerPixelV2.class);

    @Override
    public Map getLinks(PixelTrackerContext context) {
        logger.debug(" Get LinkTracker links for v2 endpoint ");
        String LTCampaignId = null;
        Campaign campaign = context.getCampaignEntity();
        CampaignItem item = context.getCampaignItem();
        Product product = item.getProduct();
        Map<String, String> links = new HashMap<String, String>();
        // check if campaign.getProperties()
        PropertiesProvider campProps = campaign.getProperties();
        Boolean isLTEnabled = campProps.getBooleanProperty(CampaignManager.CAMPAIGN_LT_LINKS_UPLOADED_PROP);

        PropertiesProvider itemProps = item.getProperties();

        //Fix for the NPE - https://jira.marketvine.com/browse/CS-492
        //Earlier we were getting a "Long LTCampaignId = campaign.getLongProperty("linkTrackerCampaignID");" We made changes
        //for ticket https://jira.marketvine.com/browse/CS-481 to support alphanumeric tracking IDs, not just longs.
        if( campProps.hasProperty("linkTrackerCampaignID"))
            LTCampaignId  = campProps.getProperty("linkTrackerCampaignID");

        if (isLTEnabled != null && LTCampaignId != null) {

            String ltUrlValue = null;
            String ltUrlKey = CampaignManager.CAMPAIGN_ITEM_LT_PROP + item.getId() + "." + LTCampaignId;


            if (itemProps.hasProperty(ltUrlKey)) {
                logger.debug("   We have an active LT Tracker ID  match " + ltUrlKey);
                ltUrlValue = itemProps.getProperty(ltUrlKey);

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
