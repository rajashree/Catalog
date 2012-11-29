/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class PriorityBasedPropertiesProvider extends AbstractPropertiesProvider {

    private final List<PropertiesProvider> providers = new CopyOnWriteArrayList<PropertiesProvider>();

    @Override
    public boolean hasProperty(String key) throws NullPointerException {
        for (int i = 0; i < providers.size(); i++) {
            if (providers.get(i).hasProperty(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = new HashSet<String>(10);
        for (PropertiesProvider provider : providers) {
            set.addAll(provider.keySet());
        }
        return set;
    }

    @Override
    public void setProperty(String key, String value) {
        for (PropertiesProvider provider : providers) {
            provider.setProperty(key, value);
        }
    }

    @Override
    public Object getObjectProperty(String key) {
        for (int i = 0; i < providers.size(); i++) {
            Object obj = providers.get(i).getObjectProperty(key);
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public void refresh() {
        for (PropertiesProvider provider : providers) {
            provider.refresh();
        }
    }

    //
    //
    //

    public void addProvider(Integer priority, PropertiesProvider provider) {
        synchronized (providers) {
            if (providers.size() < priority) {
                providers.add(priority, provider);
            } else {
                providers.add(providers.size(), provider);
            }
        }
    }

    public void removeProvider(PropertiesProvider provider) {
        providers.remove(provider);
    }

    public synchronized void setProviders(Collection<PropertiesProvider> providers) {
        synchronized (providers) {
            providers.clear();
            providers.addAll(providers);
        }
    }

    public Collection getProviders() {
        return Collections.unmodifiableCollection(providers);
    }
}
