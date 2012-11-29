/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs.jpa;

import com.sourcen.core.util.collections.PropertiesProvider;
import org.apache.commons.lang.builder.EqualsBuilder;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 1139 $, $Date:: 2012-03-30 22:25:56#$
 */
@Embeddable
public class PropertiesAware implements com.sourcen.core.persistence.domain.constructs.PropertiesAware, Serializable{

    /**
     * a RecordProperties object to store the properties.
     */
    @Transient
    private final RecordProperties properties = new RecordProperties();


    @Override
    public final PropertiesProvider getProperties() {
        return this.properties;
    }

    @Override
    public String toString() {
        return properties.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
