/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1;

import com.dell.acs.web.ws.v1.beans.WSProduct;
import com.dell.acs.web.ws.v1.beans.WSProductReview;
import com.dell.acs.web.ws.v1.beans.WSRetailerSite;
import com.sourcen.core.web.ws.WebService;

import java.util.Collection;

/**
 * User: Chethan
 * Date: 4/23/12
 * Time: 12:50 PM
 */
public interface MerchantService extends WebService {
    public Collection<WSRetailerSite> getActiveMerchants();

    public Collection<WSProduct> getPagedProductsByMerchant(
            String merchant, Integer pageSize, Integer pageNumber);

    public WSProduct getProductDetail(Long productId) throws Exception;

    public Collection<WSProduct> getProductDetails(String productIds, String scope) throws Exception;

    public Collection<WSProductReview> getPagedProductReviews(Long productID, Integer pageSize,Integer pageNumber) throws Exception;

    public Collection<WSProduct> getAllPagedProductsByMerchant(
            String merchant, Integer pageSize, Integer pageNumber);


}