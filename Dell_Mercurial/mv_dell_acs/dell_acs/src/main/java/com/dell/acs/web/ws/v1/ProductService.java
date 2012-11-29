/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1;

import com.dell.acs.managers.ContentFilterBean;
import com.dell.acs.web.ws.v1.beans.WSProduct;
import com.dell.acs.web.ws.v1.beans.WSProductReview;
import com.dell.acs.web.ws.v1.beans.WSProductSearchResult;
import com.sourcen.core.web.ws.WebService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3523 $, $Date:: 2012-06-22 11:09:34#$
 */
public interface ProductService extends WebService {

    public Collection<WSProduct> getProductRecommendations(ContentFilterBean filter);

    public Collection<WSProduct> getProducts(String searchTerm, String merchantName, Integer start, Integer maxProducts,
                                             String filter, String orderBy);

    public WSProduct getProduct(@RequestParam(required = true) Long productId) throws Exception;

    public Integer updateProductSales(Long productId) throws Exception;

    /**
     * Returns the latest shopped count for a given product.
     *
     * @param productId - the productID
     *
     * @return - Integer value of the shoppedCount. Return 0, if there is value.
     * @throws Exception
     */
    public Integer getProductShoppedCount(Long productId) throws Exception;

    public WSProduct getProductDetail(Long productId) throws Exception;

    public Collection<WSProduct> getProductDetails(String productIds) throws Exception;

    public Collection<WSProductReview> getPagedProductReviews(Long productID, Integer pageSize, Integer pageNumber) throws Exception;

    public WSProductSearchResult searchProducts(String searchTerm, String merchantName, Integer start,
                                                Integer maxProducts, String filter, String orderBy);

    public WSProduct updateProductStatus(Long productId, boolean status) throws Exception;
}
