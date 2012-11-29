package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.*;
import com.dell.acs.managers.*;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.pixeltracker.PixelTrackerContext;
import com.dell.acs.web.ws.v2.CampaignService;
import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import java.util.*;

/**
 * @author Vivek Kondur
 * @version 1.0
 */
@Component(value = "CampaignServiceV2")
@WebService
@RequestMapping("/api/v2/rest/CampaignService")
public class CampaignServiceImpl extends WebServiceImpl implements CampaignService {

    /**
     * Returns all the Campaigns for specified RetailerSiteName / MerchantName
     *
     * @param site       - String - RetailerSiteName could be: Dell, DellMpp, DellBusiness, DellWorld
     * @param filterBean - ServiceFilterBean - see {@link com.sourcen.core.util.beans.ServiceFilterBean} for usage
     * @return Collection of {@link com.dell.acs.persistence.domain.Campaign}. Empty if no Campaigns are found.
     *         <p/>
     *         Also see {@link com.dell.acs.web.ws.v2.RetailerService}
     */
    @Override
    @RequestMapping("getCampaigns")
    public Collection<Campaign> getCampaigns(@RequestParam(required = true) String site, ServiceFilterBean filterBean) throws CampaignForInactiveRetailerException {
        return this.campaignManager.getActiveCampaignsBySiteName(site, Campaign.Type.HOTDEALS.getValue());

    }

    /**
     * Returns a Campaign for a specified campaignID
     *
     * @param campaignID - Long a campaignID
     * @param trackerID  - String a trackerID. The trackerID could be related to any pixel-tracking system.
     *                   It is an optional parameter.
     * @return Campaign {@link com.dell.acs.persistence.domain.Campaign}
     */
    @Override
    @RequestMapping("getCampaign")
    public Campaign getCampaign(@RequestParam(required = true) Long campaignID, String trackerID) throws CampaignNotFoundException, CampaignForInactiveRetailerException {
        Campaign campaign = this.campaignManager.getCampaignForService(campaignID);
        RetailerSite retailerSite = this.retailerManager.getRetailerSite(campaign.getRetailerSite().getSiteName(),true);
        logger.debug(" Campaign belongs to RetailerSite  :  " + retailerSite.getSiteName());

        if ((retailerSite.getActive() == false) ||
                (retailerSite.getRetailer().getActive() == false))
            throw new CampaignForInactiveRetailerException("Campaign's retailer/retailer site is inactive");

        Set<CampaignItem> items = campaign.getItems();

        long itemID = -1L;

        PixelTrackerContext trackerContext = App.getBean(PixelTrackerContext.class);
        PropertiesProvider properties = retailerSite.getProperties();
        String trackerType = properties.getProperty(RetailerManager.RETAILER_SITE_PIXEL_TRACKER_NAME_PROPERTY_KEY);
        trackerContext.setTrackerType(trackerType);
        trackerContext.setCampaignEntity(campaign);
        trackerContext.setApiVersion("V2");

        if (trackerID != null) {
            campaign.getProperties().setProperty("linkTrackerCampaignID", trackerID);
        }

        //Handling the URLs
        //1) PixelTracked URLs for Products
        //2) CDN URLs for different resources - Images, Attachments for various Documents - Image, Document, Video.
        for (CampaignItem item : items) {

            itemID = item.getItemID();
            logger.debug(" Item Type " + item.getItemType());
            if (item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.PRODUCT.getValue())) {
                handlePixelTracking(trackerContext, item);
            }

            handleDocumentURLs(item);

            if(item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.EVENT.getValue())){
                //TODO: Need to support handling of Properties transformation in the BeanConverter
                item.setEvent(this.eventManager.getEvent(itemID));
            }

        }

        return campaign;
    }

    /**
     * Helper method which handles the CDN URL prefix for different types of Document
     * @param item CampaignItem
     */
    protected void handleDocumentURLs(CampaignItem item) {
        Document document = null;

        if (item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.VIDEO.getValue())) {
            document = this.documentManager.getDocument(item.getItemID(), CampaignItem.Type.VIDEO.getEntityID());
            document.setImage(getCDNURL(document.getImage()));
        } else if (item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.DOCUMENT.getValue())) {
            document = this.documentManager.getDocument(item.getItemID(), CampaignItem.Type.DOCUMENT.getEntityID());
            document.setDocument(getCDNURL(document.getDocument()));
            document.setImage(getCDNURL(document.getImage()));
        } else if(item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.IMAGE.getValue())){
            document = this.documentManager.getDocument(item.getItemID(), CampaignItem.Type.IMAGE.getEntityID());
            document.setUrl(getCDNURL(document.getUrl()));
            document.setImage(getCDNURL(document.getImage()));
        }else if(item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.LINK.getValue())){
            document = this.documentManager.getDocument(item.getItemID(),CampaignItem.Type.LINK.getEntityID());
        }
        item.setDocument(document);
    }

    /**
     * Helper method which is responsbile for updating the following URLs on Product - Buy, Reviews, Info, URL.
     * The URLs on a Product will be updated only if there is an valid trackerID found on the {@link CampaignItem}
     * extended property. Currently, we are supporting only MarketVine & LinkTracker
     * @param itemID  productID
     * @param trackerContext PixelTrackerContext which handles the type of Tracker
     * @param item  CampaignItem
     */
    protected void handlePixelTracking(PixelTrackerContext trackerContext, CampaignItem item) {
        try {
            final long itemID = item.getItemID();
            Product product = this.productManager.getProduct(itemID);
            item.setProduct(product);
            trackerContext.setCampaignItem(item);
            Map<String, String> urls = trackerContext.handle();

            if (urls != null && urls.size() > 0) {

                if (urls.containsKey("prodURL")) {
                    product.setUrl(urls.get("prodURL"));
                }
                if (urls.containsKey("infoURL")) {
                    product.setInfoLink(urls.get("infoURL"));
                }
                if (urls.containsKey("reviewsURL")) {
                    product.setReviewsLink(urls.get("reviewsURL"));
                }
                if (urls.containsKey("buyURL")) {
                    product.setBuyLink(urls.get("buyURL"));
                }
            }

            Collection<ProductImage> images = product.getImages();
            for(ProductImage image : images){
                image.setImageURL(getCDNURL(image.getImageURL()));
            }

        } catch (ProductDisabledException pdEx) {
            logger.warn("Product is disabled. Therefore, will not be added on the campaign.\t"+pdEx.getMessage());
        } catch (ProductNotFoundException pnEx) {
            logger.warn("Product not found in the repository. Hence will be skipped.\t"+pnEx.getMessage());
        }
    }

    /**
     * Returns all the available reviews for a give CampaignItem
     *
     * @param campaignItemID - An unique ID for a {@link com.dell.acs.persistence.domain.CampaignItem}
     * @param filterBean     - ServiceFilterBean - see {@link com.sourcen.core.util.beans.ServiceFilterBean} for usage
     * @return Collection of reviews {@link com.dell.acs.persistence.domain.ProductReview}
     */
    @Override
    @RequestMapping("getCampaignItemReviews")
    public Collection<ProductReview> getCampaignItemReviews(@RequestParam ( required = true) Long campaignItemID, ServiceFilterBean filterBean)
                                            throws CampaignItemNotFoundException, CampaignForInactiveRetailerException,
                                                    ProductNotFoundException, ProductDisabledException, ProductReviewNotFoundException {
        Collection<ProductReview> productReviews = new ArrayList<ProductReview>();
        CampaignItem campaignItem = this.campaignManager.getCampaignItem(campaignItemID);
        RetailerSite retailerSite = campaignItem.getCampaign().getRetailerSite();


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
            productReviews = productManager.getTopProductReview(itemProduct, filterBean);
        }


        return productReviews;
    }

    private String getCDNURL(String contentURL) {
        String cdnPrefix = ConfigurationServiceImpl.getInstance().getProperty("filesystem.cdnPrefix", "");
        if(StringUtils.isNotEmpty(contentURL)){
            return cdnPrefix.trim().concat(contentURL);
        }
        return contentURL;
    }

    //IOC
    @Autowired
    private CampaignManager campaignManager;

    public void setCampaignManager(CampaignManager campaignManager) {
        this.campaignManager = campaignManager;
    }

    //IOC
    @Autowired
    ProductManager productManager;

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    @Autowired
    private DocumentManager documentManager;

    public void setDocumentManager(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Autowired
    private EventManager eventManager;

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Autowired
    private RetailerManager retailerManager;

    public void setRetailerManager(RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }
}
