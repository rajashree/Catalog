/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.collections;

import com.sourcen.core.util.FileMonitor;
import com.sourcen.core.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: bhaskara $
 * @version $Revision: 2919 $, $Date:: 2012-06-05 11:36:49#$
 */
public class FileBackedPropertiesProvider extends MapBackedPropertiesProvider {

    private static final Logger log = LoggerFactory.getLogger(FileBackedPropertiesProvider.class);

    protected File file;
    protected Properties properties;
    protected Boolean checkForModification = false;
    protected Long modifiedInterval = 1000L;

    public FileBackedPropertiesProvider() {
    }

    public FileBackedPropertiesProvider(InputStream inputStream) {
        this.initialize(inputStream);
    }

    public FileBackedPropertiesProvider(String filename) {
        this.initialize(new File(filename), false, 1000L);
    }

    public FileBackedPropertiesProvider(File file) {
        this.initialize(file, false, 1000L);
    }

    public FileBackedPropertiesProvider(String filename, Boolean checkForModification, Long modifiedInterval) {
        this(new File(filename), checkForModification, modifiedInterval);
    }

    public FileBackedPropertiesProvider(File file, Boolean checkForModification, Long modifiedInterval) {
        this.initialize(file, checkForModification, modifiedInterval);
    }

    protected void initialize(InputStream stream) {
        try {
            reloadProperties(stream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    protected void initialize(File file, Boolean modifiedTimeCheck, Long modifiedInterval) {
        this.checkForModification = modifiedTimeCheck;
        this.modifiedInterval = modifiedInterval;
        this.file = file;
        try {
            reloadProperties(new FileInputStream(file));
            if (checkForModification) {
                FileMonitor.moniter(file, modifiedInterval, new FileMonitorRunnable());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void refresh() {
        try {
            reloadProperties(new FileInputStream(this.file));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Object getObjectProperty(String key) {
        return super.getObjectProperty(key);
    }


    protected synchronized void reloadProperties(InputStream inputStream) throws IOException {
        try {
            // so that if something errors out we still have the old properties.
            Properties properties = new Properties();
            properties.load(inputStream);
            synchronized (map) {
                map.clear();
                map.putAll(properties);
            }
            this.properties = properties;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    protected void saveProperties() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Saving properties to file :=" + file);
            }
            OutputStream outputStream = new FileOutputStream(file);
            // save the map into the properties file.
            this.properties.store(outputStream, "auto updated by Sourcen Framework");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void setProperty(String key, String value) {
        super.setProperty(key, value);
        this.properties.put(key, value);
        saveProperties();
    }

    private final class FileMonitorRunnable implements Runnable {
        @Override
        public void run() {
            try {
                reloadProperties(new FileInputStream(file));
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            }
        }
    }

    public static FileBackedPropertiesProvider getProvider(String filePath) {
        return getProvider(filePath, false, 1000L);
    }

    public static FileBackedPropertiesProvider getProvider(String filePath, Boolean modifiedTimeCheck, Long modifiedInterval) {
        //filePath = FileUtils.getUserDirectory().getPath() + filePath;
        URL fileUrl = FileUtils.loadResource(filePath);
        File file;
        if (fileUrl == null) {
            file = new File(filePath);
        } else {
            file = new File(fileUrl.getFile());
        }
        if (file.exists()) {
            if (file.canRead()) {
                return new FileBackedPropertiesProvider(file, modifiedTimeCheck, modifiedInterval);
            } else {
                throw new RuntimeException(file.getAbsolutePath() + " was not accessible.");
            }
        } else {
            try {

                if((fileUrl == null || ResourceUtils.isJarURL(fileUrl))){
                    // load from Stream first to see if its inside JAR.
                    try{
                        InputStream stream = FileUtils.loadStream(filePath);
                        return new FileBackedPropertiesProvider(stream);
                    }catch (Exception e){
                        // ignore, we try and create the file elsewhere.
                    }
                }
                if (file.createNewFile()) {
                    return new FileBackedPropertiesProvider(file, modifiedTimeCheck, modifiedInterval);
                } else {
                    throw new RuntimeException(file.getAbsolutePath() + " was not created.");
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create propertyProvider :=" + filePath, e);
            }
        }
    }
}
