/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import com.sourcen.core.web.ws.beans.base.WSBeanModel;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Chethan
 * Date: 4/24/12
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class WSMerchantProduct extends WSBeanModel {

    //Product ID, name, product URL, image URL, and search keywords
    Long id;
    String title;
    String url;
    Float price;
    Float listPrice;
    Collection<WSProductImage> images;
    Boolean enabled;

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

    public Collection<WSProductImage> getImages() {
        return images;
    }

    public void setImages(Collection<WSProductImage> images) {
        this.images = images;
    }

    public Float getListPrice() {
        return listPrice;
    }

    public void setListPrice(Float listPrice) {
        this.listPrice = listPrice;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
   		this.enabled = (enabled == null) ? true : enabled;
    }

    @Override
    public String toString() {
        return "WSMerchantProduct{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", listPrice=" + listPrice +
                ", price=" + price +
                ", images=" + images +
                ", enabled=" + enabled +
                '}';
    }
}
