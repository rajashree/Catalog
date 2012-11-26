/***********************************************************************
* Raining Data Corp.
*
* Copyright (c) Raining Data Corp. All Rights Reserved.
*
* This software is confidential and proprietary information belonging to
* Raining Data Corp. It is the property of Raining Data Corp. and is protected
* under the Copyright Laws of the United States of America. No part of this
* software may be copied or used in any form without the prior
* written permission of Raining Data Corp.
*
************************************************************************/
package com.rdta.eag.epharma.job;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * JobScheduler is used to schedule jobs.
 *
 * Srinivasa Raju Vegeraju
 */
public class JobScheduler {

	/** The <code>log</code> is used for Log storage. */
	private static final Logger log = Logger.getLogger(JobScheduler.class);

     // scheduler
    private Timer scheduler = null;
	
	private class TaskScheduler extends TimerTask {
        // state
        private Job job = null;

        /**
         * Class constructor specifying initial values.
         * @param  job  the <code>Job</code> to execute.
         */
        TaskScheduler(Job job) {
			if(job == null)
				throw new IllegalArgumentException("Job object should not be null"); 
			this.job = job;
        }

        /**
         * Implementation of the <code>Runnable</code> interface
         */
        public void run() {
             try {
				log.debug("run:scheduled job " + job.getName());
                job.perform();
            } catch (Throwable t) {
                log.error("run:job  " + job.getName() + " Exception: ",t);
            }
        }
    }
		
    /**
     * Class Constructor
     * @param isDaemon true if the scheduler thread should run as a daemon.
     */
    public JobScheduler(boolean isDaemon) {
        // create a scheduler
        scheduler = new Timer(isDaemon);
    }

    /**
     * Schedules the given job.
     * @param  job   the <code>Job</code> to execute.
     * @param  delay the delay in milliseconds before job is to be executed
     * @exception Exception if there is an error while scheduling the job.
     */
    public void schedule(Job job, long delay) throws Exception {

        // create the task scheduler
        TaskScheduler taskScheduler = new TaskScheduler(job);
        // schedule the task scheduler to run
        scheduler.schedule(taskScheduler,delay);
    }

    /**
     * Schedules the given job.
     * @param  job   the <code>Job</code> to execute.
     * @param  when  the time at which job is to be executed.
     * @exception Exception if there is an error while scheduling the job.
     */
    public void schedule(Job job, Date when) throws Exception {

        // create the task scheduler
        TaskScheduler taskScheduler = new TaskScheduler(job);
        // schedule the task scheduler to run
        scheduler.schedule(taskScheduler,when);
    }
	
    /**
     * Schedules the given job.
     * @param  job   the <code>Job</code> to execute.
     * @param  delay the delay in milliseconds before job is to be executed
     * @param  period in milliseconds between successive task executions. 
     * @exception Exception if there is an error while scheduling the job.
     */
    public void schedule(Job job, long delay, long period) throws Exception {

        // create the task scheduler
        TaskScheduler taskScheduler = new TaskScheduler(job);
        // schedule the task scheduler to run
        scheduler.schedule(taskScheduler,delay,period);
    }
	

    /**
     * Terminates this scheduler, discarding any currently scheduled tasks.
     */
    public void cancel() {
        scheduler.cancel();
    }
}