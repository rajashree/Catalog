/**
 * 
 */
package com.dell.acs.recover;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.FileUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Shawn_Fisk
 * 
 */
public class RecoverServiceImpl implements RecoverService,
		ApplicationListener<ContextRefreshedEvent> {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final AtomicBoolean isRecoverRunning = new AtomicBoolean(
			false);

	private Collection<RecoverTaskInfo> tasksToRun = new ConcurrentLinkedQueue<RecoverTaskInfo>();

	private static RecoverService instance;

	public static RecoverService getInstance() {
		if (instance == null) {
			instance = new RecoverServiceImpl();
		}
		return instance;
	}

	@SuppressWarnings("unused")
	private ConfigurationService configurationService;

	/**
	 * 
	 */
	private RecoverServiceImpl() {
		configurationService = App.getService(ConfigurationService.class);
		reloadTasks();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.recover.RecoverService#reloadTasks()
	 */
	@Override
	public void reloadTasks() {
		if (isRecoverRunning.get()) {
			logger.warn("currently executing upgrade tasks, cannot reload them now. try again later.");
			return;
		}

		try {
			if (RecoverServiceImpl.class.getResource("/recover.xml") == null) {
				logger.warn("Unable to find recpver.xml in classpath. skipping recover process.");
				return;
			}
			final XStream xstream = new XStream(new DomDriver());
			xstream.setMode(XStream.NO_REFERENCES);
			xstream.alias("tasks",
					RecoverServiceImpl.RecoverTaskInfoParent.class);
			xstream.alias("task", RecoverServiceImpl.RecoverTaskInfo.class);
			xstream.aliasAttribute(RecoverServiceImpl.RecoverTaskInfo.class,
					"beanName", "beanName");

			xstream.alias("task", RecoverServiceImpl.RecoverTaskInfo.class);
			xstream.addImplicitCollection(
					RecoverServiceImpl.RecoverTaskInfoParent.class, "tasks");
			RecoverServiceImpl.RecoverTaskInfoParent recoverTaskInfoParent = (RecoverServiceImpl.RecoverTaskInfoParent) xstream
					.fromXML(FileUtils.loadStream("/recover.xml"));
			Collection<RecoverTaskInfo> recoverTaskInfoCollection = recoverTaskInfoParent
					.getTasks();

			tasksToRun.clear();
			for (RecoverTaskInfo info : recoverTaskInfoCollection) {
				tasksToRun.add(info);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private Collection<RecoverTask> runRecover(ApplicationContext applicationContext) {
		if (isRecoverRunning.compareAndSet(false, true)) {
			Collection<RecoverTask> tasks = new LinkedList<RecoverTask>();
			Collection<RecoverTaskInfo> errorTasks = new LinkedList<RecoverTaskInfo>();

			for (RecoverTaskInfo taskInfo : tasksToRun) {
				try {
					RecoverTask task = null;
					if (taskInfo.getBeanName() != null) {
						task = (RecoverTask) applicationContext
								.getBean(taskInfo.getBeanName());
					}
					tasks.add(task);
					try {
						logger.debug("starting task :=" + taskInfo.toString());
						task.run();
						logger.debug("completed task :=" + taskInfo.toString());
					} catch (Exception e) {
						errorTasks.add(taskInfo);
						logger.error("Unable to execute task :=" + taskInfo, e);
					}
				} catch (Exception e) {
					errorTasks.add(taskInfo);
					logger.error("Unable to initialize task :=" + taskInfo, e);
				}
			}
			isRecoverRunning.set(false);
			if (errorTasks.size() == 0) {
			}

			return tasks;
		} else {
			throw new RuntimeException("Recover is currently executing.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationListener#onApplicationEvent(org
	 * .springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		runRecover(event.getApplicationContext());
	}

	public static final class RecoverTaskInfoParent {

		private ArrayList<RecoverTaskInfo> tasks = new ArrayList<RecoverTaskInfo>();

		private RecoverTaskInfoParent() {
		}

		public Collection<RecoverTaskInfo> getTasks() {
			return tasks;
		}

		public void setTasks(ArrayList<RecoverTaskInfo> tasks) {
			this.tasks = tasks;
		}
	}

	public static final class RecoverTaskInfo {

		private String beanName;

		private RecoverTaskInfo() {
		}

		public String getBeanName() {
			return beanName;
		}

		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}

		@Override
		public String toString() {
			return "RecoverTaskInfo{" + ", beanName='" + beanName + '\'' + '}';
		}
	}
}
