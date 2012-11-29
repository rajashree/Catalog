/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.config.providers;

import com.sourcen.core.services.Refreshable;
import com.sourcen.core.util.collections.MapBackedPropertiesProvider;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class SystemPropertiesProvider extends MapBackedPropertiesProvider implements Refreshable {

    private static SystemPropertiesProvider instance;

    public static SystemPropertiesProvider getInstance() {
        if (instance == null) {
            instance = new SystemPropertiesProvider();
        }
        return instance;
    }

    public SystemPropertiesProvider() {
        super(System.getProperties());
    }

    // this is if we want to change a property value and still read it back from the System.properties later.
    @Override
    public Boolean supportsPersistence() {
        return true;
    }

    @Override
    public void refresh() {
        refresh(System.getProperties());
    }
}
