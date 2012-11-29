package com.dell.acs.web.ws.v1.beans;


import com.sourcen.core.web.ws.beans.base.WSPropertiesAwareBeanModel;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: MarketVine
 * Date: 5/8/12
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class WSEvent extends WSPropertiesAwareBeanModel {

    Long id;
    String name;
    String description;
    Date startDate;
    Date endDate;
    String location;
    Date creationDate;
    Date modifiedDate;
    //private User createdBy;
    //private User modifiedBy;
    //private RetailerSite retailerSite;
    //private PropertiesAware properties;


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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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


}
