/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.persistence.domain.Product;
import com.sourcen.core.managers.Manager;

import java.util.Collection;

/**
 * @author Navin Raj
 * @version 1/31/12 11:37 AM
 */
public interface RecommendationManager extends Manager {

    /**
     * Gets Recommended Products based on Filter Parameters.
     *
     * @param contentFilterBean
     *
     * @return Collection of Products
     */
    Collection<Product> getActiveRecommendedProducts(ContentFilterBean contentFilterBean);

    /**
     * Get Products based on CSV Product Ids
     *
     * @param productIds
     *
     * @return Collection of Products
     */
    Collection<Product> getProducts(String productIds);

}
