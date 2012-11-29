/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import com.sourcen.core.web.ws.beans.base.WSBeanModel;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class WSProductFilter extends WSBeanModel {

    private String searchTerm;
    private String productIDs;
    private String productTitle;
    private Long retailerID;
    private String productCategories;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(final String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getProductIDs() {
        return productIDs;
    }

    public void setProductIDs(final String productIDs) {
        this.productIDs = productIDs;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(final String productTitle) {
        this.productTitle = productTitle;
    }

    public Long getRetailerID() {
        return retailerID;
    }

    public void setRetailerID(final Long retailerID) {
        this.retailerID = retailerID;
    }

    public String getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(final String productCategories) {
        this.productCategories = productCategories;
    }
}
