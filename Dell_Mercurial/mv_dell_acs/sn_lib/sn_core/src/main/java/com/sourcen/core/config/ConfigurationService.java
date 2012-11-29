/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.config;

import com.sourcen.core.services.DefaultImplementation;
import com.sourcen.core.services.ServiceProvider;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.collections.PropertiesProvider;

import java.io.IOException;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3091 $, $Date:: 2012-06-11 13:19:19#$
 */
@DefaultImplementation(className = "com.sourcen.core.config.ConfigurationServiceImpl")
public interface ConfigurationService extends PropertiesProvider, ServiceProvider<PropertiesProvider> {

    void addProvider(Integer priority, PropertiesProvider provider);

    String getCharacterEncoding();

    Boolean isDevMode();

    FileSystem getFileSystem() throws IOException;

    String getApplicationUrl();
    
    public static final class Constants {
        public static final String CHARACTER_ENCODING = "app.characterEncoding";
        public static final String DEV_MODE = "app.devMode";
    }
}
