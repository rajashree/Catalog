/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.config.providers;

import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.services.Refreshable;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.collections.FileBackedPropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2764 $, $Date:: 2012-05-29 23:24:47#$
 */
public class FileSystemStartupPropertiesProvider extends FileBackedPropertiesProvider implements Refreshable {

    private static FileSystemStartupPropertiesProvider instance;
    private static final Logger log = LoggerFactory.getLogger(FileSystemStartupPropertiesProvider.class);

    public static FileSystemStartupPropertiesProvider getInstance() {
        if (instance == null) {
            instance = new FileSystemStartupPropertiesProvider();
        }
        return instance;
    }

    @Override
    public Boolean supportsPersistence() {
        return true;
    }

    public FileSystemStartupPropertiesProvider() {
        try {
            FileSystem fileSystem = ConfigurationServiceImpl.getInstance().getFileSystem();
            File file = fileSystem.getFile("/startup.properties", true, true);
            this.initialize(file, true, 1000L);
        } catch (Exception e) {
            log.error("Failed to load startup.properties from the filesystem:=" +
                    ConfigurationServiceImpl.getInstance().getProperty("filesystem", "/app_work_dir"), e);
        }
    }
}
