package com.dell.acs.jobs;

import com.dell.acs.DellTestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.SimpleTriggerBean;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract quartz job test.
 * allows you to execute a single job test with the entire context without triggering other jobs.
 * quiet useful when you are debugging and testing a single job.
 *
 * @author Navin Raj Kumar G.S.
 */
@RunWith(Parameterized.class)
public abstract class QuartzJobTest extends DellTestCase {

    protected static final Logger logger = LoggerFactory.getLogger(QuartzJobTest.class);

    protected String jobTriggerBeanName;

    protected QuartzJobTest(final String jobTriggerBeanName) {
        this.jobTriggerBeanName = jobTriggerBeanName;
    }

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext();
        applicationContext.setConfigLocations(
                new String[]{"/spring/test/applicationContext-test.xml", "/spring/applicationContext-jobs.xml"});
        applicationContext.getEnvironment().setActiveProfiles("default", "persistence", "test");
        applicationContext.refresh();
        setUpManagers();
    }

    @Test
    public void scheduleJob() throws SchedulerException {
        executeJob(this.jobTriggerBeanName);
    }

    protected void executeJob(String jobTriggerBeanName) throws SchedulerException {
        final SimpleTriggerBean trigger = applicationContext.getBean(jobTriggerBeanName, SimpleTriggerBean.class);
        Scheduler schedulerFactory = applicationContext.getBean("schedulerFactoryBean", Scheduler.class);
        trigger.setNextFireTime(new Date());
        trigger.setStartDelay(0);
        trigger.setRepeatCount(0);
        trigger.setRepeatInterval(0);
        logger.info("Job execution started. class:=" + trigger.getJobDetail().getJobClass());
        schedulerFactory.start();
        schedulerFactory.scheduleJob(trigger.getJobDetail(), trigger);

        final Thread caller = Thread.currentThread();
        final AtomicBoolean executed = new AtomicBoolean(false);
        try {
            schedulerFactory.addGlobalJobListener(new JobListener() {
                @Override
                public String getName() {
                    return "shutdownJUnitTestCase";
                }

                @Override
                public void jobToBeExecuted(final JobExecutionContext jobExecutionContext) {
                }

                @Override
                public void jobExecutionVetoed(final JobExecutionContext jobExecutionContext) {
                }

                @Override
                public void jobWasExecuted(final JobExecutionContext jobExecutionContext,
                        final JobExecutionException e) {
                    // interrupt only if we are looking for the current trigger.
                    if (jobExecutionContext.getTrigger().equals(trigger)) {
                        executed.compareAndSet(false, true);
                        logger.info("Job execution complete. class:=" + trigger.getJobDetail().getJobClass());
                        caller.interrupt();
                    }
                }
            });
            if (!executed.get()) {
                Thread.sleep(Long.MAX_VALUE);
            }
        } catch (InterruptedException e) {
            // ignore.
        }

    }

}
