/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.impl.jpa;

import com.sourcen.core.persistence.domain.HierarchicalPropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.util.ObjectUtils;
import com.sourcen.core.util.collections.PropertiesProvider;

import javax.persistence.MappedSuperclass;

/**
 * Extended Entity that supports Nested sets.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @see http://dev.mysql.com/tech-resources/articles/hierarchical-data.html
 * @see http://en.wikipedia.org/wiki/Nested_set_model
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class HierarchicalPropertiesAwareModel extends HierarchyAwareModel implements HierarchicalPropertiesAwareEntity {


    private PropertiesAware properties;

    @Override
    public PropertiesProvider getProperties() {
        return properties.getProperties();
    }

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(properties);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(HierarchicalPropertiesAwareEntity.class) && obj.hashCode() == this.hashCode();
    }

}
