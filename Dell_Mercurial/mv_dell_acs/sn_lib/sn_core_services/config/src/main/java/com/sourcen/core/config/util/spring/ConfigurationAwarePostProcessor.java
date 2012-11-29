/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */
package com.sourcen.core.config.util.spring;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.config.ConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedMap;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ConfigurationAwarePostProcessor applies configurationService properties over spring beans. Any bean
 * implementing ConfiguratorPropertiesAware will be considered for wrapping.
 * <p/>
 * This will allow us to load all the properties over a bean at runtime using VM parameters, Database paramters or a
 * config file witouth tampering with the Spring context files. It also has the ability to load properties marked with
 * {@link ConfiguratorProperty} if the bean has fields marked with @ConfiguratorProperty and the bean must have the
 * {@link ConfiguratorPropertiesAware} annotation.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2756 $, $Date:: 2012-05-29 20:43:55#$
 * @since 1.01
 */
public final class ConfigurationAwarePostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    //
    // =================================================================================================================
    // == Class Fields ==
    // =================================================================================================================
    //

    private static final Logger log = LoggerFactory.getLogger(ConfigurationAwarePostProcessor.class);

    /**
     * static index used in the METADATA_CACHE[Object[0,1,2]], check during bean construction.
     */
    private static final int BEAN_IS_CONFIGURABLE = 0;

    /**
     * static index used in the METADATA_CACHE[Object[0,1,2]], check during bean construction.
     */
    private static final int BEAN_IS_CONFIG_PROPERTIES_AWARE = 1;

    /**
     * static index used in the METADATA_CACHE[Object[0,1,2]], check during bean construction.
     */
    private static final int BEAN_CONFIG_PROPERTIES_AWARE_FIELDS = 2;

    /**
     * stores all the cached metadata that we have scanned for {@link ConfiguratorPropertiesAware} beans.
     */
    private static final Map<Class<?>, Object[]> METADATA_CACHE = new ConcurrentHashMap<Class<?>, Object[]>(25);


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

    //
    // =================================================================================================================
    // == Constructors ==
    // =================================================================================================================
    //

    /**
     * empty-constructor.
     */
    public ConfigurationAwarePostProcessor() {
    }

    //
    // =================================================================================================================
    // == Class Methods ==
    // =================================================================================================================
    //

    @Override
    @SuppressWarnings("unchecked")
    public PropertyValues postProcessPropertyValues(final PropertyValues pvs, final PropertyDescriptor[] pds, final Object bean, final String beanName) {
        final Class<?> clazz = bean.getClass();
        Object[] states = ConfigurationAwarePostProcessor.METADATA_CACHE.get(clazz);
        if (states == null) {
            states = readBeanMetadata(clazz, bean, beanName);
        }

        // is ConfiguratorPropertiesAware
        // isBean configurable
        if ((Boolean) states[ConfigurationAwarePostProcessor.BEAN_IS_CONFIG_PROPERTIES_AWARE]) {

            final Map<String, Object> props = new HashMap<String, Object>(25);
            props.putAll(getConfigurationService().getProperties(clazz.getCanonicalName(), true, false));
            props.putAll(getConfigurationService().getProperties("spring.beans." + beanName, true, false));

            final MutablePropertyValues mpvs;
            if (!(pvs instanceof MutablePropertyValues)) {
                mpvs = new MutablePropertyValues();
                for (final PropertyValue pv : pvs.getPropertyValues()) {
                    mpvs.addPropertyValue(pv);
                }
            } else {
                mpvs = (MutablePropertyValues) pvs;
            }

            final HashMap<String, String> fieldMap = (HashMap<String, String>) states[ConfigurationAwarePostProcessor.BEAN_CONFIG_PROPERTIES_AWARE_FIELDS];
            for (final Map.Entry<String, String> entry : fieldMap.entrySet()) {
                final String fieldName = entry.getKey();
                final String propKeyName = entry.getValue();
                if (props.containsKey(propKeyName)) {
                    mpvs.removePropertyValue(fieldName);
                    mpvs.addPropertyValue(fieldName, props.get(propKeyName));
                }
            }
        }

        // isBean configurable
        if ((Boolean) states[ConfigurationAwarePostProcessor.BEAN_IS_CONFIGURABLE]) {

            final Set<String> mergedSet = new HashSet<String>();
            final Map<String, Object> props = getConfigurationService().getProperties(clazz.getCanonicalName() + ".properties", true, false);

            props.putAll(getConfigurationService().getProperties("spring.beans." + beanName + ".properties", true, false));

            final PropertyValue pv = pvs.getPropertyValue("properties");
            if (pv != null && pv.getValue() instanceof Map) {
                final ManagedMap map = (ManagedMap) pv.getValue();
                final Set<Map.Entry> set = map.entrySet();
                for (final Map.Entry entry : set) {
                    if (entry.getKey() instanceof TypedStringValue) {
                        final TypedStringValue tsv = (TypedStringValue) entry.getKey();
                        final Object entryValue = entry.getValue();
                        final Object finalValue = props.get(tsv.getValue());
                        if (finalValue != null) {
                            mergedSet.add(tsv.getValue());
                            if (entryValue instanceof TypedStringValue) {
                                map.put(tsv, new TypedStringValue((String) finalValue));
                            } else if (entryValue instanceof RuntimeBeanReference) {
                                map.put(tsv, new RuntimeBeanReference((String) finalValue));
                            }
                        }
                    }
                }
                // finally merge the properties into the pvs
                for (final Map.Entry<String, Object> entry : props.entrySet()) {
                    if (!mergedSet.contains(entry.getKey())) {
                        map.put(new TypedStringValue(entry.getKey()), new TypedStringValue(entry.getValue().toString()));
                    }
                }
            }
        }
        return pvs;
    }

    //
    // =================================================================================================================
    // == Private Methods and Helpers ==
    // =================================================================================================================
    //

    /**
     * read the bean metadata and save it into the states cache.
     *
     * @param clazz    of type Class
     * @param bean     of type Object
     * @param beanName of type String
     *
     * @return Object[]
     */
    private Object[] readBeanMetadata(final Class<?> clazz, final Object bean, final String beanName) {
        final Object[] states = new Object[3];

        // check if bean isConfigurable
        states[ConfigurationAwarePostProcessor.BEAN_IS_CONFIGURABLE] = bean instanceof Configurable;

        // check if its configuratorPropertiesAware
        final Annotation classAnnotation = clazz.getAnnotation(ConfiguratorPropertiesAware.class);

        if (classAnnotation != null) {

            final ConfiguratorPropertiesAware configuratorAnnotation = (ConfiguratorPropertiesAware) classAnnotation;
            final Field[] fields = clazz.getDeclaredFields();
            final HashMap<String, String> fieldMap = new HashMap<String, String>(fields.length);

            if (configuratorAnnotation.value() == ConfiguratorPropertiesAware.Types.ANNOTATED_FIELDS) {
                for (final Field field : fields) {
                    final Annotation fieldAnnotation = field.getAnnotation(ConfiguratorProperty.class);

                    if (fieldAnnotation != null) {
                        final ConfiguratorProperty configFieldAnnotation = (ConfiguratorProperty) fieldAnnotation;
                        final String propertyName = configFieldAnnotation.value();
                        if (propertyName.equals("")) {
                            fieldMap.put(field.getName(), field.getName());
                        } else {
                            fieldMap.put(field.getName(), propertyName);
                        }
                    }
                }
                // orelse just add all the fields as we want to map all the properties.
            } else {
                for (final Field field : fields) {
                    fieldMap.put(field.getName(), field.getName());
                }
            }

            // finally set the feild list.
            if (fieldMap.isEmpty()) {
                ConfigurationAwarePostProcessor.log.warn("bean '" + beanName + "' is defined as ConfiguratorPropertiesAware, "
                        + "but doesn't have any fields with classAnnotation 'ConfiguratorProperty'");
            }
            states[ConfigurationAwarePostProcessor.BEAN_IS_CONFIG_PROPERTIES_AWARE] = true;
            states[ConfigurationAwarePostProcessor.BEAN_CONFIG_PROPERTIES_AWARE_FIELDS] = fieldMap;

        } else if (getConfigurationService().getBooleanProperty("spring.beans." + beanName + ".isConfigPropertiesAware", false)) {

            states[ConfigurationAwarePostProcessor.BEAN_IS_CONFIG_PROPERTIES_AWARE] = true;
            final Set<String> fieldSet = getConfigurationService().getProperties("spring.beans." + beanName, true, false).keySet();
            final Map<String, Object> fieldMap = new HashMap<String, Object>(fieldSet.size());
            for (final String key : fieldSet) {
                // skip some identifier properties.
                if (key.equalsIgnoreCase("isConfigPropertiesAware")) {
                    continue;
                }
                fieldMap.put(key, key);
            }

            states[ConfigurationAwarePostProcessor.BEAN_CONFIG_PROPERTIES_AWARE_FIELDS] = fieldMap;

        } else {
            states[ConfigurationAwarePostProcessor.BEAN_IS_CONFIG_PROPERTIES_AWARE] = false;
            states[ConfigurationAwarePostProcessor.BEAN_CONFIG_PROPERTIES_AWARE_FIELDS] = null;
        }

        return states;
    }

}
