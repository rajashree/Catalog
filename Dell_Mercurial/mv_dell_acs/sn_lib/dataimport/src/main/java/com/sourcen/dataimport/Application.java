/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Application is the main client class for the whole application
 * and its start and invoke the Application Framework and spring framework
 * its reponsible for starting the multiple thread for the multitasking
 * for application.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 638 $, $Date:: 2012-03-14 07:10:19#$
 * @deprecated because we are using spring with GenericDataImportService.
 */

public final class Application implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static FileSystemXmlApplicationContext context;

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    /**
     * Initializes the migration utility.
     */
    public static void main(String args[]) {
        logger.info("Initializing Spring Context.");
        context = new FileSystemXmlApplicationContext("classpath:/spring/applicationContext.xml");
        logger.info("Application Initialized");

        Application applicationInstance = (Application) context.getBean("migrationExecutor");
        // we expect 2 parameters.
        if (args.length < 2) {
            logger.info("Executing all the migration services");
            Map<String, DataImportService> beans = context.getBeansOfType(DataImportService.class);
            applicationInstance.setDataImportServices(beans.values());
        } else {
            if (args[0].equalsIgnoreCase("execute")) {
                if (args[1].equalsIgnoreCase("all")) {
                    logger.info("Executing all the migration services");
                    Map<String, DataImportService> beans = context.getBeansOfType(DataImportService.class);
                    applicationInstance.setDataImportServices(beans.values());
                } else {
                    // this means we run the indicated services.
                    String[] servicesStr = args[1].split(",");
                    logger.info("Executing specific migration services : " + args[1]);
                    for (String serviceName : servicesStr) {
                        // if serviceName starts with a cap, lowercase it so that we can find it by name.
                        serviceName = serviceName.trim().substring(0, 1).toLowerCase() + serviceName.substring(1);
                        if (!context.containsBean(serviceName)) {
                            throw new RuntimeException("Unable to find service by name:" + serviceName);
                        } else {
                            applicationInstance.getDataImportServices().add((DataImportService) context.getBean(serviceName));
                        }
                    }
                }
            } else {
                logger.warn("unknown application execution argument:--" + args[0]);
            }
        }
        applicationInstance.run();
    }

    @Override
    public void run() {

        if (System.getProperty("ignoreDependencies", "false").equals("true")) {
            independentServices.addAll(dataImportServices);
        } else {
            for (DataImportService service : dataImportServices) {
                if (service.getDependencies().size() > 0) {
                    dependentServices.add(service);
                } else {
                    independentServices.add(service);
                }
            }
        }

        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100);
        threadPoolExecutor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.SECONDS, workQueue);
        timerTask = new ThreadPoolTimerTask(this);
        timer.schedule(timerTask, 0, 1000);
    }


    private static final Map<String, String> tableNameToBeanName = new ConcurrentHashMap<String, String>();

    public static String getBeanNameByTableName(String tableName) {
        if (tableNameToBeanName.isEmpty()) {
            synchronized (tableNameToBeanName) {
                Map<String, DataImportService> beans = getContext().getBeansOfType(DataImportService.class);
                for (Map.Entry<String, DataImportService> entry : beans.entrySet()) {
                    tableNameToBeanName.put(entry.getValue().getTableDefinitionName(), entry.getKey());
                }
            }
        }
        return tableNameToBeanName.get(tableName);
    }

    private void executeServices() {
        synchronized (independentServices) {
            for (DataImportService service : independentServices) {
                logger.info("Submitting task " + service.getClass().getSimpleName());
                Future<DataImportService> future = threadPoolExecutor.submit(service, service);
                executionResults.put(service, future);
            }
            independentServices.clear();
        }
    }

    private void reloadDependencies() {
        synchronized (independentServices) {
            for (DataImportService service : dependentServices) {
                Boolean dependenciesComplete = true;
                for (String dependent : service.getDependencies()) {
                    Future<DataImportService> future = executionResults.get(getServiceByName(dependent));
                    if (future == null || !future.isDone()) {
                        dependenciesComplete = false;
                        break;
                    }
                }
                if (dependenciesComplete) {
                    independentServices.add(service);
                    dependentServices.remove(service);
                }
            }
        }
    }

    private DataImportService getServiceByName(String name) {
        for (DataImportService service : dataImportServices) {
            if (service.getBeanName().equalsIgnoreCase(name)) {
                return service;
            }
        }
        throw new IllegalArgumentException("Unable to find the service by name:=" + name);
    }


    private static class ThreadPoolTimerTask extends TimerTask {
        private Application application;

        private ThreadPoolTimerTask(Application application) {
            this.application = application;
        }

        @Override
        public void run() {
            // update independent map based on the new timer.
            application.reloadDependencies();
            application.executeServices();

            // check if all tasks are complete and shutdown the application.
            if (application.executionResults.size() == application.dataImportServices.size()) {
                boolean terminate = true;
                for (Future<DataImportService> future : application.executionResults.values()) {
                    if (!future.isDone()) {
                        terminate = false;
                        break;
                    } else {
                        try {
                            // in many cases the task claims to be complete by not.
                            // so if future returns the value then its truely complete.
                            future.get();
                        } catch (Exception e) {
                            terminate = false;
                            break;
                        }
                    }
                }
                if (terminate) {
                    if (current_terminate_delay == 0) {
                        logger.warn("Shutting down application as all service executions is complete.");
                        logger.info("Waiting for all transactions to complete within 5 seconds.");
                    }
                    if (current_terminate_delay < 5000) {
                        current_terminate_delay += 1000;
                    } else {
                        application.timer.cancel();
                        application.timerTask.cancel();
                        application.threadPoolExecutor.shutdown();
                        Application.getContext().close();
                    }
                }
            }
        }
    }


    // --- end of initializer, this is actual execution of the selected services.
    private Collection<DataImportService> dataImportServices = new LinkedList<DataImportService>();
    private Collection<DataImportService> independentServices = new CopyOnWriteArrayList<DataImportService>();
    private Collection<DataImportService> dependentServices = new CopyOnWriteArrayList<DataImportService>();
    private Map<DataImportService, Future<DataImportService>> executionResults = new ConcurrentHashMap<DataImportService, Future<DataImportService>>();
    private ThreadPoolExecutor threadPoolExecutor;
    private ThreadPoolTimerTask timerTask;
    private Timer timer = new Timer("threadpool-executor-check");
    private static Long current_terminate_delay = 0L;

    public void setDataImportServices(Collection<DataImportService> dataImportServices) {
        this.dataImportServices = dataImportServices;
    }

    public Collection<DataImportService> getDataImportServices() {
        return dataImportServices;
    }
}
