/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.registrar;


import com.sourcen.core.event.Event;
import com.sourcen.core.event.executor.BeanEventExecutor;
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
 * @version $Revision: 2766 $, $Date:: 2012-05-30 02:19:10#$
 */
public final class EventRegistrarForBeans implements EventRegistar<String> {

    private static final EventRegistrarForBeans instance = new EventRegistrarForBeans();
    private static BeanFactory beanFactory = null;

    protected final Collection<String> register = new CopyOnWriteArraySet<String>();
    protected final Collection<String> uninitialized_register = new CopyOnWriteArraySet<String>();
    protected final Map<Class, String> class_beanName_cache = new ConcurrentHashMap<Class, String>();

    // eventName, beanName
    protected final Map<String, Collection<String>> class_event_register = new ConcurrentHashMap<String, Collection<String>>();

    static {
        EventRegistrarForObjects.getInstance().add(new BeanFactoryInitializedEventListener());
    }

    public static EventRegistrarForBeans getInstance() {
        return instance;
    }

    @Override
    public void add(String beanName) {
        if (beanFactory != null) {
            registerBean(beanName);
        } else {
            uninitialized_register.add(beanName);
        }
    }

    private void registerBean(String beanName) {
        if (beanFactory == null) {
            throw new UnsupportedOperationException("Cannot register a bean when the BeanFactory is not initialized");
        }

        Class classEntry = beanFactory.getType(beanName);

        Map<String, Map<Class, Method>> events = MetadataCache.getEventsForClass(classEntry);
        for (Map.Entry<String, Map<Class, Method>> entry : events.entrySet()) {
            Collection<String> registeredBeanNames = class_event_register.get(entry.getKey());
            if (registeredBeanNames == null) {
                registeredBeanNames = new CopyOnWriteArraySet<String>();
                class_event_register.put(entry.getKey(), registeredBeanNames);
            }
            registeredBeanNames.add(beanName);
        }
        register.add(beanName);

    }

    @Override
    public void remove(String beanName) {

        if (beanFactory != null) {
            Class classEntry = beanFactory.getType(beanName);
            Map<String, Map<Class, Method>> events = MetadataCache.getEventsForClass(classEntry);
            for (Map.Entry<String, Map<Class, Method>> entry : events.entrySet()) {
                Collection<String> registeredBeanNames = class_event_register.get(entry.getKey());
                if (registeredBeanNames != null) {
                    registeredBeanNames.remove(beanName);
                }
            }
        }

        register.remove(beanName);
        uninitialized_register.remove(beanName);

    }

    @Override
    public Collection<EventExecutor> getExecutors(Event event) {
        Collection<String> listeners = class_event_register.get(event.getType());
        if (listeners != null) {
            Collection<EventExecutor> executors = new ArrayList<EventExecutor>(listeners.size());
            for (String listener : listeners) {
                Class clazz = beanFactory.getType(listener);
                Method method = MetadataCache.getListenerForClass(clazz, event.getType());
                EventExecutor executor = new BeanEventExecutor(listener, method, event);
                executors.add(executor);
            }
            return executors;
        } else {
            return Collections.emptySet();
        }
    }

    public static final class BeanFactoryInitializedEventListener {

        public void onBeanFactoryInitialized(BeanFactoryInitializedEvent event) {
            if (beanFactory == null) {
                beanFactory = event.getBeanFactory();
                BeanEventExecutor.setBeanFactory(beanFactory);
                for (String beanName : EventRegistrarForBeans.getInstance().uninitialized_register) {
                    EventRegistrarForBeans.getInstance().registerBean(beanName);
                }
            }
        }
    }
}
