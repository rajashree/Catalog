package com.dell.acs.managers.model;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$
 */
public class RetailerSiteData implements FormData {

    private Long id;

    private RetailerData retailer;

    private String siteName;

    private String siteUrl;

    private String logoUri;

    private Long version;

    private Date createdDate;

    private Date modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RetailerData getRetailer() {
        return retailer;
    }

    public void setRetailer(final RetailerData retailer) {
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
}
