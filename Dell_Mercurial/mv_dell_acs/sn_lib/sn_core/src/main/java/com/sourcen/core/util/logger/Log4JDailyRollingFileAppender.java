/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.logger;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Layout;

import java.io.File;
import java.io.IOException;

/**
 * A simple rollingFileAppender that is capable of writing logs into custom location.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3091 $, $Date:: 2012-06-11 06:19:19#$
 * @since 1.0
 */
public class Log4JDailyRollingFileAppender extends org.apache.log4j.DailyRollingFileAppender {

    protected boolean useApplicationFileSystem = false;

    public Log4JDailyRollingFileAppender() {
    }

    public Log4JDailyRollingFileAppender(Layout layout, String filename, String datePattern) throws IOException {
        super(layout, filename, datePattern);
    }

    @Override
    public void setFile(String file) {
        if (useApplicationFileSystem) {
            try {
                ConfigurationService configurationService = App.getService(ConfigurationService.class);
                File newFile = configurationService.getFileSystem().getFile("logs/" + FilenameUtils.normalize(file), true, true);
                file = newFile.getAbsolutePath();
                if (!newFile.exists()) {
                	newFile.createNewFile();
                }
            } catch (Throwable e) {
                System.err.println("Unable to initialize fileSystem logging, using the pre-configured log location.");
            }
        }
        super.setFile(file);
    }

    public boolean getUseApplicationFileSystem() {
        return useApplicationFileSystem;
    }

    public void setUseApplicationFileSystem(boolean useApplicationFileSystem) {
        this.useApplicationFileSystem = useApplicationFileSystem;
    }
}
