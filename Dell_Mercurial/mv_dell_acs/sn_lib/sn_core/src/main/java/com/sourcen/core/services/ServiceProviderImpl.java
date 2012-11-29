/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public abstract class ServiceProviderImpl<T> implements ServiceProvider<T> {

    protected Logger log = LoggerFactory.getLogger(getClass());
    protected ServiceProviderRegistrar<T> registrar = new ServiceProviderRegistrar<T>();
    protected T currentProvider;

    @Override
    public void addProvider(Integer priority, T provider) {
        registrar.addProvider(priority, provider);
    }

    @Override
    public void removeProvider(T provider) {
        registrar.removeProvider(provider);
    }

    public void setProvider(T provider) {
        this.currentProvider = provider;
    }

    public T getCurrentProvider() {
        return currentProvider;
    }

    public void setCurrentProvider(T currentProvider) {
        this.currentProvider = currentProvider;
    }

    @Override
    public void setProviders(Collection<T> providers) {
        registrar.setProviders(providers);
    }

    @Override
    public Collection<T> getProviders() {
        return registrar.getProviders();
    }

    public void executeForEachProvider(Action<T> action) {
        for (T provider : getProviders()) {
            action.execute(provider);
        }
    }

    @Override
    public String getId() {
        return Service.ServiceIdGenerator.get(getClass());
    }

    @Override
    public void initialize() {
        registrar.initialize();
    }

    @Override
    public void refresh() {
        registrar.refresh();
    }

    @Override
    public void destroy() {
        registrar.destroy();
    }


}
