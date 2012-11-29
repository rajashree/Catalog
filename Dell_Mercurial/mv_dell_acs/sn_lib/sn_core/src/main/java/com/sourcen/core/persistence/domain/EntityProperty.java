/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EntityProperty construct that can provide simple Entity Property extension.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2699 $, $Date:: 2012-05-29 10:00:40#$
 */
public interface EntityProperty extends IdentifiableEntity<Long> {

    /**
     * @return name of this property.
     */
    String getName();

    /**
     * @param name of the property.
     */
    void setName(String name);

    /**
     * @return value of this property.
     */
    String getValue();

    /**
     * @param value this property holds.
     */
    void setValue(String value);

    public static class Util {

        public static Map<String, String> getAllValues(List<? extends EntityProperty> result) {
            if (result == null || result.isEmpty()) {
                return Collections.emptyMap();
            }
            Map<String, String> values = new HashMap<String, String>(result.size());
            for (EntityProperty property : result) {
                values.put(property.getName(), property.getValue());
            }
            return values;
        }

    }

}
