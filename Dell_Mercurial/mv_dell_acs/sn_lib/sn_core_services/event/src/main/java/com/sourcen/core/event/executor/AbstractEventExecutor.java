/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.executor;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
abstract class AbstractEventExecutor implements EventExecutor {

    protected Object result = null;
    protected Integer priority = 0;

    private final AtomicBoolean isExecuted = new AtomicBoolean(false);

    @Override
    public final Object call() {
        synchronized (isExecuted) {
            if (!isExecuted.get()) {
                this.run();
                isExecuted.set(true);
            }
        }
        return result;
    }
}
