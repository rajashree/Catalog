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
package com.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.job.JobEntryConfig.TaskInfo;



/**
 *  Mantian the reference of the all system job scheduler
 * 
 * @author Srinivasa Raju Vegeraju
 */

public class JobManager {
		

	private static Logger log = Logger.getLogger(JobManager.class);
	private static JobManager jobManager =null;
	
	private Map schedulerMap = new HashMap();  
	
	
	/**
	 * Constructs JobManager object
	 *
	 */
	private JobManager() {
	}
	
	
	/**
	 * Returns JobManager object
	 * 
	 * @return
	 */
	public static synchronized JobManager getJobManager() {
		if(jobManager == null) {
			jobManager = new JobManager();
		}
		return jobManager;
	}
	
	/**
	 * Starts configured jobs in Job.xml file
	 *
	 */
	public void startJobs(Document jobDocuemnt) {
		JobConfig config = new JobConfig(jobDocuemnt);
		List list = config.getJobsList();
		if( list != null ) {
			for(int i=0; i<list.size(); i++ ) {
				JobEntryConfig jobEntry = (JobEntryConfig) list.get(i);
				log.info(" Scheduling Tasks for Job Type: " + jobEntry.getType() ); 
				
				try {
					JobScheduler scheduler = new JobScheduler(true);
					//get tasks from the Job config
					List taskList = jobEntry.getTaskList();
					for(int j=0; j< taskList.size(); j++) {
						TaskInfo taskInfo = (TaskInfo)taskList.get(j);
						long delay = taskInfo.getDelayDuration()*60*1000;
						long sleep = taskInfo.getSleepDuration()*60*1000;
						scheduler.schedule(JobFactory.getJob(taskInfo),delay,sleep);
						
						log.info(" Task Name : " + taskInfo.getName() + " Schedule with delay : "+ delay + "ms  Sleep Duration:" + sleep + "ms");
					}
					
					schedulerMap.put(jobEntry.getType(),scheduler);
				} catch(Exception e) {
					log.error(" Not able to Schedule Job Type : " + jobEntry.getType());
					log.error(e);
				}
			}//end of for loop
		}//end of if loop
	}
	
	
	
	/**
	 * Cancel all system jobs
	 */
	public void cancelSystemJobs() {
		//invoke cancel on job schedular
	}
	
	
	/**
	 * Cancel System jobs
	 * 
	 * @param jobType
	 */
	public void cancelSystemJob(String jobType) {
		//invoke cancel on specified job schedular
	}

		
}