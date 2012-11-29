/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductImage;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Chethan
 * Date: 3/2/12
 * Time: 5:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductImageRepository extends IdentifiableEntityRepository<Long, ProductImage> {
    /**
     * Get Collection of ProductImages by Products.
     *
     * @param product
     * @return Collection of Product Images
     */
    Collection<ProductImage> getProductImages(Product product);

    Collection<ProductImage> getProductImagesByMatch(String columnName, Object columnValue,int maxSize);

}
