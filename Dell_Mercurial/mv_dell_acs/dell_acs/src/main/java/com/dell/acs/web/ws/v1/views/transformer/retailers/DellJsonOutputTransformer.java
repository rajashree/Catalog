/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.views.transformer.retailers;

import com.dell.acs.managers.CampaignManager;
import com.dell.acs.managers.DocumentManager;
import com.dell.acs.managers.EventManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.pixeltracker.PixelTrackerContext;
import com.dell.acs.web.ws.v1.beans.*;
import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.collections.PropertiesProvider;
import com.sourcen.core.web.ws.beans.WSProperty;
import com.sourcen.core.web.ws.beans.base.WSBean;
import com.sourcen.core.web.ws.views.transformer.StandardJsonOutputTransformer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: vivekk $
 * @version $Revision: 3446 $, $Date:: 2012-06-20 18:14:05#$
 */
public class DellJsonOutputTransformer extends StandardJsonOutputTransformer {


    private RetailerSiteRepository retailerSiteRepository;
    private String prodURL;
    private String inforURL;
    private String buyURL;
    private String reviewsURL;
    private static String cdnPrefix = ConfigurationServiceImpl.getInstance().getProperty("filesystem.cdnPrefix", "");

    protected String transform(final WSRetailer bean) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bean.getName(), jsonConfig);
        jsonArray.add(bean.getName(), jsonConfig);
        jsonArray.add(bean.getDescription(), jsonConfig);
        jsonArray.add(bean.getUrl(), jsonConfig);
        return jsonArray.toString();
    }

    protected String transform(final WSCampaign campaign) {
        PixelTrackerContext context = App.getBean(PixelTrackerContext.class);
        this.retailerSiteRepository = App.getBean(RetailerSiteRepository.class);

        JSONObject result = new JSONObject();
        result.accumulate("success", true);
        result.accumulate("message", "");
        Collection<JSONObject> jsonCategories = new LinkedHashSet<JSONObject>();
        Map<Long, WSCampaignCategory> categoryMap = Collections.synchronizedMap(new LinkedHashMap<Long, WSCampaignCategory>());
        if (campaign.getCategories() != null) {
            for (WSCampaignCategory campaignCategory : campaign.getCategories()) {
                categoryMap.putAll(getCategoryMap(campaignCategory));
            }
        }


        // convert this to any order we need.
        Collection<WSCampaignCategory> finalCategories = new TreeSet<WSCampaignCategory>(new Comparator<WSCampaignCategory>() {
            @Override
            public int compare(final WSCampaignCategory o1, final WSCampaignCategory o2) {
                return new Long(o1.getId() - o2.getId()).intValue();
            }
        });
        if (campaign.getCategories() != null) {
            for (WSCampaignCategory campaignCategory : campaign.getCategories()) {
                finalCategories.addAll(getFilteredCategories(campaignCategory));
            }
        }

        for (WSCampaignCategory category : finalCategories) {
            JSONObject jsonCategory = new JSONObject();
            Collection<String> parentNames = new LinkedList<String>();
            parentNames.add(category.getName());
            if (category.getParent() != null) {
                WSCampaignCategory parent = categoryMap.get(category.getParent());
                while (parent != null) {
                    parentNames.add(parent.getName());
                    parent = categoryMap.get(parent.getParent());
                }
            }

            String[] parentNamesArray = new String[parentNames.size()];
            parentNamesArray = parentNames.toArray(parentNamesArray);
            ArrayUtils.reverse(parentNamesArray);
            jsonCategory.accumulate("Category", parentNamesArray);


            Collection<WSProduct> products = new LinkedHashSet<WSProduct>();
            //Collection of Events and Documents
            Collection<WSEvent> events = new LinkedHashSet<WSEvent>();
            Collection<WSDocument> documents = new LinkedHashSet<WSDocument>();
            //Determine the pixelTracker


            PropertiesProvider properties = retailerSiteRepository.getByName(campaign.getRetailerSite().getSiteName(), true).getProperties();
            String trackerType = properties.getProperty(RetailerManager.RETAILER_SITE_PIXEL_TRACKER_NAME_PROPERTY_KEY);
            context.setTrackerType(trackerType);
            context.setCampaign(campaign);
            //Introduced APIVersion to support Version 2 endpoint support
            context.setApiVersion("V1");


            for (WSCampaignItem item : category.getItems()) {
                WSProduct product = item.getProduct();
                if (product != null) {

                    context.setItem(item);
                    //Get the appropriate Pixel Tracking URLs for the item.
                    Map<String, String> urls = context.handle();

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

                    //Setting CampaignItem Start and End Time to Product Start and End Time
                    //String startTimeKey = "dell.campaign." + item.getId() + ".startTime";
                    String startTimeKey = String.format(CampaignManager.CAMPAIGN_ITEM_START_TIME_PROP, item.getId());
                    if (item.hasProperty(startTimeKey)) {
                        Long startTime = item.getLongProperty(startTimeKey);
                        WSProperty wsProperty = new WSProperty("startTime", startTime.toString());
                        product.getProperties().add(wsProperty);
                    }
                    //String endTimeKey = "dell.campaign." + item.getId() + ".endTime";
                    String endTimeKey = String.format(CampaignManager.CAMPAIGN_ITEM_END_TIME_PROP, item.getId());
                    if (item.hasProperty(endTimeKey)) {
                        Long endTime = item.getLongProperty(endTimeKey);
                        WSProperty wsProperty = new WSProperty("endTime", endTime.toString());
                        product.getProperties().add(wsProperty);
                    }
                    //Setting showCategory to false
                    product.setShowCategory(false);
                    products.add(product);
                }
                //Setting Events
                WSEvent event = item.getEvent();
                if (event != null) {
                    events.add(event);
                }
                //Setting Documents
                WSDocument document = item.getDocument();
                if (document != null) {
                    documents.add(document);
                }
            }
            jsonCategory.accumulate("Products", transform(products));
            //Accumulating Event
            jsonCategory.accumulate("Events", transform(events));
            //Accumulating Document
            jsonCategory.accumulate("Documents", transform(documents));
            jsonCategories.add(jsonCategory);
        }

        result.accumulate("data", jsonCategories);
        return result.toString();
    }

    // This block is used by merchant service - getPagedProductReviews
    // http://localhost:8080/dell_acs/api/v1/rest/MerchantService/getPagedProductReviews.json?productId=1&pageSize=15&pageNumber=4
    protected Object transform(final WSProductReview bean) {
        JSONArray innerReviews = new JSONArray();
        innerReviews.add(bean.getName());
        innerReviews.add(bean.getTitle());
        innerReviews.add(bean.getStars());
        innerReviews.add(bean.getLocation());
        innerReviews.add(bean.getReview());
        return super.transform(innerReviews);
    }

    protected Object transform(final WSRetailerSite bean) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bean.getCreatedDate(), jsonConfig);
        jsonArray.add(bean.getLogoUri(), jsonConfig);
        jsonArray.add(bean.getModifiedDate(), jsonConfig);
        jsonArray.add(bean.getSiteName(), jsonConfig);
        jsonArray.add(bean.getSiteUrl(), jsonConfig);
        return super.transform(jsonArray);
    }

    private Map<Long, WSCampaignCategory> getCategoryMap(WSCampaignCategory campaignCategory) {
        Map<Long, WSCampaignCategory> result = Collections.synchronizedMap(new LinkedHashMap<Long, WSCampaignCategory>());
        result.put(campaignCategory.getId(), campaignCategory);
        if (campaignCategory.getChildren() != null && !campaignCategory.getChildren().isEmpty()) {
            for (WSCampaignCategory child : campaignCategory.getChildren()) {
                result.putAll(getCategoryMap(child));
            }
        }
        return result;
    }

    private Collection<WSCampaignCategory> getFilteredCategories(WSCampaignCategory campaignCategory) {
        Collection<WSCampaignCategory> result = Collections.synchronizedSet(new LinkedHashSet<WSCampaignCategory>());
        if (campaignCategory.getItems() != null && !campaignCategory.getItems().isEmpty()) {
            result.add(campaignCategory);
        }

        if (campaignCategory.getChildren() != null && !campaignCategory.getChildren().isEmpty()) {
            for (WSCampaignCategory child : campaignCategory.getChildren()) {
                result.addAll(getFilteredCategories(child));
            }
        }
        return result;

    }

    protected Object transform(WSProduct bean) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bean.getId(), jsonConfig);
        jsonArray.add(bean.getUrl(), jsonConfig);
        jsonArray.add(bean.getSiteName(), jsonConfig);
        jsonArray.add(bean.getProductId(), jsonConfig);
        jsonArray.add(bean.getSku(), jsonConfig);
        jsonArray.add(bean.getWebPartNumber(), jsonConfig);
        jsonArray.add(bean.getTitle(), jsonConfig);
        jsonArray.add(bean.getDescription(), jsonConfig);
        jsonArray.add(bean.getNewProduct(), jsonConfig);
        jsonArray.add(bean.getBestSeller(), jsonConfig);
        jsonArray.add(bean.getPrice(), jsonConfig);
        jsonArray.add(bean.getHasPriceRange(), jsonConfig);
        jsonArray.add(bean.getListPrice(), jsonConfig);
        jsonArray.add(bean.getHasListPriceRange(), jsonConfig);
        jsonArray.add("USD", jsonConfig);
        jsonArray.add(bean.getClearanceTag(), jsonConfig);
        jsonArray.add(bean.getSaleTag(), jsonConfig);
        jsonArray.add(bean.getPriceCutTag(), jsonConfig);
        jsonArray.add(bean.getTempPriceCutTag(), jsonConfig);
        jsonArray.add(bean.getHasVariations(), jsonConfig);
        jsonArray.add(bean.getSpecifications(), jsonConfig);
        jsonArray.add(bean.getEstimatedShipDate(), jsonConfig);
        jsonArray.add(bean.getPromotional(), jsonConfig);
        jsonArray.add(bean.getShippingPromotion(), jsonConfig);
        jsonArray.add(bean.getBuyLink(), jsonConfig);
        jsonArray.add(bean.getFlashLink(), jsonConfig);
        jsonArray.add(bean.getRating(), jsonConfig);
        jsonArray.add(bean.getReviews(), jsonConfig);
        jsonArray.add(bean.getReviewsLink(), jsonConfig);
        jsonArray.add(bean.getFacebookLikes(), jsonConfig);
        jsonArray.add(bean.getPlusOneGoogle(), jsonConfig);
        jsonArray.add(bean.getTweets(), jsonConfig);
        jsonArray.add(StringUtils.toString(bean.getUpdateDateTime()), jsonConfig);

        //images
        Collection<WSProductImage> images = bean.getImages();
        JSONArray imagesJson = new JSONArray();
        //String cdnPrefix = ConfigurationServiceImpl.getInstance().getProperty("filesystem.cdnPrefix", "");
        for (WSProductImage image : images) {
            String imagePath = image.getImageURL();
            imagesJson.add(imagePath, jsonConfig);
        }
        jsonArray.add(imagesJson, jsonConfig);

        //AlsoBought, AlsoViewed, OtherRelated,future options

        List<WSProductSlider> sliders = (List<WSProductSlider>) bean.getSliders();


        JSONArray jsonAlsoBrought = new JSONArray();
        JSONArray jsonAlsoViewed = new JSONArray();
        JSONArray jsonOtherProducts = new JSONArray();
        JSONArray reviews = new JSONArray();
        JSONArray jsonFutureOptions = new JSONArray();


        for (WSProductSlider slider : sliders) {
            if (slider.getTitle().equals("AlsoBought")) {
                //AlsoBought
                JSONArray jsonInnerAlsoBrought = new JSONArray();
                jsonInnerAlsoBrought.add(slider.getTargetProduct().getId());
                jsonInnerAlsoBrought.add(slider.getTargetProduct().getPrice());
                jsonInnerAlsoBrought.add(slider.getTargetProduct().getTitle());
                jsonInnerAlsoBrought.add(slider.getTargetProduct().getUrl());
                //jsonInnerAlsoBrought.add(slider.getTargetProduct().getImages());
                jsonAlsoBrought.add(jsonInnerAlsoBrought);
            }
            if (slider.getTitle().equals("AlsoViewed")) {
                //AlsoViewed
                JSONArray jsonInnerAlsoViewed = new JSONArray();
                jsonInnerAlsoViewed.add(slider.getTargetProduct().getId());
                jsonInnerAlsoViewed.add(slider.getTargetProduct().getPrice());
                jsonInnerAlsoViewed.add(slider.getTargetProduct().getTitle());
                jsonInnerAlsoViewed.add(slider.getTargetProduct().getUrl());
                //jsonInnerAlsoViewed.add(slider.getTargetProduct().getImages());
                jsonAlsoViewed.add(jsonInnerAlsoViewed);
            }
            if (slider.getTitle().equals("OtherProducts")) {
                //OtherProducts/OtherRelated
                JSONArray jsonInnerOtherProducts = new JSONArray();
                jsonInnerOtherProducts.add(slider.getTargetProduct().getId());
                jsonInnerOtherProducts.add(slider.getTargetProduct().getPrice());
                jsonInnerOtherProducts.add(slider.getTargetProduct().getTitle());
                jsonInnerOtherProducts.add(slider.getTargetProduct().getUrl());
                //jsonInnerOtherProducts.add(slider.getTargetProduct().getImages());
                jsonOtherProducts.add(jsonInnerOtherProducts);
            }

        }

        //Reviews
        List<WSProductReview> reviewList = (List<WSProductReview>) bean.getProductReviews();
        for (WSProductReview wsProductReview : reviewList) {
            JSONArray innerReviews = new JSONArray();
            innerReviews.add(wsProductReview.getName());
            innerReviews.add(wsProductReview.getTitle());
            innerReviews.add(wsProductReview.getStars());
            innerReviews.add(wsProductReview.getLocation());
            innerReviews.add(wsProductReview.getReview());
            reviews.add(innerReviews);
        }

        //Future Options
        //All new props in the future needs to go into the Future options
        //Product Start Time
        Date startTime = null;
        if (bean.hasProperty("startTime")) {
            startTime = new Date(Long.parseLong(bean.getProperty("startTime")));
            JSONArray innerStartTime = new JSONArray();
            innerStartTime.add("startTime", jsonConfig);
            innerStartTime.add(StringUtils.toString(startTime), jsonConfig);
            jsonFutureOptions.add(innerStartTime, jsonConfig);
        }

        //Product End Time
        Date endTime = null;
        if (bean.hasProperty("endTime")) {
            endTime = new Date(Long.parseLong(bean.getProperty("endTime")));
            JSONArray innerEndTime = new JSONArray();
            innerEndTime.add("endTime", jsonConfig);
            innerEndTime.add(StringUtils.toString(endTime), jsonConfig);
            jsonFutureOptions.add(innerEndTime, jsonConfig);
        }

        jsonArray.add(jsonAlsoBrought);
        jsonArray.add(jsonAlsoViewed);
        jsonArray.add(jsonOtherProducts);
        jsonArray.add(reviews);
        jsonArray.add(jsonFutureOptions);


        if (bean.getShowCategory() == null || bean.getShowCategory()) {
            JSONObject result = new JSONObject();
            result.accumulate("success", true);
            result.accumulate("message", "");
            Collection<JSONObject> jsonCategories = new LinkedHashSet<JSONObject>();
            Map<String, WSTaxonomyCategory> categoryMap =
                    Collections.synchronizedMap(new LinkedHashMap<String, WSTaxonomyCategory>());

            //Get leafCategory
            //WSTaxonomyCategory leafCategory = bean.getCategory();

            //TODO: have to changed how to get taxonomyManager Instance
            //FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("classpath:/spring/applicationContext.xml");
            //TaxonomyManager taxonomyManager = context.getBean("taxonomyManager", TaxonomyManager.class);

            //Get list of Parent of the current leafCategory
            //Collection<TaxonomyCategory> taxonomyCategories = taxonomyManager.getTree(taxonomyManager.getTaxonomyCategory(leafCategory.getId()));
            //List<WSTaxonomyCategory> parentCategories = (List) WSBeanUtil.convert(taxonomyCategories, WSTaxonomyCategory.class);


            /*Collection<WSTaxonomyCategory> categories = new ArrayList<WSTaxonomyCategory>();
            //adding parent categories to categories collection
            for (int i = 0; i < parentCategories.size(); i++) {
                WSTaxonomyCategory category = parentCategories.get(i);
                if (category.getParent() != null) {
                    categories.add(category);
                }
            }*/
            //adding leaf category to categories collection
            //categories.add(leafCategory);

            //Adding parentNames to Category Json Array
            //Collection<String> parentNames = new LinkedHashSet<String>();
            JSONObject jsonCategory = new JSONObject();
            //adding category names to parentNames Collection
            /* for (WSTaxonomyCategory category : categories) {
                parentNames.add(category.getName());
            }*/

            //String[] parentNamesArray = new String[parentNames.size()];
            //parentNamesArray = parentNames.toArray(parentNamesArray);
            //jsonCategory.accumulate("Category", parentNamesArray);
            jsonCategory.accumulate("Products", super.transform(jsonArray));
            jsonCategories.add(jsonCategory);

            result.accumulate("data", jsonCategories);
            return result.toString();
        } else {
            return super.transform(jsonArray);
        }
    }

    protected Object transform(WSEvent bean) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bean.getId(), jsonConfig);
        jsonArray.add(bean.getName(), jsonConfig);
        jsonArray.add(bean.getDescription(), jsonConfig);
        jsonArray.add(bean.getCreationDate(), jsonConfig);
        jsonArray.add(bean.getModifiedDate(), jsonConfig);
        jsonArray.add(bean.getStartDate(), jsonConfig);
        jsonArray.add(bean.getEndDate(), jsonConfig);
        jsonArray.add(bean.getLocation(), jsonConfig);

        //Adding Event Properties
        JSONArray jsonProperties = new JSONArray();

        String numberOfDays;
        if (bean.hasProperty(EventManager.EVENT_NUMBER_OF_DAYS_PROPERTIES)) {
            numberOfDays = bean.getProperty(EventManager.EVENT_NUMBER_OF_DAYS_PROPERTIES);
            JSONArray innerNumberOfDays = new JSONArray();
            innerNumberOfDays.add(numberOfDays, jsonConfig);
            jsonProperties.add(innerNumberOfDays, jsonConfig);
        }

        String infoLink;
        if (bean.hasProperty(EventManager.EVENT_INFO_LINK_PROPERTIES)) {
            infoLink = bean.getProperty(EventManager.EVENT_INFO_LINK_PROPERTIES);
            JSONArray innerInfoLink = new JSONArray();
            innerInfoLink.add(infoLink, jsonConfig);
            jsonProperties.add(innerInfoLink, jsonConfig);
        }

        String buyLink;
        if (bean.hasProperty(EventManager.EVENT_BUY_LINK_PROPERTIES)) {
            buyLink = bean.getProperty(EventManager.EVENT_BUY_LINK_PROPERTIES);
            JSONArray innerBuyLink = new JSONArray();
            innerBuyLink.add(buyLink, jsonConfig);
            jsonProperties.add(innerBuyLink, jsonConfig);
        }

        String price;
        if (bean.hasProperty(EventManager.EVENT_PRICE_PROPERTIES)) {
            price = bean.getProperty(EventManager.EVENT_PRICE_PROPERTIES);
            JSONArray innerPrice = new JSONArray();
            innerPrice.add(price, jsonConfig);
            jsonProperties.add(innerPrice, jsonConfig);
        }

        String listPrice;
        if (bean.hasProperty(EventManager.EVENT_LIST_PRICE_PROPERTIES)) {
            listPrice = bean.getProperty(EventManager.EVENT_LIST_PRICE_PROPERTIES);
            JSONArray innerListPrice = new JSONArray();
            innerListPrice.add(listPrice, jsonConfig);
            jsonProperties.add(innerListPrice, jsonConfig);
        }

        String perNpeople;
        if (bean.hasProperty(EventManager.EVENT_PER_N_PEOPLE_PROPERTIES)) {
            perNpeople = bean.getProperty(EventManager.EVENT_PER_N_PEOPLE_PROPERTIES);
            JSONArray innerPerNpeople = new JSONArray();
            innerPerNpeople.add(perNpeople, jsonConfig);
            jsonProperties.add(innerPerNpeople, jsonConfig);
        }

        String minimumPeople;
        if (bean.hasProperty(EventManager.EVENT_MINIMUM_PEOPLE_PROPERTIES)) {
            minimumPeople = bean.getProperty(EventManager.EVENT_MINIMUM_PEOPLE_PROPERTIES);
            JSONArray innerMinimumPeople = new JSONArray();
            innerMinimumPeople.add(minimumPeople, jsonConfig);
            jsonProperties.add(innerMinimumPeople, jsonConfig);
        }

        String classOfService;
        if (bean.hasProperty(EventManager.EVENT_CLASS_OF_SERVICE_PROPERTIES)) {
            classOfService = bean.getProperty(EventManager.EVENT_CLASS_OF_SERVICE_PROPERTIES);
            JSONArray innerClassOfService = new JSONArray();
            innerClassOfService.add(classOfService, jsonConfig);
            jsonProperties.add(innerClassOfService, jsonConfig);
        }

        String reviewLink;
        if (bean.hasProperty(EventManager.EVENT_REVIE_LINK_PROPERTIES)) {
            reviewLink = bean.getProperty(EventManager.EVENT_REVIE_LINK_PROPERTIES);
            JSONArray innerReviewLink = new JSONArray();
            innerReviewLink.add(reviewLink, jsonConfig);
            jsonProperties.add(innerReviewLink, jsonConfig);
        }

        String facebookLikes;
        if (bean.hasProperty(EventManager.EVENT_FACEBOOK_LIKES_PROPERTIES)) {
            facebookLikes = bean.getProperty(EventManager.EVENT_FACEBOOK_LIKES_PROPERTIES);
            JSONArray innerFacebookLikes = new JSONArray();
            innerFacebookLikes.add(facebookLikes, jsonConfig);
            jsonProperties.add(innerFacebookLikes, jsonConfig);
        }

        String twitter;
        if (bean.hasProperty(EventManager.EVENT_TWITTER_PROPERTIES)) {
            twitter = bean.getProperty(EventManager.EVENT_TWITTER_PROPERTIES);
            JSONArray innerTwitter = new JSONArray();
            innerTwitter.add(twitter, jsonConfig);
            jsonProperties.add(innerTwitter, jsonConfig);
        }

        String googlePluses;
        if (bean.hasProperty(EventManager.EVENT_GOOGLE_PLUSES_PROPERTIES)) {
            googlePluses = bean.getProperty(EventManager.EVENT_GOOGLE_PLUSES_PROPERTIES);
            JSONArray innerGooglePluses = new JSONArray();
            innerGooglePluses.add(googlePluses, jsonConfig);
            jsonProperties.add(innerGooglePluses, jsonConfig);
        }

        String ratings;
        if (bean.hasProperty(EventManager.EVENT_RATINGS_PROPERTIES)) {
            ratings = bean.getProperty(EventManager.EVENT_RATINGS_PROPERTIES);
            JSONArray innerRatings = new JSONArray();
            innerRatings.add(ratings, jsonConfig);
            jsonProperties.add(innerRatings, jsonConfig);
        }

        String stars;
        if (bean.hasProperty(EventManager.EVENT_STARS_PROPERTIES)) {
            stars = bean.getProperty(EventManager.EVENT_STARS_PROPERTIES);
            JSONArray innerStars = new JSONArray();
            innerStars.add(stars, jsonConfig);
            jsonProperties.add(innerStars, jsonConfig);
        }

        String thumbnail;
        if (bean.hasProperty(EventManager.EVENT_THUMBNAIL_PROPERTIES)) {
            thumbnail = bean.getProperty(EventManager.EVENT_THUMBNAIL_PROPERTIES);
            JSONArray innerThumbnail = new JSONArray();
            //CS-555: Adding the CDN pre-fix for Image found for Event contenttype
            innerThumbnail.add(cdnPrefix + thumbnail, jsonConfig);
            jsonProperties.add(innerThumbnail, jsonConfig);
        }

        jsonArray.add(jsonProperties, jsonConfig);

        return super.transform(jsonArray);
    }

    protected Object transform(WSDocument bean) {

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bean.getId(), jsonConfig);
        jsonArray.add(bean.getName(), jsonConfig);
        //Not showing Images since not used.
        //jsonArray.add(bean.getImage(), jsonConfig);
        //Prefixing document url with cdnPrefix
        // Image - url
        // Video - image, url
        // Link - url
        if(bean.getContentType().equalsIgnoreCase(CampaignItem.Type.DOCUMENT.name())){
            jsonArray.add(cdnPrefix + bean.getDocument(), jsonConfig);

        }else if(bean.getContentType().equalsIgnoreCase(CampaignItem.Type.VIDEO.name()) ||
                bean.getContentType().equalsIgnoreCase(CampaignItem.Type.LINK.name())){
            jsonArray.add(bean.getUrl(), jsonConfig);
        } else if(bean.getContentType().equalsIgnoreCase(CampaignItem.Type.IMAGE.name())){
            //CS-555: Adding the CDN pre-fix for Image content type
            jsonArray.add(cdnPrefix + bean.getUrl(), jsonConfig);
        }

        // Add the content type
        // https://jira.marketvine.com/browse/CS-562
//        jsonArray.add(bean.getContentType(), jsonConfig);
        jsonArray.add(bean.getDescription(), jsonConfig);
        jsonArray.add(bean.getCreationDate(), jsonConfig);
        jsonArray.add(bean.getModifiedDate(), jsonConfig);
        jsonArray.add(bean.getStartDate(), jsonConfig);
        jsonArray.add(bean.getEndDate(), jsonConfig);

        //Document Properties
        JSONArray jsonProperties = new JSONArray();
        String thumbnail;
        if (bean.hasProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY)) {
            thumbnail = bean.getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY);
            JSONArray innerThumbnail = new JSONArray();
            innerThumbnail.add(cdnPrefix + thumbnail, jsonConfig);
            jsonProperties.add(innerThumbnail, jsonConfig);
        } if( bean.getImage() != null ){
            //CS-555: Any document which is created post Sprint 4, the thumbnail is saved within '[t_documents].[image]' column.
            thumbnail = bean.getImage();
            JSONArray innerThumbnail = new JSONArray();
            innerThumbnail.add(cdnPrefix + thumbnail, jsonConfig);
            jsonProperties.add(innerThumbnail, jsonConfig);
        }
        jsonArray.add(jsonProperties, jsonConfig);
        return super.transform(jsonArray);
    }

    @Override
    protected Object transformBean(final WSBean bean) {
        if (bean instanceof WSRetailer) {
            return transform((WSRetailer) bean);
        } else if (bean instanceof WSCampaign) {
            return transform((WSCampaign) bean);
        } else if (bean instanceof WSRetailerSite) {
            return transform((WSRetailerSite) bean);
        } else if (bean instanceof WSProduct) {
            return transform((WSProduct) bean);
        } else if (bean instanceof WSEvent) {
            return transform((WSEvent) bean);
        } else if (bean instanceof WSDocument) {
            return transform((WSDocument) bean);
        }
        // Chethan - To validate is this right place to add Dell specific ProductReview data
        // EXTERNALINTERACTIVEADS-352
        else if (bean instanceof WSProductReview) {
            return transform((WSProductReview) bean);
        }
        return super.transformBean(bean);
    }

    protected Object transformCustomProduct(WSProduct bean) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bean.getId(), jsonConfig);
        jsonArray.add(bean.getUrl(), jsonConfig);
        jsonArray.add(bean.getSiteName(), jsonConfig);
        jsonArray.add(bean.getProductId(), jsonConfig);
        jsonArray.add(bean.getSku(), jsonConfig);
        jsonArray.add(bean.getWebPartNumber(), jsonConfig);
        jsonArray.add(bean.getTitle(), jsonConfig);
        jsonArray.add(bean.getDescription(), jsonConfig);
        jsonArray.add(bean.getNewProduct(), jsonConfig);
        jsonArray.add(bean.getBestSeller(), jsonConfig);
        jsonArray.add(bean.getPrice(), jsonConfig);
        jsonArray.add(bean.getHasPriceRange(), jsonConfig);
        jsonArray.add(bean.getListPrice(), jsonConfig);
        jsonArray.add(bean.getHasListPriceRange(), jsonConfig);
        jsonArray.add("USD", jsonConfig);
        jsonArray.add(bean.getClearanceTag(), jsonConfig);
        jsonArray.add(bean.getSaleTag(), jsonConfig);
        jsonArray.add(bean.getPriceCutTag(), jsonConfig);
        jsonArray.add(bean.getTempPriceCutTag(), jsonConfig);
        jsonArray.add(bean.getHasVariations(), jsonConfig);
        jsonArray.add(bean.getSpecifications(), jsonConfig);
        jsonArray.add(bean.getEstimatedShipDate(), jsonConfig);
        jsonArray.add(bean.getPromotional(), jsonConfig);
        jsonArray.add(bean.getShippingPromotion(), jsonConfig);
        jsonArray.add(bean.getBuyLink(), jsonConfig);
        jsonArray.add(bean.getFlashLink(), jsonConfig);
        jsonArray.add(bean.getRating(), jsonConfig);
        jsonArray.add(bean.getReviews(), jsonConfig);
        jsonArray.add(bean.getReviewsLink(), jsonConfig);
        jsonArray.add(bean.getFacebookLikes(), jsonConfig);
        jsonArray.add(bean.getPlusOneGoogle(), jsonConfig);
        jsonArray.add(bean.getTweets(), jsonConfig);
        jsonArray.add(StringUtils.toString(bean.getUpdateDateTime()), jsonConfig);


        //images
        Collection<WSProductImage> images = bean.getImages();
        JSONArray imagesJson = new JSONArray();

        for (WSProductImage image : images) {
            String imagePath = image.getImageURL();
            imagesJson.add(imagePath, jsonConfig);
        }
        jsonArray.add(imagesJson, jsonConfig);


        //AlsoBought, AlsoViewed, OtherRelated,future options

        List<WSProductSlider> sliders = (List<WSProductSlider>) bean.getSliders();


        JSONArray jsonAlsoBrought = new JSONArray();
        JSONArray jsonAlsoViewed = new JSONArray();
        JSONArray jsonOtherProducts = new JSONArray();
        JSONArray reviews = new JSONArray();
        JSONArray jsonFutureOptions = new JSONArray();


        for (WSProductSlider slider : sliders) {
            if (slider.getTitle().equals("AlsoBought")) {
                //AlsoBought
                JSONArray jsonInnerAlsoBrought = new JSONArray();
                jsonInnerAlsoBrought.add(slider.getTargetProduct().getId());
                jsonInnerAlsoBrought.add(slider.getTargetProduct().getPrice());
                jsonInnerAlsoBrought.add(slider.getTargetProduct().getTitle());
                jsonInnerAlsoBrought.add(slider.getTargetProduct().getUrl());
                //jsonInnerAlsoBrought.add(slider.getTargetProduct().getImages());
                jsonAlsoBrought.add(jsonInnerAlsoBrought);
            }
            if (slider.getTitle().equals("AlsoViewed")) {
                //AlsoViewed
                JSONArray jsonInnerAlsoViewed = new JSONArray();
                jsonInnerAlsoViewed.add(slider.getTargetProduct().getId());
                jsonInnerAlsoViewed.add(slider.getTargetProduct().getPrice());
                jsonInnerAlsoViewed.add(slider.getTargetProduct().getTitle());
                jsonInnerAlsoViewed.add(slider.getTargetProduct().getUrl());
                //jsonInnerAlsoViewed.add(slider.getTargetProduct().getImages());
                jsonAlsoViewed.add(jsonInnerAlsoViewed);
            }
            if (slider.getTitle().equals("OtherProducts")) {
                //OtherProducts/OtherRelated
                JSONArray jsonInnerOtherProducts = new JSONArray();
                jsonInnerOtherProducts.add(slider.getTargetProduct().getId());
                jsonInnerOtherProducts.add(slider.getTargetProduct().getPrice());
                jsonInnerOtherProducts.add(slider.getTargetProduct().getTitle());
                jsonInnerOtherProducts.add(slider.getTargetProduct().getUrl());
                //jsonInnerOtherProducts.add(slider.getTargetProduct().getImages());
                jsonOtherProducts.add(jsonInnerOtherProducts);
            }

        }


        //Reviews
        List<WSProductReview> reviewList = (List<WSProductReview>) bean.getProductReviews();

        for (WSProductReview wsProductReview : reviewList) {
            JSONArray innerReviews = new JSONArray();
            innerReviews.add(wsProductReview.getName());
            innerReviews.add(wsProductReview.getTitle());
            innerReviews.add(wsProductReview.getStars());
            innerReviews.add(wsProductReview.getLocation());
            innerReviews.add(wsProductReview.getReview());
            reviews.add(innerReviews);
        }


        //Future Options
        //All new props in the future needs to go into the Future options
        //Product Start Time
        Date startTime = null;
        if (bean.hasProperty("startTime")) {
            startTime = new Date(Long.parseLong(bean.getProperty("startTime")));
            JSONArray innerStartTime = new JSONArray();
            innerStartTime.add("startTime", jsonConfig);
            innerStartTime.add(StringUtils.toString(startTime), jsonConfig);
            jsonFutureOptions.add(innerStartTime, jsonConfig);
        }

        //Product End Time
        Date endTime = null;
        if (bean.hasProperty("endTime")) {
            endTime = new Date(Long.parseLong(bean.getProperty("endTime")));
            JSONArray innerEndTime = new JSONArray();
            innerEndTime.add("endTime", jsonConfig);
            innerEndTime.add(StringUtils.toString(endTime), jsonConfig);
            jsonFutureOptions.add(innerEndTime, jsonConfig);
        }

        jsonArray.add(jsonAlsoBrought);
        jsonArray.add(jsonAlsoViewed);
        jsonArray.add(jsonOtherProducts);
        jsonArray.add(reviews);
        jsonArray.add(jsonFutureOptions);


        /* if (bean.getShowCategory() == null || bean.getShowCategory()) {
            JSONObject result = new JSONObject();
            Collection<JSONObject> jsonCategories = new LinkedHashSet<JSONObject>();
            Map<String, WSTaxonomyCategory> categoryMap =
                    Collections.synchronizedMap(new LinkedHashMap<String, WSTaxonomyCategory>());


            JSONObject jsonCategory = new JSONObject();
            jsonCategory.accumulate("Products", super.transform(jsonArray));
            jsonCategories.add(jsonCategory);

            result.accumulate("data", jsonCategories);
            return result.toString();
        } else {
            return super.transform(jsonArray);
        }*/
        return jsonArray;
    }

    /**
     * We are over-riding this method to ensure that we have handling the WSProduct transformation on
     * generic level without adding any custom string message in the JSON Array.
     * This was done to handle the JIRA ticket - https://jira.marketvine.com/browse/EXTERNALINTERACTIVEADS-276
     *
     * @param collection
     * @return
     */
    @Override
    protected Collection<Object> transformCollection(final Collection<? extends WSBean> collection) {
        Collection<Object> result = new LinkedList<Object>();
        if (collection != null && !collection.isEmpty() && collection.iterator().next() instanceof WSProduct) {
            for (WSBean item : collection) {
                result.add(transformCustomProduct((WSProduct) item));
            }
        } else {
            return super.transformCollection(collection);
        }
        return result;
    }

    public void setRetailerSiteRepository(RetailerSiteRepository retailerSiteRepository) {
        this.retailerSiteRepository = retailerSiteRepository;
    }
}
