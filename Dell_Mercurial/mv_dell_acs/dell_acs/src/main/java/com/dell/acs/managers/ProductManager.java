/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.CampaignItemNotFoundException;
import com.dell.acs.ProductDisabledException;
import com.dell.acs.ProductNotFoundException;
import com.dell.acs.ProductReviewNotFoundException;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductReview;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.model.ProductSearchModel;
import com.sourcen.core.managers.Manager;
import com.sourcen.core.util.beans.ServiceFilterBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: Chethan Date: 3/27/12 Time: 8:22 PM
 */
public interface ProductManager extends Manager {

    public Collection<Product> getActiveSearchedProducts(String searchTerm, String merchantName, int start, int maxProducts,
                                                   String filter, String orderBy);

    public Product getProduct(Long id) throws ProductDisabledException, ProductNotFoundException;

    public void update(Product product);

    public List<ProductReview> getTopProductReview(Product product, int numOfReviews);

	List<Map<String, Object>> getAllCampaignItemProductReviews(Long campaignItemID, Long productID)	throws CampaignItemNotFoundException;

    List<Map<String, Object>> getAllCampaignItemProductReviews(Long campaignItemID, Long productID, float minStars)	throws CampaignItemNotFoundException;

    public ProductReview getProductReview(Long productReviewID) throws ProductReviewNotFoundException;

    public Collection<Product> getProductForRetailerSite(RetailerSite retailerSite, int start, int maxProducts);

    public Collection<Product> getProductForRetailerSite(Long retailerSiteID, int start, int maxProducts);

    public Collection<Product> getAllProductForRetailerSite(Long retailerSiteID, int start, int maxProducts);

    public Integer getTotalProductCountForRetailerSite(RetailerSite retailerSite);

    Product saveOrUpdate(Product product);

    Collection<ProductReview> getProductReviews(Long productID, int pageNumber, int pageSize) throws Exception ;

    public List<Map<String, String>> getFilteredProducts(String searchTerm, Collection<Long> categories, Long siteID);

    public Collection<ProductSearchModel> getSearchedProductSiteNames(String searchTerm, String merchantName,
                                                                      Integer start, Integer maxProducts,
                                                                      String filter);

    /**
     * compute the wights of missing products in the system.
     *
     * @see https://jira.marketvine.com/browse/CS-248
     */
    void computeProductWeights();


    //All v2 REST related interface
    public Collection<ProductReview> getProductReviews(Long productID, ServiceFilterBean filterBean) throws ProductNotFoundException, ProductDisabledException;

    public Collection<Product> getProducts(ServiceFilterBean filterBean, String merchantName);

    /**
     * getProducts: Provides paginated collection of products based on column name and value.
     *
     * @param columnName
     * @param columnValue
     * @param start
     * @param maxProducts
     * @return
     */
    public Collection<Product> getProducts(String columnName, Object columnValue, Integer start, Integer maxProducts);

    public Product getEntireProduct(Long id) throws Exception;

    /**
     * Get paginated reviews for a requested product
     * @param product - Product for which reviews need to be fetched
     * @param filterBean - The service filter bean which can be used for pagination, filter, etc
     * @return Collection of {@link ProductReview}
     */
    public Collection<ProductReview> getTopProductReview(Product product,  ServiceFilterBean filterBean);
}