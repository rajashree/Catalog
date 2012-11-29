/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.services;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface ServiceProvider<T> extends Service {

    void addProvider(Integer priority, T provider);

    void removeProvider(T provider);

    void setProvider(T provider);

    void setProviders(Collection<T> providers);

    Collection<T> getProviders();

    public static interface Action<T> {
        void execute(T bean);
    }
}
