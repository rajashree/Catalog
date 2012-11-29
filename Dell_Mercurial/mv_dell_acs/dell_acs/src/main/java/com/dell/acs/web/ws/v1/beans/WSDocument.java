package com.dell.acs.web.ws.v1.beans;


import com.sourcen.core.web.ws.beans.base.WSPropertiesAwareBeanModel;

import java.util.Date;

/**
 * User: MarketVine
 * Date: 5/8/12
 * Time: 10:41 AM
 */
public class WSDocument extends WSPropertiesAwareBeanModel {

    Long id;
    String name;
    String image;
    String document;
    String description;
    Date startDate;
    Date endDate;
    Date creationDate;
    Date modifiedDate;

    //User createdBy;
    //User modifiedBy;
    //RetailerSite retailerSite;
    String contentType;
    // Image - url
    // Video - image, url
    // Link - url
    String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
