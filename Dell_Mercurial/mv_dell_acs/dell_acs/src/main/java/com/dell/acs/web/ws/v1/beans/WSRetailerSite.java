/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;


import com.sourcen.core.web.ws.beans.base.WSPropertiesAwareBeanModel;

import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class WSRetailerSite extends WSPropertiesAwareBeanModel {

    //WSRetailer retailer;
    String siteName;
    String siteUrl;
    String logoUri;
    Date createdDate;
    Date modifiedDate;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }


    @Override
    public String toString() {
        return "WSRetailerSite{" +
                "siteName='" + siteName + '\'' +
                ", siteUrl='" + siteUrl + '\'' +
                ", logoUri='" + logoUri + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
