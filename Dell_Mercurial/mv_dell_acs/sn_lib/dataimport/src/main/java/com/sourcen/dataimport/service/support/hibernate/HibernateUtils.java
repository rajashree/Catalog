/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.hibernate;

import com.sourcen.dataimport.definition.ColumnDefinition;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: adarsh $
 @version $Revision: 1723 $, $Date:: 2012-04-18 11:46:28#$ */
public final class HibernateUtils {

    /**
     logger class
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HibernateUtils.class);

    private static final Map<Class, Map<String, Method>> CACHE = new ConcurrentHashMap<Class, Map<String, Method>>();

    public static Map<String, Method> getBeanSetters(final Collection<ColumnDefinition> fields, final Class clazz) {

        Map<String, Method> methods = CACHE.get(clazz);
        if (methods == null) {
            // parse.
            methods = new ConcurrentHashMap<String, Method>();

            Set<String> availableFields = new HashSet<String>();
            for (ColumnDefinition field : fields) {
                availableFields.add(field.getDestination().toLowerCase());
            }

            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(clazz);
            for (PropertyDescriptor pd : propertyDescriptors) {
                String fieldName = pd.getDisplayName();
                if (availableFields.contains(fieldName.toLowerCase())) {
                    logger.debug("adding setter/getter for field :=" + fieldName + " on " + clazz.getCanonicalName());
                    methods.put(fieldName, pd.getWriteMethod());
                } else {
                    if (!fieldName.equalsIgnoreCase("class")) {
                        logger.info("Unable to find setter/getter for field :=" + fieldName + " on "
                                + clazz.getCanonicalName());
                    }
                }
            }
            CACHE.put(clazz, methods);
        }
        return methods;
    }

}
