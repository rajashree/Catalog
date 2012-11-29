/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.dataimport.ficstar;


import com.sourcen.dataimport.service.support.hibernate.BeanProcessorAdapter;
import com.dell.acs.managers.FileSystemUtil;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductImage;
import com.dell.acs.persistence.repository.ProductRepository;
import com.sourcen.dataimport.service.support.hibernate.BeanProcessor;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.activation.MimetypesFileTypeMap;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 2471 $, $Date:: 2012-05-21 08:08:32#$
 */

/**
 * {@inheritDoc}
 */
public final class ProductImageBeanProcessor extends BeanProcessorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ProductImageBeanProcessor.class);

    @Override
    public Map<String, Object> preProcessBeanValues(Map<String, Object> row) {
        try {
            if (row.get("product") == null) {
                throw new IllegalArgumentException("product-ID is null this must be the actual ID for "
                        + "the Product entity");
            }
            Assert.notNull(row.get("srcImageURL"), "srcImageURL is null for product:=" + row.get("product"));

            Product product = productRepository.get((Long) row.get("product"));
            Assert.notNull(product, "Unable to find product for productID:=" + row.get("product"));
            row.put("product", product);

            String baseName = null;

            if (row.get("imageType") == null) {
                baseName = FilenameUtils.getBaseName(row.get("srcImageURL").toString());
                try {
                    row.put("imageType", new MimetypesFileTypeMap().getContentType(baseName));
                } catch (Exception e) {
                    row.put("imageType", "image/jpeg");
                }
            }

            if (row.get("imageName") == null) {
                if (baseName == null) {
                    baseName = FilenameUtils.getBaseName(row.get("srcImageURL").toString());
                }
                row.put("imageName", baseName);
            }



            String imageFilePath = FileSystemUtil.getPath(product.getRetailerSite(), "cdn") + "/images/"
                    + row.get("imageName");
            row.put("imageURL", imageFilePath);


             if(row.get("heroImage")==null){
                row.put("heroImage",imageFilePath);
            }

        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return row;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object preProcessBeforePersist(final Object object, final Map<String, Object> row) {
        ProductImage bean = (ProductImage) object;
        if (bean.getImageURL() == null) {
            try {
                Product product = bean.getProduct();
                String imageFilePath = FileSystemUtil.getPath(product.getRetailerSite(), "cdn") + "/images/"
                        + row.get("imageName");
                bean.setImageURL(imageFilePath);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return bean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsBean(final Class clazz) {
        return ProductImage.class.equals(clazz);
    }


    /**
     * properties and corresponding setting() for value injection
     */
    private ProductRepository productRepository;

    public void setProductRepository(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


}
