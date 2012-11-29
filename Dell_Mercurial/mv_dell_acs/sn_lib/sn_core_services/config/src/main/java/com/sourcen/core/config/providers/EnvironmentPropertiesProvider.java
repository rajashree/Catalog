/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.config.providers;

import com.sourcen.core.services.Refreshable;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.collections.FileBackedPropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: bhaskara $
 * @version $Revision: 2890 $, $Date:: 2012-06-04 14:00:26#$
 */
public class EnvironmentPropertiesProvider extends FileBackedPropertiesProvider implements Refreshable, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(EnvironmentPropertiesProvider.class);

    private static EnvironmentPropertiesProvider instance;

    public static EnvironmentPropertiesProvider getInstance() {
        if (instance == null) {
            instance = new EnvironmentPropertiesProvider();
        }
        return instance;
    }

    public EnvironmentPropertiesProvider() {
        try {
            afterPropertiesSet();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String environmentName = "sourcen-development";
        try {
            boolean loadAsStream = true;

            environmentName = SystemPropertiesProvider.getInstance().getProperty(environmentNameKey);
            if (environmentName == null) {
                environmentName = ApplicationPropertiesProvider.getInstance().getProperty(environmentNameKey, "local");
            }

            log.info("loading environmentConfig := " + environmentName);

            URL url = FileUtils.loadResource("/profiles/" + environmentName + ".properties");
            try {
                if (url != null) {
                    File file = new File(url.toURI());
                    log.debug(" evnironmentConfig path := " + file.getAbsolutePath());
                    this.initialize(file, true, 1000L);
                    loadAsStream = false;
                }
            } catch (Exception e) {
                // ignore and load as Stream.
            }

            if (loadAsStream) {
                log.info("Loading enironmentConfig from stream := /profiles/" + environmentName + ".properties");
                InputStream stream = FileUtils.loadStream("profiles/" + environmentName + ".properties");
                this.initialize(stream);
            }

        } catch (Exception e) {
            log.error("loading environmentConfig failed, file:=  /profiles/" + environmentName + ".properties"
                    + ", using empty environment properties.");
        }
    }


    private String environmentNameKey = "app.profile";

    public void setEnvironmentNameKey(final String environmentNameKey) {
        this.environmentNameKey = environmentNameKey;
    }
}
