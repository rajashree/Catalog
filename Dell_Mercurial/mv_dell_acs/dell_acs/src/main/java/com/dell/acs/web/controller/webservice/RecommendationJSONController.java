/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.webservice;

import com.dell.acs.managers.AdPublisherManager;
import com.dell.acs.managers.ContentFilterBean;
import com.dell.acs.managers.RecommendationManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductImage;
import com.dell.acs.persistence.domain.ProductSlider;
import com.dell.acs.web.controller.BaseDellController;
import com.sourcen.core.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import com.dell.acs.web.ws.version2.ContentServerService;

/**
 * Created by IntelliJ IDEA.
 * User: Chethan
 * Date: 2/28/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Deprecated
public class RecommendationJSONController extends BaseDellController {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationJSONController.class);
    /**
     * RetailerManager Bean Injection.
     */
    @Autowired
    private RetailerManager retailerManager;

    /**
     * RecommendationManager Bean Injection.
     */
    @Autowired
    private RecommendationManager recommendationManager;

    /**
     * AdPublisherManager Bean Injection.
     */
    @Autowired
    AdPublisherManager adPublisherManager;


    /**
     * /**
     * Controller to get Recommendation as JSON.
     *
     * @param zipcode
     * @param maxProducts
     * @param categoryDepth
     * @param maxCategories
     * @param maxRelated
     * @param advertiserID
     * @param adCategoryID
     * @param referralSite
     * @param searchTerms
     * @param resolution
     * @param userCookieId
     * @param productIDs
     * @param productCategories
     * @param gender
     * @param birthday
     * @param contentType
     * @param recommend
     * @param ipAddress
     * @param browser
     * @param operatingSystem
     * @param devMode
     * @param model
     * @return Recommendation JSON Output
     */
    @RequestMapping(value = "/recommendJSON.do", method = RequestMethod.GET)
    public ResponseEntity<String> getProductRecommendations(
            @RequestParam(value = "Zipcode", required = false) String zipcode,
            @RequestParam(value = "Max_Products", required = false) String maxProducts,
            @RequestParam(value = "Category_Depth", required = false) String categoryDepth,
            @RequestParam(value = "Max_Categories", required = false) String maxCategories,
            @RequestParam(value = "Max_Related", required = false) String maxRelated,
            @RequestParam(value = "Advertiser_ID", required = false) String advertiserID,
            @RequestParam(value = "Ad_Category_ID", required = false) String adCategoryID,
            @RequestParam(value = "Referral_Site", required = false) String referralSite,
            @RequestParam(value = "SearchTerms", required = false) String searchTerms,
            @RequestParam(value = "Resolution", required = false) String resolution,
            @RequestParam(value = "User_Cookie_Id", required = false) String userCookieId,
            @RequestParam(value = "Product_IDs", required = false) String productIDs,
            @RequestParam(value = "Product_Categories", required = false) String productCategories,
            @RequestParam(value = "Gender", required = false) String gender,
            @RequestParam(value = "Birthday", required = false) Date birthday,
            @RequestParam(value = "ContentType", required = false) String contentType,
            @RequestParam(value = "Recommend", required = false) String recommend,
            @RequestParam(value = "IP_Address", required = false) String ipAddress,
            @RequestParam(value = "Browser", required = false) String browser,
            @RequestParam(value = "Operating_System", required = false) String operatingSystem,
            @RequestParam(value = "devMode", required = false) String devMode,
            ModelMap model) {


        HttpHeaders responseHeaders = new HttpHeaders();
        logger.info("***********In recommendded JSON controller**********");

        /* if (devMode == null || devMode.equalsIgnoreCase("false")) {
            throw new AuthorizationServiceException("You are not Authrorized to Access");
        }*/
        String result = null;
        String error = null;

        try {
            ContentFilterBean contentFilterBean = new ContentFilterBean();
            contentFilterBean.setZipcode(zipcode);
            contentFilterBean.setMaxProducts(maxProducts != null ? Integer.parseInt(maxProducts) : 100);
            contentFilterBean.setCategoryDepth(categoryDepth != null ? Integer.parseInt(categoryDepth) : 6);
            contentFilterBean.setMaxCategories(maxCategories);
            contentFilterBean.setMaxRelated(maxRelated != null ? Integer.parseInt(maxRelated) : 10);
            contentFilterBean.setAdvertiserID(advertiserID != null ? Integer.parseInt(advertiserID) : 0);
            contentFilterBean.setAdCategoryID(adCategoryID);
            contentFilterBean.setReferralSite(referralSite);
            contentFilterBean.setSearchTerms(searchTerms);
            contentFilterBean.setResolution(resolution);
            contentFilterBean.setUserCookieID(userCookieId != null ? Integer.parseInt(userCookieId) : 0);
            contentFilterBean.setProductIDs(productIDs);
            contentFilterBean.setProductCategories(productCategories);
            contentFilterBean.setGender(gender);
            contentFilterBean.setBirthday(birthday);
            contentFilterBean.setContentType(contentType != null ? Integer.parseInt(contentType) : 0);
            contentFilterBean.setRecommend(recommend != null ? Integer.parseInt(recommend) : 1);
            contentFilterBean.setIpAddress(ipAddress);
            contentFilterBean.setBrowser(browser);
            contentFilterBean.setOperatingSystem(operatingSystem);

            Collection<Product> products = recommendationManager.getActiveRecommendedProducts(contentFilterBean);

            Map<String, String[]> srcKeyHashes = new HashMap<String, String[]>();
            Map<String[], Collection<Product>> sortedProducts = new LinkedHashMap<String[], Collection<Product>>();

            for (Product product : products) {
                addProductToCategory(srcKeyHashes, sortedProducts, product, contentFilterBean.getCategoryDepth());
            }

            //Generating Related Products
            //LinkedList<Product> relatedProducts = new LinkedList<Product>();
            Set<Product> relatedProducts = new HashSet<Product>();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonRelatedCategory = new JSONObject();
            JSONArray relatedJsonArray = new JSONArray();
            for (Map.Entry<String[], Collection<Product>> entry : sortedProducts.entrySet()) {

                JSONObject jsonCategory = new JSONObject();
                jsonCategory.accumulate("Category", entry.getKey());
                JSONArray jsonProducts = new JSONArray();
                JSONArray jsonRelatedProducts = new JSONArray();
                for (Product product : entry.getValue()) {
//                    List<ProductImage> images = (List<ProductImage>) product.getImages();
//                    if (images != null) {
//                        images.size();
//                    }
                    List<ProductSlider> sliders = (List<ProductSlider>) product.getSliders();
                    if (sliders != null && sliders.size() > 0) {
                        for (ProductSlider slider : sliders) {
                            relatedProducts.add(slider.getTargetProduct());


                        }
                    }
                    /*if (product.getRetailerSites().getSiteName().equals("dell")) {
                        //Dell processor
                        product = dellResultProcessor.convertResult(product);

                    }
                    if (product.getRetailerSites().getSiteName().equals("sheplers")) {
                        //Sheplers processor
                        product = sheplersResultProcessor.convertResult(product);
                    }
                    if (product.getRetailerSites().getSiteName().equals("target")) {
                        //Target processor
                        product = targetResultProcessor.convertResult(product);
                    }
                    if (StringUtils.isEmpty(product.getRetailerSites().getSiteName())
                            || StringUtils.isBlank(product.getRetailerSites().getSiteName())) {
                        //Generic processor
                        product = genericResultProcessor.convertResult(product);
                    }*/

                    jsonProducts.add(productToJson(product));
                }
                jsonCategory.accumulate("Products", jsonProducts);
                jsonArray.add(jsonCategory);

                //Related Products

                for (Product relatedProduct : relatedProducts) {
                    jsonRelatedProducts.add(recommendationProductToJson(relatedProduct));
                }
                jsonRelatedCategory.accumulate("Related Products", jsonRelatedProducts);
                relatedJsonArray.add(jsonRelatedCategory);
                //result = jsonArray.toString();
                //counter++;
            }
            //jsonArray.add(jsonRelatedCategory);
            //result = jsonRelatedCategory.toString();
            jsonArray.add(jsonRelatedCategory);
            result = jsonArray.toString();

            //result = result.concat(relatedJsonArray.toString());

        } catch (Exception e) {
            logger.error("Error while getting Recommendation:::" + e.getMessage(), e);
            error = e.toString();
        }
        //Insert Post Params in AdPublisherRequest
        //TODO: Store the params as Json, store only ids for result, store error, etc..
        //String postParams = null;
        //adPublisherManager.updateRequestParameters(1L, "Post", error, postParams , result, 0);

        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
    }

    /**
     * Adding Product to Category Constructed
     * Note: CategoryDepth default value is 6 since there are 6 categories present
     *
     * @param srcKeyHashes
     * @param sortedProducts
     * @param product
     */
    private void addProductToCategory(Map<String, String[]> srcKeyHashes, Map<String[],
            Collection<Product>> sortedProducts, Product product, int categoryDepth) {

//        String hashKey = product.getCategory1();
//        if ((!StringUtils.isEmpty(product.getCategory2())) && categoryDepth >= 2) {
//            hashKey = hashKey + "----" + product.getCategory2();
//        }
//        if ((!StringUtils.isEmpty(product.getCategory3())) && categoryDepth >= 3) {
//            hashKey = hashKey + "----" + product.getCategory3();
//        }
//        if ((!StringUtils.isEmpty(product.getCategory4())) && categoryDepth >= 4) {
//            hashKey = hashKey + "----" + product.getCategory4();
//        }
//        if ((!StringUtils.isEmpty(product.getCategory5())) && categoryDepth >= 5) {
//            hashKey = hashKey + "----" + product.getCategory5();
//        }
//        if ((!StringUtils.isEmpty(product.getCategory6())) && categoryDepth >= 6) {
//            hashKey = hashKey + "----" + product.getCategory6();
//        }
//
//        Collection<Product> products;
//        String[] key = srcKeyHashes.get(hashKey);
//
//
//        if (key == null) {
//            key = hashKey.split("----");
//            products = new LinkedHashSet<Product>();
//            srcKeyHashes.put(hashKey, key);
//            sortedProducts.put(key, products);
//        } else {
//            products = sortedProducts.get(key);
//        }
       // products.add(product);

    }

    /**
     * Convert Objec to JSON Array.
     *
     * @param product
     * @return JSONArray
     */

    public JSONArray productToJson(Product product) {
        JSONArray array = new JSONArray();
        /* JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object source, String name, Object value) {
                if ("product".equals(name)) {
                    return true;
                }
                return false;
            }
        });*/
        try {
            array.add(product.getId());
            array.add(product.getUrl());
            array.add(product.getSiteName());
            array.add(product.getProductId());
            array.add(product.getSku());
            array.add(product.getWebPartNumber());
            array.add(product.getTitle());
            array.add(product.getDescription());
            array.add(product.getNewProduct());
            array.add(product.getBestSeller());
            array.add(product.getPrice());
            array.add(product.getHasPriceRange());
            array.add(product.getListPrice());
            array.add(product.getHasListPriceRange());
            array.add("USD");
            array.add(product.getClearanceTag());
            array.add(product.getSaleTag());
            array.add(product.getPriceCutTag());
            array.add(product.getTempPriceCutTag());
            array.add(product.getHasVariations());
            array.add(product.getSpecifications());
            array.add(product.getEstimatedShipDate());
            array.add(product.getPromotional());
            array.add(product.getShippingPromotion());
            array.add(product.getBuyLink());
            //array.add(product.getSaleTag());
            array.add(product.getFlashLink());
            //Ratings
            array.add(product.getStars());
            array.add(product.getReviews());
            array.add(product.getReviewsLink());
            array.add(product.getFacebookLikes());
            array.add(product.getPlusOneGoogle());
            array.add(product.getTweets());
            array.add(StringUtils.toString(product.getUpdateDateTime()));

            //images

            List<ProductImage> images = (List<ProductImage>) product.getImages();
            List<String> imageNames = new LinkedList<String>();

            if (images != null && images.size() > 0) {
                for (ProductImage image : images) {
                    //imageUrls.add(image.getImageURL());
                    //changed from imageurl to image name
                    imageNames.add(image.getImageName());
                }
                if (imageNames.size() > 0) {
                    array.add(imageNames);
                }
            }

            //AlsoBought, AlsoViewed, OtherRelated,f uture options

            List<ProductSlider> sliders = (List<ProductSlider>) product.getSliders();

            JSONArray jsonAlsoBrought = new JSONArray();
            JSONArray jsonAlsoViewed = new JSONArray();
            JSONArray jsonOtherProducts = new JSONArray();
            JSONArray jsonFutureOptions = new JSONArray();

            for (ProductSlider slider : sliders) {
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
                if (slider.getTitle().equals("FutureOptions")) {
                    //futureoptions
                    JSONArray jsonInnerFutureOptions = new JSONArray();
                    jsonInnerFutureOptions.add(slider.getTargetProduct().getId());
                    jsonInnerFutureOptions.add(slider.getTargetProduct().getPrice());
                    jsonInnerFutureOptions.add(slider.getTargetProduct().getTitle());
                    jsonInnerFutureOptions.add(slider.getTargetProduct().getUrl());
                    //jsonInnerFutureOptions.add(slider.getTargetProduct().getImages());
                    jsonFutureOptions.add(jsonInnerFutureOptions);
                }
            }
            array.add(jsonAlsoBrought);
            array.add(jsonAlsoViewed);
            array.add(jsonOtherProducts);
            array.add(jsonFutureOptions);

        } catch (Exception e) {
            logger.error("Error While Converting to JSON Object::", e.getMessage());
            e.printStackTrace();

        }

        return array;
    }

    /**
     * Convert RecommendationProduct to JSON Array.
     *
     * @param product
     * @return JSONArray
     */

    public JSONArray recommendationProductToJson(Product product) {
        JSONArray array = new JSONArray();
        /* JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object source, String name, Object value) {
                if ("product".equals(name)) {
                    return true;
                }
                return false;
            }
        });*/
        try {
            array.add(product.getId());
            array.add(product.getUrl());
            array.add(product.getSiteName());
            array.add(product.getProductId());
            array.add(product.getSku());
            array.add(product.getWebPartNumber());
            array.add(product.getTitle());
            array.add(product.getDescription());
            array.add(product.getNewProduct());
            array.add(product.getBestSeller());
            array.add(product.getPrice());
            array.add(product.getHasPriceRange());
            array.add(product.getListPrice());
            array.add(product.getHasListPriceRange());
            array.add("USD");
            array.add(product.getClearanceTag());
            array.add(product.getSaleTag());
            array.add(product.getPriceCutTag());
            array.add(product.getTempPriceCutTag());
            array.add(product.getHasVariations());
            array.add(product.getSpecifications());
            array.add(product.getEstimatedShipDate());
            array.add(product.getPromotional());
            array.add(product.getShippingPromotion());
            array.add(product.getBuyLink());
            //array.add(product.getSaleTag());
            array.add(product.getFlashLink());
            //Ratings
            array.add(product.getStars());
            array.add(product.getReviews());
            array.add(product.getReviewsLink());
            array.add(product.getFacebookLikes());
            array.add(product.getPlusOneGoogle());
            array.add(product.getTweets());
            array.add(StringUtils.toString(product.getUpdateDateTime()));

        } catch (Exception e) {
            logger.error("Error While Converting to JSON Object::", e.getMessage());
            e.printStackTrace();

        }
        return array;
    }


}

