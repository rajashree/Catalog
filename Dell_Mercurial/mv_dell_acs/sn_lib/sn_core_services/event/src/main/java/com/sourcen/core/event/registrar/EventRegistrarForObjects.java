/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.registrar;


import com.sourcen.core.event.Event;
import com.sourcen.core.event.executor.EventExecutor;
import com.sourcen.core.event.executor.ObjectEventExecutor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public final class EventRegistrarForObjects implements EventRegistar<Object> {

    private static final EventRegistrarForObjects instance = new EventRegistrarForObjects();

    protected final Collection<Object> register = new CopyOnWriteArraySet<Object>();
    protected final Map<String, Collection<Object>> class_event_register = new ConcurrentHashMap<String, Collection<Object>>();

    public static EventRegistrarForObjects getInstance() {
        return instance;
    }


    @Override
    public void add(Object listener) {
        Class classEntry = listener.getClass();
        Map<String, Map<Class, Method>> events = MetadataCache.getEventsForClass(classEntry);
        for (Map.Entry<String, Map<Class, Method>> entry : events.entrySet()) {
            Collection<Object> listeners = class_event_register.get(entry.getKey());
            if (listeners == null) {
                listeners = new CopyOnWriteArraySet<Object>();
                class_event_register.put(entry.getKey(), listeners);
            }
            listeners.add(listener);
        }
        register.add(classEntry);
    }

    @Override
    public void remove(Object listener) {
        Class classEntry = listener.getClass();
        Map<String, Map<Class, Method>> events = MetadataCache.getEventsForClass(classEntry);
        for (Map.Entry<String, Map<Class, Method>> entry : events.entrySet()) {
            Collection<Object> listeners = class_event_register.get(entry.getKey());
            if (listeners != null) {
                listeners.remove(listener);
            }
        }

        register.remove(listener);

    }

    @Override
    public Collection<EventExecutor> getExecutors(Event event) {
        Collection<Object> listeners = class_event_register.get(event.getType());
        if (listeners != null) {
            Collection<EventExecutor> executors = new ArrayList<EventExecutor>(listeners.size());
            for (Object listener : listeners) {
                Class clazz = listener.getClass();
                Method method = MetadataCache.getListenerForClass(clazz, event.getType());
                EventExecutor executor = new ObjectEventExecutor(listener, method, event);
                executors.add(executor);
            }
            return executors;
        } else {
            return Collections.emptySet();
        }
    }
}
