/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util;

import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public final class FileMonitor {

    private static final Timer timer = new Timer("file-moniter-timer", true);
    private static final Map<File, FileMonitorTimerTask> monitorCache = new ConcurrentHashMap<File, FileMonitorTimerTask>();

    public static void moniter(File file, Long delay, Runnable runnable) {
        FileMonitorTimerTask task = new FileMonitorTimerTask(file, runnable);
        monitorCache.put(file, task);
        timer.schedule(task, delay);
    }

    public static void moniter(File file, Runnable runnable) {
        FileMonitorTimerTask task = new FileMonitorTimerTask(file, runnable);
        monitorCache.put(file, task);
        timer.schedule(task, 1000);
    }

    private static final class FileMonitorTimerTask extends TimerTask {

        private final File file;
        private final Runnable runnable;
        private long lastModified;

        public FileMonitorTimerTask(File file, Runnable runnable) {
            this.file = file;
            this.lastModified = file.lastModified();
            this.runnable = runnable;
        }

        @Override
        public void run() {
            if (file.lastModified() > this.lastModified) {
                this.lastModified = file.lastModified();
                if (runnable instanceof FileMoniterRunnable) {
                    ((FileMoniterRunnable) runnable).run(file, lastModified);
                } else {
                    runnable.run();
                }
            }
        }
    }


    public static abstract class SimpleFileMoniterRunnable implements FileMoniterRunnable {
        @Override
        public void run(File file, long lastModified) {
            this.run();
        }

        @Override
        public void run() {
        }
    }


}
