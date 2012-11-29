/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.config.providers;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.persistence.domain.SystemProperty;
import com.sourcen.core.persistence.repository.SystemPropertyRepository;
import com.sourcen.core.services.Refreshable;
import com.sourcen.core.util.collections.MapBackedPropertiesProvider;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO - write this to be clusterSafe.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2944 $, $Date:: 2012-06-06 08:59:52#$
 */
public class DbPropertiesProvider extends MapBackedPropertiesProvider implements Refreshable {

    private static DbPropertiesProvider instance;
    private SystemPropertyRepository repository;

    public static DbPropertiesProvider getInstance() {
        if (instance == null) {
            instance = new DbPropertiesProvider();
        }
        return instance;
    }

    public DbPropertiesProvider() {
        super();
    }

    @Override
    public Boolean supportsPersistence() {
        return true;
    }

    public void setRepository(SystemPropertyRepository repository) {
        this.repository = repository;
        refresh();
    }

    public SystemPropertyRepository getRepository() {
        return repository;
    }

    @Override
    @Transactional(readOnly = true)
    public void refresh() {
        Collection<SystemProperty> result = repository.getAll();
        Map<String, String> properties = new HashMap<String, String>();

        for (SystemProperty property : result) {
            properties.put(property.getId(), property.getValue());
        }

        synchronized (map) {
            map.clear();
            map.putAll(properties);
        }
    }

    @Override
    @Transactional
    public void setProperty(String key, String value) {
        super.setProperty(key, value);
        // TODO - distribute the event to cluster caches.
        repository.setProperty(key, value);
    }

//    public void initialize() {
//        getConfigurationService().addProvider(1, this);
//    }

    private ConfigurationService _configurationService;

    public ConfigurationService getConfigurationService() {
        if (_configurationService == null) {
            if (App.isInitialized()) {
                _configurationService = App.getService(ConfigurationService.class);
            } else {
                return ConfigurationServiceImpl.getInstance();
            }
        }
        return _configurationService;
    }

    public void setConfigurationService(ConfigurationService _configurationService) {
        this._configurationService = _configurationService;
    }


}
