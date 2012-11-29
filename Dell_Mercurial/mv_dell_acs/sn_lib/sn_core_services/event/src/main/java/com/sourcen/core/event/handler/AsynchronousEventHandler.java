/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.handler;

import com.sourcen.core.event.Event;
import com.sourcen.core.event.EventCallback;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class AsynchronousEventHandler extends EventHandlerImpl {

    public AsynchronousEventHandler(Event event) {
        this(new AsynchronousEventHandlerCallable(event, null));
    }

    public AsynchronousEventHandler(Event event, EventCallback callback) {
        this(new AsynchronousEventHandlerCallable(event, callback));
    }

    protected AsynchronousEventHandler(Callable<Collection<Future<Object>>> callable) {
        super(callable);
    }

    /**
     * just a wrapper for EventHandlerCallable.
     */
    protected static class AsynchronousEventHandlerCallable extends EventHandlerCallable {

        private EventCallback onCompleteCallback;

        protected AsynchronousEventHandlerCallable(Event event, EventCallback callback) {
            super(event);
            this.onCompleteCallback = callback;
        }

        @Override
        public Collection<Future<Object>> call() throws Exception {
            Collection<Future<Object>> futureTasks = super.call();
            for (Future<Object> task : futureTasks) {
                task.get();
            }
            // once all tasks are executed, please execute the callback.
            if (onCompleteCallback != null) {
                onCompleteCallback.onComplete(futureTasks);
            }
            return futureTasks;
        }
    }

}
