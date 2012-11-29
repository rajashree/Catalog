/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.rest;

import com.dell.acs.CampaignForInactiveRetailerException;
import com.dell.acs.managers.ProductManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductReview;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.web.ws.v1.MerchantService;
import com.dell.acs.web.ws.v1.beans.WSBeanUtil;
import com.dell.acs.web.ws.v1.beans.WSMerchantProduct;
import com.dell.acs.web.ws.v1.beans.WSProduct;
import com.dell.acs.web.ws.v1.beans.WSProductReview;
import com.dell.acs.web.ws.v1.beans.WSRetailerSite;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Chethan
 * Date: 4/23/12
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
@WebService
@RequestMapping("/api/v1/rest/MerchantService")
public class MerchantServiceImpl extends WebServiceImpl implements MerchantService {

    @Override
    @RequestMapping("getActiveMerchants")
    public Collection<WSRetailerSite> getActiveMerchants() {
       return WSBeanUtil.convert(retailerManager.getAllActiveRetailerSites(), WSRetailerSite.class);
    }

    @Override
    @RequestMapping("getPagedProductsByMerchant")
    public Collection<WSProduct> getPagedProductsByMerchant(
            @RequestParam(required = true) String merchant,
            @RequestParam(required = true) Integer pageSize,
            @RequestParam(required = true) Integer pageNumber) {
        RetailerSite retailerSite = null;
        try {
            retailerSite = retailerManager.getRetailerSitebyName(merchant);
            
            if (retailerSite.getActive() == false) 
            	throw new RuntimeException("Campaign's retailer/retailer site is inactive");   
        } catch (RuntimeException ex) {
            logger.info(ex.getMessage());
            Collection d = new ArrayList<String>();
            d.add(ex.getMessage());
            return d;
        }

        // https://jira.marketvine.com/browse/CS-385
        if(pageSize == null || pageSize < 1 || pageSize > 100){
            pageSize = 100;
        }
        int start = 0;
        if(pageNumber !=null && pageNumber > 1){
            start = (pageNumber - 1) * pageSize;
        }

        //Setting default maxProducts to 10;
        if (pageNumber > 1) {
            start = pageSize * (pageNumber - 1) + 1;
        } else {
            start = pageNumber;
        }
        Collection<Product> products = productManager.getProductForRetailerSite(retailerSite, start, pageSize);
        if (products.size() > 0) {
            return WSBeanUtil.convert(products, WSMerchantProduct.class);
        }

        return Collections.emptyList();

    }

    @Override
    @RequestMapping("getAllPagedProductsByMerchant")
    public Collection<WSProduct>getAllPagedProductsByMerchant(
            @RequestParam(required = true) String merchant,
            @RequestParam(required = true) Integer pageSize,
            @RequestParam(required = true) Integer pageNumber) {
        RetailerSite retailerSite = null;
        try {
            retailerSite = retailerManager.getRetailerSitebyName(merchant);

            if (retailerSite.getActive() == false)
                throw new RuntimeException("Campaign's retailer/retailer site is inactive");
        } catch (RuntimeException ex) {
            logger.info(ex.getMessage());
            Collection d = new ArrayList<String>();
            d.add(ex.getMessage());
            return d;
        }

        // https://jira.marketvine.com/browse/CS-385
        if(pageSize == null || pageSize < 1 || pageSize > 100){
            pageSize = 100;
        }
        int start = 0;
        if(pageNumber !=null && pageNumber > 1){
            start = (pageNumber - 1) * pageSize;
        }

        //Setting default maxProducts to 10;
        if (pageNumber > 1) {
            start = pageSize * (pageNumber - 1) + 1;
        } else {
            start = pageNumber;
        }
        Collection<Product> products = productManager.getAllProductForRetailerSite(retailerSite.getId(), start, pageSize);
        if (products.size() > 0) {
            return WSBeanUtil.convert(products, WSMerchantProduct.class);
        }

        return Collections.emptyList();

    }

    @Override
    @RequestMapping("getPagedProductReviews")
    /* EXTERNALINTERACTIVEADS-352 :: product id, page number and page size */
    public Collection<WSProductReview> getPagedProductReviews(@RequestParam(required = true) Long productId,
                                                              @RequestParam(required = true) Integer pageSize,
                                                              @RequestParam(required = true) Integer pageNumber) throws Exception {
        Collection<ProductReview> productReviews = productManager.getProductReviews(productId, pageNumber.intValue(), pageSize.intValue());
        return WSBeanUtil.convert(productReviews, WSProductReview.class);
    }


    @Override
    @RequestMapping("getProductDetail")
    public WSProduct getProductDetail(@RequestParam(required = true) Long productId) throws Exception {
        return WSBeanUtil.convert(productManager.getProduct(productId), new WSProduct());
    }

    @Override
    @RequestMapping("getProductDetails")
    public Collection<WSProduct> getProductDetails(@RequestParam(required = true) String productId,
                                                   @RequestParam(required = false, defaultValue = "") String scope) throws Exception {
        String[] ids = StringUtils.split(productId, "-");
        logger.debug("Get Product details for IDs :::  " + productId);
        Collection<Product> products = new ArrayList<Product>();
        if (ids.length > 0) {
            for (String id : ids) {
            	Product product = this.productManager.getProduct(Long.valueOf(id));
            	if (product != null)
            		products.add(product);
            }//for each ID
        }

        if (products.size() > 0) {
            MappingContext context = null;
            if (scope != null && !scope.isEmpty()) {
                context = new MappingContext(scope.trim());
            } else {
                context = new MappingContext();
            }
            return WSBeanUtil.convert(products, WSProduct.class, context);
        }
        return Collections.emptyList();
    }

    //IOC
    @Autowired
    RetailerManager retailerManager;

    public void setRetailerManager(RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }

    @Autowired
    ProductManager productManager;

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }


}
