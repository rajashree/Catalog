/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.logger;

import com.sourcen.core.util.logger.Log4JRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public final class ServiceFileAppender extends Log4JRollingFileAppender {

    private static final Map<String, FileAppender> loggers = new ConcurrentHashMap<String, FileAppender>();

    /**
     * class field
     */
    private String packageName;

    /**
     * default constructor.
     */
    public ServiceFileAppender() {
    }

    @Override
    public void append(final LoggingEvent event) {
        if (loggers.containsKey(event.getLoggerName())) {
            loggers.get(event.getLoggerName()).append(event);
        } else {
            synchronized (loggers) {
                try {
                    String fileName = getFile().replace("_all", event.getLoggerName().replace(".", "_"));
                    FileAppender appender = new Log4JRollingFileAppender(getLayout(), fileName, getAppend());

                    // set all properties as this
                    appender.setAppend(this.getAppend());
                    appender.setBufferedIO(this.getBufferedIO());
                    appender.setThreshold(this.getThreshold());
                    appender.setBufferSize(this.getBufferSize());
                    appender.setEncoding(this.getEncoding());
                    appender.setImmediateFlush(true);
                    appender.setErrorHandler(this.getErrorHandler());

                    loggers.put(event.getLoggerName(), appender);
                    appender.append(event);
                } catch (IOException e) {
                    LogLog.warn(e.getMessage(), e);
                }
            }
        }
        super.append(event);
    }


    @Override
    protected void closeFile() {
        super.closeFile();
        for (FileAppender appender : loggers.values()) {
            appender.close();
        }
    }

    /**
     * getters and setters
     */

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }
}
