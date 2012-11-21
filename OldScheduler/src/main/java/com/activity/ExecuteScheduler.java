package com.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.job.JobManager;
import com.util.Log4jSetup;
/**
 * @author ajoshi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExecuteScheduler  {

	private static Logger log = Logger.getLogger(ExecuteScheduler.class);

	// document builder factory
	private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	static {
		dbf.setNamespaceAware(false);
		dbf.setValidating(false);
		dbf.setIgnoringComments(true);
	}



	public static void main(String[] args)throws Exception {
		System.setProperty( "log.home", "C:/Raje_Work/Scheduler/log" );
		log4jSetup();
		File file = new File("C:/Raje_Work/Scheduler/config/job.xml");

		InputStream inputStream = null;
		Document jobDocuemnt =  null;

		JobManager jobManager = JobManager.getJobManager();

		try {
			inputStream = new FileInputStream(file);
			DocumentBuilder db = dbf.newDocumentBuilder();
			jobDocuemnt = db.parse(inputStream);
			//start system configured jobs
			jobManager.startJobs(jobDocuemnt);

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			// close the input stream
			try {
				inputStream.close();
			} catch (Throwable te) {

			}
		}
		Thread.currentThread().join();
		System.out.println("---exiting the system ---");  
	}

	private static void log4jSetup()
	{
		System.setProperty( "log.config", "C:/Raje_Work/Scheduler/config/log4j.xml" );
		Log4jSetup.initLog4J(System.getProperty("log.config"),"abc");
	}
}
