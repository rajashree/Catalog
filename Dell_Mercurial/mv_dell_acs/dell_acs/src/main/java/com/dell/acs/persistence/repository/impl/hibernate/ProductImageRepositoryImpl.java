/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductImage;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.ProductImageRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import com.sourcen.core.util.Assert;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: Chethan
 * Date: 3/2/12
 * Time: 5:21 PM
 */
@Repository
public class ProductImageRepositoryImpl
        extends IdentifiableEntityRepositoryImpl<Long, ProductImage>
        implements ProductImageRepository {

    /**
     * Constructor.
     */
    public ProductImageRepositoryImpl() {
        super(ProductImage.class);
    }

    @Override
    public Collection<ProductImage> getProductImages(Product product) {
        Criteria criteria = getSession().createCriteria(ProductImage.class);
        criteria.add(Restrictions.eq("product.id", product.getId()));
        return criteria.list();
    }

    @Override
    public Collection<ProductImage> getProductImagesByMatch(String columnName, Object columnValue, int maxSize) {
        final Criteria criteria = getSession().createCriteria(entityClass);
        criteria.add(Restrictions.eq(columnName, columnValue));
        criteria.add(Restrictions.isNotNull("product"));
        criteria.setMaxResults(maxSize);
        List list = (List<ProductImage>) criteria.list();
        if (list.size() > 0) {
            return list;
        }
         return Collections.emptyList();
    }
}

