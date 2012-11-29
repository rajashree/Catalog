/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductReview;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Chethan
 * Date: 4/9/12
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductReviewRepository extends IdentifiableEntityRepository<Long, ProductReview> {

    Collection<ProductReview> getProductReviews(Product product);

    Collection<ProductReview> getProductReviews(Long productID, int pageNumber, int pageSize);

    Collection<ProductReview> getProductReviews(Long productID, ServiceFilterBean filterBean);
}
