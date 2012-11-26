/*
 * Created on Apr 23, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.eag.epharma.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.rdta.eag.epharma.job.JobManager;
import com.rdta.eag.epharma.util.Log4jSetup;
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
		System.setProperty( "log.home", "C:/RainingData/scheduler/log" );
		        System.out.println("---111111111111 ---");  
		log4jSetup();
				
		        System.out.println("---222222222222---");  
	   File file = new File("C:/RainingData/scheduler/config/job.xml");
		 
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
        	Log4jSetup.initLog4J("C:/RainigData/scheduler/config/log4j.xml","abc");
	}
}
