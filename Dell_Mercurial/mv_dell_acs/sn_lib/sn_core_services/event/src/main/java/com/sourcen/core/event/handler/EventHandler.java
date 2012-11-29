/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.handler;

import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface EventHandler extends RunnableFuture<Collection<Future<Object>>> {

}
