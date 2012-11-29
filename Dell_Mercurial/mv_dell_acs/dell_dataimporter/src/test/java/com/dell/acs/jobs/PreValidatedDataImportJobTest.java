package com.dell.acs.jobs;

import com.dell.acs.managers.DataFilesDownloadManager;
import com.dell.acs.managers.DataFilesDownloadManagerBase;
import com.dell.acs.managers.PreValidatedDataImportManager;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.jobs.AbstractJob;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class PreValidatedDataImportJobTest {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(PreValidatedDataImportJobTest.class);
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

	public static class TestPreValidatedDataImportClassDisabledJob extends QuartzJobBean {
		@Override
		protected void executeInternal(JobExecutionContext context)
				throws JobExecutionException {
			try {
				ConfigurationService cs = mock(ConfigurationService.class);
				when(
						cs.getBooleanProperty(PreValidatedDataImportJob.class,
								AbstractJob.JOB_CLASS_ENABLED_KEY, false))
						.thenReturn(true);
				PreValidatedDataImportManager pvdim = mock(PreValidatedDataImportManager.class);
				PreValidatedDataImportJob job = new PreValidatedDataImportJob();
				job.setPreValidatedDataImportManager(pvdim);
				job.setConfigurationService(cs);

				job.executeInternal(context);
				Object result = context.getResult();

				if (result instanceof Exception) {
					throw ((Exception) result);
				}

				assertEquals("Download status should be CLASS_DISABLED", NewDownloadDataFilesJob.STATUS_CLASS_DISABLED, context.getJobDetail().getJobDataMap().get(NewDownloadDataFilesJob.STATUS_KEY));
				
				verify(pvdim, never()).processDataFile((DataFile)anyObject());
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
			JobDetail jd = new JobDetail("TestFeedPreprocessorClassDisabledJob",
					Scheduler.DEFAULT_GROUP, TestPreValidatedDataImportClassDisabledJob.class);
			jd.addJobListener(TestProviderName);
			jd.getJobDataMap().put(TestJobListener, anythingJobListener);
			SimpleTrigger st = new SimpleTrigger("TestPreValidatedDataImportClassDisabledJobTigger",
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
					fail("PreValidatedDataImportrJob failed for unknown reason!");
				}
			}
			assertEquals(
					"PreValidatedDataImportJob failed should have not process!",
					true, anythingJobListener.successful());

            sched.deleteJob(jd.getName(), st.getName());
            sched.removeJobListener(anythingJobListener.getName());
        } catch (JobExecutionException e) {
			e.printStackTrace();
			fail("PreValidatedDataImportJob failed to execution!");
		} catch (SchedulerException e) {
			e.printStackTrace();
			fail("PreValidatedDataImportJob failed to execution!");
		}
	}

	public static class TestNormalJob extends QuartzJobBean {
		@Override
		protected void executeInternal(JobExecutionContext context)
				throws JobExecutionException {
			try {
				DataFile dataFile1 = mock(DataFile.class);
				String srcFile = "test.cvs";
				when(dataFile1.getSrcFile()).thenReturn(srcFile);
				
				Set<String> emptyRetailerSiteNames = new HashSet<String>();
				ConfigurationService cs = mock(ConfigurationService.class);
				when(
						cs.getBooleanProperty(PreValidatedDataImportJob.class,
								AbstractJob.JOB_CLASS_ENABLED_KEY, true))
						.thenReturn(true);
				when(cs.getProperty(
								DataFilesDownloadManager.class,
								DataFilesDownloadManagerBase.SITE_NAMES_KEY))
						.thenReturn("");
				RetailerSiteRepository rsr = mock(RetailerSiteRepository.class);
				when(rsr.getByNameIds(emptyRetailerSiteNames)).thenReturn(new ArrayList<Long>());
				PreValidatedDataImportManager pvdim = mock(PreValidatedDataImportManager.class);
				when(pvdim.getLatestProductDataFile(new ArrayList<Long>())).thenReturn(dataFile1).thenReturn(null);
				PreValidatedDataImportJob job = new PreValidatedDataImportJob();
				job.setPreValidatedDataImportManager(pvdim);
				job.setConfigurationService(cs);
				job.setRetailerSiteRepository(rsr);

				job.executeInternal(context);
				Object result = context.getResult();

				if (result instanceof Exception) {
					throw ((Exception) result);
				}

				assertEquals("Download status should be COMPLETED", NewDownloadDataFilesJob.STATUS_COMPLETED, context.getJobDetail().getJobDataMap().get(NewDownloadDataFilesJob.STATUS_KEY));
				
				verify(pvdim).processDataFile(dataFile1);
			} catch (Throwable e) {
				TestJobListener jobListener = (TestJobListener) context
						.getJobDetail().getJobDataMap().get(TestJobListener);
				jobListener.handleException(e);
			} finally {

			}
		}

	};

	@Test
	public void normalJob() {
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			Object event = new Object();
			TestJobListener anythingJobListener = new TestJobListener(
					event);
			sched.addJobListener(anythingJobListener);
			sched.start();
			JobDetail jd = new JobDetail("TestNOrmalJob",
					Scheduler.DEFAULT_GROUP, TestNormalJob.class);
			jd.addJobListener(TestProviderName);
			jd.getJobDataMap().put(TestJobListener, anythingJobListener);
			jd.getJobDataMap().put(NewDownloadDataFilesJob.STATUS_KEY, "UNINTIALIZED");
			SimpleTrigger st = new SimpleTrigger("TestNormalJobTigger",
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
					fail("PreValidatedDataImportJob failed for unknown reason!");
				}
			}
			assertEquals(
					"PreValidatedDataImportJob failed to process existing feed!",
					true, anythingJobListener.successful());

            sched.deleteJob(jd.getName(), st.getName());
            sched.removeJobListener(anythingJobListener.getName());
        } catch (JobExecutionException e) {
			e.printStackTrace();
			fail("PreValidatedDataImportJob failed to execution!");
		} catch (SchedulerException e) {
			e.printStackTrace();
			fail("PreValidatedDataImportJob failed to execution!");
		}
	}

}
