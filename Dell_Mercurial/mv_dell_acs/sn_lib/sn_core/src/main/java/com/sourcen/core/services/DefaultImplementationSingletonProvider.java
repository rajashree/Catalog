/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.services;

import com.sourcen.core.SourcenRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2803 $, $Date:: 2012-06-01 08:33:25#$
 */
public class DefaultImplementationSingletonProvider {

    private static final Logger log = LoggerFactory.getLogger(DefaultImplementationSingletonProvider.class);

    private static final Map<Class, Object> defaultImplementations = new ConcurrentHashMap<Class, Object>(100);
    private static final Set<Class> noImplementationFound = new CopyOnWriteArraySet<Class>();

    private static Properties properties = new Properties();

    static {
        try {
            InputStream is = DefaultImplementationSingletonProvider.class.getClassLoader().getResourceAsStream("config/default_implementations.properties");
            if(is != null){
                properties.load(is);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static <T> T getDefaultService(Class<T> iface) {
        return getDefaultService(iface, null, true);
    }


    public static <T> T getDefaultService(final Class<T> iface, final String className) {
        try {
            Class<? extends T> clazz = (Class<? extends T>) ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
            return getDefaultService(iface, clazz, true);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SourcenRuntimeException(e);
        }
    }

    public static <T> T getDefaultService(Class<T> iface, Class<? extends T> fallbackImplementationClass) {
        return getDefaultService(iface, fallbackImplementationClass, true);
    }


    private static <T> T getDefaultService(Class<T> iface, Class<? extends T> fallbackImplementationClass, Boolean cache) {
        Object result = null;
        if (cache) {
            result = defaultImplementations.get(iface);
        }
        if (result == null) {
            result = getDefaultService0(iface, fallbackImplementationClass);
            defaultImplementations.put(iface, result);
        }
        return (T) result;
    }

    public static boolean isDefaultImplementationInitialized(Class iface) {
        return defaultImplementations.containsKey(iface);
    }

    private static <T> T getDefaultService0(Class<T> iface, Class<? extends T> fallbackImplementationClass) {

        Object result = null;

        if (noImplementationFound.contains(iface)) {
            throw new SourcenRuntimeException("No default implementation found for interface:=" + iface);
        }

        // check in properties
        Class<?> defaultImplementationClass = null;
        if (properties.containsKey(iface.getCanonicalName())) {
            try {
                defaultImplementationClass = ClassUtils.forName(properties.getProperty(iface.getCanonicalName()), ClassUtils.getDefaultClassLoader());
            } catch (ClassNotFoundException e) {
                log.warn(e.getMessage(), e);
            }
        } else {
            // just check the annotation.
            DefaultImplementation annotation = iface.getAnnotation(DefaultImplementation.class);
            if (annotation != null) {
                defaultImplementationClass = annotation.value();
                if (defaultImplementationClass.equals(Object.class)) {
                    // check for className
                    if (log.isDebugEnabled()) {
                        log.debug("trying to load class :=" + annotation.className() + " as defaultImplementation for interface:=" + iface);
                        try {
                            defaultImplementationClass = ClassUtils.forName(annotation.className(), ClassUtils.getDefaultClassLoader());
                        } catch (ClassNotFoundException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }

        if (defaultImplementationClass == null && fallbackImplementationClass != null) {
            defaultImplementationClass = fallbackImplementationClass;
        }

        if (defaultImplementationClass != null) {
            // check for singleton.
            try {
                Method instanceMethod = defaultImplementationClass.getMethod("getInstance");
                try {
                    result = instanceMethod.invoke(defaultImplementationClass);
                } catch (Exception e) {
                    log.error("Unable to get a singleton instance of interface :=" + iface + " and class:="
                            + defaultImplementationClass, e);
                }
            } catch (NoSuchMethodException nsme) {
                // just instantiate
                try {
                    result = defaultImplementationClass.newInstance();
                } catch (Exception e) {
                    log.error("Unable to instantiate default constructor for interface:=" + iface + " and class:="
                            + defaultImplementationClass, e);
                }
            }
        }

        if (result != null) {
            return (T) result;
        } else {
            noImplementationFound.add(iface);
            throw new SourcenRuntimeException("No default implementation found for interface:=" + iface);
        }
    }
}
