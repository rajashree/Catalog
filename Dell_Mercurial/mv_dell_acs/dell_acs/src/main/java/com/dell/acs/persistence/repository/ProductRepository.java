/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.managers.ContentFilterBean;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductReview;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.model.ProductSearchModel;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sameeks $
 * @version $Revision: 3630 $, $Date:: 2012-06-26 12:03:47#$
 */
public interface ProductRepository extends IdentifiableEntityRepository<Long, Product> {

    /**
     * Get Product, if active.
     *
     * @param siteName
     * @param productId
     *
     * @return Product
     */
    Product getActive(Long id);
    
    /**
     * Get Product.
     *
     * @param siteName
     * @param productId
     *
     * @return Product
     */
    Product getBySiteNameAndProductId(String siteName, String productId);

    /**
     * Get Collection of Products.
     *
     * @param contentFilterBean
     *
     * @return Collection of Product
     */
    Collection<Product> getActiveProducts(ContentFilterBean contentFilterBean);

    /**
     * Get Collection of Products by CSV Product Ids.
     *
     * @param productIds
     *
     * @return Collection of Product
     */
    Collection<Product> getProducts(String productIds);

    /**
     * Save or Update Product.
     *
     * @param product
     *
     * @return Product
     */
    Product saveOrUpdate(Product product);

    /**
     * Method to return the reviews for a product
     *
     * @param productID
     *
     * @return lis of Map objects containing reviews for an item
     */
    List<Map<String, Object>> getAllCampaignItemProductReviews(Long productID, List<Long> selectedReviews);

    /**
     * Method to return the reviews for a product
     *
     * @param productID
     *
     * @return lis of Map objects containing reviews for an item
     */
    List<Map<String, Object>> getAllCampaignItemProductReviews(Long productID, List<Long> selectedReviews, float minRating);

    /**
     * Method to return a list of Categories for a parent
     *
     * @param name       - Category name
     * @param parentID   - Parent category depth / id
     * @param campaignID - Campaign id
     *
     * @return - list of Maps which contains the Category name and ID
     */
    Map<String, String> getCategories(Long parentID, String name, Long campaignID);

    List<Map<String, String>> getFilteredItems(Long categoryID);

    List<Map<String, String>> getFilteredProducts(String searchTerm, Collection<Long> categories, Long siteID);

    //Collection<Product> getSearchedProducts(String searchTerm, int start, int maxProducts);
    Collection<Product> getActiveSearchedProducts(String searchTerm, String merchantName, int start, int maxProducts,
                                            String filter, String orderBy);

    /**
     * getProductReviewRating() is for obtains the Product Review Rating
     *
     * @param product object is accept the argument
     *
     * @return the computed value on the basic of the spelling ,profanity and centimental analysis
     */
    public long getProductReviewRating(Product product);

    /**
     * getTopProductReview() will fetch the top specified review for the Product
     *
     * @param product      object is accepted for fetching the reviews
     * @param numOfReviews in int form for the number of reviews selection
     *
     * @return List of the Product Review Object
     */
    public List<ProductReview> getTopProductReview(Product product, int numOfReviews);

    List<Product> getProductsForRetailerSite(RetailerSite retailerSite);

    Collection<Product> getProductForRetailerSite(RetailerSite retailerSite, int start, int maxProducts);

    Collection<Product> getProductForRetailerSite(Long retailerSiteID, int start, int maxProducts);

    Collection<Product> getAllProductForRetailerSite(Long retailerSiteID, int start, int maxProducts);

    public void updateProperties(Product product);

    Integer getTotalProductCountForRetailerSite(RetailerSite retailerSite);

    public Collection<ProductSearchModel> getSearchedProductSiteNames(String searchTerm, String merchantName,
                                                                      Integer start, Integer maxProducts,
                                                                      String filter);

    /**
     * compute the product-weight of a record if the weight is null.
     *
     * @param limitOfBatch
     */
    void computeMissingProductWeights(int limitOfBatch);

    public Long getMissingProductWeightsCount();

    public Collection<Product> getProducts(ServiceFilterBean filterBean, String merchantName);

    /**
     * getProducts: Provides paginated collection of products based on column name and value.
          * @param columnName
          * @param columnValue
          * @param start
          * @param maxProducts
          * @return
          */
    public Collection<Product> getProducts(String columnName, Object columnValue, Integer start, Integer maxProducts);
    /**
     * Fetch ProductReviews for a specified Product
     * @param product - Product for which reviews need to be fetched
     * @param filterBean - The service filter bean which can be used for pagination, filter, etc
     * @return Collection of {@link ProductReview}
     */
    public Collection<ProductReview> getTopProductReview(final Product product,final ServiceFilterBean filterBean);
}