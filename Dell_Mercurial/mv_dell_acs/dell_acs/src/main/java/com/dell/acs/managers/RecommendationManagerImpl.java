/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.repository.ProductImageRepository;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.persistence.repository.ProductSliderRepository;
import com.sourcen.core.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Chethan
 * Date: 2/14/12
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RecommendationManagerImpl implements RecommendationManager {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RecommendationManagerImpl.class);


    /**
     * ProductRepository Bean Injection.
     */
    @Autowired
    private ProductRepository productRepository;
    /**
     * CampaignManager Bean Injection.
     */
    @Autowired
    private CampaignManager campaignManager;
    /**
     * ProductImageRepository Bean Injection.
     */
    @Autowired
    private ProductImageRepository productImageRepository;
    /**
     * ProductSliderRepository Bean Injection.
     */
    @Autowired
    private ProductSliderRepository productSliderRepository;

    /**
     * Setter for Product Repository.
     *
     * @param productRepository
     */
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Setter for ProductImageRepository.
     *
     * @param productImageRepository
     */
    public void setProductImageRepository(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    /**
     * Setter for ProductSliderRepository.
     *
     * @param productSliderRepository
     */
    public void setProductSliderRepository(ProductSliderRepository productSliderRepository) {
        this.productSliderRepository = productSliderRepository;
    }

    /**
     * UrlUtils.
     */
    private UrlUtils urlUtils = new UrlUtils();

    // map -> K-v
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Collection<Product> getActiveRecommendedProducts(ContentFilterBean contentFilterBean) {

        Collection<Product> products = productRepository.getActiveProducts(contentFilterBean);
        if (!products.isEmpty()) {
            for (Product product : products) {
                product.setImages(productImageRepository.getProductImages(product));
                product.setSliders(productSliderRepository.getProductSlidersBySourceProduct(product));
            }
        }
        return products;


    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Collection<Product> getProducts(String productIds) {
        Collection<Product> products = productRepository.getProducts(productIds);
        if (!products.isEmpty()) {
            for (Product product : products) {
                product.setImages(productImageRepository.getProductImages(product));
                product.setSliders(productSliderRepository.getProductSlidersBySourceProduct(product));
            }
        }
        return products;
    }
}
