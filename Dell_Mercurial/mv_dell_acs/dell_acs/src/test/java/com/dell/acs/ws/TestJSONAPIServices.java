/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.ws;

import com.dell.acs.CampaignForInactiveRetailerException;
import com.dell.acs.CampaignItemNotFoundException;
import com.dell.acs.CampaignNotFoundException;
import com.dell.acs.ProductReviewNotFoundException;
import com.dell.acs.web.ws.v1.CampaignService;
import com.dell.acs.web.ws.v1.MerchantService;
import com.dell.acs.web.ws.v1.ProductService;
import com.dell.acs.web.ws.v1.beans.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Samee K.S
 * @author sameeks: svnName $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public class TestJSONAPIServices {

    private static final Logger logger = LoggerFactory.getLogger(TestJSONAPIServices.class);

    private static MerchantService merchantService;

    private static CampaignService campaignService;

    private static ProductService productService;


    protected static ApplicationContext applicationContext;

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});

        if (merchantService == null) {
            merchantService = (MerchantService) applicationContext.getBean("merchantServiceImpl");
        }
        if (campaignService == null) {
            campaignService = (CampaignService) applicationContext.getBean("campaignServiceImpl");
        }
        if (productService == null) {
            productService = (ProductService) applicationContext.getBean("productServiceImpl");
        }
    }


    /* MerchantService - related tests : START */

    @Test
    public void testGetActiveMerchantsService() {
        // Service url - <base_path>/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=sheplers1&pageSize=100&pageNumber=1
        Collection<WSRetailerSite> retailerSites = merchantService.getActiveMerchants();
        logger.info("Retailer sites - " + retailerSites.size());
    }

    @Test
    public void testGetPagedProductsByMerchantService() {
        // Service url - <base_path>/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=sheplers1&pageSize=100&pageNumber=1
        String merchant = "dell";
        Integer pageSize = 10;
        Integer pageNumber = 0;
        Collection<WSProduct> products = merchantService.getPagedProductsByMerchant(merchant, pageSize, pageNumber);
        logger.info("Products for " + products.size());
        Iterator itr = products.iterator();
        while (itr.hasNext()) {
            WSMerchantProduct product = (WSMerchantProduct) itr.next();
            logger.info("Product with id:=" + product.getId() + " with Title:=" + product.getTitle());
        }
    }

    @Test
    public void testMerchantGetPagedProductReviewsService() {
        Long productId = 1L;
        int pageSize = 10;
        int pageNumber = 1;
        try {
            Collection<WSProductReview> productReviews = merchantService.getPagedProductReviews(productId, pageSize, pageNumber);
            logger.info("productReviews - " + productReviews.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMerchantGetProductDetailService() throws Exception {
        // Service url - <base_path>/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=sheplers1&pageSize=100&pageNumber=1
        WSProduct product = merchantService.getProductDetail(3L);
        logger.info("Product with id:=" + product.getId() + " with Title:=" + product.getTitle());
    }

    @Test
    public void testgetProductDetailsService() throws Exception {
        String productids = "1-2-4";
        String scope = "minimal";
        Collection<WSProduct> products = merchantService.getProductDetails(productids, scope);
        logger.info("Number Products shows:=" + products.size());

        Iterator itr = products.iterator();
        while (itr.hasNext()) {
            WSProduct product = (WSProduct) itr.next();
            logger.info("Product with id:=" + product.getId() + " with Title:=" + product.getTitle());
        }
    }

    /* MerchantService - related tests : END */

    /* ProductService - related tests : START */

    /*@Test
    public void testGetProductRecommendationsService(){
       productService.getProductRecommendations()
    }*/
    @Test
    public void testGetProductsService() {
        String searchTerm = "laser";
        String merchantName = "dell";
        int start = 0;
        int maxProducts = 5;
        String filter = "title";
        String orderBy = "title";

        Collection<WSProduct> products = productService.getProducts(searchTerm, merchantName, start, maxProducts, filter, orderBy);
        Iterator itr = products.iterator();
        while (itr.hasNext()) {
            WSProduct product = (WSProduct) itr.next();
            logger.info("Product with id:=" + product.getId() + " with Title:=" + product.getTitle());
        }

    }

    @Test
    public void testGetProductService() throws Exception {
        Long productId = 3L;
        WSProduct product = productService.getProduct(productId);
        logger.info("Product with id:=" + product.getId() + " with Title:=" + product.getTitle());


    }

    @Test
    public void testUpdateProductSalesService() throws Exception {
        Long productId = 3L;
        Integer count = productService.updateProductSales(productId);
        logger.info("Sales Count:=" + count);
    }

    @Test
    public void testGetProductShoppedCountService() throws Exception {
        Long productId = 3L;
        Integer count = productService.getProductShoppedCount(productId);
        logger.info("Sales Count:=" + count);
    }

    @Test
    public void testProductGetPagedProductReviewsService() throws Exception {
        Long productId = 3L;
        Integer pageSize = 5;
        Integer pageNumber = 1;
        Collection<WSProductReview> productReviews = productService.getPagedProductReviews(productId, pageSize, pageNumber);
        logger.info("Number Review of Product:="+productId+" is:="+productReviews.size());
    }

    @Test
    public void testProductGetProductDetailService() throws Exception {
        Long productId = 3L;
        WSProduct product = productService.getProductDetail(productId);
        logger.info("Product with id:=" + product.getId() + " with Title:=" + product.getTitle());
    }

    @Test
    public void testGetProductDetailsService() throws Exception {
        String productIds = "1-2-3";
        Collection<WSProduct> products = productService.getProductDetails(productIds);
        Iterator itr = products.iterator();
        while (itr.hasNext()) {
            WSProduct product = (WSProduct) itr.next();
            logger.info("Product with id:=" + product.getId() + " with Title:=" + product.getTitle());
        }

    }

    @Test
    public void testSearchProductsService() {
        String searchTerm = "laser";
        String merchantName = "dell";
        int start = 0;
        int maxProducts = 5;
        String filter = "title";
        String orderBy = "title";

        WSProductSearchResult results = productService.searchProducts(searchTerm, merchantName, start, maxProducts, filter, orderBy);
        Collection<WSProduct> products = results.getProducts();

        Iterator itr = products.iterator();
        while (itr.hasNext()) {
            WSProduct product = (WSProduct) itr.next();
            logger.info("Product with id:=" + product.getId() + " with Title:=" + product.getTitle());
        }
    }


    @Test
    public void testGetProductDetailService() throws Exception {
        // Service url - <base_path>/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=sheplers1&pageSize=100&pageNumber=1
        WSProduct product = productService.getProductDetail(1L);
        logger.info("Product with id:=" + product.getId() + " with Title:=" + product.getTitle());
    }

    @Test
    public void testGetAllProductService() throws Exception {
        WSProduct product = productService.getProduct(1L);
        logger.info("Product with id:="+product.getId()+",status before is:="+ product.getEnabled());
        WSProduct modifiedProduct = productService.updateProductStatus(1L,true);
        logger.info("Product with id:="+modifiedProduct.getId()+",status after is:="+ modifiedProduct.getEnabled());
    }

    /* ProductService - related tests : END */

    /* Campaign related services */

    @Test
    public void testGetCampaignsByNameOrTypeService() {
        // Service url - <base_path>/api/v1/rest/CampaignService/getCampaignById.json?campaignId=1&trackerId=123&outputFormat=Dell
        Long campaignId = 1L;
        String trackerID = "123";
        String trackerId = "123";
        String outputFormat = "dell";
        String linkTrackerCampaignID = "122";

        try {
            WSCampaign campaign = campaignService.getCampaign(campaignId, trackerID, trackerId, outputFormat, linkTrackerCampaignID);
            logger.info("Campaign - " + campaign.toString());
        } catch (CampaignNotFoundException e) {
            e.printStackTrace();
        } catch (CampaignItemNotFoundException e) {
            e.printStackTrace();
        } catch (ProductReviewNotFoundException e) {
            e.printStackTrace();
        } catch (CampaignForInactiveRetailerException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetCampaignsService() {
        // Collection<WSCampaign> getCampaigns(String retailerSiteName, String campaignType) throws Exception;
        String retailerSiteName = "";
        String campaignType = "";
        try {
            Collection<WSCampaign> campaign = campaignService.getCampaigns(retailerSiteName, campaignType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCampaignsBySiteIDService() {
        //Collection<WSCampaign> getCampaigns(Long retailerSiteId) throws Exception;
        Long retailerSiteId = 1L;
        try {
            Collection<WSCampaign> campaigns = campaignService.getCampaigns(retailerSiteId);
            logger.info("Campaigns - " + campaigns.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCampaignItemReviewsService() throws Exception {
        Long campaignItemId = 1L;
        Collection<WSProductReview> productReviews = campaignService.getCampaignItemReviews(campaignItemId);
        logger.info("Campaign Item:="+campaignItemId+" has "+productReviews.size()+" Reviews");
    }


}
