/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductReview;
import com.dell.acs.persistence.repository.ProductReviewRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Chethan
 * Date: 4/9/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ProductReviewRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, ProductReview>
        implements ProductReviewRepository {

    /**
     * Constructor.
     */
    public ProductReviewRepositoryImpl() {
        super(ProductReview.class);
    }

    @Override
    public Collection<ProductReview> getProductReviews(Product product) {
        return getProductReviews(product.getId(), -1, -1);
    }

    @Override
    public Collection<ProductReview> getProductReviews(Long productID, int pageNumber, int pageSize) {

        final Product product = (Product) getSession().get(Product.class, productID);

        // check if product is active first.
        if(product.getRetailer().getActive() && product.getRetailerSite().getActive()){

            Criteria reviewsCriteria = getSession().createCriteria(ProductReview.class)
                    .add(Restrictions.eq("product.id", product.getId()));

            if (pageSize != -1 ) {
                reviewsCriteria.setFirstResult(pageNumber);
            }

            if (pageSize == 0) {
                pageSize = 5;
            }

            if(pageSize>-1){
                reviewsCriteria.setMaxResults(pageSize);
            }


            return onFindForList(reviewsCriteria.list());

        }   else{
            logger.info("product with id={} is inactive, will not return any reviews.", product.getId());
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<ProductReview> getProductReviews(Long productID, ServiceFilterBean filterBean) {

        final Product product = (Product) getSession().get(Product.class, productID);

        // check if product is active first.
        if(product.getRetailer().getActive() && product.getRetailerSite().getActive()){

            Criteria reviewsCriteria = getSession().createCriteria(ProductReview.class)
                    .add(Restrictions.eq("product.id", product.getId()));

            //Apply generic criteria
            applyGenericCriteria(reviewsCriteria, filterBean);

            return onFindForList(reviewsCriteria.list());

        }   else{
            logger.info("product with id={} is inactive, will not return any reviews.", product.getId());
        }
        return Collections.emptyList();
    }
}
