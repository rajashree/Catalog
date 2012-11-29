package com.dell.acs.dataimport.urbandecay;

import com.dell.acs.managers.FileSystemUtil;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductImage;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;

import javax.activation.MimetypesFileTypeMap;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Adarsh
 * Date: 6/15/12
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
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
        product.setNewProduct((Boolean) row.get("newProduct"));
        /* Object condition = row.get("newProduct");
                if (condition != null) {
                    if (condition.toString().trim().equalsIgnoreCase("true")||condition.toString().trim().equalsIgnoreCase("new")) {
                        ((Product) bean).setNewProduct(true);
                        row.put("newProduct","true");
                    } else {
                        ((Product) bean).setNewProduct(false);
                        row.put("newProduct","false");
                    }
                }
        */
        String[] categoryHierarchyGoogle = row.get("properties.google_product_category").toString().split(">");
        if (categoryHierarchyGoogle.length > 0) {
            for (int i = 1; i < categoryHierarchyGoogle.length; i++) {
                row.put("properties.google_product_category" + i, categoryHierarchyGoogle[i - 1]);
            }
        }

        String[] categoryHierarchy = row.get("category1").toString().split(">");
        for (int i = 1; i <= categoryHierarchy.length; i++) {
            row.put("category" + i, categoryHierarchy[i - 1]);
        }


        product = (Product) super.preProcessBeforePersist(bean, row);

        Assert.notNull(row.get("image.srcImageURL"), "image is null for product:=" + product.getProductId());
        Assert.notNull(row.get("image.srcImageURL").toString().trim().length() == 0 ? null :
                row.get("image.srcImageURL"), "image is null for product :=" + product.getProductId());
        String baseImageName = FilenameUtils.getName(row.get("image.srcImageURL").toString());
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
            image.setImageName(baseImageName);
            image.setSrcImageURL(row.get("image.srcImageURL").toString());
            image.setCached(0);
            String imageFilePath = FileSystemUtil.getPath(product.getRetailerSite(), "cdn") + "/images/" + baseImageName;
            row.put("imageURL", imageFilePath);
            String fileExtension = baseImageName.substring(baseImageName.lastIndexOf(".") + 1, baseImageName.length());
            if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
                image.setImageType("image/jpeg");
            } else {
                image.setImageType(new MimetypesFileTypeMap().getContentType(baseImageName));
            }
            image.setModifiedDate(new Date());
            image.setProduct(product);
            images.add(image);
            product.setImages(images);
        }
        return bean;
    }
}
