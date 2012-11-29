package com.dell.acs.managers;

import com.dell.acs.persistence.domain.ProductImage;
import com.sourcen.core.managers.Manager;

import java.util.Collection;

/**
 * @author Adarsh
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1595 $, $Date:: 5/29/12 5:26 PM#$
 */
public interface ImageDownloadManager extends Manager {

   Collection<ProductImage> getProductImagesByMatch(String columnName, Object columnValue,int maxSize);

    public void updateDownloadedImage(ProductImage productImage,Integer value);
}
