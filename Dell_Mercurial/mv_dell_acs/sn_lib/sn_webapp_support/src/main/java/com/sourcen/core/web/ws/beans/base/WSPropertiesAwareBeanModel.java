/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.ws.beans.base;

import com.sourcen.core.util.collections.AbstractPropertiesProvider;
import com.sourcen.core.web.ws.beans.WSProperty;

import javax.persistence.Transient;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class WSPropertiesAwareBeanModel extends AbstractPropertiesProvider implements WSPropertiesAwareBean {

    private Collection<WSProperty> properties;

    @Transient
    private transient Map<String, WSProperty> propertyMap = Collections.emptyMap();

    public Collection<WSProperty> getProperties() {
        return properties;
    }

    public void setProperties(final Collection<WSProperty> properties) {
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
        getPropertyMap().get(key).setValue(value);
    }

    @Override
    public Object getObjectProperty(final String key) {
        return getPropertyMap().get(key).getValue();
    }

    @Override
    public void refresh() {
        getPropertyMap().clear();
        for (WSProperty property : properties) {
            getPropertyMap().put(property.getName(), property);
        }
    }

    private Map<String, WSProperty> getPropertyMap() {
        if (properties.isEmpty()) {
            return Collections.emptyMap();
        }
        if (propertyMap.isEmpty()) {
            propertyMap = new HashMap<String, WSProperty>();
            for (WSProperty property : properties) {
                propertyMap.put(property.getName(), property);
            }
        }
        return propertyMap;
    }
}
