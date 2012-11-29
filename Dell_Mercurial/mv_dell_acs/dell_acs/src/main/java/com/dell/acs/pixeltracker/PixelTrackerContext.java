package com.dell.acs.pixeltracker;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.web.ws.v1.beans.WSCampaign;
import com.dell.acs.web.ws.v1.beans.WSCampaignItem;
import com.sourcen.core.util.StringUtils;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

/**
 * Strategy Pattern for determining which PixelTracking mechanism needs to be
 * chosen for the Campaign.
 *
 * In version 1.0 - we supported two concrete implementations - {@link LinkTrackerPixel} and {@link MarketVinePixel}. For the
 * implementations, the URLs were being uploaded for a specific campaign from the admin console.
 *
 * In version 1.1 - We introduced different version for LinkTracker and MarketVine - {@link LinkTrackerPixelV2} &
 * {@link MarketVinePixelV2}. These implementations were necessary as we are deprecating the usage of WS*Beans like WSCampaign, WSProduct, etc
 *
 *
 * @author : Vivek Kondur
 * @version 1.1
 *
 */

public class PixelTrackerContext {

    public static final Logger logger = Logger.getLogger(PixelTrackerContext.class);

    private PixelTracker linkTrackerStrategy;

    private PixelTracker marketVineStrategy;

    private PixelTracker linkTrackerStrategyV2;

    private PixelTracker marketVineStrategyV2;

    private String trackerType;
    private WSCampaignItem item;
    private WSCampaign campaign;

    //Following variable introduced to support REST v2 changes.
    private Campaign campaignEntity;
    private CampaignItem campaignItem;
    private String apiVersion;


    public Map<String, String> handle() {
        if (!StringUtils.isEmpty(trackerType)) {
            logger.debug("\n   PixelTracking type :::                " + trackerType  +" \t  apiVersion "+apiVersion);

            if (EntityConstants.EnumPixelTracker.MARKETVINE.getTrackerName().equalsIgnoreCase(trackerType)) {
                if(this.apiVersion.equalsIgnoreCase("V2"))
                    return marketVineStrategyV2.getLinks(this);
                else
                    return marketVineStrategy.getLinks(this);
            } else if (EntityConstants.EnumPixelTracker.LINKTRACKER.getTrackerName().equalsIgnoreCase(trackerType)) {
                if(this.apiVersion.equalsIgnoreCase("V2"))
                    return linkTrackerStrategyV2.getLinks(this);
                else
                    return linkTrackerStrategy.getLinks(this);
            } else {
                logger.info("\n\n ###   Un-Supported or Pre-Tracked pixel   ### \n\n");
            }
        }
        return Collections.emptyMap();
    }


    public void setLinkTrackerStrategy(PixelTracker linkTrackerStrategy) {
        this.linkTrackerStrategy = linkTrackerStrategy;
    }

    public void setMarketVineStrategy(PixelTracker marketVineStrategy) {
        this.marketVineStrategy = marketVineStrategy;
    }

    public void setLinkTrackerStrategyV2(PixelTracker linkTrackerStrategyV2) {
        this.linkTrackerStrategyV2 = linkTrackerStrategyV2;
    }

    public void setMarketVineStrategyV2(PixelTracker marketVineStrategyV2) {
        this.marketVineStrategyV2 = marketVineStrategyV2;
    }

    public String getTrackerType() {
        return trackerType;
    }

    public void setTrackerType(String trackerType) {
        this.trackerType = trackerType;
    }

    public WSCampaignItem getItem() {
        return item;
    }

    public void setItem(WSCampaignItem item) {
        this.item = item;
    }

    public WSCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(WSCampaign campaign) {
        this.campaign = campaign;
    }

    public Campaign getCampaignEntity() {
        return campaignEntity;
    }

    public void setCampaignEntity(Campaign campaignEntity) {
        this.campaignEntity = campaignEntity;
    }

    public CampaignItem getCampaignItem() {
        return campaignItem;
    }

    public void setCampaignItem(CampaignItem campaignItem) {
        this.campaignItem = campaignItem;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
