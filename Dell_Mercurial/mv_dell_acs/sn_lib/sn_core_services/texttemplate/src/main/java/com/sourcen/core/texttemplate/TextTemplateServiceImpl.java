/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.texttemplate;

import com.sourcen.core.event.EventDispatcher;
import com.sourcen.core.services.ServiceProviderRegistrar;
import com.sourcen.core.texttemplate.providers.TextTemplateProvider;
import com.sourcen.core.util.Assert;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TextTemplateServiceImpl implements TextTemplateService {

    protected Map<Integer, ServiceProviderRegistrar<TextTemplateProvider>> registrarMap =
            new ConcurrentHashMap<Integer, ServiceProviderRegistrar<TextTemplateProvider>>();

    private Map<Integer, TextTemplateProvider> currentTemplateProviders = new ConcurrentHashMap<Integer, TextTemplateProvider>();

    private static TextTemplateServiceImpl instance;

    public static TextTemplateServiceImpl getInstance() {
        if (instance == null) {
            instance = new TextTemplateServiceImpl();
        }
        return instance;
    }

    private TextTemplateServiceImpl() {
    }


    //
    // service provider
    //

    @Override
    public void addProvider(Integer priority, TextTemplateProvider provider) {

        getRegistrar(provider.getProviderType()).addProvider(priority, provider);
        rebuildProvider(provider.getProviderType());

    }

    @Override
    public void removeProvider(TextTemplateProvider provider) {
        Assert.notNull(provider.getProviderType(), "TemplateProvider type cannot be null given:=" + provider);
        getRegistrar(provider.getProviderType()).removeProvider(provider);
        rebuildProvider(provider.getProviderType());
    }

    @Override
    public void setProviders(Collection<TextTemplateProvider> providers) {
        for (TextTemplateProvider provider : providers) {
            addProvider(-1, provider);// add to the end of the list.
        }
    }

    @Override
    public Collection<TextTemplateProvider> getProviders() {
        Collection<TextTemplateProvider> providers = new LinkedList<TextTemplateProvider>();
        for (ServiceProviderRegistrar<TextTemplateProvider> registrar : registrarMap.values()) {
            providers.addAll(registrar.getProviders());
        }
        return providers;
    }


    protected void rebuildProvider(Integer type) {
        Collection<TextTemplateProvider> providers = getRegistrar(type).getProviders();

        //
        if (providers.isEmpty()) {
            return;
        }
        Iterator<TextTemplateProvider> iter = providers.iterator();
        TextTemplateProvider rootProvider = iter.next();
        TextTemplateProvider currentChild = rootProvider;

        while (iter.hasNext()) {
            TextTemplateProvider item = iter.next();
            currentChild.setParent(item);
            currentChild = item;
        }
        currentChild.setParent(null); // because we dont wait it to go into a cyclic loop.
        currentTemplateProviders.put(type, rootProvider);
        EventDispatcher.dispatch(new TextTemplateEvent(TextTemplateEvent.TemplateEventTypes.PROVIDER_REBUILT, rootProvider));
    }

    protected ServiceProviderRegistrar<TextTemplateProvider> getRegistrar(Integer textTemplateType) {
        ServiceProviderRegistrar<TextTemplateProvider> registrar = registrarMap.get(textTemplateType);
        if (registrar == null) {
            registrar = new ServiceProviderRegistrar<TextTemplateProvider>();
        }
        registrarMap.put(textTemplateType, registrar);
        return registrar;
    }

    //
    // IMPL
    //

    public <T extends TextTemplateProvider> T getTemplateProvider(Integer providerType) {
        if (!currentTemplateProviders.containsKey(providerType)) {
            throw new TextTemplateException("Unable to find a template provider for providerType:=" + providerType);
        }
        return (T) currentTemplateProviders.get(providerType);
    }

    @Override
    public void setProvider(TextTemplateProvider provider) {
        throw new UnsupportedOperationException("You cannot set a specific provider." +
                "Please use TemplateServiceConfigurer add providers to the service");
    }
    //
    // service methods
    //

    @Override
    public String getId() {
        return ServiceIdGenerator.get(TextTemplateServiceImpl.class);
    }

    @Override
    public void initialize() {
        for (ServiceProviderRegistrar<TextTemplateProvider> registrar : registrarMap.values()) {
            registrar.initialize();
        }
    }

    @Override
    public void refresh() {

        for (ServiceProviderRegistrar<TextTemplateProvider> registrar : registrarMap.values()) {
            registrar.refresh();
        }
    }

    @Override
    public void destroy() {
        for (ServiceProviderRegistrar<TextTemplateProvider> registrar : registrarMap.values()) {
            registrar.destroy();
        }
    }

}
