/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.config.util.spring;

import java.util.Map;

/**
 * Any classes that implement this interface will be injected with a propertyMap from the configurationService for the
 * specific class.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.01
 */
public interface Configurable {

    /**
     * set a list of custom properties on object construction. This will allow us to get custom properties from spring
     * bean definition.
     *
     * @param properties is a map of the properties for this Repository
     */
    void setProperties(final Map<String, Object> properties);
}
