/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A very simple profiler for the app.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2757 $, $Date:: 2012-05-29 20:44:49#$
 * @since 1.0
 */
public class Profiler {

    private volatile Map<String, ProfilerEvent> events = new ConcurrentHashMap<String, ProfilerEvent>();
    private volatile Logger logger;
    private volatile boolean isProfilerEnabled = App.getService(ConfigurationService.class).getBooleanProperty("profiler.enabled", true);

    public Profiler(String profilerId) {
        logger = LoggerFactory.getLogger(getClass().getCanonicalName() + "." + profilerId);
    }

    public void markEvent(String eventId) {
        if (isProfilerEnabled) {
            if (events.containsKey(eventId)) {
                events.get(eventId).startTime = System.currentTimeMillis();
            } else {
                events.put(eventId, new ProfilerEvent(eventId, System.currentTimeMillis()));
            }
            if (logger.isInfoEnabled()) {
                logger.info(eventId + " started");
            }
        }
    }

    public long endEvent(String eventId) {
        return endEvent(eventId, null);
    }

    public long endEvent(String eventId, String msg) {
        long diff = 0;
        if (isProfilerEnabled) {
            ProfilerEvent event = events.get(eventId);
            if (event != null) {
                event.endTime = System.currentTimeMillis();
                diff = event.endTime - event.startTime;
            }
            if (logger.isInfoEnabled()) {
                if (msg == null) {
                    logger.info(eventId + " ended with a diff:=" + diff);
                } else {
                    logger.info(eventId + " ended with a diff:=" + diff + " msg:=" + msg);
                }
            }
        }
        return diff;
    }

    public long getElapsedTime(String eventId) {
        ProfilerEvent event = events.get(eventId);
        if (event != null) {
            return event.endTime - System.currentTimeMillis();
        }
        return 0;
    }


    private static class ProfilerEvent {
        private String eventId;
        private long startTime = 0;
        private long endTime = 0;
        private boolean hasEnded = false;

        private ProfilerEvent(String eventId, long startTime) {
            this.eventId = eventId;
            this.startTime = startTime;
        }
    }


}
