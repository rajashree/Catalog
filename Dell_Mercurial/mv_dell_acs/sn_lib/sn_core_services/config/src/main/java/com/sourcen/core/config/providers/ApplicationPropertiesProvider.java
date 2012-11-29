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

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: bhaskara $
 * @version $Revision: 2890 $, $Date:: 2012-06-04 14:00:26#$
 */
public class ApplicationPropertiesProvider extends FileBackedPropertiesProvider implements Refreshable {
	// SFISK - CS-380
	static {
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
		} catch(Exception e) {
			// Only need to make sure the jtds driver is loaded for SQL Server database.
			e.printStackTrace();
		}
	}

    private static ApplicationPropertiesProvider instance;
    private static final Logger log = LoggerFactory.getLogger(ApplicationPropertiesProvider.class);

    public static ApplicationPropertiesProvider getInstance() {
        if (instance == null) {
            instance = new ApplicationPropertiesProvider();
        }
        return instance;
    }

    public ApplicationPropertiesProvider() {
        try {
            URL url = FileUtils.loadResource("/application.properties");
            boolean loadAsStream = true;
            if (url != null) {
                try {
                    File file = new File(url.toURI());
                    this.initialize(file, true, 1000L);
                    loadAsStream = false;
                } catch (Exception e) {
                    // ignore and load as stream..
                }
            }
            if (loadAsStream) {
                log.info("Loading applicationProperties from stream");
                InputStream stream = FileUtils.loadStream("application.properties");
                this.initialize(stream);
            }
        } catch (Exception e) {
            log.error("Failed to load application.properties from the classpath.", e);
        }
    }
}
