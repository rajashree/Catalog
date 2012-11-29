/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.registrar;


import com.sourcen.core.event.Event;
import com.sourcen.core.event.executor.ClassEventExecutor;
import com.sourcen.core.event.executor.EventExecutor;

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
public final class EventRegistrarForClasses implements EventRegistar<Class> {

    private static final EventRegistrarForClasses instance = new EventRegistrarForClasses();

    protected final Collection<Class> register = new CopyOnWriteArraySet<Class>();
    protected final Map<String, Collection<Class>> class_event_register = new ConcurrentHashMap<String, Collection<Class>>();

    public static EventRegistrarForClasses getInstance() {
        return instance;
    }


    @Override
    public void add(Class classEntry) {
        Map<String, Map<Class, Method>> events = MetadataCache.getEventsForClass(classEntry);
        for (Map.Entry<String, Map<Class, Method>> entry : events.entrySet()) {
            Collection<Class> classes = class_event_register.get(entry.getKey());
            if (classes == null) {
                classes = new CopyOnWriteArraySet<Class>();
                class_event_register.put(entry.getKey(), classes);
            }
            classes.add(classEntry);
        }
        register.add(classEntry);
    }

    @Override
    public void remove(Class classEntry) {

        Map<String, Map<Class, Method>> events = MetadataCache.getEventsForClass(classEntry);
        for (Map.Entry<String, Map<Class, Method>> entry : events.entrySet()) {
            Collection<Class> classes = class_event_register.get(entry.getKey());
            if (classes != null) {
                classes.remove(classEntry);
            }
        }

        register.remove(classEntry);

    }

    @Override
    public Collection<EventExecutor> getExecutors(Event event) {
        Collection<Class> classListeners = class_event_register.get(event.getType());
        if (classListeners != null) {
            Collection<EventExecutor> executors = new ArrayList<EventExecutor>(classListeners.size());
            for (Class clazz : classListeners) {
                Method method = MetadataCache.getListenerForClass(clazz, event.getType());
                EventExecutor executor = new ClassEventExecutor(clazz, method, event);
                executors.add(executor);
            }
            return executors;
        } else {
            return Collections.emptySet();
        }
    }
}
