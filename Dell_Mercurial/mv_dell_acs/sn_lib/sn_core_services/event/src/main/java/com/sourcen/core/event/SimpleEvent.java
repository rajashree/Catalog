/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.event;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An event is bean that provides us meta-data about an event such as type, creator or target, and data associated with
 * the event. When an Object would like to trigger a new event to be dispatched to all the listeners a new
 * {@link SimpleEvent} object is created, and once all the proeprties are set, should use the
 *
 * @param <T> is the type of the target object that this event represents.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2730 $, $Date:: 2012-05-29 12:07:23#$
 * @since 1.01
 */
public class SimpleEvent<T> implements Event<T> {

    private static final long serialVersionUID = -6297835015574011332L;
    private static final Map<String, String> listenerMethodNameCache = new ConcurrentHashMap<String, String>();
    /**
     * type of event.
     */
    protected String type;

    /**
     * Creator or the Target of the event.
     */
    protected T target;

    /**
     * Data that can be passed along with the event.
     */
    protected Map<String, Object> properties;

    /**
     * Creates a new SimpleEvent instance.
     *
     * @param type of type Object
     */
    public SimpleEvent(final Object type) {
        this(type.toString());
    }

    /**
     * A simple constructor to create an instance of Event with 'type' set.
     *
     * @param type of the event
     */
    public SimpleEvent(final String type) {
        this(type, null, Collections.EMPTY_MAP);
    }

    /**
     * Construct an instance of Event with the 'type' and 'target' set.
     *
     * @param type   of the event.
     * @param target creator or a target of the event.
     */
    public SimpleEvent(final String type, final T target) {
        this(type, target, Collections.EMPTY_MAP);
    }

    /**
     * A full constructor to create an instance of Event with the 'type','target' and 'data' set.
     *
     * @param type   of the event.
     * @param target creator or a target of the event.
     * @param data   to be passed with the event.
     */
    public SimpleEvent(final String type, final T target, final Map<String, Object> data) {
        this.type = type;
        this.target = target;
        this.properties = data;
    }


    /**
     * return the value of the data that is within this event.
     *
     * @param name of type Object
     *
     * @return Object
     */
    @Override
    public Object getProperty(final String name) {
        return this.properties.get(name);
    }

    public <V> V getPropertyAsType(String key) {
        return (V) getProperty(key);
    }

    /**
     * adds or updates a property within the event.
     *
     * @param name of type Object
     *
     * @return Object
     */
    public void setProperty(final String name, final Object value) {
        if (properties == Collections.EMPTY_MAP) {
            properties = new ConcurrentHashMap<String, Object>();
        }
        properties.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getType() {
        return this.type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T getTarget() {
        return this.target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Map<String, Object> getProperties() {
        return this.properties;
    }

    @Override
    public Integer getPriority() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SimpleEvent{" +
                "type='" + type + '\'' +
                ", target=" + target +
                ", properties=" + properties +
                '}';
    }
}
