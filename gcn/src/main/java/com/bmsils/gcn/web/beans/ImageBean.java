/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.beans;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/24/12
 * Time: 6:55 PM
 * ImageBean is a UI bean object being used to map Image Entity
 */
public class ImageBean {
    private Long id;
    private boolean isDefault;
    private byte[] image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
