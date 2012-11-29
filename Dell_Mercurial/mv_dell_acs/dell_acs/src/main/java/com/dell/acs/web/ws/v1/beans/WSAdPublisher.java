/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import com.sourcen.core.util.beans.FieldMapping;
import com.sourcen.core.web.ws.beans.base.WSPropertiesAwareBeanModel;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class WSAdPublisher extends WSPropertiesAwareBeanModel {

    private Long id;

    @FieldMapping("user.id")
    private Long user;

    private String website;

    private String apiKey;

    public WSAdPublisher() {
    }

    public Long getUser() {
        return user;
    }

    public void setUser(final Long user) {
        this.user = user;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(final String website) {
        this.website = website;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
