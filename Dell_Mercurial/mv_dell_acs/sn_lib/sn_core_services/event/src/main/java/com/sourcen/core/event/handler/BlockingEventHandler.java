/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.handler;

import com.sourcen.core.event.Event;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class BlockingEventHandler extends EventHandlerImpl {

    public BlockingEventHandler(Event event) {
        super(new BlockingEventHandlerCallable(event));
    }

    protected static class BlockingEventHandlerCallable extends EventHandlerCallable {

        private BlockingEventHandlerCallable(Event event) {
            super(event);
        }

        @Override
        public Collection<Future<Object>> call() throws Exception {
            Collection<Future<Object>> futures = super.call();
            for (Future<Object> future : futures) {
                //EventExecutorFutureTask task = (EventExecutorFutureTask) future;
                future.get();
            }

            return futures;
        }
    }

}
