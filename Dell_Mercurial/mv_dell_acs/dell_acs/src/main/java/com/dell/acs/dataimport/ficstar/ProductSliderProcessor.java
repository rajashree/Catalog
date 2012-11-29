/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.dataimport.ficstar;

import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductSlider;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.sourcen.dataimport.service.support.hibernate.BeanProcessorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2246 $, $Date:: 2012-05-10 14:00:33#$
 */

/**
 * {@inheritDoc}
 */
public final class ProductSliderProcessor extends BeanProcessorAdapter {


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsBean(Class clazz) {
        return ProductSlider.class.equals(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> preProcessBeanValues(Map<String, Object> row) {
        if (row.get("sourceProduct") == null) {
            throw new IllegalArgumentException("sourceProduct is null "
                    + "this must be the actual ID for the Product entity");
        }

        Product sourceProduct = productRepository.get((Long) row.get("sourceProduct"));
        Assert.notNull(sourceProduct, "Unable to find product for productID:=" + row.get("product"));
        row.put("sourceProduct", sourceProduct);

        if (row.get("targetProduct") == null) {
            throw new IllegalArgumentException("targetProduct is null"
                    + " this must be the actual ID for the Product entity");
        }
        Product targetProduct = productRepository.get((Long) row.get("targetProduct"));
        Assert.notNull(targetProduct, "Unable to find product for productID:=" + row.get("targetProduct"));
        row.put("targetProduct", targetProduct);


        if (row.get("retailerSite") == null) {
            throw new IllegalArgumentException("siteName is null"
                    + " this must be the actual ID for the RetailerSite entity");
        }
        RetailerSite retailerSite = retailerSiteRepository.getByName(row.get("retailerSite").toString());
        Assert.notNull(retailerSite, "Unable to find retailerSite for siteName:=" + row.get("retailerSite"));
        row.put("retailerSite", retailerSite);


        return row;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object preProcessBeforePersist(Object bean, Map<String, Object> row) {
        return bean;
    }

    @Autowired
    private RetailerSiteRepository retailerSiteRepository;

    public void setRetailerSiteRepository(RetailerSiteRepository retailerSiteRepository) {
        this.retailerSiteRepository = retailerSiteRepository;
    }

    @Autowired
    private ProductRepository productRepository;

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
