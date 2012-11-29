/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers.model;

import com.sourcen.core.util.collections.AbstractPropertiesProvider;

import javax.persistence.Transient;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: navinr $
 @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$ */
public abstract class PropertiesAwareData extends AbstractPropertiesProvider implements FormData {

    private Collection<PropertyData> properties;

    @Transient
    private transient Map<String, PropertyData> propertyMap = new HashMap<String, PropertyData>(5);

    public Collection<PropertyData> getProperties() {
        return properties;
    }

    public void setProperties(final Collection<PropertyData> properties) {
        this.properties = properties;
    }

    @Override
    public boolean hasProperty(final String key) throws NullPointerException {
        return getPropertyMap().containsKey(key);
    }

    @Override
    public Set<String> keySet() {
        return getPropertyMap().keySet();
    }

    @Override
    public void setProperty(final String key, final String value) {
        PropertyData obj = getPropertyMap().get(key);
        if (obj == null) {
            obj = new PropertyData(key, value);
            getPropertyMap().put(key, obj);
        } else {
            obj.setValue(value);
        }
    }

    @Override
    public Object getObjectProperty(final String key) {
        return getPropertyMap().get(key).getValue();
    }

    @Override
    public void refresh() {
        getPropertyMap().clear();
        if (properties != null) {
            for (PropertyData property : properties) {
                getPropertyMap().put(property.getName(), property);
            }
        }
    }

    private Map<String, PropertyData> getPropertyMap() {
        if (properties == null || properties.isEmpty()) {
            return propertyMap;
        }

        if (propertyMap.isEmpty()) {
            propertyMap = new HashMap<String, PropertyData>();
            for (PropertyData property : properties) {
                propertyMap.put(property.getName(), property);
            }
        }
        return propertyMap;
    }
}
