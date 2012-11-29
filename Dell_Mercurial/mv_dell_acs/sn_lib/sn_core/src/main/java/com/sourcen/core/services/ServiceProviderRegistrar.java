/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class ServiceProviderRegistrar<T> implements ServiceProvider<T> {

    private final List<T> providers = new LinkedList<T>();
    private T provider = null;
    private static final Logger log = LoggerFactory.getLogger(ServiceProviderRegistrar.class);

    @Override
    public void addProvider(Integer priority, T provider) {
        if (priority > providers.size() || priority < 0) {
            this.providers.add(providers.size(), provider);
        } else {
            this.providers.add(priority, provider);
        }
    }

    @Override
    public void setProviders(Collection<T> list) {
        log.info("Setting a new list of providers in the service registrar :" + list);
        synchronized (this.providers) {
            // destroy existing providers
            for (T provider : this.providers) {
                if (Destroyable.class.isAssignableFrom(provider.getClass())) {
                    log.debug("destroying provider :=" + provider);
                    ((Destroyable) provider).destroy();
                }
            }
            this.providers.clear();
            this.providers.addAll(list);
            log.info("initializing providers :=" + providers);
            Lifecycle.initialize(providers);
        }
    }


    public void executeForEachProvider(Action<T> action) {
        for (T provider : getProviders()) {
            action.execute(provider);
        }
    }

    @Override
    public void removeProvider(T provider) {
        providers.remove(provider);
    }


    public T getProvider() {
        return provider;
    }

    public void setProvider(T provider) {
        this.provider = provider;
    }

    @Override
    public String getId() {
        return ServiceIdGenerator.get(ServiceProviderRegistrar.class);
    }

    @Override
    public void initialize() {
        Lifecycle.initialize(providers);
    }

    @Override
    public void refresh() {
        Lifecycle.refresh(providers);
    }

    @Override
    public void destroy() {
        Lifecycle.destroy(providers);
    }

    @Override
    public Collection<T> getProviders() {
        return Collections.unmodifiableCollection(providers);
    }


}
