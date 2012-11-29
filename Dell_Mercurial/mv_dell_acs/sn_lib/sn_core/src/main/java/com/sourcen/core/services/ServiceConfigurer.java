/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.services;


import java.util.Map;

/**
 * generic Service configurer.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class ServiceConfigurer<S extends ServiceProvider<T>, T> {

    private S serviceProvider;

    public S getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(S serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    private Map<Integer, T> providers;

    public void setProviders(Map<Integer, T> providers) {
        for (Map.Entry<Integer, T> entry : providers.entrySet()) {
            serviceProvider.addProvider(entry.getKey(), entry.getValue());
        }
    }

    public Map<Integer, T> getProviders() {
        return providers;
    }


}
