package com.activity;
import com.job.Job;


import org.apache.log4j.Logger;


public class SchedulerActivity1 implements Job{
	private static Logger log = Logger.getLogger(SchedulerActivity1.class);
	
	public String getName() {
		return "SchedulerActivity1";
	}

	public void perform()     {
		log.debug("Scheduler Activity 1");
		System.out.println("Perform Scheduler Activity 1");
	}

	public static void main(String args[])
	{
		SchedulerActivity1 m = new SchedulerActivity1 ();
		m.perform();
	}
}