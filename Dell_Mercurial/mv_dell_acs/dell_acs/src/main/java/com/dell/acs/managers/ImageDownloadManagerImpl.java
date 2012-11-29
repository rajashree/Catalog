package com.dell.acs.managers;

import com.dell.acs.persistence.domain.ProductImage;
import com.dell.acs.persistence.repository.ProductImageRepository;
import com.sourcen.core.util.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.spi.LoggerFactoryBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author Adarsh
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1595 $, $Date:: 5/29/12 5:26 PM#$
 */
@Service
public class ImageDownloadManagerImpl implements ImageDownloadManager {

    private static final Logger logger = Logger.getLogger(ImageDownloadManagerImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<ProductImage> getProductImagesByMatch(String columnName, Object columnValue, int maxSize) {
        Collection<ProductImage> productImages = productImageRepository.getProductImagesByMatch(columnName, columnValue, maxSize);
        for (ProductImage productImage : productImages) {
        	if (productImage != null) {
	            Object product = productImage.getProduct();
	            if (product != null) {
	                if (productImage != null && StringUtils.isEmpty(productImage.getImageURL())) {
	                    productImage.setImageURL(FileSystemUtil.getPath(productImage.getProduct().getRetailerSite()
	                            , "cdn") + "/images/" + productImage.getImageName());
	                }
	            } else {
	                updateDownloadedImage(productImage, 2);
	                logger.error("Product isn't found for " + productImage.getId());
	            }
        	} else {
                logger.error("Internal Error - null product image in array");
        	}
        }
        return productImages;
    }


    @Override
    @Transactional
    public void updateDownloadedImage(ProductImage productImage, Integer value) {
        productImage.setCached(value);
        productImageRepository.update(productImage);
    }

    @Autowired
    private ProductImageRepository productImageRepository;

    public void setProductImageRepository(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }
}
