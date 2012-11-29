/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.event;

import java.io.Serializable;
import java.util.Map;

/**
 * An event is bean that provides us meta-data about an event such as type, creator or target, and data associated with
 * the event. Please ensure that the the implementation must be thread safe as its passed across a bunch of future tasks.
 *
 * @param <T> is the type of the target object that this event represents.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2730 $, $Date:: 2012-05-29 12:07:23#$
 * @since 1.01
 */
public interface Event<T> extends Serializable {

    /**
     * Get the type of the event.
     *
     * @return the type of the event which will be used by the EventDispatcherService to dispatch events to all the listeners.
     */
    String getType();

    /**
     * Method getTarget returns the target of this Event object.
     *
     * @return the target (type T) of this Event object.
     */
    T getTarget();

    /**
     * Method getProperties returns the properties of this Event object.
     *
     * @return the data (type Object) of this Event object.
     */
    Map<String, Object> getProperties();

    Object getProperty(String key);

    /**
     * should default to medium.
     *
     * @return 0 by default.
     */
    Integer getPriority();

    public static final class Priority {
        public static final Integer LOW = Integer.MIN_VALUE;
        public static final Integer MEDIUM = 0;
        public static final Integer HIGH = 1000;
        public static final Integer IMMEDIATE = Integer.MAX_VALUE;
    }
}
