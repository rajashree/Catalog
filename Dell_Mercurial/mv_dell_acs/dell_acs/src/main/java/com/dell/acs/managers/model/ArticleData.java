package com.dell.acs.managers.model;

import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.Date;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */
@Deprecated
public class ArticleData implements FormData {

    private Long id;

    @NotEmpty(message = "Title is a mandatory field")
    private String title;

    @NotEmpty(message = "Description is a mandatory field")
    private String description;

    private String body;

    private Long version;

    private Date creationDate;

    private Date modifiedDate;

    private UserData createdBy;

    private UserData modifiedBy;

    private RetailerSiteData retailerSite;

    private PropertiesAware properties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getVersion() {
        return version;
    }

    public RetailerSiteData getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(RetailerSiteData retailerSite) {
        this.retailerSite = retailerSite;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public PropertiesAware getProperties() {
        return properties;
    }

    public void setProperties(PropertiesAware properties) {
        this.properties = properties;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public UserData getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserData createdBy) {
        this.createdBy = createdBy;
    }

    public UserData getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserData modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
