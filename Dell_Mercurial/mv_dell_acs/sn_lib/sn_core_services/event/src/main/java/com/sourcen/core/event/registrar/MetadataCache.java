/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.registrar;

import com.sourcen.core.event.Event;
import com.sourcen.core.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public final class MetadataCache {

    private static final Map<Class, Map<String, Map<Class, Method>>> class_cache = new ConcurrentHashMap<Class, Map<String, Map<Class, Method>>>();


    public static Map<String, Map<Class, Method>> getEventsForClass(Class classEntry) {
        if (class_cache.containsKey(classEntry)) {
            return class_cache.get(classEntry);
        }

        Map<String, Map<Class, Method>> events = new ConcurrentHashMap<String, Map<Class, Method>>();

        for (Method method : classEntry.getMethods()) {
            int modifier = method.getModifiers();
            String methodName = method.getName();
            if (Modifier.isPublic(modifier) && !Modifier.isStatic(modifier) && !Modifier.isAbstract(modifier)) {
                Class[] paramTypes = method.getParameterTypes();
                if (paramTypes.length == 1 && Event.class.isAssignableFrom(paramTypes[0]) && methodName.startsWith("on")) {
                    String eventName = StringUtils.convertCamelCaseToConstant(methodName.substring(2));

                    Map<Class, Method> event_class_method_cache = events.get(eventName);
                    if (event_class_method_cache == null) {
                        event_class_method_cache = new ConcurrentHashMap<Class, Method>();
                        events.put(eventName, event_class_method_cache);
                    }
                    event_class_method_cache.put(classEntry, method);
                }
            }
        }
        class_cache.put(classEntry, events);
        return events;
    }

    public static Method getListenerForClass(Class clazz, String eventName) {
        Map<String, Map<Class, Method>> events = getEventsForClass(clazz);
        if (events.containsKey(eventName)) {
            return events.get(eventName).get(clazz);
        }
        return null;
    }

}
