/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;


import com.sourcen.core.web.ws.beans.base.WSPropertiesAwareBeanModel;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class WSRetailer extends WSPropertiesAwareBeanModel {

    private String name;
    private String description;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "WSRetailer{"
                + ", description='" + description + '\''
                + ", name='" + name + '\''
                + ", url='" + url + '\''
                + '}';
    }
}
