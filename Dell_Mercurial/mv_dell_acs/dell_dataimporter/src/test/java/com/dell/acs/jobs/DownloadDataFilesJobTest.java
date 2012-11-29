package com.dell.acs.jobs;

import com.dell.acs.managers.DataFilesDownloadManager;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.jobs.AbstractJob;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class DownloadDataFilesJobTest {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(DownloadDataFilesJobTest.class);
	private static final String TestProviderName = "Anything";
	private static final String TestJobListener = "JobListener";

	public static class TestJobListener extends
			JobListenerSupport {
		Object _event = null;
		boolean _failed = true;
		Throwable _exception = null;

		public TestJobListener(Object event) {
			this._event = event;
		}

		@Override
		public String getName() {
			return TestProviderName;
		}

		@Override
		public void jobWasExecuted(JobExecutionContext context,
				JobExecutionException jobException) {
			this._failed = false;
			this.notifyEvent();
		}

		public boolean successful() {
			return !this._failed;
		}

		public Throwable getException() {
			return this._exception;
		}

		public void handleException(Throwable e) {
			this._exception = e;
			this._failed = true;
			this.notifyEvent();
		}

		private void notifyEvent() {
			synchronized (this._event) {
				this._event.notifyAll();
			}
		}
	}

	public static class TestDownloadClassDisabledJob extends QuartzJobBean {
		@Override
		protected void executeInternal(JobExecutionContext context)
				throws JobExecutionException {
			try {
				ConfigurationService cs = mock(ConfigurationService.class);
				when(
						cs.getBooleanProperty(NewDownloadDataFilesJob.class,
								AbstractJob.JOB_CLASS_ENABLED_KEY, false))
						.thenReturn(true);
				DataFilesDownloadManager dfdm = mock(DataFilesDownloadManager.class);
				NewDownloadDataFilesJob job = new NewDownloadDataFilesJob();
				when(dfdm.isDownloadingFiles())
				.thenReturn(false);
				job.setDataFilesDownloadManager(dfdm);
				job.setConfigurationService(cs);

				job.executeInternal(context);
				Object result = context.getResult();

				if (result instanceof Exception) {
					throw ((Exception) result);
				}

				assertEquals("Download status should be CLASS_DISABLED", NewDownloadDataFilesJob.STATUS_CLASS_DISABLED, context.getJobDetail().getJobDataMap().get(NewDownloadDataFilesJob.STATUS_KEY));
				
				verify(dfdm, never()).downloadDataFiles();
			} catch (Throwable e) {
				TestJobListener jobListener = (TestJobListener) context
						.getJobDetail().getJobDataMap().get(TestJobListener);
				jobListener.handleException(e);
			} finally {

			}
		}

	};

	@Test
	public void downloadClassDisabled() {
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			Object event = new Object();
			TestJobListener anythingJobListener = new TestJobListener(
					event);
			sched.addJobListener(anythingJobListener);
			sched.start();
			JobDetail jd = new JobDetail("TestDownloadClassDisabledJob",
					Scheduler.DEFAULT_GROUP, TestDownloadClassDisabledJob.class);
			jd.addJobListener(TestProviderName);
			jd.getJobDataMap().put(TestJobListener, anythingJobListener);
			SimpleTrigger st = new SimpleTrigger("TestDownloadClassDisabledJobTigger",
					Scheduler.DEFAULT_GROUP, new Date(), null,
					SimpleTrigger.REPEAT_INDEFINITELY, 60L * 1000L);
			sched.scheduleJob(jd, st);

			try {
				synchronized (event) {
					event.wait(2 * 60 * 1000L);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!anythingJobListener.successful()) {
				if (anythingJobListener.getException() != null) {
					anythingJobListener.getException().printStackTrace();
					fail(anythingJobListener.getException().getMessage());
				} else {
					fail("DownloadDataFilesJob failed for unknown reason!");
				}
			}
			assertEquals(
					"DownloadDataFilesJob failed should have not process!",
					true, anythingJobListener.successful());

            sched.deleteJob(jd.getName(), st.getName());
            sched.removeJobListener(anythingJobListener.getName());
        } catch (JobExecutionException e) {
			e.printStackTrace();
			fail("DownloadDataFilesJob failed to execution!");
		} catch (SchedulerException e) {
			e.printStackTrace();
			fail("DownloadDataFilesJob failed to execution!");
		}
	}


	public static class TestDownloadJobDisabledJob extends QuartzJobBean {
		@Override
		protected void executeInternal(JobExecutionContext context)
				throws JobExecutionException {
			try {
				ConfigurationService cs = mock(ConfigurationService.class);
                when(
						cs.getBooleanProperty(NewDownloadDataFilesJob.class,
								AbstractJob.JOB_CLASS_ENABLED_KEY, true))
						.thenReturn(true);
				when(
						cs.getBooleanProperty(NewDownloadDataFilesJob.class,
								TestProviderName + NewDownloadDataFilesJob.JOB_ENABLED_KEY, true))
						.thenReturn(true);
				DataFilesDownloadManager dfdm = mock(DataFilesDownloadManager.class);
				NewDownloadDataFilesJob job = new NewDownloadDataFilesJob();
				when(dfdm.isDownloadingFiles())
				.thenReturn(false);
				job.setDataFilesDownloadManager(dfdm);
				job.setConfigurationService(cs);

				job.executeInternal(context);
				Object result = context.getResult();

				if (result instanceof Exception) {
					throw ((Exception) result);
				}

				assertEquals("Download status should be DISABLED", NewDownloadDataFilesJob.STATUS_DISABLED, context.getJobDetail().getJobDataMap().get(NewDownloadDataFilesJob.STATUS_KEY));
				
				verify(dfdm, never()).downloadDataFiles();
			} catch (Throwable e) {
				TestJobListener jobListener = (TestJobListener) context
						.getJobDetail().getJobDataMap().get(TestJobListener);
				jobListener.handleException(e);
			} finally {

			}
		}

	};

	@Test
	public void downloadJobDisabled() {
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			Object event = new Object();
			TestJobListener anythingJobListener = new TestJobListener(
					event);
			sched.addJobListener(anythingJobListener);
			sched.start();
			JobDetail jd = new JobDetail("TestDownloadJobDisabledJob",
					Scheduler.DEFAULT_GROUP, TestDownloadJobDisabledJob.class);
			jd.addJobListener(TestProviderName);
			jd.getJobDataMap().put(TestJobListener, anythingJobListener);
			SimpleTrigger st = new SimpleTrigger("TestDownloadJobDisabledJobTigger",
					Scheduler.DEFAULT_GROUP, new Date(), null,
					SimpleTrigger.REPEAT_INDEFINITELY, 60L * 1000L);
			sched.scheduleJob(jd, st);

			try {
				synchronized (event) {
					event.wait(2 * 60 * 1000L);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!anythingJobListener.successful()) {
				if (anythingJobListener.getException() != null) {
					anythingJobListener.getException().printStackTrace();
					fail(anythingJobListener.getException().getMessage());
				} else {
					fail("DownloadDataFilesJob failed for unknown reason!");
				}
			}
			assertEquals(
					"DownloadDataFilesJob failed, should have already downloaded file!",
					true, anythingJobListener.successful());

            sched.deleteJob(jd.getName(), st.getName());
            sched.removeJobListener(anythingJobListener.getName());
        } catch (JobExecutionException e) {
			e.printStackTrace();
			fail("DownloadDataFilesJob failed to execution!");
		} catch (SchedulerException e) {
			e.printStackTrace();
			fail("DownloadDataFilesJob failed to execution!");
		}
	}
	
	public static class TestDownloadAlreadyDownloadedJob extends QuartzJobBean {
		@Override
		protected void executeInternal(JobExecutionContext context)
				throws JobExecutionException {
			try {
				ConfigurationService cs = mock(ConfigurationService.class);
				when(
						cs.getBooleanProperty(NewDownloadDataFilesJob.class,
								AbstractJob.JOB_CLASS_ENABLED_KEY, true))
						.thenReturn(true);
				when(
						cs.getBooleanProperty(NewDownloadDataFilesJob.class,
								TestProviderName + NewDownloadDataFilesJob.JOB_ENABLED_KEY, true))
						.thenReturn(true);
				DataFilesDownloadManager dfdm = mock(DataFilesDownloadManager.class);
                when(dfdm.isDownloadingFiles()).thenReturn(true);
                when(dfdm.getProviderName()).thenReturn(TestProviderName);
                NewDownloadDataFilesJob job = new NewDownloadDataFilesJob();
				job.setDataFilesDownloadManager(dfdm);
				job.setConfigurationService(cs);

				job.executeInternal(context);
				Object result = context.getResult();

				if (result instanceof Exception) {
					throw ((Exception) result);
				}

				assertEquals("Download status should be ALREADY_DOWNLOADED", NewDownloadDataFilesJob.STATUS_ALREADY_DOWNLOADED, context.getJobDetail().getJobDataMap().get(NewDownloadDataFilesJob.STATUS_KEY));
				
				verify(dfdm, never()).downloadDataFiles();
			} catch (Throwable e) {
				TestJobListener jobListener = (TestJobListener) context
						.getJobDetail().getJobDataMap().get(TestJobListener);
				jobListener.handleException(e);
			} finally {

			}
		}

	};

	@Test
	public void downloadAlreadyDownloaded() {
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			Object event = new Object();
			TestJobListener anythingJobListener = new TestJobListener(
					event);
			sched.addJobListener(anythingJobListener);
			sched.start();
			JobDetail jd = new JobDetail("TestDownloadAlreadyDownloadedJob",
					Scheduler.DEFAULT_GROUP, TestDownloadAlreadyDownloadedJob.class);
			jd.addJobListener(TestProviderName);
			jd.getJobDataMap().put(TestJobListener, anythingJobListener);
			SimpleTrigger st = new SimpleTrigger("TestDownloadAlreadyDownloadedJobTigger",
					Scheduler.DEFAULT_GROUP, new Date(), null,
					SimpleTrigger.REPEAT_INDEFINITELY, 60L * 1000L);
			sched.scheduleJob(jd, st);

			try {
				synchronized (event) {
					event.wait(2 * 60 * 1000L);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!anythingJobListener.successful()) {
				if (anythingJobListener.getException() != null) {
					anythingJobListener.getException().printStackTrace();
					fail(anythingJobListener.getException().getMessage());
				} else {
					fail("DownloadDataFilesJob failed for unknown reason!");
				}
			}
			assertEquals(
					"DownloadDataFilesJob failed, should have already downloaded file!",
					true, anythingJobListener.successful());

            sched.deleteJob(jd.getName(), st.getName());
            sched.removeJobListener(anythingJobListener.getName());
        } catch (JobExecutionException e) {
			e.printStackTrace();
			fail("DownloadDataFilesJob failed to execution!");
		} catch (SchedulerException e) {
			e.printStackTrace();
			fail("DownloadDataFilesJob failed to execution!");
		}
	}
	
	public static class TestDownloadExistsJob extends QuartzJobBean {
		@Override
		protected void executeInternal(JobExecutionContext context)
				throws JobExecutionException {
			try {
				ConfigurationService cs = mock(ConfigurationService.class);
				when(
						cs.getBooleanProperty(NewDownloadDataFilesJob.class,
								AbstractJob.JOB_CLASS_ENABLED_KEY, true))
						.thenReturn(true);
				when(
						cs.getBooleanProperty(NewDownloadDataFilesJob.class,
								TestProviderName + NewDownloadDataFilesJob.JOB_ENABLED_KEY, true))
						.thenReturn(true);
				DataFilesDownloadManager dfdm = mock(DataFilesDownloadManager.class);
				when(dfdm.isDownloadingFiles())
				.thenReturn(false);
                when(dfdm.getProviderName()).thenReturn(TestProviderName);
                NewDownloadDataFilesJob job = new NewDownloadDataFilesJob();
                job.setDataFilesDownloadManager(dfdm);
				job.setConfigurationService(cs);

				job.executeInternal(context);
				Object result = context.getResult();

				if (result instanceof Exception) {
					throw ((Exception) result);
				}

				String status = (String)context.getJobDetail().getJobDataMap().get(NewDownloadDataFilesJob.STATUS_KEY);
				assertEquals("Download status should be COMPLETED", NewDownloadDataFilesJob.STATUS_COMPLETED, status);
				
				verify(dfdm).downloadDataFiles();
            } catch (Throwable e) {
				TestJobListener jobListener = (TestJobListener) context
						.getJobDetail().getJobDataMap().get(TestJobListener);
				jobListener.handleException(e);
			} finally {

			}
		}

	};

	@Test
	public void downloadExists() {
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			Object event = new Object();
			TestJobListener anythingJobListener = new TestJobListener(
					event);
			sched.addJobListener(anythingJobListener);
			sched.start();
			JobDetail jd = new JobDetail("TestDownloadExistsJobJob",
					Scheduler.DEFAULT_GROUP, TestDownloadExistsJob.class);
			jd.addJobListener(TestProviderName);
			jd.getJobDataMap().put(TestJobListener, anythingJobListener);
			jd.getJobDataMap().put(NewDownloadDataFilesJob.STATUS_KEY, "UNINTIALIZED");
			SimpleTrigger st = new SimpleTrigger("TestDownloadExistsJobTigger",
					Scheduler.DEFAULT_GROUP);
			sched.scheduleJob(jd, st);

			try {
				synchronized (event) {
					event.wait(2 * 60 * 1000L);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!anythingJobListener.successful()) {
				if (anythingJobListener.getException() != null) {
					anythingJobListener.getException().printStackTrace();
					fail(anythingJobListener.getException().getMessage());
				} else {
					fail("DownloadDataFilesJob failed for unknown reason!");
				}
			}
			assertEquals(
					"DownloadDataFilesJob failed to process existing feed!",
					true, anythingJobListener.successful());

            sched.deleteJob(jd.getName(), st.getName());
            sched.removeJobListener(anythingJobListener.getName());
        } catch (JobExecutionException e) {
			e.printStackTrace();
			fail("DownloadDataFilesJob failed to execution!");
		} catch (SchedulerException e) {
			e.printStackTrace();
			fail("DownloadDataFilesJob failed to execution!");
		}
	}

}
