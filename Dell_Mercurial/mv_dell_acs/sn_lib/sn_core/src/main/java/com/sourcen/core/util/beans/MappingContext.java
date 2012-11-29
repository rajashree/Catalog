/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.beans;

import com.sourcen.core.util.collections.MapBackedPropertiesProvider;
import com.sourcen.core.util.collections.PropertiesProvider;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class MappingContext {

    private PropertiesProvider properties = new MapBackedPropertiesProvider();

    private String scope = null;
    private String fieldScope = null;

    private Map<Class, Map<String, String>> stack = new LinkedHashMap<Class, java.util.Map<String, String>>(100);

    public MappingContext() {
    }

    public MappingContext(final String scope) {
        this.scope = scope;
    }

    public PropertiesProvider getProperties() {
        return properties;
    }

    public String getScope() {
        return scope;
    }

    public String getFieldScope() {
        return fieldScope;
    }

    public void setFieldScope(final String fieldScope) {
        this.fieldScope = fieldScope;
    }

    public Map<Class, Map<String, String>> getStack() {
        return stack;
    }
    public Map<String, String> getStackForClass(Class clazz) {
        Map<String, String> result = stack.get(clazz);
        if(result == null){
            result = new LinkedHashMap<String, String>();
            stack.put(clazz, result);
        }
        return result;
    }
}
