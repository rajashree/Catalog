/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.dataimport.golfsmith;

import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductImage;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;

import javax.activation.MimetypesFileTypeMap;
import java.util.*;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 3883 $, $Date:: 2012-07-09 03:31:30#$
 */
public class ProductBeanProcessor extends com.dell.acs.dataimport.ficstar.ProductBeanProcessor {

    @Override
    public boolean supportsBean(final Class clazz) {
        return clazz.equals(Product.class);
    }

    @Override
    public Map<String, Object> preProcessBeanValues(final Map<String, Object> row) {
        return row;
    }

    @Override
    public Object preProcessBeforePersist(final Object bean, final Map<String, Object> row) {
        Product product = (Product) bean;

        Assert.notNull(row.get("image.srcImageURL"), "image is null for product:=" + product.getProductId());
        Assert.notNull(row.get("image.srcImageURL").toString().trim().length() == 0 ? null :
                row.get("image.srcImageURL"), "image is null for product :=" + product.getProductId());

        String baseImageName = null;
        if (row.get("image.imageType") == null) {
            baseImageName = FilenameUtils.getName(row.get("image.srcImageURL").toString());
            try {
                row.put("image.imageType", new MimetypesFileTypeMap().getContentType(baseImageName));
            } catch (Exception e) {
                row.put("image.imageType", "image/jpeg");
            }
        }

        if (row.get("image.imageName") == null) {
            if (baseImageName == null) {
                baseImageName = FilenameUtils.getName(row.get("image.srcImageURL").toString());
            }
            row.put("image.imageName", baseImageName);
        }

        if (product.getId() != null && product.getImages().size() != 0) {
            logger.info("Product Image is found for Updating :=> " + product.getProductId());
            Collection<ProductImage> productImages = product.getImages();
            Iterator<ProductImage> iterator = productImages.iterator();
            while (iterator.hasNext()) {
                ProductImage productImage = iterator.next();
                if (productImage.getImageName().equalsIgnoreCase(baseImageName)) {
                    String fileExtension = baseImageName.substring(baseImageName.lastIndexOf(".") + 1, baseImageName.length());
                    if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
                        productImage.setImageType("image/jpeg");
                    }
                    productImage.setSrcImageURL(row.get("image.srcImageURL").toString());
                    productImage.setModifiedDate(new Date());
                    productImage.setProduct(product);
                    productImage.setCached(0);
                }
            }
        } else {
            logger.info("Product Image is found for Insert :=> " + product.getProductId());
            Collection<ProductImage> images = new LinkedList<ProductImage>();
            ProductImage image = new ProductImage();
            image.setImageType(row.get("image.imageType").toString());
            image.setImageName(row.get("image.imageName").toString());
            image.setSrcImageURL(row.get("image.srcImageURL").toString());
            image.setCached(0);
            image.setModifiedDate(new Date());
            image.setProduct(product);
            images.add(image);
            product.setImages(images);
        }
        // handle categories.
        String[] categoryHierarchy = row.get("properties.Category_Hierarchy").toString().split(">");
        for (int i = 1; i <= categoryHierarchy.length; i++) {
            row.put("category" + i, categoryHierarchy[i - 1]);
        }
        ((Product) bean).setUrl(row.get("buyLink").toString());
        return super.preProcessBeforePersist(bean, row);
    }
}