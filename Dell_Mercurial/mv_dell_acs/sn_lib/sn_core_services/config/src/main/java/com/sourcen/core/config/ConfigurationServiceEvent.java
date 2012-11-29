package com.sourcen.core.config;


import com.sourcen.core.event.SimpleEvent;

public class ConfigurationServiceEvent extends SimpleEvent<ConfigurationService> {

    private static final long serialVersionUID = -1414488161118826796L;

    public static final String PROPERTIES_LOADED = "PROPERTIES_LOADED";

    public static final String PROPERTY_UPDATED = "PROPERTY_UPDATED";

    public static final String PROPERTY_DELETED = "PROPERTY_DELETED";

    public ConfigurationServiceEvent(final String eventType) {
        super(eventType);
    }

    private String propertyName;

    private Object propertyValue;

    public String getPropertyName() {
        return this.propertyName;
    }

    public Boolean isProperty(Class<?> clazz, String key) {
        return isProperty(clazz.getCanonicalName() + "." + key);
    }

    public Boolean isProperty(String key) {
        return this.propertyName.equals(key);
    }


    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getPropertyValue() {
        return this.propertyValue;
    }

    public void setPropertyValue(final Object propertyValue) {
        this.propertyValue = propertyValue;
    }

    /**
     * just some event interfaces for ease of use.
     */
    public static interface PropertiesLoaded {
        void onPropertiesLoaded(ConfigurationServiceEvent event);
    }

    public static interface PropertyUpdated {
        void onPropertyUpdated(ConfigurationServiceEvent event);
    }

    public static interface PropertyDeleted {
        void onPropertyDeleted(ConfigurationServiceEvent event);
    }
}
