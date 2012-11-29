/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductSlider;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.persistence.repository.ProductSliderRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: Adarsh
 * Date: 2/29/12
 * Time: 1:47 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * {@inheritDoc}
 */
@Repository
public class ProductSliderRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, ProductSlider> implements ProductSliderRepository {

    public ProductSliderRepositoryImpl() {
        super(ProductSlider.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductSlider getBySiteNameAndProductId(String siteName, String productId) {
        try {
            Object o = getSession().createCriteria(ProductSlider.class)
                    .add(Restrictions.eq("siteName", siteName))
                    .add(Restrictions.eq("productId", productId))
                    .uniqueResult();
            if (o != null) {
                return (ProductSlider) o;
            }
        } catch (Exception e) {
            logger.warn("ProductSliderRepositoryImpl " + e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductSlider saveOrUpdate(ProductSlider productSlider) {
        super.getSession().saveOrUpdate(productSlider);
        return productSlider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ProductSlider> getProductSlidersBySourceProduct(Product sourceProduct) {
        Collection<ProductSlider> sliders = Collections.emptyList();

        Criteria sliderCriteria = getSession().createCriteria(ProductSlider.class);
                sliderCriteria.add(Restrictions.eq("sourceProduct.id", sourceProduct.getId()));
        sliders = sliderCriteria.list();

        //CS-544 - Optimization: Don't think we need to iterate and set the targetProduct as
        //we now have @Fetch(FetchMode.SELECT)
        /*for (ProductSlider slider : sliders) {
            slider.setTargetProduct(productRepository.get(slider.getTargetProduct().getId()));
        }*/
        return sliders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ProductSlider> getProductSlidersByTargetProduct(Product targetProduct) {
        return getSession().createCriteria(ProductSlider.class)
                .add(Restrictions.eq("targetProduct.id", targetProduct.getId())).list();
    }

    /**
     * Class Properties and its corresponding setter() and getter() for value injection
     */
    @Autowired
    ProductRepository productRepository;

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


}
