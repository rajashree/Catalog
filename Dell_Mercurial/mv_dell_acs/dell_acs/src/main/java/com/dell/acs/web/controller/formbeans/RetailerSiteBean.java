package com.dell.acs.web.controller.formbeans;

import com.dell.acs.persistence.domain.Retailer;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$
 */
public class RetailerSiteBean implements FormBean {

    private RetailerBean retailer;

    private String siteName;

    private String siteUrl;

    private String logoUri;

    public RetailerBean getRetailer() {
        return retailer;
    }

    public void setRetailer(final RetailerBean retailer) {
        this.retailer = retailer;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(final String siteName) {
        this.siteName = siteName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(final String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(final String logoUri) {
        this.logoUri = logoUri;
    }
}
