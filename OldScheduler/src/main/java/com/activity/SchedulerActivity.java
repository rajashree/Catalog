package com.activity;
import com.job.Job;


import org.apache.log4j.Logger;


public class SchedulerActivity implements Job{
	private static Logger log = Logger.getLogger(SchedulerActivity.class);
	
	public String getName() {
		return "SchedulerActivity";
	}

	public void perform()     {
		log.debug("Scheduler Activity");
		System.out.println("Perform Scheduler Activity");
	}

	public static void main(String args[])
	{
		SchedulerActivity m = new SchedulerActivity ();
		m.perform();
	}
}