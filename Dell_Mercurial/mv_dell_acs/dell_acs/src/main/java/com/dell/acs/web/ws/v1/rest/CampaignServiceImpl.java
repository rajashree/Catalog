/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.rest;

import com.dell.acs.CampaignForInactiveRetailerException;
import com.dell.acs.CampaignItemNotFoundException;
import com.dell.acs.CampaignNotFoundException;
import com.dell.acs.ProductReviewNotFoundException;
import com.dell.acs.managers.CampaignManager;
import com.dell.acs.managers.DocumentManager;
import com.dell.acs.managers.EventManager;
import com.dell.acs.managers.ProductManager;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.web.ws.v1.CampaignService;
import com.dell.acs.web.ws.v1.beans.WSBeanUtil;
import com.dell.acs.web.ws.v1.beans.WSCampaign;
import com.dell.acs.web.ws.v1.beans.WSProductReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import java.util.*;

/**
 * User: Chethan
 * Date: 3/27/12
 * Time: 2:08 PM
 */
@WebService(name = "CampaignService")
@RequestMapping("/api/v1/rest/CampaignService")
public class CampaignServiceImpl extends WebServiceImpl implements CampaignService {

    @SuppressWarnings("unused")
	private Set<Long> processedCategoriesList = new HashSet<Long>();

    //In order to have it backward we need to have outputFormat=dell and linkTrackerCampaignID=22221 as optional.
    @Override
    @RequestMapping("getCampaignById")
    public WSCampaign getCampaign(@RequestParam(required = true) Long campaignId,
                                  @RequestParam(required = false) String trackerID,
                                  @RequestParam(required = false) String trackerId,
                                  @RequestParam(required = false) String outputFormat,
                                  @RequestParam(required = false) String linkTrackerCampaignID) 
                          throws CampaignNotFoundException, CampaignForInactiveRetailerException, 
                          		 CampaignItemNotFoundException, ProductReviewNotFoundException {

        //Check whether the campaign is well before the EndDate.
        //@Chethan - Greg suggested we do not handle the expiration of the Campaign at the present :)

        //Support the previous API request parameter.
        if (linkTrackerCampaignID != null) {
            trackerID = linkTrackerCampaignID;
        }

        //Support the previous API request parameter, since "trackerId" is used few requests.
        if (trackerId != null) {
            trackerID = trackerId;
        }

        Campaign campaign = campaignManager.getCampaign(campaignId,false);
        RetailerSite retailerSite = campaign.getRetailerSite();
        
        // verify owning retailer is "active" (too many queries -- must be optimized later)
        if ((retailerSite.getActive() == false) || 
            (retailerSite.getRetailer().getActive() == false))
        	throw new CampaignForInactiveRetailerException("Campaign's retailer/retailer site is inactive");
        	
        if (trackerID != null) {
            campaign.getProperties().setProperty("linkTrackerCampaignID", trackerID);
        }

        //Setting Curated Reviews to Product Reviews
        Set<CampaignItem> campaignItems = campaign.getItems();
        for (CampaignItem campaignItem : campaignItems) {
            //To load the campaign item properties we need to explicitly load the campaign Item object. Since the hook
            // is attached to onFindObject() handler where all properties set to the loaded entity
            campaignItem = campaignManager.getCampaignItem(campaignItem.getId(), false);

            if (campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.PRODUCT.getValue())) {
                List<ProductReview> productReviews = (List) campaignManager.getCampaignItemReviews(campaignItem);
                if (campaignItem.getProduct() == null) {
                    /**
                     * CS-397: All APIs should not show up disabled products
                     */
                    //Check with product is null or disabled
                    try {
                        campaignItem.setProduct(productManager.getProduct(campaignItem.getItemID()));
                        // CS-555: Resolves the NullPointerException
                        //Set the Product Reviews only if we have a valid & active product
                        if (productReviews.size() > 0) {
                            campaignItem.getProduct().setProductReviews(productReviews);
                        } else {
                            campaignItem.getProduct().setProductReviews(productReviews);
                        }
                    } catch (Exception e) {
                        logger.error("Product with Id:="+campaignItem.getItemID()+" is disabled or Not Present");
                        e.printStackTrace();
                    }
                } else {
                    campaignItem.getProduct().setProductReviews(productReviews);
                }

            }else if (campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.EVENT.getValue())) {
                //CS-568 - FIX for Events not showing in V1 JSON
                campaignItem.setEvent(this.eventManager.getEvent(campaignItem.getItemID()));
            }else if (campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.DOCUMENT.getValue())) {
                campaignItem.setDocument(documentManager.getDocument(campaignItem.getItemID(), CampaignItem.Type.DOCUMENT.getEntityID()));
                //logger.info(campaignItem.getItemID() + " :: " + campaignItem.getItemType().getValue() + " DOCUMENT ");
            }else if (campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.IMAGE.getValue())) {
                campaignItem.setDocument(documentManager.getDocument(campaignItem.getItemID(), CampaignItem.Type.IMAGE.getEntityID()));
                //logger.info(campaignItem.getItemID() + " :: " + campaignItem.getItemType().getValue() + " IMAGE ");
            }else if (campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.LINK.getValue())) {
                campaignItem.setDocument(documentManager.getDocument(campaignItem.getItemID(), CampaignItem.Type.LINK.getEntityID()));
                //logger.info(campaignItem.getItemID() + " :: " + campaignItem.getItemType().getValue() + " LINK ");
            }else if (campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.VIDEO.getValue())) {
                campaignItem.setDocument(documentManager.getDocument(campaignItem.getItemID(), CampaignItem.Type.VIDEO.getEntityID()));
                //logger.info(campaignItem.getItemID() + " :: " + campaignItem.getItemType().getValue() + " VIDEO ");
            }
        }
        return WSBeanUtil.convert(campaign, new WSCampaign());

    }

    @Override
    @RequestMapping("getCampaigns")
    public Collection<WSCampaign> getCampaigns(@RequestParam(required = true) String retailerSiteName,
                                               @RequestParam(required = true) String campaignType) 
                                  throws CampaignForInactiveRetailerException, Exception {
        Collection<Campaign> campaigns = campaignManager.getActiveCampaignsBySiteName(retailerSiteName, campaignType);
        Collection<WSCampaign> campaignCollection = WSBeanUtil.convert(campaigns, WSCampaign.class);
        return campaignCollection;

    }

    /*@Override
    @RequestMapping("getCampaigns")
    public Collection<WSCampaign> getCampaigns(Long retailerSiteId, String campaignType) {
        Collection<Campaign> campaigns = campaignManager.getCampaignsBySiteandType(retailerSiteId,campaignType);
        return WSBeanUtil.convert(campaigns, WSCampaign.class);
    }*/

    @Override
    @RequestMapping("getCampaignsbyId")
    public Collection<WSCampaign> getCampaigns(@RequestParam(required = true) Long retailerSiteId) {
        Collection<Campaign> campaigns = campaignManager.getCampaignsBySite(retailerSiteId);
        Collection<WSCampaign> campaignCollection = WSBeanUtil.convert(campaigns, WSCampaign.class);
        return campaignCollection;
    }

    @Override
    @RequestMapping("getCampaignItemReviews")
    public Collection<WSProductReview> getCampaignItemReviews(@RequestParam(required = true) Long campaignItemID) throws CampaignForInactiveRetailerException, Exception {

        List<ProductReview> productReviews = new ArrayList<ProductReview>();
        //Getting campaignItem
        CampaignItem campaignItem = campaignManager.getCampaignItem(campaignItemID);
        RetailerSite retailerSite = campaignItem.getCampaign().getRetailerSite();
        
        // verify owning retailer is "active" (too many queries -- must be optimized later)
        if ((retailerSite.getActive() == false) ||
            (retailerSite.getRetailer().getActive() == false))
        	throw new CampaignForInactiveRetailerException("Campaign Item's retailer/retailer site is inactive");
        
        Product itemProduct = campaignItem.getProduct();
        if( itemProduct == null){
            itemProduct = productManager.getProduct(campaignItem.getItemID());
        }
        if (campaignItem.getProperties().hasProperty("dell.campaign.item.reviews")) {
            String reviewProperty = campaignItem.getProperties().getProperty("dell.campaign.item.reviews");
            String[] reviewIds = reviewProperty.split(",");
            for (int i = 0; i < reviewIds.length; i++) {
                ProductReview productReview = productManager.getProductReview(Long.parseLong(reviewIds[i]));
                productReviews.add(productReview);
            }
        } else {
            //Limiting the Max Product Reviews to 10
            productReviews = productManager.getTopProductReview(itemProduct, 10);
        }

        Collection<WSProductReview> productReviewsCollection = WSBeanUtil.convert(productReviews, WSProductReview.class);
        return productReviewsCollection;
    }

    //
    // IoC
    //
    @Autowired
    private CampaignManager campaignManager;

    public void setCampaignManager(CampaignManager campaignManager) {
        this.campaignManager = campaignManager;
    }

    //IOC
    @Autowired
    private EventManager eventManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private DocumentManager documentManager;

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }
}
