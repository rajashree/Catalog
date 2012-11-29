/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import com.dell.acs.persistence.domain.ProductImage;
import com.sourcen.core.util.beans.FieldMapping;
import com.sourcen.core.web.ws.beans.base.WSBeanModel;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Chethan
 * Date: 3/28/12
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class WSProductSearch extends WSBeanModel {


    Long id;
    String title;
    String url;
    Collection<ProductImage> images;
    String description;
    Float price;
    Float listPrice;
    @FieldMapping("stars")
    Float rating;
    WSRetailerSite retailerSite;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Collection<ProductImage> getImages() {
        return images;
    }

    public void setImages(Collection<ProductImage> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getListPrice() {
        return listPrice;
    }

    public void setListPrice(Float listPrice) {
        this.listPrice = listPrice;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }


    public WSRetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(WSRetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    @Override
    public String toString() {
        return "WSProductSearch{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", images=" + images +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", listPrice=" + listPrice +
                '}';
    }
}
