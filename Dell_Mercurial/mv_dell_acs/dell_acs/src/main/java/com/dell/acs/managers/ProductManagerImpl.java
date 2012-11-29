/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.CampaignItemNotFoundException;
import com.dell.acs.ProductDisabledException;
import com.dell.acs.ProductNotFoundException;
import com.dell.acs.ProductReviewNotFoundException;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductReview;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.CampaignItemRepository;
import com.dell.acs.persistence.repository.ProductImageRepository;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.persistence.repository.ProductReviewRepository;
import com.dell.acs.persistence.repository.ProductSliderRepository;
import com.dell.acs.persistence.repository.model.ProductSearchModel;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Chethan J.
 * @author $LastChangedBy: sameeks $
 * @version $Revision: 3630 $, $Date:: 2012-06-26 12:03:47#$
 */
@Service
public class ProductManagerImpl implements ProductManager {

    private Logger logger = Logger.getLogger(ProductManagerImpl.class);

    //IOC
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private ProductSliderRepository productSliderRepository;

    @Autowired
    private CampaignItemRepository campaignItemRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<Product> getActiveSearchedProducts(String searchTerm, String merchantName, int start, int maxProducts,
                                                         String filter, String orderBy) {
        Collection<Product> products = (Collection<Product>) productRepository.getActiveSearchedProducts(searchTerm,
                merchantName,
                start,
                maxProducts,
                filter,
                orderBy);
        if (!products.isEmpty()) {
            for (Product product : products) {
                product.setImages(productImageRepository.getProductImages(product));
            }
        }
        return products;

    }

    @Override
    @Transactional(readOnly = true)
    public Product getProduct(Long id) throws ProductDisabledException, ProductNotFoundException {
        Product product = productRepository.getActive(id);
        if (product != null) {
            /**
             * CS-397:All APIs should not show up disabled products
             */
            if (product.getEnabled() != null && product.getEnabled() == false) {
                throw new ProductDisabledException("Product with id:=" + product.getId() + " is disabled");
            } else {
                product.setImages(productImageRepository.getProductImages(product));
                product.setProductReviews(productReviewRepository.getProductReviews(product));
                product.setSliders(productSliderRepository.getProductSlidersBySourceProduct(product));
                return product;
            }
        } else {
            throw new ProductNotFoundException("Product with id:=" + id + " does not exist");
        }
    }

    @Override
    public Product getEntireProduct(Long id) throws Exception{
        Product product = productRepository.getActive(id);
        if (product != null) {
            product.setImages(productImageRepository.getProductImages(product));
            product.setProductReviews(productReviewRepository.getProductReviews(product));
            product.setSliders(productSliderRepository.getProductSlidersBySourceProduct(product));
        }else {
            throw new ProductNotFoundException("Product with id:=" + Long.toString(id) + " does not exist");
        }
        return product;
    }

    @Transactional(readOnly = true)
    public Collection<ProductReview> getProductReviews(Long productID, int pageNumber, int pageSize) throws Exception {
        /**
         * CS-397:All APIs should not show up disabled products
         */
        //getProduct used to check if product exist and if its disabled.
        Product product = getProduct(productID);
        return productReviewRepository.getProductReviews(product.getId(), pageNumber, pageSize);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Product saveOrUpdate(Product product) {
        return productRepository.saveOrUpdate(product);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(Product product) {
        productRepository.update(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReview> getTopProductReview(final Product product, final int numOfReviews) {
        return productRepository.getTopProductReview(product, numOfReviews);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllCampaignItemProductReviews(final Long campaignItemID,
                                                                   final Long productID) throws
            CampaignItemNotFoundException {
        List<Long> selectedReviews = new ArrayList<Long>();
        CampaignItem campaignItem = campaignItemRepository.get(campaignItemID);
        if (campaignItem != null && campaignItem.getProperties()
                .hasProperty(CampaignManager.CAMPAIGN_ITEM_REVIEWS_PROPERTY)) {
            selectedReviews = StringUtils.asLongList(campaignItem.getProperties()
                    .getProperty(CampaignManager.CAMPAIGN_ITEM_REVIEWS_PROPERTY));
        }
        return productRepository.getAllCampaignItemProductReviews(productID, selectedReviews);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllCampaignItemProductReviews(final Long campaignItemID,
                                                                      final Long productID, float minStars) throws
            CampaignItemNotFoundException {
        List<Long> selectedReviews = new ArrayList<Long>();
        CampaignItem campaignItem = campaignItemRepository.get(campaignItemID);
        if (campaignItem != null && campaignItem.getProperties()
                .hasProperty(CampaignManager.CAMPAIGN_ITEM_REVIEWS_PROPERTY)) {
            selectedReviews = StringUtils.asLongList(campaignItem.getProperties()
                    .getProperty(CampaignManager.CAMPAIGN_ITEM_REVIEWS_PROPERTY));
        }
        return productRepository.getAllCampaignItemProductReviews(productID, selectedReviews, minStars);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductReview getProductReview(Long productReviewID) throws ProductReviewNotFoundException {
        return productReviewRepository.get(productReviewID);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Product> getProductForRetailerSite(Long retailerSiteID, int start, int maxProducts) {
        Collection<Product> products = productRepository.getProductForRetailerSite(retailerSiteID, start, maxProducts);
        for (Product product : products) {
            product.setImages(productImageRepository.getProductImages(product));
        }
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Product> getAllProductForRetailerSite(Long retailerSiteID, int start, int maxProducts) {
        Collection<Product> products = productRepository.getAllProductForRetailerSite(retailerSiteID, start, maxProducts);
        for (Product product : products) {
            product.setImages(productImageRepository.getProductImages(product));
        }
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Product> getProductForRetailerSite(RetailerSite retailerSite, int start, int maxProducts) {
        Collection<Product> products = getProductForRetailerSite(retailerSite.getId(), start, maxProducts);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalProductCountForRetailerSite(RetailerSite retailerSite) {
        return productRepository.getTotalProductCountForRetailerSite(retailerSite);
    }

    @Override
    public List<Map<String, String>> getFilteredProducts(String searchTerm, Collection<Long> categories, Long siteID) {
        return productRepository.getFilteredProducts(searchTerm, categories, siteID);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ProductSearchModel> getSearchedProductSiteNames(String searchTerm, String merchantName,
                                                                      Integer start, Integer maxProducts,
                                                                      String filter) {
        return productRepository.getSearchedProductSiteNames(searchTerm, merchantName, start, maxProducts, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public void computeProductWeights() {
        if (productRepository.getMissingProductWeightsCount() > 0) {
            productRepository.computeMissingProductWeights(5000);
        }
    }

    /**
     * compute the weight of a product in the rank system.
     *
     * @see https://jira.marketvine.com/browse/CS-248
     */
    public static Float computeProductWeight(final Product product) {
        if (product == null) {
            return 0F;
        }
        try {
            // social weight
            int socialCount = 0;
            if (product.getFacebookLikes() != null) {
                socialCount += product.getFacebookLikes();
            }

            if (product.getPlusOneGoogle() != null) {
                socialCount += product.getPlusOneGoogle();
            }

            if (product.getTweets() != null) {
                socialCount += product.getTweets();
            }

            // retailerSpecific social weights

            // more reviews more stars == good product :)
            if (product.getStars() != null) {
                if (product.getReviews() != null) {
                    socialCount += product.getStars() * product.getReviews();
                } else {
                    socialCount += product.getStars();
                }
            } else {
                if (product.getReviews() != null) {
                    socialCount += product.getReviews();
                }
            }

            // to make the system unbiased all other weights are based on socialCount.
            // if a product is a bestSeller marked by retailer, but users hate it, its not the best :)
            // so the ranking is dependent on the socialCount as well.

            // discount 25% etc...
            float discount = 0F;
            // calculate the discount
            try {
                if (product.getListPrice() != null && product.getListPrice() > 0F &&
                        product.getPrice() != null && product.getPrice() > 0F) {
                    discount = ((product.getListPrice() - product.getPrice()) / product.getListPrice()) * 100;
                }
            } catch (Exception e) {
                // ignore
            }

            // this could happen if we didnt get any values for getListPrice and  getPrice or they were both 0.0.
            if (Float.isNaN(discount)) {
                discount = 0F;
            }
            // apply social count.
            discount = discount * socialCount;

            // retailer specific weights.
            int retailerWeight = 0;
            if (product.getBestSeller() != null && product.getBestSeller().equals(true)) {
                retailerWeight += socialCount;

            }
            if (product.getClearanceTag() != null && product.getClearanceTag().equals(true)) {
                retailerWeight += (socialCount / 2);
            }

            // compute the final weight.
            Float computedWeight = socialCount + discount + retailerWeight;
            if (Float.isNaN(computedWeight) || Float.isInfinite(computedWeight)) {
                // should never happen.
                return 0F;
            }
            return computedWeight;
        } catch (Exception e) {
            return 0F;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ProductReview> getProductReviews(Long productID, ServiceFilterBean filterBean) throws ProductNotFoundException, ProductDisabledException {
        Product product = getProduct(productID);
        return productReviewRepository.getProductReviews(product.getId(), filterBean);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Product> getProducts(ServiceFilterBean filterBean, String merchantName) {
        return this.productRepository.getProducts(filterBean, merchantName);
    }

    @Override
    @Transactional
    public Collection<Product> getProducts(String columnName, Object columnValue, Integer start, Integer maxProducts) {
        return this.productRepository.getProducts(columnName, columnValue, start, maxProducts);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ProductReview> getTopProductReview(final Product product,final ServiceFilterBean filterBean) {
        return this.productRepository.getTopProductReview(product,filterBean);
    }
}
