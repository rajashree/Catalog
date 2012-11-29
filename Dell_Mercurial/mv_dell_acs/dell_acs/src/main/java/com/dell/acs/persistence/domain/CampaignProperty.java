/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.EntityPropertyModel;
import com.sourcen.core.util.collections.PropertiesProvider;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 */
//sfisk - CS-380
@Table(name = "t_campaign_properties")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CampaignProperty extends EntityPropertyModel {

    @Embedded
    private PropertiesAware properties;

    //      @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }
}
