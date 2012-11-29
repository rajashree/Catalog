/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.registrar;

import com.sourcen.core.event.Event;
import com.sourcen.core.event.executor.EventExecutor;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
interface EventRegistar<T> {

    void add(T entry);

    void remove(T entry);

    Collection<EventExecutor> getExecutors(Event event);

}
