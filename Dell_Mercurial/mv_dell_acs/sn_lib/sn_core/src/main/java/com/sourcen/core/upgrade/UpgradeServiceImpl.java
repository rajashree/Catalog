/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.upgrade;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.services.ServiceImpl;
import com.sourcen.core.spring.context.ApplicationState;
import com.sourcen.core.util.FileUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3536 $, $Date:: 2012-06-22 12:00:42#$
 */
public class UpgradeServiceImpl extends ServiceImpl
        implements UpgradeService, ApplicationListener<ContextRefreshedEvent> {

    private static final AtomicBoolean isUpgradeRunning = new AtomicBoolean(false);

    private Collection<UpgradeTaskInfo> tasksToRun = new ConcurrentLinkedQueue<UpgradeTaskInfo>();

    private static UpgradeService instance;

    public static UpgradeService getInstance() {
        if (instance == null) {
            instance = new UpgradeServiceImpl();
        }
        return instance;
    }

    private UpgradeServiceImpl() {
        configurationService = App.getService(ConfigurationService.class);
        reloadTasks();
    }

    public void reloadTasks() {
        if (isUpgradeRunning.get()) {
            logger.warn("currently executing upgrade tasks, cannot reload them now. try again later.");
            return;
        }

        try {
            if (UpgradeServiceImpl.class.getResource("/upgrade.xml") == null) {
                logger.warn("Unable to find upgrade.xml in classpath. skipping upgrade process.");
                return;
            }
            final XStream xstream = new XStream(new DomDriver());
            xstream.setMode(XStream.NO_REFERENCES);
            xstream.alias("tasks", UpgradeServiceImpl.UpgradeTaskInfoParent.class);
            xstream.alias("task", UpgradeServiceImpl.UpgradeTaskInfo.class);
            xstream.aliasAttribute(UpgradeServiceImpl.UpgradeTaskInfo.class, "group", "group");
            xstream.aliasAttribute(UpgradeServiceImpl.UpgradeTaskInfo.class, "version", "version");
            xstream.aliasAttribute(UpgradeServiceImpl.UpgradeTaskInfo.class, "beanName", "beanName");
            xstream.aliasAttribute(UpgradeServiceImpl.UpgradeTaskInfo.class, "className", "className");

            xstream.alias("task", UpgradeServiceImpl.UpgradeTaskInfo.class);
            xstream.addImplicitCollection(UpgradeServiceImpl.UpgradeTaskInfoParent.class, "tasks");
            UpgradeServiceImpl.UpgradeTaskInfoParent upgradeTaskInfoParent = (UpgradeServiceImpl.UpgradeTaskInfoParent) xstream
                    .fromXML(FileUtils.loadStream("/upgrade.xml"));
            Collection<UpgradeTaskInfo> upgradeTaskInfoCollection = upgradeTaskInfoParent.getTasks();

            tasksToRun.clear();
            for (UpgradeTaskInfo info : upgradeTaskInfoCollection) {
                if (getCurrentVersion(info.getGroup()) < info.getVersion()) {
                    // task was not executed, just add it for execution.
                    tasksToRun.add(info);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Float getCurrentVersion() {

        return getCurrentVersion("app");
    }

    @Override
    public Float getCurrentVersion(String upgradeGroup) {
        return configurationService.getFloatProperty("upgrades." + upgradeGroup + ".version", 1.0F);
    }

    protected void setCurrentVersion(String upgradeGroup, Float version) {
        configurationService.setProperty("upgrades." + upgradeGroup + ".version", version.toString());
    }

    @Override
    public Boolean isUpgradeRequired() {
        return !tasksToRun.isEmpty();
    }

    @Override
    public Collection<UpgradeTask> runUpgrade() {
        if (isUpgradeRunning.compareAndSet(false, true)) {
            Collection<UpgradeTask> tasks = new LinkedList<UpgradeTask>();
            Collection<UpgradeTaskInfo> errorTasks = new LinkedList<UpgradeTaskInfo>();

            for (UpgradeTaskInfo taskInfo : tasksToRun) {
                try {
                    UpgradeTask task;
                    if (taskInfo.getBeanName() != null) {
                        task = (UpgradeTask) applicationContext.getBean(taskInfo.getBeanName());
                    } else {
                        task = (UpgradeTask) Class.forName(taskInfo.getClassName()).newInstance();
                        task.setApplicationContext(applicationContext);
                    }
                    tasks.add(task);
                    try {
                        logger.info("starting task :=" + taskInfo.toString());
                        task.run();
                        logger.info("completed task :=" + taskInfo.toString());
                        // successful? great.
                        try {
                        	setCurrentVersion(taskInfo.getGroup(), taskInfo.getVersion());
                        } catch (Exception e) {
                        	StringBuilder sb = new StringBuilder();
                        	sb.append("Unable to save SQL script version: (");
                        	sb.append(taskInfo);
                        	sb.append("), ");
                        	sb.append(e.getMessage());
                            logger.warn(sb.toString());
                        }
                    } catch (Exception e) {
                        errorTasks.add(taskInfo);
                        logger.error("Unable to execute task :=" + taskInfo, e);
                    }
                } catch (Exception e) {
                    errorTasks.add(taskInfo);
                    logger.error("Unable to initialize task :=" + taskInfo, e);
                }
            }
            isUpgradeRunning.set(false);
            if(contextConfigurer != null){
                if (errorTasks.size() == 0) {
                    contextConfigurer.setApplicationState(ApplicationState.COMPLETE);
                    reloadTasks();
                    if (isUpgradeRequired()) {
                        contextConfigurer.setApplicationState(ApplicationState.UPGRADE_INCOMPLETE);
                    }
                } else {
                    contextConfigurer.setApplicationState(ApplicationState.UPGRADE_ERROR);
                }
            } else{
                // did we create the context manually?
                // ignore then.

            }

            return tasks;
        }
        throw new RuntimeException("Upgrade is currently executing.");
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (isUpgradeRequired()) {
            logger.info("Upgrade is required.");
            if (configurationService.getBooleanProperty("upgradeManager.autoExecuteUpgrade", true)) {
                logger.info("auto executing upgrade");
                runUpgrade();
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        reloadTasks();
    }

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ApplicationContext applicationContext;

    private App.ContextConfigurer contextConfigurer;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public void setContextConfigurer(final App.ContextConfigurer contextConfigurer) {
        this.contextConfigurer = contextConfigurer;
    }

    //
    // helper classes to read upgrade.xml file
    //

    public static final class UpgradeTaskInfoParent {

        private ArrayList<UpgradeTaskInfo> tasks = new ArrayList<UpgradeTaskInfo>();

        private UpgradeTaskInfoParent() {
        }

        public Collection<UpgradeTaskInfo> getTasks() {
            return tasks;
        }

        public void setTasks(ArrayList<UpgradeTaskInfo> tasks) {
            this.tasks = tasks;
        }
    }

    public static final class UpgradeTaskInfo {

        private String group;

        private Float version;

        private String className;

        private String beanName;

        private UpgradeTaskInfo() {
        }

        public String getBeanName() {
            return beanName;
        }

        public void setBeanName(String beanName) {
            this.beanName = beanName;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public Float getVersion() {
            return version;
        }

        public void setVersion(Float version) {
            this.version = version;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        @Override
        public String toString() {
            return "UpgradeTaskInfo{" +
                    "group='" + group + '\'' +
                    ", version=" + version +
                    ", className='" + className + '\'' +
                    ", beanName='" + beanName + '\'' +
                    '}';
        }
    }
}
